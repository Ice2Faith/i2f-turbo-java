import SwlRsaAsymmetricEncryptor from "../SwlRsaAsymmetricEncryptor";

/**
 * @return {SwlRsaAsymmetricEncryptorSupplier}
 * @implements ISwlAsymmetricEncryptorSupplier
 */
function SwlRsaAsymmetricEncryptorSupplier() {
    return {
        /**
         * @return {ISwlAsymmetricEncryptor}
         */
        get() {
            return SwlRsaAsymmetricEncryptor()
        }
    }
}

export default SwlRsaAsymmetricEncryptorSupplier
