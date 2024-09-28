package i2f.i18n.parser.impl;

import i2f.i18n.data.I18nItem;
import i2f.i18n.parser.I18nParser;
import i2f.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
 * <pre>
 *     &lt;?xml version="1.0" encoding="UTF-8" ?&gt;
 *     &lt;i18n lang="default"&gt;
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
public class XmlI18nParser implements I18nParser {
    public static final String ATTR_LANG = "lang";
    public static final String TAG_ITEM = "item";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_KEEP = "keep";
    public static final String ATTR_ONLY = "only";
    public static final List<String> ITEM_TAG_NAMES = Collections.singletonList(TAG_ITEM);
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

    public static String firstNotEmpty(String... strs) {
        for (String str : strs) {
            if (str != null && !str.isEmpty()) {
                return str;
            }
        }
        return null;
    }

    public static String trimLeft(String str) {
        if (str == null) {
            return null;
        }
        int i = 0;
        while (i < str.length() && Character.isWhitespace(str.charAt(i))) {
            i++;
        }
        return str.substring(i);
    }

    @Override
    public Collection<I18nItem> parse() {
        StringBuilder builder = new StringBuilder();
        List<I18nItem> ret = new ArrayList<>();
        Node rootNode = XmlUtil.getRootNode(document);
        String rootLang = XmlUtil.getAttribute(rootNode, ATTR_LANG);
        List<Node> itemNodes = XmlUtil.getChildNodes(rootNode, ITEM_TAG_NAMES);
        for (Node itemNode : itemNodes) {
            String retName = XmlUtil.getAttribute(itemNode, ATTR_NAME);
            String retValue = XmlUtil.getNodeContent(itemNode);
            String itemLang = XmlUtil.getAttribute(itemNode, ATTR_LANG);
            String retLang = firstNotEmpty(itemLang, rootLang, lang);
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
                            String str = trimLeft(line);
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
        }
        return ret;
    }
}
