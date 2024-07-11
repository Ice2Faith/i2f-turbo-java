import IMatcher from "../IMatcher";

/**
 * 通配符ant模式匹配类，处理*，?，**的通配符
 *  *匹配0-多个符号
 *  ?匹配一个符号
 *  **匹配多级，每一集需要指定的分隔符
 *  允许patten中对*和?进行转义
 *  转义规则：
 *  * --> \*
 *  ? --> \?
 *  当\之后不是关键的*和?时，\的含义保持，不需要转义
 *  因此\\就是\\，而不是\
 * @param sep {string}
 * @constructor
 * @implements {IMatcher}
 * @return SimpleMatcher
 */
function SimpleMatcher(){

}

// 继承
SimpleMatcher.prototype = Object.create(IMatcher.prototype)
SimpleMatcher.prototype.constructor = SimpleMatcher

/**
 * 通配符匹配
 * 返回值含义，为负数，表示不匹配
 * 为正数，表示（末尾匹配程度+总体匹配重叠度）的均值
 * 此时可以用于表示哪一个patten更能够更加精准来匹配字符串
 * 这个返回值在某些情况下很有用，比如多个匹配规则同时适用一个目标时，优选某个规则的情况
 *
 * @param str {String}
 * @param patten {String}
 * @return
 */
SimpleMatcher.prototype.match=function( str, patten) {
    let sidx = 0;
    let pidx = 0;
    let plen = patten.length;
    let slen = str.length;
    let mlen = 0;
    while (pidx < plen && sidx < slen) {
        let pch = patten.charAt(pidx);
        if (pch == '\\') {
            if ((pidx + 1) < plen) {
                let npch = patten.charAt(pidx + 1);
                if (npch == '*' || npch == '?') {
                    if (npch != str.charAt(sidx)) {
                        return IMatcher.MATCH_FAILURE_VALUE();
                    } else {
                        sidx++;
                        pidx += 2;
                    }
                } else {
                    if (npch != str.charAt(sidx)) {
                        return IMatcher.MATCH_FAILURE_VALUE();
                    } else {
                        sidx++;
                        pidx++;
                    }
                }
            } else {
                return this.calcMatchRate(sidx, pidx, slen, plen, mlen);
            }
        } else if (pch == '*') {
            if ((pidx + 1) < plen) {
                let edx = pidx + 1;
                while (edx < plen && patten.charAt(edx) != '*' && patten.charAt(edx) != '?') {
                    edx++;
                }
                let wstr = patten.substring(pidx + 1, edx);
                let swfch = wstr.charAt(0);
                while (sidx < slen) {
                    let sch = str.charAt(sidx);
                    if (sch == swfch) {
                        let nxtstr = str.substring(sidx);
                        if (nxtstr.startsWith(wstr)) {
                            break;
                        }
                    }
                    sidx++;
                    if (sidx == slen) {
                        return IMatcher.MATCH_FAILURE_VALUE();
                    }
                }
                pidx++;
            } else {
                return this.calcMatchRate(sidx, pidx, slen, plen, mlen);
            }
        } else if (pch == '?') {
            pidx++;
            sidx++;
        } else {
            if (pch != str.charAt(sidx)) {
                return IMatcher.MATCH_FAILURE_VALUE();
            }
            pidx++;
            sidx++;
            mlen++;
        }
    }
    return this.calcMatchRate(sidx, pidx, slen, plen, mlen);
}

export default SimpleMatcher