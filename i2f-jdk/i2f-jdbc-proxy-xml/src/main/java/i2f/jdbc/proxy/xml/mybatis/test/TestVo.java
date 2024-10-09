package i2f.jdbc.proxy.xml.mybatis.test;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/10/8 16:45
 */
@Data
@NoArgsConstructor
public class TestVo {
    private Object id;
    private String code;
    private String name;
    private List<Integer> vals;
}
