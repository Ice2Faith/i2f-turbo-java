/**
 * 迭代器模式（Iterator）—— 迭代器接口
 *
 * <p><b>角色：</b>抽象迭代器（Abstract Iterator）</p>
 *
 * <p><b>模式说明：</b>定义访问和遍历聚合元素的统一接口，包含 hasNext() 和 next() 方法。
 * 迭代器模式的核心思想是：将遍历逻辑封装在迭代器中，使客户端无需了解聚合对象的内部结构。</p>
 *
 * <p><b>命名立意：</b>以"书架遍历"为场景——书架（聚合对象）上摆放着多本书，
 * 通过 BookIterator 可以依次访问每一本书，而无需知道书是如何存储的（数组、链表等）。</p>
 *
 * <p><b>与 JDK 的关系：</b>JDK 中的 {@link java.util.Iterator} 接口正是此模式的典型应用，
 * 所有集合类（List、Set、Queue 等）都通过 iterator() 方法返回迭代器实例。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see BookShelf
 * @see BookIterator
 */
package i2f.design.pattern.behavioral.iterator;

/**
 * 抽象迭代器接口 —— 定义遍历聚合对象的统一方法
 *
 * @param <T> 聚合元素类型
 */
public interface Iterator<T> {

    /**
     * 判断是否还有下一个元素。
     *
     * @return 如果还有下一个元素返回 true，否则返回 false
     */
    boolean hasNext();

    /**
     * 获取下一个元素。
     *
     * @return 下一个元素
     * @throws IllegalStateException 如果已经没有更多元素
     */
    T next();
}
