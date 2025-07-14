package i2f.springboot3.openapi.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ice2Faith
 * @date 2025/7/14 14:09
 */
@Tag(name = "test-module", description = "测试模块")
@RestController("/api/test")
public class TestController {

    @Operation(summary = "get version", description = "获取版本")
    @GetMapping("/version")
    public Object getVersion(@Parameter(name = "from", description = "请求来源", required = false, example = "api")
                             @RequestParam(value = "from", required = false, defaultValue = "api") String from) {
        return "version: v1 " + ", from " + from;
    }

}
