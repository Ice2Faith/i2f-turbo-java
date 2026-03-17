package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import i2f.extension.antlr4.xproc4j.oracle.grammar.impl.Oracle2Xproc4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Oracle2Xproc4jConvertAction extends AnAction {
    public static final Logger log = Logger.getInstance(Oracle2Xproc4jConvertAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        String selectedText = null;
        if(editor!=null) {
            selectedText=editor.getSelectionModel().getSelectedText();
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
        private JRadioButton radioOptionXproc4j;
        private JRadioButton radioOptionTinyScript;
        private JRadioButton radioOptionOgnl;
        private JTextArea outputArea;
        private JButton convertButton;

        private ButtonGroup radioGroup;

        public ConvertDialog(Project project, String initialText) {
            super(project);
            init(); // 初始化对话框
            setTitle("Oracle to XProc4J convertor");

            // 设置初始输入文本
            if (inputArea != null && initialText!=null) {
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
            radioOptionXproc4j = new JRadioButton("XProc4J");
            radioOptionTinyScript = new JRadioButton("TinyScript");
            radioOptionOgnl = new JRadioButton("Ognl");

            // 默认选中第一个
            radioOptionXproc4j.setSelected(true);

            radioGroup = new ButtonGroup();
            radioGroup.add(radioOptionXproc4j);
            radioGroup.add(radioOptionTinyScript);
            radioGroup.add(radioOptionOgnl);

            convertButton = new JButton("Convert");

            // 布局第二行
            JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            radioPanel.add(radioOptionXproc4j);
            radioPanel.add(radioOptionTinyScript);
            radioPanel.add(radioOptionOgnl);

            JPanel actionRow = new JPanel(new BorderLayout(10, 0));
            actionRow.add(radioPanel, BorderLayout.CENTER);
            actionRow.add(convertButton, BorderLayout.EAST);

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
            convertButton.addActionListener(this::onConvertClicked);

            return mainPanel;
        }

        /**
         * 处理转换按钮点击事件
         */
        private void onConvertClicked(ActionEvent e) {
            String sourceText = inputArea.getText();
            if (sourceText == null || sourceText.trim().isEmpty()) {
                outputArea.setText("Please input text!");
                return;
            }

            ConvertType type = getSelectedType();

            try {
                // 调用你定义的转换函数
                // 注意：如果 convert 方法是静态的，直接调用；如果是实例方法，需要实例化
                if(type==ConvertType.XPROC4J) {
                    String result = Oracle2Xproc4j.convert(sourceText);
                    outputArea.setText(result);
                }else{
                    outputArea.setText("current un-support this type: " + type);
                }
            } catch (Exception ex) {
                outputArea.setText("convert error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        /**
         * 根据单选框状态返回 type 值
         */
        private ConvertType getSelectedType() {
            if (radioOptionXproc4j.isSelected()) return ConvertType.XPROC4J;
            if (radioOptionTinyScript.isSelected()) return ConvertType.TINY_SCRIPT;
            if (radioOptionOgnl.isSelected()) return ConvertType.OGNL;
            return ConvertType.XPROC4J; // 默认
        }

        public enum ConvertType{
            XPROC4J,
            TINY_SCRIPT,
            OGNL
        }

    }
}
