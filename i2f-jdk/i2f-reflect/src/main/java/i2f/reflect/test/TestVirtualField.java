package i2f.reflect.test;

import i2f.reflect.ReflectResolver;
import i2f.reflect.virtual.VirtualField;
import i2f.reflect.virtual.impl.FieldGetterSetterField;
import i2f.reflect.virtual.impl.MethodGetterSetterField;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/9/28 10:01
 */
public class TestVirtualField {
    public static void main(String[] args) {
        testFields();
        testFields0();
    }

    public static void testFields() {
        Map<String, VirtualField> map = ReflectResolver.getVirtualFields(AnnName.class);
        printMap(map);

        map = ReflectResolver.getVirtualFields(TestContext.class);
        printMap(map);

        map = ReflectResolver.getVirtualFields(FieldGetterSetterField.class);
        printMap(map);

        map = ReflectResolver.getVirtualFields(MethodGetterSetterField.class);
        printMap(map);
    }

    public static void printMap(Map<String, VirtualField> map) {
        System.out.println("============fields==============");
        for (Map.Entry<String, VirtualField> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "==>" + entry);
        }
    }

    public static void testFields0() {
        Map<VirtualField, Class<?>> map = ReflectResolver.getVirtualFields0(AnnName.class);
        printMap0(map);

        map = ReflectResolver.getVirtualFields0(TestContext.class);
        printMap0(map);

        map = ReflectResolver.getVirtualFields0(FieldGetterSetterField.class);
        printMap0(map);

        map = ReflectResolver.getVirtualFields0(MethodGetterSetterField.class);
        printMap0(map);
    }

    public static void printMap0(Map<VirtualField, Class<?>> map) {
        System.out.println("===========fields0===============");
        for (Map.Entry<VirtualField, Class<?>> entry : map.entrySet()) {
            System.out.println(entry.getKey().name() + "==>" + entry);
        }
    }
}
