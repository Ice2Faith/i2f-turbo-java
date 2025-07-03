package i2f.extension.canal.test;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.ByteString;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/11/23 14:12
 */
public class TestCanal {
    public static void main(String[] args) throws Exception {
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress("192.168.1.100", 11111),
                "example",
                "canal",
                "xxx123456"
        );

        try {

            connector.connect();

            connector.subscribe("test_db.*");

            System.out.println("listening ...");
            while (true) {

                Message message = connector.get(300);
                List<CanalEntry.Entry> entries = message.getEntries();
                if (entries.isEmpty()) {
                    System.out.println("idle ...");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                    continue;
                }

                for (CanalEntry.Entry entry : entries) {
                    CanalEntry.Header header = entry.getHeader();
                    String tableName = header.getTableName();
                    CanalEntry.EntryType entryType = entry.getEntryType();

                    System.out.println("--------------------");
                    System.out.println("tableName:" + tableName);
                    System.out.println("entryType:" + entryType);
                    if (CanalEntry.EntryType.ROWDATA != entryType) {
                        continue;
                    }

                    ByteString storeValue = entry.getStoreValue();
                    CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(storeValue);
                    CanalEntry.EventType eventType = rowChange.getEventType();
                    System.out.println("eventType:" + eventType);

                    if (eventType == CanalEntry.EventType.QUERY || rowChange.getIsDdl()) {
                        System.out.println("sql:\n" + rowChange.getSql());
                    }

                    List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
                    if (rowDatasList.isEmpty()) {
                        continue;
                    }

                    for (CanalEntry.RowData rowData : rowDatasList) {
                        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
                        Map<String, Object> before = new LinkedHashMap<>();
                        for (CanalEntry.Column column : beforeColumnsList) {
                            before.put(column.getName(), column.getValue());
                        }

                        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
                        Map<String, Object> after = new LinkedHashMap<>();
                        for (CanalEntry.Column column : afterColumnsList) {
                            after.put(column.getName(), column.getValue());
                        }

                        System.out.println("before:\n" + before);
                        System.out.println("after:\n" + after);
                    }
                }
            }
        } finally {
            connector.disconnect();
        }

    }
}
