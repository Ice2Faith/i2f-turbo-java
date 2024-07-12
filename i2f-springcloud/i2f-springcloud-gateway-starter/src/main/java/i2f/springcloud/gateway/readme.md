# gateway 网关
---

- 由于Netflix闭源了，原来的zuul网关就被现在的gateway替换了
- gateway是spring提供的
- 功能相比较于zuul1.0更加强大，性能也更好，采用非阻塞的webflux进行实现
- 即使zuul2.0也采用了非阻塞，性能差异不大
- 但是netflix的开源情况不稳定

---

## 配置示例
```yml
spring:
  cloud:
    discovery:
      client:
        simple:
          instances:
            # 使用内置的简易客户端配置几个客户端实例
            # 这里就定义了两个应用
            # 和使用其他服务发现中的应用名作用一样
            # 比如 eureka 或者 nacos 等服务发现
            manage-app:
              # 这个应用有一个实例
              - uri: http://localhost:8081/
            report-app:
              # 这个应用则有两个实例
              - uri: http://localhost:8082/
              - uri: http://localhost:8083/
    gateway:
      # 路由配置列表
      routes:
        # 转发系统路由，路径前缀示例
        # 原始URL：http://gateway.com/system/user/info
        # 转发URL:http://system.com/user/info
        # id 在配置中唯一即可，无特殊要求
        - id: system
          # 转发到的 baseURL
          uri: http://system.com
          predicates:
            # 满足以 /system/ 开头的所有请求都转发
            - Path=/system/**
          filters:
            # 这里去除一层路径，也就是把 /system 这一层去掉
            - StripPrefix=1
        # 下面是拓展示例
        # 原始URL: http://localhost/upload/icon/2024/demo.png
        # 转发URL：http://192.168.1.100:8861/file/2024/demo.png
        - id: file
          # 转发到的 baseURL
          uri: http://192.168.1.100:8861/file
          predicates:
            - Path=/upload/icon/**
          filters:
            - StripPrefix=2
        # 结合使用服务发现能力进行负载均衡
        - id: report-app
          # 这里就是使用服务发现的服务名，上面那样定义也是可以的
          uri: http://report-app/
          predicates:
            - Path=/api/report/**
          filters:
            - StripPrefix=2
        # 添加请求头/响应头
        - id: route1
          uri: http://example.org
          predicates:
            - Path=/api/service1/**  # 路径匹配规则
          filters:
            - AddRequestHeader=X-Request-Foo, Bar  # 添加请求头
            - AddResponseHeader=X-Response-Red, Blue # 添加响应头

        # 路径重写
        - id: route2
          uri: http://example.org
          predicates:
            - Path=/api/service2/**
            - Method=GET,POST  # 只允许 GET 和 POST 方法
          filters:
            - RewritePath=/api/service2/(?<segment>.*), /$\{segment}  # URL 重写
        
        # 第三条路由规则
        - id: route3
          uri: https://example.org
          predicates:
            - Path=/red/{segment},/blue/{segment}

      # 全局过滤器配置
      default-filters:
        - DedupeResponseHeader=Access-Control-Allow-Credentials Access-Control-Allow-Origin  # 去重HTTP响应头

      # 用于控制 HTTP 请求的负载均衡器配置
      loadbalancer:
        use404: true  # 当无可用服务时返回404

      # HTTP请求重试配置
      retry:
        enabled: true
        retries: 3  # 重试次数
        statuses: BAD_GATEWAY,GATEWAY_TIMEOUT  # 触发重试的HTTP状态码
        methods: GET,POST  # 允许重试的HTTP方法
        backoff:
          firstBackoff: 50ms  # 首次重试的延迟
          maxBackoff: 500ms  # 最大重试延迟
          factor: 2  # 延迟因子
          basedOnPreviousValue: false  # 延迟是否基于上一次的延迟时间

      # 跨域配置
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: "*"  # 允许所有域
            allowedMethods: "*"  # 允许所有方法
            allowedHeaders: "*"  # 允许所有头
            allowCredentials: true  # 允许证书
```

