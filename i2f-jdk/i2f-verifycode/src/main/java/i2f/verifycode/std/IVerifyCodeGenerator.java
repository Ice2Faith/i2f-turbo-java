package i2f.verifycode.std;


import i2f.verifycode.data.VerifyCodeDto;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2023/8/14 20:28
 * @desc
 */
public interface IVerifyCodeGenerator {
    VerifyCodeDto generate(int width, int height, Map<String, Object> params);

    boolean verify(String result, String answer);
}
