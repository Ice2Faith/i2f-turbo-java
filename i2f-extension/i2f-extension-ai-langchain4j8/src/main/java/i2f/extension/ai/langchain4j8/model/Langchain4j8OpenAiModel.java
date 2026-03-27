package i2f.extension.ai.langchain4j8.model;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

/**
 * @author Ice2Faith
 * @date 2026/3/27 10:32
 * @desc
 */
public class Langchain4j8OpenAiModel extends Langchain4j8Model {

    protected Langchain4j8OpenAiModel(ChatLanguageModel model) {
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


        public Langchain4j8Model build() {
            return new Langchain4j8OpenAiModel(inner.build());
        }
    }

}
