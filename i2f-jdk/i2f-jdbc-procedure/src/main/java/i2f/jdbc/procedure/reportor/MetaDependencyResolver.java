package i2f.jdbc.procedure.reportor;

import i2f.extension.antlr4.script.tiny.TinyScriptParser;
import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import i2f.io.stream.StreamUtil;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.consts.TagConsts;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.JdbcProcedureJavaCaller;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.match.regex.RegexPattens;
import i2f.match.regex.RegexUtil;
import i2f.match.regex.data.RegexMatchItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.InputStream;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/4/5 14:06
 */
public class MetaDependencyResolver {

    public static StringBuilder printDependencyGraph(Map<String, DependencyNode> dependencyGraph, StringBuilder builder) {
        return printDependencyGraphNext(null, dependencyGraph, builder, 0, new LinkedHashSet<>());
    }

    public static StringBuilder printDependencyGraph(String naming, Map<String, DependencyNode> dependencyGraph, StringBuilder builder) {
        DependencyNode node = dependencyGraph.get(naming);
        if (node == null) {
            return builder;
        }
        return printDependencyGraphNext(node, dependencyGraph, builder, 0, new LinkedHashSet<>());
    }

    public static StringBuilder printDependencyGraphNext(DependencyNode node, Map<String, DependencyNode> dependencyGraph, StringBuilder builder, int level, Set<String> printed) {
        if (dependencyGraph == null || builder == null) {
            return builder;
        }
        if (level < 0) {
            level = 0;
        }
        if (printed == null) {
            printed = new LinkedHashSet<>();
        }
        List<DependencyNode> nodes = new ArrayList<>();
        if (node != null) {
            nodes.add(node);
        } else {
            List<DependencyNode> rootNodes = findAllRootNodes(dependencyGraph);
            nodes.addAll(rootNodes);
        }
        for (DependencyNode item : nodes) {

            for (int i = 0; i < level; i++) {
                if (i == level - 1) {
                    builder.append("|- ");
                } else {
                    builder.append("| ");
                }
            }
            builder.append(item.naming);
            builder.append("\n");
            if (printed.contains(item.naming)) {
                continue;
            }
            printed.add(item.naming);
            if (item.dependencies != null && !item.dependencies.isEmpty()) {
                for (String next : item.dependencies) {
                    DependencyNode nextNode = dependencyGraph.get(next);
                    if (nextNode != null) {
                        printDependencyGraphNext(nextNode, dependencyGraph, builder, level + 1, printed);
                    }
                }
            }

        }
        return builder;
    }

    @Data
    @NoArgsConstructor
    public static class EchartsGraphNode {
        public String id;
        public String name;

