import SwlAntherdSm4SymmetricEncryptor from "../SwlAntherdSm4SymmetricEncryptor";

/**
 * @return {SwlAntherdSm4SymmetricEncryptorSupplier}
 * @implements ISwlSymmetricEncryptorSupplier
 */
function SwlAntherdSm4SymmetricEncryptorSupplier() {

}

/**
 * @return {ISwlSymmetricEncryptor}
 */
SwlAntherdSm4SymmetricEncryptorSupplier.prototype.get = function () {
    return new SwlAntherdSm4SymmetricEncryptor()
}

export default SwlAntherdSm4SymmetricEncryptorSupplier
