package i2f.extension.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.ByteString;
import i2f.convert.obj.ObjectConvertor;
import i2f.extension.canal.meta.CanalMeta;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/11/23 15:43
 */
@Data
@NoArgsConstructor
public class CanalClient implements Runnable, Closeable {
    protected final AtomicInteger threadCount = new AtomicInteger(0);
    protected CanalConnector connector;
    protected String pattern = "*";
    protected int batchSize = 300;
    protected long idleMillSeconds = 1000;
    protected final AtomicBoolean running = new AtomicBoolean(false);

    public CanalClient(CanalConnector connector) {
        this.connector = connector;
    }

    public CanalClient(CanalConnector connector, String pattern) {
        this.connector = connector;
        this.pattern = pattern;
    }

    public CanalClient(CanalMeta meta) {
        this.connector = getConnector(meta);
    }

    public CanalClient(CanalMeta meta, String pattern) {
        this.connector = getConnector(meta);
        this.pattern = pattern;
    }

    public static CanalConnector getConnector(CanalMeta meta) {
        return CanalConnectors.newSingleConnector(
                new InetSocketAddress(meta.getHost(), meta.getPort() > 0 ? meta.getPort() : 11111),
                (meta.getDestination() == null || meta.getDestination().isEmpty()) ? "example" : meta.getDestination(),
                (meta.getUsername() == null || meta.getUsername().isEmpty()) ? "" : meta.getUsername(),
                (meta.getPassword() == null || meta.getPassword().isEmpty()) ? "" : meta.getPassword()
        );
    }

    public CanalClient connector(CanalConnector connector) {
        this.connector = connector;
        return this;
    }

    public CanalClient pattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public CanalClient batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public CanalClient idleMillSeconds(long idleMillSeconds) {
        this.idleMillSeconds = idleMillSeconds;
        return this;
    }

    public void connect() {
        connector.connect();
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.setName("canal-client-" + threadCount.incrementAndGet());
        thread.start();
    }