## 断言 predicates

- 用来匹配路由的，断言为真时，表示匹配上路由
- 断言，对应的就是一个断言工厂PredicateFactory

### 内置的断言

- 更多看官网

```
https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gateway-request-predicates-factories
```

- 基于时间Datetime
    - AfterRoutePredicateFactory 请求时间晚于指定时间
    - BeforeRoutePredicateFactory 请求时间早于指定时间
    - BetweenRoutePredicateFactory 请求时间介于指定时间
    - 总结，没什么用

```yaml
- After=2022-06-12T15:30:30.000+08:00[Asia/Shanghai]
```

- 基于远程地址RemoteAddr
    - RemoteAddrRoutePredicateFactory 接受一个IP地址段，判断是请求是否在地址段中
- 子网掩码可以不指定，不指定就等价于根据IP

```yaml
- RemoteAddr=192.168.1.1/24
```

- 基于Cookie
    - CookieRoutePredicateFactory 接受两个参数，分别为cookie名字和值得正则表达式，判断请求是否符合

```yaml
- Cookie=location,(Zh-)?cn
```

- 基于Header
    - HeaderRoutePredicateFactory 接受两个参数，分别为header名称和值得正则表达式

```yaml
- Header=token,[a-zA-Z0-9]{16}
```

- 基于Host
    - HostRoutePredicateFactory 接受一个ant-match的模式串，匹配主机名
- 多个参数用逗号分隔

```yaml
- Host=**.spring.io,**.apache.org
```

- 基于Method
    - MethodRoutePredicateFactory 接受一个参数，限定是否指定的请求类型
- 多个参数用逗号分隔

```yaml
- Method=GET,POST
```

- 基于Path
    - PathRoutePredicateFactory 接受一个参数，判断path是否满足路径规则
- 多个参数用逗号分隔
- 支持ant-match通配符或者{}占位符

```yaml
- Path=/user/**,/mobile/{segment}
```

- 基于Query
    - QueryRoutePredicateFactory 接受一个参数，判断请求参数中是否包含
- 值也可以使用逗号进行正则匹配

```yaml
- Query=cardId
- Query=cardId,\d+
```

- 基于Weight
    - WeightRoutePredicateFactory 接受两个参数，分别是分组名称和权重
- 其实就是一种负载均衡策略
- 基于同一个分组名称的路由规则，根据指定的权重进行路由
- 下面就是针对svc这一个分组的两个不同权重的配置示例

```yaml
- id: master-svc
  uri: http://127.0.0.1:8081
  predicates:
    - Weight=svc,8
- id: salave-svc
  uri: http://127.0.0.1:8082
  predicates:
    - Weight=svc,2
```

### 自定义断言

- 继承AbstractRoutePredicateFactory
- 注意，类名必须以RoutePredicateFactory结尾
    - 原因是，在配置中解析中，会以断言的名称+RoutePredicateFactory作为类名，查找环境中的bean
- 实现类的内部，必须包含一个静态内部类Config，用于接受断言参数
- 实现示例参考：RequestAttrRoutePredicateFactory.class
- 这里面，实现了一个尝试从请求头，请求参数中获取满足指定正则表达式的值得断言
- 下面这条规则就指定了，在请求头或者请求参数中，必须要包含一个叫做token的参数
- 并且要满足这个正则表达式

```yaml
- RequestAttr=token,[a-zA-Z0-9]{16,}
```

--- 

## 过滤器 filters

- 在过滤器中，可以实现一些相关的功能，比如请求日志，鉴权等

### 内置的过滤器

- 内置的过滤器相当丰富
- 这里就不展开说了，只说其中一些
- 完整看官网

```
https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gatewayfilter-factories
```

- AddRequestHeader 添加请求头

```yaml
- AddRequestHeader=X-Request-Type, gateway
```

- 结合占位符使用

