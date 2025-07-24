# launcher 启动器

## 作用

- 为了在java程序启动时加载额外的classpath或者动态的外部jar包
- 实现插件模式的应用程序
- 实现上，参考了 Springboot 的 Launcher
- 但是在其基础上进行了一定的简化
- 适用于，加载 JDBC Driver 此类的 SPI 组件
- 这类支持动态加载的，能够进行插件化的场景中

## 启动流程

- 命令行 java -jar app.jar
- 执行 ExtApplicationLauncher.main 加载器方法
- 初始化 ExtClasspathClassLoader 加载额外的 classpath
- 将 ExtClasspathClassLoader 设置为线程的上下文 classloader
- 反射获取真正的 App.main 方法
- 执行 实际的入口 App.main 方法
- 进入 App 执行逻辑

## 启动器的要解决的问题

- 主要是java程序，通常打包为一个jar包
- 在这个jar包中的 MATE-INF/MANIFEST.MF 文件中固定写死了所有的classpath
- 换句话说，不能够在运行的时候直接添加额外的classpath
- 如果要实现添加额外的classpath，只能通过使用 java -cp 参数
- 指定所有的classpath，也就是原来的classpath也要重写写一遍
- 这种使用 -cp 的，一般都是使用 shell 脚本来实现
- 但是比较麻烦
- 另外一点就是，java对classpath的定义上认为
- 一个目录是一个classpath，一个jar文件是一个classpath
- 因此就导致，如果一个目录已经是一个classpath
- 但是，这个目录中的jar则只会认为是普通资源
- 不会认为是一个classpath
- 所以，在写classpath的时候，就得把目录，以及目录中的所有jar都写为classpath
- 这是非常麻烦的
- 因此，就有了 launcher 启动
- 启动器先于程序的入口运行，设置一个额外的上下文classloader
- 实现加载额外的classpath下的资源
- 然后，再执行实际的入口main类
- 这样，实际的main之后的执行就不需要进行变更
- 并且也能够自动的加载到额外的classpath下的资源

## maven 插件方式

- 对如下的插件进行配置
- 按照插件中的注释进行配置即可

```xml

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.0.2</version>
    <configuration>
        <archive>

            <manifest>
                <!-- 添加classpath,指定classpath前缀，指定启动类 -->
                <addClasspath>true</addClasspath>
                <!-- 指定启动类 -->
                <mainClass>i2f.launcher.ExtApplicationLauncher</mainClass>
                <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
                <addDefaultSpecificationEntries>true</addDefaultSpecificationEntries>
            </manifest>
            <manifestEntries>
                <!-- 填写真正的入口main方法的类，springboot的情况就是springboot的入口类 -->
                <Ext-Main-Class>com.i2f.WebApplication</Ext-Main-Class>
                <!-- 
                添加额外的classpath路径，多个用逗号分隔，将会添加这些目录到classpath
                如果目录中存在jar文件，那么也会添加这些jar文件到classpath
                例如：plugins,lib,ext-libs
                这样的话，首先这三个目录都会成为classpath
                其次，这三个目录下的所有jar都会添加到classpath
                 -->
                <Ext-Path>plugins</Ext-Path>
            </manifestEntries>
        </archive>

    </configuration>
</plugin>
```