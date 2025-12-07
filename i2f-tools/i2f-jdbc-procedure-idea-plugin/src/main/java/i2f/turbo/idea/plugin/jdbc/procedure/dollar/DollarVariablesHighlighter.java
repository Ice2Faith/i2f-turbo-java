package i2f.turbo.idea.plugin.jdbc.procedure.dollar;


import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import i2f.match.regex.RegexUtil;
import i2f.match.regex.data.RegexMatchItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ice2Faith
 * @date 2025/12/7 21:34
 * @desc
 */
public class DollarVariablesHighlighter implements DocumentListener {
    protected Logger log = Logger.getInstance(DollarVariablesHighlighter.class);

    private final Editor editor;
    private List<RangeHighlighter> highlighters = new ArrayList<>();

    public DollarVariablesHighlighter(@NotNull Editor editor) {
        this.editor = editor;
        init();
    }

    private void init() {
        // 添加文档监听器，在文档变化时更新高亮
        editor.getDocument().addDocumentListener(this);
        // 初始高亮
        updateHighlights();
    }

    public void dispose() {
        // 清理资源
        removeAllHighlights();
        editor.getDocument().removeDocumentListener(this);
    }


    private void updateHighlights() {
        // 移除旧的高亮
        removeAllHighlights();

        // 获取文档文本
        applyAllHighlights();
    }

    private void applyAllHighlights() {
        MarkupModel markupModel = editor.getMarkupModel();
        String text = editor.getDocument().getText();
        List<RegexMatchItem> list = RegexUtil.regexFinds(text, "([$#](\\!)?\\{[^\\}]*\\})|([$][a-zA-Z0-9_]+)");
        for (RegexMatchItem item : list) {
//            log.warn("dollar-vars:"+item.matchStr+"["+item.idxStart+","+item.idxEnd+"]");
            TextAttributesKey key = DefaultLanguageHighlighterColors.NUMBER;
//            if(item.matchStr.startsWith("$")){
//                key=DefaultLanguageHighlighterColors.METADATA;
//            }
            if (item.matchStr.replaceAll("\\s+", "").contains("{}")) {
                key = HighlighterColors.BAD_CHARACTER;
            }

            int layer = 5000;


            RangeHighlighter highlighter = markupModel.addRangeHighlighter(
                    key,
                    item.idxStart,
                    item.idxEnd, layer,
                    HighlighterTargetArea.EXACT_RANGE
            );
            highlighters.add(highlighter);

        }
    }

    private void removeAllHighlights() {
        MarkupModel markupModel = editor.getMarkupModel();
        for (RangeHighlighter highlighter : highlighters) {
            if (highlighter != null) {
                markupModel.removeHighlighter(highlighter);
            }
        }
        highlighters.clear();
    }

    // DocumentListener 接口方法
    @Override
    public void beforeDocumentChange(@NotNull DocumentEvent event) {
        // 可以在这里处理文档变化前的逻辑
    }

    private AtomicLong lastUpdateTs = new AtomicLong(0);

    @Override
    public void documentChanged(@NotNull DocumentEvent event) {
        // 文档变化后更新高亮
        long cts = System.currentTimeMillis();
        if ((cts - lastUpdateTs.get()) < 1000) {
            return;
        }
        lastUpdateTs.set(cts);
        updateHighlights();
    }
}