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
     * @type {boolean}
     */
    this.enableRefreshSelfKey = true;
    /**
     * default 24 hours
     * @type {int}
     */
    this.selfKeyExpireSeconds = 86400;
    /**
     * @type {int}
     */
    this.selfKeyMaxCount = 3;
    /**
     * default 24 hours
     * @type {int}
     */
    this.otherKeyExpireSeconds = 86400;
}

export default SwlTransferConfig
