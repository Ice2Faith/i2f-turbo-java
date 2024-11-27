package i2f.spring.mvc.metadata.module;

import i2f.spring.mvc.metadata.api.ApiMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/5/13 22:12
 * @desc
 */
@Data
@NoArgsConstructor
public class ModuleController {
    private Class<?> clazz;
    private String fullClassName;
    private String className;
    private String trimName;
    private String comment;
    private String remark;
    private String baseUrl;
    private List<ApiMethod> methods;
}
