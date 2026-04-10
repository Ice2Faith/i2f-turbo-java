import IMatcher from "./IMatcher";

/**
 * @return IPriorMatcher
 * @implements {IMatcher}
 * @abstract
 */
function IPriorMatcher() {
    IMatcher.call(this)
}

// 继承
IPriorMatcher.prototype = Object.create(IMatcher.prototype)
IPriorMatcher.prototype.constructor = IPriorMatcher

IPriorMatcher.MATCH_FAILURE_VALUE=function(){
    return -1.0;
}

IPriorMatcher.MATCH_SUCCESS_LIMIT=function(){
    return -0.5;
}

/**
 *
 * @param str {string}
 * @param patten {string}
 * @return {double}
 */
IPriorMatcher.prototype.matchRate=function(str, patten){

}

/**
 *
 * @param str {string}
 * @param pattern {string}
 * @return {boolean}
 */
IPriorMatcher.prototype.matches=function(str,pattern){
    return this.matched(this.matchRate(str, pattern));
}

/**
 *
 * @param rate {double}
 * @return {boolean}
 */
IPriorMatcher.prototype.matched=function(rate) {
    return rate > IPriorMatcher.MATCH_FAILURE_VALUE();
}

/**
 *
 * @param matchEndStrIndex {int}
 * @param matchEndPattenIndex {int}
 * @param strLen {int}
 * @param pattenLen {int}
 * @param matchEndMatchedCount {int}
 * @return {double}
 */
IPriorMatcher.prototype.calcMatchRate=function(matchEndStrIndex, matchEndPattenIndex, strLen, pattenLen, matchEndMatchedCount) {
    return (matchEndStrIndex + matchEndPattenIndex) * 1.0 / (strLen + pattenLen) * 0.5 + matchEndMatchedCount * 1.0 / strLen * 0.5;
}

export default IPriorMatcher