package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.json.JsonLanguage;
import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.impl.AbstractFileTypeBase;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import com.intellij.sql.dialects.clickhouse.CHouseDialect;
import com.intellij.sql.dialects.db2.iseries.Db2ISDialect;
import com.intellij.sql.dialects.hive.HiveDialect;
import com.intellij.sql.dialects.maria.MariaDialect;
import com.intellij.sql.dialects.mysql.MysqlDialect;
import com.intellij.sql.dialects.oracle.OraDialect;
import com.intellij.sql.dialects.spark.SparkDialect;
import com.intellij.sql.psi.SqlLanguage;
import org.apache.velocity.VelocityContext;
import org.intellij.lang.regexp.RegExpLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.java.generate.velocity.VelocityFactory;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/3/1 19:01
 * @desc
 */
final class JdbcProcedureXmlLangInjectInjector implements MultiHostInjector {
    public static final Logger log = Logger.getInstance(JdbcProcedureXmlLangInjectInjector.class);

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

    public static final String EVAL_JAVA_IMPORTS = "\n" +
            "import i2f.jdbc.procedure.context.*;\n" +
            "import i2f.jdbc.procedure.parser.data.*;\n" +
            "import i2f.jdbc.procedure.executor.*;\n" +
            "import i2f.jdbc.procedure.caller.*;\n" +
            "import i2f.jdbc.procedure.caller.impl.*;\n" +
            "import i2f.jdbc.procedure.consts.*;\n" +
            "import i2f.jdbc.*;\n" +
            "import i2f.match.regex.*;\n" +
            "import i2f.reflect.*;\n" +
            "import i2f.reflect.vistor.*;\n" +
            "import i2f.convert.obj.*;\n" +
            "import i2f.clock.*;\n" +
            "import java.util.*;\n" +
            "import java.math.*;\n" +
            "import java.time.*;\n" +
            "import java.text.*;\n" +
            "import java.util.regex.*;\n" +
            "import java.io.*;\n" +
            "import java.lang.reflect.*;\n" +
            "import javax.sql.*;\n" +
            "import java.util.concurrent.*;\n" +
            "import java.nio.*;\n" +
            "import java.nio.charset.*;\n" +
            "import java.util.concurrent.atomic.*;\n" +
            "import java.util.concurrent.locks.*;\n" +
            "import java.time.chrono.*;\n" +
            "import java.time.format.*;\n" +
            "import java.time.temporal.*;\n" +
            "import java.time.zone.*;\n";

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