    @Override
    public void run() {
        try {
            subscribe();
        } finally {
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void subscribe() {
        running.set(true);
        connect();
        connector.subscribe(pattern);
        System.out.println(String.format("canal subscribe [%s] with batch size %d and idle %d mill-seconds ...", pattern, batchSize, idleMillSeconds));
        while (running.get()) {
            Message message = connector.get(batchSize);
            List<CanalEntry.Entry> entries = message.getEntries();
            if (entries.isEmpty()) {
                try {
                    Thread.sleep(idleMillSeconds);
                } catch (InterruptedException e) {

                }
                continue;
            }

            for (CanalEntry.Entry entry : entries) {
                CanalEntry.Header header = entry.getHeader();
                String tableName = header.getTableName();
                CanalEntry.EntryType entryType = entry.getEntryType();

                if (CanalEntry.EntryType.ROWDATA != entryType) {
                    continue;
                }

                ByteString storeValue = entry.getStoreValue();
                try {
                    CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(storeValue);
                    CanalEntry.EventType eventType = rowChange.getEventType();

                    if (eventType == CanalEntry.EventType.QUERY || rowChange.getIsDdl()) {
                        continue;
                    }

                    List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
                    if (rowDatasList.isEmpty()) {
                        continue;
                    }

                    for (CanalEntry.RowData rowData : rowDatasList) {
                        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
                        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
                        onEvent(afterColumnsList, eventType, tableName, beforeColumnsList, entry);
                    }
                } catch (Exception e) {

                }

            }
        }
    }

    public void onEvent(List<CanalEntry.Column> afterColumnsList, CanalEntry.EventType eventType, String tableName, List<CanalEntry.Column> beforeColumnsList, CanalEntry.Entry entry) {
        if (CanalEntry.EventType.INSERT == eventType) {
            onInsertEvent(afterColumnsList, tableName, entry);
        } else if (CanalEntry.EventType.DELETE == eventType) {
            onDeleteEvent(beforeColumnsList, tableName, entry);
        } else if (CanalEntry.EventType.UPDATE == eventType) {
            onUpdateEvent(afterColumnsList, beforeColumnsList, tableName, entry);
        }
    }

    public Map<String, Object> parseAsMap(List<CanalEntry.Column> columnList) {
        return parseAsMap(columnList, new LinkedHashMap<>(), null, null);
    }

    public <T extends Map<String, Object>> T parseAsMap(List<CanalEntry.Column> columnList, T ret) {
        return parseAsMap(columnList, ret, null, null);
    }

    public <T extends Map<String, Object>> T parseAsMap(List<CanalEntry.Column> columnList, T ret, Function<String, String> nameWrapper, Predicate<CanalEntry.Column> filter) {
        if (columnList == null) {
            return ret;
        }
        AtomicReference<Object> out = new AtomicReference<>();
        for (CanalEntry.Column column : columnList) {
            if (filter != null && !filter.test(column)) {
                continue;
            }
            String name = column.getName();
            if (nameWrapper != null) {
                name = nameWrapper.apply(name);
            }
            out.set(null);
            Object value = null;
            if (preParseColumnValue(column, out)) {
                value = out.get();
            } else {
                value = parseColumnValue(column);
            }
            ret.put(name, value);
        }
        return ret;
    }

    public boolean preParseColumnValue(CanalEntry.Column column, AtomicReference<Object> out) {
        return false;
    }

    public Object parseColumnValue(CanalEntry.Column column) {
        if (column.getIsNull()) {
            return null;
        }
        int sqlType = column.getSqlType();
        if (Types.NULL == sqlType) {
            return null;
        }
        if (Types.BINARY == sqlType
                || Types.VARBINARY == sqlType
                || Types.LONGVARBINARY == sqlType
                || Types.BLOB == sqlType
                || Types.CLOB == sqlType
                || Types.NCLOB == sqlType) {
            return column.getValueBytes().toByteArray();
        }
        String value = column.getValue();
        if (Types.OTHER == sqlType) {
            return value;
        }
        if (Types.VARCHAR == sqlType
                || Types.NVARCHAR == sqlType
                || Types.NCHAR == sqlType
                || Types.LONGVARCHAR == sqlType
                || Types.LONGNVARCHAR == sqlType
                || Types.CHAR == sqlType
                || Types.SQLXML == sqlType) {
            return value;
        }
        if (Types.BIGINT == sqlType) {
            return Long.parseLong(value);
        }
        if (Types.INTEGER == sqlType
                || Types.SMALLINT == sqlType
                || Types.TINYINT == sqlType) {
            return Integer.parseInt(value);
        }
        if (Types.BIT == sqlType) {
            return Integer.parseInt(value);
        }
        if (Types.FLOAT == sqlType) {
            return Float.parseFloat(value);
        }
        if (Types.REAL == sqlType
                || Types.DOUBLE == sqlType) {
            return Double.parseDouble(value);
        }
        if (Types.NUMERIC == sqlType
                || Types.DECIMAL == sqlType) {
            return new BigDecimal(value);
        }
        if (Types.DATE == sqlType
                || Types.TIME == sqlType
                || Types.TIMESTAMP == sqlType
                || Types.TIME_WITH_TIMEZONE == sqlType
                || Types.TIMESTAMP_WITH_TIMEZONE == sqlType) {
            return ObjectConvertor.tryParseDate(value);
        }
        if (Types.BOOLEAN == sqlType) {
            return ObjectConvertor.tryConvertAsType(value, Boolean.class);
        }


        return value;
    }

    public void onInsertEvent(List<CanalEntry.Column> afterColumnsList, String tableName, CanalEntry.Entry entry) {
        Map<String, Object> after = parseAsMap(afterColumnsList);
        onInsertEventMap(after, tableName, entry);
    }

    public void onDeleteEvent(List<CanalEntry.Column> beforeColumnsList, String tableName, CanalEntry.Entry entry) {
        Map<String, Object> before = parseAsMap(beforeColumnsList);
        onDeleteEventMap(before, tableName, entry);
    }

    public void onUpdateEvent(List<CanalEntry.Column> afterColumnsList, List<CanalEntry.Column> beforeColumnsList, String tableName, CanalEntry.Entry entry) {
        Map<String, Object> after = parseAsMap(afterColumnsList);
        Map<String, Object> before = parseAsMap(beforeColumnsList);
        onUpdateEventMap(after, before, tableName, entry);
    }

    public void onInsertEventMap(Map<String, Object> after, String tableName, CanalEntry.Entry entry) {

    }

    public void onDeleteEventMap(Map<String, Object> before, String tableName, CanalEntry.Entry entry) {

    }

    public void onUpdateEventMap(Map<String, Object> after, Map<String, Object> before, String tableName, CanalEntry.Entry entry) {

    }

    @Override
    public void close() throws IOException {
        if (connector != null) {
            connector.disconnect();
        }
    }
}
