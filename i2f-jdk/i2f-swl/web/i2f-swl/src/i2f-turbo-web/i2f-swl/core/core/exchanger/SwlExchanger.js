import SwlRsaAsymmetricEncryptorSupplier from "../../impl/supplier/SwlRsaAsymmetricEncryptorSupplier";
import SwlAesSymmetricEncryptorSupplier from "../../impl/supplier/SwlAesSymmetricEncryptorSupplier";
import SwlSha256MessageDigester from "../../impl/SwlSha256MessageDigester";
import SwlBase64Obfuscator from "../../impl/SwlBase64Obfuscator";
import SwlEmptyNonceManager from "./impl/SwlEmptyNonceManager";
import SwlCertUtil from "../../cert/SwlCertUtil";
import SwlData from "@/i2f-turbo-web/i2f-swl/core/data/SwlData";
import SwlHeader from "@/i2f-turbo-web/i2f-swl/core/data/SwlHeader";
import SwlContext from "@/i2f-turbo-web/i2f-swl/core/data/SwlContext";

/**
 * @return {SwlExchanger}
 * @constructor
 */
function SwlExchanger() {
    /**
     *
     * @type {ISwlAsymmetricEncryptorSupplier}
     */
    this.asymmetricEncryptorSupplier = new SwlRsaAsymmetricEncryptorSupplier()
    /**
     *
     * @type {ISwlSymmetricEncryptorSupplier}
     */
    this.symmetricEncryptorSupplier = new SwlAesSymmetricEncryptorSupplier()
    /**
     *
     * @type {ISwlMessageDigester}
     */
    this.messageDigester = new SwlSha256MessageDigester()
    /**
     *
     * @type {ISwlObfuscator}
     */
    this.obfuscator = new SwlBase64Obfuscator()

    this.timestampExpireWindowSeconds = 30

    this.nonceManager = new SwlEmptyNonceManager();

    this.nonceTimeoutSeconds=30*60
    /**
     *
     * @type Random
     */
    this.random = Random

}

/**
 * @return {AsymKeyPair}
 */
SwlExchanger.prototype.generateKeyPair = function () {
    let encryptor = this.asymmetricEncryptorSupplier.get();
    return encryptor.generateKeyPair()
}

/**
 * @return {String}
 */
SwlExchanger.prototype.generateKeyPair = function () {
    let encryptor = this.symmetricEncryptorSupplier.get();
    return encryptor.generateKey()
}

/**
 * @param certId {String}
 * @return {SwlCertPair}
 */
SwlExchanger.prototype.generateCertPair = function (certId) {
    let encryptor = this.asymmetricEncryptorSupplier.get();
    let serverKeyPair = encryptor.generateKeyPair();
    let clientKeyPair = encryptor.generateKeyPair();
    return SwlCertUtil.make(certId, serverKeyPair, clientKeyPair);
}

/**
 * @param data {String|null}
 * @return {String|null}
 */
SwlExchanger.prototype.obfuscateEncode=function(data) {
    if (!data) {
        return null;
    }
    if (!this.obfuscator) {
        return data;
    }
    return this.obfuscator.encode(data);
}

/**
 * @param data {String|null}
 * @return {String|null}
 */
SwlExchanger.prototype.obfuscateDecode=function(data) {
    if (!data) {
        return null;
    }
    if (!this.obfuscator) {
        return data;
    }
    return this.obfuscator.decode(data);
}

/**
 *
 * @param cert {SwlCert}
 * @param parts {String[]}
 * @param attaches {String[]|null}
 * @return {SwlData}
 */
SwlExchanger.prototype.sendByCert=function(cert, parts, attaches=[]) {
    return this.sendByRaw(cert.remotePublicKey, cert.certId,
        cert.privateKey, cert.certId,
        parts, attaches);
}

/**
 *
 * @param remotePublicKey {String}
 * @param remoteAsymSign {String}
 * @param selfPrivateKey {String}
 * @param selfAsymSign {String}
 * @param parts {String[]}
 * @param attaches {String[]|null}
 * @return {SwlData}
 */
