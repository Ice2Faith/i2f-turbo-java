package i2f.springboot.ops.redis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.ops.common.OpsException;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.data.OpsHomeMenuGroup;
import i2f.springboot.ops.home.provider.IOpsProvider;
import i2f.springboot.ops.redis.DynamicRedisUtil;
import i2f.springboot.ops.redis.data.RedisMeta;
import i2f.springboot.ops.redis.data.RedisOperateDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2025/11/1 23:18
 * @desc
 */
@ConditionalOnClass(RedisTemplate.class)
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/redis")
public class RedisOpsController implements IOpsProvider {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<OpsHomeMenuDto> getMenus() {
        return Collections.singletonList(new OpsHomeMenuDto()
                .title("Redis")
                .subTitle("连接/操作Redis缓存")
                .icon("el-icon-connection")
                .href("./redis/index.html")
                .group(OpsHomeMenuGroup.NoSQL)
        );
    }

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("./index.html").forward(request, response);
    }

    public Map.Entry<RedisTemplate, Boolean> getRedisTemplate(RedisMeta meta) {
        if (Boolean.TRUE.equals(meta.getUseCustom())) {
            if (meta.getHost() != null && !meta.getHost().isEmpty()) {
                return new AbstractMap.SimpleEntry<>(DynamicRedisUtil.getDynamicRedisTemplate(meta), true);
            }
        }
        String[] names = applicationContext.getBeanNamesForType(RedisTemplate.class);
        if (names != null && names.length > 0) {
            for (String name : names) {
                RedisTemplate bean = applicationContext.getBean(name, RedisTemplate.class);
                if (bean != null) {
                    return new AbstractMap.SimpleEntry<>(bean, false);
                }
            }
        }
        throw new OpsException("not found RedisTemplate");
    }


    @PostMapping("/scan")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> scan(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            RedisOperateDto req = transfer.recv(reqDto, RedisOperateDto.class);
            Map.Entry<RedisTemplate, Boolean> connEntry = getRedisTemplate(req.getMeta());
            RedisTemplate redisTemplate = connEntry.getKey();
            try {
                String pattern = req.getPattern();
                Long count = req.getCount();
                ScanOptions.ScanOptionsBuilder builder = ScanOptions.scanOptions();
                if (pattern == null || pattern.isEmpty()) {
                    pattern = "*";
                }
                builder.match(pattern);
                if (count != null && count >= 0) {
                    builder.count(count);
                }
                ScanOptions options = builder.build();
                Cursor<byte[]> cursor = (Cursor<byte[]>) redisTemplate.executeWithStickyConnection((connection) -> {
                    return connection.scan(options);
                });
                List<String> list = new ArrayList<>(300);
                if (cursor != null) {
                    while (cursor.hasNext()) {
                        byte[] item = cursor.next();
                        list.add(new String(item, StandardCharsets.UTF_8));
                    }
                }
                return transfer.success(list);
            } finally {
                if (connEntry.getValue()) {
                    DynamicRedisUtil.destroy(redisTemplate);
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/keys")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> keys(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            RedisOperateDto req = transfer.recv(reqDto, RedisOperateDto.class);
            String pattern = req.getPattern();
            if (pattern == null || pattern.isEmpty()) {
                pattern = "*";
            }
            Map.Entry<RedisTemplate, Boolean> connEntry = getRedisTemplate(req.getMeta());
            RedisTemplate redisTemplate = connEntry.getKey();
            try {
                byte[] rawKey = pattern.getBytes(StandardCharsets.UTF_8);
                Set<byte[]> rawKeys = (Set<byte[]>) redisTemplate.execute((connection) -> {
                    return connection.keys(rawKey);
                }, true);
                List<String> resp = new ArrayList<>(300);
                if (rawKeys != null) {
                    for (byte[] item : rawKeys) {
                        resp.add(new String(item, StandardCharsets.UTF_8));
                    }
                }
                return transfer.success(resp);
            } finally {
                if (connEntry.getValue()) {
                    DynamicRedisUtil.destroy(redisTemplate);
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/get")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> get(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            RedisOperateDto req = transfer.recv(reqDto, RedisOperateDto.class);
            String key = req.getKey();
            if (key == null) {
                throw new OpsException("missing key");
            }
            Map.Entry<RedisTemplate, Boolean> connEntry = getRedisTemplate(req.getMeta());
            RedisTemplate redisTemplate = connEntry.getKey();
            try {
                byte[] rawKey = key.getBytes(StandardCharsets.UTF_8);
                Long ttl = (Long) redisTemplate.execute((connection) -> {
                    try {
                        return connection.pTtl(rawKey, TimeUnit.SECONDS);
                    } catch (Exception var4) {
                        return connection.ttl(rawKey, TimeUnit.SECONDS);
                    }
                }, true);
                RedisOperateDto resp = new RedisOperateDto();
                resp.setKey(key);
                try {
                    byte[] rawValue = (byte[]) redisTemplate.execute((connection) -> {
                        return connection.get(rawKey);
                    }, true);
                    try {
                        resp.setValue(rawValue == null ? null : new String(rawValue, StandardCharsets.UTF_8));
                    } catch (Throwable e) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("value cannot be deserialize!").append("\n");
                        for (int i = 0; i < rawValue.length; i++) {
                            byte bt = rawValue[i];
                            builder.append(String.format("\\x%02x", (bt & 0x0ff)));
                        }
                        resp.setValue(builder.toString());
                    }
                } catch (Throwable e) {
                    Map<byte[], byte[]> map = (Map<byte[], byte[]>) redisTemplate.execute((connection) -> {
                        return connection.hGetAll(rawKey);
                    }, true);
                    if (map == null) {
                        resp.setValue(null);
                    } else {
                        Map<String, String> respMap = new LinkedHashMap<>();
                        for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
                            String keyStr = (entry.getKey() == null ? null : new String(entry.getKey(), StandardCharsets.UTF_8));
                            String valueStr = null;
                            try {
                                valueStr = (entry.getValue() == null ? null : new String(entry.getValue(), StandardCharsets.UTF_8));
                            } catch (Throwable ex) {
                                StringBuilder builder = new StringBuilder();
                                builder.append("value cannot be deserialize!").append("\n");
                                for (int i = 0; i < entry.getValue().length; i++) {
                                    byte bt = entry.getValue()[i];
                                    builder.append(String.format("\\x%02x", (bt & 0x0ff)));
                                }
                                valueStr = builder.toString();
                            }
                            respMap.put(keyStr, valueStr);
                        }
                        resp.setValue(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(respMap));
                    }
                }
                resp.setTtl(ttl);
                return transfer.success(resp);
            } finally {
                if (connEntry.getValue()) {
                    DynamicRedisUtil.destroy(redisTemplate);
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/set")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> set(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            RedisOperateDto req = transfer.recv(reqDto, RedisOperateDto.class);
            String key = req.getKey();
            String value = req.getValue();
            Long ttl = req.getTtl();
            if (key == null) {
                throw new OpsException("missing key");
            }
            Map.Entry<RedisTemplate, Boolean> connEntry = getRedisTemplate(req.getMeta());
            RedisTemplate redisTemplate = connEntry.getKey();
            try {
                TimeUnit unit = TimeUnit.SECONDS;
                byte[] rawKey = key.getBytes(StandardCharsets.UTF_8);
                byte[] rawValue = value == null ? new byte[0] : value.getBytes(StandardCharsets.UTF_8);
                if (ttl != null && ttl >= 0) {
                    long timeout = ttl;
                    redisTemplate.execute(new RedisCallback<Object>() {
                        @Override
                        public Object doInRedis(RedisConnection connection) throws DataAccessException {
                            this.potentiallyUsePsetEx(connection);
                            return null;
                        }

                        public void potentiallyUsePsetEx(RedisConnection connection) {
                            if (!TimeUnit.MILLISECONDS.equals(unit) || !this.failsafeInvokePsetEx(connection)) {
                                connection.setEx(rawKey, TimeoutUtils.toSeconds(timeout, unit), rawValue);
                            }

                        }

                        private boolean failsafeInvokePsetEx(RedisConnection connection) {
                            boolean failed = false;

                            try {
                                connection.pSetEx(rawKey, timeout, rawValue);
                            } catch (UnsupportedOperationException var4) {
                                failed = true;
                            }

                            return !failed;
                        }
                    }, true);

                } else {
                    redisTemplate.execute((connection) -> {
                        return connection.set(rawKey, rawValue);
                    }, true);
                }
                return transfer.success(true);
            } finally {
                if (connEntry.getValue()) {
                    DynamicRedisUtil.destroy(redisTemplate);
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> delete(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            RedisOperateDto req = transfer.recv(reqDto, RedisOperateDto.class);
            Map.Entry<RedisTemplate, Boolean> connEntry = getRedisTemplate(req.getMeta());
            RedisTemplate redisTemplate = connEntry.getKey();
            try {
                List<String> keys = req.getKeys();
                Long resp = (Long) redisTemplate.execute((connection) -> {
                    long ret = 0;
                    for (String key : keys) {
                        byte[] rawKey = key.getBytes(StandardCharsets.UTF_8);
                        Long cnt = connection.del(rawKey);
                        if (cnt != null) {
                            ret += cnt;
                        } else {
                            ret++;
                        }
                    }
                    return ret;
                }, true);
                return transfer.success(resp);
            } finally {
                if (connEntry.getValue()) {
                    DynamicRedisUtil.destroy(redisTemplate);
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/ttl")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> ttl(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            RedisOperateDto req = transfer.recv(reqDto, RedisOperateDto.class);
            String key = req.getKey();
            Long ttl = req.getTtl();
            if (key == null) {
                throw new OpsException("missing key");
            }
            Map.Entry<RedisTemplate, Boolean> connEntry = getRedisTemplate(req.getMeta());
            RedisTemplate redisTemplate = connEntry.getKey();
            try {
                byte[] rawKey = key.getBytes(StandardCharsets.UTF_8);

                Boolean resp = null;
                if (ttl != null && ttl >= 0) {
                    long timeout = ttl;
                    TimeUnit unit = TimeUnit.SECONDS;
                    long rawTimeout = TimeoutUtils.toMillis(ttl, unit);
                    resp = (Boolean) redisTemplate.execute((connection) -> {
                        try {
                            return connection.pExpire(rawKey, rawTimeout);
                        } catch (Exception var8) {
                            return connection.expire(rawKey, TimeoutUtils.toSeconds(timeout, unit));
                        }
                    }, true);
                } else {
                    resp = (Boolean) redisTemplate.execute((connection) -> {
                        return connection.persist(rawKey);
                    }, true);
                }
                return transfer.success(resp);
            } finally {
                if (connEntry.getValue()) {
                    DynamicRedisUtil.destroy(redisTemplate);
                }
            }
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }
}
