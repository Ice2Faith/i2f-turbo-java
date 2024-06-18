package i2f.springboot.dynamic.datasource.core;

import i2f.springboot.dynamic.datasource.autoconfiguration.DynamicDataSourceProperty;
import i2f.springboot.dynamic.datasource.exception.DataSourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 动态数据源
 *
 * @author Ice2Faith
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private Logger log = LoggerFactory.getLogger(DynamicDataSource.class);

    private DynamicDataSourceProperty dynamicDataSourceProperty;
    private Map<String, List<String>> groupMap;
    private Map<String, AtomicInteger> groupIndexMap;
    private SecureRandom random = new SecureRandom();

    public DynamicDataSource(DataSource defaultTargetDataSource,
                             Map<Object, Object> targetDataSources,
                             DynamicDataSourceProperty dynamicDataSourceProperty,
                             Map<String, List<String>> groupMap) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
        this.dynamicDataSourceProperty = dynamicDataSourceProperty;
        this.groupIndexMap = new ConcurrentHashMap<>();
        for (String item : groupMap.keySet()) {
            this.groupIndexMap.put(item, new AtomicInteger(0));
        }
        this.groupMap = groupMap;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String key = lookupDataSourceKey();
        log.info("determine lookup datasource: " + key);
        return DynamicDataSourceContextHolder.realDataSourceTypeName(key);
    }

    public String lookupDataSourceKey() {
        LookupDataSource lookup = DynamicDataSourceContextHolder.getDataSourceType();
        if (lookup == null) {
            return dynamicDataSourceProperty.getPrimary();
        }
        if (lookup.getKey() != null) {
            return lookup.getKey();
        }
        if (lookup.isGroup()) {
            if (!groupMap.containsKey(lookup.getType())) {
                if (dynamicDataSourceProperty.isStrict()) {
                    throw new DataSourceNotFoundException("require datasource group [" + lookup.getType() + "] not found on strict mode.");
                } else {
                    lookup.setKey(dynamicDataSourceProperty.getPrimary());
                    return lookup.getKey();
                }
            } else {
                List<String> list = groupMap.get(lookup.getType());
                int size = list.size();
                AtomicInteger idx = groupIndexMap.get(lookup.getType());
                LookupBalanceType balance = lookup.getBalance();
                if (balance == null || balance == LookupBalanceType.UNKNOWN) {
                    balance = LookupBalanceType.of(dynamicDataSourceProperty.getBalance());
                }
                if (balance == null || balance == LookupBalanceType.UNKNOWN) {
                    balance = LookupBalanceType.RING;
                }
                if (balance == LookupBalanceType.RANDOM) {
                    int i = idx.updateAndGet(v -> {
                        return random.nextInt(size);
                    });
                    lookup.setKey(list.get(i));
                    return lookup.getKey();
                } else if (balance == LookupBalanceType.RING) {
                    int i = idx.updateAndGet(v -> {
                        return (v + 1) % size;
                    });
                    lookup.setKey(list.get(i));
                    return lookup.getKey();
                } else {
                    int i = idx.updateAndGet(v -> {
                        return (v + 1) % size;
                    });
                    lookup.setKey(list.get(i));
                    return lookup.getKey();
                }
            }
        } else {
            String key = lookup.getType();
            if (!getResolvedDataSources().containsKey(key)) {
                if (dynamicDataSourceProperty.isStrict()) {
                    throw new DataSourceNotFoundException("require datasource [" + key + "] not found on strict mode.");
                } else {
                    lookup.setKey(dynamicDataSourceProperty.getPrimary());
                    return lookup.getKey();
                }
            }
            lookup.setKey(key);
            return lookup.getKey();
        }
    }
}
