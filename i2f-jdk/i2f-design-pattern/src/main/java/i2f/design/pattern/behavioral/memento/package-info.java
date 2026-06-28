/**
 * 备忘录模式（Memento）
 * <p>
 * 在不破坏封装性的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态，以便恢复原先状态。
 * 分类：行为型模式
 * </p>
 * 
 * <p><b>模式结构：</b></p>
 * <pre>
 *  Originator（发起人）—— 需要保存状态的对象（如：文本编辑器、游戏角色）
 *    └─ createMemento(): Memento        ← 创建备忘录，保存当前状态
 *    └─ restoreMemento(Memento): void   ← 从备忘录恢复状态
 * 
 *  Memento（备忘录）—— 存储发起人内部状态的容器
 *    └─ 通常包含状态的快照，对外部不可见（封装性）
 * 
 *  Caretaker（管理者）—— 保存和管理备忘录，但不能修改备忘录内容
 *    └─ 保存多个备忘录，支持撤销/重做操作
 * </pre>
 * 
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>需要保存对象的历史状态，支持撤销操作（如：文本编辑器的 Ctrl+Z）</li>
 *   <li>需要事务回滚机制（如：数据库事务）</li>
 *   <li>直接暴露内部状态会破坏封装性的场景</li>
 * </ul>
 * 
 * <p><b>典型案例：</b></p>
 * <ul>
 *   <li>JDK：序列化机制（Serializable）、对象克隆（clone()）</li>
 *   <li>Spring：BindingResult 保存数据绑定状态快照、SavedRequest 保存请求状态</li>
 *   <li>数据库：undo log 实现事务回滚</li>
 * </ul>
 * 
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
package i2f.design.pattern.behavioral.memento;
