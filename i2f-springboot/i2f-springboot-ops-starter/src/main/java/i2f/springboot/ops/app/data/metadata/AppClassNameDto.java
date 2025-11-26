package i2f.springboot.ops.app.data.metadata;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/26 22:55
 * @desc
 */
@Data
@NoArgsConstructor
public class AppClassNameDto {
    protected String className;
    protected String simpleName;
    public static AppClassNameDto of(Class<?> clazz) {
        AppClassNameDto dto = new AppClassNameDto();
        dto.className = clazz.getName();
        dto.simpleName = clazz.getSimpleName();
        return dto;
    }
}
