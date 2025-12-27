package i2f.springboot.limit.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.limit.data.LimitRule;
import i2f.springboot.limit.data.LimitRuleItem;
import i2f.springboot.limit.properties.LimitRuleProperties;
import i2f.springboot.limit.provider.LimitRuleItemProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2025/11/11 16:23
 */
@Slf4j
@Data
@NoArgsConstructor
@Component
public class LimitManager implements ApplicationRunner {
    @Autowired
    protected ApplicationContext applicationContext;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected LimitRuleProperties properties;
    @Autowired
    protected LimitRedisHolder redisHolder;

    /**
     * Map<limitType,Map<typeKey,LimitRule>>
     */
    protected final ConcurrentHashMap<String, ConcurrentHashMap<String, LimitRule>> ruleHolder = new ConcurrentHashMap<>();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        startScanRuleThread();
    }


    public void startScanRuleThread() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Map<String, Map<String, LimitRule>> map = scanRule();
                    applyRule(map);
                } catch (Throwable e) {

                }

                try {
                    Thread.sleep(5000);
                } catch (Throwable e) {
                }
            }
        });
        thread.setName("limit-rule-scanner");
        thread.setDaemon(true);
        thread.start();
    }

    public void applyRule(Map<String, Map<String, LimitRule>> map) {
        if (map == null) {
            return;
        }
        for (Map.Entry<String, Map<String, LimitRule>> entry : map.entrySet()) {
            String limitType = entry.getKey();
            if (limitType == null) {
                continue;
            }
            Map<String, LimitRule> value = entry.getValue();
            if (value == null) {
                continue;
            }
            ConcurrentHashMap<String, LimitRule> keyMap = ruleHolder.computeIfAbsent(limitType, k -> new ConcurrentHashMap<>());
            for (Map.Entry<String, LimitRule> ruleEntry : value.entrySet()) {
                String typeKey = ruleEntry.getKey();
                if (typeKey == null) {
                    continue;
                }
                LimitRule rule = ruleEntry.getValue();
                if (rule == null) {
                    continue;
                }
                keyMap.put(typeKey, rule);
            }
        }
    }

    public String getAppName() {
        String ret = properties.getAppName();
        if (ret == null || ret.isEmpty()) {
            return LimitConsts.DEFAULT_APP_NAME;
        }
        return ret;
    }

    public Map<String, Map<String, LimitRule>> scanRule() {
        Map<String, Map<String, LimitRule>> ret = new LinkedHashMap<>();
        String[] names = applicationContext.getBeanNamesForType(LimitRuleItemProvider.class);
        for (String name : names) {
            LimitRuleItemProvider provider = applicationContext.getBean(name, LimitRuleItemProvider.class);
            List<LimitRuleItem> rules = provider.getRules(getAppName());
            for (LimitRuleItem rule : rules) {
                if (provider.isDynamic()) {
                    saveRule(rule.getLimitType(), rule.getTypeKey(), rule.getRule());
                } else {
                    registryIfAbsentRule(rule.getLimitType(), rule.getTypeKey(), rule.getRule());
                }
            }
        }
        return ret;
    }

    public int registryIfAbsentRule(LimitType limitType, String typeKey, LimitRule rule) {
        return registryIfAbsentRule(limitType.value(), typeKey, rule);
    }

    public int registryIfAbsentRule(String limitType, String typeKey, LimitRule rule) {
        if (rule == null) {
            return -1;
        }
        if (limitType == null) {
            return -1;
        }
        if (typeKey == null) {
            return -1;
        }
        ConcurrentHashMap<String, LimitRule> keyMap = ruleHolder.get(limitType);
        if (keyMap != null) {
            LimitRule exRule = keyMap.get(typeKey);
            if (exRule != null) {
                return 0;
            }
        }

        saveRule(limitType, typeKey, rule);

        return 1;
    }

    public boolean saveRule(LimitType limitType, String typeKey, LimitRule rule) {
        return saveRule(limitType.value(), typeKey, rule);
    }

    public boolean saveRule(String limitType, String typeKey, LimitRule rule) {
        if (rule == null) {
            return false;
        }
        if (limitType == null) {
            return false;
        }
        if (typeKey == null) {
            return false;
        }
        String key = LimitConsts.KEY_RULE + getAppName() + ":" + limitType + ":" + typeKey;
        RedisTemplate redisTemplate = redisHolder.getRedisTemplate();
        redisTemplate.execute((conn) -> {
            byte[] rawKey = key.getBytes(StandardCharsets.UTF_8);
            byte[] rawValue = conn.get(rawKey);
            if (rawValue == null || rawValue.length == 0) {
                try {
                    String json = objectMapper.writeValueAsString(rule);
                    rawValue = json.getBytes(StandardCharsets.UTF_8);
                    conn.set(rawKey, rawValue);
                } catch (JsonProcessingException e) {
                    throw new IllegalArgumentException(e.getMessage(), e);
                }
            }
            return rawValue;
        }, true);

        ruleHolder.computeIfAbsent(limitType, k -> new ConcurrentHashMap<>())
                .put(typeKey, rule);
        return true;
    }

    public List<LimitRuleItem> getRules(LimitType limitType) {
        return getRules(limitType.value());
    }

    public List<LimitRuleItem> getRules(String limitType) {
        List<LimitRuleItem> ret = new ArrayList<>();
        if (limitType == null) {
            return ret;
        }
        ConcurrentHashMap<String, LimitRule> keyMap = ruleHolder.get(limitType);
        if (keyMap == null) {
            return ret;
        }
        for (Map.Entry<String, LimitRule> entry : keyMap.entrySet()) {
            ret.add(new LimitRuleItem(limitType, entry.getKey(), entry.getValue()));
        }
        ret.sort((v1, v2) -> {
            return Integer.compare(v1.getRule().getOrder(), v2.getRule().getOrder());
        });
        return ret;
    }

    public boolean isLimited(LimitType limitType, String typeKey) {
        return isLimited(limitType.value(), typeKey);
    }

    public boolean isLimited(String limitType, String typeKey) {
        ConcurrentHashMap<String, LimitRule> keyMap = ruleHolder.get(limitType);
        if (keyMap == null) {
            return false;
        }
        LimitRule rule = keyMap.get(typeKey);
        if (rule == null) {
            return false;
        }
        return isLimited(limitType, typeKey, rule);
    }

    public boolean isLimited(LimitType limitType, String typeKey, LimitRule rule) {
        return isLimited(limitType.value(), typeKey, rule);
    }

    public boolean isLimited(String limitType, String typeKey, LimitRule rule) {
        int count = rule.getCount();
        if (count < 0) {
            return false;
        }
        RedisTemplate redisTemplate = redisHolder.getRedisTemplate();
        Long currCnt = (Long) redisTemplate.execute((conn) -> {
            String key = LimitConsts.KEY_TARGET + getAppName() + ":" + limitType + ":" + typeKey;
            byte[] rawKey = key.getBytes(StandardCharsets.UTF_8);
            Long cnt = conn.incr(rawKey);
            if (cnt <= 1 && rule.getTtl() >= 0) {
                conn.expire(rawKey, rule.getTtl());
            }
            return cnt;
        }, true);
        if (currCnt > rule.getCount()) {
            return true;
        }
        return false;
    }

}
