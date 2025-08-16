package i2f.sm.crypto.swl.supplier;

import i2f.sm.crypto.swl.SwlSmCryptoSm2AsymmetricEncryptor;
import i2f.swl.std.ISwlAsymmetricEncryptor;
import i2f.swl.std.supplier.ISwlAsymmetricEncryptorSupplier;

/**
 * @author Ice2Faith
 * @date 2024/7/10 9:45
 * @desc
 */
public class SwlSmCryptoSm2AsymmetricEncryptorSupplier implements ISwlAsymmetricEncryptorSupplier {

    @Override
    public ISwlAsymmetricEncryptor get() {
        return new SwlSmCryptoSm2AsymmetricEncryptor();
    }
}
