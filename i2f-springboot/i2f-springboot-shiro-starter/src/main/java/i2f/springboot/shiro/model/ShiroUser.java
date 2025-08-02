package i2f.springboot.shiro.model;

import i2f.authentication.user.impl.BasicRbacAuthUser;
import i2f.springboot.shiro.IShiroUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Ice2Faith
 * @date 2022/4/21 17:38
 * @desc
 */
@Data
@NoArgsConstructor
public class ShiroUser extends BasicRbacAuthUser implements IShiroUser, Serializable {
    private String salt;
}
