package i2f.springboot.ops.openai.util;

import i2f.springboot.ops.openai.data.OpenAiOperateDto;
import i2f.springboot.ops.openai.properties.OpenAiOpsProperties;

/**
 * @author Ice2Faith
 * @date 2026/9/28 20:58
 * @desc
 */
public class OpenAiOptionsUtil {
    public static OpenAiOpsProperties.OpenAiOptions getOrDefaultEndpoint(OpenAiOperateDto req, OpenAiOpsProperties properties) {
        if (req != null) {
            OpenAiOpsProperties.OpenAiOptions ret = new OpenAiOpsProperties.OpenAiOptions();
            ret.setEnable(true);
            ret.setModel(req.getCompletion().getModel());
            ret.setBaseUrl(req.getMeta().getBaseUrl());
            ret.setApiKey(req.getMeta().getApiKey());
            return ret;
        }
        if (properties != null && properties.getDefaultEndpoint() != null) {
            OpenAiOpsProperties.OpenAiOptions endpoint = properties.getDefaultEndpoint();
            OpenAiOpsProperties.OpenAiOptions ret = new OpenAiOpsProperties.OpenAiOptions();
            ret.setEnable(endpoint.isEnable());
            ret.setModel(endpoint.getModel());
            ret.setBaseUrl(endpoint.getBaseUrl());
            ret.setApiKey(endpoint.getApiKey());
            return ret;
        }
        return null;
    }
}
