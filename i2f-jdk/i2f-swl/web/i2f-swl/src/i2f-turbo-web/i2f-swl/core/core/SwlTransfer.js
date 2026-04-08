import MemMapExpireCache from "../../../i2f-core/cache/impl/MemMapExpireCache";
import SwlTransferConfig from "./SwlTransferConfig";
import AsymKeyPair from "../../../i2f-core/crypto/asymmetric/AsymKeyPair";
import SwlExchanger from "./exchanger/SwlExchanger";
import SwlException from "../exception/SwlException";
import SwlCode from "../consts/SwlCode";
import SwlCertUtil from "../cert/SwlCertUtil";
import CodeUtil from "../../../i2f-core/util/CodeUtil";
import SwlCert from "../cert/data/SwlCert";

/**
 * @return SwlTransfer
 * @constructor {SwlTransfer}
 * @extends {SwlExchanger}
 */
function SwlTransfer() {
    SwlExchanger.call(this)
    /**
     *
     * @type {IExpireCache}
     */
    this.cache = new MemMapExpireCache();
    /**
     *
     * @type {SwlTransferConfig}
     */
    this.config = new SwlTransferConfig()


}

// 继承
SwlTransfer.prototype = Object.create(SwlExchanger.prototype)
SwlTransfer.prototype.constructor = SwlTransfer

SwlTransfer.CERT_PREFIX_KEY = function () {
    return "swl:key:cert:"
};

SwlTransfer.OTHER_KEY_PUBLIC_DEFAULT = function () {
    return "swl:key:other:default"
};

/**
 *
 * @param key {String}
 * @return {String}
 */
SwlTransfer.prototype.cacheKey = function (key) {
    let cacheKeyPrefix = this.config.cacheKeyPrefix;
    if (!cacheKeyPrefix || cacheKeyPrefix === "") {
        return key;
    }
    return cacheKeyPrefix + ":" + key;
}

/**
 *
 * @param certId {String}
 * @return {String}
 */
SwlTransfer.prototype.certKey=function(certId) {
    return SwlTransfer.CERT_PREFIX_KEY() + certId;
}

/**
 * @param certId {String}
 * @return {SwlCert}
 */
SwlTransfer.prototype.getCert=function( certId) {
    let obj = this.cache.get(this.cacheKey(this.certKey(certId)));
    if (!obj) {
        throw new SwlException(SwlCode.CERT_ID_MISSING_EXCEPTION(), "cert id missing or not built channel");
    }
    let cert = SwlCertUtil.deserialize(obj);
    cert.certId=certId;
    return cert;
}

/**
 *
 * @param certId {String}
 * @return {AsymKeyPair}
 */
SwlTransfer.prototype.getSelfKeyPair=function( certId) {
    let cert = this.getCert(certId);
    return new AsymKeyPair(cert.publicKey, cert.privateKey);
}

/**
 * @param certId {String}
 * @return {String}
 */
SwlTransfer.prototype.getSelfPublicKey = function (certId) {
    let keyPair = this.getSelfKeyPair(certId);
    return keyPair.getPublicKey();
}

/**
 * @param certId {String}
 * @return {String}
 */
SwlTransfer.prototype.getSelfPrivateKey = function (certId) {
    let keyPair = this.getSelfKeyPair(certId);
    return keyPair.getPrivateKey();
}

/**
 * @param certId {String}
 * @return {String}
 */
SwlTransfer.prototype.getOtherPublicKey = function (certId) {
    let cert = this.getCert(certId);
    return cert.remotePublicKey;
}

/**
 *
 * @param certId {String}
 * @param selfKeyPair {AsymKeyPair}
 * @param otherPublicKey {String}
 * @return {void}
 */
SwlTransfer.prototype.buildCert=function(certId, selfKeyPair, otherPublicKey) {
    let cert = new SwlCert(certId, selfKeyPair.getPublicKey(), selfKeyPair.getPrivateKey(), otherPublicKey);
    let text = SwlCertUtil.serialize(cert);
    let key = this.certKey(certId);
    this.cache.setWith(this.cacheKey(key), text, this.config.certExpireSeconds);
    this.cache.set(this.cacheKey(SwlTransfer.OTHER_KEY_PUBLIC_DEFAULT()),certId);
}

