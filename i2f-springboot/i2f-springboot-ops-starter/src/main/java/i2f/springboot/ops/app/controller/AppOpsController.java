package i2f.springboot.ops.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.extension.groovy.GroovyScript;
import i2f.springboot.ops.app.data.AppKeyValueItemDto;
import i2f.springboot.ops.app.data.AppOperationDto;
import i2f.springboot.ops.app.data.metadata.AppClassDto;
import i2f.springboot.ops.app.data.service.AppServiceInstanceDto;
import i2f.springboot.ops.app.data.thread.AppThreadInfoDto;
import i2f.springboot.ops.app.util.AppUtil;
import i2f.springboot.ops.common.*;
import i2f.springboot.ops.host.data.HostOperateDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HostIdHelper hostIdHelper;

    public void assertHostId(AppOperationDto req) {
        if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
            throw new OpsException("request not equals require hostId");
        }
    }

    @RequestMapping("/")
    public RedirectView index() {
        return new RedirectView("./index.html");
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
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
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
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
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
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
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
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

    @PostMapping("/service-instances")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> serviceInstances(@RequestBody OpsSecureDto reqDto,
                                                          HttpServletRequest request) throws Exception {
        try {
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            List<AppServiceInstanceDto> resp = new ArrayList<>();
            DiscoveryClient client = applicationContext.getBean(DiscoveryClient.class);
            List<String> services = client.getServices();
            for (String service : services) {
                List<ServiceInstance> instances = client.getInstances(service);
                for (ServiceInstance instance : instances) {
                    resp.add(AppServiceInstanceDto.of(instance));
                }
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
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
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
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
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
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
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
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

    @PostMapping("/eval")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> eval(@RequestBody OpsSecureDto reqDto,
                                              HttpServletRequest request) throws Exception {
        try {
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            String script = req.getScript();
            long waitForSeconds = req.getWaitForSeconds();
            if (waitForSeconds < 0) {
                waitForSeconds = 120;
            }
            if (waitForSeconds >= 500) {
                waitForSeconds = 500;
            }
            AtomicReference<Object> refRet = new AtomicReference<>();
            AtomicReference<Throwable> refEx = new AtomicReference<>();
            CountDownLatch latch = new CountDownLatch(1);
            Runnable task = () -> {
                try {

                    Map<String, Object> context = new HashMap<>();
                    context.put("context", applicationContext);
                    context.put("env", applicationContext.getEnvironment());
                    Map<String, Object> beanMap = new HashMap<>();
                    String[] names = applicationContext.getBeanDefinitionNames();
                    for (String name : names) {
                        Object bean = applicationContext.getBean(name);
                        beanMap.put(name, bean);
                    }
                    context.put("beanMap", beanMap);
                    Object eval = GroovyScript.eval(script, context);
                    refRet.set(eval);
                } catch (Throwable e) {
                    refEx.set(e);
                } finally {
                    latch.countDown();
                }
            };
            new Thread(task).start();
            try {
                if (waitForSeconds >= 0) {
                    latch.await(waitForSeconds, TimeUnit.SECONDS);
                } else {
                    latch.await();
                }
            } catch (InterruptedException e) {

            }
            Throwable throwable = refEx.get();
            if (throwable != null) {
                throw throwable;
            }

            Object ret = refRet.get();
            String resp = null;
            try {
                if (ret instanceof CharSequence
                        || ret instanceof Appendable
                        || ret instanceof Number) {
                    resp = String.valueOf(ret);
                } else {
                    resp = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ret);
                }
            } catch (Exception e) {
                try {
                    resp = String.valueOf(ret);
                } catch (Exception ex) {
                    resp = "response value cannot serialize as json: " + (ret.getClass().getName());
                }
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e.getClass().getSimpleName() + ":" + e.getMessage());
        }
    }

}
