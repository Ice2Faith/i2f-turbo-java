import IExpireCache from '../IExpireCache';
import SystemClock from '../../clock/SystemClock';
import ExpireData from './ExpireData';

/**
 * @retunrn {SessionStorageExpireCache}
 * @implements {IExpireCache}
 * @constructor
 */
function SessionStorageExpireCache() {}

// 继承
SessionStorageExpireCache.prototype = Object.create(IExpireCache.prototype);
SessionStorageExpireCache.prototype.constructor = SessionStorageExpireCache;

/**
 *
 * @param data {ExpireData}
 * @return {String}
 */
SessionStorageExpireCache.prototype.serialize = function (data) {
  if (!data) {
    return JSON.stringify(null);
  }
  let dataJson = JSON.stringify(data.data);
  data.data = dataJson;
  let json = JSON.stringify(data);
  return json;
};

/**
 *
 * @param data {String}
 * @return {ExpireData}
 */
SessionStorageExpireCache.prototype.deserialize = function (data) {
  if (!data) {
    return null;
  }
  /**
   * @type {ExpireData}
   */
  let jsonData = JSON.parse(data);
  jsonData.data = JSON.parse(jsonData.data);
  return jsonData;
};

/**
 *
 * @param key {String}
 * @return {ExpireData}
 */
SessionStorageExpireCache.prototype.getData = function (key) {
  let json = sessionStorage.getItem(key);
  if (!json) {
    return null;
  }
  let data = this.deserialize(json);
  if (!data) {
    sessionStorage.removeItem(key);
    return null;
  }
  if (data.expireTs >= 0) {
    if (SystemClock.currentTimeSeconds() > data.expireTs) {
      sessionStorage.removeItem(key);
      return null;
    }
  }
  return data;
};

/**
 *
 * @param key {String}
 * @return {Object}
 */
SessionStorageExpireCache.prototype.get = function (key) {
  this.clearExpireKeys();
  let data = this.getData(key);
  if (!data) {
    return null;
  }
  return data.data;
};
/**
 *
 * @param key {String}
 * @param value {Object}
 * @return {void}
 */
SessionStorageExpireCache.prototype.set = function (key, value) {
  this.clearExpireKeys();
  let str = this.serialize(new ExpireData(value));
  sessionStorage.setItem(key, str);
};

SessionStorageExpireCache.prototype.clearExpireKeys = function () {
  if (Math.random() < 0.88) {
    return;
  }
  setTimeout(() => {
    let len = sessionStorage.length;
    for (let i = 0; i < len; i++) {
      let key = sessionStorage.key(i);
      if (key) {
        try {
          this.getData(key);
        }catch (e) {
        }
      }
    }
  }, 30);
};

/**
 *
 * @param key {String}
 * @return {boolean}
 */
SessionStorageExpireCache.prototype.exists = function (key) {
  return !!this.getData(key);
};

/**
 * @param key {String}
 * @return {void}
 */
SessionStorageExpireCache.prototype.remove = function (key) {
  sessionStorage.removeItem(key);
};

/**
 *
 * @param key {String}
 * @param value {Object}
 * @param timeSeconds {int}
 * @return {void}
 */
SessionStorageExpireCache.prototype.setWith = function (key, value, timeSeconds) {
  this.clearExpireKeys();
  let str = this.serialize(
    new ExpireData(value, SystemClock.currentTimeSeconds() + timeSeconds)
  );
  sessionStorage.setItem(key, str);
};

/**
 *
 * @param key {String}
 * @param timeSeconds {int}
 * @return {void}
 */
SessionStorageExpireCache.prototype.expire = function (key, timeSeconds) {
  this.clearExpireKeys();
  let data = this.getData(key);
  if (data) {
    data.expireTs = SystemClock.currentTimeSeconds() + timeSeconds;
    let str = this.serialize(data);
    sessionStorage.setItem(key, str);
  }
};

/**
 *
 * @param key {String}
 * @return {int|null} timeSeconds
 */
SessionStorageExpireCache.prototype.getExpire = function (key) {
  let data = this.getData(key);
  if (!data) {
    return null;
  }
  if (data.expireTs < 0) {
    return -1;
  }
  return data.expireTs - SystemClock.currentTimeSeconds();
};

export default SessionStorageExpireCache;
