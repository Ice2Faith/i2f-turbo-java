package i2f.reflect.test;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/4/22 17:30
 * @desc
 */
public class TestUser {
    private List<TestRole> roles;

    public List<TestRole> getRoles() {
        return roles;
    }

    public TestUser setRoles(List<TestRole> roles) {
        this.roles = roles;
        return this;
    }
}
