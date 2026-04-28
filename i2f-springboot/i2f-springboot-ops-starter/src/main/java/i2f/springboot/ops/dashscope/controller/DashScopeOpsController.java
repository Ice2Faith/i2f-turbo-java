package i2f.springboot.ops.dashscope.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.springboot.ops.common.OpsSecureTransfer;
import i2f.springboot.ops.home.data.OpsHomeMenuDto;
import i2f.springboot.ops.home.provider.IOpsProvider;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/4/28 19:09
 * @desc
 */
@ConditionalOnClass(RestTemplate.class)
@Slf4j
@Data
@NoArgsConstructor
@Controller
@RequestMapping("/ops/dashscope")
public class DashScopeOpsController implements IOpsProvider {
    @Autowired
    protected OpsSecureTransfer transfer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private RestTemplate restTemplate = createRestTemplate();

    public RestTemplate createRestTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(30))
                .setReadTimeout(Duration.ofMinutes(15))
                .build();
    }

    @Override
    public List<OpsHomeMenuDto> getMenus() {
        return Collections.singletonList(new OpsHomeMenuDto()
                .title("DashScope")
                .subTitle("阿里云百炼大模型平台")
                .icon("el-icon-magic-stick")
                .href("./dashscope/index.html")
        );
    }

    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("./index.html").forward(request, response);
    }

}
