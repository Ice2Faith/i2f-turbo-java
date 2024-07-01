package i2f.io.filesystem.abs;


import i2f.io.filesystem.IFile;
import i2f.io.filesystem.IFileSystem;

import java.util.Stack;

public class FileSystemUtil {
    public static void recursiveMkdirs(IFile file) {
        if ("".equals(file.getPath())) {
            return;
        }
        if (".".equals(file.getPath())) {
            return;
        }
        if ("/".equals(file.getPath())) {
            return;
        }
        if (!file.getDirectory().isExists()) {
            recursiveMkdirs(file.getDirectory());
        }
        file.mkdir();
    }

    public static String combinePath(String path, String subPath, String pathSeparator) {
        if (path == null && subPath == null) {
            return null;
        }
        if (path == null) {
            return subPath;
        }
        if (subPath == null) {
            return path;
        }
        String ret = "";
        String sep = pathSeparator;
        if (path.endsWith(sep)) {
            if (subPath.startsWith(sep)) {
                ret = path + subPath.substring(sep.length());
            } else {
                ret = path + subPath;
            }
        } else {
            if (subPath.startsWith(sep)) {
                ret = path + subPath;
            } else {
                ret = path + sep + subPath;
            }
        }
        return ret;
    }

    public static String absPath(String path, String pathSeparator) {
        if (path == null) {
            return null;
        }
        int idx = path.indexOf("%00");
        if (idx >= 0) {
            path = path.substring(0, idx);
        }
        String sep = pathSeparator;
        if (path.contains("\\")) {
            path = path.replaceAll("\\\\", "/");
        }

        String[] arr = path.split("/");
        Stack<String> stack = new Stack<>();
        for (String item : arr) {
            if (".".equals(item)) {

            } else if ("..".equals(item)) {
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else if ("".equals(item)) {

            } else {
                stack.push(item);
            }
        }
        StringBuilder ret = new StringBuilder();
        boolean first = true;
        for (String str : stack) {
            if (!first) {
                ret.append(sep);
            }
            ret.append(str);
            first = false;
        }
        return ret.toString();
    }

    public static IFile getStrictFile(IFileSystem fileSystem, String rootPath, String path) {
        IFile file = fileSystem.getFile(rootPath, path);
        String fullPath = fileSystem.absPath(file.getAbsolutePath());
        if (fullPath.equals(rootPath)) {
            return file;
        }
        boolean unAccess = false;
        if (!fullPath.startsWith(rootPath + fileSystem.pathSeparator())) {
            unAccess = true;
        }
        if (unAccess) {
            throw new IllegalStateException("target path cannot access.");
        }
        return file;
    }


}
