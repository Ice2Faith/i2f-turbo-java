package i2f.ai.std.tool.intent;

/**
 * @author Ice2Faith
 * @date 2026/8/31 15:23
 * @desc
 */
public enum ToolIntents implements IToolIntent {
    FILE("file", "file operation"),
    COMMAND("command", "command line operation"),
    DATABASE("database", "database operation"),
    RAG("rag", "rag knowledge base operation"),
    ;

    private String label;
    private String description;

    private ToolIntents(String label, String description) {
        this.label = label;
        this.description = description;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public String description() {
        return description;
    }

}