    public Language detectLanguage(XmlTag tag) {
        if (tag == null) {
            return null;
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
            Set<String> dialects = new HashSet<>();
            XmlAttribute database = tag.getAttribute("database");
            if (database != null) {
                String value = database.getValue();
                if (value != null) {
                    dialects.addAll(Arrays.asList(value.split("[,;|/]")));
                }
            }
            XmlAttribute databases = tag.getAttribute("databases");
            if (databases != null) {
                String value = databases.getValue();
                if (value != null) {
                    dialects.addAll(Arrays.asList(value.split("[,;|/]")));
                }
            }
            for (String dialect : dialects) {
                dialect = dialect.trim();
                if ("mariadb".equalsIgnoreCase(dialect)
                        || "maria".equalsIgnoreCase(dialect)) {
                    return MariaDialect.INSTANCE;
                }
                if ("mysql".equalsIgnoreCase(dialect)
                        || "gbase".equalsIgnoreCase(dialect)) {
                    return MysqlDialect.INSTANCE;
                }
                if ("oracle".equalsIgnoreCase(dialect)) {
                    return OraDialect.INSTANCE;
                }
                if ("posrgre".equalsIgnoreCase(dialect)
                        || "posrgresql".equalsIgnoreCase(dialect)) {
                    return SparkDialect.INSTANCE;
                }

                if ("db2".equalsIgnoreCase(dialect)) {
                    return Db2ISDialect.INSTANCE;
                }
                if ("clickhouse".equalsIgnoreCase(dialect)) {
                    return CHouseDialect.INSTANCE;
                }
                if ("hive".equalsIgnoreCase(dialect)) {
                    return HiveDialect.INSTANCE;
                }
                if ("spark".equalsIgnoreCase(dialect)) {
                    return SparkDialect.INSTANCE;
                }
            }


            return SqlLanguage.INSTANCE;
        }
        if (isTagOfType(name, "java")) {
            return JavaLanguage.INSTANCE;
        }
        if (isTagOfType(name, "string")
                || isTagOfType(name, "render")) {
            Set<String> dialects = new HashSet<>();
            XmlAttribute lang = tag.getAttribute("_lang");
            if (lang != null) {
                String value = lang.getValue();
                if (value != null) {
                    dialects.addAll(Arrays.asList(value.split("[,;|/]")));
                }
            }
            XmlAttribute type = tag.getAttribute("_type");
            if (type != null) {
                String value = type.getValue();
                if (value != null) {
                    dialects.addAll(Arrays.asList(value.split("[,;|/]")));
                }
            }
            for (String dialect : dialects) {
                dialect = dialect.trim();
                if ("sql".equalsIgnoreCase(dialect)) {
                    return SqlLanguage.INSTANCE;
                }
                if ("java".equalsIgnoreCase(dialect)) {
                    return JavaLanguage.INSTANCE;
                }
                if ("json".equalsIgnoreCase(dialect)) {
                    return JsonLanguage.INSTANCE;
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
                if ("yaml".equalsIgnoreCase(dialect)) {
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
                if ("mariadb".equalsIgnoreCase(dialect)
                        || "maria".equalsIgnoreCase(dialect)) {
                    return MariaDialect.INSTANCE;
                }
                if ("mysql".equalsIgnoreCase(dialect)
                        || "gbase".equalsIgnoreCase(dialect)) {
                    return MysqlDialect.INSTANCE;
                }
                if ("oracle".equalsIgnoreCase(dialect)) {
                    return OraDialect.INSTANCE;
                }
                if ("posrgre".equalsIgnoreCase(dialect)
                        || "posrgresql".equalsIgnoreCase(dialect)) {
                    return SparkDialect.INSTANCE;
                }

                if ("db2".equalsIgnoreCase(dialect)) {
                    return Db2ISDialect.INSTANCE;
                }
                if ("clickhouse".equalsIgnoreCase(dialect)) {
                    return CHouseDialect.INSTANCE;
                }
                if ("hive".equalsIgnoreCase(dialect)) {
                    return HiveDialect.INSTANCE;
                }
                if ("spark".equalsIgnoreCase(dialect)) {
                    return SparkDialect.INSTANCE;
                }
                Collection<Language> langs = Language.getRegisteredLanguages();
                for (Language item : langs) {
                    String id = item.getID();
//            log.warn("found registry lang: " + id);
                    if (id == null) {
                        continue;
                    }
                    if (dialect.equalsIgnoreCase(id)) {
                        return item;
                    }
                }
            }
            Language ret = Language.findLanguageByID("Shell Script");
            if (ret != null) {
                return ret;
            }
            return null;
        }
        if (isTagOfType(name, "json")) {
            return JsonLanguage.INSTANCE;
        }
        if (isTagOfType(name, "regex")) {
            return RegExpLanguage.INSTANCE;
        }
        if (isTagOfType(name, "javascript")
                || isTagOfType(name, "js")) {
            Language lang = Language.findLanguageByID("JavaScript");
            if (lang != null) {
                return lang;
            }
        }
        if (Arrays.asList(JAVA_LANG_TAGS).contains(name)) {
            return JavaLanguage.INSTANCE;
        }
        if (Arrays.asList(SQL_LANG_TAGS).contains(name)) {
            return SqlLanguage.INSTANCE;
        }
        if (Arrays.asList("lang-eval-ts",
                "lang-eval-tinyscript").contains(name)) {
            Language lang = Language.findLanguageByID("Scala");
            if (lang != null) {
                return lang;
            }
            lang = Language.findLanguageByID("Groovy");
            if (lang != null) {
                return lang;
            }
            lang = Language.findLanguageByID("Shell Script");
            if (lang != null) {
                return lang;
            }
        }
        if (isTagOfType(name, "shell")
                || isTagOfType(name, "bash")) {
            Language lang = Language.findLanguageByID("Shell Script");
            if (lang != null) {
                return lang;
            }
        }
        if (isTagOfType(name, "dockerfile")) {
            Language lang = Language.findLanguageByID("Dockerfile");
            if (lang != null) {
                return lang;
            }
        }
        if (isTagOfType(name, "vue")) {
            Language lang = Language.findLanguageByID("Vue");
            if (lang != null) {
                return lang;
            }
        }
        if (isTagOfType(name, "yaml")) {
            Language lang = Language.findLanguageByID("yaml");
            if (lang != null) {
                return lang;
            }
        }
        if (isTagOfType(name, "groovy")) {
            Language lang = Language.findLanguageByID("Groovy");
            if (lang != null) {
                return lang;
            }
        }
        if (isTagOfType(name, "antlr4")
                || isTagOfType(name, "antlrv4")
                || isTagOfType(name, "antlr")) {
            Language lang = Language.findLanguageByID("ANTLRv4");
            if (lang != null) {
                return lang;
            }
        }
        if (isTagOfType(name, "sass")
                || isTagOfType(name, "css")
                || isTagOfType(name, "style")) {
            Language lang = Language.findLanguageByID("SASS");
            if (lang != null) {
                return lang;
            }
        }
        if (isTagOfType(name, "redis")) {
            Language lang = Language.findLanguageByID("Redis");
            if (lang != null) {
                return lang;
            }
        }
        if (isTagOfType(name, "markdown")
                || isTagOfType(name, "md")) {
            Language lang = Language.findLanguageByID("Markdown");
            if (lang != null) {
                return lang;
            }
        }
        Collection<Language> langs = Language.getRegisteredLanguages();
        for (Language lang : langs) {
            String id = lang.getID();
//            log.warn("found registry lang: " + id);
            if (id == null) {
                continue;
            }
            id = id.toLowerCase();
            if (name.equals(id)
                    || name.startsWith(id + "-")
                    || name.endsWith("-" + id)) {
                return lang;
            }
        }
        return detectLanguage(tag.getParentTag());
    }

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar,
                                     @NotNull PsiElement context) {
        if (context instanceof XmlText) {
            XmlText xmlText = (XmlText) context;
            XmlTag tag = (XmlTag) xmlText.getParentTag();
            Language targetLang = detectLanguage(xmlText);

            if (tag == null) {
                return;
            }

            if (targetLang == null) {
                return;
            }

            String tagName = tag.getName();

            log.info("xml tag :" + tagName + ", with lang:" + targetLang.getDisplayName());

            if (targetLang instanceof JavaLanguage) {
                if ("lang-java-import".equals(tagName)) {
                    registrar.startInjecting(targetLang)
                            .addPlace("",
                                    "",
                                    (PsiLanguageInjectionHost) context,
                                    new TextRange(0, context.getTextRange().getLength()))
                            .doneInjecting();
                } else if ("lang-java-member".equals(tagName)) {
                    registrar.startInjecting(targetLang)
                            .addPlace(EVAL_JAVA_IMPORTS + "\n"
                                            + "public class MyJavaProcedure { ",
                                    " }",
                                    (PsiLanguageInjectionHost) context,
                                    new TextRange(0, context.getTextRange().getLength()))
                            .doneInjecting();
                } else if ("lang-java-body".equals(tagName)) {
                    registrar.startInjecting(targetLang)
                            .addPlace(EVAL_JAVA_IMPORTS + "\n"
                                            + "public class MyJavaProcedure { "
                                            + "public Object exec(ExecuteContext context, JdbcProcedureExecutor executor,Map<String,Object> params) throws Throwable { ",
                                    "} }",
                                    (PsiLanguageInjectionHost) context,
                                    new TextRange(0, context.getTextRange().getLength()))
                            .doneInjecting();
                } else {

                    registrar.startInjecting(targetLang)
                            .addPlace("public class MyJavaEval { public void eval() throws Throwable{ ",
                                    "} }",
                                    (PsiLanguageInjectionHost) context,
                                    new TextRange(0, context.getTextRange().getLength()))
                            .doneInjecting();
                }
            } else if (Arrays.asList("javascript", "js").contains(targetLang.getID().toLowerCase())) {
                registrar.startInjecting(targetLang)
                        .addPlace("var context={};\n" +
                                        "var executor={};\n" +
                                        "var params={};\n",
                                "",
                                (PsiLanguageInjectionHost) context,
                                new TextRange(0, context.getTextRange().getLength()))
                        .doneInjecting();
            } else {
                registrar.startInjecting(targetLang)
                        .addPlace("",
                                "",
                                (PsiLanguageInjectionHost) context,
                                new TextRange(0, context.getTextRange().getLength()))
                        .doneInjecting();
            }

            log.info("xml inject success tag :" + tagName + ", with lang:" + targetLang.getDisplayName());

        }

    }

    @Override
    public @NotNull List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return List.of(XmlText.class);
    }
}
