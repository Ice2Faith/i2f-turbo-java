package i2f.spring.ai.chat.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;

/**
 * 我有哪些文件，要求文件大小使用KB为单位显示，按照文件大小从大到小排列；
 * 其中最大的三个文件是哪些
 *
 * @author Ice2Faith
 * @date 2025/5/27 21:42
 * @desc
 */
public class FileSystemTools {
    protected String rootPath = "./";

    public File getSubFile(String subPath){
        if (subPath == null) {
            subPath = "";
        }
        subPath = subPath.replace("\\", "/");
        subPath = subPath.replace("../", "");
        File rootDir=new File(rootPath);
        String absRootPath=new File(rootDir.getAbsolutePath()).getAbsolutePath().replace("\\","/");
        if(!absRootPath.endsWith("/")){
            absRootPath=absRootPath+"/";
        }


        File dir = new File(rootPath, subPath);
        String absDirPath=new File(dir.getAbsolutePath()).getAbsolutePath().replace("\\","/");

        if(!absDirPath.startsWith(absRootPath)){
            throw new IllegalArgumentException("found illegal file path access, reject operation!/" +
                    "发现非法的文件目录访问，与拒绝操作！");
        }
        return dir;
    }

    @Tool(description = "list files in user home/" +
            "列举用户目录的文件列表")
    public String listUserHomeListFiles(@ToolParam(description = "directory path,default is empty string '' when not declare/" +
            "目录名称，如果没有指定，则默认为空字符串：''") String subPath) {

        File dir=getSubFile(subPath);

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
