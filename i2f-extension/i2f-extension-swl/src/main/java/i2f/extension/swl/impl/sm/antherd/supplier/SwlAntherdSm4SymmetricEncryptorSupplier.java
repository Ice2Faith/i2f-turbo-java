package i2f.extension.swl.impl.sm.antherd.supplier;

import i2f.extension.swl.impl.sm.antherd.SwlAntherdSm4SymmetricEncryptor;
import i2f.swl.std.ISwlSymmetricEncryptor;
import i2f.swl.std.supplier.ISwlSymmetricEncryptorSupplier;

/**
 * @author Ice2Faith
 * @date 2024/7/10 9:58
 * @desc
 */
public class SwlAntherdSm4SymmetricEncryptorSupplier implements ISwlSymmetricEncryptorSupplier {

    @Override
    public ISwlSymmetricEncryptor get() {
        return new SwlAntherdSm4SymmetricEncryptor();
    }
}
