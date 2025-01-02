package i2f.streaming.type.str;

import i2f.streaming.Streaming;

/**
 * @author Ice2Faith
 * @date 2024/12/31 19:12
 * @desc
 */
public interface StringStreaming extends Streaming<String> {

    default StringStreaming notEmpty() {
        return filter(e -> e != null && !e.isEmpty())
                .string();
    }

    default StringStreaming notBlank() {
        return filter(e -> e != null && !e.trim().isEmpty())
                .string();
    }

    default StringStreaming startsWith(String prefix) {
        return filter(e -> e != null && e.startsWith(prefix))
                .string();
    }

    default StringStreaming endsWith(String suffix) {
        return filter(e -> e != null && e.endsWith(suffix))
                .string();
    }

    default StringStreaming toUpperCase() {
        return map(e -> {
            if (e == null) {
                return null;
            }
            return e.toUpperCase();
        }).string();
    }

    default StringStreaming toLowerCase() {
        return map(e -> {
            if (e == null) {
                return null;
            }
            return e.toLowerCase();
        }).string();
    }

    default StringStreaming trim() {
        return map(e -> {
            if (e == null) {
                return null;
            }
            return e.trim();
        }).string();
    }

    default StringStreaming split(String regex) {
        return flatMap((e, collector) -> {
            if (e == null) {
                return;
            }
            String[] arr = e.split(regex);
            for (String item : arr) {
                collector.accept(item);
            }
        }).string();
    }

    default StringStreaming split(String regex, int limit) {
        return flatMap((e, collector) -> {
            if (e == null) {
                return;
            }
            String[] arr = e.split(regex, limit);
            for (String item : arr) {
                collector.accept(item);
            }
        }).string();
    }

    default StringStreaming replaceAll(String regex, String replacement) {
        return map(e -> {
            if (e == null) {
                return null;
            }
            return e.replaceAll(regex, replacement);
        }).string();
    }

}
