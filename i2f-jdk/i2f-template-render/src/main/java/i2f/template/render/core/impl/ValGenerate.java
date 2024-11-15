package i2f.template.render.core.impl;


import i2f.reflect.ReflectResolver;
import i2f.template.render.core.IGenerate;
import lombok.Data;

import java.util.List;
import java.util.function.Function;

/**
 * @author ltb
 * @date 2021/10/28
 */
@Data
public class ValGenerate implements IGenerate {
    public Function<Object, String> mapper;
    public Object root;
    public Object data;
    public List<String> basePackages;
    public String userMapper;

    @Override
    public String gen() {
        Function<Object, String> currentMapper = getCurrentMapper();
        return currentMapper.apply(data);
    }

    private Function<Object, String> getCurrentMapper() {
        Function<Object, String> ret = this.mapper;
        if (userMapper != null && !"".equals(userMapper)) {
            try {
                Class clazz = ReflectResolver.loadClass(this.userMapper);
                if (clazz != null) {
                    ret = (Function<Object, String>) ReflectResolver.getInstance(clazz);
                }
            } catch (Exception e) {

            }
        }
        return ret;
    }
}
