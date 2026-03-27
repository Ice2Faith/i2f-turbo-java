package i2f.extension.ai.langchain4j.model;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

/**
 * @author Ice2Faith
 * @date 2026/3/27 10:32
 * @desc
 */
public class Langchain4jOpenAiModel extends Langchain4jModel {

    protected Langchain4jOpenAiModel(ChatModel model) {
        super(model);
    }

    public static OpenAiBuilder openai() {
        return new OpenAiBuilder();
    }

    public static class OpenAiBuilder {
        protected OpenAiChatModel.OpenAiChatModelBuilder inner = OpenAiChatModel.builder();

        public OpenAiBuilder baseUrl(String baseUrl) {
            if (baseUrl != null && !baseUrl.isEmpty()) {
                inner.baseUrl(baseUrl);
            }
            return this;
        }

        public OpenAiBuilder apiKey(String apikey) {
            if (apikey != null && !apikey.isEmpty()) {
                inner.apiKey(apikey);
            }
            return this;
        }

        public OpenAiBuilder model(String model) {
            if (model != null && !model.isEmpty()) {
                inner.modelName(model);
            }
            return this;
        }


        public Langchain4jModel build() {
            return new Langchain4jOpenAiModel(inner.build());
        }
    }

}
