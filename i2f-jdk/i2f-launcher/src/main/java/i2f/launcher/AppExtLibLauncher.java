package i2f.launcher;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AppExtLibLauncher {
    public static final String DEFAULT_CONFIG_FILE = "./launcher.cnf";
    public static final String[] DEFAULT_EXCLUDE_FILE_NAME_LIST = {
            ".git", ".gitee", ".github", ".gitlab",
            ".gitignore", ".gitattributes",
            ".idea", ".vscode",
            ".ssh",
            ".bashrc", ".bash_history",
            "node_modules", ".pnpm_modules", ".cache",
            "npm_modules", ".npm_cache",
            ".yarn_cache", "yarn_modules",
    };
    public static final String[] DEFAULT_CLASSPATH_LIST = new String[]{
            "./resources", "./config", "./conf",
            "./lib", "./lib-ext", "./lib-provided",
            "./libs", "./libs-ext", "./libs-provided",
            "./classes",
            "./static", "./public",
            "./template",
    };

    public static void main(String[] args) throws Exception {

        Map<String, List<String>> argsMap = new LinkedHashMap<>();
        parseCommandArgs(args, argsMap);

        String configFile = null;
        if (!argsMap.isEmpty()) {
            if (configFile == null || configFile.isEmpty()) {
                List<String> list = argsMap.get("--config-file");
                if (list != null && !list.isEmpty()) {
                    configFile = list.get(0);
                }
            }
            if (configFile == null || configFile.isEmpty()) {
                List<String> list = argsMap.get("--config");
                if (list != null && !list.isEmpty()) {
                    configFile = list.get(0);
                }
            }
            if (configFile == null || configFile.isEmpty()) {
                List<String> list = argsMap.get("--cf");
                if (list != null && !list.isEmpty()) {
                    configFile = list.get(0);
                }
            }
        }

        if (configFile == null || configFile.isEmpty()) {
            configFile = DEFAULT_CONFIG_FILE;
        }

        parseConfigFileArgs(new File(configFile), argsMap);


        Set<String> classPathList = new LinkedHashSet<>();
        if (!argsMap.isEmpty()) {
            List<String> list1 = argsMap.get("--classpath");
            if (list1 != null) {
                classPathList.addAll(list1);
            }
            List<String> list2 = argsMap.get("--cp");
            if (list2 != null) {
                classPathList.addAll(list2);
            }
        }

        if (!argsMap.containsKey("--exclude-default")) {
            classPathList.addAll(Arrays.asList(DEFAULT_CLASSPATH_LIST));
        }

        String entryClass = null;
        if (!argsMap.isEmpty()) {
            if (entryClass == null || entryClass.isEmpty()) {
                List<String> list = argsMap.get("--entry-class");
                if (list != null && !list.isEmpty()) {
                    entryClass = list.get(0);
                }
            }
            if (entryClass == null || entryClass.isEmpty()) {
                List<String> list = argsMap.get("--entry");
                if (list != null && !list.isEmpty()) {
                    entryClass = list.get(0);
                }
            }
            if (entryClass == null || entryClass.isEmpty()) {
                List<String> list = argsMap.get("--ec");
                if (list != null && !list.isEmpty()) {
                    entryClass = list.get(0);
                }
            }
        }

        if (entryClass == null || entryClass.isEmpty()) {
            printHelp();
            throw new RuntimeException("missing argument [--entry-class] to defined entry class.");
        }

        List<File> fileList = new ArrayList<>();

        for (String item : classPathList) {
            fileList.add(new File(item));
        }

        // 要求，入口主类与本启动类所属的classpath不一样
        // 也就是，不由同一个classloader加载
        // 简单来说，常见场景就是在不同的jar包中
        launch(fileList, entryClass, args);
    }

    public static void printHelp() {
        System.out.println("---------------------------------------");
        System.out.println("application launcher");
        System.out.println("\t--class-path: add file path or file to classpath");
        System.out.println("\t\talias:--cp");
        System.out.println("\t--exclude-default: exclude default classpath");
        System.out.println("\t\tdefault classpath:");
        for (String item : DEFAULT_CLASSPATH_LIST) {
            System.out.println("\t\t\t" + item);
        }
        System.out.println("\t--entry-class: entry class to be launched.");
        System.out.println("\t\talias:--entry");
        System.out.println("\t\talias:--ec");
        System.out.println("\t--config-file: use config file to define args.");
        System.out.println("\t\talias:--config");
        System.out.println("\t\talias:--cf");
        System.out.println("\t\tfile content every line cast as one command arg");
        System.out.println("\t\tcloud use # start with line to comment a line");
        System.out.println("\t\tfile content such as:");
        System.out.println("\t\t\t--classpath");
        System.out.println("\t\t\t./config");
        System.out.println("\t\t\t./conf");
        System.out.println("\t\t\t--entry-class");
        System.out.println("\t\t\tcom.test.TestMain");
        System.out.println("\t\t\t--exclude-default");
        System.out.println("\tusage:");
        System.out.println("\t\tby command:");
        System.out.println("\t\t\tjava -jar thisJarName.jar --cp ./resources ./targetLauncherJar.jar --entry com.test.Main");
        System.out.println("\t\tby config file:");
        System.out.println("\t\t\tjava -jar thisJarName.jar --config ./launcher.cnf");
    }

    public static Map<String, List<String>> parseConfigFileArgs(File file, Map<String, List<String>> ret) {
        if (!file.exists()) {
            return ret;
        }
        if (!file.isFile()) {
            return ret;
        }
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith("#")) {
                    continue;
                }
                lines.add(line);
            }
        } catch (IOException e) {

        }
        parseCommandArgs(lines.toArray(new String[0]), ret);
        return ret;
    }

    public static Map<String, List<String>> parseCommandArgs(String[] args, Map<String, List<String>> ret) {
        String openFlag = null;
        for (String arg : args) {
            if (arg.startsWith("--")) {
                openFlag = arg;
                if (!ret.containsKey(openFlag)) {
                    ret.put(openFlag, new ArrayList<>());
                }
            } else {
                if (openFlag == null) {
                    if (!ret.containsKey(arg)) {
                        ret.put(arg, new ArrayList<>());
                    }
                } else {
                    ret.get(openFlag).add(arg);
                }
            }
        }
        return ret;
    }

    public static void launch(List<File> fileList, String entryClassName, String[] args) throws Exception {
        List<URL> urlList = new ArrayList<>();
        for (File file : fileList) {
            loadClassPathsByFile(file, urlList);
        }

        for (URL url : urlList) {
            System.out.println("classpath:" + url);
        }

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ExtLibClassLoader classLoader = new ExtLibClassLoader(urlList.toArray(new URL[0]), contextClassLoader);
        Thread.currentThread().setContextClassLoader(classLoader);
        Class<?> mainClass = classLoader.loadClass(entryClassName);
        Method mainMethod = mainClass.getMethod("main", String[].class);
        mainMethod.invoke(null, (Object) args);
    }

    public static void loadClassPathsByFile(File file, Collection<URL> urlList) throws IOException {
        if (file == null) {
            return;
        }
        String name = file.getName();
        for (String item : DEFAULT_EXCLUDE_FILE_NAME_LIST) {
            if (item.equals(name)) {
                return;
            }
        }
        try {
            if (file.isFile()) {
                if (name.endsWith(".jar")) {
                    scanJarForNestedJars(file, urlList);
                }
            } else if (file.isDirectory()) {
                urlList.add(file.toURI().toURL());
                scanDirectoryForJars(file, urlList);
            }
        } catch (Exception e) {
            System.err.println("bad file:" + file);
            e.printStackTrace();
        }
    }

    public static void scanJarForNestedJars(File jarFile, Collection<URL> urlList) {
        if (jarFile == null) {
            return;
        }
        try (JarFile jar = new JarFile(jarFile)) {
            urlList.add(jarFile.toURI().toURL());
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                String name = entry.getName();
                if (name == null || name.isEmpty()) {
                    continue;
                }
                if (name.endsWith(".jar")) {
                    urlList.add(new URL("jar:" + jarFile.toURI().toURL() + "!/" + entry.getName()));
                }
            }
        } catch (Exception e) {
            System.err.println("bad jar:" + jarFile);
            e.printStackTrace();
        }
    }

    public static void scanDirectoryForJars(File directory, Collection<URL> urlList) {
        if (directory == null) {
            return;
        }
        try {
            if (!directory.isDirectory()) {
                return;
            }
            File[] files = directory.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                String name = file.getName();
                for (String item : DEFAULT_EXCLUDE_FILE_NAME_LIST) {
                    if (item.equals(name)) {
                        return;
                    }
                }
                if (file.isDirectory()) {
                    try {
                        scanDirectoryForJars(file, urlList);
                    } catch (Exception e) {
                        System.err.println("bad directory:" + file);
                        e.printStackTrace();
                    }
                }
                if (!file.isFile()) {
                    continue;
                }
                if (name.endsWith(".jar")) {
                    try {
                        scanJarForNestedJars(file, urlList);
                    } catch (Exception e) {
                        System.err.println("bad jar:" + file);
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("bad directory:" + directory);
            e.printStackTrace();
        }

    }
}
