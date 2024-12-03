package i2f.translate.zh2pingyin.test;


import i2f.translate.ITranslator;
import i2f.translate.zh2pingyin.impl.Zh2PinyinTranslator;
import i2f.translate.zh2pingyin.impl.Zh2SimTranslator;
import i2f.translate.zh2pingyin.impl.Zh2TraTranslator;

/**
 * @author Ice2Faith
 * @date 2024/11/30 18:26
 */
public class TestTranslate {
    public static void main(String[] args) throws Exception {

        testTranslate();

    }

    public static void testTranslate() {
        try (ITranslator translator = new Zh2PinyinTranslator();
             ITranslator asciiTranslator = new Zh2PinyinTranslator(false)) {
            System.out.println("====================");
            String str = "你好，这是一个拼音翻译测试！哈哈哈，哈气，蛤蟆，好呀，号令，蜗牛，红色，偶尔，后天，喝了，盒子，耳朵，褐色，一个，移走，蚂蚁，毅力，屋子，无敌，五个，悟性，淤泥，愚钝，下雨，遇到";
            System.out.println(str);
            String ret = translator.translate(str);
            System.out.println(ret);
            ret = asciiTranslator.translate(str);
            System.out.println(ret);
            System.out.println("====================");
            String pyln = "";
            String stln = "";
            for (int i = 0; i < str.length(); i++) {
                String pstr = str.substring(i, i + 1);
                String ppy = translator.translate(pstr);
                pyln += String.format("%7s", ppy);
                stln += String.format("%6s", pstr);
            }
            System.out.println(pyln);
            System.out.println(stln);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (ITranslator translator = new Zh2TraTranslator();
             ITranslator pinyinTranslator = new Zh2PinyinTranslator()) {
            System.out.println("====================");
            String str = "你好，这是一个拼音翻译测试！哈哈哈";
            System.out.println(str);
            String ret = translator.translate(str);
            System.out.println(ret);
            System.out.println("====================");
            String pyln = "";
            String stln = "";
            for (int i = 0; i < str.length(); i++) {
                String pstr = str.substring(i, i + 1);
                String ppy = pinyinTranslator.translate(pstr);
                pyln += String.format("%7s", ppy);
                stln += String.format("%6s", pstr);
            }
            System.out.println(pyln);
            System.out.println(stln);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (ITranslator translator = new Zh2SimTranslator();
             ITranslator pinyinTranslator = new Zh2PinyinTranslator()) {
            System.out.println("====================");
            String str = "妳好，這是一個拼音飜譯測試！哈哈哈";
            System.out.println(str);
            String ret = translator.translate(str);
            System.out.println(ret);
            System.out.println("====================");
            String pyln = "";
            String stln = "";
            for (int i = 0; i < str.length(); i++) {
                String pstr = str.substring(i, i + 1);
                String ppy = pinyinTranslator.translate(pstr);
                pyln += String.format("%7s", ppy);
                stln += String.format("%6s", pstr);
            }
            System.out.println(pyln);
            System.out.println(stln);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
