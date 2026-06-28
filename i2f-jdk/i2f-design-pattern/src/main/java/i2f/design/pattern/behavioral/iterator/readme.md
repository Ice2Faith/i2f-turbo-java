# 迭代器模式（Iterator Pattern）

> 迭代器模式是一种**行为型设计模式**，它提供一种方法顺序访问一个聚合对象中的各个元素，而又不暴露其内部表示。

---

## 一、核心逻辑

### 1.1 职责分离原则

迭代器模式的核心思想是将**遍历逻辑**与**聚合结构**完全解耦：

- **聚合对象**（如 BookShelf）：负责存储和管理元素，不关心如何遍历
- **迭代器**（如 BookIterator）：负责遍历逻辑，不关心元素如何存储

通过这种分离，客户端代码只需要依赖统一的迭代接口（`hasNext()` / `next()`），无需了解聚合对象内部是使用数组、链表、树还是其他数据结构。

### 1.2 状态封装机制

迭代器内部维护遍历状态（如 `position` 指针），每次调用 `next()` 时：
1. 检查是否还有下一个元素（`hasNext()`）
2. 返回当前元素
3. 推进状态指针

这种状态封装使得：
- **多次独立遍历**：每次 `createIterator()` 创建新实例，互不干扰
- **遍历可重置**：通过 `reset()` 方法可重新从头遍历
- **异常安全**：遍历完成后继续调用 `next()` 会抛出 `IllegalStateException`

### 1.3 开闭原则实践

- **对扩展开放**：新增遍历方式（倒序、过滤、分组等）只需新增迭代器实现类
- **对修改关闭**：无需修改聚合对象代码即可支持新的遍历策略

---

## 二、核心组成

迭代器模式包含 **5 大核心角色**，在本包中的具体实现如下：

| 角色 | 职责 | 本包实现类                                      | 说明 |
|------|------|--------------------------------------------|------|
| **抽象迭代器**<br>(Iterator) | 定义遍历接口 | [`Iterator<T>`](./Iterator.java)           | 声明 `hasNext()` 和 `next()` 方法 |
| **具体迭代器**<br>(ConcreteIterator) | 实现遍历逻辑 | [`BookIterator`](./book/BookIterator.java) | 维护 `position` 状态，持有聚合对象引用 |
| **抽象聚合**<br>(Aggregate) | 定义创建迭代器的接口 | [`Aggregate<T>`](./Aggregate.java)         | 声明 `createIterator()` 工厂方法 |
| **具体聚合**<br>(ConcreteAggregate) | 实现聚合对象 | [`BookShelf`](./book/BookShelf.java)       | 使用 `List<Book>` 存储书籍，返回专用迭代器 |
| **聚合元素**<br>(Element) | 被遍历的具体对象 | [`Book`](./book/Book.java)                 | 书籍实体（标题、作者、ISBN、出版年份） |

### 模式结构图

```
Iterator<T>（抽象迭代器）         BookIterator（具体迭代器）
──────────────────────────     ──────────────────────────────────
+ hasNext(): boolean           ├─ bookShelf: BookShelf
+ next(): T                    ├─ position: int
                               ├─ hasNext(): boolean
                               ├─ next(): Book
                               ├─ getPosition(): int
                               └─ reset(): void

Aggregate<T>（抽象聚合）        BookShelf（具体聚合）
──────────────────────────     ──────────────────────────────────
+ createIterator(): Iterator   ├─ books: List<Book>
                               ├─ addBook(book): void
                               ├─ removeBook(isbn): boolean
                               ├─ getBookCount(): int
                               └─ createIterator(): BookIterator

Book（聚合元素）
──────────────────────────
- title: String
- author: String
- isbn: String
- publishYear: int
```

---

## 三、案例设计解析

### 3.1 业务场景

本包以**"书架遍历"**为场景演示迭代器模式：
- **书架（BookShelf）** 是聚合对象，内部管理多本书的存储
- **书籍（Book）** 是被遍历的元素，包含标题、作者、ISBN、出版年份等属性
- **书籍迭代器（BookIterator）** 负责依次访问书架上的每本书
- **客户端** 通过统一的迭代接口访问书籍，无需知道书架内部使用 `ArrayList` 存储

### 3.2 核心流程演示

查看 [`Test.java`](./Test.java) 可了解以下典型用法：

#### ① 基础遍历

```java
// 创建书架并添加书籍
BookShelf bookShelf = new BookShelf();
bookShelf.addBook(new Book("深入理解 Java 虚拟机", "周志明", "978-7-111-64003-9", 2019));
bookShelf.addBook(new Book("Effective Java", "Joshua Bloch", "978-0-13-468599-1", 2018));
// ... 添加更多书籍

// 创建迭代器并遍历
Iterator<Book> iterator = bookShelf.createIterator();
while (iterator.hasNext()) {
    Book book = iterator.next();
    System.out.println(book);
}
```

