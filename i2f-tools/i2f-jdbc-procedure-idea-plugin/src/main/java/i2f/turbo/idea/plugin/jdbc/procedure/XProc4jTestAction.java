package i2f.turbo.idea.plugin.jdbc.procedure;

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.ex.FileEditorProviderManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.testFramework.LightVirtualFile;
import i2f.jdbc.procedure.consts.AttrConsts;
import i2f.jdbc.procedure.context.ProcedureMeta;
import i2f.jdbc.procedure.executor.impl.DefaultJdbcProcedureExecutor;
import i2f.jdbc.procedure.parser.JdbcProcedureParser;
import i2f.jdbc.procedure.parser.data.XmlNode;
import i2f.jdbc.procedure.reporter.IGrammarReporter;
import i2f.turbo.idea.plugin.utils.IdeaExceptionUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class XProc4jTestAction extends AnAction {
    public static final Logger log = Logger.getInstance(XProc4jTestAction.class);


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
        private Project project;
        private String initialText;
        private LightVirtualFile virtualFile;
        private FileEditor fileEditor;
        private Editor inputArea;
        private JTextArea outputArea;
        private JButton parseButton;

        public ConvertDialog(Project project, String initialText) {
            super(project);
            this.project = project;
            this.initialText = initialText;
            init(); // 初始化对话框
            setTitle(XProc4jConsts.NAME + " Test UI");
        }

        public Language getXmlLanguage(){
            Language language = Language.findLanguageByID("Xml");
            if(language==null){
                language=Language.findLanguageByID("Html");
            }
            if(language==null){
                language= PlainTextLanguage.INSTANCE;
            }
            return language;
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

            Language language=getXmlLanguage();
            FileType fileType = language.getAssociatedFileType();
            if (fileType == null) {
                fileType = PlainTextFileType.INSTANCE;
            }

            JComponent editorComponent = null;

            virtualFile = new LightVirtualFile("Virtual.xml", fileType, initialText == null ? "" : initialText);
            virtualFile.setWritable(true);

            // 2. 找到该文件类型对应的默认 FileEditorProvider (通常是 TextEditorProvider)
            List<FileEditorProvider> providers = FileEditorProviderManager.getInstance().getProviderList(project, virtualFile);

            if (providers != null && !providers.isEmpty()) {
                fileEditor = providers.get(0).createEditor(project, virtualFile);
                editorComponent = fileEditor.getComponent();
            } else {
                // 2. 创建可编辑的 Document 和 Editor
                // 初始内容设为空字符串，createEditor 的最后一个参数 false 表示【可编辑】
                Document document = EditorFactory.getInstance().createDocument(initialText == null ? "" : initialText);
                inputArea = EditorFactory.getInstance().createEditor(document, project, fileType, false);

                // 3. 配置编辑器设置（开启行号、折叠、软换行等）
                EditorSettings settings = inputArea.getSettings();
                settings.setLineNumbersShown(true);
                settings.setFoldingOutlineShown(true);
                settings.setAdditionalColumnsCount(3);
                settings.setAdditionalLinesCount(3);
                settings.setUseSoftWraps(true); // 开启软换行，提升编辑体验
                settings.setLanguageSupplier(() -> language);
                settings.setWheelFontChangeEnabled(true);

                // 4. 显式设置语法高亮（确保高亮与 Language 完美匹配）
                if (inputArea instanceof EditorEx) {
                    EditorEx editorEx = (EditorEx) inputArea;
                    editorEx.setHighlighter(
                            EditorHighlighterFactory.getInstance().createEditorHighlighter(project, fileType)
                    );
                }

                editorComponent = inputArea.getComponent();
            }


            JScrollPane inputScrollPane = new JScrollPane(editorComponent);

            JPanel inputRow = new JPanel(new BorderLayout(5, 0));
            inputRow.setPreferredSize(new Dimension(720, 480));
            inputRow.add(inputLabel, BorderLayout.NORTH);
            inputRow.add(inputScrollPane, BorderLayout.CENTER);
            mainPanel.add(inputRow, gbc);


            // --- 第二行：单选框组 + 转换按钮 ---
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 1.0; // 让输出框占据剩余垂直空间


            parseButton = new JButton("Parse");

            // 布局第二行
            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            actionPanel.add(parseButton);

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

            return mainPanel;
        }

        @Override
        protected @NotNull Action[] createActions() {
            return new Action[]{this.getOKAction()};
        }

        @Override
        protected void dispose() {
            if (fileEditor != null) {
                fileEditor.dispose();
                fileEditor = null;
            }
            if (virtualFile != null) {
                virtualFile = null;
            }
            if (inputArea != null) {
                EditorFactory.getInstance().releaseEditor(inputArea);
                inputArea = null;
            }
            super.dispose();
        }

        public String getEditorText(){
            if(fileEditor!=null){
                return virtualFile.getContent().toString();
            }
            return inputArea.getDocument().getText();
        }

        /**
         * 处理转换按钮点击事件
         */
        private void onParseClicked(ActionEvent e) {
            String sourceText = getEditorText();
            if (sourceText == null || sourceText.trim().isEmpty()) {
                outputArea.setText("Please input text!");
                return;
            }

            StringBuffer buffer = new StringBuffer();

            buffer.append("begin parse xml ...").append("\n");
            try {

                Map<String, XmlNode> ret = new HashMap<>();
                XmlNode node = null;
                try {
                    node = JdbcProcedureParser.parse(sourceText);
                } catch (Exception ex) {
                    node = JdbcProcedureParser.parse(
                            "<procedure id=\"VIRTUAL_" + (UUID.randomUUID().toString().replace("-", "")) + "\">\n" +
                                    sourceText +
                                    "\n</procedure>"
                    );
                }
                buffer.append("parse xml success .").append("\n");
                String id = node.getTagAttrMap().get(AttrConsts.ID);
                if (id != null) {
                    buffer.append("xml node found id: ").append(id).append("\n");
                    ret.put(id, node);

                    Map<String, XmlNode> next = new HashMap<>();
                    JdbcProcedureParser.resolveEmbedIdNode(node, next);
                    for (Map.Entry<String, XmlNode> entry : next.entrySet()) {
                        ret.put(entry.getKey(), entry.getValue());
                        if (!entry.getKey().equals(id)) {
                            String childId = id + "." + entry.getKey();
                            buffer.append("xml node found children id: ").append(childId).append("\n");
                            ret.put(childId, entry.getValue());
                        }
                    }
                }

                buffer.append("begin report grammar ...").append("\n");
                buffer.append("[WARN] Grammar reports may produce false positives and are for reference only.\n" +
                                "Please rely on your actual runtime environment,\n" +
                                "as this report uses a simple default executor for inference and environments may differ.")
                        .append("\n");

                Map<String, ProcedureMeta> validMap = new HashMap<>(JdbcProcedureProjectMetaHolder.PROCEDURE_META_MAP);

                AtomicInteger reportCount = new AtomicInteger(0);
                AtomicInteger nodeCount = new AtomicInteger(1);

                buffer.append("init default executor ...").append("\n");
                DefaultJdbcProcedureExecutor executor = new DefaultJdbcProcedureExecutor();
                executor.debug(true);
                executor.debugThread(true);
                executor.applyThreadLogAppender((str) -> {
                    buffer.append(str).append("\n");
                });

                IGrammarReporter reporter = executor.grammarReporter();
                if (reporter != null) {
                    buffer.append("grammar reporting ...").append("\n");
                    reporter.reportGrammar(node, validMap, executor, (str) -> {
                        buffer.append(str).append("\n");
                    }, reportCount, nodeCount);
                    buffer.append("found issue statistic, issue:").append(reportCount.get()).append(", nodes:").append(nodeCount.get()).append("\n");
                }

                buffer.append("\n");
                buffer.append("finished!").append("\n");
                outputArea.setText(buffer.toString());
            } catch (Throwable ex) {
                StringWriter writer = new StringWriter();
                PrintWriter pw = new PrintWriter(writer);
                pw.println("parse error :\n" + ex.getClass() + " : " + ex.getMessage());
                pw.println(IdeaExceptionUtil.getThrowableStackTraceText(ex));
                pw.flush();

                buffer.append("\n");
                buffer.append(writer);
                outputArea.setText(buffer.toString());
            }
        }

    }
}
