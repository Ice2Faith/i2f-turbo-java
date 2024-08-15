import SwlExchanger from "../exchanger/SwlExchanger";
import SwlStorageKeyManager from "@/i2f-turbo-web/i2f-swl/core/core/key/impl/SwlStorageKeyManager";

/**
 * @param keyManager {SwlKeyManager}
 * @return {SwlKeyExchanger}
 * @extends {SwlExchanger}
 * @constructor {SwlKeyExchanger}
 */
function SwlKeyExchanger(keyManager=new SwlStorageKeyManager()) {
    /**
     *
     * @type {SwlKeyManager}
     */
    this.keyManager=keyManager
}


// 继承
SwlKeyExchanger.prototype = Object.create(SwlExchanger.prototype)
SwlKeyExchanger.prototype.constructor = SwlKeyExchanger


/**
 * @return {AsymKeyPair}
 */
SwlKeyExchanger.prototype.getSelfKeyPair = function () {
    this.keyManager.getSelfKeyPair()
}


/**
 *
 * @return {String}
 */
SwlKeyExchanger.prototype.getSelfPublicKey=function() {
    let keyPair = this.getSelfKeyPair();
    return keyPair.getPublicKey();
}

/**
 *
 * @param publicKey {String}
 * @return {String}
 */
SwlKeyExchanger.prototype.calcKeySign=function(publicKey) {
    return this.messageDigester.digest(publicKey);
}

/**
 *
 * @param selfAsymSign {String}
 * @param selfKeyPair {AsymKeyPair}
 * @return {void}
 */
SwlKeyExchanger.prototype.setSelfKeyPair=function(selfAsymSign, selfKeyPair) {
    this.keyManager.setSelfKeyPair(selfAsymSign, selfKeyPair);
    this.keyManager.setDefaultSelfAsymSign(selfAsymSign);
}

/**
 *
 * @param selfAsymSign {String}
 * @return {String|null}
 */
SwlKeyExchanger.prototype.getSelfPrivateKey=function( selfAsymSign) {
    let keyPair = this.keyManager.getSelfKeyPair(selfAsymSign);
    if (!keyPair) {
        return null;
    }
    return keyPair.getPrivateKey();
}

/**
 *
 * @param otherAsymSign {String}
 * @return {String|null}
 */
SwlKeyExchanger.prototype.getOtherPublicKey=function(otherAsymSign) {
    return this.keyManager.getOtherPublicKey(otherAsymSign);
}

/**
 *
 * @return {String|null}
 */
SwlKeyExchanger.prototype.getOtherPublicKeyDefault=function() {
    return this.keyManager.getOtherPublicKey();
}

/**
 *
 * @param otherAsymSign {String}
 * @param publicKey {String}
 * @return {void}
 */
SwlKeyExchanger.prototype.setOtherPublicKey=function(otherAsymSign, publicKey) {
    this.keyManager.setOtherPublicKey(otherAsymSign, publicKey);
    this.keyManager.setDefaultOtherAsymSign(otherAsymSign);
}

/**
 *
 * @param otherPublicKey {String}
 * @return {void}
 */
SwlKeyExchanger.prototype.acceptOtherPublicKey=function(otherPublicKey) {
    let otherAsymSign = this.messageDigester.digest(otherPublicKey);
    this.setOtherPublicKey(otherAsymSign, otherPublicKey);
}

/**
 *
 * @return {String|null}
 */
SwlKeyExchanger.prototype.getSelfSwapKey=function() {
    let selfPublicKey = this.getSelfPublicKey();
    return this.obfuscateEncode(selfPublicKey);
}

/**
 *
 * @param otherSwapKey {String}
 * @return {void}
 */
SwlKeyExchanger.prototype.acceptOtherSwapKey=function(otherSwapKey) {
    let otherPublicKey = this.obfuscateDecode(otherSwapKey);
    this.acceptOtherPublicKey(otherPublicKey);
}

/**
 *
 * @return {AsymKeyPair}
 */
SwlKeyExchanger.prototype.resetSelfKeyPair=function() {
    let asymmetricEncryptor = this.asymmetricEncryptorSupplier.get();
    let asymKeyPair = asymmetricEncryptor.generateKeyPair();
    let selfAsymSign = this.messageDigester.digest(asymKeyPair.getPublicKey());
    this.setSelfKeyPair(selfAsymSign, asymKeyPair);
    return asymKeyPair;
}

/**
 *
 * @param remotePublicKey {String}
 * @param parts {string[]}
 * @param attaches {string[]|null}
 * @return {SwlData}
 */
SwlKeyExchanger.prototype.sendByKey=function(remotePublicKey,  parts,  attaches=[]) {
    let keyPair = this.getSelfKeyPair();
    return this.sendByRaw(remotePublicKey, this.calcKeySign(remotePublicKey),
        keyPair.getPrivateKey(), this.calcKeySign(keyPair.getPublicKey()),
        parts, attaches);
}

/**
 *
 * @param clientId {String}
 * @param request {SwlData}
 * @return {SwlData}
 */
SwlKeyExchanger.prototype.receiveByKey=function(clientId, request) {
    let selfAsymSign = request.header.remoteAsymSign;
    let otherAsymSign = request.header.localAsymSign;
    let otherPublicKey = this.getOtherPublicKey(otherAsymSign);
    let selfPrivateKey = this.getSelfPrivateKey(selfAsymSign);
    return this.receiveByRaw(clientId, request, otherPublicKey, selfPrivateKey);
}
/**
 *
 * @param parts {string[]}
 * @param attaches {string[]|null}
 * @return {SwlData}
 */
 SwlKeyExchanger.prototype.sendDefaultByKey=function(parts, attaches=[]) {
    let otherPublicKey = this.getOtherPublicKeyDefault();
    return this.sendByKey(otherPublicKey, parts, attaches);
}

/**
 *
 * @param remoteAsymSign {String}
 * @param parts {string[]}
 * @param attaches {string[]|null}
 * @return {SwlData}
 */
 SwlKeyExchanger.prototype.responseByKey=function( remoteAsymSign,  parts, attaches=[]) {
    let otherPublicKey = this.getOtherPublicKey(remoteAsymSign);
    return this.sendByKey(otherPublicKey, parts, attaches);
}


export default SwlKeyExchanger
