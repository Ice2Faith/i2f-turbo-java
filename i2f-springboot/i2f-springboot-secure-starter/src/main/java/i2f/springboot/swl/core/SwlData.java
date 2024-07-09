package i2f.springboot.swl.core;

import i2f.springboot.swl.data.SecureHeader;
import lombok.Data;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/9 20:23
 * @desc
 */
@Data
public class SwlData {
    private SecureHeader header;
    private List<String> parts;
}
