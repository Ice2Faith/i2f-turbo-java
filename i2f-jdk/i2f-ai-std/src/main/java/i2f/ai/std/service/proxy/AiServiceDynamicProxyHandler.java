package i2f.ai.std.service.proxy;

import i2f.ai.std.agent.AiAgent;
import i2f.ai.std.agent.AiAgentContext;
import i2f.ai.std.agent.AiAgentResponse;
import i2f.ai.std.model.AiRequest;
import i2f.ai.std.model.message.AiMessage;
import i2f.ai.std.service.annotations.*;
import i2f.ai.std.tool.ToolRawDefinition;
import i2f.ai.std.tool.ToolRawHelper;
import i2f.ai.std.tool.schema.JsonSchemaAnnotationResolver;
import i2f.context.std.INamingContext;
import i2f.invokable.IInvokable;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.match.regex.RegexUtil;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.reflect.vistor.Visitor;
import i2f.typeof.TypeOf;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/3/30 9:11
 * @desc
 */
@Data
@NoArgsConstructor
public class AiServiceDynamicProxyHandler implements IProxyInvocationHandler {
    protected INamingContext context;
    protected JsonSchemaAnnotationResolver resolver = JsonSchemaAnnotationResolver.INSTANCE;

    public AiServiceDynamicProxyHandler(INamingContext context) {
        this.context = context;
    }

    public AiServiceDynamicProxyHandler(INamingContext context, JsonSchemaAnnotationResolver resolver) {
        this.context = context;
        this.resolver = resolver;
    }

    @Override
    public Object invoke(Object proxy, IInvokable invokable, Object... args) throws Throwable {

        if (!(invokable instanceof JdkMethod)) {
            throw new IllegalArgumentException("only supports JdkMethod");
        }
        JdkMethod jdkMethod = (JdkMethod) invokable;
        Method method = jdkMethod.getMethod();

        // 处理默认方法，default 实现的方法不进行代理
        // 这里就允许使用默认方法实现一些 Tools(function-calling) 方法
        if (method.isDefault()) {
            // 直接使用 method.invoke(proxy,args)
            // 会导致无限自身递归调用，最终栈溢出
            // Java 8 兼容写法
            // 通过反射获取 MethodHandle
            MethodHandles.Lookup lookup = MethodHandles.lookup();

            // 获取特殊调用的 MethodHandle (unreflectSpecial)
            // 这里的关键是指定查找的起点是接口，并且绑定 this 为 proxy
            MethodHandle mh = lookup.findSpecial(
                    method.getDeclaringClass(),
                    method.getName(),
                    MethodType.methodType(method.getReturnType(), method.getParameterTypes()),
                    method.getDeclaringClass()
            );

            // 绑定实例（proxy）并调用
            return mh.bindTo(proxy).invokeWithArguments(args);
        }

        AiAgent agent = null;
        AiAgentContext agentContext = null;
        AiRequest request = new AiRequest();
        Set<String> includeToolTags = new HashSet<>();
        Set<String> includeSkillTags = new HashSet<>();

        // 先解析参数的名称和可能存在的描述
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = new String[parameters.length];
        String[] parameterDescriptions = new String[parameters.length];
        Set<String> methodUsedParameterSet = new HashSet<>();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            parameterNames[i] = parameter.getName();
            AiParam paramAnn = parameter.getAnnotation(AiParam.class);
            if (paramAnn != null) {
                String value = paramAnn.value();
                if (value != null && !value.isEmpty()) {
                    parameterNames[i] = value;
                }
                String description = paramAnn.description();
                if (description != null && !description.isEmpty()) {
                    parameterDescriptions[i] = description;
                }
            }
            if (args[i] instanceof AiAgent) {
                agent = (AiAgent) args[i];
                methodUsedParameterSet.add(parameterNames[i]);
            }
            if (args[i] instanceof AiAgentContext) {
                agentContext = (AiAgentContext) args[i];
                methodUsedParameterSet.add(parameterNames[i]);
            }
            if (args[i] instanceof AiRequest) {
                request = (AiRequest) args[i];
                methodUsedParameterSet.add(parameterNames[i]);
            }

        }

