package i2f.spring.ai.chat.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;

/**
 * @author Ice2Faith
 * @date 2025/5/27 21:42
 * @desc
 */
public class FileSystemTools {
    protected String rootPath = "./";

    @Tool(description = "list files in user home/" +
            "列举用户目录的文件列表")
    public String listUserHomeListFiles(@ToolParam(description = "directory path,default is empty string '' when not declare/" +
            "目录名称，如果没有指定，则默认为空字符串：''") String subPath) {
        if (subPath == null) {
            subPath = "";
        }
        subPath = subPath.replace("\\", "/");
        subPath = subPath.replace("../", "");
        File dir = new File(rootPath, subPath);
        File[] files = dir.listFiles();
        StringBuilder builder = new StringBuilder();
        for (File file : files) {
            builder.append("type:").append(file.isDirectory() ? "directory" : "file").append("\t")
                    .append("name:").append(file.getName())
                    .append("size:").append(file.length()).append("bytes")
                    .append("\n");
        }
        return builder.toString();
    }
}
