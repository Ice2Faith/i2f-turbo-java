package i2f.springboot.ops.datasource.controller;

import i2f.bindsql.BindSql;
import i2f.convert.obj.ObjectConvertor;
import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.DatabaseMetadataProviders;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryColumn;
import i2f.jdbc.data.QueryResult;
import i2f.jdbc.script.JdbcScriptRunner;
import i2f.rowset.impl.csv.CsvMapRowSetReader;
import i2f.rowset.impl.csv.CsvMapRowSetWriter;
import i2f.rowset.std.IRowHeader;
import i2f.rowset.std.IRowSet;
import i2f.rowset.std.impl.SimpleIteratorRowSet;
import i2f.rowset.std.impl.SimpleRowHeader;
import i2f.springboot.ops.common.OpsException;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.datasource.data.DatasourceImportOperateDto;
import i2f.springboot.ops.datasource.data.DatasourceListRespDto;
import i2f.springboot.ops.datasource.data.DatasourceOperateDto;
import i2f.springboot.ops.datasource.data.DatasourceRunnerDto;
import i2f.springboot.ops.datasource.helper.DatasourceOpsHelper;
import i2f.springboot.ops.datasource.provider.DatasourceProvider;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.provider.IOpsProvider;
import i2f.web.servlet.ServletFileUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.security.MessageDigest;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2025/11/1 21:44
 * @desc
 */
@ConditionalOnClass(DataSource.class)
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/datasource")
public class DatasourceOpsController implements IOpsProvider {

    @Autowired
    protected DatasourceProvider datasourceProvider;

    @Autowired
    protected DatasourceOpsHelper datasourceOpsHelper;

    @Autowired
    protected OpsSecureTransfer transfer;

