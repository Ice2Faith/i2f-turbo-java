# c/c++ 进行 JNI 开发说明

## 基础知识

- java 方法与 c/c++ 方法对照关系
- 直接给出例子进行说明
- java 代码

```java
// 注意这里的包名
package com.test.jni;

import i2f.natives.core.NativeUtil;

import java.io.File;

// 注意这里的类名
// 全限定类名=包名.类名
// 则，这个类的全限定类名为：com.test.jni.JniApi
public class JniApi {
    static {
        // 一般，都在静态代码块中，进行加载依赖库文件
        // 在windows中是.dll文件，在linux中是.so文件
        // 使用 System.loadLibrary 进行加载库文件时
        // 不需要指定后缀，内部会自动判断
        // 将会在lib路径中查找这个库文件
        // 举例来说，如果是在windows环境中
        // 则查找${java.lib.path}/JniApi.dll
        // 显然，这个方法的优点是简单
        // 缺点是需要在配置的库路径中，需要在环境变量配置，或者在启动变量配置
        System.loadLibrary("JniApi");
        // 因此，也可以使用另一种方式
        // 下面这种方式，就可以实现任意路径加载
        // 但是缺点是，需要绝对路径，需要带上后缀
        System.load(new File("./lib/JniApi.dll").getAbsolutePath());
        // 那么，怎么知道当前环境应该需要什么后缀的库
        // 这就借助 mapLibraryName 进行拼接后缀
        // 这个方法实际就是直接在字符串后面添加对应环境的库文件后缀
        System.load(new File(System.mapLibraryName("./lib/JniApi")).getAbsolutePath());
        // 这样，就实现了自由加载库文件了
        // 但是，需求是目标主机可能没有这个库文件，手动拷贝的方式运维的方式
        // 是很不合适的
        // 所以，就可以打包到jar里面，运行时把这个库文件释放到一个自定义路径即可
        // 通过Resource进行释放
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(System.mapLibraryName("lib/JniApi"));
        // 上述的过程，是比较麻烦的
        // 因此，也提供了工具方法
        // 这个方法就实现了上述的过程
        NativeUtil.loadClasspathLib("lib/JniApi");
    }

    // 定义一个本地方法
    // 和普通方法定义一样，只不过加上 native 关键字，并且不需要函数体
    public static native String hello(String name, int value);

    public static native int[] convert(byte[] arr);
} 
```

- 下面来看对应的cpp代码

```c
// 在c++中，避免多次连链接
#pragma once
// 添加jni头文件
// 这个头文件，在安装JDK之后的路径如下
// ${JAVA_HOME}\include
// ${JAVA_HOME}\include\win32
#include <jni.h>
// 添加系统头文件
// 这里以windows为例
#include<Windows.h>

// 定义一个宏，后面会说这个宏的作用
#define JNI_METHOD(name) Java_com_test_jni_JniApi_##name

// 函数说明
// 第一行，固定格式，指定返回值即可：extern "C" JNIEXPORT [返回值类型] JNICALL
extern "C" JNIEXPORT jstring JNICALL
// 第二行，函数名，这里有命名规则要求
// 函数名=Java_+[全限定类名转下划线格式]+_+[函数名]
// 同时，native函数不支持重载，也就是说，函数名不能相同
// 那么，全限定类名=com.test.jni.JniApi
// 全限定类名转下划线格式=con_test_jni_JniApi
// 函数名=hello
// 所以，完整名称=Java_com_test_jni_JniApi_hello
// 一般来说，一个库文件，对应一个java类
// 所以，每次都写这么长的名称，就有点麻烦
// Java_com_test_jni_JniApi_
// 另外，如果java类的包名变了，改这里也比较麻烦
// 所以，可以使用上面的宏 JNI_METHOD 来做这件事
// 这个宏，后面一个函数讲
Java_com_test_jni_JniApi_hello(
JNIEnv* env, // 固定参数，提供了JNI环境，能够与java代码进行交互
jobject obj, // 固定参数，指明了是java中的哪个对象调用的这个方法，可以理解为 JniApi 产生的对象，但是这里是静态方法，所以，这个值没有意义
jstring name, // 从第三个参数开始，就对应了java方法中定义的参数了，参数类型基本上都是 j+[java数据类型]，例如：int -> jint, String -> jstring
jint value
){
    // 在c/c++中使用string，需要转换为char*进行使用，使用方式固定
    // 并且，一定要调用release方法进行释放
	const char * cstr = env->GetStringUTFChars(str, NULL); 
	// 实现一个简单的字符串格式化功能
	char buff[2048]={0];
	sprintf(buff,"hello, %s:%d",cstr,value);
	// 释放指针，固定格式
	env->ReleaseStringUTFChars(str, cstr);
	// 构造jstring进行返回，固定格式
	jstring ret = env->NewStringUTF(buff);
	return ret;
}

// 第一行，和上面一样，只不过返回值类型变为 jintArray
// 注意，j{类型}Array=={类型}[]
// 举例：jintArray==int[],jbyteArray==byte[]
extern "C" JNIEXPORT jintArray JNICALL
// 上面说了，名称太长，不方便修改包名
// 所以使用JNI_METHOD宏
// 这个宏只带了一个name参数
// 内部实现就是将name参数拼接在固定的前缀后面
// 这就是宏里面##的作用，就是直接拼接前后两部分
// 所以，JNI_METHOD(convert)==Java_com_test_jni_JniApi_convert
JNI_METHOD(convert)(
JNIEnv* env, // 两个固定参数，一样的
jobject obj,
jbyteArray arr // 传入了一个byte[]数组
){
    // 获取数组指针，固定格式，注意对应类型即可
	jbyte* elems=env->GetByteArrayElements(arr, NULL);
	// 获取数组长度，固定格式
	int len=env->GetArrayLength(arr);

    // 申请一块一样长度的内存控件
	jint* iarr = (jint*)malloc(sizeof(jint)*len);
	// 0字节填充数组
	memset((void *)iarr, 0, sizeof(jint)*len);

    // 拷贝转换类型
	for (int i = 0; i < len; i++){
		iarr[i] = elems[i];
	}
	
	// 释放转换的数组，在使用完即可释放
	env->ReleaseByteArrayElements(arr, elems, 0);

    // 生成一个int[]数组
	jintArray ret = env->NewIntArray(len);
	// 使用iarr填充ret数组元素
	env->SetIntArrayRegion(ret, 0, len, iarr);

    // 释放申请的内存，在填充完即可释放
    free(iarr);

	return ret;
}
```

