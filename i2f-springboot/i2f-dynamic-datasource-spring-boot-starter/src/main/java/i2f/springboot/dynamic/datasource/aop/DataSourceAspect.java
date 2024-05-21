package i2f.springboot.dynamic.datasource.aop;

import i2f.springboot.dynamic.datasource.autoconfiguration.DynamicDataSourceConfig;
import i2f.springboot.dynamic.datasource.core.DynamicDataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;

import java.util.Objects;

/**
 * 多数据源处理
 *
 * @author Ice2Faith
 */
@ConditionalOnExpression("${" + DynamicDataSourceConfig.CONFIG_PREFIX + ".aop.enable:true}")
@Aspect
@Order(1)
public class DataSourceAspect {
    protected Logger logger = LoggerFactory.getLogger(getClass());


    @Pointcut("@annotation(i2f.springboot.dynamic.datasource.aop.DataSource)"
            + "|| @within(i2f.springboot.dynamic.datasource.aop.DataSource)")
    public void dsPointCut() {

    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        DataSource dataSource = getDataSource(point);

        if (dataSource != null) {
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value());
        }

        try {
            return point.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public DataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource)) {
            return dataSource;
        }

        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}
