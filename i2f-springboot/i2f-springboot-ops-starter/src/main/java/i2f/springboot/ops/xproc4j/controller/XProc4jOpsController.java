package i2f.springboot.ops.xproc4j.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.ai.std.tool.ToolCallContextHolder;
import i2f.springboot.ops.common.*;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.data.OpsHomeMenuGroup;
import i2f.springboot.ops.home.provider.IOpsProvider;
import i2f.springboot.ops.host.data.HostOperateDto;
import i2f.springboot.ops.xproc4j.data.XProc4jOperateDto;
import i2f.springboot.ops.xproc4j.helper.XProc4jHelper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author Ice2Faith
 * @date 2025/12/5 9:30
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.ops.xproc4j.enable:true}")
@Data
@NoArgsConstructor
@Slf4j
@Controller
@RequestMapping(OpsConsts.SPEL_BASE_URL + "/xproc4j")
public class XProc4jOpsController implements IOpsProvider {
    public static final String ATTR_LOG = "log";

    @Autowired
    private XProc4jHelper xProc4jHelper;

    @Autowired
    private HostIdHelper hostIdHelper;

    @Autowired
    private HostIdProxyHelper hostIdProxyHelper;

    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    protected ObjectMapper objectMapper;

    @Override
    public List<OpsHomeMenuDto> getMenus() {
        try {
            Object bean = xProc4jHelper.getExecutor();
            if (bean != null) {
                return Collections.singletonList(new OpsHomeMenuDto()
                        .title("XProc4J")
                        .subTitle("执行 XProc4J 脚本")
                        .icon("el-icon-table-lamp")
                        .href("./xproc4j/index.html")
                        .group(OpsHomeMenuGroup.Component)
                );
            }
        } catch (Exception e) {

        }
        return Collections.emptyList();
    }

    @RequestMapping({"/"})
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


