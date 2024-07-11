/**
 * 字符串工具
 * @const
 */
const StringUtils = {
    /**
     *
     * @param str {String|null|undefined}
     * @return {boolean}
     */
    isEmpty(str) {
        return str == null || str == undefined || str == ''
    }
}

export default StringUtils
