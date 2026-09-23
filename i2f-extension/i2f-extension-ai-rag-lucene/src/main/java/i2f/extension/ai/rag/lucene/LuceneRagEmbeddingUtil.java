package i2f.extension.ai.rag.lucene;

import java.nio.charset.StandardCharsets;

/**
 * @author Ice2Faith
 * @date 2026/9/23 10:09
 * @desc 主要提供从UTF8字符编码角度，进行字符串字节与向量的强行转换
 * 字符串->UTF8编码字节->除以1000得到纯浮点数
 */
public class LuceneRagEmbeddingUtil {

    public static double[] string2vector(String content) {
        if (content == null) {
            return null;
        }
        if (content.isEmpty()) {
            return new double[0];
        }
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        int length = bytes.length;
        double[] arr = new double[length];
        for (int i = 0; i < length; i++) {
            byte bt = bytes[i];
            arr[i] = (bt & 0x0ff) / 1000.0;
        }
        return arr;
    }

    public static String vector2string(double[] vec) {
        if (vec == null) {
            return null;
        }
        if (vec.length == 0) {
            return "";
        }
        byte[] arr = new byte[vec.length];
        for (int i = 0; i < vec.length; i++) {
            int v = (int) (vec[i] * 1000.0);
            arr[i] = (byte) (v & 0x0ff);
        }
        return new String(arr, StandardCharsets.UTF_8);
    }
}
