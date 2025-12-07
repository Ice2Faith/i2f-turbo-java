package i2f.turbo.idea.plugin.jdbc.procedure.dollar;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/12/7 22:14
 * @desc
 */
public class DollarVariablesEditorFactoryListener implements EditorFactoryListener {
    protected Logger log = Logger.getInstance(DollarVariablesEditorFactoryListener.class);

    private Map<Editor, DollarVariablesHighlighter> editors = new HashMap<>();


    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
        Editor editor = event.getEditor();

        // 为每个新创建的编辑器添加高亮器
        DollarVariablesHighlighter highlighter = new DollarVariablesHighlighter(editor);
        editors.put(editor, highlighter);

//        log.warn("dollar-vars-highlighter created");

    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
        Editor editor = event.getEditor();
        DollarVariablesHighlighter highlighter = editors.remove(editor);

        if (highlighter != null) {
            highlighter.dispose();
        }
//        log.warn("dollar-vars-highlighter released");

    }


}