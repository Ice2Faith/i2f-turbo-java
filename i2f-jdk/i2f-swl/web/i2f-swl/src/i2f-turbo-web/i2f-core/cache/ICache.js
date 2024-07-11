/**
 * @retunrn {ICache}
 * @interface
 */
function ICache() {

}

/**
 *
 * @param key {String}
 * @return {Object}
 */
ICache.prototype.get = function (key) {

}
/**
 *
 * @param key {String}
 * @param value {Object}
 * @return {void}
 */
ICache.prototype.set = function (key, value) {

}

/**
 *
 * @param key {String}
 * @return {boolean}
 */
ICache.prototype.exists = function (key) {

}

/**
 * @param key {String}
 * @return {void}
 */
ICache.prototype.remove = function (key) {

}

export default ICache
