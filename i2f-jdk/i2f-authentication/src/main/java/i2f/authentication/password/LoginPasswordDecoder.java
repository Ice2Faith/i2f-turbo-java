package i2f.authentication.password;

/**
 * 使用在前端传输密码时使用
 * @author Ice2Faith
 * @date 2022/4/8 17:49
 * @desc
 */
public interface LoginPasswordDecoder {
    String decode(String encodePassword);
}
