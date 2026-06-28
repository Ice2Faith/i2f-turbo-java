package i2f.spring.ai.model;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;

/**
 * @author Ice2Faith
 * @date 2026/3/27 10:32
 * @desc
 */
public class SpringAiOpenAiModel extends SpringAiModel {

    protected SpringAiOpenAiModel(ChatModel model) {
        super(model);
    }

    public static OpenAiBuilder openai() {
        return new OpenAiBuilder();
    }

    public static class OpenAiBuilder {
        protected String baseUrl;
        protected String apiKey;
        protected String model;

        public OpenAiBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public OpenAiBuilder apiKey(String apikey) {
            this.apiKey = apikey;
            return this;
        }

        public OpenAiBuilder model(String model) {
            this.model = model;
            return this;
        }


        public SpringAiModel build() {
            OpenAiChatOptions.Builder optionsBuilder = OpenAiChatOptions.builder();

            if (baseUrl != null && !baseUrl.isEmpty()) {
                optionsBuilder.baseUrl(baseUrl);
            }
            if (apiKey != null && !apiKey.isEmpty()) {
                optionsBuilder.apiKey(apiKey);
            }

            optionsBuilder.n(1);
            if (model != null && !model.isEmpty()) {
                optionsBuilder.model(model);
            }

            OpenAiChatModel chatModel = OpenAiChatModel.builder()
                    .options(optionsBuilder.build())
                    .build();
            return new SpringAiOpenAiModel(chatModel);
        }
    }

}
