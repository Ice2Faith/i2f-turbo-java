package i2f.authentication.user.impl;

import i2f.authentication.user.IRbacAuthUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2024/11/20 20:30
 * @desc
 */
@Data
@NoArgsConstructor
public class BasicRbacAuthUser extends BasicAuthUser implements IRbacAuthUser, Serializable {
    protected Set<String> roles = new HashSet<>();
    protected Set<String> permissions = new HashSet<>();
}
