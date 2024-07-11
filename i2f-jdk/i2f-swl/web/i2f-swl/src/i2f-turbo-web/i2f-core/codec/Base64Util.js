/**
 * Base64工具
 */
import {Base64} from 'js-base64'

/**
 * @const
 */
const Base64Util = {
    /**
     *
     * @param data {String}
     * @return {String}
     */
    encrypt(data) {
        return Base64.encode(data)
    },
    /**
     *
     * @param data {String}
     * @return {String}
     */
    decrypt(data) {
        return Base64.decode(data)
    },
    /**
     *
     * @param data {Object}
     * @return {String}
     */
    encryptObj(data) {
        const js = JSON.stringify(data)
        return this.encrypt(js)
    },
    /**
     *
     * @param data {String}
     * @return {Object}
     */
    decryptObj: function (data) {
        const js = this.decrypt(data)
        return JSON.parse(js)
    }
}

export default Base64Util
