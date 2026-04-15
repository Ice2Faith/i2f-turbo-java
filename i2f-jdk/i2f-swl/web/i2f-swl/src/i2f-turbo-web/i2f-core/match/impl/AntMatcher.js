import IPriorMatcher from "../IPriorMatcher";

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
 * @extends {IPriorMatcher}
 * @return AntMatcher
 */
function AntMatcher(sep = "/") {
    IPriorMatcher.call(this);

    this.sep = sep
}


// 继承
AntMatcher.prototype = Object.create(IPriorMatcher.prototype)
AntMatcher.prototype.constructor = AntMatcher

AntMatcher.PATH = function () {
    return new AntMatcher('/');
}
AntMatcher.PKG = function () {
    return new AntMatcher('.');
}
AntMatcher.PERM = function () {
    return new AntMatcher(':');
}

/**
 * 支持ant-match方式，可以自行指定分隔符
 * 例如.分隔的包名匹配方式，/分隔的路径匹配方式
 *
 * @param str
 * @param pattern
 * @return {doubole}
 */
AntMatcher.prototype.matchRate = function (str, pattern) {
    if (str == null || pattern == null) {
        return IPriorMatcher.MATCH_FAILURE_VALUE();
    }
    if (str == "" && pattern == "") {
        return 1.0;
    }
    if (str == "" || pattern == "") {
        return IPriorMatcher.MATCH_FAILURE_VALUE();
    }
    let pi = 0;
    let plen = pattern.length;
    let si = 0;
    let slen = str.length;
    let mlen = 0;
    while (pi < plen) {
        let cpattern = pattern.substring(pi);
        let cstr = str.substring(si);
        if (si >= slen) {
            let leftPattern = pattern.substring(pi);
            if (leftPattern == ""
                || "*" == leftPattern
                || "**" == leftPattern
                || leftPattern == (this.sep + "*")
                || leftPattern == (this.sep + "**")) {
                pi += leftPattern.length;
                break;
            }
            return IPriorMatcher.MATCH_FAILURE_VALUE();
        }
        let pch = pattern.charAt(pi);
        if (pch == '\\') {
            if ((pi + 1) < plen) {
                let npch = pattern.charAt(pi + 1);
                if (npch == '*' || npch == '?') {
                    if (npch != str.charAt(si)) {
                        return IPriorMatcher.MATCH_FAILURE_VALUE();
                    } else {
                        si++;
                        pi += 2;
                    }
                } else {
                    if (npch != str.charAt(si)) {
                        return IPriorMatcher.MATCH_FAILURE_VALUE();
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
                let npch = pattern.charAt(pi + 1);
                if (npch == '*') {
                    // 多分段匹配
                    let jumpPatten = "";
                    let nextPatten = "";
                    let j = 0;
                    while ((pi + 2 + j) < plen && (pattern.charAt(pi + 2 + j) == '*' || pattern.substring(pi + 2 + j).startsWith(this.sep))) {
                        jumpPatten += pattern.charAt(pi + 2 + j);
                        j++;
                    }
                    let k = 0;
                    while ((pi + 2 + j + k) < plen && pattern.charAt(pi + 2 + j + k) != '*' && !pattern.substring(pi + 2 + j + k).startsWith(this.sep)) {
                        nextPatten += pattern.charAt(pi + 2 + j + k);
                        k++;
                    }

                    let l = 0;
                    while (l < k) {
                        if (nextPatten.charAt(l) != '?') {
                            break;
                        }
                        l++;
                    }
                    k -= l;
                    nextPatten = nextPatten.substring(l);

                    l = nextPatten.indexOf("?");
                    if (l >= 0) {
                        nextPatten = nextPatten.substring(0, l);
                        k = nextPatten.length;
                    }

                    if (nextPatten == "") {
                        si = slen;
                        break;
                    }


                    let m = 0;
                    while ((m + si) < slen) {
                        let nextStr = str.substring(m + si);
                        if (m == 0) {
                            if (nextStr.indexOf(nextPatten) < 0) {
                                return IPriorMatcher.MATCH_FAILURE_VALUE();
                            }
                        }
                        if (nextStr.startsWith(nextPatten)) {
                            m += nextPatten.length;
                            mlen += nextPatten.length;
                            break;
                        }
                        m++;
                    }
                    pi = Math.max(pi, pattern.lastIndexOf(this.sep, pi + 2 + j));
                    if (pattern.substring(pi).startsWith(this.sep)) {
                        pi += this.sep.length;
                    }
                    if (str.lastIndexOf(this.sep, si + m) < 0) {
                        return IPriorMatcher.MATCH_FAILURE_VALUE();
                    }
                    if (str.substring(si + m).startsWith(this.sep)) {
                        m -= this.sep.length;
                    }
                    si = Math.max(si, str.lastIndexOf(this.sep, si + m));
                    if (str.substring(si).startsWith(this.sep)) {
                        si += this.sep.length;
                    }

                    // 以多分段结尾，完全匹配剩余部分
                    if (nextPatten == "") {
                        si = slen;
                    }
                } else {
                    // 单分段匹配
                    let nextPatten = "";
                    let j = 0;
                    while ((pi + 1 + j) < plen && pattern.charAt(pi + 1 + j) != '*') {
                        nextPatten += pattern.charAt(pi + 1 + j);
                        j++;
                    }

                    let l = nextPatten.indexOf("?");
                    if (l >= 0) {
                        nextPatten = nextPatten.substring(0, l);
                        j = nextPatten.length;
                    }

                    if (nextPatten == "") {
                        si = slen;
                        break;
                    }

                    let m = 0;
                    while ((m + si) < slen) {
                        let nextStr = str.substring(m + si);
                        if (m == 0) {
                            if (nextStr.indexOf(nextPatten) < 0) {
                                return IPriorMatcher.MATCH_FAILURE_VALUE();
                            }
                        }
                        if (nextStr.startsWith(nextPatten)) {
                            m += nextPatten.length;
                            mlen += nextPatten.length;
                            break;
                        }
                        if (nextStr.startsWith(this.sep)) {
                            return IPriorMatcher.MATCH_FAILURE_VALUE();
                        }
                        m++;
                    }
                    pi += j + 1;
                    si += m;
                }
            } else {
                let lstr = str.substring(si);
                if (lstr.indexOf(this.sep) >= 0) {
                    return IPriorMatcher.MATCH_FAILURE_VALUE();
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
                return IPriorMatcher.MATCH_FAILURE_VALUE();
            }
        }
    }

    if (si < slen) {
        return IPriorMatcher.MATCH_FAILURE_VALUE();
    }

    return this.calcMatchRate(si, pi, slen, plen, mlen);

}


AntMatcher.testMain = function () {
    let antMatcher = new AntMatcher();
    let cases = [
        // --- 1. 精确匹配 ---
        ["精确匹配-相同", "/api/users", "/api/users", true],
        ["精确匹配-根路径", "/", "/", true],
        ["精确匹配-不同", "/api/users", "/api/admin", false],
        ["精确匹配-大小写", "/Api/Users", "/api/users", false],
        ["精确匹配-尾部斜杠", "/api/users", "/api/users/", false], // 视具体实现而定，通常为 false

        // --- 2. 单星号 * 测试 ---
        ["星号-匹配单层文件", "/users.json", "/*.json", true],
        ["星号-匹配单层目录", "/admin/settings", "/admin/*", true],
        ["星号-匹配中间层级", "/v1/data", "/v1/*/data", false],
        ["星号-跨层级失败(关键)", "/api/v1/users", "/api/*/users", true], // * 不能包含 /
        ["星号-深层跨层失败", "/users/123/details", "/users/*", false],
        ["星号-空匹配失败", "/api/", "/api/*", true], // * 通常至少匹配一个字符
        ["星号-仅星号", "users", "*", true],

        // --- 3. 双星号 ** 测试 ---
        ["双星号-匹配深层路径", "/api/v1/users/123", "/api/**", true],
        ["双星号-匹配中间层级", "/api/v1/users", "/api/**/users", true],
        ["双星号-匹配多层级", "/a/b/c/d/e", "/a/**/e", true],
        ["双星号-匹配空层级", "/api/users", "/api/**/users", true], // ** 可以匹配空
        ["双星号-深层文件匹配", "/src/main/java/com/App.java", "/**/*.java", true],
        ["双星号-前缀不匹配", "/public/data", "/admin/**", false],
        ["双星号-结尾双星号", "/api/users", "/api/users/**", true],

        // --- 4. 问号 ? 测试 ---
        ["问号-匹配单个字符", "/a", "/?", true],
        ["问号-匹配数字", "/1", "/?", true],
        ["问号-多字符失败(关键)", "/ab", "/?", false],
        ["问号-空字符失败", "/", "/?", false],
        ["问号-组合使用", "/user/1", "/user/?", true],
        ["问号-组合失败", "/user/12", "/user/?", false],
        ["问号-多个问号", "/ab", "/??", true],
        ["问号-多个问号失败", "/abc", "/??", false],

        // --- 5. 组合与复杂场景 ---
        ["组合-*和?", "/img_01.jpg", "/img_*.jpg", true],
        ["组合-*和?失败", "/img_12.jpg", "/img_?.jpg", false],
        ["组合-**和*", "/api/v1/users.json", "/api/**/*", true],
        ["组合-复杂深层", "/org/project/module/class", "/org/**/module/*", true],
        ["组合-路径参数", "/users/123/profile", "/users/*/profile", true],

        // --- 6. 特殊字符与边界 ---
        ["特殊-点号", "/style.css", "/*.css", true],
        ["特殊-连字符", "/my-page", "/my-*", true],
        ["特殊-下划线", "/user_data", "/user_*", true],
        ["边界-空字符串", "", "", true],
        ["边界-仅双星号", "/anything/goes/here", "**", true],
        ["边界-仅双星号", "/anything", "/*", true]

    ];
//        double ok = antMatcher.matchRate("/name/user/exp2ort", "/**/*exp?ort*");
//        System.out.println("ok");

    for (let arr of cases) {
        console.log("============================");
        console.log(JSON.stringify(arr));
        let rate = antMatcher.matchRate(arr[1], arr[2]);
        console.log(rate);
        console.log(antMatcher.matched(rate));
        console.log(antMatcher.matched(rate) == arr[3] ? "pass" : "fail");
    }
}

export default AntMatcher