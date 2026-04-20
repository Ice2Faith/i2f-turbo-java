package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import i2f.match.regex.RegexUtil;
import i2f.turbo.idea.plugin.jdbc.procedure.completion.CompletionHelper;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2025/3/1 19:01
 * @desc
 */
final class JdbcProcedureXmlLangInjectInjector implements MultiHostInjector {
    public static final Logger log = Logger.getInstance(JdbcProcedureXmlLangInjectInjector.class);
    public static final String LANG_JAVA_ENUM_CLASS_NAME = "InjectLangEnum";
    public static volatile String LANG_JAVA_ENUM = "public static enum " + LANG_JAVA_ENUM_CLASS_NAME + " {" +
            "JAVA,SQL,VTL" +
            "};";
    public static final ConcurrentHashMap<String, Language> LANG_JAVA_ENUM_MAP = new ConcurrentHashMap<>();
    public static volatile String JS_INJECT_PREFIX = "let executor={};let params={};";

    public static final AtomicBoolean initRefreshThread = new AtomicBoolean(false);

    static {
        initInjectPrefix();
        startRefreshThread();
    }

    public static void initInjectPrefix() {
        URL url = null;
        if (url == null) {
            try {
                url = JdbcProcedureXmlLangInjectInjector.class.getResource("/assets/JsInject.js");
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        }
        if (url == null) {
            try {
                url = Thread.currentThread().getContextClassLoader().getResource("/assets/JsInject.js");
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            JS_INJECT_PREFIX = builder.toString();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }

    }

    public static void startRefreshThread() {
        if (initRefreshThread.getAndSet(true)) {
            return;
        }
        Thread thread = new Thread(() -> {
            do {
                refreshLangTask();
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {

                }
            } while (true);
        });
        thread.setName("xproc4j-lang-injector");
        thread.setDaemon(true);
        thread.start();
    }

    public static void refreshLangTask() {
        Collection<Language> langs = Language.getRegisteredLanguages();
        for (Language lang : langs) {
            String id = lang.getID();
            if (id == null || id.isEmpty()) {
                continue;
            }
            if (!id.matches("[a-zA-Z0-9\\-\\_ ]+")) {
                continue;
            }
            id = id.replaceAll(" ", "_");
            id = id.replaceAll("-", "_");
            LANG_JAVA_ENUM_MAP.put(id, lang);
        }
        StringBuilder builder = new StringBuilder();
        builder.append("public static enum ").append(LANG_JAVA_ENUM_CLASS_NAME).append(" {");
        boolean isFirst = true;
        for (Map.Entry<String, Language> entry : LANG_JAVA_ENUM_MAP.entrySet()) {
            if (!isFirst) {
                builder.append(",");
            }
            builder.append(entry.getKey());
            isFirst = false;
        }
        builder.append("}");
        LANG_JAVA_ENUM = builder.toString();
    }

    public static final String[] JAVA_LANG_TAGS = {
            "lang-eval",
            "lang-eval-java",
            "lang-java-import",
            "lang-java-member",
            "lang-java-body"
    };

    public static final String[] SQL_LANG_TAGS = {
            "sql-query-row",
            "sql-query-list",
            "sql-query-object",
            "sql-update",
            "sql-dialect",
            "sql-cursor",
            "sql-etl"
    };

    public static final String EVAL_JAVA_IMPORTS_TEMPLATE = "\n" +

            "\n" +
            "import i2f.bindsql.BindSql;\n" +
            "import i2f.bindsql.count.CountWrappers;\n" +
            "import i2f.bindsql.count.ICountWrapper;\n" +
            "import i2f.bindsql.page.IPageWrapper;\n" +
            "import i2f.bindsql.page.PageWrappers;\n" +
            "import i2f.clock.SystemClock;\n" +
            "import i2f.context.impl.ListableNamingContext;\n" +
            "import i2f.context.std.INamingContext;\n" +
            "import i2f.convert.obj.ObjectConvertor;\n" +
            "import i2f.database.type.DatabaseType;\n" +
            "import i2f.environment.impl.ListableDelegateEnvironment;\n" +
            "import i2f.environment.std.IEnvironment;\n" +
            "import i2f.invokable.method.IMethod;\n" +
            "import i2f.jdbc.JdbcResolver;\n" +
            "import i2f.jdbc.data.QueryColumn;\n" +
            "import i2f.jdbc.data.QueryResult;\n" +
            "import i2f.jdbc.procedure.annotations.JdbcProcedureComponent;\n" +
            "import i2f.jdbc.procedure.annotations.JdbcProcedureFunction;\n" +
            "import i2f.jdbc.procedure.consts.*;\n" +
            "import i2f.jdbc.procedure.context.ContextHolder;\n" +
            "import i2f.jdbc.procedure.context.JdbcProcedureContext;\n" +
            "import i2f.jdbc.procedure.context.ProcedureMeta;\n" +
            "import i2f.jdbc.procedure.context.impl.DefaultJdbcProcedureContext;\n" +
            "import i2f.jdbc.procedure.datasource.DataSourceProvider;\n" +
            "import i2f.jdbc.procedure.datasource.impl.NamingContextDataSourceProvider;\n" +
            "import i2f.jdbc.procedure.event.XProc4jEvent;\n" +
            "import i2f.jdbc.procedure.event.XProc4jEventHandler;\n" +
            "import i2f.jdbc.procedure.event.XProc4jEventListener;\n" +
            "import i2f.jdbc.procedure.event.impl.ContextXProc4jEventHandler;\n" +
            "import i2f.jdbc.procedure.executor.*;\n" +
            "import i2f.jdbc.procedure.executor.event.*;\n" +
            "import i2f.jdbc.procedure.log.JdbcProcedureLogger;\n" +
            "import i2f.jdbc.procedure.log.impl.DefaultJdbcProcedureLogger;\n" +
            "import i2f.jdbc.procedure.node.ExecutorNode;\n" +
            "import i2f.jdbc.procedure.node.event.XmlExecUseTimeEvent;\n" +
            "import i2f.jdbc.procedure.node.impl.*;\n" +
            "import i2f.jdbc.procedure.parser.JdbcProcedureParser;\n" +
            "import i2f.jdbc.procedure.parser.data.XmlNode;\n" +
            "import i2f.jdbc.procedure.reportor.GrammarReporter;\n" +
            "import i2f.jdbc.procedure.script.EvalScriptProvider;\n" +
            "import i2f.jdbc.procedure.signal.SignalException;\n" +
            "import i2f.jdbc.procedure.signal.impl.ControlSignalException;\n" +
            "import i2f.jdbc.procedure.signal.impl.NotFoundSignalException;\n" +
            "import i2f.jdbc.procedure.signal.impl.ThrowSignalException;\n" +
            "import i2f.jdbc.procedure.util.JdbcProcedureUtil;\n" +
            "import i2f.jdbc.proxy.xml.mybatis.data.MybatisMapperNode;\n" +
            "import i2f.jdbc.proxy.xml.mybatis.inflater.MybatisMapperInflater;\n" +
            "import i2f.jdbc.proxy.xml.mybatis.parser.MybatisMapperParser;\n" +
            "import i2f.lock.ILockProvider;\n" +
            "import i2f.lru.CachedSupplier;\n" +
            "import i2f.lru.LruMap;\n" +
            "import i2f.lru.WeakStackRetrieveCacheProvider;\n" +
            "import i2f.page.ApiOffsetSize;\n" +
            "import i2f.reference.Reference;\n" +
            "import i2f.reflect.ReflectResolver;\n" +
            "import i2f.reflect.vistor.Visitor;\n" +
            "import i2f.text.StringUtils;\n" +
            "import i2f.typeof.TypeOf;\n" +
            "import i2f.uid.SnowflakeLongUid;\n" +
            "\n" +
            "import javax.sql.DataSource;\n" +
            "import java.io.ByteArrayOutputStream;\n" +
            "import java.io.PrintStream;\n" +
            "import java.sql.Connection;\n" +
            "import java.sql.SQLException;\n" +
            "import java.text.SimpleDateFormat;\n" +
            "import java.time.Duration;\n" +
            "import java.time.LocalDate;\n" +
            "import java.time.LocalDateTime;\n" +
            "import java.time.LocalTime;\n" +
            "import java.time.format.DateTimeFormatter;\n" +
            "import java.util.*;\n" +
            "import java.util.concurrent.ConcurrentHashMap;\n" +
            "import java.util.concurrent.CopyOnWriteArrayList;\n" +
            "import java.util.concurrent.CopyOnWriteArraySet;\n" +
            "import java.util.concurrent.TimeUnit;\n" +
            "import java.util.concurrent.atomic.AtomicBoolean;\n" +
            "import java.util.concurrent.atomic.AtomicInteger;\n" +
            "import java.util.concurrent.atomic.AtomicLong;\n" +
            "import java.util.concurrent.atomic.AtomicReference;\n" +
            "import java.util.concurrent.locks.ReentrantLock;\n" +
            "import java.util.function.Consumer;\n" +
            "import java.util.function.Function;\n" +
            "import java.util.function.Supplier;\n";


    public static String getEvalJavaImports(Project project) {
        String pkg = CompletionHelper.getBasePackage(project);
        if (pkg == null || pkg.isEmpty()) {
            return EVAL_JAVA_IMPORTS_TEMPLATE;
        }
        if (!pkg.endsWith(".")) {
            pkg = pkg + ".";
        }
        return EVAL_JAVA_IMPORTS_TEMPLATE.replace("i2f.", pkg);
    }


    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar,
                                     @NotNull PsiElement context) {
        if (context instanceof XmlText) {
            XmlText xmlText = (XmlText) context;

            injectXmlText(registrar, xmlText);
        } else if (context instanceof XmlAttribute) {
            XmlAttribute attr = (XmlAttribute) context;

            injectXmlAttribute(registrar, attr);
        }

    }

    @Override
    public @NotNull
    List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return List.of(XmlText.class, XmlAttribute.class);
    }

