package i2f.codec.data;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/6/18 14:52
 * @desc
 */
@Data
public class DataProtocolMeta {
    private String mimeType;
    private String codecType;
    private String dataBody;
}
