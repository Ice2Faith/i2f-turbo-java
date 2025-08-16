# sm-crypto 项目说明
- 项目是从 npm 前端依赖 sm-crypto 中根据 js 源码进行的 java 代码转换
- 原始项目
```shell
npm install sm-crypto
```
- 项目的 npm 介绍
```shell
https://www.npmjs.com/package/sm-crypto
```
- 仓库地址
```shell
https://github.com/JuneAndGreen/sm-crypto
```
- 原本也有一个 java 端的依赖库
```xml
 <dependency>
    <groupId>com.antherd</groupId>
    <artifactId>sm-crypto</artifactId>
    <version>0.3.2.1-RELEASE</version>
</dependency>
```
- 仓库地址
```shell
https://github.com/antherd/sm-crypto
```
- 但是这个库是直接在java中通过js引擎nashorn调用js代码的方式
- 这种方式，效率比较慢
- 因此，就有了本项目
- 本项目根据原来的js代码，使用java语法进行重写转换
- 并且进行了一部分调整以优化性能
- 从对比性能测试上来看
- 对比结果如下
- 这里两种方式都是在java中调用
- 不再这里对比和在浏览器中调用的情况
- 因为浏览器是对于单个用户而言的，性能问题可以直接忽略

| 实现方式   | sm3摘要       | sm4加解密      | sm2生成密钥对      | sm2签名验签         | sm2加解密          |
|-----------|--------------|---------------|------------------|--------------------|-------------------|
| java代码   | 25ms, 0ms    | 45ms, 0ms     | 3235ms, 3ms      | 10104ms, 10ms      | 9103ms, 9ms       |
| js引擎调用  | 1709ms, 1ms | 1181ms, 1ms    | 99536ms, 99ms   | 246843ms, 246ms    | 236513ms, 236ms   |
| 加速比     | 68.36        | 26.24         | 30.76            | 24.43              | 25.98              |

- 表格中，每一项的含义如下
- 表格中，数据均是进行 1000 此操作得到的时间
- 分别是，1000 此的总耗时，平均耗时
- 加速比为 java代码 对比 js引擎调用 获得的加速倍速
