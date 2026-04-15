package i2f.springboot.auth.permission.helper;

import i2f.springboot.auth.permission.IRabcLoginUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2026/4/3 9:41
 * @desc
 */
@Data
@NoArgsConstructor
public class RbacCheckPermissionHelper {
    protected IRabcLoginUser user;

    public RbacCheckPermissionHelper(IRabcLoginUser user) {
        this.user = user;
    }

    public boolean hasAllRoles(String... roles) {
        Set<String> list = user.getRoles();
        for (String role : roles) {
            if (!list.contains(role)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAnyRoles(String... roles) {
        Set<String> list = user.getRoles();
        for (String role : roles) {
            if (list.contains(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAllPerms(String... perms) {
        Set<String> list = user.getPermissions();
        for (String perm : perms) {
            if (!list.contains(perm)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAnyPerms(String... perms) {
        Set<String> list = user.getPermissions();
        for (String perm : perms) {
            if (list.contains(perm)) {
                return true;
            }
        }
        return false;
    }
}
