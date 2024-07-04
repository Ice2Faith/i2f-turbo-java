package i2f.url.test;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/7/4 10:22
 * @desc
 */
@Data
@NoArgsConstructor
public class TestFormBean {
    private String username;
    private String password;
    private List<RoleItem> roles;

    @Data
    @NoArgsConstructor
    public static class RoleItem {
        private Integer id;
        private String key;
        private String name;
        private List<String> perms;
    }
}
