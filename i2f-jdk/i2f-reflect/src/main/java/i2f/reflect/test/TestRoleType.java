package i2f.reflect.test;

/**
 * @author Ice2Faith
 * @date 2024/4/22 17:30
 * @desc
 */
public class TestRoleType<U, T> {
    private String name;
    private U key;
    private T perms;


    public String getName() {
        return name;
    }

    public TestRoleType setName(String name) {
        this.name = name;
        return this;
    }

    public U getKey() {
        return key;
    }

    public TestRoleType setKey(U key) {
        this.key = key;
        return this;
    }

    public T getPerms() {
        return perms;
    }

    public TestRoleType setPerms(T perms) {
        this.perms = perms;
        return this;
    }
}
