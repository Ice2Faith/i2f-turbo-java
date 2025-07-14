package i2f.springboot3.openapi.test;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/7/14 14:16
 */
@Schema(name = "test-pojo", description = "测试对象")
@Data
@NoArgsConstructor
public class TestModel {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "名称")
    private String name;
}
