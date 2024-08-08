package i2f.swl.core.exchanger;

/**
 * @author Ice2Faith
 * @date 2024/8/4 21:46
 * @desc
 */
public interface SwlNonceManager {
    boolean contains(String nonce);

    void set(String nonce, long timeoutSeconds);
}
