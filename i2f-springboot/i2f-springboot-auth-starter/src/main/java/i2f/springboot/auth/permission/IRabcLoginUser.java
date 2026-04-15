package i2f.springboot.auth.permission;

import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/4/3 10:15
 * @desc
 */
public interface IRabcLoginUser {
    Set<String> getRoles();

    Set<String> getPermissions();
}
