package i2f.turbo.idea.plugin.funic.lang.debugger;

import com.intellij.debugger.PositionManager;
import com.intellij.debugger.PositionManagerFactory;
import com.intellij.debugger.engine.DebugProcess;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiParameterList;
import com.intellij.xdebugger.breakpoints.*;
import com.intellij.xdebugger.impl.breakpoints.XBreakpointUtil;
import com.intellij.xdebugger.impl.breakpoints.XLineBreakpointImpl;
import i2f.turbo.idea.plugin.funic.FunicDebugReporter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2026/5/15
 * @desc Funic 脚本调试位置管理器工厂。
 * 由 IntelliJ Java 调试器在每次调试会话启动时调用，
 * 创建 FunicPositionManager 实例以支持 .fic 文件的断点调试。
 * <p>
 * 重要说明：FunicLineBreakpointType 继承自 XLineBreakpointType（XDebugger 通用 API），
 * 不在 Java 调试器的内部断点流水线中，因此 PositionManager.createPrepareRequest
 * 不会被 .fic 断点触发。本工厂在调试会话启动时主动注册 ClassPrepareRequest 和
 * BreakpointRequest，确保 JVM 在 FunicDebugReporter.report() 处暂停，
 * 从而让 FunicPositionManager.getSourcePosition() 能够正确映射到 .fic 源码位置。
 */
public class FunicPositionManagerFactory extends PositionManagerFactory {
    public static final Logger log=Logger.getInstance(FunicPositionManagerFactory.class);

    private static final String REPORTER_SIMPLE_CLASS_NAME = FunicDebugReporter.class.getSimpleName();
    private static final String REPORT_METHOD_NAME = "report";

    public static final ConcurrentHashMap<String, Map<String,Map.Entry<String,Integer>>> BREAKPOINT_MAP = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String,FunicBreakpointListener> LISTENER_MAP=new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String,PsiMethod> METHOD_MAP=new ConcurrentHashMap<>();

    public static class FunicBreakpointListener implements XBreakpointListener<XLineBreakpoint<FunicBreakpointProperties>> {
        protected Project project;
        public FunicBreakpointListener(Project project) {
            this.project = project;
        }
        @Override
        public void breakpointAdded(@NotNull XLineBreakpoint<FunicBreakpointProperties> breakpoint) {
            log.warn("breakpointAdded-0,url="+breakpoint.getFileUrl()+",line="+breakpoint.getLine());
            String projectFilePath = project.getProjectFilePath();
            String fileUrl = breakpoint.getFileUrl();
            String normalizeFileUrl = fileUrl.replace("\\", "/");
            String fileName=normalizeFileUrl;
            int idx=normalizeFileUrl.lastIndexOf("/");
            if(idx>=0){
                fileName=normalizeFileUrl.substring(idx+1);
            }
            log.warn("breakpointAdded-1,file="+fileName+",line="+breakpoint.getLine());
            Map<String, Map.Entry<String,Integer>> breakMap = BREAKPOINT_MAP.computeIfAbsent(projectFilePath, k -> new ConcurrentHashMap<>());
            String unqKey=fileName+"#"+breakpoint.getLine();
            breakMap.put(unqKey,new AbstractMap.SimpleEntry<>(fileName, breakpoint.getLine()));
            updateBreakpointCondition(project);
        }

        @Override
        public void breakpointRemoved(@NotNull XLineBreakpoint<FunicBreakpointProperties> breakpoint) {
            log.warn("breakpointRemoved-0,url="+breakpoint.getFileUrl()+",line="+breakpoint.getLine());
            String projectFilePath = project.getProjectFilePath();
            String fileUrl = breakpoint.getFileUrl();
            String normalizeFileUrl = fileUrl.replace("\\", "/");
            String fileName=normalizeFileUrl;
            int idx=normalizeFileUrl.lastIndexOf("/");
            if(idx>=0){
                fileName=normalizeFileUrl.substring(idx+1);
            }
            log.warn("breakpointRemoved-1,file="+fileName+",line="+breakpoint.getLine());
            Map<String, Map.Entry<String,Integer>> breakMap = BREAKPOINT_MAP.computeIfAbsent(projectFilePath, k -> new ConcurrentHashMap<>());
            String unqKey=fileName+"#"+breakpoint.getLine();
            breakMap.remove(unqKey);
            updateBreakpointCondition(project);
        }
    }

    @Nullable
    @Override
    public PositionManager createPositionManager(@NotNull DebugProcess process) {
        log.warn("createPositionManager-0,process="+process);
        FunicPositionManager manager = new FunicPositionManager(process);
        ApplicationManager.getApplication().runReadAction(() -> {
            addBreakpointListener(process.getProject());
            initCurrentBreakpoints(process.getProject());
            updateBreakpointCondition(process.getProject());
        });
        return manager;
    }

