package i2f.template.render.core.impl;

import i2f.template.render.core.IGenerate;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * @author ltb
 * @date 2021/10/28
 */
@Data
public class DatefmtGenerate implements IGenerate {
    public Function<Object, String> mapper;
    public Object root;
    public Object data;
    public List<String> basePackages;
    public String format;

    @Override
    public String gen() {
        if (data == null) {
            return mapper.apply(data);
        }
        if (format == null) {
            return mapper.apply(data);
        }
        if (data instanceof Date) {
            SimpleDateFormat fmt = new SimpleDateFormat(format);
            Date date = (Date) data;
            return fmt.format(date);
        }
        return mapper.apply(data);
    }


}
