package i2f.i18n.parser.impl;

import i2f.i18n.I18n;
import i2f.i18n.data.I18nItem;
import i2f.i18n.parser.I18nParser;
import i2f.i18n.util.I18nUtil;
import i2f.xml.XmlUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * xml 中加载i18n配置
 * 支持使用\t\r\n的转义
 * XML格式如下
 * i18n为根节点
 * 所有的配置都在i18n根节点下面用item节点表示
 * item节点的name属性表示键名
 * item节点的内容表示键值
 * i18n节点和item节点都支持lang属性，用于指定内容的lang语言
 * 内容的lang采用就近原则，也就是item.lang>i180.lang>文件名.lang
 * item节点支持keep属性，此属性表示不要trim节点的内容，同时去除每行|前面的内容，以保证格式对其
 * item节点支持only属性，此属性表示keep的内容仅需要|符号引导的行
 * ----------------------------------------
 * 另外，为了更好的支持多行文本和特殊的格式需求
 * 支持再i18n根节点下使用ref节点指明一个外部文件引用
 * 实际的内容将会从fileNames中尝试查找
 * 查找文件例如：i18n/i18n-{name}-{lang}.txt
 * <pre>
 *     &lt;?xml version="1.0" encoding="UTF-8" ?&gt;
 *     &lt;i18n lang="default"&gt;
 *         &lt;ref name="app.help"/&gt;
 *         &lt;item name="app.version"&gt;
 *             1.0.0
 *         &lt;/item&gt;
 *         &lt;item name="app.banner" lang="en" keep="true" only="true"&gt;
 *             |-----------------
 *             |name: svc
 *             |active: prod
 *             |fallback
 *             |-----------------
 *         &lt;/item&gt;
 *     &lt;/i18n&gt;
 * </pre>
 *
 * @author Ice2Faith
 * @date 2024/9/28 14:44
 */
@Data
@NoArgsConstructor
public class XmlI18nParser implements I18nParser {
    public static final String ATTR_LANG = "lang";
    public static final String TAG_ITEM = "item";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_KEEP = "keep";
    public static final String ATTR_ONLY = "only";
    public static final String TAG_REF = "ref";
    public static final List<String> ITEM_TAG_NAMES = Arrays.asList(TAG_ITEM, TAG_REF);
    public static final String[] DEFAULT_SUFFIXES = {
            ".txt",
            ".xml"
    };
    public static final String[] DEFAULT_FILE_NAMES = I18nUtil.cartesianProduct(
            Arrays.asList(
                    Arrays.asList(I18nUtil.DEFAULT_TYPES),
                    Arrays.asList(I18nUtil.DEFAULT_DIRECTORIES),
                    Arrays.asList(I18nUtil.DEFAULT_FILENAMES),
                    Collections.singletonList("-%s-%s"),
                    Arrays.asList(DEFAULT_SUFFIXES)
            )
    ).toArray(new String[0]);
    protected CopyOnWriteArrayList<String> fileNames = new CopyOnWriteArrayList<>(Arrays.asList(DEFAULT_FILE_NAMES));

    protected String lang;
    protected Document document;

    public XmlI18nParser(String lang, Document document) {
        this.lang = lang;
        this.document = document;
    }

    public XmlI18nParser(String lang, InputStream is) throws Exception {
        this.lang = lang;
        this.document = XmlUtil.parseXml(is);
    }

    public XmlI18nParser(String lang, byte[] xmlBytes) throws Exception {
        this.lang = lang;
        this.document = XmlUtil.parseXml(xmlBytes);
    }

