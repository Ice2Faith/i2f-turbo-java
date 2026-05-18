package i2f.turbo.idea.plugin.jdbc.procedure.debugger;

import com.intellij.debugger.PositionManager;
import com.intellij.debugger.PositionManagerFactory;
import com.intellij.debugger.engine.DebugProcess;
import com.intellij.debugger.engine.DebugProcessListener;
import com.intellij.debugger.engine.SuspendContext;
import com.intellij.debugger.jdi.StackFrameProxyImpl;
import com.intellij.debugger.jdi.ThreadReferenceProxyImpl;
import com.intellij.debugger.ui.breakpoints.JavaLineBreakpointType;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.breakpoints.*;
import com.intellij.xdebugger.impl.breakpoints.XBreakpointUtil;
import com.sun.jdi.*;
import i2f.lru.LruMap;
import i2f.turbo.idea.plugin.utils.PsiBreakpointUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.java.debugger.breakpoints.properties.JavaLineBreakpointProperties;

import java.util.Collection;
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

            private final AtomicReference<XLineBreakpoint<JavaLineBreakpointProperties>> managedBreakpointRef
                    = new AtomicReference<>();


            private volatile MessageBusConnection messageBusConnection;


            private volatile String cachedFileUrl;
            private volatile int cachedLine = -1;

            @Override
            public void paused(@NotNull SuspendContext suspendContext) {

                try {
                    ThreadReferenceProxyImpl thread = (ThreadReferenceProxyImpl) suspendContext.getThread();
                    // log.warn("resolveThisObject-2:thread=" + thread);
                    if (thread == null) {
                        return;
                    }
                    ObjectReference thisObj = null;
                    StackFrameProxyImpl frame = thread.frame(0);
                    // log.warn("resolveThisObject-3:frame=" + frame);
                    if (frame != null && isInReportMethod(frame)) {
                        thisObj = frame.thisObject();
                        // log.warn("resolveThisObject-4:thisObj=" + thisObj);
                    }
                    if (thisObj == null) {
                        return;
                    }


                    ReferenceType thisType = thisObj.referenceType();
                    Field fileNameField = thisType.fieldByName(JdbcProcedureDebugConsts.FILE_NAME_FIELD_NAME);
                    Field lineNumberField = thisType.fieldByName(JdbcProcedureDebugConsts.LINE_NUMBER_FIELD_NAME);
                    if (fileNameField == null || lineNumberField == null) {
                        return;
                    }
                    Value fileNameVal = thisObj.getValue(fileNameField);
                    Value lineNumberVal = thisObj.getValue(lineNumberField);
                    // log.warn("getSourcePosition-6:name=" + fileNameVal + ",line=" + lineNumberVal);
                    if (!(fileNameVal instanceof StringReference) || !(lineNumberVal instanceof IntegerValue)) {
                        return;
                    }
                    // log.warn("getSourcePosition-7:");
                    String fileName = ((StringReference) fileNameVal).value();
                    int lineNumber = ((IntegerValue) lineNumberVal).intValue();

                    VirtualFile virtualFile = findVirtualFile(fileName);
                    if (virtualFile == null) {
                        // log.warn("paused: cannot find virtual file for: " + fileName);
                        return;
                    }

                    int navigateLine = Math.max(0, lineNumber - 1);
                    new Thread(() -> {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {

                        }
                        ApplicationManager.getApplication().invokeLater(() -> {
                            new OpenFileDescriptor(project, virtualFile, navigateLine, 0)
                                    .navigate(true);
                        });
                    }).start();
                } catch (Throwable e) {
                    log.warn("paused: failed to navigate to source position", e);
                }

            }

            private boolean isInReportMethod(StackFrameProxyImpl frame) {
                try {
                    Location loc = frame.location();
                    if (loc == null) {
                        return false;
                    }
                    Method m = loc.method();
                    if (m == null || !JdbcProcedureDebugConsts.METHOD_NAME.equals(m.name())) {
                        return false;
                    }
                    return JdbcProcedureDebugConsts.SIMPLE_CLASS_NAME.equals(getSimpleName(loc.declaringType().name()));
                } catch (Exception e) {
                    return false;
                }
            }

            private static String getSimpleName(String fqn) {
                if (fqn == null) {
                    return "";
                }
                int dot = fqn.lastIndexOf('.');
                return dot >= 0 ? fqn.substring(dot + 1) : fqn;
            }

            protected LruMap<String, VirtualFile> virtualFileMap = new LruMap<>(512);

            private VirtualFile findVirtualFile(@Nullable String fileName) {
                // log.warn("findVirtualFile-0:" + fileName);
                if (fileName == null || fileName.isEmpty()) {
                    return null;
                }
                VirtualFile virtualFile = virtualFileMap.get(fileName);
                if (virtualFile != null) {
                    return virtualFile;
                }
                // log.warn("findVirtualFile-1:" + fileName);
                String normalizedPath = fileName.replace('\\', '/');
                // log.warn("findVirtualFile-2:" + normalizedPath);
                VirtualFile file = LocalFileSystem.getInstance().findFileByPath(normalizedPath);
                // log.warn("findVirtualFile-3:" + file);
                if (file != null && file.getFileType() == getCompareFileType()) {
                    return file;
                }
                // log.warn("findVirtualFile-4:");
                int lastSlash = normalizedPath.lastIndexOf('/');
                String baseName = lastSlash >= 0 ? normalizedPath.substring(lastSlash + 1) : normalizedPath;
                boolean hasPathHint = lastSlash > 0;

                AtomicReference<VirtualFile> found = new AtomicReference<>();
                // log.warn("findVirtualFile-5:");
                ApplicationManager.getApplication().runReadAction(() -> {
                    // log.warn("findVirtualFile-6-0:");
                    Collection<VirtualFile> candidates = FilenameIndex.getVirtualFilesByName(
                            baseName,
                            GlobalSearchScope.projectScope(project)
                    );
                    // log.warn("findVirtualFile-6-1:size=" + candidates.size());
                    VirtualFile fallback = null;

                    for (VirtualFile vf : candidates) {
                        // log.warn("findVirtualFile-6-2:" + vf);
                        if (vf.getFileType() != getCompareFileType()) {
                            continue;
                        }
                        // log.warn("findVirtualFile-6-3:");
                        // 路径后缀匹配优先
                        if (hasPathHint) {
                            // log.warn("findVirtualFile-6-4:");
                            String vfPath = vf.getPath().replace('\\', '/');
                            // log.warn("findVirtualFile-6-5:" + vfPath);
                            if (vfPath.endsWith(normalizedPath)) {
                                // log.warn("findVirtualFile-6-6:");
                                found.set(vf);
                                virtualFileMap.put(fileName, vf);
                                return;
                            }
                        }
                        // log.warn("findVirtualFile-6-7:");
                        if (fallback == null) {
                            // log.warn("findVirtualFile-6-8:");
                            fallback = vf;
                        }
                    }
                    // log.warn("findVirtualFile-6-9:");
                    if (found.get() == null && fallback != null) {
                        // log.warn("findVirtualFile-6-10:");
                        found.set(fallback);
                        virtualFileMap.put(fileName, fallback);
                    }
                    // log.warn("findVirtualFile-6-11:");
                });
                // log.warn("findVirtualFile-7:");
                VirtualFile ret = found.get();
                if (ret != null) {
                    virtualFileMap.put(fileName, ret);
                }
                return ret;
            }

            private FileType getCompareFileType() {
                return XmlFileType.INSTANCE;
            }

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
                XLineBreakpoint<JavaLineBreakpointProperties> bp = managedBreakpointRef.getAndSet(null);
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

                                    PsiCodeBlock body = method.getBody();
                                    if (body != null) {
                                        PsiStatement[] statements = body.getStatements();
                                        if (statements.length > 0) {
                                            line = file.getFileDocument().getLineNumber(statements[0].getTextOffset());
                                        }
                                    }

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
                        XLineBreakpoint<JavaLineBreakpointProperties> bp = managedBreakpointRef.getAndSet(null);
                        if (bp != null) {
                            bpManager.removeBreakpoint(bp);
                            // log.warn("BreakpointBridge: removed managed method breakpoint (no .fic breakpoints)");
                        }
                        return;
                    }

                    XLineBreakpoint<JavaLineBreakpointProperties> existing = managedBreakpointRef.get();
                    if (existing != null) {
                        existing.setCondition(condition);
                        // log.warn("BreakpointBridge: updated condition on existing method breakpoint");
                    } else {
                        JavaLineBreakpointType javaLineType =
                                (JavaLineBreakpointType) XBreakpointUtil.findType("java-line");
                        if (javaLineType == null) {
                            // log.warn("BreakpointBridge: JavaMethodBreakpointType not found");
                            return;
                        }

                        JavaLineBreakpointProperties props = new JavaLineBreakpointProperties();
                        XLineBreakpoint<JavaLineBreakpointProperties> newBp =
                                bpManager.addLineBreakpoint(javaLineType, cachedFileUrl, cachedLine, props);
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