        public EchartsGraphNode(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Data
    @NoArgsConstructor
    public static class EchartsGraphLink {
        public String source;
        public String target;

        public EchartsGraphLink(String source, String target) {
            this.source = source;
            this.target = target;
        }
    }

    @Data
    public static class EchartsGraph {
        public List<EchartsGraphNode> data;
        public List<EchartsGraphLink> links;
    }

    public static EchartsGraph convertAsEchartsGraph(Map<String, DependencyNode> dependencyGraph) {
        EchartsGraph ret = new EchartsGraph();
        ret.data = new ArrayList<>();
        ret.links = new ArrayList<>();
        for (Map.Entry<String, DependencyNode> entry : dependencyGraph.entrySet()) {
            ret.data.add(new EchartsGraphNode(entry.getKey(), entry.getKey()));
            DependencyNode value = entry.getValue();
            Set<String> dependencies = value.getDependencies();
            if (dependencies != null) {
                for (String dependency : dependencies) {
                    ret.links.add(new EchartsGraphLink(entry.getKey(), dependency));
                }
            }
        }
        return ret;
    }

    public static List<DependencyNode> findAllRootNodes(Map<String, DependencyNode> dependencyGraph) {
        List<DependencyNode> ret = new ArrayList<>();
        for (Map.Entry<String, DependencyNode> entry : dependencyGraph.entrySet()) {
            DependencyNode node = entry.getValue();
            if (node.usages == null || node.usages.isEmpty()) {
                ret.add(node);
            }
        }
        return ret;
    }

    public static Map<String, DependencyNode> getDependencyGraph(Map<String, Set<String>> dependencyMap) {
        Map<String, Set<String>> usageMap = getDependencyUsageMap(dependencyMap);
        Map<String, DependencyNode> ret = new LinkedHashMap<>();
        for (Map.Entry<String, Set<String>> entry : dependencyMap.entrySet()) {
            DependencyNode node = new DependencyNode();
            node.naming = entry.getKey();
            node.dependencies = new LinkedHashSet<>();
            Set<String> dependencies = entry.getValue();
            if (dependencies != null) {
                node.dependencies.addAll(dependencies);
            }
            node.usages = new LinkedHashSet<>();
            Set<String> usages = usageMap.get(entry.getKey());
            if (usages != null) {
                node.usages.addAll(usages);
            }
            ret.put(node.naming, node);

        }
        return ret;
    }

    public static Map<String, Set<String>> getDependencyUsageMap(Map<String, Set<String>> dependencyMap) {
        Map<String, Set<String>> ret = new LinkedHashMap<>();
        for (Map.Entry<String, Set<String>> entry : dependencyMap.entrySet()) {
            String naming = entry.getKey();
            Set<String> dependencies = entry.getValue();
            if (dependencies == null || dependencies.isEmpty()) {
                continue;
            }
            for (String dependency : dependencies) {
                Set<String> usages = ret.computeIfAbsent(dependency, (key) -> new LinkedHashSet<>());
                usages.add(naming);
            }
        }
        return ret;
    }

    public static Map<String, Set<String>> getDependencyMap(Map<String, ProcedureMeta> metaMap) {
        Map<String, Set<String>> ret = new HashMap<>();
        for (Map.Entry<String, ProcedureMeta> entry : metaMap.entrySet()) {
            String name = entry.getKey();
            ret.put(name, new LinkedHashSet<>());
            ProcedureMeta value = entry.getValue();
            if (value.getType() == ProcedureMeta.Type.XML) {
                XmlNode node = (XmlNode) value.getTarget();
                getDependencyMapNext(node, ret.get(name));
            } else if (value.getType() == ProcedureMeta.Type.JAVA) {
                JdbcProcedureJavaCaller caller = (JdbcProcedureJavaCaller) value.getTarget();
                getDependencyMapNext(caller, ret.get(name));
            }
        }
        return ret;
    }

    public static void getDependencyMapNext(JdbcProcedureJavaCaller caller, Set<String> dependencies) {
        String fileName = caller.getClass().getName().replace(".", "/") + ".class";
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            byte[] bytes = StreamUtil.readBytes(is, true);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                byte bt = bytes[i];
                if (bt >= 32 && bt <= 127) {
                    builder.append((char) bt);
                } else {
                    builder.append(String.format(" \\x%02x ", (int) (bt & 0x0ff)));
                }
            }
            String byteStr = builder.toString();
            List<RegexMatchItem> items = RegexUtil.regexFinds(byteStr, "[a-zA-Z0-9_]+");
            for (RegexMatchItem item : items) {
                String naming = item.getMatchStr();
                if (isDependencyNaming(naming)) {
                    dependencies.add(naming);
                }
            }
        } catch (Exception e) {

        }
    }

    public static void getDependencyMapNext(XmlNode node, Set<String> dependencies) {
        if (node == null) {
            return;
        }
        String tagName = node.getTagName();
        if (tagName == null) {
            return;
        }
        if (Arrays.asList(
                TagConsts.PROCEDURE_CALL,
                TagConsts.FUNCTION_CALL,
                TagConsts.JAVA_CALL,
                TagConsts.SCRIPT_INCLUDE
        ).contains(tagName)) {
            String refid = node.getTagAttrMap().get(AttrConsts.REFID);
            if (refid != null && !refid.isEmpty()) {
                dependencies.add(refid);
            }
        } else if (Arrays.asList(
                TagConsts.LANG_EVAL_TS,
                TagConsts.LANG_EVAL_TINYSCRIPT
        ).contains(tagName)) {
            String script = node.getTextBody();
            getTinyScriptDependencyMapNext(dependencies, script);
        } else if (Arrays.asList(
                TagConsts.LANG_EVAL_GROOVY,
                TagConsts.LANG_EVAL_JAVA,
                TagConsts.LANG_JAVA_IMPORT,
                TagConsts.LANG_JAVA_MEMBE,
                TagConsts.LANG_JAVA_BODY,
                TagConsts.LANG_EVAL_JAVASCRIPT,
                TagConsts.LANG_EVAL_JS,
                TagConsts.LANG_EVAL
        ).contains(tagName)) {
            String script = node.getTextBody();
            getEvalScriptDependencyMapNext(dependencies, script);
        } else if (Arrays.asList(
                TagConsts.LANG_STRING,
                TagConsts.LANG_RENDER
        ).contains(tagName)) {
            String lang = node.getTagAttrMap().get("_lang");
            if ("sql".equalsIgnoreCase(lang)) {
                String script = node.getTextBody();
                getSqlStringDependencyMapNext(dependencies, script);
            }
        }

        for (Map.Entry<String, String> entry : node.getTagAttrMap().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String feature = null;
            List<String> features = node.getAttrFeatureMap().get(key);
            if (features != null && !features.isEmpty()) {
                feature = features.get(0);
            }
            if (feature == null) {
                if (AttrConsts.TEST.equals(key)) {
                    feature = FeatureConsts.EVAL;
                }
            }
            if (feature != null) {
                if (Arrays.asList(FeatureConsts.EVAL_TINYSCRIPT,
                        FeatureConsts.EVAL_TS).contains(feature)) {
                    getTinyScriptDependencyMapNext(dependencies, value);
                } else if (Arrays.asList(FeatureConsts.EVAL_GROOVY,
                        FeatureConsts.EVAL_JAVA,
                        FeatureConsts.EVAL_JS).contains(feature)) {
                    getEvalScriptDependencyMapNext(dependencies, value);
                }
            }
        }

        List<XmlNode> children = node.getChildren();
        if (children != null && !children.isEmpty()) {
            for (XmlNode item : children) {
                getDependencyMapNext(item, dependencies);
            }
        }
    }

