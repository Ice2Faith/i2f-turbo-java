package i2f.turbo.idea.plugin.funic.lang.debugger;

import com.intellij.debugger.NoDataException;
import com.intellij.debugger.PositionManager;
import com.intellij.debugger.SourcePosition;
import com.intellij.debugger.engine.*;
import com.intellij.debugger.engine.evaluation.EvaluateException;
import com.intellij.debugger.engine.events.DebuggerCommandImpl;
import com.intellij.debugger.engine.managerThread.SuspendContextCommand;
import com.intellij.debugger.jdi.StackFrameProxyImpl;
import com.intellij.debugger.jdi.ThreadReferenceProxyImpl;
import com.intellij.debugger.jdi.VirtualMachineProxyImpl;
import com.intellij.debugger.requests.ClassPrepareRequestor;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
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
import i2f.turbo.idea.plugin.funic.FunicDebugReporter;
import i2f.turbo.idea.plugin.funic.FunicFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ice2Faith
 * @date 2026/5/15
 * @desc Funic 脚本调试位置管理器。
 * <p>
 * 工作原理：
 * 1. 当用户在 .fic 文件中打断点时，本 PositionManager 将该断点映射到
 * 被调试 JVM 中 FunicDebugReporter（final 唯一实现类）的 report() 方法入口处。
 * 2. 当 JVM 在 report() 方法入口暂停时，本管理器读取当前栈帧的参数
 * (fileName, lineNumber)，并返回对应的 Funic 源码位置。
 * 3. IntelliJ 比较返回的源码位置与注册的断点位置：
 * - 匹配 → 保持暂停，打开 .fic 文件并高亮对应行
 * - 不匹配 → 自动继续执行
 * <p>
 * 变量查看：
 * 暂停时 Variables 面板会展示 report() 方法的三个参数：
 * - fileName (String): 当前 Funic 脚本文件名
 * - lineNumber (int): 当前执行行号
 * - variableMap (Map): 脚本运行时所有变量，可展开查看每个变量的值
 */
public class FunicPositionManager implements PositionManager {

    private static final Logger LOG = Logger.getInstance(FunicPositionManager.class);

    /**
     * FunicDebugReporter.report 方法名
     */
    private static final String REPORT_METHOD_NAME = "report";

    /**
     * FunicDebugReporter 的简单类名。
     * 不使用全限定类名，是因为被调试应用中的 FunicDebugReporter 可能来自不同的包，
     * 但 simpleName（"FunicDebugReporter"）一定相同。
     */
    private static final String REPORTER_SIMPLE_CLASS_NAME = FunicDebugReporter.class.getSimpleName();

    /**
     * ClassPrepareRequest 通配符模式，匹配任意包下的 FunicDebugReporter
     */
    private static final String REPORTER_CLASS_PATTERN = "*." + REPORTER_SIMPLE_CLASS_NAME;

    private final DebugProcess myDebugProcess;
    private final Project myProject;

    public FunicPositionManager(@NotNull DebugProcess debugProcess) {
        this.myDebugProcess = debugProcess;
        this.myProject = debugProcess.getProject();
    }

