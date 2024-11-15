package i2f.verifycode.data;

import i2f.verifycode.consts.VerifyCodeType;
import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * @author Ice2Faith
 * @date 2023/8/14 20:29
 * @desc
 */
@Data
public class VerifyCodeStdDto<T> {
    private String question;
    private BufferedImage img;
    private T result;
    private int type = VerifyCodeType.INPUT.code();
    private int count = 0;
}
