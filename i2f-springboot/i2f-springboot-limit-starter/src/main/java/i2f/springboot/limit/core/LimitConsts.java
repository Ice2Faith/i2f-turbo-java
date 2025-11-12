package i2f.springboot.limit.core;

/**
 * @author Ice2Faith
 * @date 2025/11/12 14:42
 */
public interface LimitConsts {
    String KEY_BASE="limit";
    String KEY_RULE=KEY_BASE+":rule:";
    String KEY_TARGET=KEY_BASE+":target:";

    String DEFAULT_APP_NAME="default";

    String LIMIT_REQUEST_USER_ID="limit-request-user-id";
}