    /**
     * 将 JVM 位置（report() 方法入口）映射回 Funic 源码位置。
     * 通过读取当前挂起线程栈帧的 fileName 和 lineNumber 参数实现。
     */
    @Nullable
    @Override
    public SourcePosition getSourcePosition(@Nullable Location location) throws NoDataException {
        LOG.warn("getSourcePosition-0:" + location);
        // 先做 null 检查，避免在 LOG.warn 中调用 location.lineNumber() 引发 NPE
        if (location == null) {
            throw NoDataException.INSTANCE;
        }
        LOG.warn("getSourcePosition-0:" + location + ",line=" + location.lineNumber() + ",method=" + location.method());
        LOG.warn("getSourcePosition-1:");
        try {
            // 只处理 FunicDebugReporter.report 方法
            Method method = location.method();
            if (method == null || !REPORT_METHOD_NAME.equals(method.name())) {
                throw NoDataException.INSTANCE;
            }
            LOG.warn("getSourcePosition-2:");

            // 校验声明类的 simpleName 是 FunicDebugReporter（包名可能不同，但 simpleName 固定）
            if (!REPORTER_SIMPLE_CLASS_NAME.equals(getSimpleName(location.declaringType().name()))) {
                throw NoDataException.INSTANCE;
            }
            LOG.warn("getSourcePosition-3:");
            // 通过 SuspendManager 获取当前挂起上下文和栈帧，替代已移除的 DebuggerManagerEx
            DebugProcessImpl processImpl = (DebugProcessImpl) myDebugProcess;
            SuspendContextImpl suspendContext = processImpl.getSuspendManager().getPausedContext();
            LOG.warn("getSourcePosition-4:" + suspendContext);
            if (suspendContext == null) {
                throw NoDataException.INSTANCE;
            }
            LOG.warn("getSourcePosition-5:" + suspendContext.getThread());
            ThreadReferenceProxyImpl thread = suspendContext.getThread();
            if (thread == null) {
                throw NoDataException.INSTANCE;
            }
            LOG.warn("getSourcePosition-6:");
            StackFrameProxyImpl frameProxy;
            try {
                frameProxy = thread.frame(0);
            } catch (EvaluateException e) {
                throw NoDataException.INSTANCE;
            }
            LOG.warn("getSourcePosition-7:frame=" + frameProxy);
            if (frameProxy == null) {
                throw NoDataException.INSTANCE;
            }
            LOG.warn("getSourcePosition-8:" + frameProxy.getArgumentValues());
            List<Value> args = frameProxy.getArgumentValues();
            if (args.size() < 2) {
                throw NoDataException.INSTANCE;
            }
            LOG.warn("getSourcePosition-9:");
            Value fileNameVal = args.get(0);
            Value lineNumberVal = args.get(1);
            LOG.warn("getSourcePosition-10:name=" + fileNameVal + ",line=" + lineNumberVal);
            if (!(fileNameVal instanceof StringReference) || !(lineNumberVal instanceof IntegerValue)) {
                throw NoDataException.INSTANCE;
            }
            LOG.warn("getSourcePosition-11:");
            String fileName = ((StringReference) fileNameVal).value();
            int lineNumber = ((IntegerValue) lineNumberVal).intValue();
            LOG.warn("getSourcePosition-12:");
            // 在项目中查找对应的 Funic 虚拟文件
            VirtualFile funicFile = findFunicFile(fileName);
            if (funicFile == null) {
                LOG.debug("FunicPositionManager: cannot find funic file for: " + fileName);
                throw NoDataException.INSTANCE;
            }
            LOG.warn("getSourcePosition-13:" + funicFile);
            // 读取 PSI 文件（需要 ReadAction）
            LOG.warn("getSourcePosition-14:");
            PsiFile psiFile = getPsiFile(funicFile);
            if (psiFile == null) {
                throw NoDataException.INSTANCE;
            }
            LOG.warn("getSourcePosition-15:");
            // lineNumber 是 1-based，SourcePosition 需要 0-based
            return SourcePosition.createFromLine(psiFile, Math.max(0, lineNumber - 1));

        } catch (NoDataException e) {
            throw e;
        } catch (Exception e) {
            LOG.debug("getSourcePosition error: " + e.getMessage());
            throw NoDataException.INSTANCE;
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

    /**
     * 返回与给定 Funic 源码位置相关的 JVM 类（即 FunicDebugReporter 唯一实现类）。
     * IntelliJ 用此方法为新加载的类重新绑定断点。
     */
    @NotNull
    @Override
    public List<ReferenceType> getAllClasses(@NotNull SourcePosition classPosition) throws NoDataException {
        LOG.warn("getAllClasses-0:" + classPosition + ",line=" + classPosition.getLine() + ",file=" + classPosition.getFile() + ",element=" + classPosition.getElementAt());
        if (classPosition.getFile().getFileType() != FunicFileType.INSTANCE) {
            throw NoDataException.INSTANCE;
        }

        LOG.warn("getAllClasses-1:");

        List<ReferenceType> result = findReportableClasses();
        LOG.warn("getAllClasses-2:" + result.size());
        if (result.isEmpty()) {
            throw NoDataException.INSTANCE;
        }
        LOG.warn("getAllClasses-3:");
        return result;
    }

    /**
     * 在给定 ReferenceType 中返回对应 Funic 源码位置的 JVM Location 列表。
     * 只处理 FunicDebugReporter 类，返回 report() 方法入口位置。
     * 所有 Funic 行断点均映射到该入口，具体行匹配由 getSourcePosition() 在运行时判断。
     */
    @Override
    public @NotNull List<Location> locationsOfLine(@NotNull ReferenceType referenceType,
                                                   @NotNull SourcePosition sourcePosition)
            throws NoDataException {
        LOG.warn("locationsOfLine-0:" + referenceType + ",position=" + sourcePosition + ",line=" + sourcePosition.getLine() + ",element=" + sourcePosition.getElementAt());
        if (sourcePosition.getFile().getFileType() != FunicFileType.INSTANCE) {
            throw NoDataException.INSTANCE;
        }
        LOG.warn("locationsOfLine-1:name=" + referenceType.name());
        // 只处理 simpleName 为 FunicDebugReporter 的类（包名可能不同）
        if (!REPORTER_SIMPLE_CLASS_NAME.equals(getSimpleName(referenceType.name()))) {
            return Collections.emptyList();
        }
        LOG.warn("locationsOfLine-2:");

        List<Location> locations = new ArrayList<>();
        List<Method> methods = referenceType.methodsByName(REPORT_METHOD_NAME);
        LOG.warn("locationsOfLine-3:size=" + methods.size());
        for (Method method : methods) {
            LOG.warn("locationsOfLine-4-0:method=" + method);
            try {
                if (method.argumentTypeNames().size() == 3) {
                    LOG.warn("locationsOfLine-4-1:method=" + method);
                    try {
                        List<Location> methodLocations = method.allLineLocations();
                        LOG.warn("locationsOfLine-4-2:size=" + methodLocations);
                        if (!methodLocations.isEmpty()) {
                            // 取方法第一行（方法入口）作为断点位置
                            locations.add(methodLocations.get(0));
                        }
                    } catch (AbsentInformationException e) {
                        LOG.warn("locationsOfLine-4-3:method=" + method);
                        // 没有调试信息时，使用方法自身的位置
                        Location methodLoc = method.location();
                        if (methodLoc != null) {
                            locations.add(methodLoc);
                        }

                    }
                }
            } catch (Throwable e) {
                LOG.warn("locationsOfLine-4-4:e=" + e);
            }
        }
        return locations;
    }

    /**
     * 创建 ClassPrepareRequest，当被调试 JVM 加载 FunicDebugReporter 时通知调试器，
     * 以便及时将断点绑定到该类。
     */
    @Override
    public @Nullable ClassPrepareRequest createPrepareRequest(@NotNull ClassPrepareRequestor requestor,
                                                              @NotNull SourcePosition sourcePosition)
            throws NoDataException {
        LOG.warn("createPrepareRequest-0:" + sourcePosition + ",line=" + sourcePosition.getLine() + ",file=" + sourcePosition.getFile() + ",element=" + sourcePosition.getElementAt());
        if (sourcePosition.getFile().getFileType() != FunicFileType.INSTANCE) {
            throw NoDataException.INSTANCE;
        }
        LOG.warn("createPrepareRequest-1:" + sourcePosition);
        // 使用通配符模式，匹配任意包下的 FunicDebugReporter，类加载后调试器会自动绑定断点
        ClassPrepareRequest classPrepareRequest = myDebugProcess.getRequestsManager().createClassPrepareRequest(requestor, REPORTER_CLASS_PATTERN);
        LOG.warn("createPrepareRequest-2:" + classPrepareRequest);
        return classPrepareRequest;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 私有辅助方法
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 在被调试的 JVM 中查找所有 simpleName 为 FunicDebugReporter 的类。
     * 由于被调试应用的包名可能与插件包名不同，不能用全限定类名精确查找，
     * 改为遍历所有已加载类并按 simpleName 过滤。
     */
    private List<ReferenceType> findReportableClasses() {
        try {
            LOG.warn("findReportableClasses-0:");
            VirtualMachineProxyImpl vm = ((DebugProcessImpl) myDebugProcess).getVirtualMachineProxy();
            LOG.warn("findReportableClasses-1:" + vm);
            List<ReferenceType> result = new ArrayList<>();
            for (ReferenceType rt : vm.allClasses()) {
                LOG.warn("findReportableClasses-2:" + rt);
                if (REPORTER_SIMPLE_CLASS_NAME.equals(getSimpleName(rt.name()))) {
                    LOG.warn("findReportableClasses-3:" + rt);
                    result.add(rt);
                }
            }
            return result;
        } catch (Exception e) {
            LOG.debug("findReportableClasses error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 从全限定类名中提取 simpleName（最后一个 '.' 之后的部分）。
     * 例如："com.example.FunicDebugReporter" → "FunicDebugReporter"
     */
    private static String getSimpleName(String fqn) {
        if (fqn == null) {
            return "";
        }
        int dot = fqn.lastIndexOf('.');
        return dot >= 0 ? fqn.substring(dot + 1) : fqn;
    }

    protected LruMap<String, VirtualFile> virtualFileMap = new LruMap<>(512);

    /**
     * 根据脚本运行时提供的 fileName 在项目中查找对应的 VirtualFile。
     * <p>
     * 查找策略（按优先级）：
     * 1. 将 fileName 作为绝对路径直接查找
     * 2. 在项目范围内按文件名搜索，优先选择路径后缀匹配的文件
     * 3. 若无路径匹配则返回同名的第一个 .fic 文件
     */
    @Nullable
    private VirtualFile findFunicFile(@Nullable String fileName) {
        LOG.warn("findFunicFile-0:" + fileName);
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        VirtualFile virtualFile = virtualFileMap.get(fileName);
        if (virtualFile != null) {
            return virtualFile;
        }
        LOG.warn("findFunicFile-1:" + fileName);
        // 统一路径分隔符
        String normalizedPath = fileName.replace('\\', '/');
        LOG.warn("findFunicFile-2:" + normalizedPath);
        // 优先作为绝对路径查找
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(normalizedPath);
        LOG.warn("findFunicFile-3:" + file);
        if (file != null && file.getFileType() == FunicFileType.INSTANCE) {
            return file;
        }
        LOG.warn("findFunicFile-4:");
        // 提取文件名部分，在项目范围内搜索
        int lastSlash = normalizedPath.lastIndexOf('/');
        String baseName = lastSlash >= 0 ? normalizedPath.substring(lastSlash + 1) : normalizedPath;
        boolean hasPathHint = lastSlash > 0;

        AtomicReference<VirtualFile> found = new AtomicReference<>();
        LOG.warn("findFunicFile-5:");
        ApplicationManager.getApplication().runReadAction(() -> {
            LOG.warn("findFunicFile-6-0:");
            Collection<VirtualFile> candidates = FilenameIndex.getVirtualFilesByName(
                    baseName,
                    GlobalSearchScope.projectScope(myProject)
            );
            LOG.warn("findFunicFile-6-1:size=" + candidates.size());
            VirtualFile fallback = null;

            for (VirtualFile vf : candidates) {
                LOG.warn("findFunicFile-6-2:" + vf);
                if (vf.getFileType() != FunicFileType.INSTANCE) {
                    continue;
                }
                LOG.warn("findFunicFile-6-3:");
                // 路径后缀匹配优先
                if (hasPathHint) {
                    LOG.warn("findFunicFile-6-4:");
                    String vfPath = vf.getPath().replace('\\', '/');
                    LOG.warn("findFunicFile-6-5:" + vfPath);
                    if (vfPath.endsWith(normalizedPath)) {
                        LOG.warn("findFunicFile-6-6:");
                        found.set(vf);
                        virtualFileMap.put(fileName, vf);
                        return;
                    }
                }
                LOG.warn("findFunicFile-6-7:");
                // 记录第一个同名文件作为回退
                if (fallback == null) {
                    LOG.warn("findFunicFile-6-8:");
                    fallback = vf;
                }
            }
            LOG.warn("findFunicFile-6-9:");
            // 没有路径精确匹配时，使用同名回退
            if (found.get() == null && fallback != null) {
                LOG.warn("findFunicFile-6-10:");
                found.set(fallback);
                virtualFileMap.put(fileName, fallback);
            }
            LOG.warn("findFunicFile-6-11:");
        });
        LOG.warn("findFunicFile-7:");
        VirtualFile ret = found.get();
        if (ret != null) {
            virtualFileMap.put(fileName, ret);
        }
        return ret;
    }
}
