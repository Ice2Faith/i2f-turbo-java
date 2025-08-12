# consul 注册中心/配置中心
- 如果用过nacos的话，那么consul做的事情和nacos就差不多
- 都是作为注册中心和配置中心使用

## 安装
- consul 是以二进制安装为主的，开发语言是golang
- 因此是二进制可执行程序的方式运行
- 而不是jar包的形式运行

- 下载页面
```shell
https://developer.hashicorp.com/consul/install?product_intent=consul
```
- 根据自己的系统选择对应的下载即可
- 我这里以windows为例
```shell
https://releases.hashicorp.com/consul/1.21.3/consul_1.21.3_windows_amd64.zip
```
- 解压
```shell
unzip consul_1.21.3_windows_amd64.zip
```
- 得到一个可执行程序
```shell
consul.exe
```
- 下面直接以开发模式启动
```shell
consul.exe agent -dev
```
- 浏览器访问页面
```shell
http://localhost:8500/
```
- 到这里就算是安装了使用了
- 但是，一般来说，开发模式肯定不行
- 所以，改一下启动命令启动即可
```shell
consul agent -server -ui -bootstrap-expect 1 -data-dir 自己本地保存的数据地址 -node=n1 -bind=127.0.0.1

consul agent -server -ui -data-dir ./consul-data -bootstrap-expect 1 -node=n1 -bind=127.0.0.1
```