package i2f.springboot.ops.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.extension.groovy.GroovyScript;
import i2f.springboot.ops.app.data.AppOperationDto;
import i2f.springboot.ops.common.*;
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
import java.util.HashMap;
import java.util.Map;
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
public class AppEvalOpsController {
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
            return transfer.error(e);
        }
    }

}