    @PostMapping({"/datasources"})
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> datasources(@RequestBody OpsSecureDto reqDto,
                                                     HttpServletRequest request) throws Exception {
        try {
            XProc4jOperateDto req = transfer.recv(reqDto, XProc4jOperateDto.class);
            List<String> list = xProc4jHelper.getDatasources();
            return transfer.success(list);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping({"/metas"})
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> metas(@RequestBody OpsSecureDto reqDto,
                                               HttpServletRequest request) throws Exception {
        try {
            XProc4jOperateDto req = transfer.recv(reqDto, XProc4jOperateDto.class);
            Map<String, Map<String, Object>> metasMap = xProc4jHelper.getMetasMap();
            List<Map<String, Object>> list = new ArrayList<>();
            for (String key : metasMap.keySet()) {
                list.add(metasMap.get(key));
            }
            return transfer.success(list);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

    @PostMapping({"/call"})
    public SseEmitter call(@RequestBody OpsSecureDto reqDto,
                           HttpServletRequest request) throws Exception {
        SseEmitter emitter = new SseEmitter(TimeUnit.MINUTES.toMillis(5));
        new Thread(() -> {
            try {
                XProc4jOperateDto req = transfer.recv(reqDto, XProc4jOperateDto.class);
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
                    try {
                        xProc4jHelper.debugThread(req.getEnableDebug());
                        if (req.getEnableLog() != null && req.getEnableLog()) {
                            xProc4jHelper.applyThreadLogAppender((str) -> {
                                try {
                                    OpsSecureReturn<?> opsResp = OpsSecureReturn.success(str);
                                    opsResp.withAttr("type", ATTR_LOG);
                                    String respJson = objectMapper.writeValueAsString(opsResp);

                                    emitter.send(respJson);
                                } catch (Exception e) {

                                }
                            });
                        }
                        String procedureId = req.getProcedureId();
                        Map<String, Object> params = req.getParams();
                        Map<String, Object> map = xProc4jHelper.call(procedureId, params);
                        xProc4jHelper.trimContextParams(map);
                        refRet.set(map);
                    } catch (Throwable e) {
                        refEx.set(e);
                    } finally {
                        try {
                            xProc4jHelper.debugThread(null);
                            xProc4jHelper.applyThreadLogAppender(null);
                        } catch (Exception e) {

                        }
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
                } catch (Throwable e) {
                    try {
                        resp = String.valueOf(ret);
                    } catch (Throwable ex) {
                        resp = "response value cannot serialize as json: " + (ret.getClass().getName());
                    }
                }

                OpsSecureReturn<?> opsResp = transfer.success(resp);
                String respJson = objectMapper.writeValueAsString(opsResp);

                emitter.send(respJson);

                emitter.complete();
            } catch (Throwable e) {
                try {
                    log.warn(e.getMessage(), e);
                    OpsSecureReturn<?> opsResp = transfer.error(e);
                    String respJson = objectMapper.writeValueAsString(opsResp);

                    emitter.send(respJson);

                    emitter.complete();
                } catch (Exception ex) {
                    emitter.completeWithError(ex);
                }
            }
        }).start();

        emitter.onCompletion(() -> {
            // System.out.println("前端 SSE 连接已正常关闭");
        });
        emitter.onTimeout(() -> {
            ToolCallContextHolder.clear();
            System.out.println("前端 SSE 连接超时");
        });
        emitter.onError((ex) -> {
            ToolCallContextHolder.clear();
            System.err.println("前端 SSE 发生错误: " + ex.getMessage());
        });

        return emitter;
    }

    @PostMapping({"/eval-script"})
    @ResponseBody
    public SseEmitter evalScript(@RequestBody OpsSecureDto reqDto,
                                 HttpServletRequest request) throws Exception {
        SseEmitter emitter = new SseEmitter(TimeUnit.MINUTES.toMillis(5));
        new Thread(() -> {
            try {
                XProc4jOperateDto req = transfer.recv(reqDto, XProc4jOperateDto.class);
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
                    try {
                        xProc4jHelper.debugThread(req.getEnableDebug());
                        if (req.getEnableLog() != null && req.getEnableLog()) {
                            xProc4jHelper.applyThreadLogAppender((str) -> {
                                try {
                                    OpsSecureReturn<?> opsResp = OpsSecureReturn.success(str);
                                    opsResp.withAttr("type", ATTR_LOG);
                                    String respJson = objectMapper.writeValueAsString(opsResp);

                                    emitter.send(respJson);
                                } catch (Exception e) {

                                }
                            });
                        }
                        String lang = req.getLang();
                        String script = req.getScript();
                        Map<String, Object> params = req.getParams();
                        Object obj = xProc4jHelper.evalScript(lang, script, params);
                        refRet.set(obj);
                    } catch (Throwable e) {
                        refEx.set(e);
                    } finally {
                        try {
                            xProc4jHelper.debugThread(null);
                            xProc4jHelper.applyThreadLogAppender(null);
                        } catch (Exception e) {

                        }
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
                Object ret = refRet.get();
                if (ret instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) ret;
                    xProc4jHelper.trimContextParams(map);
                }
                String resp = null;
                try {
                    if (ret instanceof CharSequence
                            || ret instanceof Appendable
                            || ret instanceof Number) {
                        resp = String.valueOf(ret);
                    } else {
                        resp = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ret);
                    }
                } catch (Throwable e) {
                    try {
                        resp = String.valueOf(ret);
                    } catch (Throwable ex) {
                        resp = "response value cannot serialize as json: " + (ret.getClass().getName());
                    }
                }

                OpsSecureReturn<?> opsResp = transfer.success(resp);
                String respJson = objectMapper.writeValueAsString(opsResp);

                emitter.send(respJson);

                emitter.complete();
            } catch (Throwable e) {
                try {
                    log.warn(e.getMessage(), e);
                    OpsSecureReturn<?> opsResp = transfer.error(e);
                    String respJson = objectMapper.writeValueAsString(opsResp);

                    emitter.send(respJson);

                    emitter.complete();
                } catch (Exception ex) {
                    emitter.completeWithError(ex);
                }
            }
        }).start();

        emitter.onCompletion(() -> {
            // System.out.println("前端 SSE 连接已正常关闭");
        });
        emitter.onTimeout(() -> {
            ToolCallContextHolder.clear();
            System.out.println("前端 SSE 连接超时");
        });
        emitter.onError((ex) -> {
            ToolCallContextHolder.clear();
            System.err.println("前端 SSE 发生错误: " + ex.getMessage());
        });

        return emitter;
    }
}
