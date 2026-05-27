package i2f.design.pattern.behavioral.iterator.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 迭代器模式（Iterator）—— 书籍实体（Product）
 *
 * <p><b>角色：</b>聚合元素（Element）</p>
 *
 * <p><b>模式说明：</b>这是被聚合和遍历的具体元素对象。迭代器模式中，
 * 聚合对象管理多个此类元素的实例，迭代器负责按顺序访问它们。</p>
 *
 * <p><b>命名立意：</b>以"书架上的书"为场景——每本书有标题、作者、ISBN 等属性，
 * 书架管理多本书，迭代器依次取出每本书供客户端使用。</p>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    /**
     * 书籍标题。
     */
    private String title;

    /**
     * 作者。
     */
    private String author;

    /**
     * ISBN 编号。
     */
    private String isbn;

    /**
     * 出版年份。
     */
    private int publishYear;

    @Override
    public String toString() {
        return String.format("《%s》 by %s [%s, %d年]", title, author, isbn, publishYear);
    }
}
