import SwlRsaAsymmetricEncryptorSupplier from "../impl/supplier/SwlRsaAsymmetricEncryptorSupplier";
import SwlAesSymmetricEncryptorSupplier from "../impl/supplier/SwlAesSymmetricEncryptorSupplier";
import SwlSha256MessageDigester from "../impl/SwlSha256MessageDigester";
import SwlBase64Obfuscator from "../impl/SwlBase64Obfuscator";
import MemMapExpireCache from "../../../i2f-core/cache/impl/MemMapExpireCache";
import SwlTransferConfig from "./SwlTransferConfig";
import Random from "../../../i2f-core/util/Random";
import AsymKeyPair from "../../../i2f-core/crypto/asymmetric/AsymKeyPair";
import SystemClock from "../../../i2f-core/clock/SystemClock";
import SwlData from "../data/SwlData";
import SwlHeader from "../data/SwlHeader";
import SwlContext from "../data/SwlContext";
import SwlException from "../exception/SwlException";
import SwlCode from "../consts/SwlCode";

/**
 * @return SwlTransfer
 * @constructor
 */
function SwlTransfer() {
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
     * @type {IExpireCache}
     */
    this.cache = new MemMapExpireCache();
    /**
     *
     * @type {SwlTransferConfig}
     */
    this.config = new SwlTransferConfig()
    /**
     *
     * @type Random
     */
    this.random = Random
    /**
     *
     * @type {boolean}
     */
    this.refreshing = false
    /**
     *
     * @type {number}
     */
    this.schedulePool = null
}

SwlTransfer.SELF_KEY_PAIR_CURRENT_KEY = function () {
    return "swl:key:self:current"

};
SwlTransfer.SELF_KEY_PAIR_HISTORY_KEY_PREFIX = function () {
    return "swl:key:self:history:"
};
SwlTransfer.OTHER_KEY_PUBLIC_KEY_PREFIX = function () {
    return "swl:key:other:keys:"
};
SwlTransfer.OTHER_KEY_PUBLIC_DEFAULT = function () {
    return "swl:key:other:default"
};
SwlTransfer.NONCE_PREFIX = function () {
    return "swl:nonce:"
};
SwlTransfer.KEYPAIR_SEPARATOR = function () {
    return "\n==========\n"
};

/**
 * @return {void}
 */
SwlTransfer.prototype.initialRefreshingThread = function () {
    if (this.refreshing) {
        return
    }
    this.refreshing = true
    let timeout = this.config.selfKeyExpireSeconds - 10;
    if (timeout <= 0) {
        timeout = this.config.selfKeyExpireSeconds;
    }
    let _this = this
    this.schedulePool = setInterval(() => {
        _this.resetSelfKeyPair()
    }, timeout)

    window.onclose = function () {
        if (_this.schedulePool) {
            clearInterval(_this.schedulePool)
            _this.schedulePool = null
        }
    }
}

/**
 *
 * @param key {String}
 * @return {String}
 */
SwlTransfer.prototype.cacheKey = function (key) {
    let cacheKeyPrefix = this.config.cacheKeyPrefix;
    if (cacheKeyPrefix == null || cacheKeyPrefix === "") {
        return key;
    }
    return cacheKeyPrefix + ":" + key;
}

/**
 *
 * @return {AsymKeyPair}
 */
SwlTransfer.prototype.getSelfKeyPair = function () {
    let obj = this.cache.get(this.cacheKey(SwlTransfer.SELF_KEY_PAIR_CURRENT_KEY()));
    if (obj == null) {
        return this.resetSelfKeyPair();
    }
    let key = SwlTransfer.SELF_KEY_PAIR_HISTORY_KEY_PREFIX() + obj;
    obj = this.cache.get(this.cacheKey(key));
    if (obj == null) {
        return this.resetSelfKeyPair();
    }

    let arr = obj.split(SwlTransfer.KEYPAIR_SEPARATOR(), 2);
    let ret = new AsymKeyPair();
    ret.setPublicKey(this.obfuscateDecode(arr[0]));
    ret.setPrivateKey(this.obfuscateDecode(arr[1]));
    return ret;
}

/**
 * @return {String}
 */
SwlTransfer.prototype.getSelfPublicKey = function () {
    let keyPair = this.getSelfKeyPair();
    return keyPair.getPublicKey();
}

