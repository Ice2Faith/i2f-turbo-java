package i2f.design.pattern.behavioral.iterator;

import i2f.design.pattern.behavioral.iterator.book.Book;
import i2f.design.pattern.behavioral.iterator.book.BookIterator;
import i2f.design.pattern.behavioral.iterator.book.BookShelf;

/**
 * 迭代器模式 —— 调用演示
 *
 * <p>演示迭代器模式的核心机制：客户端通过迭代器（{@link BookIterator}）顺序访问
 * 聚合对象（{@link BookShelf}）中的元素，无需了解聚合对象的内部存储结构，
 * 实现"遍历逻辑"与"聚合结构"的解耦。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
public class Test {
    public static void main(String[] args) {

        // ==================== 1. 迭代器模式核心演示 ====================
        System.out.println("====== 迭代器模式（Iterator）演示 ======");
        System.out.println("场景：书架（Aggregate）通过迭代器（Iterator）遍历书籍（Element）");
        System.out.println("      客户端无需知道书架内部如何存储书籍（数组、链表等）\n");

        // ==================== 2. 创建书架并添加书籍 ====================
        System.out.println("────── 初始化书架 ──────");
        BookShelf bookShelf = new BookShelf();

        bookShelf.addBook(new Book("深入理解 Java 虚拟机", "周志明", "978-7-111-64003-9", 2019));
        bookShelf.addBook(new Book("Effective Java", "Joshua Bloch", "978-0-13-468599-1", 2018));
        bookShelf.addBook(new Book("设计模式：可复用面向对象软件的基础", "Erich Gamma", "978-7-111-07575-5", 2007));
        bookShelf.addBook(new Book("重构：改善既有代码的设计", "Martin Fowler", "978-7-115-50899-3", 2019));
        bookShelf.addBook(new Book("代码整洁之道", "Robert C. Martin", "978-7-115-21687-8", 2010));

        System.out.println("\n书架状态：" + bookShelf + "\n");

        // ==================== 3. 使用迭代器遍历书架 ====================
        System.out.println("────── 使用迭代器遍历书架（第 1 次） ──────");
        Iterator<Book> iterator1 = bookShelf.createIterator();

        int bookNumber = 1;
        while (iterator1.hasNext()) {
            Book book = iterator1.next();
            System.out.println("  第 " + bookNumber + " 本：" + book);
            bookNumber++;
        }

        System.out.println("\n迭代器状态：" + iterator1 + "\n");

        // ==================== 4. 验证：每次 createIterator() 创建新实例 ====================
        System.out.println("====== 验证：每次 createIterator() 创建独立迭代器实例 ======");
        Iterator<Book> iterator2 = bookShelf.createIterator();
        Iterator<Book> iterator3 = bookShelf.createIterator();

        System.out.println("iterator2: " + iterator2);
        System.out.println("iterator3: " + iterator3);
        System.out.println("iterator2 == iterator3 ? " + (iterator2 == iterator3));
        System.out.println("两个迭代器可以独立遍历同一书架，互不干扰\n");

        // ==================== 5. 多次独立遍历演示 ====================
        System.out.println("────── 多次独立遍历演示 ──────");
        System.out.println("迭代器 A（只读前 3 本）：");
        Iterator<Book> iteratorA = bookShelf.createIterator();
        for (int i = 0; i < 3 && iteratorA.hasNext(); i++) {
            System.out.println("  " + iteratorA.next().getTitle());
        }
        System.out.println("  迭代器 A 当前位置：" + ((BookIterator) iteratorA).getPosition() + "\n");

        System.out.println("迭代器 B（从头开始遍历）：");
        Iterator<Book> iteratorB = bookShelf.createIterator();
        while (iteratorB.hasNext()) {
            System.out.println("  " + iteratorB.next().getTitle());
        }
        System.out.println("  迭代器 B 当前位置：" + ((BookIterator) iteratorB).getPosition() + "\n");

        // ==================== 6. 演示 reset() 功能 ====================
        System.out.println("====== 演示：迭代器 reset() 重置功能 ======");
        BookIterator resettableIterator = new BookIterator(bookShelf);

        System.out.println("第一次遍历：");
        while (resettableIterator.hasNext()) {
            Book book = resettableIterator.next();
            System.out.println("  " + book.getTitle());
        }
        System.out.println("遍历完成，当前位置：" + resettableIterator.getPosition());

        System.out.println("\n调用 reset() 重置迭代器...");
        resettableIterator.reset();
        System.out.println("重置后位置：" + resettableIterator.getPosition());

        System.out.println("\n第二次遍历（从开头重新开始）：");
        while (resettableIterator.hasNext()) {
            Book book = resettableIterator.next();
            System.out.println("  " + book.getTitle());
        }

        System.out.println();

        // ==================== 7. 演示异常处理 ====================
        System.out.println("====== 演示：遍历完成后继续调用 next() 会抛出异常 ======");
        Iterator<Book> exhaustedIterator = bookShelf.createIterator();
        while (exhaustedIterator.hasNext()) {
            exhaustedIterator.next();
        }

        try {
            System.out.println("尝试在遍历完成后调用 next()...");
            exhaustedIterator.next();
        } catch (IllegalStateException e) {
            System.out.println("  捕获异常：" + e.getMessage());
        }

        System.out.println();

        // ==================== 8. 演示移除元素后遍历 ====================
        System.out.println("====== 演示：书架移除元素后重新遍历 ======");
        System.out.println("移除 ISBN 为 978-7-111-64003-9 的书籍...");
        bookShelf.removeBook("978-7-111-64003-9");
        System.out.println("书架状态：" + bookShelf + "\n");

        System.out.println("重新遍历书架：");
        Iterator<Book> iteratorAfterRemove = bookShelf.createIterator();
        bookNumber = 1;
        while (iteratorAfterRemove.hasNext()) {
            System.out.println("  第 " + bookNumber + " 本：" + iteratorAfterRemove.next());
            bookNumber++;
        }

        System.out.println();

        // ==================== 9. 模式优势总结 ====================
        System.out.println("====== 迭代器模式优势总结 ======");
        System.out.println("1. 封装遍历逻辑：客户端无需了解聚合对象的内部结构（数组、链表、树等）");
        System.out.println("2. 支持多种遍历方式：可为同一聚合对象创建不同类型的迭代器（正序、倒序、过滤等）");
        System.out.println("3. 多次独立遍历：每次 createIterator() 返回新实例，互不干扰");
        System.out.println("4. 符合单一职责原则：聚合对象负责存储，迭代器负责遍历");
        System.out.println("5. 符合开闭原则：新增遍历方式只需新增迭代器类，无需修改聚合对象");
        System.out.println("\nJDK 典型案例：");
        System.out.println("  - java.util.Iterator 接口（所有集合类都支持）");
        System.out.println("  - foreach 循环底层基于 Iterator 实现");
        System.out.println("  - JDBC ResultSet（游标式迭代数据库结果集）");
    }
}
