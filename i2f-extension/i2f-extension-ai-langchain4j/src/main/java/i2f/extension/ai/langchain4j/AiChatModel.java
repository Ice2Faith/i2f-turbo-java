package i2f.extension.ai.langchain4j;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Ice2Faith
 * @date 2025/5/10 15:52
 */
public class AiChatModel {
    protected String baseUrl;
    protected String apiKey;
    protected String modelName;
    protected String system;
    protected List<ChatMessage> messages=new ArrayList<>();
    protected StreamingChatModel chatModel;

    protected static class Builder{
        protected AiChatModel model;
        protected Builder(AiChatModel model){
            this.model=model;
        }
        public AiChatModel build(){
            OpenAiStreamingChatModel.OpenAiStreamingChatModelBuilder builder = OpenAiStreamingChatModel.builder();
            if(model.baseUrl!=null){
                builder.baseUrl(model.baseUrl);
            }
            if(model.apiKey!=null){
                builder.apiKey(model.apiKey);
            }
            if(model.modelName!=null){
                builder.modelName(model.modelName);
            }
            if(model.system!=null){
                model.messages.add(new SystemMessage(model.system));
            }
            model.chatModel= builder.build();
            return model;
        }

        public Builder baseUrl(String baseUrl){
            model.baseUrl=baseUrl;
            return this;
        }
        public Builder apiKey(String apiKey){
            model.apiKey=apiKey;
            return this;
        }
        public Builder modelName(String modelName){
            model.modelName=modelName;
            return this;
        }
        public Builder system(String system){
            model.system=system;
            return this;
        }
    }


    public static Builder builder(){
        return new Builder(new AiChatModel());
    }

    public String chat(String message){
        messages.add(new UserMessage(message));
        AtomicReference<String> ref=new AtomicReference<>();
        CountDownLatch latch=new CountDownLatch(1);
        chatModel.chat(messages, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String s) {

            }

            @Override
            public void onCompleteResponse(ChatResponse chatResponse) {
                AiMessage aiMessage = chatResponse.aiMessage();
                messages.add(aiMessage);
                ref.set(aiMessage.text());
                latch.countDown();
            }

            @Override
            public void onError(Throwable throwable) {
                latch.countDown();
            }
        });
        try{
            latch.await();
        }catch(Exception e){

        }
        return ref.get();
    }

    public void stream(String message, Consumer<String> consumer){
        messages.add(new UserMessage(message));
        chatModel.chat(messages, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String s) {
                consumer.accept(s);
            }

            @Override
            public void onCompleteResponse(ChatResponse chatResponse) {
                consumer.accept(null);
            }

            @Override
            public void onError(Throwable throwable) {
                consumer.accept(throwable.getMessage());
                consumer.accept(null);
            }
        });
    }

    public Iterator<String> stream(String message){
        messages.add(new UserMessage(message));
        LinkedBlockingQueue<String> queue=new LinkedBlockingQueue<>();
        AtomicBoolean finish=new AtomicBoolean(false);
        StreamingChatResponseHandler handler = new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String s) {
                try {
                    queue.put(s);
                } catch (InterruptedException e) {

                }
            }

            @Override
            public void onCompleteResponse(ChatResponse chatResponse) {
                try {
                    queue.put("");
                } catch (InterruptedException e) {

                }
                finish.set(true);
            }

            @Override
            public void onError(Throwable throwable) {
                try {
                    queue.put(throwable.getMessage());
                } catch (InterruptedException e) {

                }
                finish.set(true);
            }
        };
        Iterator<String> ret=new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return !finish.get() || !queue.isEmpty();
            }

            @Override
            public String next() {
                try {
                    String str = queue.take();
                    return str;
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e.getMessage(),e);
                }
            }
        };
        chatModel.chat(messages, handler);
        return ret;
    }
}
