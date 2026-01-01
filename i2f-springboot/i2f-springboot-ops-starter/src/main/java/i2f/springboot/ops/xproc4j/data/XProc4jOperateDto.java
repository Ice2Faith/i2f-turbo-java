package i2f.springboot.ops.xproc4j.data;

import i2f.springboot.ops.common.OpsHostIdDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/12/5 10:02
 * @desc
 */
@Data
@NoArgsConstructor
public class XProc4jOperateDto extends OpsHostIdDto {
    protected String procedureId;
    protected Map<String, Object> params;

    protected String lang;
    protected String script;

    protected Boolean async;
    protected Long waitForSeconds;

    protected Boolean enableDebug;
    protected Boolean enableLog;
}
