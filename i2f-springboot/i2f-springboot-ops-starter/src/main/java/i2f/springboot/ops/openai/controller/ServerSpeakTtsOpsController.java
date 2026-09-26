package i2f.springboot.ops.openai.controller;

import i2f.springboot.ops.common.OpsConsts;
import i2f.springboot.ops.common.OpsSecureDto;
import i2f.springboot.ops.common.OpsSecureReturn;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.openai.data.OpenAiOperateDto;
import i2f.springboot.ops.openai.tts.cmd.CommandTtsSpeaker;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/9/16 19:28
 * @desc
 */
@ConditionalOnExpression("${ai.tts.server-speak.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping(OpsConsts.SPEL_BASE_URL + "/open-ai")
public class ServerSpeakTtsOpsController {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    protected ApplicationContext applicationContext;

    @PostMapping("/tts/server/speak")
    @ResponseBody
    public OpsSecureReturn<OpsSecureDto> speak(@RequestBody OpsSecureDto reqDto) throws Exception {
        try {
            OpenAiOperateDto req = transfer.recv(reqDto, OpenAiOperateDto.class);
            String ttsContent = req.getTtsContent();

            List<CommandTtsSpeaker> list = new ArrayList<>();

            try {
                String[] names = applicationContext.getBeanNamesForType(CommandTtsSpeaker.class);
                for (String name : names) {
                    CommandTtsSpeaker speaker = (CommandTtsSpeaker) applicationContext.getBean(name);
                    if (speaker.available()) {
                        list.add(speaker);
                    }
                }
            } catch (Exception e) {
                // ignore
            }

            Comparator<CommandTtsSpeaker> comparing = Comparator.comparing(CommandTtsSpeaker::prior);
            list.sort(comparing.reversed());

            boolean resolved = false;
            for (CommandTtsSpeaker speaker : list) {
                speaker.speak(ttsContent);
                resolved = true;
            }

            return transfer.success(resolved);
        } catch (Throwable e) {
            log.warn(e.getMessage(), e);
            return transfer.error(e);
        }
    }

}
