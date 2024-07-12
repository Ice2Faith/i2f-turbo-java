/**
 * RSA工具
 */
import {sm2} from 'sm-crypto'
import AsymKeyPair from '../../data/AsymKeyPair'
import SecureException from "../../excception/SecureException";
import SecureErrorCode from "../../consts/SecureErrorCode";
import Base64Util from "../../util/Base64Util";

const Sm2Util = {
    MODE_C1C3C2: () => 0,
    MODE_C1C2C3: () => 1,
    cipherMode: 1,
    genKeyPair(size = 1024) {
        let keyPair = sm2.generateKeyPairHex()
        let publicKey = keyPair.publicKey // 公钥
        let privateKey = keyPair.privateKey // 私钥
        return AsymKeyPair.newObj(publicKey, privateKey)
    },
    publicKeyEncrypt(pubKey, text) {
        let ret = sm2.doEncrypt(text, pubKey, this.cipherMode)
        ret = Base64Util.encrypt(ret)
        return ret
    },
    publicKeyDecrypt(pubKey, text) {
        throw SecureException.newObj(SecureErrorCode.SECURE_NOT_SUPPORT(), "Sm2算法不支持公钥解密")
    },
    privateKeyEncrypt(priKey, text) {
        throw SecureException.newObj(SecureErrorCode.SECURE_NOT_SUPPORT(), "Sm2算法不支持私钥加密")
    },
    privateKeyDecrypt(priKey, text) {
        text = Base64Util.decrypt(text)
        return sm2.doDecrypt(text, priKey, this.cipherMode)
    },
    makeSign(priKey, text) {
        return sm2.doSignature(text, priKey)
    },
    verifySign(pubKey, sign, text) {
        return sm2.doVerifySignature(text, sign, pubKey)
    }
}

export default Sm2Util
