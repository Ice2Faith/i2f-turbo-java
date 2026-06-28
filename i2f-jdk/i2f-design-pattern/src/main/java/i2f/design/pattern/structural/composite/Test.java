package i2f.design.pattern.structural.composite;

import i2f.design.pattern.structural.composite.component.FileSystemComponent;
import i2f.design.pattern.structural.composite.component.impl.File;
import i2f.design.pattern.structural.composite.component.impl.Folder;

/**
 * 组合模式 —— 调用演示
 *
 * <p>演示组合模式的核心机制：客户端面向抽象组件（{@link FileSystemComponent}）编程，
 * 统一对待单个对象（文件）和组合对象（文件夹），通过递归操作树形结构。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 组合模式核心演示 ====================
        System.out.println("====== 组合模式（Composite）演示 ======");
        System.out.println("场景：文件系统（Component）通过树形结构组织文件和文件夹");
        System.out.println("      客户端统一对待文件（Leaf）和文件夹（Composite）\n");

        // ==================== 2. 构建文件树结构 ====================
        System.out.println("────── 构建文件系统树形结构 ──────");

        // 根目录：项目文件夹
        Folder projectFolder = new Folder("App_Project");

        // 源码目录
        Folder srcFolder = new Folder("src");
        File mainJava = new File("Main.java", 5120, ".java");
        File configYml = new File("application.yml", 2048, ".yml");
        srcFolder.add(mainJava);
        srcFolder.add(configYml);

        // 资源目录
        Folder resourcesFolder = new Folder("resources");
        File logoPng = new File("logo.png", 102400, ".png");
        File readmeMd = new File("README.md", 4096, ".md");
        resourcesFolder.add(logoPng);
        resourcesFolder.add(readmeMd);

        // 文档目录（包含子文件夹）
        Folder docsFolder = new Folder("docs");
        Folder apiDocsFolder = new Folder("api");
        File userApiDoc = new File("user-api.md", 8192, ".md");
        File orderApiDoc = new File("order-api.md", 10240, ".md");
        apiDocsFolder.add(userApiDoc);
        apiDocsFolder.add(orderApiDoc);

        Folder designDocsFolder = new Folder("design");
        File architectureDoc = new File("architecture.md", 15360, ".md");
        designDocsFolder.add(architectureDoc);

        docsFolder.add(apiDocsFolder);
        docsFolder.add(designDocsFolder);

        // 组装根目录
        projectFolder.add(srcFolder);
        projectFolder.add(resourcesFolder);
        projectFolder.add(docsFolder);

        System.out.println("文件树构建完成！\n");

        // ==================== 3. 显示文件树详情 ====================
        System.out.println("────── 显示完整文件树结构 ──────");
        projectFolder.showDetail("");
        System.out.println();

        // ==================== 4. 计算文件夹大小 ====================
        System.out.println("====== 递归计算文件夹大小 ======");
        System.out.println("项目根目录总大小：" + formatSize(projectFolder.calculateSize()));
        System.out.println("源码目录大小：" + formatSize(srcFolder.calculateSize()));
        System.out.println("文档目录大小：" + formatSize(docsFolder.calculateSize()));
        System.out.println("API文档子目录大小：" + formatSize(apiDocsFolder.calculateSize()));
        System.out.println();

        // ==================== 5. 面向抽象编程演示 ====================
        System.out.println("====== 面向抽象编程 —— 客户端无需知道具体组件类型 ======");
        System.out.println("统一处理文件和文件夹组件：\n");

        FileSystemComponent[] components = {
                mainJava,              // 文件（Leaf）
                srcFolder,             // 文件夹（Composite）
                logoPng,               // 文件（Leaf）
                docsFolder             // 文件夹（Composite）
        };

        for (int i = 0; i < components.length; i++) {
            System.out.println("组件 " + (i + 1) + " [" + components[i].getType() + "]：");
            components[i].showDetail("  ");
            System.out.println("  大小：" + formatSize(components[i].calculateSize()));
            System.out.println();
        }

        // ==================== 6. 动态操作树结构 ====================
        System.out.println("====== 动态操作：添加/移除子组件 ======");

        // 添加新文件
        File newFile = new File("test-case.java", 3072, ".java");
        System.out.println("添加新文件到 src 目录：test-case.java");
        srcFolder.add(newFile);
        System.out.println("src 目录现在包含 " + srcFolder.getChildCount() + " 个组件\n");

        // 移除文件
        System.out.println("从 src 目录移除：application.yml");
        srcFolder.remove(configYml);
        System.out.println("src 目录现在包含 " + srcFolder.getChildCount() + " 个组件\n");

        // 显示更新后的 src 目录
        System.out.println("更新后的 src 目录：");
        srcFolder.showDetail("  ");
        System.out.println();

        // ==================== 7. 验证叶子节点不支持子组件操作 ====================
        System.out.println("====== 验证：叶子节点（文件）不支持子组件操作 ======");
        try {
            File file = new File("test.txt", 1024, ".txt");
            file.add(new File("another.txt", 512, ".txt"));
        } catch (UnsupportedOperationException e) {
            System.out.println("✓ 预期异常：文件不能包含子组件");
            System.out.println("  异常信息：" + e.getMessage());
        }
        System.out.println();

        // ==================== 8. 模式优势总结 ====================
        System.out.println("====== 组合模式优势总结 ======");
        System.out.println("1. 简化客户端代码：统一对待单个对象和组合对象，无需类型判断");
        System.out.println("2. 符合开闭原则：新增组件类型无需修改已有代码");
        System.out.println("3. 递归操作天然支持：树形结构的遍历、计算等操作可通过递归简洁实现");
        System.out.println("4. 层次结构清晰：直观表达\"部分-整体\"的关系");
        System.out.println("5. 动态组合：运行时可以灵活添加/移除子组件，构建树形结构");
    }

    /**
     * 格式化文件大小显示。
     *
     * @param bytes 字节数
     * @return 格式化后的大小字符串
     */
    private static String formatSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
        }
    }
}
