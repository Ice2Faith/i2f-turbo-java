package i2f.design.pattern.creational.singleton.eager;

/**
 * @author Ice2Faith
 * @date 2026/5/21 10:00
 * @desc
 */
public class Test {
    public static void main(String[] args) {
        Earth item = Earth.getInstance();
        System.out.println(item);
    }
}
