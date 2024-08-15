/**
 * @return {SwlHeader}
 * @constructor
 */
function SwlHeader() {
    /**
     * 时间戳
     * @type {String}
     */
    this.timestamp = null;
    /**
     * 一次性消息
     * @type {String}
     */
    this.nonce = null;
    /**
     * 随机秘钥
     * @type {String}
     */
    this.randomKey = null;
    /**
     * 签名
     * @type {String}
     */
    this.sign = null;
    /**
     * 数字签名
     * @type {String}
     */
    this.digital = null;

    /**
     * 服务端非对称签名
     * @type {String}
     */
    this.remoteAsymSign = null;

    /**
     * 客户端非对称签名
     * @type {String}
     */
    this.localAsymSign = null;
}

/**
 *
 * @param src {SwlHeader}
 * @param dst {SwlHeader}
 * @return {SwlHeader}
 */
SwlHeader.copy = function (src, dst) {
    dst.timestamp=src.timestamp;
    dst.nonce = src.nonce;
    dst.randomKey = src.randomKey;
    dst.sign = src.sign;
    dst.digital = src.digital;
    dst.localAsymSign = src.localAsymSign;
    dst.remoteAsymSign = src.remoteAsymSign;
    return dst;
}

export default SwlHeader
