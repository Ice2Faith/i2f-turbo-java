package i2f.authentication.user;

import java.util.Collection;

/**
 * @author Ice2Faith
 * @date 2024/11/20 20:28
 * @desc
 */
public interface IRbacAuthUser extends IAuthUser {
    Collection<String> getRoles();

    Collection<String> getPermissions();
}
