# Permission 权限管理模块

## 模块概述

该模块是基于 Spring Boot 的 RBAC（Role-Based Access Control）权限控制解决方案，通过 AOP 切面和 SpEL 表达式，为应用提供灵活、声明式的权限验证机制。

## 核心功能

### 1. 声明式权限检查

- 通过 `@CheckPermissions` 注解实现方法级别和类级别的权限检查
- 使用 SpEL（Spring Expression Language）表达式灵活定制权限规则
- 支持在方法和类上同时使用注解，两个条件都满足才放行

### 2. RBAC 权限模型

- 支持角色（Role）和权限（Permission）两个维度的权限控制
- 用户实现 `IRabcLoginUser` 接口提供角色和权限信息
- 提供辅助工具类简化权限判断逻辑

### 3. 灵活的权限表达式

- 基于 SpEL 表达式引擎，支持复杂的权限判断逻辑
- 在表达式中可访问当前登录用户、方法参数等上下文信息
- 支持与或非等逻辑运算

## 使用指南

### 第一步：实现用户接口

实现 `IRabcLoginUser` 接口，返回用户的角色和权限集合：

```java
public class LoginUser implements IRabcLoginUser {
    private Set<String> roles;
    private Set<String> permissions;

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public Set<String> getPermissions() {
        return permissions;
    }
}
```

### 第二步：实现权限上下文提供者

继承 `AbstractRbacCheckPermissionContextProvider` 并实现 `getRbacLoginUser` 方法，负责从当前请求上下文中获取登录用户：

```java

@Component
public class RbacCheckPermissionContextProviderImpl extends AbstractRbacCheckPermissionContextProvider {
    @Override
    public Object getRbacLoginUser(JoinPoint joinPoint) {
        // 从 ThreadLocal、SecurityContextHolder 或其他方式获取当前登录用户
        return getCurrentUser();
    }
}
```

### 第三步：在方法或类上使用注解

在需要权限检查的方法或类上添加 `@CheckPermissions` 注解：

#### 基本用法

```java
// 检查是否拥有指定权限
@CheckPermissions("auth.hasAnyPerms('user:view')")
public void viewUser() {
    // 业务代码
}

// 检查是否拥有指定角色
@CheckPermissions("auth.hasAnyRoles('admin')")
public void adminOperation() {
    // 业务代码
}
```

#### 类级别权限

```java

@RestController
@CheckPermissions("auth.hasAnyPerms('user:manage')")  // 该类所有方法都需要此权限
public class UserController {

    @CheckPermissions("auth.hasAnyPerms('user:create')")  // 额外的权限要求
    public void createUser() {
        // 需要同时满足类级别和方法级别的权限
    }
}
```

#### 复杂表达式

```java
// 多权限判断（与逻辑）
@CheckPermissions("auth.hasAllPerms('user:view', 'user:export')")
public void exportUser() {
}

// 多角色判断（或逻辑）
@CheckPermissions("auth.hasAnyRoles('admin', 'manager')")
public void manage() {
}

// 复杂逻辑
@CheckPermissions("auth.hasAnyRoles('admin') || auth.hasAllPerms('user:view', 'user:edit')")
public void complexCheck() {
}

// 使用方法参数
@CheckPermissions("user != null && user.id == args[0]")  // 检查用户id是否匹配
public void updateProfile(Long userId) {
}
```

### 开发体验

- 安装 IDEA 插件

```shell
SpEL Assistant
或者
SpEL Extension
```

- 将一下内容写入到 `resources/spel-extension.json` 文件中
- 只要在项目的classpath能扫描到就行，不管在哪个子模块里面，被引用进入即可

```json
{
  "com.rwd.common.security.permission.std.annotations.CheckPermissions@value": {
    "method": {
      "parameters": true,
      "parametersPrefix": ["p"]
    },
    "fields": {
      "auth": "i2f.springboot.auth.permission.helper.RbacCheckPermissionHelper",
      "user": "i2f.springboot.auth.permission.IRabcLoginUser",
      "jp": "org.aspectj.lang.JoinPoint",
      "args": "java.lang.Object[]"
    }
  }
}
```

## 模块架构

### 主要组件

| 组件                                           | 说明                          |
|----------------------------------------------|-----------------------------|
| `IRabcLoginUser`                             | 登录用户接口，定义角色和权限获取方法          |
| `CheckPermissions`                           | 权限检查注解，可应用于方法和类             |
| `CheckPermissionAspect`                      | AOP 切面，拦截被注解的方法进行权限检查       |
| `CheckPermissionContextProvider`             | 权限上下文提供者接口，提供 SpEL 表达式执行的变量 |
| `AbstractRbacCheckPermissionContextProvider` | 权限上下文提供者抽象实现                |
| `RbacCheckPermissionHelper`                  | RBAC 权限检查辅助类，提供快捷的权限判断方法    |
| `PermissionDenyException`                    | 权限被拒绝异常                     |
| `CheckPermissionProperties`                  | 权限检查配置属性                    |

