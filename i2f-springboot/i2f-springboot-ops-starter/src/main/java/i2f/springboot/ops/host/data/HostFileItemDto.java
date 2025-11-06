package i2f.springboot.ops.host.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/4 23:31
 * @desc
 */
@Data
@NoArgsConstructor
public class HostFileItemDto {
    protected String path;
    protected String name;
    protected String type; // file,dir
    protected long size;
    protected String sizeText;
}