SwlExchanger.prototype.sendByRaw=function(remotePublicKey,remoteAsymSign,
                                          selfPrivateKey,selfAsymSign,
                                          , parts, attaches=[]) {
    let ret = new SwlData();
    ret.parts=[]
    ret.attaches=[]
    ret.header=new SwlHeader();
    ret.context=new SwlContext();

    ret.context.setRemotePublicKey(remotePublicKey);
    ret.header.setRemoteAsymSign(remoteAsymSign);
    ret.context.setRemoteAsymSign(remoteAsymSign);


    ret.context.setSelfPrivateKey(selfPrivateKey);
    ret.header.setLocalAsymSign(selfAsymSign);
    ret.context.setSelfAsymSign(selfAsymSign);


    String timestamp = String.valueOf(SystemClock.currentTimeMillis() / 1000);
    ret.header.setTimestamp(timestamp);
    ret.context.setTimestamp(timestamp);

    String nonce = String.valueOf(random.nextInt(0x7fff)) + String.valueOf(random.nextInt(0x7fff));
    ret.header.setNonce(nonce);
    ret.context.setNonce(nonce);

    ISwlSymmetricEncryptor symmetricEncryptor = symmetricEncryptorObjectPool.require();
    String key = symmetricEncryptor.generateKey();
    ret.context.setKey(key);

    ISwlAsymmetricEncryptor asymmetricEncryptor = asymmetricEncryptorObjectPool.require();
    asymmetricEncryptor.setPublicKey(remotePublicKey);
    String randomKey = asymmetricEncryptor.encrypt(key);
    ret.header.setRandomKey(randomKey);
    ret.context.setRandomKey(randomKey);

    symmetricEncryptor.setKey(key);
    StringBuilder builder = new StringBuilder();
    for (String part : parts) {
        String data = null;
        if (part != null) {
            data = symmetricEncryptor.encrypt(part);
        }
        if (data != null) {
            builder.append(data);
        }
        ret.getParts().add(data);
    }
    if (attaches != null) {
        for (String attach : attaches) {
            if (attach != null) {
                builder.append(attach);
            }
            ret.getAttaches().add(attach);
        }
    }

    symmetricEncryptorObjectPool.release(symmetricEncryptor);

    String data = builder.toString();
    ret.context.setData(data);

    String sign = messageDigester.digest(data + randomKey + timestamp + nonce + selfAsymSign + remoteAsymSign);
    ret.header.setSign(sign);
    ret.context.setSign(sign);

    asymmetricEncryptor.setPrivateKey(selfPrivateKey);
    String digital = asymmetricEncryptor.sign(sign);
    ret.header.setDigital(digital);
    ret.context.setDigital(digital);

    asymmetricEncryptorObjectPool.release(asymmetricEncryptor);

    encode(ret.getHeader());

    return ret;
}

/**
 *
 * @param header {SwlHeader}
 * @return {SwlHeader}
 */
SwlExchanger.prototype.encode=function(header) {
    header.randomKey=this.obfuscateEncode(header.randomKey);
    header.nonce=this.obfuscateEncode(header.nonce);
    header.sign=this.obfuscateEncode(header.sign);
    header.digital=this.obfuscateEncode(header.digital);
    return header;
}

/**
 *
 * @param header {SwlHeader}
 * @return {SwlHeader}
 */
SwlExchanger.prototype.decode=function(header) {
    header.randomKey=this.obfuscateDecode(header.randomKey);
    header.nonce=this.obfuscateDecode(header.nonce);
    header.sign=this.obfuscateDecode(header.sign);
    header.digital=this.obfuscateDecode(header.digital);
    return header;
}

/**
 *
 * @param header {SwlHeader}
 * @return {SwlHeader}
 */
SwlExchanger.prototype.swap=function(header) {
    let str=header.localAsymSign
    header.localAsymSign=header.remoteAsymSign
    header.remoteAsymSign=str
    return header;
}

export default SwlExchanger
