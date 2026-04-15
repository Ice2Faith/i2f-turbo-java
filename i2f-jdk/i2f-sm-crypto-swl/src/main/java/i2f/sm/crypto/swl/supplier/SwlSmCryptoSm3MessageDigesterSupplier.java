package i2f.sm.crypto.swl.supplier;

import i2f.sm.crypto.swl.SwlSmCryptoSm3MessageDigester;
import i2f.swl.std.ISwlMessageDigester;
import i2f.swl.std.supplier.ISwlMessageDigesterSupplier;

/**
 * @author Ice2Faith
 * @date 2026/4/8 15:33
 * @desc
 */
public class SwlSmCryptoSm3MessageDigesterSupplier implements ISwlMessageDigesterSupplier {
    @Override
    public ISwlMessageDigester get() {
        return new SwlSmCryptoSm3MessageDigester();
    }
}
