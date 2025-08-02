package i2f.template.render.impl;


import i2f.io.file.FileUtil;

import java.io.File;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2021/10/26
 * 文件模板加载器
 * 支持classpath写法
 */
public class FileTemplateLoader implements Function<String, String> {
    @Override
    public String apply(String val) {
        if (val == null) {
            return null;
        }
        val = val.trim();
        try {
            File file = FileUtil.getFileWithClasspath(val);
            if (file != null) {
                return FileUtil.loadTxtFile(file);
            }
        } catch (Exception e) {

        }
        return null;
    }
}
