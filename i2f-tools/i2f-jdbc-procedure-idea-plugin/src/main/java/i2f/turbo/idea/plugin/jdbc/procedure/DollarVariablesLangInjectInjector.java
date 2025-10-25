package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteral;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.PsiLiteralValue;
import com.intellij.psi.util.PsiEditorUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlText;
import i2f.match.regex.RegexUtil;
import i2f.match.regex.data.RegexMatchItem;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptConstMultilineString;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptConstRenderString;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.elements.TinyScriptConstString;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/6/25 10:18
 */
public class DollarVariablesLangInjectInjector implements MultiHostInjector {
    public static final Logger log = Logger.getInstance(DollarVariablesLangInjectInjector.class);

    public static final WeakHashMap<PsiElement, List<RangeHighlighter>> weakHighlighterMap = new WeakHashMap<>();

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar multiHostRegistrar, @NotNull PsiElement psiElement) {
        highlighterDollarVariables(psiElement);
    }

    @Override
    public @NotNull
    List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return List.copyOf(elementTypeLayerMapping.keySet());
    }

    public static final Map<Class<? extends PsiElement>, Integer> elementTypeLayerMapping = new LinkedHashMap<>() {
        {
            int topLayer = 50000;
            int normalLayer = 500;//100
            // xml 字符串
            put(XmlText.class, normalLayer);
            put(XmlAttribute.class, normalLayer);
            put(XmlAttributeValue.class, topLayer);
            // java 字符串
            put(PsiLiteralExpression.class, topLayer);
            // ts字符串
            put(TinyScriptConstString.class, topLayer);
            put(TinyScriptConstRenderString.class, topLayer);
            put(TinyScriptConstMultilineString.class, topLayer);
            // IDEA 标准的字符串
            put(PsiLiteral.class, topLayer);
            put(PsiLiteralValue.class, topLayer);
        }
    };

    public static void highlighterDollarVariables(PsiElement context) {
        if (context == null) {
            return;
        }
        Editor editor = PsiEditorUtil.findEditor(context);
        if (editor == null) {
            return;
        }
        Class<? extends PsiElement> clazz = context.getClass();
        int layer = 100;
        Integer mappingLayer = elementTypeLayerMapping.get(clazz);
        if (mappingLayer == null) {
            for (Map.Entry<Class<? extends PsiElement>, Integer> entry : elementTypeLayerMapping.entrySet()) {
                Class<? extends PsiElement> item = entry.getKey();
                if (item.isAssignableFrom(clazz)) {
                    mappingLayer = entry.getValue();
                    break;
                }
            }
        }
        if (mappingLayer != null) {
            layer = mappingLayer;
        }
//        log.warn("dollar-vars-layer:"+clazz.getSimpleName()+":"+layer);
        String text = context.getText();
        int startOffset = context.getTextOffset();
        MarkupModel markupModel = editor.getMarkupModel();
        synchronized (weakHighlighterMap) {
            List<RangeHighlighter> weakList = weakHighlighterMap.get(context);
            if (weakList != null) {
                for (RangeHighlighter highlighter : weakList) {
                    try {
                        markupModel.removeHighlighter(highlighter);
                    } catch (Throwable e) {

                    }
                }
            }

            List<RegexMatchItem> list = RegexUtil.regexFinds(text, "[$#](\\!)?\\{[^\\}]*\\}");
            for (RegexMatchItem item : list) {
//            log.warn("dollar-vars:"+item.matchStr+"["+item.idxStart+","+item.idxEnd+"]");
                TextAttributesKey key = DefaultLanguageHighlighterColors.NUMBER;
//            if(item.matchStr.startsWith("$")){
//                key=DefaultLanguageHighlighterColors.METADATA;
//            }
                if (item.matchStr.replaceAll("\\s+", "").contains("{}")) {
                    key = HighlighterColors.BAD_CHARACTER;
                }


                RangeHighlighter highlighter = markupModel.addRangeHighlighter(
                        key,
                        startOffset + item.idxStart,
                        startOffset + item.idxEnd, layer,
                        HighlighterTargetArea.EXACT_RANGE
                );
                weakHighlighterMap.computeIfAbsent(context, k -> new ArrayList<>())
                        .add(highlighter);
            }
        }

    }
}
