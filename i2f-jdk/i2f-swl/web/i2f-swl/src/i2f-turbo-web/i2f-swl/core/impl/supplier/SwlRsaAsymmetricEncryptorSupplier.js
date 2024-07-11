import SwlRsaAsymmetricEncryptor from "../SwlRsaAsymmetricEncryptor";

/**
 * @return {SwlRsaAsymmetricEncryptorSupplier}
 * @implements ISwlAsymmetricEncryptorSupplier
 */
function SwlRsaAsymmetricEncryptorSupplier() {

}

/**
 * @return {ISwlAsymmetricEncryptor}
 */
SwlRsaAsymmetricEncryptorSupplier.prototype.get = function () {
    return new SwlRsaAsymmetricEncryptor()
}

export default SwlRsaAsymmetricEncryptorSupplier
