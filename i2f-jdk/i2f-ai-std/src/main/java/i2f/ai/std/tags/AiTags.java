package i2f.ai.std.tags;

/**
 * @author Ice2Faith
 * @date 2026/4/1 22:15
 * @desc
 */
public enum AiTags {
    READONLY("readonly", "只读的，描述操作是只读的，不会发生变更"),
    EXECUTABLE("executable", "可执行的，描述操作是可执行的，可能导致执行一些能力"),
    WRITABLE("writable", "可写的，描述操作是可写的，会发生变更"),

    PUBLIC_NET("public_net", "公共网络，或者公网环境，描述操作设计到公共网络"),
    PRIVATE_NET("private_net", "私有网络，或者内部环境，描述操作在可控网络内部"),

    HUMAN("human", "人类参与的，描述操作需要人类参与其中进行决策或者控制等"),

    SANDBOX("sandbox", "沙箱控制的，表示操作将会在沙箱中进行，在一定程度范围内事可控的");

    private String tag;
    private String description;

    private AiTags(String tag, String description) {
        this.tag = tag;
        this.description = description;
    }

    public String tag() {
        return tag;
    }

    public String description() {
        return description;
    }

}
