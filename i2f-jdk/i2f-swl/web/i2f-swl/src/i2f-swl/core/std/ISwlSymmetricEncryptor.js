/**
 * @return {ISwlSymmetricEncryptor}
 * @interface ISwlSymmetricEncryptor
 */
function ISwlSymmetricEncryptor() {
    return {
        /**
         * @return {String}
         */
        generateKey() {

        },
        /**
         * @return {String}
         */
        getKey() {

        },
        /**
         *
         * @param key {String}
         * @return {void}
         */
        setKey(key) {

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

        }
    }
}

export default ISwlSymmetricEncryptor
