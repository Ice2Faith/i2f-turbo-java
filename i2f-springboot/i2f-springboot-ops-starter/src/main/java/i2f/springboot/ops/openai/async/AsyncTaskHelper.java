package i2f.springboot.ops.openai.async;

import i2f.codec.bytes.raw.HexStringByteCodec;
import i2f.springboot.ops.openai.tool.impl.UrlSigner;
import i2f.url.FormUrlEncodedEncoder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/9/29 20:59
 * @desc
 */
@Data
@NoArgsConstructor
@Slf4j
@Component
public class AsyncTaskHelper {
    public static final String PROTOCOL = "task";

    @Autowired
    private UrlSigner urlSigner;


    public String signTask(AsyncTaskItem item) {
        String parameters = FormUrlEncodedEncoder.toForm(item.getTaskParameters());
        parameters = HexStringByteCodec.INSTANCE.encode(parameters.getBytes(StandardCharsets.UTF_8));
        String url = PROTOCOL + "://" + item.getTaskId() + "/" + item.getType() + "/" + item.getResultType() + "/" + parameters;

        return urlSigner.signedUrl(url);
    }

    public AsyncTaskItem verifyTask(String url) {
        AsyncTaskItem ret = new AsyncTaskItem();
        String originUrl = urlSigner.verifyUrl(url);
        String[] arr = originUrl.split("://", 2);
        if (arr.length != 2) {
            throw new IllegalArgumentException("bad url");
        }
        String protocol = arr[0];
        String path = arr[1];
        if (!PROTOCOL.equals(protocol)) {
            throw new IllegalArgumentException("bad protocol, only support `" + PROTOCOL + "`");
        }

        arr = path.split("/");
        if (arr.length != 4) {
            throw new IllegalArgumentException("bad url structure");
        }
        ret.setTaskId(arr[0]);
        ret.setType(arr[1]);
        ret.setResultType(arr[2]);
        ret.setTaskParameters(new HashMap<>());
        String parameters = arr[3];
        if (parameters != null && !parameters.isEmpty()) {
            parameters = new String(HexStringByteCodec.INSTANCE.decode(parameters), StandardCharsets.UTF_8);
            Map<String, Object> map = FormUrlEncodedEncoder.ofFormMapTree(parameters);
            if (map != null) {
                ret.setTaskParameters(map);
            }
        }
        ret.setStatus(AsyncTaskItem.Status.RUNNING);

        return ret;
    }
}
