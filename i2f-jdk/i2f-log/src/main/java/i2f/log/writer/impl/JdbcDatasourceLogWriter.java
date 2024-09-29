package i2f.log.writer.impl;

import i2f.log.data.LogData;
import i2f.log.writer.ILogWriter;
import i2f.uid.Uid;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/9/29 8:59
 */
@Data
@NoArgsConstructor
public class JdbcDatasourceLogWriter implements ILogWriter {
    public static final String DEFAULT_APPLICATION;
    public static final String DEFAULT_HOST;

    static {
        DEFAULT_APPLICATION = System.getProperty("log.default.application", "noappname");
        String addr = "0.0.0.0";
        try {
            addr = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {

        }
        DEFAULT_HOST = addr;
    }

    protected final LinkedBlockingQueue<LogData> queue = new LinkedBlockingQueue<>();
    protected final AtomicBoolean refreshing = new AtomicBoolean(false);
    protected final AtomicBoolean first = new AtomicBoolean(true);
    protected final AtomicLong lastWriteTs = new AtomicLong(0);
    protected final AtomicInteger maxSleepMillSeconds = new AtomicInteger(1000);
    protected final AtomicInteger minBatchSize = new AtomicInteger(30);
    protected final AtomicInteger maxBatchSize = new AtomicInteger(300);
    protected final AtomicInteger maxIdleMillSeconds = new AtomicInteger(15 * 1000);
    protected DataSource dataSource;
    protected volatile String application = DEFAULT_APPLICATION;
    protected volatile String host = DEFAULT_HOST;
    protected volatile String tableName = "i2f_log";
    protected boolean detectNameWrapper = true;
    protected Function<String, String> tableNameWrapper;
    protected Function<String, String> columnNameWrapper;

    public JdbcDatasourceLogWriter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JdbcDatasourceLogWriter(DataSource dataSource, String application) {
        this.dataSource = dataSource;
        this.application = application;
    }

    public JdbcDatasourceLogWriter(DataSource dataSource, String application, String host) {
        this.dataSource = dataSource;
        this.application = application;
        this.host = host;
    }

    public JdbcDatasourceLogWriter(DataSource dataSource, String application, String host, String tableName) {
        this.dataSource = dataSource;
        this.application = application;
        this.host = host;
        this.tableName = tableName;
    }

    public static String mysqlName(String name) {
        if (name == null) {
            return null;
        }
        if (name.startsWith("`")) {
            return name;
        }
        return "`" + name + "`";
    }

    public static String oracleName(String name) {
        if (name == null) {
            return null;
        }
        if (name.startsWith("\"")) {
            return name;
        }
        return "\"" + name + "\"";
    }

    @Override
    public void write(LogData data) {
        queue.add(data);
        trigger();
    }

    public void trigger() {
        if (refreshing.getAndSet(true)) {
            return;
        }
        Thread thread = new Thread(() -> {
            double sleepTs = 1;
            while (true) {
                if (!queue.isEmpty()) {
                    sleepTs = 1;
                    try {
                        refreshData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sleepTs *= 1.1;
                    if (sleepTs > maxSleepMillSeconds.get()) {
                        sleepTs = maxSleepMillSeconds.get();
                    }
                    try {
                        Thread.sleep((int) sleepTs);
                    } catch (Exception e) {

                    }
                }
            }
        }, "jdbc-log-writer");
        thread.setDaemon(true);
        thread.start();
    }

    public void refreshData() throws SQLException {
        if (queue.size() < minBatchSize.get() && System.currentTimeMillis() - lastWriteTs.get() < maxIdleMillSeconds.get()) {
            return;
        }
        lastWriteTs.set(System.currentTimeMillis());
        try (Connection conn = dataSource.getConnection()) {
            if (first.getAndSet(false)) {
                checkTable(conn);
            }
            List<LogData> list = new ArrayList<>();
            int count = 0;
            while (count < maxBatchSize.get()) {
                LogData data = queue.poll();
                if (data == null) {
                    break;
                }
                list.add(data);
                count++;
            }
            if (!list.isEmpty()) {
                writeData(conn, list);
            }
        }
    }

    public String column(String name) {
        if (name == null) {
            return null;
        }
        if (columnNameWrapper == null) {
            return name;
        }
        return columnNameWrapper.apply(name);
    }

    public String table(String name) {
        if (name == null) {
            return null;
        }
        if (tableNameWrapper == null) {
            return name;
        }
        return tableNameWrapper.apply(name);
    }

    public void writeData(Connection conn, List<LogData> list) throws SQLException {
        String sql = new StringBuilder()
                .append("insert into ").append(table(tableName)).append("\n")
                .append("    (")
                .append(column("id")).append(",")
                .append(column("application")).append(",")
                .append(column("host")).append(",")
                .append(column("location")).append(",")
                .append(column("level")).append(",")
                .append(column("date")).append(",")
                .append(column("msg")).append(",")
                .append(column("is_ex")).append(",")
                .append(column("ex_msg")).append(",")
                .append(column("ex_trace")).append(",")
                .append(column("thread_name")).append(",")
                .append(column("thread_id")).append(",")
                .append(column("class_name")).append(",")
                .append(column("method_name")).append(",")
                .append(column("file_name")).append(",")
                .append(column("line_number")).append(",")
                .append(column("trace_id"))
                .append(")\n")
                .append("values \n")
                .append("    (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")
                .toString();
        PreparedStatement stat = conn.prepareStatement(sql);
        for (LogData item : list) {
            int i = 1;
            stat.setLong(i++, Uid.getId());
            stat.setString(i++, application);
            stat.setString(i++, host);
            stat.setString(i++, item.getLocation());
            stat.setInt(i++, item.getLevel().level());
            stat.setDate(i++, new java.sql.Date(item.getDate().getTime()));
            stat.setString(i++, item.getMsg());
            stat.setInt(i++, item.getEx() == null ? 0 : 1);
            stat.setString(i++, item.getEx() == null ? null : item.getEx().getMessage());
            stat.setString(i++, stringifyThrowable(item.getEx()));
            stat.setString(i++, item.getThreadName());
            stat.setLong(i++, item.getThreadId());
            stat.setString(i++, item.getClassName());
            stat.setString(i++, item.getMethodName());
            stat.setString(i++, item.getFileName());
            stat.setInt(i++, item.getLineNumber());
            stat.setString(i++, item.getTraceId());
            stat.addBatch();
        }
        int[] nums = stat.executeBatch();

    }

    public String stringifyThrowable(Throwable ex) {
        if (ex == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getClass().getName()).append(" : ").append(ex.getMessage());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        ex.printStackTrace(ps);
        ps.close();
        return builder.toString();
    }

    public void checkTable(Connection conn) throws SQLException {
        if (detectNameWrapper) {
            if (tableNameWrapper == null || columnNameWrapper == null) {
                String url = conn.getMetaData().getURL();
                if (url.contains(":mysql:")
                        || url.contains(":postgre:")
                        || url.contains(":db2:")
                        || url.contains(":gbase:")
                        || url.contains(":h2:")) {
                    if (tableNameWrapper == null) {
                        tableNameWrapper = JdbcDatasourceLogWriter::mysqlName;
                    }
                    if (columnNameWrapper == null) {
                        columnNameWrapper = JdbcDatasourceLogWriter::mysqlName;
                    }
                } else if (url.contains(":oracle:")
                        || url.contains(":dm:")
                        || url.contains(":kingbase:")) {
                    if (tableNameWrapper == null) {
                        tableNameWrapper = JdbcDatasourceLogWriter::oracleName;
                    }
                    if (columnNameWrapper == null) {
                        columnNameWrapper = JdbcDatasourceLogWriter::oracleName;
                    }
                }
            }
        }
        if (tableExists(conn)) {
            return;
        }
        createTable(conn);
    }

    public void createTable(Connection conn) throws SQLException {
        String sql = new StringBuilder()
                .append("create table " + table(tableName) + "(\n")
                .append("    ").append(column("id")).append(" bigint primary key,\n")
                .append("    ").append(column("application")).append(" varchar(300),\n")
                .append("    ").append(column("host")).append(" varchar(300),\n")
                .append("    ").append(column("location")).append(" varchar(800),\n")
                .append("    ").append(column("level")).append(" int,\n")
                .append("    ").append(column("date")).append(" datetime,\n")
                .append("    ").append(column("msg")).append(" blob,\n")
                .append("    ").append(column("is_ex")).append(" int,\n")
                .append("    ").append(column("ex_msg")).append(" blob,\n")
                .append("    ").append(column("ex_trace")).append(" blob,\n")
                .append("    ").append(column("thread_name")).append(" varchar(300),\n")
                .append("    ").append(column("thread_id")).append(" long,\n")
                .append("    ").append(column("class_name")).append(" varchar(800),\n")
                .append("    ").append(column("method_name")).append(" varchar(300),\n")
                .append("    ").append(column("file_name")).append(" varchar(300),\n")
                .append("    ").append(column("line_number")).append(" int,\n")
                .append("    ").append(column("trace_id")).append(" varchar(300)\n")
                .append(")")
                .toString();
        PreparedStatement stat = conn.prepareStatement(sql);
        int num = stat.executeUpdate();
        stat.close();
    }

    public boolean tableExists(Connection conn) {
        try {
            PreparedStatement stat = conn.prepareStatement("select 1 from " + table(tableName) + " where 1!=1");
            ResultSet rs = stat.executeQuery();
            rs.close();
            stat.close();
            return true;
        } catch (SQLException e) {

        }
        return false;
    }
}
