import SwlRsaAsymmetricEncryptorSupplier from "../../impl/supplier/SwlRsaAsymmetricEncryptorSupplier";
import SwlAesSymmetricEncryptorSupplier from "../../impl/supplier/SwlAesSymmetricEncryptorSupplier";
import SwlSha256MessageDigester from "../../impl/SwlSha256MessageDigester";
import SwlBase64Obfuscator from "../../impl/SwlBase64Obfuscator";
import SwlEmptyNonceManager from "./impl/SwlEmptyNonceManager";
import SwlCertUtil from "../../cert/SwlCertUtil";
import SwlData from "../../data/SwlData";
import SwlHeader from "../../data/SwlHeader";
import SwlContext from "../../data/SwlContext";
import SystemClock from "../../../../i2f-core/clock/SystemClock";
import Random from "../../../../i2f-core/util/Random";
import SwlException from "../../exception/SwlException";
import SwlCode from "../../consts/SwlCode";

/**
 * @return {SwlExchanger}
 * @constructor
 */
function SwlExchanger() {
    /**
     *
     * @type {boolean}
     */
    this.enableTimestamp=true
    /**
     *
     * @type {long} seconds
     */
    this.timestampExpireWindowSeconds = 30
    /**
     *
     * @type {boolean}
     */
    this.enableNonce=true
    /**
     *
     * @type {long} seconds
     */
    this.nonceTimeoutSeconds=30*60
    /**
     *
     * @type {boolean}
     */
    this.enableDigital=true
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

    /**
     *
     * @type {SwlNonceManager}
     */
    this.nonceManager = new SwlEmptyNonceManager();

    /**
     *
     * @type Random
     */
    this.random = new Random()

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
SwlExchanger.prototype.generateKey = function () {
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
                                           parts, attaches=[]) {
    let ret = new SwlData();
    ret.parts=[]
    ret.attaches=[]
    ret.header=new SwlHeader();
    ret.context=new SwlContext();

    ret.context.remotePublicKey=remotePublicKey;
    ret.header.remoteAsymSign=remoteAsymSign;
    ret.context.remoteAsymSign=remoteAsymSign;


    ret.context.selfPrivateKey=selfPrivateKey;
    ret.header.localAsymSign=selfAsymSign;
    ret.context.selfAsymSign=selfAsymSign;


    let timestamp = ''+Math.floor(SystemClock.currentTimeMillis() / 1000);
    ret.header.timestamp=timestamp;
    ret.context.timestamp=timestamp;

    let nonce = ''+(this.random.nextLowerInt(0x7fff)) + ''+(this.random.nextLowerInt(0x7fff));
    ret.header.nonce=nonce;
    ret.context.nonce=nonce;

    let symmetricEncryptor = this.symmetricEncryptorSupplier.get();
    let key = symmetricEncryptor.generateKey();
    ret.context.key=key;

    let asymmetricEncryptor = this.asymmetricEncryptorSupplier.get();
    asymmetricEncryptor.setPublicKey(remotePublicKey);
    let randomKey = asymmetricEncryptor.encrypt(key);
    ret.header.randomKey=randomKey;
    ret.context.randomKey=randomKey;

    symmetricEncryptor.setKey(key);
    let builder = '';
    for (let i=0;i<parts.length;i++) {
        let part=parts[i]
        let data = null;
        if (part) {
            data = symmetricEncryptor.encrypt(part);
        }
        if (data) {
            builder+=data;
        }
        ret.parts.push(data);
    }
    if (attaches != null) {
        for (let i=0;i< attaches.length;i++) {
            let attach=attaches[i]
            if (attach != null) {
                builder+=attach;
            }
            ret.attaches.push(attach);
        }
    }

    let data = builder;
    ret.context.data=data;

    let sign = this.messageDigester.digest(data + randomKey + timestamp + nonce + selfAsymSign + remoteAsymSign);
    ret.header.sign=sign;
    ret.context.sign=sign;


    let digital="none";
    if(this.enableDigital) {
        asymmetricEncryptor.setPrivateKey(selfPrivateKey);
        digital = asymmetricEncryptor.sign(sign);
    }
    ret.header.digital=digital;
    ret.context.digital=digital;


    this.encode(ret.header);

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

/**
 *
 * @param request {SwlData}
 * @param cert {SwlCert}
 * @param clientId {String|null}
 * @return {SwlData}
 */
SwlExchanger.prototype.receiveByCert=function(request,
    cert,
    clientId=null) {
    if(!clientId){
        clientId=cert.certId
    }
    return this.receiveByRaw(clientId, request,
        cert.remotePublicKey, cert.privateKey);
}

/**
 *
 * @param clientId {String}
 * @param request {SwlData}
 * @param remotePublicKey {String}
 * @param selfPrivateKey {String}
 * @return {SwlData}
 */
SwlExchanger.prototype.receiveByRaw=function(clientId,
    request,
    remotePublicKey,
    selfPrivateKey) {
    let ret = new SwlData();
    ret.parts=[];
    ret.attaches=[];
    ret.header=SwlHeader.copy(request.header, new SwlHeader());
    ret.context=new SwlContext();

    this.decode(ret.header);

    this.swap(ret.header);

    ret.context.clientId=clientId;

    let currentTimestamp = Math.floor(SystemClock.currentTimeMillis() / 1000);
    ret.context.currentTimestamp=''+(currentTimestamp);

    let timestamp = ret.header.timestamp;
    let ts = parseInt(timestamp);
    ret.context.timestamp=timestamp;

    let window = this.timestampExpireWindowSeconds;
    ret.context.window=(''+(window));

    if(this.enableTimestamp) {
        if (Math.abs(currentTimestamp - ts) > window) {
            throw new SwlException(SwlCode.NONCE_TIMESTAMP_EXCEED_EXCEPTION.code(), "timestamp is exceed allow window seconds!");
        }
    }

    let nonce = ret.header.nonce;
    ret.context.nonce=nonce;
    if (!nonce || nonce=='') {
        throw new SwlException(SwlCode.NONCE_MISSING_EXCEPTION.code(), "nonce cannot is empty!");
    }

    if(this.enableNonce) {
        if (this.nonceManager != null) {
            let nonceKey = timestamp + "-" + nonce;
            if (clientId && clientId != '') {
                nonceKey = clientId + "-" + nonce;
            }
            ret.context.nonceKey = nonceKey;
            if (this.nonceManager.contains(nonceKey)) {
                throw new SwlException(SwlCode.NONCE_ALREADY_EXISTS_EXCEPTION.code(), "nonce key already exists!");
            }

            this.nonceManager.set(nonceKey, this.nonceTimeoutSeconds);
        }
    }

    let sign = ret.header.sign;
    ret.context.sign=sign;
    if (!sign || sign=='') {
        throw new SwlException(SwlCode.SIGN_MISSING_EXCEPTION.code(), "sign cannot be empty!");
    }

    let builder = '';
    let parts = request.parts;
    if (parts != null) {
        for (let i=0;i<parts.length;i++) {
            let part=parts[i]
            if (part) {
                builder+=part;
            }
        }
    }

    let attaches = request.attaches;
    if (attaches != null) {
        for (let i=0;i<attaches.length;i++) {
            let attach=attaches[i]
            if (attach) {
                builder+=attach;
            }
        }
    }

    let data = builder;
    ret.context.data=data;

    let randomKey = ret.header.randomKey;
    ret.context.randomKey=randomKey;
    if (!randomKey || randomKey=='') {
        throw new SwlException(SwlCode.RANDOM_KEY_MISSING_EXCEPTION.code(), "random key cannot be empty!");
    }

    let remoteAsymSign = ret.header.remoteAsymSign;
    ret.context.remoteAsymSign=remoteAsymSign;
    if (!remoteAsymSign || remoteAsymSign=='') {
        throw new SwlException(SwlCode.CLIENT_ASYM_KEY_SIGN_MISSING_EXCEPTION.code(), "remote asym sign cannot be empty!");
    }

    let localAsymSign = ret.header.localAsymSign;
    ret.context.selfAsymSign=localAsymSign;
    if (!localAsymSign || localAsymSign=='') {
        throw new SwlException(SwlCode.SERVER_ASYM_KEY_SIGN_MISSING_EXCEPTION.code(), "local asym sign cannot be empty!");
    }

    let signOk = this.messageDigester.verify(sign, data + randomKey + timestamp + nonce + remoteAsymSign + localAsymSign);
    ret.context.signOk=signOk;
    if (!signOk) {
        throw new SwlException(SwlCode.SIGN_VERIFY_FAILURE_EXCEPTION.code(), "verify sign failure!");
    }

    let digital = ret.header.digital;
    ret.context.digital=digital;
    if (!digital || digital=='') {
        throw new SwlException(SwlCode.DIGITAL_MISSING_EXCEPTION.code(), "digital cannot be empty!");
    }

    ret.context.remotePublicKey=remotePublicKey;
    if (!remotePublicKey || remotePublicKey=='') {
        throw new SwlException(SwlCode.CLIENT_ASYM_KEY_NOT_FOUND_EXCEPTION.code(), "remote key not found!");
    }

    let asymmetricEncryptor = this.asymmetricEncryptorSupplier.get();
    asymmetricEncryptor.setPublicKey(remotePublicKey);

    if(this.enableDigital) {
        let digitalOk = asymmetricEncryptor.verify(digital, sign);
        ret.context.digitalOk = digitalOk;
        if (!digitalOk) {
            throw new SwlException(SwlCode.DIGITAL_VERIFY_FAILURE_EXCEPTION.code(), "verify digital failure!");
        }
    }

    ret.context.selfPrivateKey=selfPrivateKey;
    if (!selfPrivateKey || selfPrivateKey=='') {
        throw new SwlException(SwlCode.SERVER_ASYM_KEY_NOT_FOUND_EXCEPTION.code(), "server key not found!");
    }

    asymmetricEncryptor.setPrivateKey(selfPrivateKey);
    let key = asymmetricEncryptor.decrypt(randomKey);
    ret.context.key=key;
    if (!key  || key=='') {
        throw new SwlException(SwlCode.RANDOM_KEY_INVALID_EXCEPTION.code(), "random key is invalid!");
    }


    let symmetricEncryptor = this.symmetricEncryptorSupplier.get();
    symmetricEncryptor.setKey(key);

    if (parts != null) {
        for (let i=0;i<parts.length;i++) {
            let part=parts[i]
            let item = null;
            if (part) {
                item = symmetricEncryptor.decrypt(part);
            }
            ret.parts.push(item);
        }
    }

    return ret;
}

export default SwlExchanger
