import ICache from "./ICache";

/**
 * @retunrn {IExpireCache}
 * @extends {ICache}
 * @interface
 */
function IExpireCache() {

}

// 继承
IExpireCache.prototype = Object.create(ICache.prototype)
IExpireCache.prototype.constructor = IExpireCache

/**
 *
 * @param key {String}
 * @param value {Object}
 * @param timeSeconds {int}
 * @return {void}
 */
IExpireCache.prototype.setWith = function (key, value, timeSeconds) {

}

/**
 *
 * @param key {String}
 * @param timeSeconds {int}
 * @return {void}
 */
IExpireCache.prototype.expire = function (key, timeSeconds) {

}

/**
 *
 * @param key {String}
 * @return {int|null} timeSeconds
 */
IExpireCache.prototype.getExpire = function (key) {

}

export default IExpireCache
