package i2f.design.pattern.behavioral.iterator.book;

import i2f.design.pattern.behavioral.iterator.Iterator;

import java.util.List;

/**
 * 迭代器模式（Iterator）—— 书籍迭代器（ConcreteIterator：BookIterator）
 *
 * <p><b>角色：</b>具体迭代器（Concrete Iterator）</p>
 *
 * <p><b>模式说明：</b>实现 Iterator 接口，维护遍历 BookShelf 所需的内部状态（当前位置指针）。
 * 迭代器持有聚合对象的引用，通过 hasNext() 和 next() 方法依次访问元素，
 * 客户端无需了解聚合对象的内部结构（数组、链表、树等）。</p>
 *
 * <p><b>命名立意：</b>BookIterator 专门用于遍历 BookShelf，
 * 使用 position 指针记录当前访问位置，每次 next() 调用后指针前移，
 * 直到遍历完所有书籍。</p>
 *
 * <p><b>关键设计：</b></p>
 * <ul>
 *   <li>迭代器持有聚合对象的引用，通过聚合对象获取元素</li>
 *   <li>维护 position 状态，记录遍历进度</li>
 *   <li>hasNext() 判断是否越界，next() 返回元素并推进指针</li>
 *   <li>每次 createIterator() 都创建新实例，支持多次独立遍历</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Iterator
 * @see BookShelf
 * @see Book
 */
public class BookIterator implements Iterator<Book> {

    /**
     * 被遍历的书架（聚合对象）。
     */
    private final BookShelf bookShelf;

    /**
     * 当前遍历位置（从 0 开始）。
     */
    private int position = 0;

    /**
     * 构造迭代器。
     *
     * @param bookShelf 要遍历的书架
     */
    public BookIterator(BookShelf bookShelf) {
        this.bookShelf = bookShelf;
        this.position = 0;
    }

    /**
     * 判断书架上是否还有下一本书。
     *
     * @return 如果当前位置小于书籍总数返回 true，否则返回 false
     */
    @Override
    public boolean hasNext() {
        List<Book> books = bookShelf.getBooks();
        return position < books.size();
    }

    /**
     * 获取书架上的下一本书，并将位置指针前移。
     *
     * @return 下一本书
     * @throws IllegalStateException 如果已经没有更多书籍
     */
    @Override
    public Book next() {
        if (!hasNext()) {
            throw new IllegalStateException("书架上没有更多书籍了！");
        }

        List<Book> books = bookShelf.getBooks();
        Book currentBook = books.get(position);
        position++;

        return currentBook;
    }

    /**
     * 获取当前遍历位置（仅供调试使用）。
     *
     * @return 当前位置索引
     */
    public int getPosition() {
        return position;
    }

    /**
     * 重置迭代器到初始位置（可选功能）。
     */
    public void reset() {
        this.position = 0;
    }

    @Override
    public String toString() {
        return String.format("BookIterator[position=%d, total=%d]",
                position, bookShelf.getBookCount());
    }
}
