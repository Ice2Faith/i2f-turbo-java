package i2f.codec.data;

import i2f.codec.bytes.base64.Base64UrlStringByteCodec;
import i2f.io.stream.StreamUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Ice2Faith
 * @date 2024/6/18 14:43
 * @desc data 协议
 * data:[mimeType][;codecType],dataBody
 * mimeType和codecType都是可选的
 * dataBody一定是一个字符串
 * mimeType和codecType用分号分隔
 * dataBody用逗号分隔
 * 例如：
 * data:,文本数据
 * data:text/plain,文本数据
 * data:image/png;base64,base64编码的png图片数据
 */
public class DataProtocolUtil {

    public static String imageFileToUri(File file) throws IOException {
        byte[] bytes = StreamUtil.readBytes(file);
        String body = Base64UrlStringByteCodec.INSTANCE.encode(bytes);
        return toUri("image/png", "base64", body);
    }

    public static String toUri(DataProtocolMeta meta) {
        return toUri(meta.getMimeType(), meta.getCodecType(), meta.getDataBody());
    }

    public static String toUri(String mimeType, String codecType, String dataBody) {
        if (mimeType == null) {
            mimeType = "";
        }
        if (codecType == null) {
            codecType = "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("data:");
        builder.append(mimeType);
        if (!codecType.isEmpty()) {
            builder.append(";");
        }
        builder.append(codecType);
        builder.append(",");
        builder.append(dataBody);
        return builder.toString();
    }

    public static DataProtocolMeta ofUri(String uri) throws MalformedURLException {
        if (!uri.startsWith("data:")) {
            throw new MalformedURLException("only support data: protocol uri");
        }
        DataProtocolMeta ret = new DataProtocolMeta();
        String[] arr = uri.split(",", 2);
        ret.setDataBody(arr.length > 1 ? arr[1] : null);
        arr = arr[0].split(":", 2);
        if (arr.length > 1) {
            arr = arr[1].split(";", 2);
            ret.setMimeType(arr[0].isEmpty() ? null : arr[0]);
            if (arr.length > 1) {
                ret.setCodecType(arr[1].isEmpty() ? null : arr[1]);
            }
        }
        return ret;
    }
}
