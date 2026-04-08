import SwlSha256MessageDigester from "../SwlSha256MessageDigester";

/**
 * @return {SwlSha256MessageDigesterSupplier}
 * @implements ISwlMessageDigesterSupplier
 */
function SwlSha256MessageDigesterSupplier() {

}

/**
 * @return {ISwlMessageDigester}
 */
SwlSha256MessageDigesterSupplier.prototype.get = function () {
    return new SwlSha256MessageDigester()
}

export default SwlSha256MessageDigesterSupplier
