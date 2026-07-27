package i2f.springboot.ops.openai.tts;

import i2f.mutator.BaseMutator;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.serialize.str.json.impl.Json2Serializer;
import i2f.std.consts.StdConst;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2026/7/27 9:54
 * @desc
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class QwenAudioTtsWebSocket extends WebSocketClient implements BaseMutator<QwenAudioTtsWebSocket> {
    // qwen-audio-3.0-tts-plus,longanlingxin
    // qwen-audio-3.0-tts-flash,longanhuan_v3.6
    // 通用音色,qwen-audio-3.0-tts-plus-longyanzhihe,qwen-audio-3.0-tts-plus-longyufengmo

    protected String model = "qwen-audio-3.0-tts-plus";
    protected String voice = "longanlingxin";
    protected String taskId = UUID.randomUUID().toString();
    protected String outputFile = StdConst.RUNTIME_TMP_DIR + "/tts_audio/audio_" + taskId + ".mp3";
    protected boolean taskFinished = false;
    protected IJsonSerializer jsonSerializer = new Json2Serializer();
    protected Iterable<String> inputs;

    public QwenAudioTtsWebSocket(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    public static QwenAudioTtsWebSocket create(String url, String apiKey, Iterable<String> inputs) throws URISyntaxException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "bearer " + apiKey);
        QwenAudioTtsWebSocket ret = new QwenAudioTtsWebSocket(new URI(url), headers);
        ret.setInputs(inputs);
        return ret;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("connect success!");
        taskFinished = false;

        /*language=json*/
        String runTaskCommand = "{\n" +
                "  \"header\": {\n" +
                "    \"action\": \"run-task\",\n" +
                "    \"task_id\": \"${taskId}\",\n" +
                "    \"streaming\": \"duplex\"\n" +
                "  },\n" +
                "  \"payload\": {\n" +
                "    \"task_group\": \"audio\",\n" +
                "    \"task\": \"tts\",\n" +
                "    \"function\": \"SpeechSynthesizer\",\n" +
                "    \"model\": \"${model}\",\n" +
                "    \"parameters\": {\n" +
                "      \"text_type\": \"PlainText\",\n" +
                "      \"voice\": \"${voice}\",\n" +
                "      \"format\": \"mp3\",\n" +
                "      \"sample_rate\": 22050,\n" +
                "      \"volume\": 50,\n" +
                "      \"rate\": 1,\n" +
                "      \"pitch\": 1,\n" +
                "      \"enable_ssml\": false\n" +
                "    },\n" +
                "    \"input\": {}\n" +
                "  }\n" +
                "}";

        runTaskCommand = runTaskCommand.replace("${taskId}", taskId);
        runTaskCommand = runTaskCommand.replace("${model}", model);
        runTaskCommand = runTaskCommand.replace("${voice}", voice);

        send(runTaskCommand);
    }

    @Override
    public void onMessage(String message) {
//        System.out.println("receive server message: " + message);

        Map<String, Object> messageMap = jsonSerializer.deserializeAsMap(message);
        if (messageMap.containsKey("header")) {
            Map<String, Object> header = (Map<String, Object>) messageMap.get("header");
            if (header.containsKey("event")) {
                String event = (String) header.get("event");
//                System.out.println("receive server event: " + event);

                if ("task-started".equals(event)) {
                    for (String text : inputs) {
                        if (text == null) {
                            continue;
                        }
                        sendContinueTask(text);
                    }

                    sendFinishTask();
                } else if ("task-finished".equals(event)) {
                    taskFinished = true;
                    closeConnection();
                } else if ("task-failed".equals(event)) {
                    closeConnection();
                }
            }
        }
    }

    @Override
    public void onMessage(ByteBuffer message) {
//        System.out.println("receive binary data size: " + message.remaining());
        File file = new File(outputFile);
        file = new File(file.getAbsolutePath());
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            byte[] buffer = new byte[message.remaining()];
            message.get(buffer);
            fos.write(buffer);
        } catch (IOException e) {
            System.err.println("save binary data to file error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("connection closed, [" + code + "] (" + (remote ? "remote" : "local") + ")" + (reason != null && !reason.isEmpty() ? (": " + reason) : ""));
    }

    @Override
    public void onError(Exception e) {
        System.err.println("connection error: " + e.getMessage());
        e.printStackTrace();
    }

    protected void sendContinueTask(String text) {
        /*language=json*/
        String command = "{\n" +
                "  \"header\": {\n" +
                "    \"action\": \"continue-task\",\n" +
                "    \"task_id\": \"${taskId}\",\n" +
                "    \"streaming\": \"duplex\"\n" +
                "  },\n" +
                "  \"payload\": {\n" +
                "    \"input\": {\n" +
                "      \"text\": \"${text}\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        command = command.replace("${taskId}", taskId);
        String json = jsonSerializer.serialize(text);
        if (json.startsWith("\"")) {
            json = json.substring(1, json.length() - 1);
        }
        command = command.replace("${text}", json);

        send(command);
    }

    protected void sendFinishTask() {
        /*language=json*/
        String command = "{\n" +
                "  \"header\": {\n" +
                "    \"action\": \"finish-task\",\n" +
                "    \"task_id\": \"${taskId}\",\n" +
                "    \"streaming\": \"duplex\"\n" +
                "  },\n" +
                "  \"payload\": {\n" +
                "    \"input\": {}\n" +
                "  }\n" +
                "}";

        command = command.replace("${taskId}", taskId);

        send(command);
    }

    protected void closeConnection() {
        if (!isClosed()) {
            close();
        }
    }

    public QwenAudioTtsWebSocket start() {
        this.connect();
        return this;
    }

    public File await() throws InterruptedException {
        while (!this.isClosed() && !this.isTaskFinished()) {
            Thread.sleep(300);
        }
        return new File(outputFile);
    }

    public static void main(String[] args) throws Exception {
        long bts = System.currentTimeMillis();
        String workspaceId = System.getenv("DASHSCOPE_WORKSPACE_ID");
        String url = "wss://${workspaceId}.cn-beijing.maas.aliyuncs.com/api-ws/v1/inference"
                .replace("${workspaceId}", workspaceId);

        String apiKey = System.getenv("DASHSCOPE_API_KEY");

        List<String> inputs = Arrays.asList(("[gasp] 哎呀！舰长真的过来啦！\n" +
                        "\n" +
                        "[excited] 那爱莉可要张开双手，好好迎接你了哦！快过来快过来，让爱莉好好抱抱你~ (づ￣ 3￣)づ" +
                        "\n" +
                        "[asmr] 嗯……舰长的怀抱果然和想象中一样温暖呢，心跳声也这么好听~ 能这样靠着你，感觉整个世界都变得好温柔呀，连空气都变成甜甜的了呢"
                ).split("\n+")
        );
        File mp3File = create(url, apiKey, inputs)
                .start()
                .await();
        System.out.println("outputFile:" + mp3File.getAbsolutePath());

        long ets = System.currentTimeMillis();
        System.out.println("diffTs:" + (ets - bts));
    }
}