**执行流程**：
1. `bookShelf.createIterator()` 创建 `BookIterator` 实例，`position = 0`
2. `iterator.hasNext()` 检查 `position < books.size()`，返回 `true`
3. `iterator.next()` 返回 `books.get(0)`，`position++` 变为 1
4. 重复步骤 2-3，直到 `position == books.size()` 时 `hasNext()` 返回 `false`

#### ② 多次独立遍历

```java
// 创建两个独立的迭代器
Iterator<Book> iteratorA = bookShelf.createIterator();
Iterator<Book> iteratorB = bookShelf.createIterator();

// iteratorA 只遍历前 3 本
for (int i = 0; i < 3 && iteratorA.hasNext(); i++) {
    System.out.println(iteratorA.next().getTitle());
}

// iteratorB 从头开始完整遍历
while (iteratorB.hasNext()) {
    System.out.println(iteratorB.next().getTitle());
}
```

**关键设计**：每次 `createIterator()` 都返回全新的 `BookIterator` 实例，各迭代器的 `position` 状态互不干扰。

#### ③ 迭代器重置

```java
BookIterator resettableIterator = new BookIterator(bookShelf);

// 第一次遍历
while (resettableIterator.hasNext()) {
    System.out.println(resettableIterator.next().getTitle());
}

// 重置后重新遍历
resettableIterator.reset();  // position = 0
while (resettableIterator.hasNext()) {
    System.out.println(resettableIterator.next().getTitle());
}
```

#### ④ 异常处理

```java
Iterator<Book> exhaustedIterator = bookShelf.createIterator();
while (exhaustedIterator.hasNext()) {
    exhaustedIterator.next();
}

// 遍历完成后继续调用 next() 会抛出异常
try {
    exhaustedIterator.next();
} catch (IllegalStateException e) {
    System.out.println("捕获异常：" + e.getMessage());
}
```

#### ⑤ 动态修改后重新遍历

```java
// 移除一本书
bookShelf.removeBook("978-7-111-64003-9");

// 创建新迭代器重新遍历（反映最新状态）
Iterator<Book> iteratorAfterRemove = bookShelf.createIterator();
while (iteratorAfterRemove.hasNext()) {
    System.out.println(iteratorAfterRemove.next());
}
```

### 3.3 设计模式应用要点

1. **工厂方法模式结合**：`BookShelf.createIterator()` 是工厂方法，每次返回新实例
2. **单一职责原则**：`BookShelf` 只负责存储，`BookIterator` 只负责遍历
3. **封装内部结构**：`getBooks()` 使用包级私有访问修饰符，仅对迭代器可见
4. **可扩展性**：可轻松新增 `ReverseBookIterator`（倒序遍历）、`FilterBookIterator`（按条件过滤）等

---

## 四、典型应用场景

迭代器模式广泛应用于以下场景：

### 4.1 集合类遍历（JDK 标准库）

**场景说明**：Java 集合框架（`List`、`Set`、`Queue`、`Map`）统一通过 `Iterator` 接口提供遍历能力。

**典型案例**：
- `ArrayList.iterator()` 返回基于数组索引的迭代器
- `LinkedList.iterator()` 返回基于双向链表的迭代器
- `HashSet.iterator()` 返回基于哈希表的迭代器
- **foreach 循环**底层基于 `Iterator` 实现语法糖

**代码示例**：
```java
List<String> list = Arrays.asList("A", "B", "C");
for (String item : list) {  // 编译器自动转换为 Iterator
    System.out.println(item);
}
```

### 4.2 数据库结果集游标遍历

**场景说明**：JDBC 的 `ResultSet` 本质上是一个迭代器，通过游标逐行访问查询结果，避免一次性加载全部数据到内存。

**典型案例**：
```java
ResultSet rs = statement.executeQuery("SELECT * FROM users");
while (rs.next()) {  // 迭代器模式：hasNext() + next()
    String name = rs.getString("name");
    int age = rs.getInt("age");
}
```

**MyBatis 扩展**：`Cursor<T>` 游标查询支持流式迭代大数据集，内存占用恒定。

### 4.3 分页数据迭代

**场景说明**：处理大量数据时，通过分页迭代器按需加载每一页数据，客户端感知不到分页细节。

