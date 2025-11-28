package i2f.springboot.ops.app.data.metadata;

import i2f.springboot.ops.app.util.AppUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/26 22:42
 * @desc
 */
@Data
@NoArgsConstructor
public class AppMethodDto {
    protected List<String> modifiers;
    protected AppClassNameDto returns;
    protected String name;
    protected List<AppFieldDto> parameterList;
    protected AppClassNameDto declareClass;

    public static AppMethodDto of(Method method) {
        AppMethodDto dto = new AppMethodDto();
        dto.modifiers = AppUtil.resolveModifier(method.getModifiers());
        dto.returns = AppClassNameDto.of(method.getReturnType());
        dto.name = method.getName();
        dto.parameterList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            dto.parameterList.add(AppFieldDto.of(parameter));
        }
        dto.declareClass = AppClassNameDto.of(method.getDeclaringClass());
        return dto;
    }

}
