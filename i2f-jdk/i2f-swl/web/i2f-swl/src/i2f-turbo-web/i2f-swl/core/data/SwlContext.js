import AsymKeyPair from "../../../i2f-core/crypto/asymmetric/AsymKeyPair";

/**
 * @reutrn {SwlContext}
 * @constructor
 */
function SwlContext() {
    /**
     * @type {String}
     */
    this.remotePublicKey = null
    /**
     * @type {AsymKeyPair}
     */
    this.remoteAsymSign = null
    /**
     * @type {String}
     */
    this.selfPrivateKey = null
    /**
     * @type {String}
     */
    this.selfAsymSign = null
    /**
     * @type {String}
     */
    this.timestamp = null
    /**
     * @type {String}
     */
    this.nonce = null
    /**
     * @type {String}
     */
    this.key = null
    /**
     * @type {String}
     */
    this.randomKey = null
    /**
     * @type {String}
     */
    this.data = null
    /**
     * @type {String}
     */
    this.sign = null
    /**
     * @type {String}
     */
    this.digital = null
    /**
     * @type {String}
     */
    this.clientId = null
    /**
     * @type {String}
     */
    this.currentTimestamp = null
    /**
     * @type {String}
     */
    this.window = null
    /**
     * @type {String}
     */
    this.nonceKey = null
    /**
     * @type {boolean}
     */
    this.signOk = null
    /**
     * @type {boolean}
     */
    this.digitalOk = null
}

export default SwlContext
