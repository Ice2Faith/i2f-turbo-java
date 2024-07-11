/**
 * RSA工具
 */
import {sm2} from 'sm-crypto'
import AsymKeyPair from './AsymKeyPair'
import Exception from "../../excception/Exception";

const Sm2Util = {
    /**
     *
     * @return {int}
     */
    MODE_C1C3C2: () => 0,
    /**
     *
     * @return {int}
     */
    MODE_C1C2C3: () => 1,
    /**
     *
     * @return {String}
     */
    EXCEPTION_TYPE() {
        return "Sm2Exception"
    },
    /**
     * @type {int}
     */
    _cipherMode: 1,
    /**
     *
     * @param size {int} , key size 1024
     * @return {AsymKeyPair}
     */
    genKeyPair(size = 1024) {
        let keyPair = sm2.generateKeyPairHex()
        let publicKey = keyPair.publicKey // 公钥
        let privateKey = keyPair.privateKey // 私钥
        return AsymKeyPair(publicKey, privateKey)
    },
    /**
     *
     * @param pubKey {String} hex
     * @param text {String}
     * @return {string} hex
     */
    publicKeyEncrypt(pubKey, text) {
        let ret = sm2.doEncrypt(text, pubKey, this._cipherMode)
        return ret
    },
    /**
     *
     * @param pubKey {String} hex
     * @param text {String} hex
     */
    publicKeyDecrypt(pubKey, text) {
        throw Exception(this.EXCEPTION_TYPE(), "Sm2算法不支持公钥解密")
    },
    /**
     *
     * @param priKey {String} hex
     * @param text {String}
     */
    privateKeyEncrypt(priKey, text) {
        throw Exception(this.EXCEPTION_TYPE(), "Sm2算法不支持私钥加密")
    },
    /**
     *
     * @param priKey {String} hex
     * @param text {String} hex
     * @return {String}
     */
    privateKeyDecrypt(priKey, text) {
        return sm2.doDecrypt(text, priKey, this._cipherMode)
    },
    /**
     *
     * @param priKey {String} hex
     * @param text {String}
     * @return {String} hex
     */
    makeSign(priKey, text) {
        return sm2.doSignature(text, priKey)
    },
    /**
     *
     * @param pubKey {String} hex
     * @param sign {String} hex
     * @param text {String}
     * @return {boolean}
     */
    verifySign(pubKey, sign, text) {
        return sm2.doVerifySignature(text, sign, pubKey)
    }
}

export default Sm2Util
