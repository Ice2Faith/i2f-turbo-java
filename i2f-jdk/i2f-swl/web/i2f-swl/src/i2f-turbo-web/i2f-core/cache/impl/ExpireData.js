/**
 *
 * @param data {Object}
 * @param expireTs {int}
 * @constructor
 */
function ExpireData(data, expireTs = -1) {
    this.data = data;
    this.expireTs = expireTs;
}

export default ExpireData
