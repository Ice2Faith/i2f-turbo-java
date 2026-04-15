package i2f.extension.swl.impl.sm.antherd.supplier;

import i2f.extension.swl.impl.sm.antherd.SwlAntherdSm3MessageDigester;
import i2f.swl.std.ISwlMessageDigester;
import i2f.swl.std.supplier.ISwlMessageDigesterSupplier;

/**
 * @author Ice2Faith
 * @date 2026/4/8 15:34
 * @desc
 */
public class SwlAntherdSm3MessageDigesterSupplier implements ISwlMessageDigesterSupplier {
    @Override
    public ISwlMessageDigester get() {
        return new SwlAntherdSm3MessageDigester();
    }
}
