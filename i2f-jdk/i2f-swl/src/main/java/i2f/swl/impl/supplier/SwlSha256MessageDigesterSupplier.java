package i2f.swl.impl.supplier;

import i2f.swl.impl.SwlSha256MessageDigester;
import i2f.swl.std.ISwlMessageDigester;
import i2f.swl.std.supplier.ISwlMessageDigesterSupplier;

/**
 * @author Ice2Faith
 * @date 2026/4/8 15:13
 * @desc
 */
public class SwlSha256MessageDigesterSupplier implements ISwlMessageDigesterSupplier {
    @Override
    public ISwlMessageDigester get() {
        return new SwlSha256MessageDigester();
    }
}