/**
 *
 * @param publicKey String
 * @return {String}
 */
SwlTransfer.prototype.calcKeySign = function (publicKey) {
    return this.messageDigester.digest(publicKey);
}

/**
 *
 * @param selfAsymSign {String}
 * @param selfKeyPair {AsymKeyPair}
 * @return {void}
 */
SwlTransfer.prototype.setSelfKeyPair = function (selfAsymSign, selfKeyPair) {
    let key = SwlTransfer.SELF_KEY_PAIR_HISTORY_KEY_PREFIX() + selfAsymSign;
    let keyPair = new AsymKeyPair(
        this.obfuscateEncode(selfKeyPair.getPublicKey()),
        this.obfuscateEncode(selfKeyPair.getPrivateKey())
    );
    let text = keyPair.getPublicKey() + SwlTransfer.KEYPAIR_SEPARATOR() + keyPair.getPrivateKey();
    this.cache.setWith(this.cacheKey(key), text, this.config.selfKeyMaxCount * this.config.selfKeyExpireSeconds);
    this.cache.set(this.cacheKey(SwlTransfer.SELF_KEY_PAIR_CURRENT_KEY()), selfAsymSign);
}

/**
 *
 * @param selfAsymSign {String}
 * @return {String|null}
 */
SwlTransfer.prototype.getSelfPrivateKey = function (selfAsymSign) {
    let key = SwlTransfer.SELF_KEY_PAIR_HISTORY_KEY_PREFIX() + selfAsymSign;
    let obj = this.cache.get(this.cacheKey(key));
    if (obj == null) {
        return null;
    }
    let arr = obj.split(SwlTransfer.KEYPAIR_SEPARATOR(), 2);
    return this.obfuscateDecode(arr[1]);
}

/**
 *
 * @param otherAsymSign {String}
 * @return {String|null}
 */
SwlTransfer.prototype.getOtherPublicKey = function (otherAsymSign) {
    let key = SwlTransfer.OTHER_KEY_PUBLIC_KEY_PREFIX() + otherAsymSign;
    let obj = this.cache.get(this.cacheKey(key));
    if (obj == null) {
        return null;
    }
    return this.obfuscateDecode(obj);
}

/**
 *
 * @return {String|null}
 */
SwlTransfer.prototype.getOtherPublicKeyDefault = function () {
    let obj = this.cache.get(this.cacheKey(SwlTransfer.OTHER_KEY_PUBLIC_DEFAULT()));
    if (obj == null) {
        return null;
    }
    let otherAsymSign = obj;
    return this.getOtherPublicKey(otherAsymSign);
}

/**
 *
 * @param otherAsymSign {String}
 * @param publicKey {String}
 * @return {void}
 */
SwlTransfer.prototype.setOtherPublicKey = function (otherAsymSign, publicKey) {
    let key = SwlTransfer.OTHER_KEY_PUBLIC_KEY_PREFIX() + otherAsymSign;
    this.cache.setWith(this.cacheKey(key), this.obfuscateEncode(publicKey), this.config.otherKeyExpireSeconds);
    this.cache.set(this.cacheKey(SwlTransfer.OTHER_KEY_PUBLIC_DEFAULT()), otherAsymSign);
}

/**
 *
 * @param otherAsymSign {String}
 * @return {void}
 */
SwlTransfer.prototype.refreshOtherPublicKeyExpire = function (otherAsymSign) {
    let key = SwlTransfer.OTHER_KEY_PUBLIC_KEY_PREFIX() + otherAsymSign;
    this.cache.expire(this.cacheKey(key), this.config.otherKeyExpireSeconds);
}

/**
 *
 * @param nonce {String}
 * @return {boolean}
 */
SwlTransfer.prototype.containsNonce = function (nonce) {
    let key = SwlTransfer.NONCE_PREFIX() + nonce;
    return this.cache.exists(this.cacheKey(key));
}

/**
 *
 * @param nonce {String}
 * @param timeoutSeconds {int}
 * @return {void}
 */
SwlTransfer.prototype.setNonce = function (nonce, timeoutSeconds) {
    let key = SwlTransfer.NONCE_PREFIX() + nonce;
    this.cache.setWith(this.cacheKey(key), '' + SystemClock.currentTimeMillis(), timeoutSeconds);
}

