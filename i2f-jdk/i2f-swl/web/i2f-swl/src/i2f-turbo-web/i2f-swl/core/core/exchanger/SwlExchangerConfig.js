
/**
 * @return {SwlExchangerConfig}
 * @constructor
 */
function SwlExchangerConfig() {
    /**
     *
     * @type {boolean}
     */
    this.enableTimestamp = true;
    /**
     *
     * @type {long}
     */
    this.timestampExpireWindowSeconds = 30;

    /**
     *
     * @type {boolean}
     */
    this.enableNonce = false;
    /**
     *
     * @type {long}
     */
    this.nonceTimeoutSeconds = 180;

    /**
     *
     * @type {boolean}
     */
    this.enableEncrypt=true;
    /**
     *
     * @type {boolean}
     */
    this.enableDigital = true;
}

export default SwlExchangerConfig
