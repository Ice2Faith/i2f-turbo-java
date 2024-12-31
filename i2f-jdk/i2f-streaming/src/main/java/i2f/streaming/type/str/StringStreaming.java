package i2f.streaming.type.str;

import i2f.streaming.Streaming;

/**
 * @author Ice2Faith
 * @date 2024/12/31 19:12
 * @desc
 */
public interface StringStreaming extends Streaming<String> {

    default Streaming<String> notEmpty() {
        return filter(e -> e != null && !e.isEmpty());
    }

    default Streaming<String> notBlank() {
        return filter(e -> e != null && !e.trim().isEmpty());
    }

    default Streaming<String> startsWith(String prefix) {
        return filter(e -> e != null && e.startsWith(prefix));
    }

    default Streaming<String> endsWith(String suffix) {
        return filter(e -> e != null && e.endsWith(suffix));
    }

    default Streaming<String> toUpperCase() {
        return map(e -> {
            if (e == null) {
                return null;
            }
            return e.toUpperCase();
        });
    }

    default Streaming<String> toLowerCase() {
        return map(e -> {
            if (e == null) {
                return null;
            }
            return e.toLowerCase();
        });
    }

    default Streaming<String> trim() {
        return map(e -> {
            if (e == null) {
                return null;
            }
            return e.trim();
        });
    }

    default Streaming<String> split(String regex) {
        return flatMap((e, collector) -> {
            if (e == null) {
                return;
            }
            String[] arr = e.split(regex);
            for (String item : arr) {
                collector.accept(item);
            }
        });
    }

    default Streaming<String> split(String regex, int limit) {
        return flatMap((e, collector) -> {
            if (e == null) {
                return;
            }
            String[] arr = e.split(regex, limit);
            for (String item : arr) {
                collector.accept(item);
            }
        });
    }

    default Streaming<String> replaceAll(String regex, String replacement) {
        return map(e -> {
            if (e == null) {
                return null;
            }
            return e.replaceAll(regex, replacement);
        });
    }

}
