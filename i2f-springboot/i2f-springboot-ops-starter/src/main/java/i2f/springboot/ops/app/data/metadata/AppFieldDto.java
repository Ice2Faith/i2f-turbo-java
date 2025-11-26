package i2f.springboot.ops.app.data.metadata;

import i2f.springboot.ops.app.util.AppUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/26 22:41
 * @desc
 */
@Data
@NoArgsConstructor
public class AppFieldDto {
    protected List<String> modifiers;
    protected String name;
    protected AppClassNameDto type;
    public static AppFieldDto of(Field field) {
        AppFieldDto dto = new AppFieldDto();
        dto.modifiers= AppUtil.resolveModifier(field.getModifiers());
        dto.name= field.getName();
        dto.type= AppClassNameDto.of(field.getType());
        return dto;
    }
    public static AppFieldDto of(Parameter field) {
        AppFieldDto dto = new AppFieldDto();
        dto.modifiers= AppUtil.resolveModifier(field.getModifiers());
        dto.name= field.getName();
        dto.type= AppClassNameDto.of(field.getType());
        return dto;
    }
}
