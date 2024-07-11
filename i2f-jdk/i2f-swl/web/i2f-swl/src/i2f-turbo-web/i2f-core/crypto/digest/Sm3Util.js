import {sm3} from 'sm-crypto'

const Sm3Util = {
    /**
     *
     * @param text {String}
     * @return {String} hex
     */
    sign(text) {
        return sm3(text)
    }
}

export default Sm3Util
