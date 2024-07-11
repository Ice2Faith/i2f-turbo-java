/**
 * @constructor
 * @param publicKey {String}
 * @param privateKey {String}
 * @return {AsymKeyPair}
 */
function AsymKeyPair(publicKey = null, privateKey = null) {
    return {
        /**
         * @type String
         */
        _publicKey: publicKey,
        /**
         * @type String
         */
        _privateKey: privateKey,
        /**
         * @return String
         */
        getPublicKey() {
            return this._publicKey
        },
        /**
         * @param publicKey String
         * @return void
         */
        setPublicKey(publicKey) {
            this._publicKey = publicKey
        },
        /**
         * @return String
         */
        getPrivateKey() {
            return this._privateKey
        },
        /**
         * @param privateKey String
         * @return void
         */
        setPrivateKey(privateKey) {
            this._privateKey = privateKey
        }
    }
}

export default AsymKeyPair
