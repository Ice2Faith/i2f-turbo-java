package i2f.container.builder.test;

import i2f.container.builder.Builders;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Ice2Faith
 * @date 2024/4/24 15:07
 * @desc
 */
public class TestBuilder {
    public static void main(String[] args) {
        HashMap<String, Object> hashMap = Builders.newMap(HashMap::new, String.class, Object.class)
                .put("1", 1)
                .put("2", "2")
                .putKeys(true, String::valueOf, 3, 4, 5, 6, 7, 8, 9)
                .get();
        System.out.println(hashMap);

        ArrayList<String> arrayList = Builders.newList(ArrayList::new, String.class)
                .add("1")
                .adds(String::valueOf, 2, 3, 4, 5, 6, 7, 8)
                .get();
        System.out.println(arrayList);

        TestBean bean = Builders.newObj(TestBean::new)
                .set(TestBean::setAge, 22)
                .set(TestBean::setName, "zhang")
                .then(TestBean::incAge)
                .get();
        System.out.println(bean);
    }
}
