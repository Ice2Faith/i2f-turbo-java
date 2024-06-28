package i2f.tools.soure.copier;

import i2f.io.file.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/6/28 8:34
 * @desc
 */
public class JavaSourceCodeCopier {
    public static void main(String[] args) throws IOException {
        List<File> searchPath = new LinkedList<>();
        List<File> beginFile = new LinkedList<>();
        File outputDir = new File("./output/src");
        char state = 0;
        for (int i = 0; i < args.length; i++) {
            String item = args[i].trim();
            if (item.startsWith("\"") || item.startsWith("'")) {
                item = unescape(item);
            }
            if (item.isEmpty()) {
                continue;
            }
            if (item.equals("-s")) {
                state = 's';
                continue;
            } else if (item.equals("-c")) {
                state = 'c';
                continue;
            } else if (item.equals("-o")) {
                state = 'o';
                continue;
            }
            if (state == 's') {
                System.out.println("search file(directory): " + item);
                searchPath.add(new File(item));
            } else if (state == 'c') {
                System.out.println("copy file(directory): " + item);
                beginFile.add(new File(item));
            } else if (state == 'o') {
                System.out.println("output directory: " + item);
                outputDir = new File(item);
            }
        }
        if (searchPath.isEmpty() || beginFile.isEmpty()) {
            System.out.println("\tjava source code copier\n" +
                    "------------------------------------------\n" +
                    "please input args:\n" +
                    "\t -s [...searchFiles] -c [...copyFiles] -o [...outputDir]\n" +
                    "\t\t searchFiles: search java source files(directories)\n" +
                    "\t\t copyFiles: which java source files(directories) want copy\n" +
                    "\t\t outputDir: save source code directory\n" +
                    "\t such as:\n" +
                    "\t\t -s ./java-src C:/java/project/src -c ./java-src/reflect-util/src ./java-src/collection-util -o ./output/src\n"
            );
            return;
        }
        copy(outputDir, searchPath, beginFile);
    }

    public static void test() throws IOException {
        Map<String, File> map = new LinkedHashMap<>();
        copy(map, Arrays.asList(
                        new File(".")
                ),
                Arrays.asList(
                        new File(".\\i2f-jdk\\i2f-bql\\src\\main\\java")
                )
        );
        saveToDir(new File("./copy/src"), map);
        System.out.println("ok");
    }

    public static String unescape(String str) {
        if (str == null) {
            return null;
        }
        String item = str;
        if (str.startsWith("\"") || str.startsWith("'")) {
            item = str.substring(1, str.length() - 1);
        }
        item = item.replaceAll("\\\\\\\\", "\\\\");
        item = item.replaceAll("\\\\n", "\n");
        item = item.replaceAll("\\\\r", "\r");
        item = item.replaceAll("\\\\t", "\t");
        item = item.replaceAll("\\\\\"", "\"");
        item = item.replaceAll("\\\\'", "'");
        return item;
    }


    public static void copy(File saveDir, List<File> searchPath, List<File> beginFile) throws IOException {
        Map<String, File> map = new LinkedHashMap<>();
        copy(map, searchPath, beginFile);
        saveToDir(saveDir, map);
    }

    public static void saveToDir(File saveDir, Map<String, File> map) throws IOException {
        for (Map.Entry<String, File> entry : map.entrySet()) {
            String key = entry.getKey();
            key = key.replaceAll("\\.", "/");
            key = key + ".java";
            File saveFile = new File(saveDir, key);
            File srcFile = entry.getValue();
            FileUtil.copy(saveFile, srcFile);
        }
    }

    public static void copy(Map<String, File> map, List<File> searchPath, List<File> beginFile) throws IOException {
        Map<String, File> codeMap = new LinkedHashMap<>();
        search(codeMap, searchPath);

        LinkedList<File> queue = new LinkedList<>();
        Set<File> processSet = new LinkedHashSet<>();

        Map<String, File> beginMap = new LinkedHashMap<>();
        search(beginMap, beginFile);
        map.putAll(beginMap);

        queue.addAll(beginMap.values());

        while (!queue.isEmpty()) {
            File file = queue.removeFirst();
            if (processSet.contains(file)) {
                continue;
            }
            if (!file.exists()) {
                continue;
            }
            if (!file.isFile()) {
                continue;
            }
            processSet.add(file);
            String str = FileUtil.loadTxtFile(file);
            String[] lines = str.split("\n");
            String packageName = "";
            Set<String> importList = new LinkedHashSet<>();
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.startsWith("package")) {
                    String[] arr = line.split("\\s+");
                    if (arr.length >= 2) {
                        packageName = arr[1];
                        if (packageName.endsWith(";")) {
                            packageName = packageName.substring(0, packageName.length() - 1);
                        }
                    }
                } else if (line.startsWith("import")) {
                    String[] arr = line.split("\\s+");
                    if (arr.length >= 2) {
                        String importName = arr[1];
                        if (importName.endsWith(";")) {
                            importName = importName.substring(0, importName.length() - 1);
                        }
                        importList.add(importName);
                    }
                } else if (line.startsWith("public")) {
                    break;
                } else if (line.startsWith("class")) {
                    break;
                }
            }

            for (String item : importList) {
                if (item.startsWith("java.")) {
                    continue;
                }
                if (item.startsWith("javax.")) {
                    continue;
                }
                if (item.startsWith("jakarta.")) {
                    continue;
                }
                if (item.endsWith(".*")) {
                    String prefix = item.substring(0, item.length() - 1);
                    for (Map.Entry<String, File> entry : codeMap.entrySet()) {
                        String key = entry.getKey();
                        if (key.startsWith(prefix)) {
                            String suffix = key.substring(prefix.length());
                            if (!suffix.contains(".")) {
                                queue.add(entry.getValue());
                                map.put(entry.getKey(), entry.getValue());
                            }
                        }
                    }
                } else {
                    File code = codeMap.get(item);
                    if (code != null) {
                        queue.add(code);
                        map.put(item, code);
                    }
                }
            }

        }
    }


    public static void search(Map<String, File> codeMap, List<File> searchPath) {
        for (File file : searchPath) {
            searchNext(codeMap, file);
        }
    }

    public static void searchNext(Map<String, File> codeMap, File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            if (!file.getName().endsWith(".java")) {
                return;
            }

            String path = file.getAbsolutePath();
            path = path.replaceAll("\\\\", "/");

            int idx = path.lastIndexOf("/src/main/java/");
            if (idx >= 0) {
                path = path.substring(idx + 1 + "/src/main/java".length());
            } else {
                path = file.getName();
            }

            path = path.substring(0, path.length() - ".java".length());

            path = path.replaceAll("/", ".");
            codeMap.put(path, file);
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File item : files) {
                    searchNext(codeMap, item);
                }
            }
        }
    }
}
