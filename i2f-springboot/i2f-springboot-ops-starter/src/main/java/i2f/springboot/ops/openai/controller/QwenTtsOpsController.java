package i2f.springboot.ops.openai.controller;

import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.openai.data.OpenAiOperateDto;
import i2f.springboot.ops.openai.tool.impl.TmpFileTools;
import i2f.springboot.ops.openai.tts.QwenAudioTtsWebSocket;
import i2f.springboot.ops.openai.tts.QwenTtsProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/7/27 11:24
 * @desc
 */
@ConditionalOnExpression("${ai.tts.qwen.enable:true}")
@EnableConfigurationProperties({
        QwenTtsProperties.class
})
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/open-ai")
public class QwenTtsOpsController {
    @Autowired
    protected OpsSecureTransfer transfer;


    @Autowired
    private QwenTtsProperties qwenTtsProperties;

    @Autowired(required = false)
    private TmpFileTools tmpFileTools;

    @PostMapping("/tts/qwen/generate")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> qwenTts(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            OpenAiOperateDto req = transfer.recv(reqDto, OpenAiOperateDto.class);

            String ttsContent = req.getTtsContent();
            List<String> inputs = Arrays.asList(ttsContent.split("\n+"));
            QwenAudioTtsWebSocket socket = QwenAudioTtsWebSocket.create(qwenTtsProperties.getUrl(),
                    qwenTtsProperties.getApiKey(),
                    inputs);
            String model = qwenTtsProperties.getModel();
            if (model != null && !model.isEmpty()) {
                socket.setModel(model);
            }

            String voice = qwenTtsProperties.getVoice();
            if (voice != null && !voice.isEmpty()) {
                socket.setVoice(voice);
            }

            File mp3File = socket.start()
                    .await();

            Map<String, Object> metadata = tmpFileTools.saveFile(new FileInputStream(mp3File), mp3File.getName());

            return transfer.success(metadata);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

}
