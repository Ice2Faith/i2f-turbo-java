package i2f.mixin.impl;

import i2f.match.impl.AntMatcher;
import i2f.match.impl.SimpleMatcher;

/**
 * @author Ice2Faith
 * @date 2026/5/14 19:56
 * @desc
 */
public interface MatchMixins {
    default boolean ant_match_path(String text, String pattern) {
        return AntMatcher.PATH.matches(text, pattern);
    }

    default boolean ant_match_pkg(String text, String pattern) {
        return AntMatcher.PKG.matches(text, pattern);
    }

    default boolean simple_match(String text, String pattern) {
        return SimpleMatcher.INSTANCE.matches(text, pattern);
    }
}