/**
 *
 * @param certId {String}
 * @returns {SwlCert|null}
 */
SwlTransfer.prototype.removeCert=function(certId) {
    let key = this.certKey(certId);
    let text = this.cache.get(this.cacheKey(key));
    if (text != null) {
        return SwlCertUtil.deserialize(text);
    }
    this.cache.remove(this.cacheKey(key));
    return null;
}

/**
 *
 * @param certId {String}
 * @return {void}
 */
SwlTransfer.prototype.resetCertExpire=function(certId) {
    let key = this.certKey(certId);
    this.cache.expire(this.cacheKey(key), this.config.certExpireSeconds);
}


/**
 *
 * @param otherPublicKey {String}
 * @return {String}
 */
SwlTransfer.prototype.acceptOtherPublicKey = function (otherPublicKey) {
    let certId=CodeUtil.makeCheckCode(32,false);
    return this.acceptOtherPublicKeyWithId(certId, otherPublicKey);
}

/**
 *
 * @param certId {String}
 * @param otherPublicKey {String}
 * @return {String}
 */
SwlTransfer.prototype.acceptOtherPublicKeyWithId=function(certId, otherPublicKey) {
    let asymmetricEncryptor = this.requireAsymmetricEncryptor();
    let selfKeyPair = asymmetricEncryptor.generateKeyPair();
    return this.acceptOtherPublicKeyRaw(certId, selfKeyPair, otherPublicKey);
}

/**
 *
 * @param certId {String}
 * @param selfKeyPair {AsymKeyPair}
 * @param otherPublicKey {String}
 * @return {String}
 */
SwlTransfer.prototype.acceptOtherPublicKeyRaw=function(certId, selfKeyPair, otherPublicKey) {
    this.buildCert(certId, selfKeyPair, otherPublicKey);
    return certId;
}

/**
 *
 * @return {AsymKeyPair}
 */
SwlTransfer.prototype.getSelfSwapKey = function () {
    let swapKeyPair = this.config.swapKeyPair;
    return swapKeyPair;
}

/**
 *
 * @param otherSwapKey {String}
 * @return {String}
 */
SwlTransfer.prototype.acceptOtherSwapKey = function (otherSwapKey) {
    let otherPublicKey = this.obfuscateDecode(otherSwapKey);
    this.acceptOtherPublicKey(otherPublicKey);
}

/**
 *
 * @param certId {String}
 * @param parts {string[]}
 * @param attaches {string[]|null}
 * @return {SwlData}
 */
SwlTransfer.prototype.send = function (certId, parts, attaches = null) {
    let cert = this.getCert(certId);
    return this.sendByRaw(certId,
        cert.remotePublicKey,
        cert.privateKey,
        parts, attaches);
}

/**
 *
 * @param clientId {String}
 * @param request {SwlData}
 * @return {SwlData}
 */
SwlTransfer.prototype.receive = function (clientId, request) {
    let certId = request.header.certId;
    let cert=this.getCert(certId);
    return this.receiveByRaw(clientId, request, cert.remotePublicKey, cert.privateKey);
}

/**
 *
 * @return {String}
 */
SwlTransfer.prototype.getOtherCertIdDefault=function(){
    return this.cache.get(this.cacheKey(SwlTransfer.OTHER_KEY_PUBLIC_DEFAULT()));
}

/**
 *
 * @param parts {string[]}
 * @param attaches {string[]|null}
 * @return {SwlData}
 */
SwlTransfer.prototype.sendDefault=function(parts, attaches) {
    let certId = this.getOtherCertIdDefault();
    return this.send(certId, parts, attaches);
}

/**
 *
 * @param certId {String}
 * @param parts {string[]}
 * @param attaches {string[]|null}
 * @return {SwlData}
 */
SwlTransfer.prototype.response = function (certId, parts, attaches = null) {
    return this.send(certId, parts, attaches);
}

export default SwlTransfer
