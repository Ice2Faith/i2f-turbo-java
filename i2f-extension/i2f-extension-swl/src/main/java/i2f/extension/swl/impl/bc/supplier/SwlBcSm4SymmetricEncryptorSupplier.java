package i2f.extension.swl.impl.bc.supplier;

import i2f.extension.swl.impl.bc.SwlBcSm4SymmetricEncryptor;
import i2f.swl.std.ISwlSymmetricEncryptor;
import i2f.swl.std.supplier.ISwlSymmetricEncryptorSupplier;

/**
 * @author Ice2Faith
 * @date 2024/7/10 19:28
 * @desc
 */
public class SwlBcSm4SymmetricEncryptorSupplier implements ISwlSymmetricEncryptorSupplier {

    @Override
    public ISwlSymmetricEncryptor get() {
        return new SwlBcSm4SymmetricEncryptor();
    }
}