    public static void initCurrentBreakpoints(Project project) {
        log.warn("initCurrentBreakpoints-0,project="+project);
        String projectFilePath = project.getProjectFilePath();
        XBreakpointManager breakpointManager = PsiBreakpointUtil.getBreakpointManager(project);
        XBreakpoint<?>[] allBreakpoints = breakpointManager.getAllBreakpoints();
        log.warn("initCurrentBreakpoints-1,length="+allBreakpoints.length);
        for (XBreakpoint<?> item : allBreakpoints) {
            log.warn("initCurrentBreakpoints-2,itemClass="+item.getClass()+",item="+item);
            if(item.getType() instanceof FunicLineBreakpointType){
                log.warn("initCurrentBreakpoints-3,item="+item);
                XLineBreakpoint<?> breakpoint = (XLineBreakpoint<?>) item;
                String fileUrl = breakpoint.getFileUrl();
                String normalizeFileUrl = fileUrl.replace("\\", "/");
                String fileName=normalizeFileUrl;
                int idx=normalizeFileUrl.lastIndexOf("/");
                if(idx>=0){
                    fileName=normalizeFileUrl.substring(idx+1);
                }
                log.warn("initCurrentBreakpoints-1,file="+fileName+",line="+breakpoint.getLine());
                Map<String, Map.Entry<String,Integer>> breakMap = BREAKPOINT_MAP.computeIfAbsent(projectFilePath, k -> new ConcurrentHashMap<>());
                String unqKey=fileName+"#"+breakpoint.getLine();
                breakMap.put(unqKey,new AbstractMap.SimpleEntry<>(fileName, breakpoint.getLine()));
            }
        }
        updateBreakpointCondition(project);
    }

    public static void addBreakpointListener(Project project){
        log.warn("addBreakpointListener-0,project="+project);
        String projectFilePath = project.getProjectFilePath();

        XBreakpointManager breakpointManager = PsiBreakpointUtil.getBreakpointManager(project);
        log.warn("addBreakpointListener-1,manager="+breakpointManager);
        FunicLineBreakpointType lineBreakpointType = (FunicLineBreakpointType) XBreakpointUtil.findType(FunicLineBreakpointType.ID);
        log.warn("addBreakpointListener-2,type="+lineBreakpointType);
        FunicBreakpointListener breakpointListener = LISTENER_MAP.computeIfAbsent(projectFilePath, k -> new FunicBreakpointListener(project));
        breakpointManager.removeBreakpointListener(lineBreakpointType,breakpointListener);
        log.warn("addBreakpointListener-3,remove");
        breakpointManager.addBreakpointListener(lineBreakpointType, breakpointListener);
        log.warn("addBreakpointListener-4,add");
    }

    public static PsiMethod getBreakpointMethod(Project project) {
        log.warn("getBreakpointMethod-0,project="+project);
        String projectFilePath = project.getProjectFilePath();
        PsiMethod ret = METHOD_MAP.get(projectFilePath);
        log.warn("getBreakpointMethod-1,cache="+ret);
        if(ret!=null){
            return ret;
        }
        List<PsiClass> classList = PsiBreakpointUtil.getPsiClass(project, REPORTER_SIMPLE_CLASS_NAME);
        log.warn("getBreakpointMethod-2,size="+classList.size());
        PsiClass psiClass = classList.get(0);
        log.warn("getBreakpointMethod-3,class="+psiClass);
        List<PsiMethod> methodList = PsiBreakpointUtil.getPsiMethod(psiClass, REPORT_METHOD_NAME, 3);
        log.warn("getBreakpointMethod-4,size="+methodList.size());
        PsiMethod psiMethod = methodList.get(0);
        log.warn("getBreakpointMethod-5,method="+psiMethod);
        METHOD_MAP.put(projectFilePath,psiMethod);
        return psiMethod;
    }


    public static void updateBreakpointCondition(Project project) {
        log.warn("updateBreakpointCondition-0,project="+project);
        String projectFilePath = project.getProjectFilePath();

        PsiMethod psiMethod=getBreakpointMethod(project);

        log.warn("updateBreakpointCondition-1,method="+psiMethod);
        PsiParameterList parameterList = psiMethod.getParameterList();
        PsiParameter[] parameters = parameterList.getParameters();
        log.warn("updateBreakpointCondition-2,length="+parameters.length);
        PsiParameter fileNameParameter = parameters[0];
        PsiParameter lineNumberParameter = parameters[1];
        PsiParameter variableMapParameter = parameters[2];

        String fileNameParameterName = fileNameParameter.getName();
        String lineNumberParameterName = lineNumberParameter.getName();
        String variableMapParameterName = variableMapParameter.getName();

        log.warn("updateBreakpointCondition-3,fileName="+fileNameParameterName+",lineNumber="+lineNumberParameterName+",variableMap="+variableMapParameterName);
        StringBuilder builder=new StringBuilder();
        builder.append("1==0").append("\n");
        Map<String, Map.Entry<String,Integer>> breakMap = BREAKPOINT_MAP.computeIfAbsent(projectFilePath, k -> new ConcurrentHashMap<>());
        for (Map.Entry<String, Map.Entry<String,Integer>> unqEntry : breakMap.entrySet()) {
            Map.Entry<String, Integer> entry = unqEntry.getValue();
            // || (fileNameParameterName.equals(entry.getKey()) && lineNumberParameterName == entry.getValue())
            builder.append(" || (")
                    .append(fileNameParameterName).append(".equals(\"").append(entry.getKey()).append("\")")
                    .append(" && ").append(lineNumberParameterName).append(" == ").append(entry.getValue()+1)
                    .append(")").append("\n");
        }

        String condition = builder.toString();
        log.warn("updateBreakpointCondition-4,condition="+condition);
        PsiBreakpointUtil.addOrUpdateConditionBreakpoint(psiMethod,condition);

    }



}
