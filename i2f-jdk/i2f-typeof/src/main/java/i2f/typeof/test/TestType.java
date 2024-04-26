package i2f.typeof.test;

import i2f.typeof.token.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/4/26 13:56
 * @desc
 */
public class TestType {
    private List<Map.Entry<String, Object>> list;
    private Map<String, Integer> map;

    public static void main(String[] args) {
        System.out.println(new TypeToken<Integer>() {
        }.fullType().simpleName());
        System.out.println(new TypeToken<Map<Integer, Boolean>>() {
        }.fullType().importName());
        System.out.println(new TypeToken<Map<Map.Entry<Integer, String>, Boolean>>() {
        }.fullType().fullName());

        System.out.println(TypeToken.fullFieldType(TestType.class, "list").simpleName());
        System.out.println(TypeToken.fullFieldType(TestType.class, "map").simpleName());

        System.out.println(TypeToken.getListType(TestType.class, "list"));
        System.out.println(TypeToken.getMapKeyType(TestType.class, "map"));
        System.out.println(TypeToken.getMapValueType(TestType.class, "map"));
    }
}
