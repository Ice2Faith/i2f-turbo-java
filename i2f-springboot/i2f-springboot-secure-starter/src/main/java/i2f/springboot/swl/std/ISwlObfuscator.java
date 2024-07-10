package i2f.springboot.swl.std;

/**
 * @author Ice2Faith
 * @date 2024/7/10 10:33
 * @desc
 */
public interface ISwlObfuscator {
    String encode(String data);

    String decode(String data);
}
