package i2f.springboot.swl.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/7/10 15:33
 * @desc
 */
@Data
@NoArgsConstructor
public class SwlCtrl {
    protected boolean in = false;
    protected boolean out = false;

    public SwlCtrl(boolean in, boolean out) {
        this.in = in;
        this.out = out;
    }
}
