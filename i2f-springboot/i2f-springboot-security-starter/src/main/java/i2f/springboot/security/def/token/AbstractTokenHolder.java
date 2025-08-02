package i2f.springboot.security.def.token;

import i2f.cache.std.expire.IExpireCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2022/4/7 11:05
 * @desc
 */
@Slf4j
public abstract class AbstractTokenHolder {
    public static final String TOKEN_PREFIX = "TOKEN_";
    public static final String TOKEN_USER_PREFIX = "TKUSER_";

    public String getTokenPrefix() {
        return TOKEN_PREFIX;
    }

    public String getTokenUserPrefix() {
        return TOKEN_USER_PREFIX;
    }

    protected abstract int getExpireTime();

    protected abstract TimeUnit getExpireTimeUnit();

    protected abstract IExpireCache<String, Object> getCache();

    public void setToken(String token, UserDetails obj) {
        getCache().set(getTokenPrefix() + token, obj, getExpireTime(), getExpireTimeUnit());
    }

    public UserDetails getToken(String token) {
        return (UserDetails) getCache().get(getTokenPrefix() + token);
    }

    public void refreshToken(String token) {
        getCache().expire(getTokenPrefix() + token, getExpireTime(), getExpireTimeUnit());
    }

    public void removeToken(String token) {
        getCache().remove(getTokenPrefix() + token);
    }

    public void setSingleToken(String username, String token, UserDetails obj) {
        String oldToken = (String) getCache().get(getTokenUserPrefix() + username);
        if (oldToken != null && !"".equals(oldToken)) {
            removeToken(oldToken);
        }
        getCache().set(getTokenUserPrefix() + username, token);
        setToken(token, obj);
    }

    public void removeSingleToken(String username, String token) {
        String oldToken = (String) getCache().get(getTokenUserPrefix() + username);
        if (oldToken != null && !"".equals(oldToken)) {
            removeToken(token);
        }
        getCache().remove(getTokenUserPrefix() + username);
    }
}
