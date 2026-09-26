package i2f.springboot.ops.openai.tts.cmd;

/**
 * @author Ice2Faith
 * @date 2026/9/16 19:39
 * @desc
 */
public interface CommandTtsSpeaker {
    boolean available();

    int prior();

    void speak(String text) throws Throwable;
}
