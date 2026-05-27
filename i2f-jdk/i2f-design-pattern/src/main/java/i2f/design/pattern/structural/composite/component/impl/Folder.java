package i2f.design.pattern.structural.composite.component.impl;

import i2f.design.pattern.structural.composite.component.FileSystemComponent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合模式 —— 文件夹（Composite：Folder）
 *
 * <p><b>角色：</b>组合节点（Composite）</p>
 *
 * <p><b>说明：</b>文件夹可以包含文件和其他文件夹，形成树形结构。
 * 它实现抽象组件的接口，并维护一个子组件列表。
 * 对于业务方法（showDetail/calculateSize），它会递归调用所有子组件的对应方法。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21
 * @see FileSystemComponent
 * @see File
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Folder extends FileSystemComponent {

    /**
     * 子组件列表。
     */
    private List<FileSystemComponent> children = new ArrayList<>();

    public Folder(String name) {
        super(name);
    }

    @Override
    public void add(FileSystemComponent component) {
        children.add(component);
        component.setParent(this);
    }

    @Override
    public void remove(FileSystemComponent component) {
        children.remove(component);
        component.setParent(null);
    }

    @Override
    public FileSystemComponent getChild(int index) {
        if (index < 0 || index >= children.size()) {
            throw new IndexOutOfBoundsException("索引越界: " + index);
        }
        return children.get(index);
    }

    @Override
    public List<FileSystemComponent> getChildren() {
        return new ArrayList<>(children);
    }

    @Override
    public void showDetail(String indent) {
        System.out.println(indent + "📁 " + getName() + "/");

        // 递归显示所有子组件
        String childIndent = indent + "  ";
        for (FileSystemComponent child : children) {
            child.showDetail(childIndent);
        }
    }

    @Override
    public long calculateSize() {
        long totalSize = 0;

        // 递归计算所有子组件大小之和
        for (FileSystemComponent child : children) {
            totalSize += child.calculateSize();
        }

        return totalSize;
    }

    @Override
    public String getType() {
        return "文件夹";
    }

    /**
     * 获取子组件数量。
     *
     * @return 子组件数量
     */
    public int getChildCount() {
        return children.size();
    }

    @Override
    public String toString() {
        return String.format("Folder{name='%s', children=%d}",
                getName(), children.size());
    }
}
