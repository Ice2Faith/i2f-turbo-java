package i2f.reflect.test;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/4/22 17:28
 * @desc
 */
public class TestContext {
    private String name;
    private List<TestContextPerm> perm;
    private TestContextCategory category;
    private TestContextRequires requires;

    public String getName() {
        return name;
    }

    public TestContext setName(String name) {
        this.name = name;
        return this;
    }

    public List<TestContextPerm> getPerm() {
        return perm;
    }

    public TestContext setPerm(List<TestContextPerm> perm) {
        this.perm = perm;
        return this;
    }

    public TestContextCategory getCategory() {
        return category;
    }

    public TestContext setCategory(TestContextCategory category) {
        this.category = category;
        return this;
    }

    public TestContextRequires getRequires() {
        return requires;
    }

    public TestContext setRequires(TestContextRequires requires) {
        this.requires = requires;
        return this;
    }
}
