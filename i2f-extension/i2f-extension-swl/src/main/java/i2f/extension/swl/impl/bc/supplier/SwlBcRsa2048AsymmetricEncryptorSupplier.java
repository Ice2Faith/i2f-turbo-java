package i2f.extension.swl.impl.bc.supplier;

import i2f.extension.swl.impl.bc.SwlBcRsa2048AsymmetricEncryptor;
import i2f.swl.std.ISwlAsymmetricEncryptor;
import i2f.swl.std.supplier.ISwlAsymmetricEncryptorSupplier;

/**
 * @author Ice2Faith
 * @date 2024/7/10 18:59
 * @desc
 */
public class SwlBcRsa2048AsymmetricEncryptorSupplier implements ISwlAsymmetricEncryptorSupplier {

    @Override
    public ISwlAsymmetricEncryptor get() {
        return new SwlBcRsa2048AsymmetricEncryptor();
    }
}