```yaml
predicates:
- Path=/user/{segment}
filters:
- AddRequestHeader=X-Request-Client, client-{segment}
```

- AddRequestParameter 添加请求参数

```yaml
- AddRequestParameter=form, gateway
```

- AddResponseHeader 添加响应头

```yaml
- AddResponseHeader=X-Response-Cache, support
```

- DedupeResponseHeader 去除重复的响应头

```yaml
- DedupeResponseHeader=Access-Control-Allow-Credentials Access-Control-Allow-Origin
```

- PrefixPath 添加转发前缀

```yaml
- PrefixPath=/api
```

- RedirectTo 请求重定向

```yaml
- RedirectTo=302, https://spring.io
```

- RemoveRequestHeader 移除请求头

```yaml
- RemoveRequestHeader=clientType
```

- RemoveResponseHeader 移除响应头

```yaml
- RemoveResponseHeader=Expires
```

- RemoveRequestParameter 移除请求参数

```yaml
- RemoveRequestParameter=userId
```

- RequestHeaderSize 限制请求头大小

```yaml
- RequestHeaderSize=1000B
```

- RewritePath 重写请求路径

```yaml
predicates:
- Path=/user/**
filters:
- RewritePath=/user/?(?<segment>.*), /$\{segment}
```

- SetPath 设置新路径

```yaml
predicates:
- Path=/user/{segment}
filters:
- SetPath=/{segment}
```

- SetRequestHeader 设置请求头

```yaml
- SetRequestHeader=form, gateway
```

- SetResponseHeader 设置响应头

```yaml
- SetResponseHeader=form, gateway
```

- SetStatus 设置响应状态

```yaml
- SetStatus=UNAUTHORIZED

- SetStatus=401
```

- 同时可以指定原始的响应状态

```yaml
spring:
  cloud:
    gateway:
      set-status:
        original-status-header-name: original-http-status
```

- StripPrefix 移除路径前缀个数

```yaml
- StripPrefix=2
```

- Retry 失败重试

```yaml
- name: Retry
  args:
    retries: 3
    statuses: BAD_GATEWAY
    methods: GET,POST
    backoff:
      firstBackoff: 10ms
      maxBackoff: 50ms
      factor: 2
      basedOnPreviousValue: false
```

- RequestSize 请求大小限定

```yaml
- name: RequestSize
  args:
    maxSize: 5000000
```

- 更多过滤器，查看官网

### 默认过滤器配置

```yaml
spring:
  cloud:
    gateway:
      default-filters:
      - PrefixPath=/api
```

### 全局过滤器配置

- 实现GlobalFilter接口
- 并添加到环境中
- 实现参考：RequestLogGlobalFilter.class

- 官方的全局过滤器
- LoadBalancerClientFilter 负载均衡获取真实URI 也就是lb://处理
- NettyRoutingFilter netty的转发
- NettyWriteResponseFilter netty的响应回写
- WebsocketRoutingFilter websocket的转发
- ForwardPathFilter 路径解析转发
- RouteToRequestUrlFilter 转换路由规则中的uri
- WebClientHttpRoutingFilter webclient的转发
- WebClientWriteResponseFilter webclient的响应回写

### 自定义过滤器

- 继承AbstractGatewayFilterFactory类，或者直接继承AbstractNameValueGatewayFilterFactory
- 实现参考：RequestAttrGatewayFilterFactory.class
- 这里实现了一个，从header/query中获取指定参数，满足指定配置的正则的值
- 如果不满足，直接返回402 PAYMENT_REQUIRED

### 访问日志

- 开启默认的日志，需要使用-D启动参数方式

```bash
-Dreactor.netty.http.server.accessLogEnabled=true
```

### 配置跨域

- 使用配置方式配置跨域

```yaml
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods:
            - GET
            - POST
            - PUT
            - DELETE
            - OPTION
```

- 使用代码方式配置跨域
- 参考配置类：GatewayCorsConfig.class
