package i2f.springboot.ops.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/2 22:02
 * @desc
 */
@Data
@NoArgsConstructor
public class OpsSecureReturn<T> {
    public static final int SUCCESS=200;
    public static final int ERROR=500;
    protected int code;
    protected String msg;
    protected T data;
    public OpsSecureReturn(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> OpsSecureReturn<T> success(T data) {
        return new OpsSecureReturn<T>(SUCCESS, "success", data);
    }
    public static <T> OpsSecureReturn<T> error(String msg) {
        return new OpsSecureReturn<T>(ERROR, msg, null);
    }
}
