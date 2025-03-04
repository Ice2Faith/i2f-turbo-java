package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import org.jetbrains.annotations.NotNull;

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

    public Language findPossibleTagLanguage(String tagName) {
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
        for (Language item : langs) {
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
        return null;
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
                Language ret = findPossibleDatabaseDialect(dialect);
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
                Language ret = findPossibleLanguage(dialect);
                if (ret != null) {
                    return ret;
                }
            }
            Language ret = Language.findLanguageByID("Shell Script");
            if (ret != null) {
                return ret;
            }
            return null;
        }

        if (Arrays.asList(JAVA_LANG_TAGS).contains(name)) {
            return Language.findLanguageByID("JAVA");
        }
        if (Arrays.asList(SQL_LANG_TAGS).contains(name)) {
            return Language.findLanguageByID("SQL");
        }
        if (Arrays.asList("lang-eval-ts",
                "lang-eval-tinyscript").contains(name)) {
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
            if (lang != null) {
                return lang;
            }
        }

        if (true) {
            Language ret = findPossibleTagLanguage(name);
            if (ret != null) {
                return ret;
            }
        }
        return detectLanguage(tag.getParentTag());
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

        log.info("xml tag :" + tagName + ", with lang:" + targetLang.getDisplayName());

        if (targetLang instanceof JavaLanguage) {
            if ("lang-java-import".equals(tagName)) {
                registrar.startInjecting(targetLang)
                        .addPlace("",
                                "class MyDsl {}",
                                (PsiLanguageInjectionHost) xmlText,
                                new TextRange(0, xmlText.getTextRange().getLength()))
                        .doneInjecting();
            } else if ("lang-java-member".equals(tagName)) {
                registrar.startInjecting(targetLang)
                        .addPlace(EVAL_JAVA_IMPORTS + "\n"
                                        + "public class MyJavaProcedure { ",
                                " }",
                                (PsiLanguageInjectionHost) xmlText,
                                new TextRange(0, xmlText.getTextRange().getLength()))
                        .doneInjecting();
            } else if ("lang-java-body".equals(tagName)) {
                registrar.startInjecting(targetLang)
                        .addPlace(EVAL_JAVA_IMPORTS + "\n"
                                        + "public class MyJavaProcedure { "
                                        + "public Object exec(ExecuteContext context, JdbcProcedureExecutor executor,Map<String,Object> params) throws Throwable { ",
                                "} }",
                                (PsiLanguageInjectionHost) xmlText,
                                new TextRange(0, xmlText.getTextRange().getLength()))
                        .doneInjecting();
            } else {

                registrar.startInjecting(targetLang)
                        .addPlace("public class MyJavaEval { public void eval() throws Throwable{ ",
                                "} }",
                                (PsiLanguageInjectionHost) xmlText,
                                new TextRange(0, xmlText.getTextRange().getLength()))
                        .doneInjecting();
            }
        } else if (Arrays.asList("javascript", "js").contains(targetLang.getID().toLowerCase())) {
            registrar.startInjecting(targetLang)
                    .addPlace("var context={};\n" +
                                    "var executor={};\n" +
                                    "var params={};\n",
                            "",
                            (PsiLanguageInjectionHost) xmlText,
                            new TextRange(0, xmlText.getTextRange().getLength()))
                    .doneInjecting();
        } else {
            registrar.startInjecting(targetLang)
                    .addPlace("",
                            "",
                            (PsiLanguageInjectionHost) xmlText,
                            new TextRange(0, xmlText.getTextRange().getLength()))
                    .doneInjecting();
        }

        log.info("xml inject success tag :" + tagName + ", with lang:" + targetLang.getDisplayName());
    }

    public void injectXmlAttribute(MultiHostRegistrar registrar, XmlAttribute attr) {
        XmlAttributeValue attrValueElement = attr.getValueElement();
        XmlTag tag = attr.getParent();
        String tagName = tag.getName();
        String attrName = attr.getName();
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
                            .addPlace(EVAL_JAVA_IMPORTS + "\n"
                                            + "public class MyJavaProcedure { "
                                            + "public Object exec(ExecuteContext context, JdbcProcedureExecutor executor,Map<String,Object> params) throws Throwable { ",
                                    "} }",
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
                    || "await".equalsIgnoreCase(attrName)) {
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
        } else if ("class".equalsIgnoreCase(attrName)) {
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

        if (attrName.contains(".eval-java")) {
            Language lang = findPossibleLanguage("java");
            if (lang != null) {
                registrar.startInjecting(lang)
                        .addPlace(EVAL_JAVA_IMPORTS + "\n"
                                        + "public class MyJavaProcedure { "
                                        + "public Object exec(ExecuteContext context, JdbcProcedureExecutor executor,Map<String,Object> params) throws Throwable { ",
                                "} }",
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
                        .addPlace("var context={};\n" +
                                        "var executor={};\n" +
                                        "var params={};\n",
                                "",
                                (PsiLanguageInjectionHost) attrValueElement,
                                new TextRange(0, attrValueElement.getTextRange().getLength()))
                        .doneInjecting();
                return;
            }
        }
    }

}
