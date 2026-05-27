package i2f.design.pattern.behavioral.iterator.book;

import i2f.design.pattern.behavioral.iterator.Aggregate;
import i2f.design.pattern.behavioral.iterator.Iterator;

import java.util.ArrayList;
import java.util.List;

/**
 * 迭代器模式（Iterator）—— 书架（ConcreteAggregate：BookShelf）
 *
 * <p><b>角色：</b>具体聚合（Concrete Aggregate）</p>
 *
 * <p><b>模式说明：</b>实现 Aggregate 接口，管理一组 Book 对象（内部使用 ArrayList 存储）。
 * 通过 createIterator() 方法创建并返回一个 BookIterator 实例，
 * 将遍历职责委托给迭代器，符合"单一职责原则"。</p>
 *
 * <p><b>命名立意：</b>书架天然就是一个聚合对象——它存放多本书，提供添加/移除书籍的方法，
 * 并通过迭代器让外部能够依次访问每本书，而无需暴露内部存储结构。</p>
 *
 * <p><b>模式结构：</b></p>
 * <pre>
 *  Aggregate（抽象聚合）
 *    └─ createIterator(): Iterator
 *
 *  ConcreteAggregate（具体聚合）
 *    └─ BookShelf
 *       ├─ books: List<Book>              ← 内部存储（可替换为数组、链表等）
 *       ├─ addBook(book): void
 *       ├─ removeBook(isbn): boolean
 *       └─ createIterator(): BookIterator ← 返回专用迭代器
 * </pre>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Book
 * @see BookIterator
 * @see Aggregate
 */
public class BookShelf implements Aggregate<Book> {

    /**
     * 书架上存储的书籍列表。
     *
     * <p>使用 ArrayList 作为内部存储，但客户端无需知道这一点——
     * 即使将来改为链表、数组或其他结构，只要迭代器实现相应调整，客户端代码完全不受影响。</p>
     */
    private final List<Book> books = new ArrayList<>();

    /**
     * 添加书籍到书架。
     *
     * @param book 要添加的书籍
     */
    public void addBook(Book book) {
        books.add(book);
        System.out.println("  [书架] 添加书籍：" + book.getTitle());
    }

    /**
     * 根据 ISBN 移除书籍。
     *
     * @param isbn 要移除的书籍 ISBN
     * @return 如果成功移除返回 true，否则返回 false
     */
    public boolean removeBook(String isbn) {
        boolean removed = books.removeIf(book -> book.getIsbn().equals(isbn));
        if (removed) {
            System.out.println("  [书架] 移除书籍 ISBN：" + isbn);
        }
        return removed;
    }

    /**
     * 获取书架上的书籍总数。
     *
     * @return 书籍数量
     */
    public int getBookCount() {
        return books.size();
    }

    /**
     * 根据索引获取书籍（仅供演示使用，实际场景中不建议暴露此方法）。
     *
     * @param index 索引
     * @return 指定位置的书籍
     */
    public Book getBook(int index) {
        if (index < 0 || index >= books.size()) {
            throw new IndexOutOfBoundsException("索引越界：" + index);
        }
        return books.get(index);
    }

    /**
     * 创建迭代器 —— 工厂方法模式的应用。
     *
     * <p>每次调用都返回全新的迭代器实例，支持对同一聚合对象进行多次独立遍历。</p>
     *
     * @return 用于遍历书架的 BookIterator 实例
     */
    @Override
    public Iterator<Book> createIterator() {
        return new BookIterator(this);
    }

    /**
     * 获取内部书籍列表（仅供迭代器使用，对外应隐藏）。
     *
     * @return 书籍列表
     */
    List<Book> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return String.format("BookShelf[共%d本书]", books.size());
    }
}
