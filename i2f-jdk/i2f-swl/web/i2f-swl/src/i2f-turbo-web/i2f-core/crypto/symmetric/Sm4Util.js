/**
 * AES工具
 */
import {sm4} from 'sm-crypto'
import CodeUtil from '../../util/CodeUtil'

const Sm4Util = {
    /**
     * @param size {int} , key size 128
     * @return {String} hex
     */
    genKey(size = 128) {
        size = Math.floor(size / 8)
        let ret = CodeUtil.makeCheckCode(size)
        return this.hexKey(ret)
    },
    /**
     *
     * @param key {String}
     * @return {String} hex
     */
    hexKey(key) {
        key = key + ''
        let ret = ''
        for (let i = 0; i < key.length; i++) {
            let hex = key.charCodeAt(i).toString(16)
            if (hex.length == 1) {
                hex = '0' + hex
            }
            ret += hex
        }
        return ret
    },
    /**
     *
     * @param data {String}
     * @param key {String} hex
     * @return {String} hex
     */
    encrypt: function (data, key) {
        let ret = sm4.encrypt(data, key)
        return ret
    },
    /**
     *
     * @param data {String} hex
     * @param key {String} hex
     * @return String
     */
    decrypt: function (data, key) {
        return sm4.decrypt(data, key)
    }
}
export default Sm4Util
