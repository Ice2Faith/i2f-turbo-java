/**
 * @return IMatcher
 * @abstract
 */
function IMatcher() {

}

IMatcher.MATCH_FAILURE_VALUE = function () {
    return -1.0
}
IMatcher.MATCH_SUCCESS_LIMIT = function () {
    return -0.5
}

/**
 * 通配符匹配
 * 返回值含义，为负数，表示不匹配
 * 为正数，表示（末尾匹配程度+总体匹配重叠度）的均值
 * 此时可以用于表示哪一个patten更能够更加精准来匹配字符串
 * 这个返回值在某些情况下很有用，比如多个匹配规则同时适用一个目标时，优选某个规则的情况
 *
 * @param str {String}
 * @param patten {String}
 * @return {double}
 */
IMatcher.prototype.match=function(str, patten){

}
/**
 *
 * @param str {String}
 * @param patten {String}
 * @return {boolean}
 */
IMatcher.prototype.isMatch=function(str, patten) {
    return this.matched(this.match(str,patten));
}
/**
 * @param rate {double}
 * @return {boolean}
 */
IMatcher.prototype.matched=function(rate) {
    return rate > IMatcher.MATCH_SUCCESS_LIMIT();
}

/**
 * @param matchEndStrIndex {int}
 * @param matchEndPattenIndex {int}
 * @param strLen {int}
 * @param pattenLen {int}
 * @param matchEndMatchedCount {int}
 * @return {double}
 */
IMatcher.prototype.calcMatchRate=function(matchEndStrIndex, matchEndPattenIndex, strLen, pattenLen, matchEndMatchedCount) {
    return ((((matchEndStrIndex + matchEndPattenIndex) * 1.0 / (strLen + pattenLen)) * 0.5) + ((matchEndMatchedCount * 1.0 / strLen) * 0.5));
}

export default IMatcher