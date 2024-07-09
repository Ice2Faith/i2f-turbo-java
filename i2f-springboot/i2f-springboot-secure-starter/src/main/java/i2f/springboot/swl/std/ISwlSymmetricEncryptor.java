package i2f.springboot.swl.std;

/**
 * @author Ice2Faith
 * @date 2024/7/9 18:41
 * @desc
 */
public interface ISwlSymmetricEncryptor {
    String generateKey();

    String getKey();

    void setKey(String key);

    String encrypt(String data);

    String decrypt(String data);
}
