# i2f-turbo-java

## 项目介绍

- 提供一些常用的工具封装包
- 简练、独立、单一、易用

## project introduce

- provide some useful utils package
- tiny,unique,simple

## 打包文件

- 个人项目
- 愿意就使用
- 目录：bash/release/*.jar

## package file

- personal project
- to use it, unless you willing accept it
- dir: bash/release/*.jar

## 项目宗旨

- 尽量小的模块划分
- 模块职责明确
- 模块只负责实现自己的功能
- 模块只包含需要的最小依赖
- 尽可能高的模块复用
- 减少尽可能多的重复代码
- 同时，维护依赖的单向流动性，不形成循环依赖
- 基于jdk的部分，打包时包含所需的所有依赖
- 不仅依赖jdk的部分，打包时不包含依赖
- 实现哪里需要哪里搬，不搬多余代码

## project principles

- module as more as possible minimalize
- module has clear responsibility
- module only implements itself feature in duty
- module only require minimalize dependencies
- module as more as possible reused
- reduce repeat code wether possible
- at the time, keep dependency module single direction flow, avoid circle dependencies
- module package include all dependencies when only rely on jdk
- otherwise only package itself, exclude all dependencies
- the gole is that copy it to anywhere you need, and only include you need

## 必要说明
- 本项目属于个人项目
- 主要来自于个人的学习、思考、或者经验总结与提炼
- 同时也作为个人的开发知识库
- 所以维护代码可能随时变更
- 类名或者包名可能都会调整
- 同时，存在认知局限性
- 也就会导致一些可拓展性的缺乏
- 另外，部分功能存在未测试的情况
- 例如，不具备环境测试或其他原因
- 最后，BUG的问题，一定是会有的
- 如果使用，请尽量使用源码
- 方便进行修改拓展和变更

## essential declaration

- this project is a personal project
- the content from learn, consideration, or extract or sum up for experience
- and, also as mine knowledge database for help me develop
- so that, this project maybe refactor any time or change
- maybe package name, or class name, and so on
- and, cannot avoid cognization limitation
- beacause of it, maybe missing some extendable point
- the other hand, exists some not tested code
- such as not has suitable test environment, or other reason
- at the end, about bugs, it would be exists and will forevery and cannot avoid
- recomend use source code instead of release package
- because of you can modify, change, adjust it to resolve your problem or bugs.

## 项目规划

- i2f-jdk
  - 包含仅依赖于jdk的功能
  - 最小jdk为开发jdk8
  - 后续jdk版本未进行测试
  - 可能部分功能由于jdk版本升级的变更而不可用
- i2f-jdk-all
  - 是一个聚合包
  - 包含了i2f-jdk下的所有模块的整合包
- i2f-extension
  - 用于对于第三方jar包的功能进行二次封装
  - 因此，这部分依赖第三方功能
  - 打包时，仅打包本包内容
  - 同时，依赖i2f-jdk模块的内容
  - 因此，使用时，需要本包引入，第三方包引入，以及jdk的依赖引入
- i2f-spring
  - 用于针对spring的部分进行封装
  - 这部分，仅针对spring的基础部分，也就是springframework部分
  - 不包含springboot自动装配的部分
  - 包含但不限于bean/tx/context/web/...
  - 这部分，仅依赖spring以及jdk依赖
  - 某种意义上来说，也算是extension的一部分
  - 只不过单独拧出来
- i2f-springboot
  - 用于针对springboot的自动装配
  - 实现一些自动装配的功能，也就是starter功能
  - 基于jdk,spring,extension
- 其他
  - 可能会提供一些以all结尾的模块
  - 这部分就是一些整合同一类特征的包
  - 功能是打包其他模块
  - 模块本身无实现逻辑

## project plan

- i2f-jdk
  - only rely on jdk to develop
  - minimal jdk version support jdk8
  - the after version not tested
  - some feature maybe un-useful because of higher jdk version
- i2f-jdk-all
  - it an merge package
  - included all module of in i2f-jdk
- i2f-extension
  - the features base on the thrid java package
  - so, it rely on the thrid package
  - only include itself code when package module
  - and, maybe rely some module from i2f-jdk
  - so that, add itself, thrid package and jdk module package when you use it
- i2f-spring
  - make features for spring package only
  - it only for spring basic features, also is springframework
  - excludes springboot partial
  - include bean/tx/context/web...
  - and, only rely on jdk modules
  - in some means, it is a patrial of extension
  - but, use single module built.
- i2f-springboot
  - make features fir springboot to auto-confuguration
  - implements some features to auto-configuration, also is some springboot starter
  - base on jdk, spring, extension
- the others
  - maybe provide some module of the name end with all
  - those module only merge some has similiar feature modules to package
  - it only package other modules
  - itself not implements any features.
