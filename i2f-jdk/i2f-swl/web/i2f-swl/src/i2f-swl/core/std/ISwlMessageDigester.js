/**
 * @return {ISwlMessageDigester}
 * @interface ISwlMessageDigester
 */
function ISwlMessageDigester() {
    return {
        /**
         * @param data {String}
         * @return {String}
         */
        digest(data) {

        },
        /**
         *
         * @param digest {String}
         * @param data {String}
         * @return {boolean}
         */
        verify(digest, data) {

        }
    }
}

export default ISwlMessageDigester
