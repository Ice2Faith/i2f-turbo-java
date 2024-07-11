/**
 * RSA工具
 */
import JSEncrypt from '../static/jsencrypt'
import jsrsasign from 'jsrsasign'
import AsymKeyPair from './AsymKeyPair'

const RsaUtil = {
    /**
     * @param size {int} , key size,1024/2048
     * @return {AsymKeyPair}
     */
    genKeyPair(size = 1024) {
        if (!size || size <= 0) {
            size = 1024
        }
        const rsaKeypair = jsrsasign.KEYUTIL.generateKeypair('RSA', size)
        const publicKey = this.keyInflater(jsrsasign.KEYUTIL.getPEM(rsaKeypair.prvKeyObj)) // 获取公钥
        const privateKey = this.keyInflater(jsrsasign.KEYUTIL.getPEM(rsaKeypair.prvKeyObj, 'PKCS1PRV')) // 获取私钥
        return new AsymKeyPair(publicKey, privateKey)
    },
    /**
     *
     * @param str {String}
     * @returns {String}
     */
    keyInflater(str) {
        const arr = str.split('\n')
        let ret = ''
        for (let i = 1; i < arr.length - 2; i++) {
            ret += arr[i].trim()
        }
        return ret
    },
    /**
     *
     * @param pubKey {String} base64
     * @param text {String}
     * @return {String} base64
     */
    publicKeyEncrypt(pubKey, text) {
        const chiper = new JSEncrypt()
        chiper.setKey(pubKey)
        return chiper.encrypt(text, false)
    },
    /**
     *
     * @param pubKey {String} base64
     * @param text {String} base64
     * @return {String}
     */
    publicKeyDecrypt(pubKey, text) {
        const chiper = new JSEncrypt()
        chiper.setKey(pubKey)
        return chiper.decrypt(text, false)
    },
    /**
     *
     * @param priKey {String} base64
     * @param text {String}
     * @return {String} base64
     */
    privateKeyEncrypt(priKey, text) {
        const chiper = new JSEncrypt()
        chiper.setKey(priKey)
        return chiper.encrypt(text, true)
    },
    /**
     *
     * @param priKey {String} base64
     * @param text {String} base64
     * @return {String}
     */
    privateKeyDecrypt(priKey, text) {
        const chiper = new JSEncrypt()
        chiper.setKey(priKey)
        return chiper.decrypt(text, true)
    },
    /**
     *
     * @param priKey {String} base64
     * @param text {String}
     * @return {String} base64
     */
    makeSign(priKey, text) {
        return this.privateKeyEncrypt(priKey, text);
    },
    /**
     *
     * @param pubKey {String} base64
     * @param sign {String} base64
     * @param text {String}
     * @return boolean
     */
    verifySign(pubKey, sign, text) {
        let realData = this.publicKeyDecrypt(pubKey, sign)
        return realData == text
    }
}

export default RsaUtil
