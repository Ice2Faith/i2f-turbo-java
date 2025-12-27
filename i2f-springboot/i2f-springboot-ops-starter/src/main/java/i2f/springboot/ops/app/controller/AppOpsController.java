package i2f.springboot.ops.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.ops.app.data.AppKeyValueItemDto;
import i2f.springboot.ops.app.data.AppOperationDto;
import i2f.springboot.ops.app.data.logging.AppLoggingDto;
import i2f.springboot.ops.app.data.metadata.AppClassDto;
import i2f.springboot.ops.app.data.thread.AppThreadInfoDto;
import i2f.springboot.ops.app.util.AppUtil;
import i2f.springboot.ops.common.*;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.provider.IOpsProvider;
import i2f.springboot.ops.host.data.HostOperateDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;
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
public class AppOpsController implements IOpsProvider {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HostIdHelper hostIdHelper;

    @Override
    public List<OpsHomeMenuDto> getMenus() {
        return Collections.singletonList(new OpsHomeMenuDto()
                .title("Application")
                .subTitle("获取应用信息与执行表达式脚本")
                .icon("el-icon-mobile")
                .href("./app/index.html")
        );
    }

    public void assertHostId(AppOperationDto req) {
        if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
            throw new OpsException("request not equals require hostId");
        }
    }

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("./index.html").forward(request, response);
    }

    @PostMapping("/hostId")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> hostId(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            HostOperateDto req = transfer.recv(reqDto, HostOperateDto.class);
            String hostIp = hostIdHelper.getHostId();
            return transfer.success(hostIp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/logging-level/set")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> loggingLevelSet(@RequestBody OpsSecureDto reqDto,
                                                         HttpServletRequest request) throws Exception {
        try {
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            AppLoggingDto conf = req.getLogging();

            LogLevel level = null;
            for (LogLevel item : LogLevel.values()) {
                if (item.name().equalsIgnoreCase(conf.getLevel())) {
                    level = item;
                    break;
                }
            }
            LoggingSystem bean = applicationContext.getBean(LoggingSystem.class);
            bean.setLogLevel(conf.getLocation(), level);
            LoggerConfiguration cfg = bean.getLoggerConfiguration(conf.getLocation());
            AppLoggingDto resp = new AppLoggingDto();
            if (cfg != null) {
                resp.setLocation(cfg.getName());
                resp.setLevel(String.valueOf(cfg.getConfiguredLevel()));
                resp.setEffectLevel(String.valueOf(cfg.getEffectiveLevel()));
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/system-properties")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> systemProperties(@RequestBody OpsSecureDto reqDto,
                                                          HttpServletRequest request) throws Exception {
        try {
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            List<AppKeyValueItemDto> resp = new ArrayList<>();
            Properties properties = System.getProperties();
            Set<String> set = properties.stringPropertyNames();
            for (String key : set) {
                String value = properties.getProperty(key);
                AppKeyValueItemDto dto = new AppKeyValueItemDto(key, value);
                resp.add(dto);
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/input-arguments")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> inputArguments(@RequestBody OpsSecureDto reqDto,
                                                        HttpServletRequest request) throws Exception {
        try {
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            List<String> resp = runtimeMXBean.getInputArguments();
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/system-env")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> systemEnv(@RequestBody OpsSecureDto reqDto,
                                                   HttpServletRequest request) throws Exception {
        try {
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            List<AppKeyValueItemDto> resp = new ArrayList<>();
            Map<String, String> envMap = System.getenv();
            for (Map.Entry<String, String> entry : envMap.entrySet()) {
                AppKeyValueItemDto dto = new AppKeyValueItemDto(entry.getKey(), entry.getValue());
                resp.add(dto);
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/class-metadata")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> classMetadata(@RequestBody OpsSecureDto reqDto,
                                                       HttpServletRequest request) throws Exception {
        try {
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            String className = req.getClassName();
            Class<?> clazz = AppUtil.loadClass(className);
            if (clazz == null) {
                throw new ClassNotFoundException(className);
            }
            AppClassDto resp = AppClassDto.of(clazz);

            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/locked-threads")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> lockedThreads(@RequestBody OpsSecureDto reqDto,
                                                       HttpServletRequest request) throws Exception {
        try {
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            List<AppThreadInfoDto> resp = new ArrayList<>();
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
            if (deadlockedThreads != null) {
                ThreadInfo[] threadInfo = threadMXBean.getThreadInfo(deadlockedThreads);
                if (threadInfo != null) {
                    for (ThreadInfo info : threadInfo) {
                        AppThreadInfoDto dto = AppThreadInfoDto.of(info);
                        resp.add(dto);
                    }
                }
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping("/beans")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> beans(@RequestBody OpsSecureDto reqDto,
                                               HttpServletRequest request) throws Exception {
        try {
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            List<AppKeyValueItemDto> resp = new ArrayList<>();
            String[] names = applicationContext.getBeanDefinitionNames();
            for (String name : names) {
                Object bean = applicationContext.getBean(name);
                Class<?> clazz = bean.getClass();
                resp.add(new AppKeyValueItemDto(name, unescapeEnhancerClassName(clazz.getName())));
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    public static String unescapeEnhancerClassName(String className) {
        if (className == null) {
            return null;
        }
        int idx = className.lastIndexOf("$EnhancerBySpring");
        if (idx >= 0) {
            return className.substring(0, idx);
        }
        return className;
    }

}
