package i2f.extension.agent.javassist.evaluate;

import i2f.compiler.MemoryCompiler;
import i2f.extension.agent.javassist.context.AgentContextHolder;
import i2f.io.file.FileUtil;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2024/8/2 13:48
 * @desc
 */
public class LocalFileExpressionEvaluator {
    public static final String ADDITIONAL_IMPORTS = "\n" +
            "import java.util.concurrent.atomic.*;\n" +
            "import i2f.extension.agent.javassist.context.*;\n" +
            "import i2f.compiler.*;\n" +
            "import i2f.io.file.*;\n" +
            "import i2f.extension.agent.javassist.*;\n" +
            "import i2f.extension.agent.javassist.evaluate.*;\n" +
            "import i2f.extension.agent.javassist.transformer.*;\n" +
            "import i2f.agent.*;\n" +
            "import i2f.agent.transformer.*;\n";

    private static AtomicBoolean hasInterval = new AtomicBoolean(false);
    public static AtomicLong lastExpressionHashCode = new AtomicLong(0L);
    public static AtomicInteger threadSleepMillSeconds = new AtomicInteger(3 * 1000);
    public static AtomicReference<String> expressionFilename = new AtomicReference<>("./expression/expression.java");

    public static void initFileWatchThread() {
        if (hasInterval.getAndSet(true)) {
            return;
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(threadSleepMillSeconds.get());
                    } catch (InterruptedException e) {

                    }
                    if (AgentContextHolder.springApplicationContext != null) {
                        try {
                            File file = new File(expressionFilename.get());
                            if (file.exists() && file.isFile()) {
                                String expression = FileUtil.loadTxtFile(file);
                                int hash = expression.hashCode();
                                // string 发生变化，则执行
                                if (lastExpressionHashCode.getAndSet(hash) != hash) {
                                    expression = expression.trim();
                                    if (!expression.isEmpty()) {
                                        Object ret = MemoryCompiler.evaluateExpression(expression, AgentContextHolder.HOLDER, ADDITIONAL_IMPORTS);
                                        System.out.println("expression:" + ret);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, "local-file-expression-evaluator");
        thread.setDaemon(true);
        System.out.println("local-file-expression-evaluator started.");
        thread.start();
    }
}
