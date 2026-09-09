package i2f.springboot.ops.common;

/**
 * @author Ice2Faith
 * @date 2026/9/9 19:05
 * @desc
 */
public interface OpsConsts {
    String BASE_URL_PROPERTY = "i2f.springboot.ops.base-url";
    String DEFAULT_BASE_URL="/ops";
    String SPEL_BASE_URL="${"+BASE_URL_PROPERTY+":"+DEFAULT_BASE_URL+"}";
}
