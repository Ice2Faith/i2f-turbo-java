package i2f.mixin.test;


import i2f.mixin.MixinProxyFactory;
import i2f.mixin.all.AllMixins;

/**
 * @author Ice2Faith
 * @date 2026/5/14 10:34
 * @desc
 */
public class TestMixins {
    public static void main(String[] args) {
        AllMixins mixins = MixinProxyFactory.getMixinInstance(AllMixins.class);
        String str = mixins.regex_find_join("1,2,3,4,5,6", "\\d+", "-");
        System.out.println("str = " + str);


    }
}
