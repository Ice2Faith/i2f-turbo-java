import SwlAesSymmetricEncryptor from "../SwlAesSymmetricEncryptor";

/**
 * @return {SwlAesSymmetricEncryptorSupplier}
 * @implements ISwlSymmetricEncryptorSupplier
 */
function SwlAesSymmetricEncryptorSupplier() {

}

/**
 * @return {ISwlSymmetricEncryptor}
 */
SwlAesSymmetricEncryptorSupplier.prototype.get = function () {
    return new SwlAesSymmetricEncryptor()
}

export default SwlAesSymmetricEncryptorSupplier
