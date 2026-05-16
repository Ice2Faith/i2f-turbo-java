package i2f.turbo.idea.plugin.jdbc.procedure.debugger;

import com.intellij.debugger.PositionManager;
import com.intellij.debugger.PositionManagerFactory;
import com.intellij.debugger.engine.DebugProcess;
import com.intellij.debugger.engine.DebugProcessListener;
import com.intellij.debugger.ui.breakpoints.JavaMethodBreakpointType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.breakpoints.*;
import com.intellij.xdebugger.impl.breakpoints.XBreakpointUtil;
import i2f.turbo.idea.plugin.utils.PsiBreakpointUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.java.debugger.breakpoints.properties.JavaMethodBreakpointProperties;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2026/5/15
 * @desc
 */
public class JdbcProcedurePositionManagerFactory extends PositionManagerFactory {
    public static final Logger log = Logger.getInstance(JdbcProcedurePositionManagerFactory.class);


    @Nullable
    @Override
    public PositionManager createPositionManager(@NotNull DebugProcess process) {
        // log.warn("createPositionManager,process=" + process);
        JdbcProcedurePositionManager manager = new JdbcProcedurePositionManager(process);
        setupBreakpointBridge(process);
        return manager;
    }

    private void setupBreakpointBridge(@NotNull DebugProcess process) {
        Project project = process.getProject();

        process.addDebugProcessListener(new DebugProcessListener() {

            private final AtomicReference<XLineBreakpoint<JavaMethodBreakpointProperties>> managedBreakpointRef
                    = new AtomicReference<>();


            private volatile MessageBusConnection messageBusConnection;


            private volatile String cachedFileUrl;
            private volatile int cachedLine = -1;

            @Override
            public void processAttached(@NotNull DebugProcess dp) {
                // log.warn("BreakpointBridge: processAttached");
                ApplicationManager.getApplication().invokeLater(() -> {
                    if (!resolveReportMethodLocation(project)) {
                        // log.warn("BreakpointBridge: Bridge.report() not found, bridge disabled");
                        return;
                    }

                    refreshManagedBreakpoint(project);

                    messageBusConnection = project.getMessageBus().connect();
                    messageBusConnection.subscribe(XBreakpointListener.TOPIC, new XBreakpointListener<XBreakpoint<?>>() {
                        @Override
                        public void breakpointAdded(@NotNull XBreakpoint<?> breakpoint) {
                            if (instanceOfHookBreakpointType(breakpoint)) {
                                // log.warn("BreakpointBridge: .fic breakpoint added");
                                refreshManagedBreakpoint(project);
                            }
                        }

                        @Override
                        public void breakpointRemoved(@NotNull XBreakpoint<?> breakpoint) {
                            if (instanceOfHookBreakpointType(breakpoint)) {
                                // log.warn("BreakpointBridge: .fic breakpoint removed");
                                refreshManagedBreakpoint(project);
                            }
                        }

                        @Override
                        public void breakpointChanged(@NotNull XBreakpoint<?> breakpoint) {
                            if (instanceOfHookBreakpointType(breakpoint)) {
                                // log.warn("BreakpointBridge: .fic breakpoint changed");
                                refreshManagedBreakpoint(project);
                            }
                        }
                    });
                });
            }

            @Override
            public void processDetached(@NotNull DebugProcess dp, boolean closedByUser) {
                // log.warn("BreakpointBridge: processDetached");
                if (messageBusConnection != null) {
                    messageBusConnection.disconnect();
                    messageBusConnection = null;
                }
                XLineBreakpoint<JavaMethodBreakpointProperties> bp = managedBreakpointRef.getAndSet(null);
                if (bp != null) {
                    ApplicationManager.getApplication().invokeLater(() -> {
                        ApplicationManager.getApplication().runWriteAction(() -> {
                            try {
                                XDebuggerManager.getInstance(project).getBreakpointManager().removeBreakpoint(bp);
                                // log.warn("BreakpointBridge: managed breakpoint removed");
                            } catch (Exception e) {
                                // log.warn("BreakpointBridge: failed to remove managed breakpoint: " + e.getMessage());
                            }
                        });
                    });
                }
            }

            private boolean resolveReportMethodLocation(Project project) {
                return ApplicationManager.getApplication().runReadAction(
                        (com.intellij.openapi.util.Computable<Boolean>) () -> {
                            List<PsiClass> classes = PsiBreakpointUtil.getPsiClass(project, JdbcProcedureDebugConsts.SIMPLE_CLASS_NAME);
                            if (classes.isEmpty()) {
                                return false;
                            }

                            for (PsiClass psiClass : classes) {
                                List<PsiMethod> methods = PsiBreakpointUtil.getPsiMethod(psiClass, JdbcProcedureDebugConsts.METHOD_NAME, JdbcProcedureDebugConsts.PARAM_COUNT);
                                for (PsiMethod method : methods) {
                                    PsiFile file = method.getContainingFile();
                                    if (file == null) {
                                        continue;
                                    }
                                    VirtualFile vf = file.getVirtualFile();
                                    if (vf == null) {
                                        continue;
                                    }

                                    int line = file.getFileDocument().getLineNumber(method.getTextOffset());

                                    cachedFileUrl = vf.getUrl();
                                    cachedLine = line;
                                    // log.warn("BreakpointBridge: resolved report() at " + cachedFileUrl + ":" + cachedLine);
                                    return true;
                                }
                            }
                            return false;
                        });
            }

            private void refreshManagedBreakpoint(Project project) {
                if (cachedFileUrl == null || cachedLine < 0) {
                    return;
                }

                String condition = buildConditionFromHookBreakpoints(project);
                // log.warn("BreakpointBridge: condition = " + condition);

                ApplicationManager.getApplication().runWriteAction(() -> {
                    XBreakpointManager bpManager = XDebuggerManager.getInstance(project).getBreakpointManager();

                    if (condition == null || condition.isEmpty()) {
                        XLineBreakpoint<JavaMethodBreakpointProperties> bp = managedBreakpointRef.getAndSet(null);
                        if (bp != null) {
                            bpManager.removeBreakpoint(bp);
                            // log.warn("BreakpointBridge: removed managed method breakpoint (no .fic breakpoints)");
                        }
                        return;
                    }

                    XLineBreakpoint<JavaMethodBreakpointProperties> existing = managedBreakpointRef.get();
                    if (existing != null) {
                        existing.setCondition(condition);
                        // log.warn("BreakpointBridge: updated condition on existing method breakpoint");
                    } else {
                        JavaMethodBreakpointType javaMethodType =
                                (JavaMethodBreakpointType) XBreakpointUtil.findType("java-method");
                        if (javaMethodType == null) {
                            // log.warn("BreakpointBridge: JavaMethodBreakpointType not found");
                            return;
                        }

                        JavaMethodBreakpointProperties props = new JavaMethodBreakpointProperties();
                        XLineBreakpoint<JavaMethodBreakpointProperties> newBp =
                                bpManager.addLineBreakpoint(javaMethodType, cachedFileUrl, cachedLine, props);
                        if (newBp != null) {
                            newBp.setCondition(condition);
                            newBp.setSuspendPolicy(SuspendPolicy.THREAD);
                            managedBreakpointRef.set(newBp);
                            // log.warn("BreakpointBridge: created managed method breakpoint at " + cachedFileUrl + ":" + cachedLine);
                        }
                    }
                });
            }

            @Nullable
            private String buildConditionFromHookBreakpoints(Project project) {
                XBreakpointManager bpManager = XDebuggerManager.getInstance(project).getBreakpointManager();
                StringBuilder sb = new StringBuilder();

                for (XBreakpoint<?> bp : bpManager.getAllBreakpoints()) {
                    if (!instanceOfHookBreakpointType(bp)) {
                        continue;
                    }
                    if (!bp.isEnabled()) {
                        continue;
                    }
                    if (!(bp instanceof XLineBreakpoint)) {
                        continue;
                    }

                    XLineBreakpoint<?> lineBp = (XLineBreakpoint<?>) bp;
                    String fileUrl = lineBp.getFileUrl();
                    int line = lineBp.getLine() + 1;

                    String fileName = fileUrl;
                    int lastSlash = fileUrl.lastIndexOf('/');
                    if (lastSlash >= 0) {
                        fileName = fileUrl.substring(lastSlash + 1);
                    }

                    if (sb.length() > 0) {
                        sb.append("\n || ");
                    }

                    sb.append("(\"")
                            .append(escapeJava(fileName))
                            .append("\".equals(this.fileName) && this.lineNumber == ")
                            .append(line)
                            .append(")");
                }

                return sb.length() > 0 ? sb.toString() : null;
            }

            private String escapeJava(String s) {
                return s.replace("\\", "\\\\").replace("\"", "\\\"");
            }
        });
    }

    public static boolean instanceOfHookBreakpointType(XBreakpoint<?> bk) {
        return bk.getType() instanceof JdbcProcedureLineBreakpointType;
    }
}
