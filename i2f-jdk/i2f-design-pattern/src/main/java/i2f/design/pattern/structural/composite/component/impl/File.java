package i2f.design.pattern.structural.composite.component.impl;

import i2f.design.pattern.structural.composite.component.FileSystemComponent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 组合模式 —— 文件（Leaf：File）
 *
 * <p><b>角色：</b>叶子节点（Leaf）</p>
 *
 * <p><b>说明：</b>文件是文件系统的最小单位，不包含子组件。
 * 它实现抽象组件的接口，但对于管理子组件的方法（add/remove/getChild）
 * 会抛出 UnsupportedOperationException，因为文件不能有子节点。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21
 * @see FileSystemComponent
 * @see Folder
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class File extends FileSystemComponent {

    /**
     * 文件大小（字节）。
     */
    private long size;

    /**
     * 文件扩展名。
     */
    private String extension;

    public File(String name, long size, String extension) {
        super(name);
        this.size = size;
        this.extension = extension;
    }

    @Override
    public void showDetail(String indent) {
        System.out.println(indent + "📄 " + getName() + " [" + formatSize(size) + "]");
    }

    @Override
    public long calculateSize() {
        return size;
    }

    @Override
    public String getType() {
        return "文件";
    }

    /**
     * 格式化文件大小显示。
     *
     * @param bytes 字节数
     * @return 格式化后的大小字符串
     */
    private String formatSize(long bytes) {
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

    @Override
    public String toString() {
        return String.format("File{name='%s', size=%d, extension='%s'}",
                getName(), size, extension);
    }
}
