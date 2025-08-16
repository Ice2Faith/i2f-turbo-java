package i2f.swl.std;

/**
 * @author Ice2Faith
 * @date 2024/7/9 18:46
 * @desc
 */
public interface ISwlMessageDigester {
    String digest(String data);

    boolean verify(String digest, String data);
}
