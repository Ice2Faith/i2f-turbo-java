package i2f.sm.crypto.swl.supplier;

import i2f.sm.crypto.swl.SwlSmCryptoSm4SymmetricEncryptor;
import i2f.swl.std.ISwlSymmetricEncryptor;
import i2f.swl.std.supplier.ISwlSymmetricEncryptorSupplier;

/**
 * @author Ice2Faith
 * @date 2024/7/10 9:58
 * @desc
 */
public class SwlSmCryptoSm4SymmetricEncryptorSupplier implements ISwlSymmetricEncryptorSupplier {

    @Override
    public ISwlSymmetricEncryptor get() {
        return new SwlSmCryptoSm4SymmetricEncryptor();
    }
}
