import AntMatcher from "./impl/AntMatcher";

const MatcherUtil = {
    /**
     * @type {IMatcher}
     */
    antUrlMatcher: new AntMatcher("/"),
    /**
     * @type {IMatcher}
     */
    antPkgMatcher: new AntMatcher("."),
    /**
     *
     * @param str {String}
     * @param patten {String}
     * @return {boolean}
     */
    antUrlMatched(str, patten) {
        return this.antUrlMatcher.isMatch(str, patten);
    },
    /**
     *
     * @param str {String}
     * @param patten {String}
     * @return {boolean}
     */
    antPkgMatched(str, patten) {
        return this.antPkgMatcher.isMatch(str, patten);
    },
    /**
     *
     * @param str {String}
     * @param pattens {string[]}
     * @return {boolean}
     */
    antUrlMatchedAny(str, pattens) {
        for (let i = 0; i < pattens.length; i++) {
            let patten = pattens[i]
            if (!patten) {
                continue;
            }
            if (this.antUrlMatched(str, patten)) {
                return true;
            }
        }
        return false;
    },
    /**
     *
     * @param str {String}
     * @param pattens {string[]}
     * @return {boolean}
     */
    antPkgMatchedAny(str, pattens) {
        for (let i = 0; i < pattens.length; i++) {
            let patten = pattens[i]
            if (!patten) {
                continue;
            }
            if (antPkgMatched(str, patten)) {
                return true;
            }
        }
        return false;
    }
}

export default MatcherUtil