    public static void getSqlStringDependencyMapNext(Set<String> dependencies, String script) {
        if (script == null) {
            return;
        }
        try {
            script = script.replaceAll(RegexPattens.SINGLE_LINE_COMMENT_SQL_REGEX, "");
        } catch (Exception e) {

        }
        try {
            script = script.replaceAll(RegexPattens.MULTI_LINE_COMMENT_REGEX, "");
        } catch (Exception e) {

        }
        List<RegexMatchItem> items = RegexUtil.regexFinds(script, "[a-zA-Z0-9_]{3,}\\s*\\(");
        for (RegexMatchItem item : items) {
            String matchStr = item.getMatchStr();
            List<RegexMatchItem> strs = RegexUtil.regexFinds(matchStr, "[a-zA-Z0-9_]{3,}");
            for (RegexMatchItem str : strs) {
                String naming = str.getMatchStr();
                naming = naming.substring(1, naming.length() - 1);
                if (isDependencyNaming(naming)) {
                    dependencies.add(naming);
                }
            }
        }
    }

    public static void getEvalScriptDependencyMapNext(Set<String> dependencies, String script) {
        if (script == null) {
            return;
        }
        script = script.trim();
        if (!script.isEmpty()) {
            List<RegexMatchItem> items = RegexUtil.regexFinds(script, "\\.(call|invoke|exec(AsProcedure)?)\\s*\\((\"[a-zA-Z0-9_]+\"|'[a-zA-Z0-9_]+')\\s*(,|\\))");
            for (RegexMatchItem item : items) {
                String matchStr = item.getMatchStr();
                List<RegexMatchItem> strs = RegexUtil.regexFinds(matchStr, "\"[a-zA-Z0-9_]+\"|'[a-zA-Z0-9_]+'");
                for (RegexMatchItem str : strs) {
                    String naming = str.getMatchStr();
                    naming = naming.substring(1, naming.length() - 1);
                    if (isDependencyNaming(naming)) {
                        dependencies.add(naming);
                    }
                }
            }
        }
    }

    public static void getTinyScriptDependencyMapNext(Set<String> dependencies, String script) {
        if (script == null) {
            return;
        }
        script = script.trim();
        if (!script.isEmpty()) {
            try {
                ParseTree tree = TinyScript.parse(script);
                getDependencyMapNext(tree, dependencies);
            } catch (Exception e) {

            }
        }
    }

    public static void getDependencyMapNext(ParseTree tree, Set<String> dependencies) {
        if (tree == null) {
            return;
        }
        if (tree instanceof TinyScriptParser.FunctionCallContext) {
            ParseTree child = tree.getChild(0);
            String naming = child.getText();
            if (isDependencyNaming(naming)) {
                dependencies.add(naming);
            }
        }
        int count = tree.getChildCount();
        for (int i = 0; i < count; i++) {
            ParseTree item = tree.getChild(i);
            getDependencyMapNext(item, dependencies);
        }
    }

    public static boolean isDependencyNaming(String naming) {
        if (naming == null) {
            return false;
        }
        return naming.startsWith("FUN_")
                || naming.startsWith("SP_")
                || naming.startsWith("PKG_")
                || naming.startsWith("F_")
                || naming.startsWith("FUNC_")
                || naming.startsWith("FN_")
                || naming.startsWith("PROC_")
                ;
    }

    @Data
    @NoArgsConstructor
    public static class DependencyNode {
        public String naming;
        public Set<String> dependencies = new LinkedHashSet<>();
        public Set<String> usages = new LinkedHashSet<>();
    }
}
