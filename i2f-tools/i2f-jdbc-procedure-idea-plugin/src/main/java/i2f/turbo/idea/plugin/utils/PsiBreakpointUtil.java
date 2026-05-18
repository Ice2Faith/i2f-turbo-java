package i2f.turbo.idea.plugin.utils;

import com.intellij.debugger.ui.breakpoints.JavaLineBreakpointType;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.breakpoints.SuspendPolicy;
import com.intellij.xdebugger.breakpoints.XBreakpoint;
import com.intellij.xdebugger.breakpoints.XBreakpointManager;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.impl.breakpoints.XBreakpointUtil;
import i2f.lru.LruMap;
import org.jetbrains.java.debugger.breakpoints.properties.JavaLineBreakpointProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/5/16 13:44
 * @desc
 */
public class PsiBreakpointUtil {
    protected static LruMap<String, List<PsiClass>> cacheClass = new LruMap<>(1024);

    public static List<PsiClass> getPsiClass(Project project, String shortName) {
        String cacheKey = project.getProjectFilePath() + "#" + shortName;
        List<PsiClass> ret = cacheClass.get(cacheKey);
        if (ret != null) {
            return new ArrayList<>(ret);
        }
        //
        GlobalSearchScope searchScope = GlobalSearchScope.everythingScope(project);
        PsiShortNamesCache shortNamesCache = PsiShortNamesCache.getInstance(project);
        PsiClass[] psiClassArr = shortNamesCache.getClassesByName(shortName, searchScope);
        ret = new ArrayList<>(Arrays.asList(psiClassArr));
        cacheClass.put(cacheKey, new ArrayList<>(ret));
        return ret;
    }

    public static List<PsiMethod> getPsiMethod(PsiClass psiClass, String methodName, int parameterCount) {
        List<PsiMethod> ret = new ArrayList<>();
        PsiMethod[] allMethods = psiClass.getAllMethods();
        for (PsiMethod item : allMethods) {
            String name = item.getName();
            PsiParameterList parameterList = item.getParameterList();
            PsiParameter[] parameters = parameterList.getParameters();
            if (name.equals(methodName)) {
                if (parameters.length == parameterCount) {
                    ret.add(item);
                }
            }
        }
        return ret;
    }

    public static XBreakpointManager getBreakpointManager(Project project) {
        XDebuggerManager debuggerManager = XDebuggerManager.getInstance(project);
        XBreakpointManager breakpointManager = debuggerManager.getBreakpointManager();
        return breakpointManager;
    }

    public static void openFileInEditor(Project project, VirtualFile virtualFile, int lineNumber) {
        // 1. 获取文档管理器，通过 VirtualFile 拿到对应的 Document

        // 2. 使用 FileEditorManager 打开文件，并使其成为当前选中的编辑器
        // openFile 方法会自动处理文件是否已经打开的情况，如果已打开则直接激活该标签页
        FileEditorManager.getInstance(project).openFile(virtualFile, true);

        // 3. 获取刚刚打开的文件的编辑器实例
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor != null) {
            // 4. 获取文档对象，用于计算文本偏移量(offset)
            var document = editor.getDocument();

            // 5. 将传入的行号转换为编辑器内部的偏移量
            // 注意：IDEA API 中行号是从 0 开始的。如果你的 lineNumber 是从 1 开始计数的，请改为 (lineNumber - 1)
            int lineOffset = document.getLineStartOffset(lineNumber);
            int lineEndOffset = document.getLineEndOffset(lineNumber);

            // 6. 设置光标位置并选中整行
            // 移除之前的所有选择区域（如果有），保证只选中目标行
            // 选中从行首到行尾的文本
            SelectionModel selectionModel = editor.getSelectionModel();
            selectionModel.removeSelection(true);
            selectionModel.setSelection(lineOffset, lineEndOffset);

            CaretModel caretModel = editor.getCaretModel();
            caretModel.moveToOffset(lineOffset);

            // 7. 滚动编辑器视图，确保选中的行出现在可见区域内
            ScrollingModel scrollingModel = editor.getScrollingModel();
            // scrollTo 会平滑滚动，caretModel.scrollTo 也可以直接使用
            scrollingModel.scrollTo(caretModel.getLogicalPosition(), ScrollType.CENTER);
        } else {
            new OpenFileDescriptor(project, virtualFile, lineNumber, 0)
                    .navigate(true);
        }
    }

    /**
     * 为指定的 PsiMethod 添加或更新条件断点
     *
     * @param psiMethod 已经获取到的 PsiMethod 对象
     * @param condition 条件表达式，例如 "userId == 1001"
     */
    public static void addOrUpdateConditionBreakpoint(PsiMethod psiMethod, String condition) {
        Project project = psiMethod.getProject();

        // 1. 提取文件 VirtualFile 和起始行号
        PsiFile containingFile = psiMethod.getContainingFile();
        if (containingFile == null) {
            return;
        }

        VirtualFile virtualFile = containingFile.getVirtualFile();
        if (virtualFile == null) {
            return;
        }


        // 获取方法的起始行号（IDEA API 行号从 0 开始）
        int startLine = containingFile.getFileDocument().getLineNumber(psiMethod.getTextOffset());

        PsiCodeBlock body = psiMethod.getBody();
        if (body != null) {
            PsiStatement[] statements = body.getStatements();
            if (statements.length > 0) {
                PsiStatement statement = statements[0];
                startLine = containingFile.getFileDocument().getLineNumber(statement.getTextOffset());
            }
        }


        String fileUrl = virtualFile.getUrl();

        // 2. 获取断点管理器与 Java 行断点类型
        XBreakpointManager breakpointManager = getBreakpointManager(project);
        JavaLineBreakpointType lineBreakpointType = (JavaLineBreakpointType) XBreakpointUtil.findType("java-line");

        // 3. 遍历所有现有断点，判断是否已经在该位置添加过断点
        XBreakpoint<?> existingBreakpoint = null;
        for (XBreakpoint<?> bp : breakpointManager.getAllBreakpoints()) {
            // 筛选出同类型的行断点
            if (bp instanceof XLineBreakpoint) {
                XLineBreakpoint lineBreakpoint = (XLineBreakpoint) bp;
                // 检查文件路径和行号是否完全一致
                if (fileUrl.equals(lineBreakpoint.getFileUrl()) && startLine == lineBreakpoint.getLine()) {
                    existingBreakpoint = bp;
                    break;
                }
            }
        }

        // 4. 如果断点已存在，直接更新条件；否则创建新断点
        if (existingBreakpoint != null) {
            existingBreakpoint.setCondition(condition);
            System.out.println("检测到断点已存在，已成功更新方法 " + psiMethod.getName() + " 的断点条件为：" + condition);
        } else {
            JavaLineBreakpointProperties properties = new JavaLineBreakpointProperties();
            XLineBreakpoint<JavaLineBreakpointProperties> newBreakpoint = breakpointManager.addLineBreakpoint(lineBreakpointType, fileUrl, startLine, properties);
            if (newBreakpoint != null) {
                newBreakpoint.setCondition(condition);
                // 建议设置为仅暂停当前线程，避免阻塞整个应用
                newBreakpoint.setSuspendPolicy(SuspendPolicy.THREAD);
                System.out.println("成功为方法 " + psiMethod.getName() + " 的新位置添加了条件断点，条件为：" + condition);
            }
        }
    }
}