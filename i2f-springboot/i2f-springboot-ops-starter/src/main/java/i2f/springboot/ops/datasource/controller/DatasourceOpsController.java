package i2f.springboot.ops.datasource.controller;

import i2f.bindsql.BindSql;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.DatabaseMetadataProviders;
import i2f.database.metadata.reverse.ddl.DdlDatabaseReverseEngineer;
import i2f.database.metadata.reverse.ddl.DdlDatabaseReverseEngineers;
import i2f.database.metadata.std.DatabaseMetadataProvider;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.data.QueryResult;
import i2f.springboot.ops.common.OpsException;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.datasource.data.DatasourceListRespDto;
import i2f.springboot.ops.datasource.data.DatasourceMetadataDto;
import i2f.springboot.ops.datasource.data.DatasourceOperateDto;
import i2f.springboot.ops.datasource.provider.DatasourceProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private DatasourceProvider datasourceProvider;

    @Autowired
    protected OpsSecureTransfer transfer;

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
            String datasourceName = req.getDatasource();
            if(StringUtils.isEmpty(datasourceName)){
                datasourceName=datasourceProvider.getDefaultDataSourceName();
            }
            DataSource datasource = datasourceProvider.getDatasource(datasourceName);
            if(datasource==null){
                throw new OpsException("missing datasource!");
            }
            String sql = req.getSql();
            Integer maxCount = req.getMaxCount();
            try(Connection conn= datasource.getConnection()) {
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
            String datasourceName = req.getDatasource();
            if(StringUtils.isEmpty(datasourceName)){
                datasourceName=datasourceProvider.getDefaultDataSourceName();
            }
            DataSource datasource = datasourceProvider.getDatasource(datasourceName);
            if(datasource==null){
                throw new OpsException("missing datasource!");
            }
            String sql = req.getSql();
            try(Connection conn= datasource.getConnection()) {
                int update = JdbcResolver.update(conn, new BindSql(sql));
                return transfer.success(update);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

}
