package i2f.swl.impl.supplier;

import i2f.swl.impl.SwlAesSymmetricEncryptor;
import i2f.swl.std.ISwlSymmetricEncryptor;
import i2f.swl.std.supplier.ISwlSymmetricEncryptorSupplier;

/**
 * @author Ice2Faith
 * @date 2024/7/10 19:28
 * @desc
 */
public class SwlAesSymmetricEncryptorSupplier implements ISwlSymmetricEncryptorSupplier {
    @Override
    public ISwlSymmetricEncryptor get() {
        return new SwlAesSymmetricEncryptor();
    }
}
