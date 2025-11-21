package i2f.springboot.ops.xxljob.controller;

import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.impl.MethodJobHandler;
import i2f.springboot.ops.common.*;
import i2f.springboot.ops.xxljob.data.XxlJobOperateDto;
import i2f.typeof.TypeOf;
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

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2025/11/8 16:37
 * @desc
 */
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/xxl-job")
public class XxlJobOpsController {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private HostIdHelper hostIdHelper;

    public void assertHostId(XxlJobOperateDto req) {
        if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
            throw new OpsException("request not equals require hostId");
        }
    }

    private final AtomicBoolean init = new AtomicBoolean(false);
    private final Map<String, IJobHandler> jobMap = new ConcurrentHashMap<>();

    public Map<String, IJobHandler> getJobMap() {
        if (init.getAndSet(true)) {
            return jobMap;
        }
        Map<String, IJobHandler> handlerMap = getXxlJobHandlerMap();
        jobMap.putAll(handlerMap);
        init.set(true);
        return jobMap;
    }

    public Map<String, IJobHandler> getXxlJobHandlerMap() {
        Map<String, IJobHandler> ret = new LinkedHashMap<>();
        Class<?> clazz = XxlJobExecutor.class;
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                Class<?> type = field.getType();
                if (!TypeOf.typeOf(type, Map.class)) {
                    continue;
                }
                field.setAccessible(true);
                Map<?, ?> map = (Map<?, ?>) field.get(null);
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    Object value = entry.getValue();
                    if (value instanceof IJobHandler) {
                        ret.put(String.valueOf(entry.getKey()), (IJobHandler) value);
                    }
                }
            }
        } catch (Exception e) {

        }
        return ret;
    }

    @PostMapping("/jobs")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> jobs(@RequestBody OpsSecureDto reqDto,
                                              HttpServletRequest request) throws Exception {
        try {
            XxlJobOperateDto req = transfer.recv(reqDto, XxlJobOperateDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            Map<String, IJobHandler> map = getJobMap();
            List<String> resp = new ArrayList<>(map.keySet());
            resp.sort((v1, v2) -> v1.toLowerCase().compareTo(v2.toLowerCase()));
            return transfer.success(resp);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e.getMessage());
        }
    }

    @PostMapping("/run")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> run(@RequestBody OpsSecureDto reqDto,
                                             HttpServletRequest request) throws Exception {
        try {
            XxlJobOperateDto req = transfer.recv(reqDto, XxlJobOperateDto.class);
            if (!hostIdHelper.canAcceptHostId(req.getHostId())) {
                if (req.isProxyHostId()) {
                    return hostIdHelper.proxyHostId(req, req.getHostId(), request);
                }
            }
            assertHostId(req);
            Boolean async = req.getAsync();
            if (async == null) {
                async = false;
            }
            Long maxAwaitSeconds = req.getWaitForSeconds();
            if (maxAwaitSeconds == null) {
                maxAwaitSeconds = -1L;
            }
            AtomicReference<Object> refRet = new AtomicReference<>();
            AtomicReference<Throwable> refEx = new AtomicReference<>();
            CountDownLatch latch = new CountDownLatch(1);
            Runnable task = () -> {
                IJobHandler handler = null;
                try {
                    String method = req.getMethod();
                    String param = req.getParam();
                    if (method == null) {
                        method = "";
                    }
                    method = method.trim();
                    Map<String, IJobHandler> map = getJobMap();
                    handler = map.get(method);
                    if (handler == null) {
                        throw new OpsException("not found job handler");
                    }

                    handler.init();

                    XxlJobContext context = new XxlJobContext(-1, param, "ops-" + method + ".log", 0, 1);
                    XxlJobContext.setXxlJobContext(context);

                    if (handler instanceof MethodJobHandler) {
                        MethodJobHandler methodJobHandler = (MethodJobHandler) handler;

                        Field fieldMethod = null;
                        Method javaMethod = null;

                        Field fieldTarget = null;
                        Object invokeTarget = null;
                        try {
                            fieldMethod = MethodJobHandler.class.getDeclaredField("method");
                            fieldMethod.setAccessible(true);
                            javaMethod = (Method) fieldMethod.get(methodJobHandler);
                            if (!Modifier.isPublic(javaMethod.getModifiers())) {
                                javaMethod.setAccessible(true);
                            }
                        } catch (Exception e) {

                        }

                        try {
                            fieldTarget = MethodJobHandler.class.getDeclaredField("target");
                            fieldTarget.setAccessible(true);
                            invokeTarget = fieldTarget.get(methodJobHandler);
                        } catch (Exception e) {

                        }

                        Class<?>[] parameterTypes = javaMethod.getParameterTypes();
                        if (fieldMethod != null
                                && javaMethod != null
                                && fieldTarget != null) {
                            if (parameterTypes.length > 0
                                    && TypeOf.typeOf(parameterTypes[0], CharSequence.class)) {
                                Object ret = javaMethod.invoke(invokeTarget, param);
                                refRet.set(ret);
                            } else {
                                Object ret = javaMethod.invoke(invokeTarget);
                                refRet.set(ret);
                            }
                        } else {
                            methodJobHandler.execute();
                        }
                    } else {
                        handler.execute();
                    }
                } catch (Throwable ex) {
                    refEx.set(ex);
                } finally {
                    if (handler != null) {
                        try {
                            handler.destroy();
                        } catch (Exception ex) {
                            refEx.set(ex);
                        }
                    }
                    XxlJobContext.setXxlJobContext(null);
                    latch.countDown();
                }
            };
            if (async) {
                new Thread(task).start();
                if (maxAwaitSeconds >= 0) {
                    latch.await(maxAwaitSeconds, TimeUnit.SECONDS);
                }
            } else {
                task.run();
            }
            Throwable throwable = refEx.get();
            if (throwable != null) {
                throw throwable;
            }
            return transfer.success(refRet.get());
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e.getMessage());
        }
    }

}
