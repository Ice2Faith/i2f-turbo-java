import AsymKeyPair from "../../../i2f-core/crypto/asymmetric/AsymKeyPair";

/**
 * @return {SwlTransferConfig}
 * @constructor
 */
function SwlTransferConfig() {
    /**
     * @type {String}
     */
    this.cacheKeyPrefix = null;
    /**
     * default 30 seconds
     * @type {int}
     */
    this.timestampExpireWindowSeconds = 30;
    /**
     * default 30 minutes
     * @type {int}
     */
    this.nonceTimeoutSeconds = 1800;
    /**
     * default 30 minute
     * @type {int}
     */
    this.certExpireSeconds = 1800;
    /**
     * @type {AsymKeyPair}
     */
    this.swapKeyPair = new AsymKeyPair(SwlTransferConfig.DEFAULT_SWAP_PUBLIC_KEY(),null);
}

/**
 * 默认RSA交换公钥
 * @return {String}
 */
SwlTransferConfig.DEFAULT_SWAP_RSA_PUBLIC_KEY = function () {
    return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnhyhaYnyql1Vt0CFroJqDKNcQAvcHyigtjTijXwg5h4841Zlt8xl98HX7a8YZmUTIAnNeKF5tMX6OAYGl/GteUXuL8QFbj7nNSo9cDdT7ZEfK+rs+d/Pz7zxbuMI1UbUs+OFXIICLqOT+Ze1KJoBlm9r42qqvwMEYntKG8KI4bwIDAQAB";
}

/**
 * 默认SM2交换公钥
 * @return {String}
 */
SwlTransferConfig.DEFAULT_SWAP_SM2_PUBLIC_KEY = function () {
    return "04ba6a9a137978cbee04f0783e14f7ea43eb81e2897c04c4561183b0bac1fbcf8c3aa63a5bd443bc1c58af06b55bb434322f5dbe296f780610f2d460beb41ef29a";
}

/**
 * 默认非对称交换公钥
 * @return {String}
 */
SwlTransferConfig.DEFAULT_SWAP_PUBLIC_KEY=function(){
    return SwlTransferConfig.DEFAULT_SWAP_RSA_PUBLIC_KEY()
}

export default SwlTransferConfig
