package i2f.extension.ai.langchain4j;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * @author Ice2Faith
 * @date 2025/5/10 15:15
 */
public class TestAiChat {

    public static final String AI_BASE_URL = "https://dashscope.aliyuncs.com/compatible-mode/v1";
    public static final String AI_API_KEY = "sk-xxxxx";
    public static final String AI_MODEL_NAME = "qwq-plus";
    public static final String AI_SYSTEM = "你熟悉各种植物习性与特性，是资深植物学家";

    public static void main(String[] args) throws Throwable {
//        stream();
//        aiChat();
//        aiStream();
        aiConsumer();
    }

    public static void aiConsumer() throws Throwable {
        AiChatModel chatModel = AiChatModel.builder()
                .baseUrl(AI_BASE_URL)
                .apiKey(AI_API_KEY)
                .modelName(AI_MODEL_NAME)
                .system(AI_SYSTEM)
                .build();

        Scanner scanner = new Scanner(System.in);
        String line = null;
        while (true) {
            System.out.println("\n please input message for chat with ai, input \"/bye\" to exit:");
            System.out.print(">/ ");
            line = scanner.nextLine();
            if ("/bye".equals(line)) {
                break;
            }

            System.out.println("ai>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
            CountDownLatch latch = new CountDownLatch(1);
            chatModel.stream(line, new Consumer<String>() {
                @Override
                public void accept(String s) {
                    if (s == null) {
                        latch.countDown();
                        return;
                    }
                    System.out.print(s);
                }
            });
            try {
                latch.await();
            } catch (Exception e) {
            }
            System.out.println();
        }

        System.out.println("system: bye~");
    }

    public static void aiStream() throws Throwable {
        AiChatModel chatModel = AiChatModel.builder()
                .baseUrl(AI_BASE_URL)
                .apiKey(AI_API_KEY)
                .modelName(AI_MODEL_NAME)
                .system(AI_SYSTEM)
                .build();

        Scanner scanner = new Scanner(System.in);
        String line = null;
        while (true) {
            System.out.println("\n please input message for chat with ai, input \"/bye\" to exit:");
            System.out.print(">/ ");
            line = scanner.nextLine();
            if ("/bye".equals(line)) {
                break;
            }

            Iterator<String> resp = chatModel.stream(line);
            System.out.println("ai>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
            while (resp.hasNext()) {
                System.out.print(resp.next());
            }
            System.out.println();
        }

        System.out.println("system: bye~");
    }

    public static void aiChat() throws Throwable {
        AiChatModel chatModel = AiChatModel.builder()
                .baseUrl(AI_BASE_URL)
                .apiKey(AI_API_KEY)
                .modelName(AI_MODEL_NAME)
                .system(AI_SYSTEM)
                .build();

        Scanner scanner = new Scanner(System.in);
        String line = null;
        while (true) {
            System.out.println("\n please input message for chat with ai, input \"/bye\" to exit:");
            System.out.print(">/ ");
            line = scanner.nextLine();
            if ("/bye".equals(line)) {
                break;
            }

            String resp = chatModel.chat(line);
            System.out.println("ai>>>>>>>>>>>>>>>>>>>>>>>>>>\n" + resp);
        }

        System.out.println("system: bye~");
    }

    public static void stream() throws Throwable {
        StreamingChatModel chatModel = OpenAiStreamingChatModel.builder()
                .baseUrl(AI_BASE_URL)
                .apiKey(AI_API_KEY)
                .modelName(AI_MODEL_NAME)
                .build();

        List<ChatMessage> messages = new ArrayList<>();
        sendChatSystem(messages, AI_SYSTEM);

        sendChatMessage(messages, "香水柠檬");
        awaitChatResponse(chatModel, messages);

        sendChatMessage(messages, "花香吗");
        awaitChatResponse(chatModel, messages);

        Scanner scanner = new Scanner(System.in);
        String line = null;
        while (true) {
            System.out.println("\n please input message for chat with ai, input \"/bye\" to exit:");
            System.out.print(">/ ");
            line = scanner.nextLine();
            if ("/bye".equals(line)) {
                break;
            }

            sendChatMessage(messages, line);
            awaitChatResponse(chatModel, messages);
        }

        System.out.println("system: bye~");
    }

    public static void sendChatSystem(List<ChatMessage> messages, String text) throws Throwable {
        messages.add(new SystemMessage(text));
        System.out.println("\nsystem:********************************\n" + text);
    }

    public static void sendChatMessage(List<ChatMessage> messages, String text) throws Throwable {
        messages.add(new UserMessage(text));
        System.out.println("\nuser:<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n" + text);
    }

    public static void awaitChatResponse(StreamingChatModel chatModel, List<ChatMessage> messages) throws Throwable {
        CountDownLatch latch = new CountDownLatch(1);
        chatModel.chat(messages, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String s) {
                System.out.print(s);
            }

            @Override
            public void onCompleteResponse(ChatResponse chatResponse) {
                AiMessage aiMessage = chatResponse.aiMessage();
                messages.add(aiMessage);
                System.out.println("\nai:>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n" + aiMessage.text());
                latch.countDown();
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                latch.countDown();
            }
        });

        latch.await();
    }

    public static void chat() {
        ChatModel chatModel = OpenAiChatModel.builder()
                .baseUrl(AI_BASE_URL)
                .apiKey(AI_API_KEY)
                .modelName(AI_MODEL_NAME)
                .build();

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new SystemMessage(AI_SYSTEM));

        messages.add(new UserMessage("香水柠檬"));

        AiMessage aiMessage = chatModel.chat(messages).aiMessage();
        messages.add(aiMessage);
        System.out.println(aiMessage.text());

        messages.add(new UserMessage("花香吗"));

        aiMessage = chatModel.chat(messages).aiMessage();
        messages.add(aiMessage);
        System.out.println(aiMessage.text());
    }
}
