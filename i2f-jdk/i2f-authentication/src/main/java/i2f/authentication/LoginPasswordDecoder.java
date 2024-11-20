package i2f.authentication;

/**
 * @author ltb
 * @date 2022/4/8 17:49
 * @desc
 */
public interface LoginPasswordDecoder {
    String decode(String encodePassword);
}
