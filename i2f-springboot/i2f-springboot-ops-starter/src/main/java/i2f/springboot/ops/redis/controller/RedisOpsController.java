package i2f.springboot.ops.redis.controller;

import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.redis.data.RedisOperateDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2025/11/1 23:18
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/redis")
public class RedisOpsController {
    @Autowired(required = false)
    private RedisTemplate redisTemplate;

    @Autowired
    protected OpsSecureTransfer transfer;

    @PostMapping("/scan")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> scan(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            RedisOperateDto req = transfer.recv(reqDto, RedisOperateDto.class);
            String pattern = req.getPattern();
            Long count = req.getCount();
            ScanOptions.ScanOptionsBuilder builder = ScanOptions.scanOptions();
            if(pattern==null || pattern.isEmpty()) {
               pattern="*";
            }
            builder.match(pattern);
            if(count!=null && count>=0){
                builder.count(count);
            }
            Cursor cursor = redisTemplate.scan(builder.build());
            List<String> list=new ArrayList<>(300);
            while(cursor.hasNext()){
                Object item = cursor.next();
                list.add(String.valueOf(item));
            }
            return transfer.success(list);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/keys")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> keys(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            RedisOperateDto req = transfer.recv(reqDto, RedisOperateDto.class);
            String pattern = req.getPattern();
            if(pattern==null || pattern.isEmpty()) {
                pattern="*";
            }
            Set keys = redisTemplate.keys(pattern);
            return transfer.success(keys);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/get")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> get(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            RedisOperateDto req = transfer.recv(reqDto, RedisOperateDto.class);
            String key = req.getKey();
            Object result = redisTemplate.opsForValue().get(key);
            Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            RedisOperateDto resp=new RedisOperateDto();
            resp.setKey(key);
            resp.setValue(String.valueOf(result));
            resp.setTtl(ttl);
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
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
            if(ttl!=null && ttl>=0){
                redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.SECONDS);
            }else{
                redisTemplate.opsForValue().set(key, value);
            }
            return transfer.success(true);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> delete(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            RedisOperateDto req = transfer.recv(reqDto, RedisOperateDto.class);
            List<String> keys = req.getKeys();
            Long resp = redisTemplate.delete(keys);
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/ttl")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> ttl(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            RedisOperateDto req = transfer.recv(reqDto, RedisOperateDto.class);
            String key = req.getKey();
            Long ttl = req.getTtl();
            Boolean resp=null;
            if(ttl!=null && ttl>=0){
                resp=redisTemplate.expire(key, ttl, TimeUnit.SECONDS);
            }else{
                resp=redisTemplate.persist(key);
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }
}
