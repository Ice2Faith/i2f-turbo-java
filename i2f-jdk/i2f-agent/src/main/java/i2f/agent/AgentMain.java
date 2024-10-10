package i2f.agent;


import i2f.agent.transformer.SystemLoadedClassesPrintTransformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;
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
        Map<String, Set<String>> actionPattens = AgentUtil.parseClassPattenMap(arg);

        List<ClassFileTransformer> transformers = new ArrayList<>();
        transformers.add(new SystemLoadedClassesPrintTransformer());
        AgentUtil.addTransformers(inst, transformers);

    }

}
