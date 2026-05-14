# mixin 混入

- 旨在通过接口类的默认方法的方式，提供一些列具有默认方法的接口
- 使得其他类可以通过直接引入这一些列的接口，从而获得函数式编程的环境
- 使用接口类的目的也是为了变相多重继承
- 这些接口类要求都是默认方法实现，混入之后没副作用

## 混入接口实例的获取

### 【不推荐】匿名对象获取

- 传统方法是直接通过创建匿名实例对象方式获取实例
- 这种方式，会创建大量的匿名对象，不利于管理和使用

```java
import i2f.mixin.all.AllMixins;

public class Test {
    public void test() {
        AllMixins mixins = new AllMixins() {
        };
    }
}
```

### 【推荐】通过代理工厂获取

- 提供了一个代理工厂，使用代理的方式直接使用
- 而不用创建匿名对象
- 因为混入的目的就是为了混入一些列的函数
- 这些函数实际上没有必要依附于对象才能执行
- 因此直接进行接口代理，执行默认方法即可

```java
import i2f.mixin.MixinProxyFactory;
import i2f.mixin.all.AllMixins;

public class Test {
    public void test() {
        AllMixins mixins = MixinProxyFactory.getMixinInstance(AllMixins.class);
    }
}
```


