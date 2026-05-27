package i2f.design.pattern.structural.composite.component;

import i2f.design.pattern.structural.composite.component.impl.File;
import i2f.design.pattern.structural.composite.component.impl.Folder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合模式 —— 文件系统组件（Component：FileSystemComponent）
 *
 * <p><b>角色：</b>抽象组件（Component）</p>
 *
 * <p><b>模式说明：</b>声明叶子节点（文件）和组合节点（文件夹）共有的接口，
 * 定义管理子组件的方法（add/remove/getChild）以及业务方法（showDetail/calculateSize）。
 * 这就是组合模式的核心：<b>"对单个对象和组合对象的使用具有一致性"</b>。</p>
 *
 * <p><b>命名立意：</b>以"文件系统"为场景——文件夹可以包含文件和其他文件夹（组合对象），
 * 文件是最小单位（叶子对象）。客户端可以统一对待文件和文件夹，
 * 调用相同的方法显示详情或计算大小，无需关心具体是文件还是文件夹。</p>
 *
 * <p><b>类层次结构：</b></p>
 * <pre>
 *  抽象组件                    叶子节点          组合节点
 *  ─────────────────────────   ──────────────   ────────────────────────
 *  FileSystemComponent         File（文件）      Folder（文件夹）
 *    ├─ add()                                     可以包含多个子组件
 *    ├─ remove()
 *    ├─ getChild()
 *    ├─ showDetail()           显示文件信息       显示文件夹信息及子树
 *    └─ calculateSize()        返回文件大小       递归计算所有子组件大小之和
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21
 * @see File
 * @see Folder
 */
@Data
@NoArgsConstructor
public abstract class FileSystemComponent {

    /**
     * 组件名称。
     */
    protected String name;

    /**
     * 父组件（用于构建树形结构）。
     */
    protected FileSystemComponent parent;

    public FileSystemComponent(String name) {
        this.name = name;
    }

    /**
     * 添加子组件。
     *
     * <p>叶子节点（文件）不支持此操作，组合节点（文件夹）实现此方法。</p>
     *
     * @param component 子组件
     * @throws UnsupportedOperationException 叶子节点调用时抛出异常
     */
    public void add(FileSystemComponent component) {
        throw new UnsupportedOperationException("叶子节点不支持添加子组件");
    }

    /**
     * 移除子组件。
     *
     * <p>叶子节点（文件）不支持此操作，组合节点（文件夹）实现此方法。</p>
     *
     * @param component 子组件
     * @throws UnsupportedOperationException 叶子节点调用时抛出异常
     */
    public void remove(FileSystemComponent component) {
        throw new UnsupportedOperationException("叶子节点不支持移除子组件");
    }

    /**
     * 获取指定索引的子组件。
     *
     * <p>叶子节点（文件）不支持此操作，组合节点（文件夹）实现此方法。</p>
     *
     * @param index 索引
     * @return 子组件
     * @throws UnsupportedOperationException 叶子节点调用时抛出异常
     */
    public FileSystemComponent getChild(int index) {
        throw new UnsupportedOperationException("叶子节点没有子组件");
    }

    /**
     * 获取所有子组件列表。
     *
     * <p>叶子节点（文件）返回空列表，组合节点（文件夹）返回子组件列表。</p>
     *
     * @return 子组件列表
     */
    public List<FileSystemComponent> getChildren() {
        return new ArrayList<>();
    }

    /**
     * 显示组件详情。
     *
     * <p>文件显示自身信息，文件夹显示自身信息及递归显示所有子组件。</p>
     *
     * @param indent 缩进字符串（用于树形展示）
     */
    public abstract void showDetail(String indent);

    /**
     * 计算组件大小。
     *
     * <p>文件返回自身大小，文件夹递归计算所有子组件大小之和。</p>
     *
     * @return 组件大小（字节）
     */
    public abstract long calculateSize();

    /**
     * 获取组件类型描述。
     *
     * @return 类型名称
     */
    public abstract String getType();
}
