package i2f.springboot.ops.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/11/2 22:02
 * @desc
 */
@Data
@NoArgsConstructor
public class OpsSecureReturn<T> {
    public static final int SUCCESS = 200;
    public static final int ERROR = 500;
    protected int code;
    protected String msg;
    protected T data;
    protected Map<String,Object> attrs;

    public OpsSecureReturn(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public OpsSecureReturn<T> withAttr(String key,Object value){
        if(attrs==null){
            attrs=new LinkedHashMap<>();
        }
        attrs.put(key,value);
        return this;
    }

    public static <T> OpsSecureReturn<T> success(T data) {
        return new OpsSecureReturn<T>(SUCCESS, "success", data);
    }

    public static <T> OpsSecureReturn<T> error(String msg) {
        return new OpsSecureReturn<T>(ERROR, msg, null);
    }

    public static <T> OpsSecureReturn<T> error(String msg, Throwable e) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        e.printStackTrace(ps);
        ps.flush();
        String errmsg = new String(bos.toByteArray());
        return error((msg == null ? "" : (msg + "\n")) + errmsg);
    }

    public static <T> OpsSecureReturn<T> error(Throwable e) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        e.printStackTrace(ps);
        ps.flush();
        String errmsg = new String(bos.toByteArray());
        return error(errmsg);
    }
}
