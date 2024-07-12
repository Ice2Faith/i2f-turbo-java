/**
 * @return SwlWebRes
 * @constructor
 */
function SwlWebRes() {
    /**
     *
     * @type {Object|Map|null}
     */
    this.config = {}
    /**
     *
     * @type {string|null}
     */
    this.baseURL = null
    /**
     *
     * @type {string|null}
     */
    this.url = null
    /**
     *
     * @type {string|null} , such as 'get','post','put','delete'
     */
    this.method = null
    /**
     *
     * @type {Object|Map}
     */
    this.headers = {}
    /**
     *
     * @type {Object|Map|null|undefined}
     */
    this.data = undefined
    /**
     *
     * @type {Object|Map|null|undefined}
     */
    this.params = undefined

}

export default SwlWebRes