**典型案例**：
- **Spring Data**：`Page<T>` 与 `Slice<T>` 提供分页迭代能力
- **自定义分页迭代器**：
  ```java
  public class PaginatedIterator<T> implements Iterator<T> {
      private int currentPage = 0;
      private List<T> currentPageData;
      
      @Override
      public boolean hasNext() {
          return currentPageData != null && 
                 (position < currentPageData.size() || hasMorePages());
      }
      
      @Override
      public T next() {
          if (position >= currentPageData.size()) {
              loadNextPage();  // 自动加载下一页
          }
          return currentPageData.get(position++);
      }
  }
  ```

### 4.4 树形结构遍历

**场景说明**：文件系统目录、组织架构、DOM 树等树形结构需要多种遍历方式（前序、中序、后序、层序）。

**典型案例**：
- **JDK**：`java.nio.file.FileVisitor` 遍历文件树
- **自定义树迭代器**：
  ```java
  public class TreeIterator<T> implements Iterator<T> {
      private Stack<TreeNode<T>> stack = new Stack<>();
      
      public TreeIterator(TreeNode<T> root) {
          stack.push(root);  // 前序遍历初始化
      }
      
      @Override
      public boolean hasNext() {
          return !stack.isEmpty();
      }
      
      @Override
      public T next() {
          TreeNode<T> node = stack.pop();
          // 先压右子节点，再压左子节点（保证左子树先遍历）
          if (node.right != null) stack.push(node.right);
          if (node.left != null) stack.push(node.left);
          return node.value;
      }
  }
  ```

### 4.5 组合模式遍历

**场景说明**：组合模式（Composite）中的树形对象结构需要统一的迭代访问方式。

**典型案例**：
- **Spring**：`CompositeCacheManager` 组合多个 `CacheManager`，通过迭代器依次查找缓存
- **Spring Security**：`ProviderManager` 迭代多个 `AuthenticationProvider` 进行认证
- **GUI 组件树**：遍历容器中的所有子组件

### 4.6 流式数据处理

**场景说明**：处理日志文件、网络数据流、消息队列等无限或超大数据集时，使用迭代器按需消费数据。

**典型案例**：
- **JDK**：`java.util.Scanner` 按行/按 token 迭代输入流
- **Kafka 消费者**：`ConsumerRecords.iterator()` 逐条处理消息
- **RxJava / Reactor**：响应式流 `Flux<T>` 本质上是异步迭代器

### 4.7 多态遍历策略

**场景说明**：同一聚合对象需要支持多种遍历策略（正序、倒序、过滤、分组、随机等）。

**典型案例**：
```java
// 同一书架支持不同遍历策略
Iterator<Book> forwardIterator = bookShelf.createIterator();         // 正序
Iterator<Book> reverseIterator = bookShelf.createReverseIterator();  // 倒序
Iterator<Book> filterIterator = bookShelf.createFilterIterator(      // 过滤
    book -> book.getPublishYear() >= 2018
);
Iterator<Book> randomIterator = bookShelf.createRandomIterator();    // 随机
```

**设计优势**：新增遍历策略无需修改 `BookShelf` 代码，符合开闭原则。

---

## 五、模式优势与局限

### 5.1 优势

✅ **封装内部结构**：客户端无需了解聚合对象的实现细节（数组/链表/树）  
✅ **简化客户端代码**：统一的 `hasNext()` / `next()` 接口  
✅ **支持多种遍历**：可为同一聚合对象创建不同类型的迭代器  
✅ **多次独立遍历**：每次创建新实例，状态互不干扰  
✅ **符合单一职责**：聚合对象负责存储，迭代器负责遍历  
✅ **符合开闭原则**：新增遍历方式只需新增迭代器类  

### 5.2 局限性

⚠️ **增加类数量**：每个聚合对象至少需要额外的迭代器类  
⚠️ **遍历过程中修改集合**：可能导致 `ConcurrentModificationException`（需使用迭代器的 `remove()` 方法）  
⚠️ **不适用于随机访问**：链表结构的迭代器 `next()` 时间复杂度为 O(n)  

---

## 六、相关模式

- **工厂方法模式**：`createIterator()` 方法是工厂方法的典型应用
- **组合模式**：树形结构通常需要迭代器进行遍历
- **访问者模式**：迭代器可用于遍历对象结构，访问者对元素施加操作
- **备忘录模式**：迭代器的状态（如 `position`）可通过备忘录保存和恢复

---

## 参考资料

- GoF《设计模式：可复用面向对象软件的基础》第 9 章：Iterator
- JDK 源码：`java.util.Iterator`、`java.util.Enumeration`、`java.lang.Iterable`
- Spring Data：`Page<T>`、`Slice<T>`、`Cursor<T>`
- MyBatis：`Cursor` 游标查询机制

---

*文档生成时间：2026-05-22*  
*作者：Ice2Faith*
