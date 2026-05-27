# 组合模式（Composite Pattern）

> 将对象组合成树形结构以表示"部分-整体"的层次结构,使用户对单个对象和组合对象的使用具有一致性。

**分类**:结构型模式(Structural Pattern)

---

## 一、核心逻辑

组合模式的核心思想是**统一对待单个对象和组合对象**,通过抽象组件接口屏蔽叶子节点和组合节点的差异,使得客户端可以以相同的方式处理树形结构中的所有对象。

### 关键设计原则

1. **透明性**:客户端面向抽象组件编程,无需关心具体是叶子还是组合节点
2. **递归性**:组合节点通过递归调用子组件的方法实现树形操作
3. **一致性**:叶子节点和组合节点实现相同的接口,保证调用方式统一

---

## 二、核心组成

### 1. 模式角色

| 角色 | 说明 | 本案例实现 |
|------|------|-----------|
| **Component(抽象组件)** | 声明叶子和组合对象共有的接口,定义管理子组件的方法和业务方法 | `FileSystemComponent` |
| **Leaf(叶子节点)** | 表示树形结构中的叶子对象,无子节点,实现抽象组件接口 | `File`(文件) |
| **Composite(组合节点)** | 存储子组件,实现与子组件相关的操作,递归执行业务方法 | `Folder`(文件夹) |

### 2. 核心接口设计

```java
// 抽象组件 - 定义统一接口
public abstract class FileSystemComponent {
    // 管理子组件的方法(叶子节点抛异常)
    public void add(FileSystemComponent component)
    public void remove(FileSystemComponent component)
    public FileSystemComponent getChild(int index)
    
    // 业务方法(抽象,由子类实现)
    public abstract void showDetail(String indent);
    public abstract long calculateSize();
    public abstract String getType();
}
```

### 3. 实现特点

- **安全组合模式**:在抽象组件中提供子组件管理的默认实现(抛出异常),叶子节点继承即可,无需重复代码
- **递归计算**:文件夹的`calculateSize()`递归累加所有子组件大小
- **递归展示**:文件夹的`showDetail()`递归打印树形结构
- **面向抽象编程**:客户端统一使用`FileSystemComponent`类型,无需类型判断

---

## 三、案例设计说明

### 1. 场景选择:文件系统

选择文件系统作为演示场景的原因:
- **天然的树形结构**:文件夹可以包含文件和子文件夹,完美契合组合模式
- **易于理解**: everyone都熟悉文件系统的层次结构
- **操作直观**:显示目录、计算大小等操作能清晰展示递归特性

### 2. 设计实现

#### (1) 构建文件树
```java
// 创建根目录
Folder projectFolder = new Folder("App_Project");

// 创建子目录和文件
Folder srcFolder = new Folder("src");
File mainJava = new File("Main.java", 5120, ".java");
srcFolder.add(mainJava);

// 组装树结构
projectFolder.add(srcFolder);
```

#### (2) 统一操作
```java
// 客户端面向抽象编程,无需关心是文件还是文件夹
FileSystemComponent[] components = {mainJava, srcFolder, logoPng, docsFolder};

for (FileSystemComponent comp : components) {
    comp.showDetail("  ");  // 统一调用
    System.out.println(comp.calculateSize());  // 统一计算
}
```

#### (3) 递归操作
```java
// 文件夹递归计算大小
public long calculateSize() {
    long totalSize = 0;
    for (FileSystemComponent child : children) {
        totalSize += child.calculateSize();  // 递归调用
    }
    return totalSize;
}
```

### 3. 模式优势体现

- **简化客户端代码**:无需`if-else`判断类型,统一调用接口
- **符合开闭原则**:新增组件类型(如快捷方式、链接文件)无需修改已有代码
- **递归操作简洁**:树形结构的遍历、计算天然支持递归
- **动态组合**:运行时灵活添加/移除子组件

---

## 四、典型应用场景

### 1. UI组件树
**场景**:GUI框架中的容器组件和原子组件
- **组合节点**:Panel、Window、Dialog等容器,可包含其他组件
- **叶子节点**:Button、TextBox、Label等原子控件
- **统一操作**:绘制(render)、事件分发、布局计算
- **典型案例**:Java Swing的`Container.add(Component)`,AWT组件体系

### 2. 组织架构管理
**场景**:企业部门层级结构
- **组合节点**:部门、事业部、分公司,可包含子部门和员工
- **叶子节点**:员工
- **统一操作**:计算人力成本、统计人数、权限继承
- **优势**:统一处理组织查询,无需区分部门和个人

### 3. 菜单系统
**场景**:多级菜单导航
- **组合节点**:菜单分类、子菜单,可包含菜单项和子菜单
- **叶子节点**:具体菜单项(点击执行操作)
- **统一操作**:渲染菜单树、权限校验、搜索菜单
- **典型案例**:后台管理系统的侧边栏菜单

### 4. 文件系统/目录结构
**场景**:操作系统文件管理、云存储
- **组合节点**:文件夹、目录
- **叶子节点**:文件
- **统一操作**:计算大小、搜索、复制、删除、权限管理
- **延伸场景**:代码仓库结构、文档管理系统

### 5. 表达式树/语法树
**场景**:编译器、公式计算器、SQL解析
- **组合节点**:二元表达式、函数调用、语句块
- **叶子节点**:常量、变量、字面量
- **统一操作**:求值(evaluate)、代码生成、优化
- **典型案例**:抽象语法树(AST)、正则表达式解析

### 6. XML/JSON文档树
**场景**:文档对象模型(DOM)解析
- **组合节点**:Element、Document,可包含子节点
- **叶子节点**:Text、Attribute、Comment
- **统一操作**:遍历、序列化、查找节点、XPath查询
- **典型案例**:W3C DOM API、Jackson JSON树模型

### 7. 分类/标签体系
**场景**:商品分类、文章标签、知识图谱
- **组合节点**:大类、子类,可包含子分类和具体条目
- **叶子节点**:具体商品、文章、知识点
- **统一操作**:层级查询、路径计算、权限控制
- **优势**:支持无限层级分类,查询逻辑统一

---

## 五、使用建议

### 适用条件
✅ 需要表示对象的部分-整体层次结构(树形)
✅ 希望客户端忽略组合对象与单个对象的差异
✅ 需要对树形结构进行递归操作(遍历、计算、搜索)
✅ 结构相对稳定,但操作可能频繁扩展

### 注意事项
⚠️ 叶子节点的子组件操作需要妥善处理(抛异常或返回空)
⚠️ 深层递归可能导致栈溢出,需考虑树深度控制
⚠️ 透明性与安全性的权衡:透明组合(接口统一)vs 安全组合(接口分离)

---

## 六、参考实现

- **包结构**: `i2f.design.pattern.structural.composite`
- **演示入口**: [Test.java](Test.java) 的 `main` 方法
- **核心类**:
  - 抽象组件: [FileSystemComponent.java](component/FileSystemComponent.java)
  - 叶子节点: [File.java](component/impl/File.java)
  - 组合节点: [Folder.java](component/impl/Folder.java)

---

> **设计原则**: 组合模式体现了**依赖倒置原则**(DIP)和**开闭原则**(OCP),通过抽象组件解耦客户端与具体实现,新增组件类型无需修改已有代码。