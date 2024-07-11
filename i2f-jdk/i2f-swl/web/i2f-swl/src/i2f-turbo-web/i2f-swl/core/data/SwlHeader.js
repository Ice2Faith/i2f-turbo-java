/**
 * @return {SwlHeader}
 * @constructor
 */
function SwlHeader() {
    /**
     * 签名
     * @type {String}
     */
    this.sign = null;
    /**
     * 一次性消息
     * @type {String}
     */
    this.nonce = null;
    /**
     * 数字签名
     * @type {String}
     */
    this.digital = null;
    /**
     * 随机秘钥
     * @type {String}
     */
    this.randomKey = null;
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
 * @param header {SwlHeader}
 * @return {SwlHeader}
 */
SwlHeader.copy = function (header) {
    let ret = new SwlHeader();
    ret.sign = header.sign;
    ret.nonce = header.nonce;
    ret.digital = header.digital;
    ret.randomKey = header.randomKey;
    ret.localAsymSign = header.localAsymSign;
    ret.remoteAsymSign = header.remoteAsymSign;
    return ret;
}

export default SwlHeader