        if (agentContext == null) {
            agentContext = new AiAgentContext();
        }

        // 得到参数名 Map
        Map<String, Object> paramMap = new LinkedHashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            paramMap.put(parameterNames[i], args[i]);
        }

        // 类上允许定义一些默认值或者兜底值
        Class<?> declareClass = method.getDeclaringClass();
        AiService serviceAnn = declareClass.getAnnotation(AiService.class);

        // AiAgent 查找策略，优先以 method 上指定的为准，其次按照 class 类上指定的为准，最后从上下文中选取一个
        if (agent == null) {
            List<AiAgents> agentList = new ArrayList<>();
            AiAgents methodAgentAnn = method.getAnnotation(AiAgents.class);
            if (methodAgentAnn != null) {
                agentList.add(methodAgentAnn);
            }
            if (serviceAnn != null) {
                AiAgents[] arr = serviceAnn.agent();
                for (AiAgents item : arr) {
                    if (item == null) {
                        continue;
                    }
                    agentList.add(item);
                }
            }
            for (AiAgents agentAnn : agentList) {
                if (agent == null) {
                    try {
                        String agentName = agentAnn.value();
                        if (agentName != null && !agentName.isEmpty()) {
                            Object bean = context.getBean(agentName);
                            if (bean != null) {
                                agent = (AiAgent) bean;
                                break;
                            }
                        }
                    } catch (Exception e) {

                    }
                }
                if (agent == null) {
                    try {
                        Class<?> agentClass = agentAnn.clazz();
                        if (agentClass != null) {
                            Object bean = context.getBean(agentClass);
                            if (bean != null) {
                                agent = (AiAgent) bean;
                                break;
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }

        if (agent == null) {
            agent = context.getBean(AiAgent.class);
        }


        // 处理方法上的 system 提示词
        AiSystem sysAnn = method.getAnnotation(AiSystem.class);
        if (sysAnn != null) {
            String[] value = sysAnn.value();
            if (value != null && value.length > 0) {
                String text = String.join("\n", value);
                if (sysAnn.format()) {
                    text = RegexUtil.regexFindAndReplace(text, "\\$\\{\\s*[^}]+\\s*\\}", v -> {
                        v = v.substring(2, v.length() - 1).trim();
                        methodUsedParameterSet.add(v);
                        Object val = Visitor.visit(v, paramMap);
                        if (val == null) {
                            return "";
                        }
                        return String.valueOf(val);
                    });
                }
                if (!text.isEmpty()) {
                    request.system(text);
                }
            }
        }

        // 处理方法上的 user 提示词，
        // 这种用法应该比较少，一般应该都是启用 format 来进行的按模版的提示词
        AiUser userAnn = method.getAnnotation(AiUser.class);
        if (userAnn != null) {
            String[] value = userAnn.value();
            if (value != null && value.length > 0) {
                String text = String.join("\n", value);
                if (userAnn.format()) {
                    text = RegexUtil.regexFindAndReplace(text, "\\$\\{\\s*[^}]+\\s*\\}", v -> {
                        v = v.substring(2, v.length() - 1).trim();
                        methodUsedParameterSet.add(v);
                        Object val = Visitor.visit(v, paramMap);
                        if (val == null) {
                            return "";
                        }
                        return String.valueOf(val);
                    });
                }
                if (!text.isEmpty()) {
                    request.user(text);
                }
            }
        }

        // 处理 Tools(function-calling) 使用后注册覆盖先注册的Map规则
        // 先注册类上的，再注册方法上的，最后注册被代理的同类中的
        List<AiTools> toolList = new ArrayList<>();
        AiTools methodToolsAnn = method.getAnnotation(AiTools.class);
        if (serviceAnn != null) {
            AiTools[] arr = serviceAnn.tools();
            for (AiTools item : arr) {
                if (item == null) {
                    continue;
                }
                toolList.add(item);
            }
        }
        if (methodToolsAnn != null) {
            toolList.add(methodToolsAnn);
        }
        for (AiTools toolsAnn : toolList) {
            String[] tags = toolsAnn.tags();
            if (tags != null) {
                includeToolTags.addAll(Arrays.asList(tags));
            }
            String[] value = toolsAnn.value();
            if (value != null) {
                for (String name : value) {
                    if (name == null || name.isEmpty()) {
                        continue;
                    }
                    try {
                        Object bean = context.getBean(name);
                        Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(resolver, bean);
                        request.tools(map);
                    } catch (Exception e) {

                    }
                }
            }
            Class<?>[] classes = toolsAnn.classes();
            if (classes != null) {
                for (Class<?> clazz : classes) {
                    if (clazz == null || Object.class.equals(clazz)) {
                        continue;
                    }
                    Object bean = null;
                    try {
                        bean = context.getBean(clazz);
                    } catch (Exception e) {

                    }
                    if (bean == null) {
                        try {
                            bean = clazz.newInstance();
                        } catch (Exception e) {

                        }
                    }
                    if (bean != null) {
                        Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(resolver, bean);
                        request.tools(map);
                    }
                }
            }
        }

        Map<String, ToolRawDefinition> selfToolMap = ToolRawHelper.parseTools(resolver, proxy);
        request.tools(selfToolMap);


        // 处理 Skills 使用后注册覆盖先注册的Map规则
        // 先注册类上的，再注册方法上的，最后注册被代理的同类中的
        List<AiSkills> skillList = new ArrayList<>();
        AiSkills methodSkillsAnn = method.getAnnotation(AiSkills.class);
        if (methodSkillsAnn != null) {
            skillList.add(methodSkillsAnn);
        }
        if (serviceAnn != null) {
            AiSkills[] arr = serviceAnn.skills();
            for (AiSkills item : arr) {
                if (item == null) {
                    continue;
                }
                skillList.add(item);
            }
        }
        Boolean enableSkill = null;
        for (AiSkills skillsAnn : skillList) {
            if (enableSkill == null) {
                enableSkill = skillsAnn.enable();
            }
            String[] tags = skillsAnn.tags();
            if (tags != null) {
                includeSkillTags.addAll(Arrays.asList(tags));
            }
        }

        // 处理参数，参数中未被特殊使用的参数，需要额外加入用户提示词
        StringBuilder parameterBuilder = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String paramName = parameterNames[i];

            AiSystem paramSystemAnn = parameter.getAnnotation(AiSystem.class);
            if (paramSystemAnn != null) {
                Object bean = args[i];
                if (bean != null) {
                    request.system(String.valueOf(bean));
                    continue;
                }
            }

            AiUser paramUserAnn = parameter.getAnnotation(AiUser.class);
            if (paramUserAnn != null) {
                Object bean = args[i];
                if (bean != null) {
                    request.user(String.valueOf(bean));
                    continue;
                }
            }

            AiTools paramToolAnn = parameter.getAnnotation(AiTools.class);
            if (paramToolAnn != null) {
                String[] tags = paramToolAnn.tags();
                if (tags != null) {
                    includeToolTags.addAll(Arrays.asList(tags));
                }
                Object bean = args[i];
                if (bean != null) {
                    if (bean instanceof Iterable) {
                        Iterable<?> list = (Iterable<?>) bean;
                        for (Object item : list) {
                            if (item == null) {
                                continue;
                            }
                            if (item instanceof CharSequence) {
                                String name = String.valueOf(item);
                                try {
                                    Object val = context.getBean(name);
                                    Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(resolver, val);
                                    request.tools(map);
                                } catch (Exception e) {

                                }
                            } else if (item instanceof Class) {
                                Class<?> name = (Class<?>) item;
                                try {
                                    Object val = context.getBean(name);
                                    Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(resolver, val);
                                    request.tools(map);
                                } catch (Exception e) {

                                }
                            } else {
                                Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(resolver, item);
                                request.tools(map);
                            }
                        }
                    } else if (bean.getClass().isArray()) {
                        int len = Array.getLength(bean);
                        for (int j = 0; j < len; j++) {
                            Object item = Array.get(bean, j);
                            if (item == null) {
                                continue;
                            }
                            if (item instanceof CharSequence) {
                                String name = String.valueOf(item);
                                try {
                                    Object val = context.getBean(name);
                                    Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(resolver, val);
                                    request.tools(map);
                                } catch (Exception e) {

                                }
                            } else if (item instanceof Class) {
                                Class<?> name = (Class<?>) item;
                                try {
                                    Object val = context.getBean(name);
                                    Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(resolver, val);
                                    request.tools(map);
                                } catch (Exception e) {

                                }
                            } else {
                                Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(resolver, item);
                                request.tools(map);
                            }
                        }
                    } else if (bean instanceof CharSequence) {
                        String name = String.valueOf(bean);
                        try {
                            Object val = context.getBean(name);
                            Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(resolver, val);
                            request.tools(map);
                        } catch (Exception e) {

                        }
                    } else {
                        Map<String, ToolRawDefinition> map = ToolRawHelper.parseTools(resolver, bean);
                        request.tools(map);
                    }
                    continue;
                }
            }


            if (!methodUsedParameterSet.contains(paramName)) {
                parameterBuilder.append("-------------------------------").append("\n");
                parameterBuilder.append("## arg name: ").append(paramName).append("\n");
                String paramDesc = parameterDescriptions[i];
                if (paramDesc != null && !paramDesc.isEmpty()) {
                    parameterBuilder.append("### arg desc: ").append(paramDesc).append("\n");
                }
                parameterBuilder.append("### arg json value:").append("\n");
                parameterBuilder.append(agent.getJsonSerializer().serialize(args[i]));
            }

        }

        if (parameterBuilder.length() != 0) {
            request.user("\n# arg list\n" + parameterBuilder + "\n");
        }

        boolean structOutput = false;
        Class<?> returnType = method.getReturnType();
        if (Object.class.equals(returnType)
                || Void.class.equals(returnType)) {
            structOutput = false;
        } else if (TypeOf.typeOf(returnType, AiAgentResponse.class)) {
            structOutput = false;
        } else if (TypeOf.typeOf(returnType, AiMessage.class)) {
            structOutput = false;
        } else if (TypeOf.typeOf(returnType, CharSequence.class)) {
            structOutput = false;
        } else {
            structOutput = true;
        }


        agentContext.enableStructOutput(structOutput)
                .outputType(returnType);

        if (enableSkill != null) {
            agentContext.enableSkills(enableSkill);
        }
        agentContext.addSkillTagsFilter((tags) -> {
            return AiAgentContext.hasAnyTagsFilter(tags, includeSkillTags);
        });

        agentContext.addSkillTagsFilter((tags) -> {
            return AiAgentContext.hasAnyTagsFilter(tags, includeToolTags);
        });


        AiAgentResponse response = agent.generate(request, agentContext);

        if (Object.class.equals(returnType)
                || Void.class.equals(returnType)) {
            return response;
        } else if (TypeOf.typeOf(returnType, AiAgentResponse.class)) {
            return response;
        } else if (TypeOf.typeOf(returnType, AiMessage.class)) {
            return response.last();
        } else if (TypeOf.typeOf(returnType, CharSequence.class)) {
            return response.last().text();
        } else if (structOutput) {
            String json = response.last().text();
            return agent.getJsonSerializer().deserialize(json, returnType);
        } else {
            throw new IllegalArgumentException("un-support return type: " + returnType);
        }
    }


}
