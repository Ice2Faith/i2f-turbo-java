/**
 * 对象工具
 * @const
 */
const ObjectUtils = {
    /**
     *
     * @param obj {Object}
     * @return {Object}
     */
    deepClone(obj) {
        try {
            return JSON.parse(JSON.stringify(obj))
        } catch (e) {

        }
        return obj
    }
}

export default ObjectUtils
