/**
 * 迭代器模式（Iterator）
 * <p>
 * 提供一种方法顺序访问一个聚合对象中的各个元素，而又不暴露其内部表示。
 * 分类：行为型模式
 * </p>
 *
 * <h3>模式说明</h3>
 * <p>迭代器模式的核心思想是将遍历逻辑封装在迭代器对象中，使客户端无需了解聚合对象的内部结构
 * （数组、链表、树等）即可访问其元素。通过定义统一的迭代接口（hasNext/next），
 * 实现"遍历逻辑"与"聚合结构"的解耦。</p>
 *
 * <h3>适用场景</h3>
 * <ul>
 *   <li>需要访问聚合对象内容而不暴露内部表示</li>
 *   <li>需要为聚合对象提供多种遍历方式（正序、倒序、过滤等）</li>
 *   <li>提供统一的遍历接口，简化客户端代码</li>
 * </ul>
 *
 * <h3>典型案例</h3>
 * <ul>
 *   <li>JDK：{@link java.util.Iterator}、{@link java.util.Enumeration}、{@link Iterable} 接口</li>
 *   <li>Spring Data：{@code Page<T>} 与 {@code Slice<T>} 分页迭代</li>
 *   <li>JDBC：{@link java.sql.ResultSet}（游标式迭代数据库结果集）</li>
 *   <li>MyBatis：{@code Cursor} 游标查询（流式迭代大数据集）</li>
 * </ul>
 *
 * <h3>本包实现</h3>
 * <p>以"书架遍历"为场景演示迭代器模式：</p>
 * <ul>
 *   <li>{@link Iterator} —— 抽象迭代器接口（定义 hasNext/next 方法）</li>
 *   <li>{@link Aggregate} —— 抽象聚合接口（定义 createIterator 方法）</li>
 *   <li>{@link Book} —— 聚合元素（书籍实体）</li>
 *   <li>{@link BookShelf} —— 具体聚合（书架，管理多本书）</li>
 *   <li>{@link BookIterator} —— 具体迭代器（遍历书架的迭代器实现）</li>
 *   <li>{@link Test} —— 演示类（展示迭代器的创建、遍历、重置、异常处理等）</li>
 * </ul>
 *
 * <h3>模式结构</h3>
 * <pre>
 *  Iterator（抽象迭代器）          ConcreteIterator（具体迭代器）
 *  ──────────────────────────     ──────────────────────────────────
 *  + hasNext(): boolean           BookIterator
 *  + next(): T                    ├─ bookShelf: BookShelf
 *                                 ├─ position: int
 *                                 ├─ hasNext(): boolean
 *                                 └─ next(): Book
 *
 *  Aggregate（抽象聚合）           ConcreteAggregate（具体聚合）
 *  ──────────────────────────     ──────────────────────────────────
 *  + createIterator(): Iterator   BookShelf
 *                                 ├─ books: List&lt;Book&gt;
 *                                 ├─ addBook(book): void
 *                                 └─ createIterator(): BookIterator
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Iterator
 * @see Aggregate
 * @see Book
 * @see BookShelf
 * @see BookIterator
 * @see Test
 */
package i2f.design.pattern.behavioral.iterator;
