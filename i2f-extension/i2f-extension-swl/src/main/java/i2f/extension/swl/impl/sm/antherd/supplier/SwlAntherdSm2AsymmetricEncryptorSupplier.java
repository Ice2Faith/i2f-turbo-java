package i2f.extension.swl.impl.sm.antherd.supplier;

import i2f.extension.swl.impl.sm.antherd.SwlAntherdSm2AsymmetricEncryptor;
import i2f.swl.std.ISwlAsymmetricEncryptor;
import i2f.swl.std.supplier.ISwlAsymmetricEncryptorSupplier;

/**
 * @author Ice2Faith
 * @date 2024/7/10 9:45
 * @desc
 */
public class SwlAntherdSm2AsymmetricEncryptorSupplier implements ISwlAsymmetricEncryptorSupplier {

    @Override
    public ISwlAsymmetricEncryptor get() {
        return new SwlAntherdSm2AsymmetricEncryptor();
    }
}
