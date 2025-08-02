package i2f.template.render.core.impl;

import i2f.os.OsUtil;
import i2f.template.render.RegexGenerator;
import i2f.template.render.core.IGenerate;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2021/10/28
 */
@Data
public class CmdGenerate implements IGenerate {
    public Function<Object, String> mapper;
    public Object root;
    public Object data;
    public List<String> basePackages;
    public String command;
    public String show;
    public String charset;

    @Override
    public String gen() {
        String cmdLine = getCmdline();
        boolean isShow = true;
        if (show != null) {
            if ("true".equals(show)) {
                isShow = true;
            }
            if ("false".equals(show)) {
                isShow = false;
            }
        }
        if (!isShow) {
            return "";
        }
        if (charset == null || "".equals(charset)) {
            charset = "UTF-8";
        }
        return getCmdlineExecuteResult(cmdLine, charset);
    }

    private String getCmdlineExecuteResult(String cmdLine, String charset) {
        try {
            String rs = OsUtil.runCmd(cmdLine, charset);
            return rs;
        } catch (Exception e) {

        }
        return "";
    }

    public String getCmdline() {
        Map<String, Object> param = new HashMap<>(16);
        param.put("_root", root);
        param.put("_item", data);
        Map<String, Object> ctx = new HashMap<>(16);

        param.put("_ctx", ctx);
        String cmdLine = RegexGenerator.render(command, param, mapper, basePackages);
        return cmdLine;
    }

}
