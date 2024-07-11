/**
 * @return {ISwlAsymmetricEncryptor}
 * @interface ISwlAsymmetricEncryptor
 */
function ISwlAsymmetricEncryptor() {
    return {
        /**
         * @return {AsymKeyPair}
         */
        generateKeyPair() {

        },
        /**
         * @return {AsymKeyPair}
         */
        getKeyPair() {

        },
        /**
         * @param key {AsymKeyPair}
         * @return {void}
         */
        setKeyPair(key) {

        },
        /**
         * @return {String}
         */
        getPublicKey() {

        },
        /**
         * @param publicKey {String}
         * @return {void}
         */
        setPublicKey(publicKey) {

        },
        /**
         * @return {String}
         */
        getPrivateKey() {

        },
        /**
         *
         * @param privateKey {String}
         * @return {void}
         */
        setPrivateKey(privateKey) {

        },
        /**
         *
         * @param data {String}
         * @return {String}
         */
        encrypt(data) {

        },
        /**
         *
         * @param data {String}
         * @return {String}
         */
        decrypt(data) {

        },
        /**
         *
         * @param data {String}
         * @return {String}
         */
        sign(data) {

        },
        /**
         *
         * @param sign {String}
         * @param data {String}
         * @return {boolean}
         */
        verify(sign, data) {

        }
    }
}

export default ISwlAsymmetricEncryptor