/**
 *
 * @param otherPublicKey {String}
 * @return {void}
 */
SwlTransfer.prototype.acceptOtherPublicKey = function (otherPublicKey) {
    let otherAsymSign = this.messageDigester.digest(otherPublicKey);
    this.setOtherPublicKey(otherAsymSign, otherPublicKey);
}

/**
 *
 * @return {String}
 */
SwlTransfer.prototype.getSelfSwapKey = function () {
    let selfPublicKey = this.getSelfPublicKey();
    return this.obfuscateEncode(selfPublicKey);
}

/**
 *
 * @param otherSwapKey {String}
 * @return {void}
 */
SwlTransfer.prototype.acceptOtherSwapKey = function (otherSwapKey) {
    let otherPublicKey = this.obfuscateDecode(otherSwapKey);
    this.acceptOtherPublicKey(otherPublicKey);
}

/**
 *
 * @return {AsymKeyPair}
 */
SwlTransfer.prototype.resetSelfKeyPair = function () {
    let asymmetricEncryptor = this.asymmetricEncryptorSupplier.get();
    let asymKeyPair = asymmetricEncryptor.generateKeyPair();
    let selfAsymSign = this.messageDigester.digest(asymKeyPair.getPublicKey());
    this.setSelfKeyPair(selfAsymSign, asymKeyPair);
    return asymKeyPair;
}

/**
 *
 * @param data {String|null}
 * @return {String|null}
 */
SwlTransfer.prototype.obfuscateEncode = function (data) {
    if (!data) {
        return null;
    }
    if (!this.obfuscator) {
        return data;
    }
    return this.obfuscator.encode(data);
}

/**
 *
 * @param data {String|null}
 * @return {String|null}
 */
