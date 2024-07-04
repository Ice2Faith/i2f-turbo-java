package i2f.url.test;

import i2f.url.FormUrlEncodedEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/7/4 10:26
 * @desc
 */
public class TestFormUrlEncoded {
    public static void main(String[] args) throws Exception {

        TestFormBean jsonMap = new TestFormBean();
        jsonMap.setUsername("admin");
        jsonMap.setPassword("123456");
        jsonMap.setRoles(new ArrayList<>());
        jsonMap.getRoles().add(new TestFormBean.RoleItem());
        jsonMap.getRoles().get(0).setId(1);
        jsonMap.getRoles().get(0).setKey("admin");
        jsonMap.getRoles().get(0).setName("admin");
        jsonMap.getRoles().get(0).setPerms(new ArrayList<>(Arrays.asList("home", "index")));
        jsonMap.getRoles().add(new TestFormBean.RoleItem());
        jsonMap.getRoles().get(1).setId(2);
        jsonMap.getRoles().get(1).setKey("root");
        jsonMap.getRoles().get(1).setName("root");
        jsonMap.getRoles().get(1).setPerms(new ArrayList<>(Arrays.asList("home", "index", "system")));


        String form = FormUrlEncodedEncoder.toForm(jsonMap);

        Map<String, Object> formMap = FormUrlEncodedEncoder.ofFormMapTree(form);

        TestFormBean bean = FormUrlEncodedEncoder.ofFormBean(form, TestFormBean.class);
        System.out.println("ok");
    }
}