    @Override
    public Collection<I18nItem> parse() {
        StringBuilder builder = new StringBuilder();
        List<I18nItem> ret = new ArrayList<>();
        Node rootNode = XmlUtil.getRootNode(document);
        String rootLang = XmlUtil.getAttribute(rootNode, ATTR_LANG);
        List<Node> itemNodes = XmlUtil.getChildNodes(rootNode, ITEM_TAG_NAMES);
        for (Node itemNode : itemNodes) {
            String tagName = XmlUtil.getTagName(itemNode);
            String retName = XmlUtil.getAttribute(itemNode, ATTR_NAME);
            String itemLang = XmlUtil.getAttribute(itemNode, ATTR_LANG);
            String retLang = I18nUtil.firstNotEmpty(itemLang, rootLang, lang);
            if (TAG_ITEM.equals(tagName)) {
                String retValue = XmlUtil.getNodeContent(itemNode);
                String itemKeep = XmlUtil.getAttribute(itemNode, ATTR_KEEP);
                String itemOnly = XmlUtil.getAttribute(itemNode, ATTR_ONLY);
                boolean keep = false;
                if (itemKeep != null) {
                    keep = true;
                    if ("false".equalsIgnoreCase(itemKeep) || "0".equalsIgnoreCase(itemKeep)) {
                        keep = false;
                    }
                }
                boolean only = false;
                if (itemOnly != null) {
                    only = true;
                    if ("false".equalsIgnoreCase(itemOnly) || "0".equalsIgnoreCase(itemOnly)) {
                        only = false;
                    }
                }
                if (retValue != null) {
                    retValue = I18nParser.unescape(retValue);
                    if (keep) {
                        if (retValue.contains("|")) {
                            builder.setLength(0);
                            String[] lines = retValue.split("\n");
                            boolean isFirst = true;
                            for (int i = 0; i < lines.length; i++) {
                                String line = lines[i];
                                if (!isFirst) {
                                    builder.append("\n");
                                }
                                String str = I18nUtil.trimLeft(line);
                                if (str.startsWith("|")) {
                                    str = str.substring(1);
                                    builder.append(str);
                                    isFirst = false;
                                } else {
                                    if (!only) {
                                        builder.append(line);
                                        isFirst = false;
                                    }
                                }
                            }
                            retValue = builder.toString();
                        }
                    } else {
                        retValue = retValue.trim();
                    }
                }

                if (retName != null) {
                    retName = retName.trim();
                }
                ret.add(new I18nItem(retLang, retName, retValue));
            } else if (TAG_REF.equals(tagName)) {
                try {
                    I18nItem item = resolveRefItem(retLang, retName);
                    if (item != null) {
                        ret.add(item);
                    }
                } catch (Exception e) {

                }
            }
        }
        return ret;
    }


    public I18nItem resolveRefItem(String retLang, String retName) throws IOException {
        InputStream is = findRefStream(retLang, retName);
        if (is == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len = 0;
        while ((len = is.read(buff)) > 0) {
            bos.write(buff, 0, len);
        }
        bos.close();
        is.close();
        return new I18nItem(retLang, retName, new String(bos.toByteArray(), "UTF-8"));
    }

    public InputStream findRefStream(String retLang, String retName) {
        if (retName == null || retName.isEmpty()) {
            return null;
        }
        if (retLang == null || retLang.isEmpty()) {
            retLang = I18n.DEFAULT_LANG;
        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (String fileName : fileNames) {
            boolean isClasspath = true;
            int idx = fileName.indexOf(":");
            if (idx >= 0) {
                String prefix = fileName.substring(0, idx);
                fileName = fileName.substring(idx + 1);
                if ("classpath".equalsIgnoreCase(prefix)
                        || "classpath*".equalsIgnoreCase(prefix)) {
                    isClasspath = true;
                } else if ("file".equalsIgnoreCase(prefix)) {
                    isClasspath = false;
                } else {
                    continue;
                }
            }
            fileName = String.format(fileName, retName, retLang);
            List<String> searchNames = Collections.singletonList(fileName);
            if (I18n.DEFAULT_LANG.equals(retLang)) {
                searchNames = Arrays.asList(fileName, fileName.replace("-" + I18n.DEFAULT_LANG, ""));
            }
            for (String searchName : searchNames) {
                if (isClasspath) {
                    InputStream is = I18nUtil.getClasspathInputStreams(searchName, classLoader);
                    if (is != null) {
                        return is;
                    }
                } else {
                    InputStream is = I18nUtil.getFileInputStream(searchName);
                    if (is != null) {
                        return is;
                    }
                }
            }
        }
        return null;
    }
}
