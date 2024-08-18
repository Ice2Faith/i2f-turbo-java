import SwlCertUtil from "../SwlCertUtil";
import SwlCertManager from "../SwlCertManager";

/**
 * @return {SwlStorageCertManager}
 * @extends {SwlCertManager}
 */
function SwlStorageCertManager(cachePrefix=SwlStorageCertManager.DEFAULT_KEY_PREFIX(),
                               storage=localStorage) {
    /**
     * 继承父类属性
     */
    SwlCertManager.call(this)
    /**
     *
     * @type {string}
     */
    this.cachePrefix = cachePrefix;
    /**
     *
     * @type {localStorage|sessionStorage}
     */
    this.storage=storage
    /**
     *
     * @type {Map|Object}
     */
    this.cacheCert=new Map()
}

// 继承
SwlStorageCertManager.prototype = Object.create(SwlCertManager.prototype)
SwlStorageCertManager.prototype.constructor = SwlStorageCertManager

/**
 * @return {string}
 */
SwlStorageCertManager.DEFAULT_KEY_PREFIX=function(){
    return "swl:cert:"
}

/**
 * @return {string}
 */
SwlStorageCertManager.SERVER_SUFFIX=function(){
    return ".server"
}

/**
 * @return {string}
 */
SwlStorageCertManager.CLIENT_SUFFIX=function(){
    return ".client"
}

/**
 * @param certId {String}
 * @param server {boolean}
 * @return {String}
 */
SwlStorageCertManager.prototype.getCertName = function (certId,server) {
    if (server) {
        return certId + SwlStorageCertManager.SERVER_SUFFIX();
    }
    return certId + SwlStorageCertManager.CLIENT_SUFFIX();
}

/**
 * @param key {String}
 * @return {String}
 */
SwlStorageCertManager.prototype.cacheKey = function (key) {
    if (!this.cachePrefix || this.cachePrefix=='') {
        return key;
    }
    return this.cachePrefix + ":" + key;
}

/**
 * @param certId {String}
 * @param server {boolean}
 * @return {SwlCert}
 */
SwlStorageCertManager.prototype.load = function (certId,server) {
    let certName = this.getCertName(certId, server);
    let cert = this.cacheCert[certName];
    if (cert) {
        return cert.copy();
    }
    let str = this.storage.getItem(this.cacheKey(certName));
    let ret = SwlCertUtil.deserialize(str);
    if (ret != null) {
        this.cacheCert[certName]=ret.copy();
    }
    return ret;
}
/**
 * @param cert {SwlCert}
 * @param server {boolean}
 * @return {void}
 */
SwlStorageCertManager.prototype.store = function (cert,server) {
    if (cert == null) {
        return;
    }
    let certId = cert.certId;
    let certName = this.getCertName(certId, server);
    this.cacheCert[certName]=cert.copy();
    let str = SwlCertUtil.serialize(cert);
    this.storage.setItem(this.cacheKey(certName), str);
}

export default SwlStorageCertManager
