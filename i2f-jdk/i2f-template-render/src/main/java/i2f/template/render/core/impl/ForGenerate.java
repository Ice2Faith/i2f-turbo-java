package i2f.template.render.core.impl;

import i2f.template.render.RegexGenerator;
import i2f.template.render.core.IGenerate;
import lombok.Data;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2021/10/20
 */
@Data
public class ForGenerate implements IGenerate {
    public Function<Object, String> mapper;
    public Object root;
    public Object data;
    public String separator = ",";
    public String prefix;
    public String suffix;
    public String template;
    public String blank;
    public String jump;
    public List<String> basePackages;

    @Override
    public String gen() {
        if (data == null) {
            return mapper.apply(data);
        }
        Class<?> clazz = this.data.getClass();
        if (clazz.isArray()) {
            int len = Array.getLength(this.data);
            List list = new ArrayList<>(len);
            for (int i = 0; i < len; i++) {
                Object val = Array.get(this.data, i);
                list.add(val);
            }
            return proxyIterable(list);
        } else if (this.data instanceof Iterable) {
            return proxyIterable((Iterable) this.data);
        } else if (this.data instanceof Map) {
            Map map = (Map) this.data;
            Set<Map.Entry> vals = map.entrySet();
            return proxyIterable(vals);
        } else {
            return mapper.apply(this.data);
        }
    }

    private String proxyIterable(Iterable data) {
        if (template != null) {
            String tpl = template.trim();
            if ("".equals(tpl)) {
                template = null;
            }
        }

        if (blank != null) {
            String blk = blank.trim().toLowerCase();
            if ("true".equals(blk)) {
                Iterator it = data.iterator();
                if (!it.hasNext()) {
                    return "";
                }
            }

        }

        StringBuilder builder = new StringBuilder(256);
        if (prefix != null) {
            builder.append(prefix);
        }

        boolean isJump = false;
        if (jump != null) {
            jump = jump.trim();
            if ("true".equals(jump)) {
                isJump = true;
            }
            if ("false".equals(jump)) {
                isJump = false;
            }
        }

        int idx = 0;
        boolean isFirst = true;
        Iterator it = data.iterator();
        while (it.hasNext()) {

            Object val = it.next();
            String str = "";
            if (template != null) {
                Map<String, Object> param = new HashMap<>(16);
                param.put("_item", val);
                param.put("_root", root);
                Map<String, Object> ctx = new HashMap<>(16);
                ctx.put("first", isFirst);
                ctx.put("last", !it.hasNext());
                ctx.put("index", idx);

                param.put("_ctx", ctx);
                str = RegexGenerator.render(template, param, mapper, basePackages);
            } else {
                str = mapper.apply(val);
            }
            boolean isLegal = false;
            if (!isJump) {
                isLegal = true;
            } else {
                str = str.trim();
                if (!"".equals(str)) {
                    isLegal = true;
                }
            }
            if (isLegal) {
                if (!isFirst) {
                    if (separator != null) {
                        builder.append(separator);
                    }
                }
                builder.append(str);
                isFirst = false;
            }
            idx++;
        }

        if (suffix != null) {
            builder.append(suffix);
        }
        return builder.toString();
    }
}
