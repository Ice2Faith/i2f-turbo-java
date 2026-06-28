package i2f.turbo.idea.plugin.jdbc.procedure.debugger;

import com.intellij.debugger.NoDataException;
import com.intellij.debugger.PositionManager;
import com.intellij.debugger.SourcePosition;
import com.intellij.debugger.engine.DebugProcess;
import com.intellij.debugger.engine.DebugProcessImpl;
import com.intellij.debugger.engine.SuspendContext;
import com.intellij.debugger.engine.SuspendContextImpl;
import com.intellij.debugger.engine.evaluation.EvaluateException;
import com.intellij.debugger.jdi.StackFrameProxyImpl;
import com.intellij.debugger.jdi.ThreadReferenceProxyImpl;
import com.intellij.debugger.jdi.VirtualMachineProxyImpl;
import com.intellij.debugger.requests.ClassPrepareRequestor;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.sun.jdi.*;
import com.sun.jdi.request.ClassPrepareRequest;
import i2f.lru.LruMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2026/5/15
 * @desc
 */
public class JdbcProcedurePositionManager implements PositionManager {

    private static final Logger log = Logger.getInstance(JdbcProcedurePositionManager.class);

    private final DebugProcess myDebugProcess;
    private final Project myProject;

    public JdbcProcedurePositionManager(@NotNull DebugProcess debugProcess) {
        this.myDebugProcess = debugProcess;
        this.myProject = debugProcess.getProject();
    }


    @Nullable
    @Override
    public SourcePosition getSourcePosition(@Nullable Location location) throws NoDataException {
        // log.warn("getSourcePosition-0:" + location);
        if (location == null) {
            throw NoDataException.INSTANCE;
        }
        // log.warn("getSourcePosition-0:" + location + ",line=" + location.lineNumber() + ",method=" + location.method());
        // log.warn("getSourcePosition-1:");
        try {
            Method method = location.method();
            if (method == null || !JdbcProcedureDebugConsts.METHOD_NAME.equals(method.name())) {
                throw NoDataException.INSTANCE;
            }
            // log.warn("getSourcePosition-2:");

            if (!JdbcProcedureDebugConsts.SIMPLE_CLASS_NAME.equals(getSimpleName(location.declaringType().name()))) {
                throw NoDataException.INSTANCE;
            }
            // log.warn("getSourcePosition-3:");

            ObjectReference thisObj = resolveThisObject(location);
            // log.warn("getSourcePosition-4:thisObj=" + thisObj);
            if (thisObj == null) {
                throw NoDataException.INSTANCE;
            }
            // log.warn("getSourcePosition-5:");
            ReferenceType thisType = thisObj.referenceType();
            Field fileNameField = thisType.fieldByName(JdbcProcedureDebugConsts.FILE_NAME_FIELD_NAME);
            Field lineNumberField = thisType.fieldByName(JdbcProcedureDebugConsts.LINE_NUMBER_FIELD_NAME);
            if (fileNameField == null || lineNumberField == null) {
                throw NoDataException.INSTANCE;
            }
            Value fileNameVal = thisObj.getValue(fileNameField);
            Value lineNumberVal = thisObj.getValue(lineNumberField);
            // log.warn("getSourcePosition-6:name=" + fileNameVal + ",line=" + lineNumberVal);
            if (!(fileNameVal instanceof StringReference) || !(lineNumberVal instanceof IntegerValue)) {
                throw NoDataException.INSTANCE;
            }
            // log.warn("getSourcePosition-7:");
            String fileName = ((StringReference) fileNameVal).value();
            int lineNumber = ((IntegerValue) lineNumberVal).intValue();
            // log.warn("getSourcePosition-8:fileName=" + fileName + ",lineNumber=" + lineNumber);
            // 在项目中查找对应的虚拟文件
            VirtualFile virtualFile = findVirtualFile(fileName);
            if (virtualFile == null) {
                // log.warn("getSourcePosition-8: cannot find virtual file for: " + fileName);
                throw NoDataException.INSTANCE;
            }
            // log.warn("getSourcePosition-9:" + virtualFile);
            PsiFile psiFile = getPsiFile(virtualFile);
            if (psiFile == null) {
                throw NoDataException.INSTANCE;
            }
            // log.warn("getSourcePosition-10:" + psiFile);
            return SourcePosition.createFromLine(psiFile, Math.max(0, lineNumber - 1));

        } catch (NoDataException e) {
            throw e;
        } catch (Exception e) {
            // log.warn("getSourcePosition error: " + e.getMessage());
            throw NoDataException.INSTANCE;
        }
    }

