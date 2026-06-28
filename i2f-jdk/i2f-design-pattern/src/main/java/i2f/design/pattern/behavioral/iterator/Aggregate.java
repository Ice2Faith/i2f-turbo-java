/**
 * 迭代器模式（Iterator）—— 聚合接口
 *
 * <p><b>角色：</b>抽象聚合（Abstract Aggregate）</p>
 *
 * <p><b>模式说明：</b>定义创建迭代器的接口，声明 iterator() 方法返回一个迭代器实例。
 * 聚合对象负责管理一组元素，但遍历时由迭代器负责，实现"职责分离"。</p>
 *
 * <p><b>命名立意：</b>书架（BookShelf）作为聚合对象，管理多本书的存储和访问，
 * 通过 createIterator() 方法创建专门的迭代器来遍历书籍。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Iterator
 * @see Book
 */
package i2f.design.pattern.behavioral.iterator;

/**
 * 抽象聚合接口 —— 定义创建迭代器的方法
 *
 * @param <T> 聚合元素类型
 */
public interface Aggregate<T> {

    /**
     * 创建一个迭代器实例。
     *
     * @return 用于遍历聚合对象的迭代器
     */
    Iterator<T> createIterator();
}
