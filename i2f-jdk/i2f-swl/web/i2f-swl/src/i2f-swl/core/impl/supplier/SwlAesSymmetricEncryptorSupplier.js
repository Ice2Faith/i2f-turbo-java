import SwlAesSymmetricEncryptor from "../SwlAesSymmetricEncryptor";

/**
 * @return {SwlAesSymmetricEncryptorSupplier}
 * @implements ISwlSymmetricEncryptorSupplier
 */
function SwlAesSymmetricEncryptorSupplier() {
    return {
        /**
         * @return {ISwlSymmetricEncryptor}
         */
        get() {
            return SwlAesSymmetricEncryptor()
        }
    }
}

export default SwlAesSymmetricEncryptorSupplier