    public ObjectReference getThisObject(SuspendContext suspendContext) throws EvaluateException {
        ThreadReferenceProxyImpl thread = (ThreadReferenceProxyImpl) suspendContext.getThread();
        // log.warn("resolveThisObject-2:thread=" + thread);
        if (thread == null) {
            return null;
        }
        StackFrameProxyImpl frame = thread.frame(0);
        // log.warn("resolveThisObject-3:frame=" + frame);
        if (frame != null && isInReportMethod(frame)) {
            ObjectReference thisObj = frame.thisObject();
            // log.warn("resolveThisObject-4:thisObj=" + thisObj);
            if (thisObj != null) {
                return thisObj;
            }
        }
        return null;
    }

    @Nullable
    public ObjectReference resolveThisObject(Location location) {
        DebugProcessImpl processImpl = (DebugProcessImpl) myDebugProcess;

        // 策略1：通过 pausedContext 获取
        try {
            SuspendContextImpl suspendContext = processImpl.getSuspendManager().getPausedContext();
            // log.warn("resolveThisObject-1:ctx=" + suspendContext);
            if (suspendContext != null) {
                ObjectReference thisObj = getThisObject(suspendContext);
                if (thisObj != null) {
                    return thisObj;
                }
            }
        } catch (Exception e) {
            // log.warn("resolveThisObject-strategy1 failed: " + e.getMessage());
        }

        try {
            VirtualMachineProxyImpl vm = processImpl.getVirtualMachineProxy();
            // log.warn("resolveThisObject-5:vm=" + vm);
            for (ThreadReferenceProxyImpl tp : vm.allThreads()) {
                try {
                    if (!tp.isSuspended()) {
                        continue;
                    }
                    StackFrameProxyImpl frame = tp.frame(0);
                    if (frame == null || !isInReportMethod(frame)) {
                        continue;
                    }
                    ObjectReference thisObj = frame.thisObject();
                    // log.warn("resolveThisObject-6:thread=" + tp + ",thisObj=" + thisObj);
                    if (thisObj != null) {
                        return thisObj;
                    }
                } catch (EvaluateException e) {
                    // 跳过无法访问的线程
                }
            }
        } catch (Exception e) {
            // log.warn("resolveThisObject-strategy2 failed: " + e.getMessage());
        }

        return null;
    }

