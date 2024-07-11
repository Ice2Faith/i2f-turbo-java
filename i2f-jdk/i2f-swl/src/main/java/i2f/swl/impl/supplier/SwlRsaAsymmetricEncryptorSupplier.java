package i2f.swl.impl.supplier;

import i2f.swl.impl.SwlRsaAsymmetricEncryptor;
import i2f.swl.std.ISwlAsymmetricEncryptor;
import i2f.swl.std.supplier.ISwlAsymmetricEncryptorSupplier;

/**
 * @author Ice2Faith
 * @date 2024/7/10 18:59
 * @desc
 */
public class SwlRsaAsymmetricEncryptorSupplier implements ISwlAsymmetricEncryptorSupplier {
    @Override
    public ISwlAsymmetricEncryptor get() {
        return new SwlRsaAsymmetricEncryptor();
    }
}
