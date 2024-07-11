import IExpireCache from "../IExpireCache";
import SystemClock from "../../clock/SystemClock";
import ExpireData from "./ExpireData";

/**
 * @retunrn {LocalStorageExpireCache}
 * @implements {IExpireCache}
 * @constructor
 */
function LocalStorageExpireCache() {

}

// 继承
LocalStorageExpireCache.prototype = Object.create(IExpireCache.prototype)
LocalStorageExpireCache.prototype.constructor = LocalStorageExpireCache

/**
 *
 * @param data {ExpireData}
 * @return {String}
 */
LocalStorageExpireCache.prototype.serialize = function (data) {
    if (!data) {
        return JSON.stringify(null)
    }
    let dataJson = JSON.stringify(data.data)
    data.data = dataJson
    let json = JSON.stringify(data)
    return json
}

/**
 *
 * @param data {String}
 * @return {ExpireData}
 */
LocalStorageExpireCache.prototype.deserialize = function (data) {
    if (!data) {
        return null
    }
    /**
     * @type {ExpireData}
     */
    let jsonData = JSON.parse(data);
    jsonData.data = JSON.parse(jsonData.data)
    return jsonData
}

/**
 *
 * @param key {String}
 * @return {ExpireData}
 */
LocalStorageExpireCache.prototype.getData = function (key) {
    let json = localStorage.getItem(key)
    if (!json) {
        return null
    }
    let data = this.deserialize(json)
    if (data == null) {
        localStorage.removeItem(key)
        return null
    }
    if (data.expireTs >= 0) {
        if (SystemClock.currentTimeSeconds() > data.expireTs) {
            localStorage.removeItem(key)
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
LocalStorageExpireCache.prototype.get = function (key) {
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
LocalStorageExpireCache.prototype.set = function (key, value) {
    let str = this.serialize(new ExpireData(value))
    localStorage.setItem(key, str)
}

/**
 *
 * @param key {String}
 * @return {boolean}
 */
LocalStorageExpireCache.prototype.exists = function (key) {
    return !!this.getData(key)
}

/**
 * @param key {String}
 * @return {void}
 */
LocalStorageExpireCache.prototype.remove = function (key) {
    localStorage.removeItem(key)
}


/**
 *
 * @param key {String}
 * @param value {Object}
 * @param timeSeconds {int}
 * @return {void}
 */
LocalStorageExpireCache.prototype.setWith = function (key, value, timeSeconds) {
    let str = this.serialize(new ExpireData(value, SystemClock.currentTimeSeconds() + timeSeconds))
    localStorage.setItem(key, str)
}

/**
 *
 * @param key {String}
 * @param timeSeconds {int}
 * @return {void}
 */
LocalStorageExpireCache.prototype.expire = function (key, timeSeconds) {
    let data = this.getData(key)
    if (data) {
        data.expireTs = SystemClock.currentTimeSeconds() + timeSeconds
        let str = this.serialize(data)
        localStorage.setItem(key, str)
    }
}

/**
 *
 * @param key {String}
 * @return {int|null} timeSeconds
 */
LocalStorageExpireCache.prototype.getExpire = function (key) {
    let data = this.getData(key)
    if (!data) {
        return null
    }
    if (data.expireTs < 0) {
        return -1
    }
    return data.expireTs - SystemClock.currentTimeSeconds()
}

export default LocalStorageExpireCache
