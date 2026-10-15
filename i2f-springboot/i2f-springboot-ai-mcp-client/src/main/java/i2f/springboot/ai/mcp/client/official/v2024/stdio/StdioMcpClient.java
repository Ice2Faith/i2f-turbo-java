package i2f.springboot.ai.mcp.client.official.v2024.stdio;

import com.newland.bi3.os.OsUtil;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2026/10/14 8:47
 * @desc
 */
public class StdioMcpClient implements AutoCloseable {

    private IJsonSerializer jsonSerializer = new Json2Serializer();
    private final Process process;
    private final BufferedReader stdout;
    private final BufferedWriter stdin;
    private final ConcurrentHashMap<String, CompletableFuture<Object>> pending = new ConcurrentHashMap<>();

    public StdioMcpClient(String[] cmdarray) throws IOException {
        this(cmdarray, null, null);
    }

    public StdioMcpClient(String[] cmdarray, String[] envp) throws IOException {
        this(cmdarray, envp, null);
    }

    public StdioMcpClient(String[] cmdarray, String[] envp, File dir) throws IOException {

        this.process = createProcess(cmdarray, envp, dir);
        this.stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
        this.stdin = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

        // 单独线程读取 stdout
        Thread reader = new Thread(this::readLoop, "mcp-stdout-reader");
        reader.setDaemon(true);
        reader.start();
    }

    public Process createProcess(String[] cmdarray, String[] envp, File dir) throws IOException {
        List<String> cmd = new ArrayList<>(Arrays.asList(cmdarray));

        Map<String, String> envMap = new HashMap<>();
        if (envp != null) {
            for (String line : envp) {
                String[] arr = line.split("=", 2);
                envMap.put(arr[0], arr.length == 2 ? arr[1] : "");
            }
        }
        try {
            ProcessBuilder builder = new ProcessBuilder(cmd);
            if (envMap != null && !envMap.isEmpty()) {
                builder.environment().putAll(envMap);
            }
            if (dir != null) {
                builder.directory(dir);
            }
            return builder
                    .redirectErrorStream(false)
                    .start();
        } catch (IOException e) {
            if (OsUtil.isWindows()) {
                cmd.addAll(0, Arrays.asList("cmd", "/c"));

                ProcessBuilder builder = new ProcessBuilder(cmd);
                if (envMap != null && !envMap.isEmpty()) {
                    builder.environment().putAll(envMap);
                }
                if (dir != null) {
                    builder.directory(dir);
                }
                return builder
                        .redirectErrorStream(false)
                        .start();
            } else {
                cmd.addAll(0, Arrays.asList("sh", "-c"));

                ProcessBuilder builder = new ProcessBuilder(cmd);
                if (envMap != null && !envMap.isEmpty()) {
                    builder.environment().putAll(envMap);
                }
                if (dir != null) {
                    builder.directory(dir);
                }
                return builder
                        .redirectErrorStream(false)
                        .start();
            }
        }
    }

    private void readLoop() {
        try {
            String line = null;
            while ((line = stdout.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                try {
                    Map<String, Object> msg = jsonSerializer.deserializeAsMap(line);
                    if (msg.containsKey("id")) {
                        String id = String.valueOf(msg.get("id"));
                        CompletableFuture<Object> future = pending.remove(id);
                        if (future != null) {
                            future.complete(msg);
                        }
                    }
                } catch (Exception ignore) {
                }
            }
        } catch (IOException ignore) {
        }
    }

    public CompletableFuture<Object> send(String id, Object request) throws IOException {

        CompletableFuture<Object> future = new CompletableFuture<>();
        pending.put(id, future);

        stdin.write(jsonSerializer.serialize(request));
        stdin.newLine();
        stdin.flush();

        return future;
    }

    @Override
    public void close() {
        try {
            stdin.close();
        } catch (IOException ignore) {
        }
        process.destroy();
    }
}