    public boolean isInReportMethod(StackFrameProxyImpl frame) {
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

    protected LruMap<String, PsiFile> psiFileLruMap = new LruMap<>(512);

    public PsiFile getPsiFile(VirtualFile virtualFile) {
        String url = virtualFile.getUrl();
        PsiFile psiFile = psiFileLruMap.get(url);
        if (psiFile != null) {
            return psiFile;
        }
        AtomicReference<PsiFile> psiFileRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        ApplicationManager.getApplication().runReadAction(() -> {
            PsiFile file = PsiManager.getInstance(myProject).findFile(virtualFile);
            psiFileRef.set(file);
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {

        }
        psiFile = psiFileRef.get();
        psiFileLruMap.put(url, psiFile);
        return psiFile;
    }

    @NotNull
    @Override
    public List<ReferenceType> getAllClasses(@NotNull SourcePosition classPosition) throws NoDataException {
        // log.warn("getAllClasses-0:" + classPosition + ",line=" + classPosition.getLine() + ",file=" + classPosition.getFile() + ",element=" + classPosition.getElementAt());
        if (classPosition.getFile().getFileType() != getCompareFileType()) {
            throw NoDataException.INSTANCE;
        }

        // log.warn("getAllClasses-1:");

        List<ReferenceType> result = findReportableClasses();
        // log.warn("getAllClasses-2:" + result.size());
        if (result.isEmpty()) {
            throw NoDataException.INSTANCE;
        }
        // log.warn("getAllClasses-3:");
        return result;
    }

    @Override
    public @NotNull List<Location> locationsOfLine(@NotNull ReferenceType referenceType,
                                                   @NotNull SourcePosition sourcePosition)
            throws NoDataException {
        // log.warn("locationsOfLine-0:" + referenceType + ",position=" + sourcePosition + ",line=" + sourcePosition.getLine() + ",element=" + sourcePosition.getElementAt());
        if (sourcePosition.getFile().getFileType() != getCompareFileType()) {
            throw NoDataException.INSTANCE;
        }
        // log.warn("locationsOfLine-1:name=" + referenceType.name());
        if (!JdbcProcedureDebugConsts.SIMPLE_CLASS_NAME.equals(getSimpleName(referenceType.name()))) {
            return Collections.emptyList();
        }
        // log.warn("locationsOfLine-2:");

        List<Location> locations = new ArrayList<>();
        List<Method> methods = referenceType.methodsByName(JdbcProcedureDebugConsts.METHOD_NAME);
        // log.warn("locationsOfLine-3:size=" + methods.size());
        for (Method method : methods) {
            // log.warn("locationsOfLine-4-0:method=" + method);
            try {
                if (method.argumentTypeNames().size() == JdbcProcedureDebugConsts.PARAM_COUNT) {
                    // log.warn("locationsOfLine-4-1:method=" + method);
                    try {
                        List<Location> methodLocations = method.allLineLocations();
                        // log.warn("locationsOfLine-4-2:size=" + methodLocations);
                        if (!methodLocations.isEmpty()) {
                            locations.add(methodLocations.get(0));
                        }
                    } catch (AbsentInformationException e) {
                        // log.warn("locationsOfLine-4-3:method=" + method);
                        Location methodLoc = method.location();
                        if (methodLoc != null) {
                            locations.add(methodLoc);
                        }

                    }
                }
            } catch (Throwable e) {
                // log.warn("locationsOfLine-4-4:e=" + e);
            }
        }
        return locations;
    }

    @Override
    public @Nullable ClassPrepareRequest createPrepareRequest(@NotNull ClassPrepareRequestor requestor,
                                                              @NotNull SourcePosition sourcePosition)
            throws NoDataException {
        // log.warn("createPrepareRequest-0:" + sourcePosition + ",line=" + sourcePosition.getLine() + ",file=" + sourcePosition.getFile() + ",element=" + sourcePosition.getElementAt());
        if (sourcePosition.getFile().getFileType() != getCompareFileType()) {
            throw NoDataException.INSTANCE;
        }
        // log.warn("createPrepareRequest-1:" + sourcePosition);
        ClassPrepareRequest classPrepareRequest = myDebugProcess.getRequestsManager().createClassPrepareRequest(requestor, JdbcProcedureDebugConsts.CLASS_PATTERN);
        // log.warn("createPrepareRequest-2:" + classPrepareRequest);
        return classPrepareRequest;
    }

    private List<ReferenceType> findReportableClasses() {
        try {
            // log.warn("findReportableClasses-0:");
            VirtualMachineProxyImpl vm = ((DebugProcessImpl) myDebugProcess).getVirtualMachineProxy();
            // log.warn("findReportableClasses-1:" + vm);
            List<ReferenceType> result = new ArrayList<>();
            for (ReferenceType rt : vm.allClasses()) {
                // log.warn("findReportableClasses-2:" + rt);
                if (JdbcProcedureDebugConsts.SIMPLE_CLASS_NAME.equals(getSimpleName(rt.name()))) {
                    // log.warn("findReportableClasses-3:" + rt);
                    result.add(rt);
                }
            }
            return result;
        } catch (Exception e) {
            // log.warn("findReportableClasses error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public FileType getCompareFileType() {
        return XmlFileType.INSTANCE;
    }

    private static String getSimpleName(String fqn) {
        if (fqn == null) {
            return "";
        }
        int dot = fqn.lastIndexOf('.');
        return dot >= 0 ? fqn.substring(dot + 1) : fqn;
    }

    protected LruMap<String, VirtualFile> virtualFileMap = new LruMap<>(512);

    @Nullable
    public VirtualFile findVirtualFile(@Nullable String fileName) {
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
                    GlobalSearchScope.projectScope(myProject)
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
}
