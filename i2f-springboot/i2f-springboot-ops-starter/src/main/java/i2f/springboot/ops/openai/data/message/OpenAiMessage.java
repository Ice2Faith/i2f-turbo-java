package i2f.springboot.ops.openai.data.message;

/**
 * @author Ice2Faith
 * @date 2026/6/2 9:20
 * @desc
 */
public interface OpenAiMessage {
    String role();

    String content();
}
