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
 * @return AntMatcher
 */
function AntMatcher(sep="/"){
    this.sep=sep
}

// 继承
AntMatcher.prototype = Object.create(IMatcher.prototype)
AntMatcher.prototype.constructor = AntMatcher

/**
 * 支持ant-match方式，可以自行指定分隔符
 * 例如.分隔的包名匹配方式，/分隔的路径匹配方式
 *
 * @param str {String}
 * @param patten {String}
 * @return {double}
 */
AntMatcher.prototype.match=function(str, patten) {
    let pi = 0;
    let plen = patten.length;
    let si = 0;
    let slen = str.length;
    let mlen = 0;
    while (pi < plen) {
        if (si >= slen) {
            return IMatcher.MATCH_FAILURE_VALUE();
        }
        let pch = patten.charAt(pi);
        if (pch == '\\') {
            if ((pi + 1) < plen) {
                let npch = patten.charAt(pi + 1);
                if (npch == '*' || npch == '?') {
                    if (npch != str.charAt(si)) {
                        return AntMatcher.MATCH_FAILURE_VALUE();
                    } else {
                        si++;
                        pi += 2;
                    }
                } else {
                    if (npch != str.charAt(si)) {
                        return IMatcher.MATCH_FAILURE_VALUE();
                    } else {
                        si++;
                        pi++;
                    }
                }
            } else {
                return this.calcMatchRate(si, pi, slen, plen, mlen);
            }
        } else if (pch == '*') {
            if ((pi + 1) < plen) {
                let npch = patten.charAt(pi + 1);
                if (npch == '*') {
                    // 多分段匹配
                    let jumpPatten = "";
                    let nextPatten = "";
                    let j = 0;
                    while ((pi + 2 + j) < plen && (patten.charAt(pi + 2 + j) == '*' || patten.substring(pi + 2 + j).startsWith(this.sep))) {
                        jumpPatten += patten.charAt(pi + 2 + j);
                        j++;
                    }
                    let k = 0;
                    while ((pi + 2 + j + k) < plen && patten.charAt(pi + 2 + j + k) != '*' && !patten.substring(pi + 2 + j + k).startsWith(this.sep)) {
                        nextPatten += patten.charAt(pi + 2 + j + k);
                        k++;
                    }


                    let m = 0;
                    while ((m + si) < slen) {
                        let nextStr = str.substring(m + si);
                        if (nextStr.startsWith(nextPatten)) {
                            m += nextPatten.length;
                            mlen += nextPatten.length;
                            break;
                        }
                        m++;
                    }
                    pi += j + 2 + k;
                    si += m;
                } else {
                    // 单分段匹配
                    let nextPatten = "";
                    let j = 0;
                    while ((pi + 1 + j) < plen && patten.charAt(pi + 1 + j) != '*') {
                        nextPatten += patten.charAt(pi + 1 + j);
                        j++;
                    }
                    let m = 0;
                    while ((m + si) < slen) {
                        let nextStr = str.substring(m + si);
                        if (nextStr.startsWith(nextPatten)) {
                            m += nextPatten.length;
                            mlen += nextPatten.length;
                            break;
                        }
                        if (nextStr.startsWith(this.sep)) {
                            break;
                        }
                        m++;
                    }
                    pi += j + 1;
                    si += m;
                }
            } else {
                let lstr = str.substring(si);
                if (lstr.indexOf(this.sep)>=0) {
                    return IMatcher.MATCH_FAILURE_VALUE();
                } else {
                    return this.calcMatchRate(si, pi, slen, plen, mlen);
                }
            }
        } else if (pch == '?') {
            pi++;
            si++;
        } else {
            if (str.charAt(si) == pch) {
                pi++;
                si++;
                mlen++;
            } else {
                return IMatcher.MATCH_FAILURE_VALUE();
            }
        }
    }

    return this.calcMatchRate(si, pi, slen, plen, mlen);
}

export default AntMatcher