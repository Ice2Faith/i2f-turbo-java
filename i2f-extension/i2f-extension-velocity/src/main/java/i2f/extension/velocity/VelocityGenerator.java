package i2f.extension.velocity;

import i2f.extension.velocity.directives.common.*;
import i2f.extension.velocity.directives.sql.SqlSetDirective;
import i2f.extension.velocity.directives.sql.SqlWhereDirective;
import i2f.io.stream.StreamUtil;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.apache.velocity.runtime.resource.util.StringResourceRepositoryImpl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class VelocityGenerator {

    /**
     * 批量渲染整个目录模板
     * 说明：
     * 模板文件名，以.vm为后缀，输出文件名会自动去除这个后缀
     * 例如：
     * LoginController.java.vm
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
     * Controller.java.vm
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
                if (name.endsWith(".vm")) {
                    String tpl = StreamUtil.readString(item, charset);
                    String rs = render(tpl, params);
                    String sname = name.substring(0, name.length() - ".vm".length());
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
    public static String renderByFileResource(Properties config, boolean isInClassPath, String fileName, Map<String, Object> params) {
        //初始化引擎，默认从classpath加载模板文件
        VelocityEngine engine = new VelocityEngine();

        settingEngine(engine);

        String templateName = useFileResource(engine, isInClassPath, fileName);

        initEngine(config, engine);

        return renderAsString(engine, templateName, params);
    }

    protected static volatile VelocityEngine DEFAULT_STRING_ENGINE = null;

    public static VelocityEngine getDefaultStringEngine(Properties config) {
        if (config == null || config.isEmpty()) {
            if (DEFAULT_STRING_ENGINE == null) {
                synchronized (VelocityGenerator.class) {
                    if (DEFAULT_STRING_ENGINE == null) {
                        DEFAULT_STRING_ENGINE = getDefaultStringEngine0(config);
                    }
                }
            }
            return DEFAULT_STRING_ENGINE;
        } else {
            return getDefaultStringEngine0(config);
        }
    }

    public static VelocityEngine getDefaultStringEngine0(Properties config) {
        //初始化引擎，默认从classpath加载模板文件
        VelocityEngine engine = new VelocityEngine();

        settingEngine(engine);

        // 设置资源加载器为字符串资源加载器
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "string");
        engine.setProperty("resource.loader.string.class", StringResourceLoader.class.getName());
        engine.setProperty("resource.loader.string.repository.name", "stringRepo");
        engine.setProperty("resource.loader.string.repository.class", StringResourceRepositoryImpl.class.getName());

        initEngine(config, engine);

        return engine;
    }

    public static String renderByStringResource(Properties config, String template, Map<String, Object> params) {
        VelocityEngine engine = getDefaultStringEngine(config);

        String templateName = useStringResource(engine, template);

        try {
            return renderAsString(engine, templateName, params);
        } finally {
            removeStringResource(engine, templateName);
        }
    }

    public static void removeStringResource(VelocityEngine engine, String templateName) {
        // 创建并配置字符串资源仓库
        StringResourceRepository repo = (StringResourceRepository) engine.getApplicationAttribute("stringRepo");
        if (repo == null) {
            repo = StringResourceLoader.getRepository();
            engine.setApplicationAttribute("stringRepo", repo);
        }
        repo.removeStringResource(templateName);
    }

    public static String useStringResource(VelocityEngine engine, String template) {
        // 创建并配置字符串资源仓库
        StringResourceRepository repo = (StringResourceRepository) engine.getApplicationAttribute("stringRepo");
        if (repo == null) {
            repo = StringResourceLoader.getRepository();
            engine.setApplicationAttribute("stringRepo", repo);
        }
        String templateName = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        repo.putStringResource(templateName, template);

        return templateName;
    }

    public static String useFileResource(VelocityEngine engine, boolean isInClassPath, String fileName) {
        String templateName = fileName;
        if (isInClassPath) {
            engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        } else {
            File file = new File(fileName);
            engine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, file.getParentFile().getAbsolutePath());
            templateName = file.getName();
        }
        return templateName;
    }

    public static String renderAsString(VelocityEngine engine, String templateName, Map<String, Object> params) {
        //创建模板对象
        Template template = engine.getTemplate(templateName);

        //创建绑定对象
        VelocityContext ctx = new VelocityContext(params);
        ctx.put("_vm", new GeneratorTool());

        //创建写出对象
        StringWriter writer = new StringWriter();

        //渲染结果
        template.merge(ctx, writer);

        return writer.toString();
    }

    public static void settingEngine(VelocityEngine engine) {
        engine.loadDirective(TrimDirective.class.getName());
        engine.loadDirective(ReplaceAllDirective.class.getName());
        engine.loadDirective(SqlWhereDirective.class.getName());
        engine.loadDirective(SqlSetDirective.class.getName());
        engine.loadDirective(RichForDirective.class.getName());
        engine.loadDirective(ForiDirective.class.getName());
        engine.loadDirective(ScriptDirective.class.getName());

        engine.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        engine.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        engine.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
    }

    public static void initEngine(Properties config, VelocityEngine engine) {
        //有指定配置则使用配置覆盖
        if (config == null || config.isEmpty()) {
            engine.init();
        } else {
            engine.init(config);
        }
    }

}

