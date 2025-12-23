package i2f.springboot.ops.datasource.provider.impl;

import i2f.jdbc.JdbcResolver;
import i2f.jdbc.datasource.impl.DirectConnectionDatasource;
import i2f.springboot.ops.datasource.configuration.DatasourceAutoConfiguration;
import i2f.springboot.ops.datasource.data.DatasourceItemDto;
import i2f.springboot.ops.datasource.provider.DatasourceCollector;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2025/12/23 22:29
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@Component
public class StaticDatasourceCollector implements DatasourceCollector {
    @Autowired
    protected DatasourceAutoConfiguration configuration;

    protected ConcurrentHashMap<String,DataSource> dataSourceMap = new ConcurrentHashMap<>();
    protected AtomicBoolean initialized = new AtomicBoolean(false);

    @Override
    public Map<String, DataSource> collect() {
        if(!initialized.getAndSet(true)) {
            Map<String, DatasourceItemDto> map = configuration.getDatasourceMap();
            for (Map.Entry<String, DatasourceItemDto> entry : map.entrySet()) {
                String key = entry.getKey();
                try {
                    DatasourceItemDto value = entry.getValue();
                    DirectConnectionDatasource ds = new DirectConnectionDatasource(() -> {
                        Connection conn = JdbcResolver.getConnection(value.getDriver(), value.getUrl(), value.getUsername(), value.getPassword());
                        return conn;
                    });
                    dataSourceMap.put(key, ds);
                } catch (Exception e) {
                    log.warn("init static datasource ["+key+"] failed: "+e.getMessage(), e);
                }
            }
        }
        return new LinkedHashMap<>(dataSourceMap);
    }
}
