package i2f.extension.jackson.sensible.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.extension.jackson.sensible.handler.AbsDictSensibleHandler;
import i2f.extension.jackson.sensible.handler.ISensibleHandler;
import i2f.extension.jackson.sensible.holder.SensibleHandlersHolder;

import java.util.Arrays;
import java.util.List;


public class TestSensible extends AbsDictSensibleHandler {

    public static void main(String[] args) throws Exception {
        List<ISensibleHandler> handlers = Arrays.asList(new TestSensible());
        SensibleHandlersHolder.GLOBAL_HANDLERS.addAll(handlers);
        test(new ObjectMapper());
    }


    public static void test(ObjectMapper objectMapper) throws Exception {
        TestBean bean = new TestBean();
        bean.setPhone("18200001111");
        bean.setEmail("554711112222@163.com");
        bean.setIdCard("522000190001010001");
        bean.setPassword("123456");
        bean.setRealname("刘明华");
        bean.setCountry(86L);

        String str = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bean);

        System.out.println(str);
    }

    @Override
    protected Object decodeDict(String dictType, Object dictValue) {
        String dictCode = dictType;
        if ("country".equals(dictCode)) {
            if (86L == (Long) dictValue) {
                return "中国";
            }
        }
        return "";
    }
}
