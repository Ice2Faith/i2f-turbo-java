package i2f.springboot.ops.datasource.controller;

import i2f.bindsql.BindSql;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryColumn;
import i2f.jdbc.data.QueryResult;
import i2f.jdbc.script.JdbcScriptRunner;
import i2f.rowset.impl.csv.CsvMapRowSetWriter;
import i2f.rowset.std.IRowHeader;
import i2f.rowset.std.impl.SimpleIteratorRowSet;
import i2f.rowset.std.impl.SimpleRowHeader;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

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
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
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
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

    @PostMapping("/query")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> query(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            DatasourceOperateDto req = transfer.recv(reqDto, DatasourceOperateDto.class);
            String sql = req.getSql();
            int maxCount = req.getMaxCount()==null?-1:req.getMaxCount();
            Map<String, Connection> connMap = datasourceOpsHelper.getMultipleConnection(req);
            QueryResult resp = new QueryResult();
            resp.setColumns(new ArrayList<>());
            resp.setRows(new ArrayList<>(maxCount > 0 ? Math.min(maxCount, 500) : 0));
            for (Map.Entry<String, Connection> entry : connMap.entrySet()) {
                try (Connection conn = entry.getValue()) {
                    QueryResult qr = JdbcResolver.query(conn, new BindSql(sql), maxCount);
                    resp.setColumns(qr.getColumns());
                    resp.getRows().addAll(qr.getRows());
                }
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

    @PostMapping("/export")
    @ResponseBody
    public void export(@RequestBody OpsSecureDto reqDto,HttpServletResponse response) throws Exception {
        try {
            DatasourceOperateDto req = transfer.recv(reqDto, DatasourceOperateDto.class);
            String sql = req.getSql();
            int maxCount = req.getMaxCount()==null?-1:req.getMaxCount();
            Map<String, Connection> connMap = datasourceOpsHelper.getMultipleConnection(req);
            File ret=File.createTempFile("export-"+(UUID.randomUUID().toString().replace("-","")),".csv");
            try {
                try (OutputStream os = new FileOutputStream(ret)) {
                    for (Map.Entry<String, Connection> entry : connMap.entrySet()) {
                        try (Connection conn = entry.getValue()) {
                            QueryResult qr = JdbcResolver.query(conn, new BindSql(sql), maxCount);
                            JdbcResolver.query(conn, new BindSql(sql), (rs -> {

                                ResultSetMetaData metaData = rs.getMetaData();
                                List<QueryColumn> columns = JdbcResolver.parseResultSetColumns(metaData);

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
                                            Map<String, Object> map = JdbcResolver.convertResultSetRowAsMap(columns, rs);
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
            }finally {
                if(ret!=null && ret.exists()){
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
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

    @PostMapping("/runner")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> runner(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            DatasourceRunnerDto req = transfer.recv(reqDto, DatasourceRunnerDto.class);
            String sql = req.getSql();
            Map<String, Connection> connMap = datasourceOpsHelper.getMultipleConnection(req);
            StringBuilder builder = new StringBuilder();
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
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

}
