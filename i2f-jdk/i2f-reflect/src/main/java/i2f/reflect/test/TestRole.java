package i2f.reflect.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/4/22 17:30
 * @desc
 */
public class TestRole {
    private String name;
    private String key;
    private List<String> perms;

    public List<String> getKeys(String name, String perm) {
        List<String> ret = new ArrayList<>();
        if (!this.name.equals(name)) {
            ret.add("view");
            return ret;
        }
        if (!this.perms.contains(perm)) {
            ret.add("view");
            return ret;
        }
        ret.add(perm);
        ret.add("view");
        return ret;
    }

    public String getName() {
        return name;
    }

    public TestRole setName(String name) {
        this.name = name;
        return this;
    }

    public String getKey() {
        return key;
    }

    public TestRole setKey(String key) {
        this.key = key;
        return this;
    }

    public List<String> getPerms() {
        return perms;
    }

    public TestRole setPerms(List<String> perms) {
        this.perms = perms;
        return this;
    }
}
