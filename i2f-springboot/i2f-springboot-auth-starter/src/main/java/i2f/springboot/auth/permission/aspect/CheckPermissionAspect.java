package i2f.springboot.auth.permission.aspect;

import i2f.springboot.auth.permission.annotations.CheckPermissions;
import i2f.springboot.auth.permission.exception.PermissionDenyException;
import i2f.springboot.auth.permission.properties.CheckPermissionProperties;
import i2f.springboot.auth.permission.provider.CheckPermissionContextProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 权限检查切面
 * 依次检查方法上的注解和类上的注解
 * 只有都满足注解条件，才会放行，否则抛出异常
 * 方法上的注解，用于限制此方法需要的权限
 * 类上的注解，用于限制类中所有带注解的方法都需要满足的公共权限
 * 比如，
 * 一个类是关于用户模块的，
 * 那么类上的权限就可以是，拥有用户管理模块权限
 * 方法上的权限就可以分别是添加用户、删除用户等的权限
 *
 * @author Ice2Faith
 * @date 2026/4/3 9:40
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.auth.permission.aspect.enable:true}")
@Data
@NoArgsConstructor
@Aspect
@EnableConfigurationProperties(CheckPermissionProperties.class)
@Component
public class CheckPermissionAspect implements Ordered {

    @Autowired(required = false)
    protected CheckPermissionContextProvider provider;

    @Autowired
    protected CheckPermissionProperties properties;

    @Override
    public int getOrder() {
        // 设置优先级，确保在事务切面之前执行
        return properties.getAspectOrder();
    }

    private final SpelExpressionParser parser = new SpelExpressionParser();

    @Before("@annotation(i2f.springboot.auth.permission.annotations.CheckPermissions)")
    public void checkPermission(JoinPoint joinPoint) throws Throwable {
        // 获取方法签名和注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        List<CheckPermissions> annList = new ArrayList<>();

        CheckPermissions annMethod = method.getAnnotation(CheckPermissions.class);
        if (annMethod != null) {
            annList.add(annMethod);
        }

        Class<?> clazz = method.getDeclaringClass();
        CheckPermissions annClazz = clazz.getAnnotation(CheckPermissions.class);
        if (annClazz != null) {
            annList.add(annClazz);
        }

        for (CheckPermissions ann : annList) {

            if (ann == null) {
                continue;
            }

            // 获取SPEL表达式 - 注意：这里的value可能包含#前缀也可能不包含
            String expressionStr = ann.value();

            // 没有获取到有效表达式，不进行判定
            if (expressionStr == null || expressionStr.isEmpty()) {
                continue;
            }

            // 解析SPEL表达式
            Expression expression = parser.parseExpression(expressionStr);

            // 构建EvaluationContext并添加变量
            EvaluationContext context = SimpleEvaluationContext
                    .forReadOnlyDataBinding()
                    .withInstanceMethods()
                    .build();

            if (provider == null) {
                throw new PermissionDenyException("please add a bean type of " + CheckPermissionContextProvider.class);
            }

            Map<String, Object> map = provider.getContext(joinPoint);

            // 添加变量到上下文
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }

            // 执行SPEL表达式
            Boolean result = expression.getValue(context, Boolean.class);

            // 判断结果，如果不为true则抛出异常
            if (result == null || !result) {
                throw new PermissionDenyException("permission denied")
                        .joinPoint(joinPoint)
                        .context(map);
            }

        }

        // 允许继续执行
    }


}
