package i2f.springboot.ops.datasource.provider.impl;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import i2f.springboot.ops.datasource.provider.DatasourceCollector;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/11/28 11:18
 */
@ConditionalOnClass(DynamicRoutingDataSource.class)
@Data
@NoArgsConstructor
@Component
public class BaomidouDynamicDatasourceCollector implements DatasourceCollector {
    @Autowired
    protected ApplicationContext applicationContext;

    @Override
    public Map<String, DataSource> collect() {
        Map<String, DataSource> ret = new LinkedHashMap<>();
        try {
            DynamicRoutingDataSource bean = applicationContext.getBean(DynamicRoutingDataSource.class);
            Map<String, DataSource> map = bean.getDataSources();
            ret.putAll(map);
        } catch (Exception e) {
        }
        return ret;
    }
}
