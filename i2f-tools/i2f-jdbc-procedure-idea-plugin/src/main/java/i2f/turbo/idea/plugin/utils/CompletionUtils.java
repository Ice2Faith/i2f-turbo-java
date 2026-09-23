package i2f.turbo.idea.plugin.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLConnection;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;

/**
 * @author Ice2Faith
 * @date 2026/8/20 14:48
 * @desc
 */
public class CompletionUtils {
    public static final Map<String, Class<?>> FUNCTIONS = Collections.unmodifiableMap(getFunctions());

    public static void getFunctionsNext(Class<?> clazz, Map<String, Class<?>> completions) {
        // 类名
        String name = clazz.getName();
        if (!completions.containsKey(name)) {
            completions.put(name, clazz);
        }
        // 包路径
        String[] arr = name.split("\\.");
        for (String item : arr) {
            if (item.isEmpty()) {
                continue;
            }
            if (!completions.containsKey(item)) {
                completions.put(item, clazz);
            }
        }

        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            String next = method.getName() + "()";
            if (!completions.containsKey(next)) {
                completions.put(next, clazz);
            }
        }
    }

    public static Map<String, Class<?>> getFunctions() {
        Map<String, Class<?>> completions = new LinkedHashMap<>();

        Class<?>[] types = {
                String.class, Object.class, System.class, Runtime.class,
                //
                Objects.class, Collections.class, Arrays.class,
                //
                Collection.class, Map.class, List.class, HashMap.class,
                Queue.class, Stack.class, Deque.class,
                //
                Math.class, Date.class, Enum.class, Random.class,
                //
                Iterator.class, Iterable.class, Enumeration.class,
                Integer.class, Long.class, Double.class,
                //
                Exception.class,
                //
                Lock.class, CountDownLatch.class,
                //
                Class.class, Method.class, Field.class, Array.class, ClassLoader.class,
                //
                File.class,
                InputStream.class, OutputStream.class,
                Reader.class, Writer.class, BufferedReader.class,
                //
                Connection.class, Statement.class, PreparedStatement.class, ResultSet.class,
                DriverManager.class, Driver.class,
                //
                URLConnection.class,
                //
                ObjectMapper.class, ObjectWriter.class
        };

        for (Class<?> type : types) {
            getFunctionsNext(type, completions);
        }

        return completions;
    }
}
