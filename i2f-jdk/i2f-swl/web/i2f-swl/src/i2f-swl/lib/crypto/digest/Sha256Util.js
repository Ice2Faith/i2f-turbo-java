import CryptoJS from 'crypto-js'

const Sha256Util = {
    /**
     *
     * @param text {String}
     * @return {String} hex
     */
    sign(text) {
        return CryptoJS.SHA256(text).toString(CryptoJS.enc.Hex).toUpperCase()
    }
}

export default Sha256Util
