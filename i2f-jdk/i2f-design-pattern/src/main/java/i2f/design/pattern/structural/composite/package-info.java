/**
 * 组合模式（Composite）
 * <p>
 * 将对象组合成树形结构以表示"部分-整体"的层次结构，使用户对单个对象和组合对象的使用具有一致性。
 * 分类：结构型模式
 * </p>
 *
 * <h3>模式结构</h3>
 * <pre>
 *  Component（抽象组件）
 *    ├─ FileSystemComponent（文件系统组件）
 *    │   ├─ add(component): void
 *    │   ├─ remove(component): void
 *    │   ├─ getChild(index): FileSystemComponent
 *    │   ├─ showDetail(): void
 *    │   └─ calculateSize(): long
 *    │
 *    ├─ Leaf（叶子节点）
 *    │   └─ File（文件）
 *    │
 *    └─ Composite（组合节点）
 *        └─ Folder（文件夹）
 * </pre>
 *
 * <h3>角色说明</h3>
 * <ul>
 *   <li><b>Component（抽象组件）：</b>声明叶子和组合对象共有的接口</li>
 *   <li><b>Leaf（叶子节点）：</b>表示树形结构中的叶子对象，无子节点</li>
 *   <li><b>Composite（组合节点）：</b>存储子组件，实现与子组件相关的操作</li>
 * </ul>
 *
 * <h3>演示入口</h3>
 * <p>运行 {@link i2f.design.pattern.structural.composite.Test#main(String[])} 查看完整演示</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21
 * @see i2f.design.pattern.structural.composite.Test
 */
package i2f.design.pattern.structural.composite;
