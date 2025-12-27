package i2f.springboot.limit.provider.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.limit.core.LimitConsts;
import i2f.springboot.limit.core.LimitRedisHolder;
import i2f.springboot.limit.data.LimitRule;
import i2f.springboot.limit.data.LimitRuleItem;
import i2f.springboot.limit.provider.LimitRuleItemProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/12 14:22
 */
@ConditionalOnExpression("${i2f.springboot.rule-provider.redis-dynamic.enable:true}")
@Data
@NoArgsConstructor
@Component
public class RedisDynamicLimitRuleItemProvider implements LimitRuleItemProvider {

    @Autowired
    private LimitRedisHolder redisHolder;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public List<LimitRuleItem> getRules(String appName) {
        List<LimitRuleItem> ret = new ArrayList<>();
        RedisTemplate redisTemplate = redisHolder.getRedisTemplate();
        String prefix = LimitConsts.KEY_RULE + appName + ":";
        String pattern = prefix + "*";
        byte[] rawPattern = pattern.getBytes(StandardCharsets.UTF_8);
        ScanOptions options = ScanOptions.scanOptions()
                .match(rawPattern)
                .build();
        Cursor<byte[]> cursor = (Cursor<byte[]>) redisTemplate.executeWithStickyConnection((conn) -> {
            return conn.scan(options);
        });
        while (cursor.hasNext()) {
            byte[] next = cursor.next();
            String key = new String(next, StandardCharsets.UTF_8);
            String str = key.substring(prefix.length());
            String[] arr = str.split(":", 2);
            if (arr.length != 2) {
                continue;
            }
            String limitType = arr[0];
            String typeKey = arr[1];
            byte[] jsonBytes = (byte[]) redisTemplate.execute((conn) -> {
                return conn.get(next);
            }, true);
            if (jsonBytes == null) {
                continue;
            }
            LimitRule rule = null;
            try {
                rule = objectMapper.readValue(jsonBytes, LimitRule.class);
            } catch (Exception e) {

            }
            if (rule == null) {
                continue;
            }

            LimitRuleItem flatRule = new LimitRuleItem(limitType, typeKey, rule);
            ret.add(flatRule);
        }
        return ret;
    }
}
