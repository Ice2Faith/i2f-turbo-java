package i2f.turbo.idea.plugin.tinyscript.lang.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import i2f.extension.antlr4.script.tiny.TinyScriptParser;
import i2f.extension.antlr4.script.tiny.impl.TinyScript;
import i2f.turbo.idea.plugin.inject.utils.JsonUtils;
import i2f.turbo.idea.plugin.tinyscript.TinyScriptConsts;
import i2f.turbo.idea.plugin.utils.IdeaExceptionUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class TinyScriptTestAction extends AnAction {
    public static final Logger log = Logger.getInstance(TinyScriptTestAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        String selectedText = null;
        if (editor != null) {
            selectedText = editor.getSelectionModel().getSelectedText();
        }
        if (selectedText == null) {
            selectedText = "";
        }

        ConvertDialog dialog = new ConvertDialog(project, selectedText);
        dialog.show();
    }


    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    public static class ConvertDialog extends DialogWrapper {
        private JTextArea inputArea;
        private JTextArea outputArea;
        private JButton parseButton;
        private JButton evalButton;

        public ConvertDialog(Project project, String initialText) {
            super(project);
            init(); // 初始化对话框
            setTitle(TinyScriptConsts.LANGUAGE_ID + " Test UI");

            // 设置初始输入文本
            if (inputArea != null && initialText != null) {
                inputArea.setText(initialText);
            }
        }

        @Override
        protected @Nullable JComponent createCenterPanel() {
            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setPreferredSize(new Dimension(720, 480));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.gridx = 0;
            gbc.weightx = 1.0;

            // --- 第一行：输入框 ---
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 5.0; // 让输出框占据剩余垂直空间
            JLabel inputLabel = new JLabel("Input:");
            inputArea = new JTextArea(5, 20);
            inputArea.setLineWrap(true);
            inputArea.setWrapStyleWord(true);
            inputArea.setEditable(true);
            inputArea.setBackground(UIManager.getColor("TextField.background")); // 保持背景色一致

            JScrollPane inputScrollPane = new JScrollPane(inputArea);

            JPanel inputRow = new JPanel(new BorderLayout(5, 0));
            inputRow.add(inputLabel, BorderLayout.NORTH);
            inputRow.add(inputScrollPane, BorderLayout.CENTER);
            mainPanel.add(inputRow, gbc);


            // --- 第二行：单选框组 + 转换按钮 ---
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 1.0; // 让输出框占据剩余垂直空间


            parseButton = new JButton("Parse");
            evalButton = new JButton("Eval");

            // 布局第二行
            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            actionPanel.add(parseButton);
            actionPanel.add(evalButton);

            JPanel actionRow = new JPanel(new BorderLayout(10, 0));
            actionRow.add(actionPanel, BorderLayout.CENTER);

            mainPanel.add(actionRow, gbc);

            // --- 第三行：输出框 ---
            gbc.gridy = 2;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 4.0; // 让输出框占据剩余垂直空间
            JLabel outputLabel = new JLabel("Output:");
            outputArea = new JTextArea(5, 20);
            outputArea.setLineWrap(true);
            outputArea.setWrapStyleWord(true);
            outputArea.setEditable(false); // 只读
            outputArea.setBackground(UIManager.getColor("TextField.background")); // 保持背景色一致

            JScrollPane outputScrollPane = new JScrollPane(outputArea);

            JPanel outputRow = new JPanel(new BorderLayout(5, 0));
            outputRow.add(outputLabel, BorderLayout.NORTH);
            outputRow.add(outputScrollPane, BorderLayout.CENTER);
            mainPanel.add(outputRow, gbc);

            // --- 绑定按钮事件 ---
            parseButton.addActionListener(this::onParseClicked);
            evalButton.addActionListener(this::onEvalClicked);

            return mainPanel;
        }

        /**
         * 处理转换按钮点击事件
         */
        private void onParseClicked(ActionEvent e) {
            String sourceText = inputArea.getText();
            if (sourceText == null || sourceText.trim().isEmpty()) {
                outputArea.setText("Please input text!");
                return;
            }

            try {
                TinyScriptParser.ScriptContext ctx = TinyScript.parse(sourceText);
                String result = "parse success !\nchild count:" + ctx.getChildCount();
                outputArea.setText(result);
            } catch (Throwable ex) {
                StringWriter writer = new StringWriter();
                PrintWriter pw = new PrintWriter(writer);
                pw.println("parse error :\n" + ex.getClass() + " : " + ex.getMessage());
                pw.println(IdeaExceptionUtil.getThrowableStackTraceText(ex));
                pw.flush();
                outputArea.setText(writer.toString());
            }
        }

        private void onEvalClicked(ActionEvent e) {
            String sourceText = inputArea.getText();
            if (sourceText == null || sourceText.trim().isEmpty()) {
                outputArea.setText("Please input text!");
                return;
            }

            try {
                Map<String, Object> params = new LinkedHashMap<>();
                Object output = TinyScript.script(sourceText, params);
                String result = "eval success !\nresult type:\n" + (output == null ? null : output.getClass().getSimpleName());
                try {
                    result += "\nresult:" + JsonUtils.toPrettyJson(output);
                } catch (Exception ex) {

                }
                try {
                    result += "\nparams:" + JsonUtils.toPrettyJson(output);
                } catch (Exception ex) {

                }
                outputArea.setText(result);
            } catch (Throwable ex) {
                StringWriter writer = new StringWriter();
                PrintWriter pw = new PrintWriter(writer);
                pw.println("eval error :\n" + ex.getClass() + " : " + ex.getMessage());
                pw.println(IdeaExceptionUtil.getThrowableStackTraceText(ex));
                pw.flush();
                outputArea.setText(writer.toString());
            }
        }

    }
}
