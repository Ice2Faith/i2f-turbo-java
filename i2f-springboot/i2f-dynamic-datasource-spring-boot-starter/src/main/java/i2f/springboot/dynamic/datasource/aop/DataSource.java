package i2f.springboot.dynamic.datasource.aop;

import i2f.springboot.dynamic.datasource.core.LookupBalanceType;

import java.lang.annotation.*;

/**
 * 自定义多数据源切换注解
 * <p>
 * 优先级：先方法，后类，如果方法覆盖了类上的数据源类型，以方法的为准，否则以类上的为准
 *
 * @author Ice2Faith
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    /**
     * 切换数据源名称
     * 这个名称就是数据源ID，也就是在配置中表明的：
     * spring.datasource.multiply.{datasourceId}.url=
     * 中的datasourceId
     */
    String value() default DataSourceType.MASTER;

    /**
     * 是否是一个分组，如果是分组，则执行负载策略
     *
     * @return
     */
    boolean group() default false;

    /**
     * 指定为group时的选定策略
     *
     * @return
     */
    LookupBalanceType balance() default LookupBalanceType.UNKNOWN;
}
