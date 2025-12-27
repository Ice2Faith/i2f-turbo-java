package i2f.rowset.std.impl;

import i2f.rowset.std.IRowHeader;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/12/2 22:13
 * @desc
 */
@Data
@NoArgsConstructor
public class SimpleRowHeader implements IRowHeader {
    protected String name;

    public SimpleRowHeader(String name) {
        this.name = name;
    }
}
