package i2f.springboot.ops.datasource.controller;

import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.DatabaseMetadataProviders;
import i2f.database.metadata.reverse.ddl.DdlDatabaseReverseEngineer;
import i2f.database.metadata.reverse.ddl.DdlDatabaseReverseEngineers;
import i2f.database.metadata.reverse.ddl.impl.GbaseDdlDatabaseReverseEngineer;
import i2f.database.metadata.reverse.ddl.impl.MysqlDdlDatabaseReverseEngineer;
import i2f.database.metadata.reverse.ddl.impl.OracleDdlDatabaseReverseEngineer;
import i2f.database.metadata.reverse.ddl.impl.PostgreDdlDatabaseReverseEngineer;
import i2f.database.metadata.std.DatabaseMetadataProvider;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.datasource.data.DatasourceListRespDto;
import i2f.springboot.ops.datasource.data.DatasourceMetadataDto;
import i2f.springboot.ops.datasource.helper.DatasourceOpsHelper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/1 21:44
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/datasource/metadata")
public class DatasourceOpsMetadataController {

    @Autowired
    private DatasourceOpsHelper datasourceOpsHelper;

    @Autowired
    protected OpsSecureTransfer transfer;

    @PostMapping("/databases")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> databases(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            DatasourceMetadataDto req = transfer.recv(reqDto, DatasourceMetadataDto.class);
            try (Connection conn = datasourceOpsHelper.getConnection(req)) {
                DatabaseMetadataProvider provider = DatabaseMetadataProviders.findProvider(conn);
                List<String> databases = provider.getDatabases(conn);
                String defaultDatabase = provider.detectDefaultDatabase(conn);
                DatasourceListRespDto resp=new DatasourceListRespDto();
                resp.setList(databases);
                resp.setDefaultName(defaultDatabase);
                return transfer.success(resp);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/tables")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> tables(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            DatasourceMetadataDto req = transfer.recv(reqDto, DatasourceMetadataDto.class);
            String database = req.getDatabase();
            try (Connection conn = datasourceOpsHelper.getConnection(req)) {
                DatabaseMetadataProvider provider = DatabaseMetadataProviders.findProvider(conn);
                List<TableMeta> tables = provider.getTables(conn, database);
                return transfer.success(tables);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/table/info")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> tableInfo(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            DatasourceMetadataDto req = transfer.recv(reqDto, DatasourceMetadataDto.class);
            String database = req.getDatabase();
            String table = req.getTable();
            try (Connection conn = datasourceOpsHelper.getConnection(req)) {
                DatabaseMetadataProvider provider = DatabaseMetadataProviders.findProvider(conn);
                TableMeta tableMeta = provider.getTableInfo(conn, database, table);
                return transfer.success(tableMeta);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/table/ddl")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> tableDdl(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            DatasourceMetadataDto req = transfer.recv(reqDto, DatasourceMetadataDto.class);
            String database = req.getDatabase();
            String table = req.getTable();
            try (Connection conn = datasourceOpsHelper.getConnection(req)) {
                DdlDatabaseReverseEngineer engineer = DdlDatabaseReverseEngineers.getEngineer(conn);
                String ddlType = req.getDdlType();
                if("oracle".equalsIgnoreCase(ddlType)){
                    engineer= OracleDdlDatabaseReverseEngineer.CONVERT;
                }else if("mysql".equalsIgnoreCase(ddlType)){
                    engineer= MysqlDdlDatabaseReverseEngineer.CONVERT;
                }if("postgre".equalsIgnoreCase(ddlType)){
                    engineer= PostgreDdlDatabaseReverseEngineer.CONVERT;
                }if("gbase".equalsIgnoreCase(ddlType)){
                    engineer= GbaseDdlDatabaseReverseEngineer.CONVERT;
                }
                DatabaseMetadataProvider provider = DatabaseMetadataProviders.findProvider(conn);
                TableMeta tableMeta = provider.getTableInfo(conn, database, table);
                String ddl = engineer.generate(tableMeta);
                return transfer.success(ddl);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }
}
