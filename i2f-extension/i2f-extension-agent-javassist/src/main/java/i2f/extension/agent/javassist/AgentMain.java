package i2f.extension.agent.javassist;


import i2f.agent.AgentUtil;
import i2f.extension.agent.javassist.context.AgentContextHolder;
import i2f.extension.agent.javassist.evaluate.LocalFileExpressionEvaluator;
import i2f.extension.agent.javassist.transformer.file.FileUsedClassesTransformer;
import i2f.extension.agent.javassist.transformer.jdbc.ConnectionSqlClassesTransformer;
import i2f.extension.agent.javassist.transformer.jdbc.StatementSqlClassesTransformer;
import i2f.extension.agent.javassist.transformer.shutdown.ShutdownLogClassTransformer;
import i2f.extension.agent.javassist.transformer.spring.SpringApplicationContextHoldClassesTransformer;
import i2f.extension.agent.javassist.transformer.spring.SpringApplicationHoldClassesTransformer;
import i2f.extension.agent.javassist.transformer.throwables.ThrowableRecordClassTransformer;
import i2f.extension.agent.javassist.transformer.web.WebFilterThreadTraceIdClassesTransformer;
import i2f.extension.agent.javassist.transformer.web.XxeGuardClassTransformer;

import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.Set;

/**
 * @author ltb
 * @date 2022/4/3 13:02
 * @desc 一个简单的监控程序示例
 * 参数：
 * args,stat,ret@com.i2f.**,i2f.core.**&args@org.**
 * 参数解释:
 * 每一组参数使用&分隔
 * 因此上面就有两组
 * args,stat,ret@com.i2f.**,i2f.core.**
 * args@org.**
 * 每一组分为actions@full-method-ant-pattens
 * 含义为监视在一组ant-pattens定义的全限定函数名上的actions动作
 * args,stat,ret@com.i2f.**,i2f.core.**
 * 这一个就表示，监视com.i2f包和i2f.core包下面的所有方法的入参，统计，返回值
 * 可选的actions:
 * args: 监视入参
 * stat: 监视统计，调用耗时统计
 * ret: 监视返回值
 */
public class AgentMain {
    public static void premain(String arg, Instrumentation inst) {
        agentProxy(arg, inst);
    }

    public static void agentmain(String arg, Instrumentation inst) {
        agentProxy(arg, inst);
    }


    public static void agentProxy(String arg, Instrumentation inst) {
        System.out.println("AgentMain start run ! , arg is " + arg);

        AgentUtil.appendAgentJarToBootstrapClassLoaderSearch(inst);

        Map<String, Set<String>> actionPattens = AgentUtil.parseClassPattenMap(arg);
        AgentContextHolder.instrumentation = inst;
        AgentContextHolder.agentArg = arg;

        LocalFileExpressionEvaluator.initFileWatchThread(inst);

        AgentContextHolder.transformers.add(new FileUsedClassesTransformer());
        AgentContextHolder.transformers.add(new SpringApplicationContextHoldClassesTransformer());
        AgentContextHolder.transformers.add(new SpringApplicationHoldClassesTransformer());
        AgentContextHolder.transformers.add(new ShutdownLogClassTransformer());
        AgentContextHolder.transformers.add(new ThrowableRecordClassTransformer());
        AgentContextHolder.transformers.add(new WebFilterThreadTraceIdClassesTransformer());
        AgentContextHolder.transformers.add(new StatementSqlClassesTransformer());
        AgentContextHolder.transformers.add(new ConnectionSqlClassesTransformer());
        AgentContextHolder.transformers.add(new XxeGuardClassTransformer());

//        AgentContextHolder.transformers.add(new InvokeWatchClassesTransformer(actionPattens));

//        AgentContextHolder.transformers.add(new SystemLoadedClassesPrintTransformer());

        AgentUtil.addTransformers(inst, AgentContextHolder.transformers);

//        AgentUtil.retransformLoadedClasses(inst, actionPattens);

    }


}
