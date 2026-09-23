package i2f.match.impl;


import i2f.match.std.IPriorMatcher;

import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2022/4/25 17:15
 * @desc 通配符ant模式匹配类，处理*，?，**的通配符
 * *匹配0-多个符号
 * ?匹配一个符号
 * **匹配多级，每一集需要指定的分隔符
 * 允许patten中对*和?进行转义
 * 转义规则：
 * * --> \*
 * ? --> \?
 * 当\之后不是关键的*和?时，\的含义保持，不需要转义
 * 因此\\就是\\，而不是\
 */
public class AntMatcher implements IPriorMatcher {
    public static final AntMatcher PATH = new AntMatcher("/");
    public static final AntMatcher PKG = new AntMatcher(".");

    protected String sep;

    public AntMatcher() {
        sep = "/";
    }

    public AntMatcher(String sep) {
        this.sep = sep;
    }

    /**
     * 支持ant-match方式，可以自行指定分隔符
     * 例如.分隔的包名匹配方式，/分隔的路径匹配方式
     *
     * @param str
     * @param pattern
     * @return
     */
    @Override
    public double matchRate(String str, String pattern) {
        if (str == null || pattern == null) {
            return MATCH_FAILURE_VALUE;
        }
        if (str.isEmpty() && pattern.isEmpty()) {
            return 1.0;
        }
        if (str.isEmpty() || pattern.isEmpty()) {
            return MATCH_FAILURE_VALUE;
        }
        Pattern sepPattern = Pattern.compile(sep, Pattern.LITERAL);
        int pi = 0;
        int plen = pattern.length();
        int si = 0;
        int slen = str.length();
        int mlen = 0;
        while (pi < plen) {
            String cpattern = pattern.substring(pi);
            String cstr = str.substring(si);
            if (si >= slen) {
                String leftPattern = pattern.substring(pi);
                if ("*".equals(leftPattern)
                        || "**".equals(leftPattern)
                        || (sep + "*").equals(leftPattern)
                        || (sep + "**").equals(leftPattern)
                ) {
                    pi += leftPattern.length();
                    break;
                }
                return MATCH_FAILURE_VALUE;
            }
            char pch = pattern.charAt(pi);
            if (pch == '\\') {
                if ((pi + 1) < plen) {
                    char npch = pattern.charAt(pi + 1);
                    if (npch == '*' || npch == '?') {
                        if (npch != str.charAt(si)) {
                            return MATCH_FAILURE_VALUE;
                        } else {
                            si++;
                            pi += 2;
                        }
                    } else {
                        if (npch != str.charAt(si)) {
                            return MATCH_FAILURE_VALUE;
                        } else {
                            si++;
                            pi++;
                        }
                    }
                } else {
                    return calcMatchRate(si, pi, slen, plen, mlen);
                }
            } else if (pch == '*') {
                if ((pi + 1) < plen) {
                    char npch = pattern.charAt(pi + 1);
                    if (npch == '*') {
                        // 多分段匹配
                        String jumpPatten = "";
                        String nextPatten = "";
                        int j = 0;
                        while ((pi + 2 + j) < plen && (pattern.charAt(pi + 2 + j) == '*' || pattern.substring(pi + 2 + j).startsWith(sep))) {
                            jumpPatten += pattern.charAt(pi + 2 + j);
                            j++;
                        }
                        int k = 0;
                        while ((pi + 2 + j + k) < plen && pattern.charAt(pi + 2 + j + k) != '*' && !pattern.substring(pi + 2 + j + k).startsWith(sep)) {
                            nextPatten += pattern.charAt(pi + 2 + j + k);
                            k++;
                        }

                        int l = 0;
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
                            k = nextPatten.length();
                        }

                        if (nextPatten.isEmpty()) {
                            si = slen;
                            break;
                        }

                        String nextTestStr = str.substring(si);
                        String nextTestPattern = pattern.substring(pi + 2);

                        // ** 多级的时候，需要尝试贪婪匹配，任意一个匹配即满足
                        int tryCount = 100;
                        do {
                            tryCount--;

                            double nextRate = matchRate(nextTestStr, nextTestPattern);
                            if (matched(nextRate)) {
                                double currRate = calcMatchRate(si, pi, slen, plen, mlen);
                                double currPer = si * 1.0 / str.length();
                                return currRate * currRate + nextRate * (1.0 - currPer);
                            }

                            if (nextTestStr.startsWith(sep)) {
                                nextTestStr = nextTestStr.substring(sep.length());
                            }
                            String[] arr = sepPattern.split(nextTestStr, 2);
                            if (arr.length != 2) {
                                break;
                            }
                            String tmpTestStr = arr[1];
                            if (tmpTestStr.isEmpty()) {
                                break;
                            }
                            if (!tmpTestStr.startsWith(sep)) {
                                tmpTestStr = sep + tmpTestStr;
                            }
                            if (tmpTestStr.equals(nextTestStr)) {
                                break;
                            }
                            nextTestStr = tmpTestStr;
                        } while (tryCount > 0);


                        int m = 0;
                        while ((m + si) < slen) {
                            String nextStr = str.substring(m + si);
                            if (m == 0) {
                                if (!nextStr.contains(nextPatten)) {
                                    return MATCH_FAILURE_VALUE;
                                }
                            }
                            if (nextStr.startsWith(nextPatten)) {
                                m += nextPatten.length();
                                mlen += nextPatten.length();
                                break;
                            }
                            m++;
                        }
                        pi = Math.max(pi, pattern.lastIndexOf(sep, pi + 2 + j));
                        if (pattern.substring(pi).startsWith(sep)) {
                            pi += sep.length();
                        }
                        if (str.lastIndexOf(sep, si + m) < 0) {
                            return MATCH_FAILURE_VALUE;
                        }
                        if (str.substring(si + m).startsWith(sep)) {
                            m -= sep.length();
                        }
                        si = Math.max(si, str.lastIndexOf(sep, si + m));
                        if (str.substring(si).startsWith(sep)) {
                            si += sep.length();
                        }

                        // 以多分段结尾，完全匹配剩余部分
                        if (nextPatten.isEmpty()) {
                            si = slen;
                        }
                    } else {
                        // 单分段匹配
                        String nextPatten = "";
                        int j = 0;
                        while ((pi + 1 + j) < plen && pattern.charAt(pi + 1 + j) != '*') {
                            nextPatten += pattern.charAt(pi + 1 + j);
                            j++;
                        }

                        int l = nextPatten.indexOf("?");
                        if (l >= 0) {
                            nextPatten = nextPatten.substring(0, l);
                            j = nextPatten.length();
                        }

                        if (nextPatten.isEmpty()) {
                            si = slen;
                            break;
                        }

                        int m = 0;
                        while ((m + si) < slen) {
                            String nextStr = str.substring(m + si);
                            if (m == 0) {
                                if (!nextStr.contains(nextPatten)) {
                                    return MATCH_FAILURE_VALUE;
                                }
                            }
                            if (nextStr.startsWith(nextPatten)) {
                                m += nextPatten.length();
                                mlen += nextPatten.length();
                                break;
                            }
                            if (nextStr.startsWith(sep)) {
                                return MATCH_FAILURE_VALUE;
                            }
                            m++;
                        }
                        pi += j + 1;
                        si += m;
                    }
                } else {
                    String lstr = str.substring(si);
                    if (lstr.contains(sep)) {
                        return MATCH_FAILURE_VALUE;
                    } else {
                        return calcMatchRate(si, pi, slen, plen, mlen);
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
                    return MATCH_FAILURE_VALUE;
                }
            }
        }

        if (si < slen) {
            return MATCH_FAILURE_VALUE;
        }

        return calcMatchRate(si, pi, slen, plen, mlen);
    }
}
