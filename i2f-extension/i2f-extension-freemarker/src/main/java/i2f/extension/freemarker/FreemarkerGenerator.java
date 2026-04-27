package i2f.extension.freemarker;

import i2f.extension.freemarker.GeneratorTool;
import i2f.io.stream.StreamUtil;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Ice2Faith
 * @date 2026/4/27 14:51
 * @desc
 */
public class FreemarkerGenerator {
    /**
     * 批量渲染整个目录模板
     * 说明：
     * 模板文件名，以.ftl为后缀，输出文件名会自动去除这个后缀
     * 例如：
     * LoginController.java.ftl
     * 则输出文件名为：
     * LoginController.java
     * 参数json文件，需要是一个JSON格式的合法对象
     * 输出路径，输出文件树和模板文件树结构一致
     * 模板文件特殊标记识别：
     * 模板文件第一行：
     * #filename 指定本模板渲染之后的文件名，不包含后缀，后缀按照模板文件后缀
     * 例如：
     * #filename ${tableName}Controller
     * 模板文件名为：
     * Controller.java.ftl
     * 参数中tableName的值为User
     * 则输出文件名为：
     * UserController.java
     * 没有此标记或不在第一行，按照默认规则进行
     *
     * @param templatePath 模板文件路径
     * @param params       渲染参数
     * @param outputPath   输出文件路径
     */
    public static void batchRender(String templatePath, Map<String, Object> params, String outputPath, String charset) throws IOException {
        File ipath = new File(templatePath);
        if (!ipath.exists()) {
            return;
        }

        if (ipath.isFile()) {
            ipath = ipath.getParentFile();
        }
        File[] files = ipath.listFiles();
        for (File item : files) {
            if (item.isFile()) {
                String name = item.getName();
                if (name.endsWith(".ftl")) {
                    String tpl = StreamUtil.readString(item, charset);
                    String rs = render(tpl, params);
                    String sname = name.substring(0, name.length() - ".ftl".length());
                    int idx = rs.indexOf("\n");
                    if (idx >= 0) {
                        String fileNameLine = rs.substring(0, idx);
                        if (rs.startsWith("#filename ")) {
                            rs = rs.substring(idx + 1);
                            String fileName = fileNameLine.substring("#filename ".length());
                            fileName = fileName.trim();
                            if (!"".equals(fileName)) {
                                int pidx = sname.lastIndexOf(".");
                                String suffix = "";
                                if (pidx >= 0) {
                                    suffix = sname.substring(pidx);
                                }
                                sname = fileName + suffix;
                            }
                        }
                    }
                    File sfile = new File(outputPath, sname);
                    if (!sfile.getParentFile().exists()) {
                        sfile.getParentFile().mkdirs();
                    }
                    StreamUtil.writeString(rs, charset, sfile);
                }
            } else {
                String nextTplPath = item.getAbsolutePath();
                String nextOutputPath = new File(outputPath, item.getName()).getAbsolutePath();
                batchRender(nextTplPath, params, nextOutputPath, charset);
            }
        }
    }

    /**
     * 通过模板字符串通过渲染参数进行渲染，并返回结果
     * 实际上是借助临时文件达到目的
     *
     * @param template
     * @param params
     * @return
     * @throws IOException
     */
    public static String render(String template, Map<String, Object> params) throws IOException {
        return renderByStringResource(null, template, params);
    }

    /**
     * 模板引擎使用config配置加载是否在classpath下isInClassPath的模板文件fileName使用params参数绑定进行渲染
     * 并返回渲染结果字符串
     *
     * @param config        模板引擎配置
     * @param isInClassPath 模板是否在classpath下
     * @param fileName      模板文件（在classpath下时，写classpath的相对路径，不在时写完整路径）
     * @param params        渲染参数
     * @return
     */
    public static String renderByFileResource(Properties config, boolean isInClassPath, String fileName, Map<String, Object> params) throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_34);
        configuration.setEncoding(Locale.CHINA, StandardCharsets.UTF_8.name());
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setOutputEncoding(StandardCharsets.UTF_8.name());

        configuration.setDateFormat("yyyy-MM-dd");
        configuration.setTimeFormat("HH:mm:ss");
        configuration.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");


        TemplateLoader loader = null;
        String templateName = fileName;
        if (isInClassPath) {
            loader = new ClassTemplateLoader(FreemarkerGenerator.class, "");
        } else {
            File file = new File(fileName);
            loader = new FileTemplateLoader(file.getParentFile());
            templateName = file.getName();
        }
        configuration.setTemplateLoader(loader);

        try {
            if (config != null) {
                configuration.setSettings(config);
            }

            Template tpl = configuration.getTemplate(templateName);

            StringWriter writer = new StringWriter();

            params.put("_vm", new GeneratorTool());
            tpl.process(params, writer);

            return writer.toString();
        } catch (TemplateException e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            params.remove("_vm");
        }
    }

    public static String renderByStringResource(Properties config, String template, Map<String, Object> params) throws IOException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_34);
        configuration.setEncoding(Locale.CHINA, StandardCharsets.UTF_8.name());
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setOutputEncoding(StandardCharsets.UTF_8.name());

        configuration.setDateFormat("yyyy-MM-dd");
        configuration.setTimeFormat("HH:mm:ss");
        configuration.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");

        String id = UUID.randomUUID().toString().replace("-", "");

        StringTemplateLoader loader = new StringTemplateLoader();
        loader.putTemplate(id, template);
        configuration.setTemplateLoader(loader);

        try {
            if (config != null) {
                configuration.setSettings(config);
            }

            Template tpl = configuration.getTemplate(id);

            StringWriter writer = new StringWriter();

            params.put("_vm", new GeneratorTool());
            tpl.process(params, writer);

            return writer.toString();
        } catch (TemplateException e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            params.remove("_vm");
            loader.removeTemplate(id);
        }
    }


}
