package i2f.springboot.ops.app.controller;

import com.xxl.job.core.handler.IJobHandler;
import i2f.springboot.ops.app.data.AppKeyValueItemDto;
import i2f.springboot.ops.app.data.AppThreadInfoDto;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/11/8 17:55
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/app")
public class AppOpsController {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private ApplicationContext applicationContext;

    @PostMapping("/system-properties")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> systemProperties(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            Long req = transfer.recv(reqDto, Long.class);
            List<AppKeyValueItemDto> resp=new ArrayList<>();
            Properties properties = System.getProperties();
            Set<String> set = properties.stringPropertyNames();
            for (String key : set) {
                String value = properties.getProperty(key);
                AppKeyValueItemDto dto=new AppKeyValueItemDto(key,value);
                resp.add(dto);
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/input-arguments")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> inputArguments(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            Long req = transfer.recv(reqDto, Long.class);
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            List<String> resp = runtimeMXBean.getInputArguments();
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/locked-threads")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> lockedThreads(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            Long req = transfer.recv(reqDto, Long.class);
            List<AppThreadInfoDto> resp = new ArrayList<>();
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
            ThreadInfo[] threadInfo = threadMXBean.getThreadInfo(deadlockedThreads);
            for (ThreadInfo info : threadInfo) {
                AppThreadInfoDto dto = AppThreadInfoDto.of(info);
                resp.add(dto);
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

}
