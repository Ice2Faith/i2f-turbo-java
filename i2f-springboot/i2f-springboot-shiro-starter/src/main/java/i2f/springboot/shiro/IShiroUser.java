package i2f.springboot.shiro;

import i2f.authentication.user.IRbacAuthUser;

/**
 * @author Ice2Faith
 * @date 2022/4/21 16:26
 * @desc
 */
public interface IShiroUser extends IRbacAuthUser {
    String getSalt();
}