    public Language detectLanguage(XmlText text) {
        if (text == null) {
            return null;
        }
        return detectLanguage(text.getParentTag());
    }

    public boolean isTagOfType(String name, String type) {
        return name.equals(type)
                || name.startsWith(type + "-")
                || name.endsWith("-" + type);
    }

    public Language findPossibleTagNameLanguage(String tagName) {
        if (tagName == null) {
            return null;
        }
        String name = tagName.toLowerCase();
        Language ret = findPossibleLanguage(name);
        if (ret != null) {
            return ret;
        }
        int idx = name.indexOf("-");
        if (idx >= 0) {
            String prefix = name.substring(0, idx);
            ret = findPossibleLanguage(prefix);
            if (ret != null) {
                return ret;
            }
        }
        idx = name.lastIndexOf("-");
        if (idx >= 0) {
            String suffix = name.substring(idx + 1);
            ret = findPossibleLanguage(suffix);
            if (ret != null) {
                return ret;
            }
        }
        return ret;
    }

    public Language findPossibleLanguage(String dialect) {
        if ("sql".equalsIgnoreCase(dialect)) {
            return Language.findLanguageByID("SQL");
        }
        if ("java".equalsIgnoreCase(dialect)) {
            return Language.findLanguageByID("JAVA");
        }
        if (true) {
            Language ret = findPossibleDatabaseDialect(dialect);
            if (ret != null) {
                return ret;
            }
        }
        if ("json".equalsIgnoreCase(dialect)) {
            return Language.findLanguageByID("JSON");
        }
        if ("regex".equalsIgnoreCase(dialect)
                || "regexp".equalsIgnoreCase(dialect)) {
            return Language.findLanguageByID("RegExp");
        }
        if ("javascript".equalsIgnoreCase(dialect)
                || "js".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("JavaScript");
            if (ret != null) {
                return ret;
            }
        }
        if ("shell".equalsIgnoreCase(dialect)
                || "bash".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("Shell Script");
            if (ret != null) {
                return ret;
            }
        }
        if ("dockerfile".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("Dockerfile");
            if (ret != null) {
                return ret;
            }
        }
        if ("vue".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("Vue");
            if (ret != null) {
                return ret;
            }
        }
        if ("yaml".equalsIgnoreCase(dialect)
                || "yml".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("yaml");
            if (ret != null) {
                return ret;
            }
        }
        if ("groovy".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("Groovy");
            if (ret != null) {
                return ret;
            }
        }
        if ("markdown".equalsIgnoreCase(dialect)
                || "md".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("Markdown");
            if (ret != null) {
                return ret;
            }
        }
        if ("csv".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("CSV");
            if (ret != null) {
                return ret;
            }
        }
        if ("cron".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("CronExp");
            if (ret != null) {
                return ret;
            }
        }
        if ("expr".equalsIgnoreCase(dialect)
                || "el".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("EL");
            if (ret != null) {
                return ret;
            }
        }
        if ("spel".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("SpEL");
            if (ret != null) {
                return ret;
            }
        }
        if ("typescript".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("TypeScript");
            if (ret != null) {
                return ret;
            }
        }
        if ("thymeleaf".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("ThymeleafExpressions");
            if (ret != null) {
                return ret;
            }
        }
        if ("vtl".equalsIgnoreCase(dialect)
                || "vm".equalsIgnoreCase(dialect)
                || "velocity".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("VTL");
            if (ret != null) {
                return ret;
            }
        }
        if ("freemarker".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("InjectedFreeMarker");
            if (ret != null) {
                return ret;
            }
        }
        if ("dotenv".equalsIgnoreCase(dialect)
                || "env".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("DotEnt");
            if (ret != null) {
                return ret;
            }
        }


        if ("antlr4".equalsIgnoreCase(dialect)
                || "antlrv4".equalsIgnoreCase(dialect)
                || "antlr".equalsIgnoreCase(dialect)) {
            Language lang = Language.findLanguageByID("ANTLRv4");
            if (lang != null) {
                return lang;
            }
            lang = Language.findLanguageByID("ANTLR");
            if (lang != null) {
                return lang;
            }
        }
        if ("sass".equalsIgnoreCase(dialect)
                || "css".equalsIgnoreCase(dialect)) {
            Language lang = Language.findLanguageByID("SASS");
            if (lang != null) {
                return lang;
            }
            lang = Language.findLanguageByID("CSS");
            if (lang != null) {
                return lang;
            }
        }
        if ("redis".equalsIgnoreCase(dialect)) {
            Language lang = Language.findLanguageByID("Redis");
            if (lang != null) {
                return lang;
            }
        }

        String dialectId = dialect.replaceAll(" ", "_");
        dialectId = dialectId.replaceAll("-", "_");
        for (Map.Entry<String, Language> entry : LANG_JAVA_ENUM_MAP.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(dialectId)) {
                return entry.getValue();
            }
        }

