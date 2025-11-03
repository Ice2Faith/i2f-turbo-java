package i2f.springboot.ops.datasource.controller;

import i2f.bindsql.BindSql;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryResult;
import i2f.jdbc.script.JdbcScriptRunner;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.datasource.data.DatasourceListRespDto;
import i2f.springboot.ops.datasource.data.DatasourceOperateDto;
import i2f.springboot.ops.datasource.data.DatasourceRunnerDto;
import i2f.springboot.ops.datasource.helper.DatasourceOpsHelper;
import i2f.springboot.ops.datasource.provider.DatasourceProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/11/1 21:44
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/datasource")
public class DatasourceOpsController {

    @Autowired
    protected DatasourceProvider datasourceProvider;

    @Autowired
    protected DatasourceOpsHelper datasourceOpsHelper;

    @Autowired
    protected OpsSecureTransfer transfer;

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
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/datasources")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> datasources(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            Long req = transfer.recv(reqDto, Long.class);
            Map<String, DataSource> datasourceMap = datasourceProvider.getDatasourceMap();
            DatasourceListRespDto resp=new DatasourceListRespDto();
            List<String> list=new ArrayList<>();
            Set<String> set = datasourceMap.keySet();
            list.addAll(set);
            list.sort(String::compareTo);
            resp.setList(list);
            String defaultDataSourceName = datasourceProvider.getDefaultDataSourceName();
            resp.setDefaultName(defaultDataSourceName);
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/query")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> query(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            DatasourceOperateDto req = transfer.recv(reqDto, DatasourceOperateDto.class);
            String sql = req.getSql();
            Integer maxCount = req.getMaxCount();
            try (Connection conn = datasourceOpsHelper.getConnection(req)) {
                QueryResult resp = JdbcResolver.query(conn, new BindSql(sql), maxCount == null ? -1 : maxCount);
                return transfer.success(resp);
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> update(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            DatasourceOperateDto req = transfer.recv(reqDto, DatasourceOperateDto.class);
            String sql = req.getSql();
            try (Connection conn = datasourceOpsHelper.getConnection(req)) {
                int update = JdbcResolver.update(conn, new BindSql(sql));
                return transfer.success(update);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/runner")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> runner(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            DatasourceRunnerDto req = transfer.recv(reqDto, DatasourceRunnerDto.class);
            String sql = req.getSql();
            try (Connection conn = datasourceOpsHelper.getConnection(req)) {
                StringBuilder builder = new StringBuilder();
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
                return transfer.success(builder.toString());
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e.getMessage());
        }
    }

}
