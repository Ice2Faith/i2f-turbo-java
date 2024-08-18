import IExpireCache from "../IExpireCache";
import SystemClock from "../../clock/SystemClock";
import ExpireData from "./ExpireData";

/**
 * @retunrn {MemMapExpireCache}
 * @implements {IExpireCache}
 * @constructor
 */
function MemMapExpireCache() {
    this._map = new Map()
}

// 继承
MemMapExpireCache.prototype = Object.create(IExpireCache.prototype)
MemMapExpireCache.prototype.constructor = MemMapExpireCache

/**
 *
 * @param key {String}
 * @return {ExpireData}
 */
MemMapExpireCache.prototype.getData = function (key) {
    let data = this._map.get(key)
    if (!data) {
        return null
    }
    if (data.expireTs >= 0) {
        if (SystemClock.currentTimeSeconds() > data.expireTs) {
            this._map.delete(key)
            return null
        }
    }
    return data
}

/**
 *
 * @param key {String}
 * @return {Object}
 */
MemMapExpireCache.prototype.get = function (key) {
    let data = this.getData(key);
    if (!data) {
        return null
    }
    return data.data
}
/**
 *
 * @param key {String}
 * @param value {Object}
 * @return {void}
 */
MemMapExpireCache.prototype.set = function (key, value) {
    let str = new ExpireData(value)
    this._map.set(key, str)
}

/**
 *
 * @param key {String}
 * @return {boolean}
 */
MemMapExpireCache.prototype.exists = function (key) {
    return !!this.getData(key)
}

/**
 * @param key {String}
 * @return {void}
 */
MemMapExpireCache.prototype.remove = function (key) {
    this._map.delete(key)
}


/**
 *
 * @param key {String}
 * @param value {Object}
 * @param timeSeconds {int}
 * @return {void}
 */
MemMapExpireCache.prototype.setWith = function (key, value, timeSeconds) {
    let str = new ExpireData(value, SystemClock.currentTimeSeconds() + timeSeconds)
    this._map.set(key, str)
}

/**
 *
 * @param key {String}
 * @param timeSeconds {int}
 * @return {void}
 */
MemMapExpireCache.prototype.expire = function (key, timeSeconds) {
    let data = this.getData(key)
    if (data) {
        data.expireTs = SystemClock.currentTimeSeconds() + timeSeconds
        let str = data
        this._map.set(key, str)
    }
}

/**
 *
 * @param key {String}
 * @return {int|null} timeSeconds
 */
MemMapExpireCache.prototype.getExpire = function (key) {
    let data = this.getData(key)
    if (!data) {
        return null
    }
    if (data.expireTs < 0) {
        return -1
    }
    return data.expireTs - SystemClock.currentTimeSeconds()
}

export default MemMapExpireCache
