package i2f.ai.std.model.message;

/**
 * @author Ice2Faith
 * @date 2026/3/25 15:21
 * @desc
 */
public interface AiMessage {
    public enum Type {
        SYSTEM, ASSISTANT, TOOL, USER
    }

    Type type();

    String text();

    Object rawMessage();
}
