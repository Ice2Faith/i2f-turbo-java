package i2f.extension.reverse.engineer.generator.database;

import lombok.Data;

/**
 * @author ltb
 * @date 2022/6/15 16:49
 * @desc
 */
@Data
public class JavaCodeContext {
    private String pkg;
    private String author;
    private boolean useLombok;
    private boolean useSwagger;
    private boolean useMybatisPlus;
    private boolean useDbAnnotation;
}