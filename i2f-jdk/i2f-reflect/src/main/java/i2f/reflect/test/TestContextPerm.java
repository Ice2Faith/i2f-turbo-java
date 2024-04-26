package i2f.reflect.test;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/4/22 17:29
 * @desc
 */
public class TestContextPerm {
    private List<String> perms;

    public String get(int index) {
        return this.perms.get(index);
    }

    public List<String> getPerms() {
        return perms;
    }

    public TestContextPerm setPerms(List<String> perms) {
        this.perms = perms;
        return this;
    }
}
