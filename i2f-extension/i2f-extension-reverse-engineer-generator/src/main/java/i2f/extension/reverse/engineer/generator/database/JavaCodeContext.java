package i2f.extension.reverse.engineer.generator.database;

import lombok.Data;

/**
 * @author ltb
 * @date 2022/6/15 16:49
 * @desc
 */
@Data
public class JavaCodeContext {
    private String author = "test";
    private String pkg = "com.test";
    private String api = "/api";
    private boolean restful = true;
    private boolean lombok = true;
    private boolean swagger = true;
    private boolean pageHelper = false;
    private boolean beanCopy = true;
    private boolean springCopy = false;
    private boolean unionBatch = true;
}
