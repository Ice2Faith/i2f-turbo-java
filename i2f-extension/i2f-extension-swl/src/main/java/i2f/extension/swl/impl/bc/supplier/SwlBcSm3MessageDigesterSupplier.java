package i2f.extension.swl.impl.bc.supplier;

import i2f.extension.swl.impl.bc.SwlBcSm3MessageDigester;
import i2f.swl.std.ISwlMessageDigester;
import i2f.swl.std.supplier.ISwlMessageDigesterSupplier;

/**
 * @author Ice2Faith
 * @date 2026/4/8 15:31
 * @desc
 */
public class SwlBcSm3MessageDigesterSupplier implements ISwlMessageDigesterSupplier {
    @Override
    public ISwlMessageDigester get() {
        return new SwlBcSm3MessageDigester();
    }
}
