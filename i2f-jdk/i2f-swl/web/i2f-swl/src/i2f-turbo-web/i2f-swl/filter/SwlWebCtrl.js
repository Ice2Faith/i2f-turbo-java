/**
 *
 * @param enableIn {boolean}
 * @param enableOut {boolean}
 * @constructor
 * @return SwlWebCtrl
 */
function SwlWebCtrl(enableIn = false, enableOut = false) {
    /**
     * @type {boolean}
     */
    this.enableIn = enableIn
    /**
     * @type {boolean}
     */
    this.enableOut = enableOut
}

export default SwlWebCtrl