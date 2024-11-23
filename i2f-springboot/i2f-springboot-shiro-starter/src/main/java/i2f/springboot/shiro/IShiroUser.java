package i2f.springboot.shiro;

import i2f.authentication.user.IRbacAuthUser;

import java.util.Set;

/**
 * @author ltb
 * @date 2022/4/21 16:26
 * @desc
 */
public interface IShiroUser extends IRbacAuthUser {
    String getSalt();
}
