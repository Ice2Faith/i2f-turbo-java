package i2f.extension.elasticsearch;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/26 22:05
 * @desc
 */
@Data
@NoArgsConstructor
public class EsMeta {
    protected List<String> urls=new ArrayList<>();
    protected String username;
    protected String password;
    protected Integer maxConnTotal;
    protected Integer maxConnPerRoute;
}
