package i2f.authentication.user.impl;

import i2f.authentication.user.IAuthUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Ice2Faith
 * @date 2024/11/20 20:26
 * @desc
 */
@Data
@NoArgsConstructor
public class BasicAuthUser implements IAuthUser, Serializable {
    protected String username;
    protected String password;
}