SwlTransfer.prototype.obfuscateDecode = function (data) {
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
 * @param remotePublicKey {String}
 * @param parts {string[]}
 * @param attaches {string[]|null}
 * @return {SwlData}
 */
SwlTransfer.prototype.send = function (remotePublicKey, parts, attaches = null) {
    let ret = new SwlData();
    ret.parts = [];
    ret.attaches = []
    ret.header = new SwlHeader();
    ret.context = new SwlContext();

    ret.context.remotePublicKey = remotePublicKey;

    let asymmetricEncryptor = this.asymmetricEncryptorSupplier.get();
    let symmetricEncryptor = this.symmetricEncryptorSupplier.get();

    let selfKeyPair = this.getSelfKeyPair();
    ret.context.selfKeyPair = selfKeyPair;

    let localAsymSign = this.messageDigester.digest(selfKeyPair.getPublicKey());
    ret.header.localAsymSign = localAsymSign;
    ret.context.localAsymSign = localAsymSign;

    let remoteAsymSign = this.messageDigester.digest(remotePublicKey);
    ret.header.remoteAsymSign = remoteAsymSign;
    ret.context.remoteAsymSign = remoteAsymSign;

    let timestamp = Math.floor(SystemClock.currentTimeMillis() / 1000);
    let seq = this.random.nextLowerInt(0x7fff);
    let nonce = timestamp.toString(16) + "-" + seq.toString(16);
    ret.header.nonce = nonce;
    ret.context.timestamp = ('' + timestamp);
    ret.context.seq = ('' + seq);
    ret.context.nonce = (nonce);

    let key = symmetricEncryptor.generateKey();
    ret.context.key = (key);

    asymmetricEncryptor.setPublicKey(remotePublicKey);
    let randomKey = asymmetricEncryptor.encrypt(key);
    ret.header.randomKey = (randomKey);
    ret.context.randomKey = (randomKey);

    symmetricEncryptor.setKey(key);
    let builder = '';
    for (let i = 0; i < parts.length; i++) {
        let part = parts[i]
        let data = null;
        if (part) {
            data = symmetricEncryptor.encrypt(part);
        }
        if (data) {
            builder += data;
        }
        ret.parts.push(data);
    }
    if (attaches) {
        for (let i = 0; i < attaches.length; i++) {
            let attach = attaches[i]
            if (attach) {
                builder += attach;
            }
            ret.attaches.push(attach);
        }
    }

    let data = builder;
    ret.context.data = (data);

    let sign = this.messageDigester.digest(data + randomKey + nonce + localAsymSign + remoteAsymSign);
    ret.header.sign = (sign);
    ret.context.sign = (sign);

    asymmetricEncryptor.setPrivateKey(selfKeyPair.getPrivateKey());
    let digital = asymmetricEncryptor.sign(sign);
    ret.header.digital = (digital);
    ret.context.digital = (digital);

    ret.header.localAsymSign = (this.obfuscateEncode(ret.header.localAsymSign));
    ret.header.remoteAsymSign = (this.obfuscateEncode(ret.header.remoteAsymSign));
    ret.header.randomKey = (this.obfuscateEncode(ret.header.randomKey));
    ret.header.nonce = (this.obfuscateEncode(ret.header.nonce));
    ret.header.sign = (this.obfuscateEncode(ret.header.sign));
    ret.header.digital = (this.obfuscateEncode(ret.header.digital));
    return ret;
}

/**
 *
 * @param clientId {String}
 * @param request {SwlData}
 * @return {SwlData}
 */
SwlTransfer.prototype.receive = function (clientId, request) {
    let ret = new SwlData();
    ret.parts = ([]);
    ret.attaches = ([]);
    ret.header = (SwlHeader.copy(request.header));
    ret.context = (new SwlContext());

    ret.header.localAsymSign = (this.obfuscateDecode(ret.header.localAsymSign));
    ret.header.remoteAsymSign = (this.obfuscateDecode(ret.header.remoteAsymSign));
    ret.header.randomKey = (this.obfuscateDecode(ret.header.randomKey));
    ret.header.nonce = (this.obfuscateDecode(ret.header.nonce));
    ret.header.sign = (this.obfuscateDecode(ret.header.sign));
    ret.header.digital = (this.obfuscateDecode(ret.header.digital));

    let str = ret.header.localAsymSign;
    ret.header.localAsymSign = (ret.header.remoteAsymSign);
    ret.header.remoteAsymSign = (str);

    ret.context.clientId = (clientId);

    let currentTimestamp = Math.floor(SystemClock.currentTimeMillis() / 1000);
    ret.context.currentTimestamp = ('' + currentTimestamp);

    let nonce = ret.header.nonce;
    ret.context.nonce = (nonce);
    if (nonce == null || nonce === "") {
        throw new SwlException(SwlCode.NONCE_MISSING_EXCEPTION(), "nonce cannot is empty!");
    }

    let nonceArr = nonce.split("-", 2);
    if (nonceArr.length != 2) {
        throw new SwlException(SwlCode.NONCE_INVALID_EXCEPTION(), "nonce is invalid!");
    }

    let timestamp = parseInt(nonceArr[0], 16);
    let seq = nonceArr[1];
    ret.context.timestamp = ('' + timestamp);
    ret.context.seq = (seq);

    let window = this.config.nonceWindowSeconds;
    ret.context.window = ('' + window);

    if (Math.abs(currentTimestamp - timestamp) > window) {
        throw new SwlException(SwlCode.NONCE_TIMESTAMP_EXCEED_EXCEPTION(), "timestamp is exceed allow window seconds!");
    }

    let nonceKey = nonce;
    if (clientId != null && clientId !== "") {
        nonceKey = clientId + "-" + nonce;
    }
    ret.context.nonceKey = (nonceKey);
    if (this.containsNonce(nonceKey)) {
        throw new SwlException(SwlCode.NONCE_ALREADY_EXISTS_EXCEPTION(), "nonce key already exists!");
    }

    this.setNonce(nonceKey, this.config.nonceTimeoutSeconds);

    let sign = ret.header.sign;
    ret.context.sign = (sign);
    if (sign == null || sign === "") {
        throw new SwlException(SwlCode.SIGN_MISSING_EXCEPTION(), "sign cannot be empty!");
    }

    let builder = "";
    let parts = request.parts;
    if (parts != null) {
        for (let i = 0; i < parts.length; i++) {
            let part = parts[i]
            if (part != null) {
                builder += part;
            }
        }
    }
    let attaches = request.attaches;
    if (attaches != null) {
        for (let i = 0; i < attaches.length; i++) {
            let attach = attaches[i]
            if (attach != null) {
                builder += attach;
            }
            ret.attaches.push(attach)
        }
    }

    let data = builder;
    ret.context.data = (data);

    let randomKey = ret.header.randomKey;
    ret.context.randomKey = (randomKey);
    if (randomKey == null || randomKey === "") {
        throw new SwlException(SwlCode.RANDOM_KEY_MISSING_EXCEPTION(), "random key cannot be empty!");
    }

    let remoteAsymSign = ret.header.remoteAsymSign;
    ret.context.remoteAsymSign = (remoteAsymSign);
    if (remoteAsymSign == null || remoteAsymSign === "") {
        throw new SwlException(SwlCode.CLIENT_ASYM_KEY_SIGN_MISSING_EXCEPTION(), "remote asym sign cannot be empty!");
    }

    let localAsymSign = ret.header.localAsymSign;
    ret.context.localAsymSign = (localAsymSign);
    if (localAsymSign == null || localAsymSign === "") {
        throw new SwlException(SwlCode.SERVER_ASYM_KEY_SIGN_MISSING_EXCEPTION(), "local asym sign cannot be empty!");
    }

    let signOk = this.messageDigester.verify(sign, data + randomKey + nonce + remoteAsymSign + localAsymSign);
    ret.context.signOk = (signOk);
    if (!signOk) {
        throw new SwlException(SwlCode.SIGN_VERIFY_FAILURE_EXCEPTION(), "verify sign failure!");
    }

    let digital = ret.header.digital;
    ret.context.digital = (digital);
    if (digital == null || digital === "") {
        throw new SwlException(SwlCode.DIGITAL_MISSING_EXCEPTION(), "digital cannot be empty!");
    }

    let remotePublicKey = this.getOtherPublicKey(remoteAsymSign);
    ret.context.remotePublicKey = (remotePublicKey);
    if (remotePublicKey == null || remotePublicKey === "") {
        throw new SwlException(SwlCode.CLIENT_ASYM_KEY_NOT_FOUND_EXCEPTION(), "remote key not found!");
    }

    this.refreshOtherPublicKeyExpire(remoteAsymSign);

    let asymmetricEncryptor = this.asymmetricEncryptorSupplier.get();
    asymmetricEncryptor.setPublicKey(remotePublicKey);

    let digitalOk = asymmetricEncryptor.verify(digital, sign);
    ret.context.digitalOk = (digitalOk);
    if (!digitalOk) {
        throw new SwlException(SwlCode.DIGITAL_VERIFY_FAILURE_EXCEPTION(), "verify digital failure!");
    }

    let localPrivateKey = this.getSelfPrivateKey(localAsymSign);
    ret.context.selfKeyPair = (new AsymKeyPair(null, localPrivateKey));
    if (localPrivateKey == null || localPrivateKey === "") {
        throw new SwlException(SwlCode.SERVER_ASYM_KEY_NOT_FOUND_EXCEPTION(), "server key not found!");
    }

    asymmetricEncryptor.setPrivateKey(localPrivateKey);
    let key = asymmetricEncryptor.decrypt(randomKey);
    ret.context.key = (key);
    if (key == null || key === "") {
        throw new SwlException(SwlCode.RANDOM_KEY_INVALID_EXCEPTION(), "random key is invalid!");
    }

    let symmetricEncryptor = this.symmetricEncryptorSupplier.get();
    symmetricEncryptor.setKey(key);

    if (parts != null) {
        for (let i = 0; i < parts.length; i++) {
            let part = parts[i]
            let item = null;
            if (part != null) {
                item = symmetricEncryptor.decrypt(part);
            }
            ret.parts.push(item);
        }
    }

    return ret;
}

/**
 *
 * @param parts {string[]}
 * @param attaches {string[]|null}
 * @return {SwlData}
 */
SwlTransfer.prototype.sendDefault = function (parts, attaches = null) {
    let otherPublicKey = this.getOtherPublicKeyDefault();
    return this.send(otherPublicKey, parts, attaches);
}

/**
 *
 * @param remoteAsymSign {String}
 * @param parts {string[]}
 * @param attaches {string[]|null}
 * @return {SwlData}
 */
SwlTransfer.prototype.response = function (remoteAsymSign, parts, attaches = null) {
    let otherPublicKey = this.getOtherPublicKey(remoteAsymSign);
    return this.send(otherPublicKey, parts, attaches);
}

export default SwlTransfer
