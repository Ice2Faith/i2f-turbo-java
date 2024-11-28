package i2f.javacode.graph.data;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/11/27 21:33
 * @desc
 */
@Data
public class JavaCodeRelation {
    protected String startSignature;
    protected String endSignature;
    protected JavaRelationType relationType;
}