    @Override
    public List<OpsHomeMenuDto> getMenus() {
        return Collections.singletonList(new OpsHomeMenuDto()
                .title("Database")
                .subTitle("连接/操作数据库")
                .icon("el-icon-coin")
                .href("./datasource/index.html")
        );
    }

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("./index.html").forward(request, response);
    }

    @PostMapping("/drivers")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> drivers(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            Long req = transfer.recv(reqDto, Long.class);
            ServiceLoader<Driver> drivers = ServiceLoader.load(Driver.class);
            List<String> list = new ArrayList<>();
            for (Driver item : drivers) {
                Class<? extends Driver> clazz = item.getClass();
                list.add(clazz.getName());
            }
            return transfer.success(list);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/datasources")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> datasources(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            Long req = transfer.recv(reqDto, Long.class);
            Map<String, DataSource> datasourceMap = datasourceProvider.getDatasourceMap();
            DatasourceListRespDto resp = new DatasourceListRespDto();
            List<String> list = new ArrayList<>();
            Set<String> set = datasourceMap.keySet();
            list.addAll(set);
            list.sort(String::compareTo);
            resp.setList(list);
            String defaultDataSourceName = datasourceProvider.getDefaultDataSourceName();
            resp.setDefaultName(defaultDataSourceName);
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/query")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> query(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            DatasourceOperateDto req = transfer.recv(reqDto, DatasourceOperateDto.class);
            String sql = req.getSql();
            int maxCount = req.getMaxCount() == null ? -1 : req.getMaxCount();
            Map<String, Connection> connMap = datasourceOpsHelper.getMultipleConnection(req);
            QueryResult resp = new QueryResult();
            resp.setColumns(new ArrayList<>());
            resp.setRows(new ArrayList<>(maxCount > 0 ? Math.min(maxCount, 500) : 0));
            int connMapCount = connMap.size();
            QueryColumn datasourceColumn = new QueryColumn();
            if (connMapCount > 1) {
                fillDatasourceColumn(datasourceColumn);
            }
            for (Map.Entry<String, Connection> entry : connMap.entrySet()) {
                try (Connection conn = entry.getValue()) {
                    QueryResult qr = JdbcResolver.query(conn, new BindSql(sql), maxCount);
                    if (connMapCount > 1) {
                        qr.getColumns().add(0, datasourceColumn);
                        qr.getRows().forEach(e -> e.put(datasourceColumn.getName(), entry.getKey()));
                    }
                    resp.setColumns(qr.getColumns());
                    resp.getRows().addAll(qr.getRows());
                }
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/export")
    @ResponseBody
    public void export(@RequestBody OpsSecureDto reqDto, HttpServletResponse response) throws Exception {
        try {
            DatasourceOperateDto req = transfer.recv(reqDto, DatasourceOperateDto.class);
            String sql = req.getSql();
            int maxCount = req.getMaxCount() == null ? -1 : req.getMaxCount();
            Map<String, Connection> connMap = datasourceOpsHelper.getMultipleConnection(req);
            int connMapCount = connMap.size();
            QueryColumn datasourceColumn = new QueryColumn();
            if (connMapCount > 1) {
                fillDatasourceColumn(datasourceColumn);
            }
            File ret = File.createTempFile("export-" + (UUID.randomUUID().toString().replace("-", "")), ".csv");
            try {
                try (OutputStream os = new FileOutputStream(ret)) {
                    for (Map.Entry<String, Connection> entry : connMap.entrySet()) {
                        try (Connection conn = entry.getValue()) {
                            JdbcResolver.query(conn, new BindSql(sql), (rs -> {

                                ResultSetMetaData metaData = rs.getMetaData();
                                List<QueryColumn> columns = JdbcResolver.parseResultSetColumns(metaData);
                                List<QueryColumn> parseColumns = new ArrayList<>(columns);
                                if (connMapCount > 1) {
                                    columns.add(0, datasourceColumn);
                                }

                                Iterator<Map<String, Object>> iterator = new Iterator<Map<String, Object>>() {
                                    private int currCount = 0;

                                    @Override
                                    public boolean hasNext() {
                                        try {
                                            return (!(maxCount >= 0 && currCount >= maxCount)) && rs.next();
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e.getMessage(), e);
                                        }
                                    }

                                    @Override
                                    public Map<String, Object> next() {
                                        try {
                                            Map<String, Object> map = JdbcResolver.convertResultSetRowAsMap(parseColumns, rs);
                                            if (connMapCount > 1) {
                                                map.put(datasourceColumn.getName(), entry.getKey());
                                            }
                                            currCount++;
                                            return map;
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e.getMessage(), e);
                                        }
                                    }
                                };

                                List<IRowHeader> headers = new ArrayList<>();
                                for (QueryColumn column : columns) {
                                    headers.add(new SimpleRowHeader(column.getName()));
                                }

                                SimpleIteratorRowSet<Map<String, Object>> rowSet = new SimpleIteratorRowSet<>(headers, iterator);
                                CsvMapRowSetWriter<Map<String, Object>> writer = new CsvMapRowSetWriter<>();
                                try {
                                    writer.write(rowSet, os);
                                } catch (IOException e) {
                                    throw new RuntimeException(e.getMessage(), e);
                                }

                                return ret;
                            }));
                        }
                    }
                }
                ServletFileUtil.responseFileAttachment(ret, response);
            } finally {
                if (ret != null && ret.exists()) {
                    ret.delete();
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            PrintWriter writer = response.getWriter();
            writer.write("Internal Server Error");
            writer.flush();
            return;
        }
    }

    public static void fillDatasourceColumn(QueryColumn column) {
        column.setIndex(-1);
        column.setName("_datasource");
        column.setOriginName(column.getName());
        column.setCatalog("virtual");
        column.setClazzName(String.class.getName());
        column.setClazz(String.class);
        column.setDisplaySize(20);
        column.setLabel(column.getName());
        column.setType(Types.VARCHAR);
        column.setJdbcType(JDBCType.VARCHAR);
        column.setTypeName(JDBCType.VARCHAR.name());
        column.setPrecision(20);
        column.setScale(0);
        column.setSchema("virtual");
        column.setTable("virtual");
        column.setNullable(true);
        column.setAutoIncrement(false);
        column.setReadonly(true);
        column.setWritable(false);
        column.setCaseSensitive(true);
        column.setCurrency(false);
        column.setDefinitelyWritable(false);
        column.setSearchable(false);
        column.setSigned(false);
    }

    @PostMapping("/update")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> update(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            DatasourceOperateDto req = transfer.recv(reqDto, DatasourceOperateDto.class);
            String sql = req.getSql();
            Map<String, Object> resp = new HashMap<>();
            Map<String, Connection> connMap = datasourceOpsHelper.getMultipleConnection(req);
            for (Map.Entry<String, Connection> entry : connMap.entrySet()) {
                try (Connection conn = entry.getValue()) {
                    int update = JdbcResolver.update(conn, new BindSql(sql));
                    resp.put(entry.getKey(), update);
                }
            }
            return transfer.success(resp);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/runner")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> runner(@RequestBody OpsSecureDto reqDto) throws Exception {
        StringBuilder builder = new StringBuilder();
        try {
            DatasourceRunnerDto req = transfer.recv(reqDto, DatasourceRunnerDto.class);
            String sql = req.getSql();
            Map<String, Connection> connMap = datasourceOpsHelper.getMultipleConnection(req);
            for (Map.Entry<String, Connection> entry : connMap.entrySet()) {
                builder.append("-- =============== run on " + entry.getKey() + " ===============\n");
                try (Connection conn = entry.getValue()) {
                    JdbcScriptRunner runner = new JdbcScriptRunner(conn);
                    if (req.getAutoCommit() != null) {
                        runner.setAutoCommit(req.getAutoCommit());
                    }
                    if (req.getDelimiter() != null && !req.getDelimiter().isEmpty()) {
                        runner.setDelimiter(req.getDelimiter());
                    }
                    if (req.getSendFullScript() != null) {
                        runner.setSendFullScript(req.getSendFullScript());
                    }
                    if (req.getStopOnError() != null) {
                        runner.setStopOnError(req.getStopOnError());
                    }
                    runner.setLogPrinter((o) -> builder.append(o).append("\n"));
                    runner.setLogErrorPrinter((o, e) -> {
                        builder.append(o).append("\n");
                        if (e != null) {
                            builder.append(e.getMessage()).append("\n");
                            builder.append(e).append("\n");
                        }
                    });
                    runner.runScript(sql);

                }
            }
            return transfer.success(builder.toString());
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return transfer.error(builder.toString(), e);
        }
    }

    @PostMapping("/import")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> doImport(MultipartFile file,
                                                  OpsSecureDto reqDto,
                                                  HttpServletRequest request) throws Exception {
        StringBuilder respBuilder = new StringBuilder();
        AtomicInteger respCount = new AtomicInteger(0);
        try {
            DatasourceImportOperateDto req = transfer.recv(reqDto, DatasourceImportOperateDto.class);

            String table = req.getTable();

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                originalFilename = "tmp.data";
            }
            String suffix = "";
            int idx = originalFilename.lastIndexOf(".");
            if (idx >= 0) {
                suffix = originalFilename.substring(idx).toLowerCase();
            }
            if (!".csv".equals(suffix)) {
                return transfer.error("仅支持导入 .csv 文件");
            }

            File tmpFile = File.createTempFile("upload-" + (UUID.randomUUID().toString().replace("-", "")), ".tmp");
            try {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                OutputStream os = new FileOutputStream(tmpFile);
                byte[] buffer = new byte[2048];
                int len = 0;
                InputStream is = file.getInputStream();
                while ((len = is.read(buffer)) > 0) {
                    digest.update(buffer, 0, len);
                    os.write(buffer, 0, len);
                }
                os.close();
                byte[] bytes = digest.digest();
                StringBuilder builder = new StringBuilder();
                for (byte bt : bytes) {
                    builder.append(String.format("%02x", (int) (bt & 0x0ff)));
                }
                String calcMd5 = builder.toString();
                if (!calcMd5.equalsIgnoreCase(req.getMd5())) {
                    throw new OpsException("file check sum error");
                }

                Map<String, Connection> connMap = datasourceOpsHelper.getMultipleConnection(req);
                for (Map.Entry<String, Connection> entry : connMap.entrySet()) {
                    respCount.set(0);
                    respBuilder.append("-- =============== import on " + entry.getKey() + " ===============\n");
                    CsvMapRowSetReader reader = new CsvMapRowSetReader();
                    try (Connection conn = entry.getValue();
                         IRowSet<Map<String, Object>> read = reader.read(new FileInputStream(tmpFile))) {
                        List<IRowHeader> headers = read.getHeaders();
                        if (headers == null || headers.isEmpty()) {
                            throw new IllegalArgumentException("导入文件没有表头");
                        }
                        Map<String, IRowHeader> rowHeaderMap = new LinkedHashMap<>();
                        for (IRowHeader header : headers) {
                            String name = header.getName().toLowerCase();
                            rowHeaderMap.put(name, header);
                        }

                        TableMeta tableMeta = DatabaseMetadataProviders.findProvider(conn).getTableInfoByQuery(conn, table);
                        List<ColumnMeta> columns = tableMeta.getColumns();
                        Map<String, ColumnMeta> columnMetaMap = new LinkedHashMap<>();
                        for (ColumnMeta column : columns) {
                            String name = column.getName().toLowerCase();
                            columnMetaMap.put(name, column);
                        }

                        Set<String> matchColumns = new LinkedHashSet<>();
                        for (IRowHeader header : headers) {
                            String name = header.getName().toLowerCase();
                            if (!columnMetaMap.containsKey(name)) {
                                continue;
                            }
                            matchColumns.add(name);
                        }

                        StringBuilder sqlBuilder = new StringBuilder();
                        sqlBuilder.append("insert into ").append(table).append(" (");
                        boolean isFirst = true;
                        for (String name : matchColumns) {
                            if (!isFirst) {
                                sqlBuilder.append(", ");
                            }
                            IRowHeader header = rowHeaderMap.get(name);
                            sqlBuilder.append(header.getName());
                            isFirst = false;
                        }
                        sqlBuilder.append(") values (");
                        isFirst = true;
                        for (String name : matchColumns) {
                            if (!isFirst) {
                                sqlBuilder.append(", ");
                            }
                            IRowHeader header = rowHeaderMap.get(name);
                            sqlBuilder.append("${").append(header.getName()).append("}");
                            isFirst = false;
                        }
                        sqlBuilder.append(")");


                        JdbcResolver.batch(conn, sqlBuilder.toString(), new Iterator<Object>() {
                            @Override
                            public boolean hasNext() {
                                return read.hasNext();
                            }

                            @Override
                            public Object next() {
                                respCount.incrementAndGet();
                                Map<String, Object> map = read.next();
                                for (String name : matchColumns) {
                                    IRowHeader header = rowHeaderMap.get(name);
                                    String headerName = header.getName();
                                    Object value = map.get(headerName);
                                    ColumnMeta column = columnMetaMap.get(name);
                                    Class<?> javaType = column.getRawLooseJavaType();
                                    try {
                                        value = ObjectConvertor.tryConvertAsType(value, javaType);
                                    } catch (Throwable e) {

                                    }
                                    map.put(headerName, value);
                                }
                                return map;
                            }
                        }, 300);
                    }
                    respBuilder.append("import count: " + respCount.get());
                }
                return transfer.success(respBuilder.toString());
            } finally {
                if (tmpFile != null && tmpFile.exists()) {
                    tmpFile.delete();
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(respBuilder + "\n" + e.getMessage(), e);
        }
    }
}
