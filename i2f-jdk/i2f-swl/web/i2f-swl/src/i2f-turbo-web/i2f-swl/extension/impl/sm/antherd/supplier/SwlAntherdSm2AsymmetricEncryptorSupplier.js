import SwlAntherdSm2AsymmetricEncryptor from "../SwlAntherdSm2AsymmetricEncryptor";

/**
 * @return {SwlAntherdSm2AsymmetricEncryptorSupplier}
 * @implements ISwlAsymmetricEncryptorSupplier
 */
function SwlAntherdSm2AsymmetricEncryptorSupplier() {

}

/**
 * @return {ISwlAsymmetricEncryptor}
 */
SwlAntherdSm2AsymmetricEncryptorSupplier.prototype.get = function () {
    return new SwlAntherdSm2AsymmetricEncryptor()
}

export default SwlAntherdSm2AsymmetricEncryptorSupplier