        for (Language item : LANG_JAVA_ENUM_MAP.values()) {
            String id = item.getID();
//            log.warn("found registry lang: " + id);
            if (id == null) {
                continue;
            }
            id = id.toLowerCase();
            String[] arr = id.split("\\s+");
            id = arr[0];
            if (dialect.equals(id)
                    || dialect.startsWith(id + "-")
                    || dialect.endsWith("-" + id)) {
                return item;
            }

        }
        return null;
    }

    public Language findPossibleDatabaseDialect(String dialect) {
        if ("mariadb".equalsIgnoreCase(dialect)
                || "maria".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("MariaDB");
            if (ret != null) {
                return ret;
            }
        }
        if ("mysql".equalsIgnoreCase(dialect)
                || "gbase".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("MySQL");
            if (ret != null) {
                return ret;
            }
        }
        if ("oracle".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("Oracle");
            if (ret != null) {
                return ret;
            }
        }
        if ("posrgre".equalsIgnoreCase(dialect)
                || "posrgresql".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("PostgreSQL");
            if (ret != null) {
                return ret;
            }
        }

        if ("db2".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("DB2_IS");
            if (ret != null) {
                return ret;
            }
        }
        if ("clickhouse".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("ClickHouse");
            if (ret != null) {
                return ret;
            }
        }
        if ("hive".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("HiveQL");
            if (ret != null) {
                return ret;
            }
        }
        if ("spark".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("SparkSQL");
            if (ret != null) {
                return ret;
            }
        }
        if ("cassandra".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("CassandraQL");
            if (ret != null) {
                return ret;
            }
        }
        if ("mongodb".equalsIgnoreCase(dialect)) {
            Language ret = Language.findLanguageByID("Micronaut-MongoDB-JSON");
            if (ret != null) {
                return ret;
            }
            ret = Language.findLanguageByID("MongoDB-JSON");
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }

    public Language findPossibleDatabaseDialectSplit(String value) {
        if (value != null) {
            String[] arr = value.split("[,;|/]");
            for (String item : arr) {
                Language lang = findPossibleDatabaseDialect(item);
                if (lang != null) {
                    return lang;
                }
            }
        }
        return null;
    }

    public Language detectPossibleLanguageSplit(String value) {
        if (value != null) {
            String[] arr = value.split("[,;|/]");
            for (String item : arr) {
                Language lang = findPossibleLanguage(item);
                if (lang != null) {
                    return lang;
                }
            }
        }
        return null;
    }

    public Language detectXmlTagLangAttribute(XmlTag tag) {
        XmlAttribute attr = tag.getAttribute("_lang");

        if (attr != null) {
            String value = attr.getValue();
            Language lang = detectPossibleLanguageSplit(value);
            if (lang != null) {
                return lang;
            }
        }
        XmlAttribute type = tag.getAttribute("_type");
        if (type != null) {
            String value = type.getValue();
            Language lang = detectPossibleLanguageSplit(value);
            if (lang != null) {
                return lang;
            }
        }
        return null;
    }

    public Language getPossibleRenderLanguage() {
        Language ret = Language.findLanguageByID("VTL");
        if (ret != null) {
            return ret;
        }
        ret = Language.findLanguageByID("InjectedFreeMarker");
        if (ret != null) {
            return ret;
        }
        ret = Language.findLanguageByID("ThymeleafExpressions");
        if (ret != null) {
            return ret;
        }

        ret = Language.findLanguageByID("Shell Script");
        return ret;
    }

    public Language getPossibleTinyScriptLanguage() {
        Language lang = Language.findLanguageByID("TinyScript");
        if (lang != null) {
            return lang;
        }
        lang = Language.findLanguageByID("Scala");
        if (lang != null) {
            return lang;
        }
        lang = Language.findLanguageByID("Groovy");
        if (lang != null) {
            return lang;
        }
        lang = Language.findLanguageByID("Shell Script");
        return lang;
    }

    public Language getPossibleEvalLanguage() {
        Language lang = Language.findLanguageByID("Spring EL");
        if (lang != null) {
            return lang;
        }
        lang = Language.findLanguageByID("EL");
        if (lang != null) {
            return lang;
        }
        lang = Language.findLanguageByID("JavaScript");
        return lang;
    }

    public Language detectLanguage(XmlTag tag) {
        if (tag == null) {
            return null;
        }
        if (tag != null) {
            Language lang = detectXmlTagLangAttribute(tag);
            if (lang != null) {
                return lang;
            }
        }
        String name = tag.getName();
        if (Arrays.asList(
                "lang-body",
                "lang-catch",
                "lang-finally",
                "script-segment",
                "lang-synchronized",
                "lang-async",
                "sql-scope"
        ).contains(name)) {
            return null;
        }

        if (isTagOfType(name, "sql")) {
            XmlAttribute database = tag.getAttribute("database");
            if (database != null) {
                String value = database.getValue();
                Language ret = findPossibleDatabaseDialectSplit(value);
                if (ret != null) {
                    return ret;
                }
            }
            XmlAttribute databases = tag.getAttribute("databases");
            if (databases != null) {
                String value = databases.getValue();
                Language ret = findPossibleDatabaseDialectSplit(value);
                if (ret != null) {
                    return ret;
                }
            }

            return Language.findLanguageByID("SQL");
        }
        if (isTagOfType(name, "java")) {
            return Language.findLanguageByID("JAVA");
        }
        if (isTagOfType(name, "groovy")) {
            return Language.findLanguageByID("Groovy");
        }
        if (isTagOfType(name, "string")
                || isTagOfType(name, "render")) {
            return getPossibleRenderLanguage();
        }

        if (Arrays.asList(JAVA_LANG_TAGS).contains(name)) {
            return Language.findLanguageByID("JAVA");
        }
        if (Arrays.asList(SQL_LANG_TAGS).contains(name)) {
            return Language.findLanguageByID("SQL");
        }
        if (Arrays.asList("lang-eval-ts",
                "lang-eval-tinyscript").contains(name)) {
            Language lang = getPossibleTinyScriptLanguage();
            if (lang != null) {
                return lang;
            }
        }

        if (Arrays.asList("lang-eval").contains(name)) {
            Language lang = getPossibleEvalLanguage();
            if (lang != null) {
                return lang;
            }
        }

        if (true) {
            Language ret = findPossibleTagNameLanguage(name);
            if (ret != null) {
                return ret;
            }
        }

        XmlAttribute[] attributes = tag.getAttributes();
        if (attributes != null) {
            for (XmlAttribute attr : attributes) {
                String attrName = attr.getName();
                if (attrName == null || attrName.isEmpty()) {
                    continue;
                }
                String[] arr = attrName.split("[\\.;:]");
                Set<String> set = new LinkedHashSet<>(Arrays.asList(arr));
                if (set.contains("body-xml")
                        || set.contains("body-text")) {
                    if (set.contains("eval-java")) {
                        return Language.findLanguageByID("JAVA");
                    } else if (set.contains("eval-js")) {
                        return Language.findLanguageByID("JavaScript");
                    } else if (set.contains("eval-js")) {
                        return Language.findLanguageByID("Groovy");
                    } else if (set.contains("eval-ts")
                            || set.contains("eval-tinyscript")) {
                        Language lang = getPossibleTinyScriptLanguage();
                        if (lang != null) {
                            return lang;
                        }
                    } else if (set.contains("render")) {
                        Language ret = getPossibleRenderLanguage();
                        if (ret != null) {
                            return ret;
                        }
                    } else if (set.contains("eval")) {
                        Language lang = getPossibleEvalLanguage();
                        if (lang != null) {
                            return lang;
                        }
                    }
                }
            }
        }
        return detectLanguage(tag.getParentTag());
    }

    public void searchEvalJavaParts(PsiElement element,
                                    AtomicReference<String> importsRef,
                                    AtomicReference<String> membersRef,
                                    AtomicReference<String> bodyRef) {
        if (element == null) {
            return;
        }
        if (element instanceof XmlTag) {
            XmlTag xmlTag = (XmlTag) element;
            String name = xmlTag.getName();
            if (!Arrays.asList(
                    "lang-eval-java",
                    "lang-java-import",
                    "lang-java-member",
                    "lang-java-body",
                    "lang-eval-groovy"
            ).contains(name)) {
                if (bodyRef != null) {
                    String text = xmlTag.getValue().getText();
                    if (text != null && !text.isEmpty()) {
                        bodyRef.set(text);
                    }
                }
                return;
            }
            if ("lang-eval-java".equals(name)
                    || "lang-eval-groovy".equals(name)) {
                XmlTag[] subTags = xmlTag.getSubTags();
                if (subTags != null && subTags.length > 0) {
                    for (XmlTag subTag : subTags) {
                        String subName = subTag.getName();
                        if ("lang-java-import".equals(subName)) {
                            if (importsRef != null) {
                                String text = subTag.getValue().getText();
                                if (text != null && !text.isEmpty()) {
                                    importsRef.set(text);
                                }
                            }
                        } else if ("lang-java-member".equals(subName)) {
                            if (membersRef != null) {
                                String text = subTag.getValue().getText();
                                if (text != null && !text.isEmpty()) {
                                    membersRef.set(text);
                                }
                            }
                        } else if ("lang-java-body".equals(subName)) {
                            if (bodyRef != null) {
                                String text = subTag.getValue().getText();
                                if (text != null && !text.isEmpty()) {
                                    bodyRef.set(text);
                                }
                            }
                        }
                    }
                } else {
                    if (bodyRef != null) {
                        String text = xmlTag.getValue().getText();
                        if (text != null && !text.isEmpty()) {
                            bodyRef.set(text);
                        }
                    }
                }
                return;
            }
        }
        searchEvalJavaParts(element.getParent(), importsRef, membersRef, bodyRef);
    }

    public void injectXmlText(MultiHostRegistrar registrar, XmlText xmlText) {
        XmlTag tag = (XmlTag) xmlText.getParentTag();
        Language targetLang = detectLanguage(xmlText);

        if (tag == null) {
            return;
        }

        if (targetLang == null) {
            return;
        }

        String tagName = tag.getName();

//        log.info("xml tag :" + tagName + ", with lang:" + targetLang.getDisplayName());

        if (targetLang instanceof JavaLanguage) {
            AtomicReference<String> importsRef = new AtomicReference<>();
            AtomicReference<String> memberRef = new AtomicReference<>();
            AtomicReference<String> bodyRef = new AtomicReference<>();
            searchEvalJavaParts(xmlText, importsRef, memberRef, bodyRef);

            String importSegment = getEvalJavaImports(xmlText.getProject()) + "\n"
                    + Optional.ofNullable(importsRef.get()).orElse("");
            String memberSegment = Optional.ofNullable(memberRef.get()).orElse("");
            String bodySegment = Optional.ofNullable(bodyRef.get()).orElse("");

            Set<String> additionalImports = new LinkedHashSet<>();
            memberSegment = RegexUtil.regexFindAndReplace(memberSegment, "(\\s|^|;)import\\s+[a-zA-Z0-9_\\$\\.]+(\\.\\*)?;", s -> {
                additionalImports.add(s);
                return "/*" + s + "*/";
            });
            bodySegment = RegexUtil.regexFindAndReplace(bodySegment, "(\\s|^|;)import\\s+[a-zA-Z0-9_\\$\\.]+(\\.\\*)?;", s -> {
                additionalImports.add(s);
                return "/*" + s + "*/";
            });
            if (!additionalImports.isEmpty()) {
                importSegment += "\n" + String.join("\n", additionalImports);
            }

            if ("lang-java-import".equals(tagName)) {
                registrar.startInjecting(targetLang)
                        .addPlace(importSegment + "\n",
                                "class MyDsl {}",
                                (PsiLanguageInjectionHost) xmlText,
                                new TextRange(0, xmlText.getTextRange().getLength()))
                        .doneInjecting();
            } else if ("lang-java-member".equals(tagName)) {
                registrar.startInjecting(targetLang)
                        .addPlace(importSegment + "\n"
                                        + "public class MyJavaProcedure { ",
                                " }",
                                (PsiLanguageInjectionHost) xmlText,
                                new TextRange(0, xmlText.getTextRange().getLength()))
                        .doneInjecting();
            } else if ("lang-java-body".equals(tagName)
                    || "lang-eval-java".equals(tagName)) {

                registrar.startInjecting(targetLang)
                        .addPlace(importSegment + "\n"
                                        + "public class MyJavaProcedure { \n"
                                        + Optional.ofNullable(memberRef.get()).orElse("") + "\n"
                                        + "public Object exec(JdbcProcedureExecutor executor,Map<String,Object> params) throws Throwable { \n",
                                "\n} \n}",
                                (PsiLanguageInjectionHost) xmlText,
                                new TextRange(0, xmlText.getTextRange().getLength()))
                        .doneInjecting();
            } else {

                registrar.startInjecting(targetLang)
                        .addPlace(importSegment + "\n" +
                                        "public class MyJavaEval { public Object eval() throws Throwable{ ",
                                "} }",
                                (PsiLanguageInjectionHost) xmlText,
                                new TextRange(0, xmlText.getTextRange().getLength()))
                        .doneInjecting();
            }
        } else if (Arrays.asList("javascript", "js").contains(targetLang.getID().toLowerCase())) {
            registrar.startInjecting(targetLang)
                    .addPlace(
                            JS_INJECT_PREFIX + "\n",
                            "",
                            (PsiLanguageInjectionHost) xmlText,
                            new TextRange(0, xmlText.getTextRange().getLength()))
                    .doneInjecting();
        } else if (Arrays.asList("groovy").contains(targetLang.getID().toLowerCase())) {
            AtomicReference<String> importsRef = new AtomicReference<>();
            AtomicReference<String> memberRef = new AtomicReference<>();
            AtomicReference<String> bodyRef = new AtomicReference<>();
            searchEvalJavaParts(xmlText, importsRef, memberRef, bodyRef);

            String importSegment = getEvalJavaImports(xmlText.getProject()) + "\n"
                    + Optional.ofNullable(importsRef.get()).orElse("");
            String memberSegment = Optional.ofNullable(memberRef.get()).orElse("");
            String bodySegment = Optional.ofNullable(bodyRef.get()).orElse("");

            Set<String> additionalImports = new LinkedHashSet<>();
            memberSegment = RegexUtil.regexFindAndReplace(memberSegment, "(\\s|^|;)import\\s+[a-zA-Z0-9_\\$\\.]+(\\.\\*)?;", s -> {
                additionalImports.add(s);
                return "/*" + s + "*/";
            });
            bodySegment = RegexUtil.regexFindAndReplace(bodySegment, "(\\s|^|;)import\\s+[a-zA-Z0-9_\\$\\.]+(\\.\\*)?;", s -> {
                additionalImports.add(s);
                return "/*" + s + "*/";
            });
            if (!additionalImports.isEmpty()) {
                importSegment += "\n" + String.join("\n", additionalImports);
            }

            if ("lang-eval-groovy".equals(tagName)) {
                registrar.startInjecting(targetLang)
                        .addPlace(importSegment + "\n"
                                        + "class MyGroovyProcedure { \n"
                                        + "def exec(JdbcProcedureExecutor executor, Map<String,Object> params) throws Throwable { \n",
                                "\n} \n}",
                                (PsiLanguageInjectionHost) xmlText,
                                new TextRange(0, xmlText.getTextRange().getLength()))
                        .doneInjecting();
            } else {
                registrar.startInjecting(targetLang)
                        .addPlace(importSegment + "\n",
                                "",
                                (PsiLanguageInjectionHost) xmlText,
                                new TextRange(0, xmlText.getTextRange().getLength()))
                        .doneInjecting();
            }
        } else {
            registrar.startInjecting(targetLang)
                    .addPlace("",
                            "",
                            (PsiLanguageInjectionHost) xmlText,
                            new TextRange(0, xmlText.getTextRange().getLength()))
                    .doneInjecting();
        }

//        log.info("xml inject success tag :" + tagName + ", with lang:" + targetLang.getDisplayName());
    }

    public void injectXmlAttribute(MultiHostRegistrar registrar, XmlAttribute attr) {
        XmlAttributeValue attrValueElement = attr.getValueElement();
        if (attrValueElement == null) {
            return;
        }
        XmlTag tag = attr.getParent();
        String tagName = tag.getName();
        String attrName = attr.getName();
        if (attrName == null) {
            attrName = "";
        }
        if ("lang-set".equals(tagName)) {
            if ("radix".equals(attrName)) {
                Language lang = findPossibleLanguage("java");
                if (lang != null) {
                    registrar.startInjecting(lang)
                            .addPlace("class MyDsl { public Object v=",
                                    ";}",
                                    (PsiLanguageInjectionHost) attrValueElement,
                                    new TextRange(0, attrValueElement.getTextRange().getLength()))
                            .doneInjecting();
                    return;
                }
            }
            if ("value".equals(attrName) || attrName.startsWith("value.")) {
                Language lang = detectXmlTagLangAttribute(tag);
                if (lang != null) {
                    registrar.startInjecting(lang)
                            .addPlace("",
                                    "",
                                    (PsiLanguageInjectionHost) attrValueElement,
                                    new TextRange(0, attrValueElement.getTextRange().getLength()))
                            .doneInjecting();
                    return;
                }
            }
        } else if ("lang-fori".equals(tagName)) {
            if ("begin".equals(attrName)
                    || "end".equals(attrName)
                    || "incr".equals(attrName)) {
                Language lang = findPossibleLanguage("java");
                if (lang != null) {
                    registrar.startInjecting(lang)
                            .addPlace("class MyDsl { public Object v=",
                                    ";}",
                                    (PsiLanguageInjectionHost) attrValueElement,
                                    new TextRange(0, attrValueElement.getTextRange().getLength()))
                            .doneInjecting();
                    return;
                }
            }
        } else if ("lang-invoke".equals(tagName)) {
            if ("method".equals(attrName)) {
                Language lang = findPossibleLanguage("java");
                if (lang != null) {
                    registrar.startInjecting(lang)
                            .addPlace(getEvalJavaImports(attrValueElement.getProject()) + "\n"
                                            + "public class MyJavaProcedure { \n"
                                            + "public Object exec(JdbcProcedureExecutor executor,Map<String,Object> params) throws Throwable { \n",
                                    "\n} \n}",
                                    (PsiLanguageInjectionHost) attrValueElement,
                                    new TextRange(0, attrValueElement.getTextRange().getLength()))
                            .doneInjecting();
                    return;
                }
            }
        } else if ("lang-catch".equals(tagName)
                || "lang-throw".equals(tagName)) {
            if ("type".equals(attrName)) {
                Language lang = findPossibleLanguage("java");
                if (lang != null) {
                    registrar.startInjecting(lang)
                            .addPlace("class MyDsl { public  ",
                                    " a; }",
                                    (PsiLanguageInjectionHost) attrValueElement,
                                    new TextRange(0, attrValueElement.getTextRange().getLength()))
                            .doneInjecting();
                    return;
                }
            }
        } else if (tagName.startsWith("sql-")) {
            if ("result-type".equals(attrName)) {
                Language lang = findPossibleLanguage("java");
                if (lang != null) {
                    registrar.startInjecting(lang)
                            .addPlace("class MyDsl { public  ",
                                    " a; }",
                                    (PsiLanguageInjectionHost) attrValueElement,
                                    new TextRange(0, attrValueElement.getTextRange().getLength()))
                            .doneInjecting();
                    return;
                }
            }
        } else if ("lang-async".equals(tagName)
                || "lang-latch-await".equals(tagName)
                || "lang-sleep".equalsIgnoreCase(tagName)) {
            if ("time-unit".equals(attrName)) {
                Language lang = findPossibleLanguage("java");
                if (lang != null) {
                    registrar.startInjecting(lang)
                            .addPlace("class MyDsl { void call() { Object obj=java.util.concurrent.TimeUnit.",
                                    "; } }",
                                    (PsiLanguageInjectionHost) attrValueElement,
                                    new TextRange(0, attrValueElement.getTextRange().getLength()))
                            .doneInjecting();
                    return;
                }
            } else if ("delay".equalsIgnoreCase(attrName)
                    || "timeout".equalsIgnoreCase(attrName)
                    || "await".equalsIgnoreCase(attrName)
                    || "read-only".equalsIgnoreCase(attrName)
                    || "params_share".equalsIgnoreCase(attrName)
                    || "limited".equalsIgnoreCase(attrName)
                    || "accept-batch".equalsIgnoreCase(attrName)
                    || "before-truncate".equalsIgnoreCase(attrName)
            ) {
                Language lang = findPossibleLanguage("java");
                if (lang != null) {
                    registrar.startInjecting(lang)
                            .addPlace("class MyDsl { public Object v=",
                                    ";}",
                                    (PsiLanguageInjectionHost) attrValueElement,
                                    new TextRange(0, attrValueElement.getTextRange().getLength()))
                            .doneInjecting();
                    return;
                }
            }
        } else if ("propagation".equalsIgnoreCase(attrName)) {
            Language lang = findPossibleLanguage("java");
            if (lang != null) {
                registrar.startInjecting(lang)
                        .addPlace(" enum Propagation { REQUIRED,SUPPORTS,MANDATORY,REQUIRES_NEW,NOT_SUPPORTED,NEVER,NESTED; }" +
                                        " class MyDsl { public Propagation v=Propagation.",
                                ";}",
                                (PsiLanguageInjectionHost) attrValueElement,
                                new TextRange(0, attrValueElement.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        } else if ("isolation".equalsIgnoreCase(attrName)) {
            Language lang = findPossibleLanguage("java");
            if (lang != null) {
                registrar.startInjecting(lang)
                        .addPlace(" enum Connection {TRANSACTION_NONE ,TRANSACTION_READ_UNCOMMITTED ,TRANSACTION_READ_COMMITTED ,TRANSACTION_REPEATABLE_READ ,TRANSACTION_SERIALIZABLE; }" +
                                        " class MyDsl { public int v=java.sql.Connection.",
                                ";}",
                                (PsiLanguageInjectionHost) attrValueElement,
                                new TextRange(0, attrValueElement.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        } else if (
                "class".equalsIgnoreCase(attrName)
                        || "type".equalsIgnoreCase(attrName)
                        || "result-type".equalsIgnoreCase(attrName)
                        || "rollback-for".equalsIgnoreCase(attrName)
                        || "no-rollback-for".equalsIgnoreCase(attrName)
                        || "resultType".equalsIgnoreCase(attrName)
                        || "parameterType".equalsIgnoreCase(attrName)
                        || "className".equalsIgnoreCase(attrName)
                        || "packageName".equalsIgnoreCase(attrName)
                        || "return-type".equalsIgnoreCase(attrName)
                        || "returnType".equalsIgnoreCase(attrName)
                        || "javaType".equalsIgnoreCase(attrName)
                        || "typeHandler".equalsIgnoreCase(attrName)
        ) {
            Language lang = findPossibleLanguage("java");
            if (lang != null) {
                registrar.startInjecting(lang)
                        .addPlace("class MyDsl { public  ",
                                " a; }",
                                (PsiLanguageInjectionHost) attrValueElement,
                                new TextRange(0, attrValueElement.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        } else if ("package".equalsIgnoreCase(attrName)) {
            Language lang = findPossibleLanguage("java");
            if (lang != null) {
                registrar.startInjecting(lang)
                        .addPlace("package ",
                                ";",
                                (PsiLanguageInjectionHost) attrValueElement,
                                new TextRange(0, attrValueElement.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        } else if ("jdbc-type".equals(attrName)
                || "jdbcType".equals(attrName)) {
            Language lang = findPossibleLanguage("java");
            if (lang != null) {
                registrar.startInjecting(lang)
                        .addPlace("class MyDsl { void call() { java.sql.JDBCType obj=java.sql.JDBCType.",
                                "; } }",
                                (PsiLanguageInjectionHost) attrValueElement,
                                new TextRange(0, attrValueElement.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        }

        if (attrName.contains(".int")
                || attrName.contains(".long")
                || attrName.contains(".boolean")
                || attrName.contains(".float")
                || attrName.contains(".double")) {
            Language lang = findPossibleLanguage("java");
            if (lang != null) {
                registrar.startInjecting(lang)
                        .addPlace("class MyDsl { public Object v=",
                                ";}",
                                (PsiLanguageInjectionHost) attrValueElement,
                                new TextRange(0, attrValueElement.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        }

        if (attrName.contains(".render")) {
            Language lang = getPossibleRenderLanguage();
            if (lang != null) {
                registrar.startInjecting(lang)
                        .addPlace("",
                                "",
                                (PsiLanguageInjectionHost) attrValueElement,
                                new TextRange(0, attrValueElement.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        }

        if (attrName.equals("test")
                || attrName.equals("collection")
                || attrName.contains(".eval.")
                || attrName.endsWith(".eval")) {
            Language lang = getPossibleEvalLanguage();
            if (lang != null) {
                registrar.startInjecting(lang)
                        .addPlace("",
                                "",
                                (PsiLanguageInjectionHost) attrValueElement,
                                new TextRange(0, attrValueElement.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        }

        if (attrName.contains(".eval-java")) {
            Language lang = findPossibleLanguage("java");
            if (lang != null) {
                registrar.startInjecting(lang)
                        .addPlace(getEvalJavaImports(attrValueElement.getProject()) + "\n"
                                        + "public class MyJavaProcedure { \n"
                                        + "public Object exec(JdbcProcedureExecutor executor,Map<String,Object> params) throws Throwable { \n",
                                "\n} \n}",
                                (PsiLanguageInjectionHost) attrValueElement,
                                new TextRange(0, attrValueElement.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        }

        if (attrName.contains(".eval-js")) {
            Language lang = findPossibleLanguage("javascript");
            if (lang != null) {
                registrar.startInjecting(lang)
                        .addPlace(
                                JS_INJECT_PREFIX + "\n",
                                "",
                                (PsiLanguageInjectionHost) attrValueElement,
                                new TextRange(0, attrValueElement.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        }

        if (attrName.contains(".eval-ts")
                || attrName.contains(".eval-tinyscript")) {
            Language lang = getPossibleTinyScriptLanguage();
            if (lang != null) {
                registrar.startInjecting(lang)
                        .addPlace("",
                                "",
                                (PsiLanguageInjectionHost) attrValueElement,
                                new TextRange(0, attrValueElement.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        }

        if (attrName.contains(".eval-groovy")) {
            Language lang = findPossibleLanguage("groovy");
            if (lang != null) {
                registrar.startInjecting(lang)
                        .addPlace(getEvalJavaImports(attrValueElement.getProject()) + "\n"
                                        + "class MyGroovyProcedure { \n"
                                        + "def exec(JdbcProcedureExecutor executor, Map<String,Object> params) throws Throwable { \n",
                                "\n} \n}",
                                (PsiLanguageInjectionHost) attrValueElement,
                                new TextRange(0, attrValueElement.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        }

        if (attrName.equals("_lang")) {
            Language lang = findPossibleLanguage("java");
            if (lang != null) {
                registrar.startInjecting(lang)
                        .addPlace("public class MyDsl { "
                                        + LANG_JAVA_ENUM + "\n"
                                        + " public Object v=" + LANG_JAVA_ENUM_CLASS_NAME + ".",
                                ";}",
                                (PsiLanguageInjectionHost) attrValueElement,
                                new TextRange(0, attrValueElement.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        }
    }

}
