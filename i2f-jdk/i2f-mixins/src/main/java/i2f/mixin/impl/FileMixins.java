package i2f.mixin.impl;


import i2f.io.stream.StreamUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/5/14 9:04
 * @desc
 */
public interface FileMixins {
    default long length(File file) {
        return file.length();
    }

    default File file(String path) {
        return new File(path);
    }

    default boolean is_file(File file) {
        if (file == null) {
            return false;
        }
        return file.isFile();
    }

    default boolean file_exists(String path) {
        return file_exists(file(path));
    }

    default boolean file_exists(File file) {
        if (file == null) {
            return false;
        }
        return file.exists();
    }

    default boolean is_file(String path) {
        return is_file(file(path));
    }

    default boolean is_dir(File file) {
        if (file == null) {
            return false;
        }
        return file.isDirectory();
    }

    default boolean is_dir(String path) {
        return is_dir(file(path));
    }

    default List<File> list_file(String path) {
        return list_file(file(path));
    }

    default List<File> list_file(File file) {
        List<File> ret = new ArrayList<>();
        if (file == null) {
            return ret;
        }
        File[] files = file.listFiles();
        if (files == null) {
            return ret;
        }
        ret.addAll(Arrays.asList(files));
        return ret;
    }

    default void mkdirs(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    default String read_text_file(String filePath) throws IOException {
        return read_text_file(filePath, StandardCharsets.UTF_8.name());
    }

    default String read_text_file(String filePath, String charset) throws IOException {
        return StreamUtil.readString(new File(filePath), charset);
    }

    default void write_text_file(String filePath, Object content) throws IOException {
        write_text_file(filePath, content, StandardCharsets.UTF_8.name());
    }

    default void write_text_file(String filePath, Object content, String charset) throws IOException {
        String os = String.valueOf(content);
        StreamUtil.writeString(os, charset, new File(filePath));
    }
}
