# 语言注入插件使用指导

- 本插件功能主要提供针对java中的注解进行语言注入的。
- 项目级配置隔离，丰富的语言注入支持。
- 另外也提供一些其他类型的注入点


## 注入点配置

- 类型配置参考：[language-inject-template.jsonc](./docs/language-inject-template.jsonc)

## 注入点类型简介

### annotation

Java 注解的属性值

- Velocity 可用参数：[annotation-template-params.jsonc](./docs/params/annotation-template-params.jsonc)

### xml-attr-value

XML 文件中的标签属性值

- Velocity 可用参数：[xml-attr-value-template-params.jsonc](./docs/params/xml-attr-value-template-params.jsonc)

### xml-tag-body

XML文件中的标签内部内容

- Velocity 可用参数：[xml-tag-body-template-params.jsonc](./docs/params/xml-tag-body-template-params.jsonc)

### xml-text-sql-parameter

XML文件中的标签内部的SQL语句

- Velocity 可用参数：[xml-text-sql-parameter-template-params.jsonc](./docs/params/xml-text-sql-parameter-template-params.jsonc)

### json-prop-value

JSON文件中的属性值

- Velocity 可用参数：[json-prop-value-template-params.jsonc](./docs/params/json-prop-value-template-params.jsonc)

### json-prop-name

JSON文件中的属性名

- Velocity 可用参数：[json-prop-name-template-params.jsonc](./docs/params/json-prop-name-template-params.jsonc)

### properties-value

Properties中的属性值

- Velocity 可用参数：[properties-value-template-params.jsonc](./docs/params/properties-value-template-params.jsonc)


## 使用案例

以Java注解注入为例进行讲解，其他类型参考配置即可。

- 安装本插件
- 在项目根目录下创建文件 `language-inject-template.jsonc` （与 `.idea` 目录同级），内容如下
    - 也可以是 `language-inject-template.json` ，`jsonc` 里面可以写注释，`json` 里面不能写注释

```json
[
  {
    // 注解类型
    "type": "annotation",
    // 注入点，各个类型的配置基本都不一样
    "points": [
      {
        // 注解类型，支持 ant-match 风格匹配
        "type": "com.security.permission.std.annotations.CheckPermissions",
        // 注入的字段名，全字段匹配
        "prop": "value"
      }
    ],
    // 注入类型，各个类型基本是一样的
    "inject": {
      // 注入的语言，IDEA中有的都可以
      "language": "Groovy",
      // 注入是的前后辅助代码
      "prefixTemplate": "class Worker {\n    com.security.permission.impl.DefaultRbacCheckPermissionHelper auth;\n    com.api.model.LoginUser user;\n    org.aspectj.lang.JoinPoint jp;\n    java.lang.Object[] args;\n    #foreach( ${item} in ${method.parameters})\n        ${item.fullType} p${item.index};\n    #end\n\n    def run(){",
      "suffixTemplate": "\n    }\n}"
    }
  }
]
```

- 这个配置是一个数组，其中每一项都是一个注入配置对象
- `type` 属性指定注入点的类型，目前仅支持 `annotation` 注解属性类型
- `points` 属性指定一系列适用于同一个注入规则的注解属性
    - `type` 属性指定注解的类名，可以使用 ant-match 风格
    - `prop` 属性指定对注解的哪个字段进行注入，全匹配
    - 示例配置的就是针对 `CheckPermissions` 注解的 `value` 属性进行注入的
- `inject` 属性指定语言注入的规则
    - `language` 属性指定注入的语言类型，只要是你的 IDEA 包含的语言类型都是支持的
    - `prefixTemplate` 属性指定环绕属性的之前的脚本内容，支持使用 `Velocity` 语言进行渲染脚本
    - `suffixTemplate` 属性指定环绕属性的之后的脚本内容，支持使用 `Velocity` 语言进行渲染脚本

- 可能有点不好理解，那么就直接用案例进行说明
- 下面的针对的注解

```java
import com.api.model.UserVo;
public class Test{
    
    @CheckPermissions(value="auth.hasAllPerms('user:add')")
    public UserVo add(UserVo user){
        
    }
}
```

- 那么，根据上面的配置，对 `CheckPermissions` 注解的 `value` 属性注入 `Groovy` 语言
- 那么注入之后，属性的上下文就变成了

```groovy
class Worker {
    com.security.permission.RbacCheckPermissionHelper auth;
    com.api.model.LoginUser user;
    org.aspectj.lang.JoinPoint jp;
    java.lang.Object[] args;
    com.api.model.UserVo user;

    def run(){
        // 这里往上的部分作为 prefixTemplate
        // ----------------------------------------------------

        // 中间的部分就是表达式部分，这样之前和之后的完整构成了一个完整的代码，就能够进行自动补全了
        auth.hasAllPerms('user:add')

        // ----------------------------------------------------
        // 这里往下的部分作为 suffixTemplate
    }
}
```

- 因此，针对注入语言的部分，是完全符合 `Groovy` 语法的
- 因为 `Groovy` 语法上，始终返回最后一条语句执行的结果，因此，不需要显式的 `return` 语句
- `auth`, `user`, `jp`, `args` 是在 `prefixTemplate` 中固定的成员变量
- `user` 是在 `prefixTemplate` 中使用 `Velocity` 动态渲染出来的成员变量

## 答疑

### `prefixTemplate`/`suffixTemplate` 中可以使用哪些变量？

提供了 `Velocity` 进行动态模板注入语言，就是为了能够进行提取一些代码的元数据，提供更加完善的注入体验。

- 当注解在函数上时，包含下面的 `method` 节点
- 当注解在字段上时，包含下面的 `field` 节点
- 当注解在类上时，包含下面的 `class` 节点
- 注意，在使用 `method` 的时候， `parameters[i].name` 在实际运行得时候，可能会被命名擦除，变为 `arg0`,`arg1`,`arg2`,...
- 因此，需要根据自己的代码决定，或者在 `Template` 中，使用参数名前缀结合 `index` 使用的方式
- 因此，你自己业务中的切面逻辑，也要考虑这个问题，配合插件使用
- 具体可参考： [annotation-template-params.jsonc](./docs/params/annotation-template-params.jsonc)


### 怎么编写 `Velocity` 模板？

在 IDEA 中， `Velocity` 文件以 `.vm` 后缀表示，`.vm` 之前可以添加模板生成后内容的实际文件后缀（例如： `.groovy`）。
这样的文件名，对于 IDEA 来说，他就是一个 `Velocity` 模板文件，想要生成 `groovy` 脚本。
因此，IDEA 会在文件中，具有 `Velocity` 的语法高亮，与 `Groovy` 的语法高亮。
方便编写模板。

- 可以参考: [template.groovy.vm](./docs/template.groovy.vm)


---

**文档版本：** 1.0  
**最后更新：** 2026-04-16  
**维护者：** Ice2Faith