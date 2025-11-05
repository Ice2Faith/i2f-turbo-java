package i2f.springboot.ops.datasource.helper;

import i2f.springboot.ops.common.OpsException;
import i2f.springboot.ops.datasource.data.DatasourceSelectDto;
import i2f.springboot.ops.datasource.provider.DatasourceProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/11/3 13:49
 */
@Data
@NoArgsConstructor
@Component
public class DatasourceOpsHelper {
    @Autowired
    private DatasourceProvider datasourceProvider;

    public Connection getConnection(DatasourceSelectDto req) throws Exception {
        Boolean useCustomDatasource = req.getUseCustomDatasource();
        if (useCustomDatasource != null && useCustomDatasource) {
            Connection conn=getCustomConnection(req);
            return conn;
        }
        String datasourceName = req.getDatasource();
        Connection  conn = getConnectionByName(datasourceName);
        return conn;
    }

    public Map<String,Connection> getMultipleConnection(DatasourceSelectDto req) throws Exception {
        Map<String,Connection> ret = new LinkedHashMap<>();
        Boolean useCustomDatasource = req.getUseCustomDatasource();
        if(useCustomDatasource != null && useCustomDatasource) {
            Connection conn=getCustomConnection(req);
            ret.put("*custom",conn);
            return ret;
        }
        Boolean useMultiDatasource = req.getUseMultiDatasource();
        if(useMultiDatasource != null && useMultiDatasource) {
            List<String> multiDatasourceList = req.getMultiDatasourceList();
            if(multiDatasourceList==null || multiDatasourceList.isEmpty()) {
                throw new OpsException("use multi datasource require at least one datasource");
            }
            for (String item : multiDatasourceList) {
                if(item==null || item.isEmpty()) {
                    continue;
                }
                if(ret.containsKey(item)) {
                    continue;
                }
                Connection conn = getConnectionByName(item);
                ret.put(item,conn);
            }
            if(ret.isEmpty()){
                throw new OpsException("use multi datasource require at least one datasource");
            }
            return ret;
        }
        String datasourceName = req.getDatasource();
        if (StringUtils.isEmpty(datasourceName)) {
            datasourceName = datasourceProvider.getDefaultDataSourceName();
        }
        Connection conn = getConnectionByName(datasourceName);
        ret.put(datasourceName,conn);
        return ret;
    }

    public Connection getCustomConnection(DatasourceSelectDto req) throws Exception {
        ServiceLoader<Driver> drivers = ServiceLoader.load(Driver.class);
        Driver driver = null;
        for (Driver item : drivers) {
            if (item.acceptsURL(req.getUrl())) {
                driver = item;
            }
        }
        if (driver == null) {
            throw new OpsException("not found suitable driver for url");
        }
        Properties properties = new Properties();
        if (req.getUsername() != null) {
            properties.put("user", req.getUsername());
        }
        if (req.getPassword() != null) {
            properties.put("password", req.getPassword());
        }
        Connection conn = driver.connect(req.getUrl(), properties);
        return conn;
    }

    public Connection getConnectionByName(String datasourceName) throws Exception {
        if (StringUtils.isEmpty(datasourceName)) {
            datasourceName = datasourceProvider.getDefaultDataSourceName();
        }
        DataSource datasource = datasourceProvider.getDatasource(datasourceName);
        if (datasource == null) {
            throw new OpsException("missing datasource!");
        }
        Connection conn = datasource.getConnection();
        return conn;
    }


}
