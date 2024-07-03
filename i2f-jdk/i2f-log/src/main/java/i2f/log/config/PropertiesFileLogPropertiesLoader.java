package i2f.log.config;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Ice2Faith
 * @date 2024/7/3 11:42
 * @desc
 */
public class PropertiesFileLogPropertiesLoader {
    public static final String PREFIX = "log.";

    public static LogProperties load(URL url) throws IOException {
        InputStream is = url.openStream();
        return load(is);
    }

    public static LogProperties load(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        return load(fis);
    }

    public static LogProperties load(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        Properties properties = new Properties();
        properties.load(reader);
        reader.close();
        return load(properties);
    }

    public static LogProperties load(Properties properties) {
        LogProperties ret = new LogProperties();

        Map<String, Map<String, String>> writersMap = new LinkedHashMap<>();
        Map<String, Map<String, String>> levelsMap = new LinkedHashMap<>();

        for (String propName : properties.stringPropertyNames()) {
            String item = propName.trim();
            if (!item.startsWith(PREFIX)) {
                continue;
            }
            String value = properties.getProperty(item);
            if (value != null) {
                value = value.trim();
            }
            String name = item.substring(PREFIX.length());
            String[] arr = name.split("\\.");
            if (arr.length < 1) {
                continue;
            }
            String group = arr[0].replaceAll("-|_", "");
            try {
                if ("stdoutRedirect".equalsIgnoreCase(group)) {
                    if (arr.length < 2) {
                        continue;
                    }
                    LogProperties.StdoutRedirectProperties root = ret.getStdoutRedirect();
                    String prop = arr[1].replaceAll("-|_", "");
                    if ("enable".equalsIgnoreCase(prop)) {
                        root.setEnable(Boolean.parseBoolean(value));
                    } else if ("keepConsole".equalsIgnoreCase(prop)) {
                        root.setKeepConsole(Boolean.parseBoolean(value));
                    } else if ("useTrace".equalsIgnoreCase(prop)) {
                        root.setUseTrace(Boolean.parseBoolean(value));
                    }
                } else if ("stdoutWriter".equalsIgnoreCase(group)) {
                    if (arr.length < 2) {
                        continue;
                    }
                    LogProperties.StdoutWriterProperties root = ret.getStdoutWriter();
                    String prop = arr[1].replaceAll("-|_", "");
                    if ("enable".equalsIgnoreCase(prop)) {
                        root.setEnable(Boolean.parseBoolean(value));
                    }
                } else if ("fileWriter".equalsIgnoreCase(group)) {
                    if (arr.length < 2) {
                        continue;
                    }
                    LogProperties.FileWriterProperties root = ret.getFileWriter();
                    String prop = arr[1].replaceAll("-|_", "");
                    if ("enable".equalsIgnoreCase(prop)) {
                        root.setEnable(Boolean.parseBoolean(value));
                    } else if ("filePath".equalsIgnoreCase(prop)) {
                        root.setFilePath(value);
                    } else if ("applicationName".equalsIgnoreCase(prop)) {
                        root.setApplicationName(value);
                    } else if ("limitLevel".equalsIgnoreCase(prop)) {
                        root.setLimitLevel(value);
                    } else if ("fileLimitSizeMb".equalsIgnoreCase(prop)) {
                        root.setFileLimitSizeMb(Long.parseLong(value));
                    } else if ("fileLimitTotalSizeMb".equalsIgnoreCase(prop)) {
                        root.setFileLimitTotalSizeMb(Long.parseLong(value));
                    } else if ("fileSizeCheckCount".equalsIgnoreCase(prop)) {
                        root.setFileSizeCheckCount(Integer.parseInt(value));
                    }
                } else if ("broadcastWriter".equalsIgnoreCase(group)) {
                    if (arr.length < 2) {
                        continue;
                    }
                    LogProperties.BroadcastWriterProperties root = ret.getBroadcastWriter();
                    String prop = arr[1].replaceAll("-|_", "");
                    if ("enable".equalsIgnoreCase(prop)) {
                        root.setEnable(Boolean.parseBoolean(value));
                    } else if ("async".equalsIgnoreCase(prop)) {
                        root.setAsync(Boolean.parseBoolean(value));
                    } else if ("parallelism".equalsIgnoreCase(prop)) {
                        root.setParallelism(Integer.parseInt(value));
                    } else if (prop.startsWith("items[")) {
                        if (arr.length < 3) {
                            continue;
                        }
                        String key = prop;
                        if (!writersMap.containsKey(key)) {
                            writersMap.put(key, new HashMap<>());
                        }
                        writersMap.get(key).put(arr[2], value);
                    }
                } else if ("loggingLevel".equalsIgnoreCase(group)) {
                    if (arr.length < 2) {
                        continue;
                    }
                    LogProperties.LoggingLevelProperties root = ret.getLoggingLevel();
                    String prop = arr[1].replaceAll("-|_", "");
                    if ("enable".equalsIgnoreCase(prop)) {
                        root.setEnable(Boolean.parseBoolean(value));
                    } else if ("rootLevel".equalsIgnoreCase(prop)) {
                        root.setRootLevel(value);
                    } else if (prop.startsWith("items[")) {
                        if (arr.length < 3) {
                            continue;
                        }
                        String key = prop;
                        if (!levelsMap.containsKey(key)) {
                            levelsMap.put(key, new HashMap<>());
                        }
                        levelsMap.get(key).put(arr[2], value);
                    }
                }
            } catch (Exception e) {

            }
        }

        for (Map.Entry<String, Map<String, String>> entry : writersMap.entrySet()) {
            Map<String, String> map = entry.getValue();
            LogProperties.LogWriterItemProperties root = new LogProperties.LogWriterItemProperties();
            for (Map.Entry<String, String> item : map.entrySet()) {
                String prop = item.getKey();
                String value = item.getValue();
                if (value == null) {
                    continue;
                }
                value = value.trim();
                if (value.isEmpty()) {
                    continue;
                }
                try {
                    prop = prop.replaceAll("-|_", "");
                    if ("name".equalsIgnoreCase(prop)) {
                        root.setName(value);
                    } else if ("enable".equalsIgnoreCase(prop)) {
                        root.setEnable(Boolean.parseBoolean(value));
                    } else if ("className".equalsIgnoreCase(prop)) {
                        root.setClassName(value);
                    } else if ("params".equalsIgnoreCase(prop)) {
                        root.setParams(value);
                    }
                } catch (Exception e) {

                }
            }
            ret.getBroadcastWriter().getItems().add(root);
        }

        for (Map.Entry<String, Map<String, String>> entry : levelsMap.entrySet()) {
            Map<String, String> map = entry.getValue();
            LogProperties.LoggingLevelItemProperties root = new LogProperties.LoggingLevelItemProperties();
            for (Map.Entry<String, String> item : map.entrySet()) {
                String prop = item.getKey();
                String value = item.getValue();
                if (value == null) {
                    continue;
                }
                value = value.trim();
                if (value.isEmpty()) {
                    continue;
                }
                try {
                    prop = prop.replaceAll("-|_", "");
                    if ("patten".equalsIgnoreCase(prop)) {
                        root.setPatten(value);
                    } else if ("enable".equalsIgnoreCase(prop)) {
                        root.setEnable(Boolean.parseBoolean(value));
                    } else if ("level".equalsIgnoreCase(prop)) {
                        root.setLevel(value);
                    }
                } catch (Exception e) {

                }
            }
            ret.getLoggingLevel().getItems().add(root);
        }

        return ret;
    }
}
