package i2f.springboot.ops.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.job.core.handler.IJobHandler;
import i2f.extension.groovy.GroovyScript;
import i2f.springboot.ops.app.data.AppKeyValueItemDto;
import i2f.springboot.ops.app.data.AppOperationDto;
import i2f.springboot.ops.app.data.AppThreadInfoDto;
import i2f.springboot.ops.common.*;
import i2f.springboot.ops.host.data.HostOperateDto;
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

    public void assertHostId(AppOperationDto req){
        String reqHostId = req.getHostId();
        if(reqHostId!=null && !reqHostId.isEmpty()){
            String currHostId = hostIdHelper.getHostIp();
            if(!Objects.equals(currHostId,reqHostId)){
                throw new OpsException("request not equals require hostId");
            }
        }
    }

    @PostMapping("/system-properties")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> systemProperties(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
            assertHostId(req);
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
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
            assertHostId(req);
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
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
            assertHostId(req);
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

    @PostMapping("/beans")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> beans(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
            assertHostId(req);
            List<AppKeyValueItemDto> resp=new ArrayList<>();
            String[] names = applicationContext.getBeanDefinitionNames();
            for (String name : names) {
                Object bean = applicationContext.getBean(name);
                Class<?> clazz = bean.getClass();
                resp.add(new AppKeyValueItemDto(name,unescapeEnhancerClassName(clazz.getName())));
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

    public static String unescapeEnhancerClassName(String className){
        if(className==null){
            return null;
        }
        int idx=className.lastIndexOf("$EnhancerBySpring");
        if(idx>=0){
            return className.substring(0,idx);
        }
        return className;
    }

    @PostMapping("/eval")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> eval(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            AppOperationDto req = transfer.recv(reqDto, AppOperationDto.class);
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
            AtomicReference<Throwable> refEx=new AtomicReference<>();
            CountDownLatch latch = new CountDownLatch(1);
            Runnable task=()-> {
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
                }catch (Throwable e){
                    refEx.set(e);
                }finally {
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
            if(throwable!=null){
                throw throwable;
            }

            Object ret = refRet.get();
            String resp=null;
            try{
                resp=objectMapper.writeValueAsString(ret);
            }catch(Exception e){
                resp="response value cannot serialize as json";
            }
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(),e);
            return transfer.error(e.getMessage());
        }
    }

}