### 上下文变量

在 SpEL 表达式中可使用以下变量：

| 变量名    | 类型                          | 说明                                                        |
|--------|-----------------------------|-----------------------------------------------------------|
| `user` | `LoginUser`                 | 当前登录用户对象，根据具体的 CheckPermissionContextProvider 判断实际类型      |
| `auth` | `RbacCheckPermissionHelper` | RBAC 权限检查辅助工具，根据具体的 CheckPermissionContextProvider 判断实际类型 |
| `jp`   | `JoinPoint`                 | AspectJ 连接点对象                                             |
| `args` | `Object[]`                  | 方法参数数组                                                    |

## RbacCheckPermissionHelper 方法

辅助工具类提供以下便捷方法：

```java
// 检查是否拥有所有指定角色
boolean hasAllRoles(String... roles)

// 检查是否拥有任一指定角色
boolean hasAnyRoles(String... roles)

// 检查是否拥有所有指定权限
boolean hasAllPerms(String... perms)

// 检查是否拥有任一指定权限
boolean hasAnyPerms(String... perms)
```

## 配置示例

在 `application.yml` 或 `application.properties` 中配置：

```yaml
i2f:
  springboot:
    auth:
      permission:
        # 权限切面的执行顺序，默认为 -999（较早执行）
        aspect-order: -999
```

## 异常处理

当权限检查失败时，会抛出 `PermissionDenyException` 异常，可通过全局异常处理器捕获处理：

```java

@ExceptionHandler(PermissionDenyException.class)
public ResponseEntity<ErrorResponse> handlePermissionDeny(PermissionDenyException e) {
    ErrorResponse error = new ErrorResponse();
    error.setCode("403");
    error.setMessage("权限不足");
    error.setDetails(e.getContext());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
}
```

## 工作流程

1. 请求到达被 `@CheckPermissions` 注解的方法
2. `CheckPermissionAspect` 拦截该请求
3. 从 `CheckPermissionContextProvider` 获取权限上下文（包括用户、权限等信息）
4. 解析并执行 SpEL 表达式
5. 表达式返回 true 则放行请求，返回 false 则抛出 `PermissionDenyException`

## 切面执行顺序

- 权限切面通过实现 `Ordered` 接口，默认优先级为 `-999`
- 该优先级确保权限检查在事务等其他切面之前执行
- 可通过配置修改切面执行顺序

## 快速开始示例

```java
// 1. 实现用户接口
public class MyLoginUser implements IRabcLoginUser {
    private String id;
    private Set<String> roles;
    private Set<String> permissions;

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public Set<String> getPermissions() {
        return permissions;
    }
}

// 2. 实现上下文提供者
@Component
public class MyPermissionContextProvider extends AbstractRbacCheckPermissionContextProvider {
    @Override
    public Object getRbacLoginUser(JoinPoint joinPoint) {
        // 从 SecurityContextHolder 获取当前用户
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

// 3. 使用注解
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    @CheckPermissions("auth.hasAnyPerms('user:view')")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    @CheckPermissions("auth.hasAnyPerms('user:create')")
    public User createUser(@RequestBody UserDTO dto) {
        return userService.create(dto);
    }

    @DeleteMapping("/{id}")
    @CheckPermissions("auth.hasAnyPerms('user:delete') || auth.hasAnyRoles('admin')")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
```

## 最佳实践

1. **权限粒度**：将权限控制在合理的粒度，如模块级别和操作级别
2. **表达式简化**：对于复杂的权限逻辑，考虑在 Helper 中添加自定义方法
3. **缓存用户权限**：建议缓存用户的角色和权限信息，避免频繁查询
4. **异常处理**：正确处理 `PermissionDenyException`，返回恰当的 HTTP 状态码
5. **日志记录**：在异常处理中记录权限拒绝事件，便于审计和监控

## 常见问题

**Q: 表达式中如何访问方法参数？**
A: 使用 `args[index]` 访问，例如 `args[0]` 表示第一个参数。

**Q: 如何跳过权限检查？**
A: 不在方法或类上添加 `@CheckPermissions` 注解即可。

**Q: 权限检查失败时的默认返回？**
A: 抛出 `PermissionDenyException`，需要通过异常处理器处理。

**Q: 是否支持方法级别和类级别的权限注解同时使用？**
A: 支持，两个条件都必须满足才能放行请求。