## 在 Visual Studio 中进行 JNI 开发

- 经过上面的基础
- 其实你已经学会了 JNI 开发
- 剩下的就是一个合适的工具
- 以及怎么使用工具的问题了
- 所以，这里就给出如何在 VS 中开发
- 开始创建工程
-
    1. 新建工程
-
    2. 选择，C++类型
-
    3. 选择，Win32
-
    4. 选择，Win32控制台应用程序
-
    5. 更改项目名称，不改也可以，这个名称将作为自动生成DLL的文件名
- 设置工程向导
-
    1. 点击，下一步
-
    2. 勾选，应用程序类型为：DLL
-
    3. 勾选，附件选项为：空项目
-
    4. 取消勾选，附件选项的：安全开发周期（SDL）检查
-
    5. 点击完成
- 新建源文件
-
    1. 找到，解决方案资源管理器
-
    2. 右键，源文件
-
    3. 点击，添加
-
    4. 点击，新建项
-
    5. 选择，C++文件
-
    6. 修改名称，名称不改也可以，无所谓
-
    7. 点击，确定
- 编辑源代码
-
    1. 双击，刚才添加的源文件
-
    2. 添加基础内容

    - 注意，这里的源代码，和上面介绍的不同
    - 自己根据内容，编写对应的java类进行调用

```c
#pragma once
// Windows 头文件
#include <windows.h>
// JNI 头文件
#include <jni.h>
// 一些基础的头文件
#include <string>
#include<string.h>
#include<stdio.h>
#include<stdlib.h>

// 定义方法宏
#define JNI_METHOD(name) Java_com_test_HelloJni_##name

extern "C" JNIEXPORT jstring JNICALL
JNI_METHOD(hello)(
JNIEnv* env,
jobject /* this */) {

	std::string hello = "Hello, Windows Api for JNI!";

	return env->NewStringUTF(hello.c_str());
}
```

- 更改项目配置
-
    1. 找到，菜单
-
    2. 点击，项目
-
    3. 点击，属性，最后那个就是，一般叫：项目名+属性
-
    4. 右上角，点击，配置管理器
-
    5. 更改，活动解决方案配置，为，Release
-
    6. 点击，下方列表，下拉选择平台
-
    7. 点击，新建
-
    8. 下拉，新建平台
-
    9. 更改，为，X64，这里只需要更改为和JDK一致的即可，JDK是x86就是x86,是x64就是x64
-
    10. 点击，确定
-
    11. 点击，关闭
-
    12. 注意，现在，你还在项目属性页
-
    13. 左侧树，进入，配置属性-常规
-
    14. 确认，配置类型为，动态库(.dll)
-
    15. 左侧树，进入，配置属性-VC++目录
-
    16. 更改，包含目录，下拉，编辑
-
    17. 添加下面两条记录

```shell
$(JAVA_HOME)\include
$(JAVA_HOME)\include\win32
```

-
    18. 点击，确定
-
    19. 点击，应用
-
    20. 点击，确定
-
    21. 这时候，头文件 jni.h 就找到了
- 编译生成
-
    1. 点击，菜单，生成
-
    2. 点击，重新生成解决方案
-
    3. 这时候，DLL文件就生成好了
-
    4. 在文件夹中查看即可

    - 目录结果大概如下

```shell
- [项目名]
  - [项目名].sln // 解决方案配置文件
  - [项目名].sdf // VC 的索引库，可以删除
  - [项目名]
    - [项目名].vcxproj // 项目配置文件
    - [项目名].vcxproj.filters // 项目资源配置文件
    - [源文件名].cpp // 源代码
  - x64
    - Release
      - [项目名].dll // 生成的DLL文件
      - [项目名].lib // 连接文件
```
