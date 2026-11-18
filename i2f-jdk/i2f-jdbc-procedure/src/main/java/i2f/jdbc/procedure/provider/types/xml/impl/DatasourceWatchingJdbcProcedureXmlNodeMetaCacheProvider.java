package i2f.jdbc.procedure.provider.types.xml.impl;

import i2f.bindsql.BindSql;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.datasource.impl.DirectConnectionDatasource;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.XProc4jConsts;
import i2f.jdbc.procedure.event.XProc4jEventHandler;
import i2f.jdbc.procedure.event.impl.DefaultXProc4jEventHandler;
import i2f.jdbc.procedure.log.JdbcProcedureLogger;
import i2f.jdbc.procedure.log.impl.DefaultJdbcProcedureLogger;
import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.provider.event.ProcedureMetaProviderChangeEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ice2Faith
 * @date 2025/4/16 13:57
 */
@Data
public class DatasourceWatchingJdbcProcedureXmlNodeMetaCacheProvider extends AbstractJdbcProcedureXmlNodeMetaCacheProvider implements Runnable {
    public static final String DEFAULT_TABLE_NAME = "xproc4j_procedure_info";
    protected final DataSource dataSource;
    protected final String tableName;
    protected final AtomicLong updateTimestamp = new AtomicLong(0L);
    protected final ConcurrentHashMap<String, XmlNode> nodeMap = new ConcurrentHashMap<>();
    protected volatile XProc4jEventHandler eventHandler = new DefaultXProc4jEventHandler();
    protected volatile Thread loopThread = null;
    protected volatile JdbcProcedureLogger logger = new DefaultJdbcProcedureLogger();

    public DatasourceWatchingJdbcProcedureXmlNodeMetaCacheProvider(DataSource dataSource) {
        this(dataSource, null);
    }

    public DatasourceWatchingJdbcProcedureXmlNodeMetaCacheProvider(DataSource dataSource, String tableName) {
        if (tableName == null || tableName.isEmpty()) {
            tableName = DEFAULT_TABLE_NAME;
        }
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource cannot be null");
        }
        this.dataSource = dataSource;
        this.tableName = tableName;
    }

    public static void main(String[] args) {
        DataSource ds = new DirectConnectionDatasource();

        DatasourceWatchingJdbcProcedureXmlNodeMetaCacheProvider provider = new DatasourceWatchingJdbcProcedureXmlNodeMetaCacheProvider(ds);
        provider.watching();
        provider.loopHandleEvents();
        System.out.println("ok");
    }


    public void notifyChange(Map<String, XmlNode> nodeMap) {
        ProcedureMetaProviderChangeEvent event = new ProcedureMetaProviderChangeEvent();
        event.setProvider(this);
        if (nodeMap == null) {
            nodeMap = new LinkedHashMap<>(this.nodeMap);
        }
        event.setMetaMap(parseMetaMap(nodeMap));
        if (eventHandler != null) {
            eventHandler.publish(event);
        }
    }

    public synchronized void watching() {
        try {
            loadChanges();
            if (!nodeMap.isEmpty()) {
                notifyChange(nodeMap);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Data
    @NoArgsConstructor
    public static class ProcedureInfo {
        protected String fileName;
        protected String xmlContent;
        protected Date changeTime;
    }

    public Date queryChangeTime() {
        Date lastUpdateDate = null;
        if (updateTimestamp.get() > 0) {
            lastUpdateDate = new Date(updateTimestamp.get() - TimeUnit.SECONDS.toMillis(30));
        }
        return lastUpdateDate;
    }

    public synchronized void loadChanges() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Date lastUpdateDate = queryChangeTime();
            /*language=sql*/
            BindSql bql = BindSql.of("select file_name,xml_content,change_time from " + tableName)
                    .where(q -> q
                            .andGte("change_time", lastUpdateDate)
                    );
            long queryTs = System.currentTimeMillis();
            List<ProcedureInfo> list = JdbcResolver.list(conn, bql, ProcedureInfo.class);
            for (ProcedureInfo item : list) {
                handleXmlNodeFile(item, false);
            }
            if (!nodeMap.isEmpty()) {
                notifyChange(nodeMap);
            }
            updateTimestamp.set(queryTs);
        }

    }

    public String createTableMySqlDdl() {
        /*language=sql*/
        return "create table " + tableName + " (" +
                " file_name varchar(300) unique not null," +
                " xml_content clob," +
                " change_time datetime not null default now()," +
                " " +
                " key idx_ctime(change_time)" +
                ")";
    }


    @Override
    public void run() {
        this.watching();
        this.loopHandleEvents();
    }

    public synchronized void runOnBackground() {
        if (loopThread != null) {
            return;
        }
        loopThread = new Thread(this);
        loopThread.setDaemon(true);
        loopThread.setName("jdbc-proc-watcher");
        loopThread.start();
    }

    // 处理所有事件
    void loopHandleEvents() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                loadChanges();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(15 * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void handleXmlNodeFile(ProcedureInfo info, boolean needNotifyChange) {
        if (info == null) {
            return;
        }
        String name = info.getFileName();
        String suffix = "";
        int idx = name.lastIndexOf(".");
        if (idx >= 0) {
            suffix = name.substring(idx + 1);
        }
        if (!"xml".equalsIgnoreCase(suffix)) {
            return;
        }
        Map<String, XmlNode> ret = new HashMap<>();
        try {
            XmlNode node = null;
            Exception lasEx = null;
            // nio 通知发生的时候，有可能文件还没有落盘完成
            // 如果直接读取可能会出现错误，因此延迟重试几次，确保落盘完成
            for (int i = 0; i < 3; i++) {
                try {
                    node = JdbcProcedureParser.parse(info.getFileName(), new ByteArrayInputStream(info.getXmlContent().getBytes(StandardCharsets.UTF_8)));
                    break;
                } catch (Exception e) {
                    lasEx = e;
                }
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                }
            }
            if (node == null) {
                if (lasEx != null) {
                    throw lasEx;
                } else {
                    throw new IllegalStateException("read xml file error: " + info.getFileName());
                }
            }
            if (needNotifyChange) {
                logger.logDebug(XProc4jConsts.NAME + " watching xml-node file change parsed:" + info.getFileName());
            }
            String id = node.getTagAttrMap().get(AttrConsts.ID);
            if (id != null) {
                ret.put(id, node);
                if (needNotifyChange) {
                    logger.logDebug(XProc4jConsts.NAME + " watching xml-node node:" + id);
                }
                Map<String, XmlNode> next = new HashMap<>();
                JdbcProcedureParser.resolveEmbedIdNode(node, next);
                for (Map.Entry<String, XmlNode> entry : next.entrySet()) {
                    ret.put(entry.getKey(), entry.getValue());
                    if (!entry.getKey().equals(id)) {
                        String childId = id + "." + entry.getKey();
                        if (needNotifyChange) {
                            logger.logDebug(XProc4jConsts.NAME + " watching xml-node node-child:" + childId);
                        }
                        ret.put(childId, entry.getValue());
                    }
                }
            }
            if (!ret.isEmpty()) {
                nodeMap.putAll(ret);
                if (needNotifyChange) {
                    notifyChange(ret);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, XmlNode> parseResources() {
        return new LinkedHashMap<>(nodeMap);
    }
}
