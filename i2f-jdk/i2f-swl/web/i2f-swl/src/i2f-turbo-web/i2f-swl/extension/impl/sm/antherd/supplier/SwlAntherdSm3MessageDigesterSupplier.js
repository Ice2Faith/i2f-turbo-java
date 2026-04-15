import SwlAntherdSm3MessageDigester from "../SwlAntherdSm3MessageDigester";

/**
 * @return {SwlAntherdSm3MessageDigesterSupplier}
 * @implements ISwlMessageDigesterSupplier
 */
function SwlAntherdSm3MessageDigesterSupplier() {

}

/**
 * @return {ISwlMessageDigester}
 */
SwlAntherdSm3MessageDigesterSupplier.prototype.get = function () {
    return new SwlAntherdSm3MessageDigester()
}

export default SwlAntherdSm3MessageDigesterSupplier
