package i2f.turbo.idea.plugin.jdbc.procedure.reference;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.application.ex.ApplicationUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.xml.XmlElementType;
import com.intellij.psi.xml.XmlTag;
import i2f.lru.LruMap;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Ice2Faith
 * @date 2026/1/20 8:46
 * @desc
 */
public class XmlTagReference extends PsiReferenceBase<XmlTag> implements PsiPolyVariantReference {
    public static final Logger log = Logger.getInstance(XmlTagReference.class);

    public static final LinkedBlockingQueue<Map.Entry<String, Project>> xmlTagRefImplQueue = new LinkedBlockingQueue<>();
    public static final LruMap<String, Map<String, PsiClass[]>> xmlTagRefImplHolder = new LruMap<>(10);
    public static final SecureRandom RANDOM = new SecureRandom();

    static {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    ApplicationUtil.tryRunReadAction(() -> {
                        scanXmlTagRefImpl();
                        return null;
                    });
                } catch (Throwable e) {
                    log.warn(e.getMessage(), e);
                }
                try {
                    Thread.sleep(1000);
                } catch (Throwable e) {

                }
            }
        });
        thread.setName("xml-tag-ref-impl-collector");
        thread.setDaemon(true);
        thread.start();
    }

    public static void scanXmlTagRefImpl() {
        while (true) {
            Map.Entry<String, Project> poll = xmlTagRefImplQueue.poll();
            if (poll == null) {
                break;
            }
            String className = poll.getKey();
            Project project = poll.getValue();
            synchronized (xmlTagRefImplHolder) {
                String projectFilePath = project.getProjectFilePath();
                Map<String, PsiClass[]> map = xmlTagRefImplHolder.computeIfAbsent(projectFilePath, k -> new LinkedHashMap<>());
                PsiClass[] classes = map.get(className);
                if (classes == null || RANDOM.nextDouble() < 0.05) {
                    classes = getXmlTagRefImpl(className, project);
                }
                map.put(className, classes);
            }
            try {
                Thread.sleep(30);
            } catch (Exception e) {

            }
        }
    }

    public static PsiClass[] getXmlTagRefImpl(String className, Project project) {
        // 在整个项目（含依赖库）中查找类
        GlobalSearchScope searchScope = GlobalSearchScope.everythingScope(project);
        PsiShortNamesCache shortNamesCache = PsiShortNamesCache.getInstance(project);
        PsiClass[] classes = shortNamesCache.getClassesByName(className, searchScope);
//        log.warn("xml-tag-ref-match-impl:"+(classes==null || classes.length==0?null:(classes.length+":"+classes[0])));
        return classes;
    }

    public XmlTagReference(@NotNull XmlTag element) {
        super(element, TextRange.from(1, element.getName().length()));
        xmlTagRefImplQueue.add(new AbstractMap.SimpleEntry<>(convertToClassName(element), element.getProject()));
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
//        log.warn("xml-tag-ref-elem:"+myElement);
        String tagName = myElement.getName();
        String className = convertToClassName(tagName);
//        log.warn("xml-tag-ref-className:"+className);

        Project project = myElement.getProject();
        String projectFilePath = project.getProjectFilePath();
//        log.warn("xml-tag-ref-project-path:"+projectFilePath);
        PsiClass[] classes =null;
        synchronized (xmlTagRefImplHolder) {
            Map<String, PsiClass[]> map = xmlTagRefImplHolder.computeIfAbsent(projectFilePath, k -> new LinkedHashMap<>());
             classes=map.get(className);
        }
//        log.warn("xml-tag-ref-cache1:"+(classes==null || classes.length==0?null:(classes.length+":"+classes[0])));
        if (classes == null) {
            xmlTagRefImplQueue.add(new AbstractMap.SimpleEntry<>(className, project));
            return new ResolveResult[0];
        }

//        log.warn("xml-tag-ref-cache2:"+(classes==null || classes.length==0?null:(classes.length+":"+classes[0])));

        ResolveResult[] results = new ResolveResult[classes.length];
        for (int i = 0; i < classes.length; i++) {
            results[i] = new PsiElementResolveResult(classes[i]);
        }
//        log.warn("xml-tag-ref-cache3:"+results.length);
        return results;
    }

    @Override
    public PsiElement resolve() {
        ResolveResult[] results = multiResolve(false);
        return results.length == 1 ? results[0].getElement() : null;
    }

//    @Override
//    public boolean isSoft() {
//        return true; // 软引用，找不到引用也不要报红色提示
//    }

    @Override
    public TextRange getRangeInElement() {
        ASTNode tagNode = myElement.getNode();
        if (tagNode == null) {
            return super.getRangeInElement();
        }

        // 遍历子节点，找到 XML_NAME 类型的 token
        ASTNode[] children = tagNode.getChildren(TokenSet.create(XmlElementType.XML_NAME));
        if (children != null && children.length > 0) {
            ASTNode child = children[0];
            int start = child.getStartOffset() - myElement.getTextOffset();
            int end = start + child.getTextLength();
            return TextRange.create(start, end);
        }

        // fallback
        String name = myElement.getName();
        if (name != null) {
            return TextRange.from(1, name.length());
        }
        return super.getRangeInElement();
    }

    public static String convertToClassName(XmlTag tag) {
        String tagName = tag.getName();
        return convertToClassName(tagName);
    }

    public static final Map<String,String> TAG_ALIAS_MAP=new HashMap<>(){
        {
            put("lang-eval-ts", "lang-eval-tinyScript");
            put("lang-eval-tinyscript", "lang-eval-tinyScript");
            put("lang-eval-js", "lang-eval-javascript");
            put("lang-catch", "lang-try");
            put("lang-finally", "lang-try");
            put("lang-otherwise", "lang-choose");
            put("lang-java-import", "lang-eval-java");
            put("lang-java-member", "lang-eval-java");
            put("lang-java-body", "lang-eval-java");
            put("etl-extra", "sql-etl");
            put("etl-load", "sql-etl");
            put("etl-transform", "sql-etl");
            put("etl-before", "sql-etl");
            put("etl-after", "sql-etl");
        }
    };

    public static String convertToClassName(String tagName) {
        String aliasName = TAG_ALIAS_MAP.get(tagName);
        if(aliasName!=null){
            tagName = aliasName;
        }
        String[] arr = tagName.split("_|-");
        StringBuilder builder = new StringBuilder();
        for (String item : arr) {
            item = item.trim();
            if (item.isEmpty()) {
                continue;
            }
            builder.append(item.substring(0, 1).toUpperCase()).append(item.substring(1));
        }
        builder.append("Node");
        return builder.toString();
    }
}
