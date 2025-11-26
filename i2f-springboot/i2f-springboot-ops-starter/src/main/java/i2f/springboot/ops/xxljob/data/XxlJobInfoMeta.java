package i2f.springboot.ops.xxljob.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2025/11/26 14:57
 */
@Data
@NoArgsConstructor
public class XxlJobInfoMeta {
    protected String id;
    protected String jobGroup;
    protected String jobDesc;
    protected String addTime;
    protected String updateTime;
    protected String author;
    protected String alarmEmail;
    protected String scheduleType;
    protected String scheduleConf;
    protected String misfireStrategy;
    protected String executorRouteStrategy;
    protected String executorHandler;
    protected String executorParam;
    protected String executorBlockStrategy;
    protected String executorTimeout;
    protected String executorFailRetryCount;
    protected String glueType;
    protected String glueSource;
    protected String glueRemark;
    protected String glueUpdatetime;
    protected String childJobid;
    protected String triggerStatus;
    protected String triggerLastTime;
    protected String triggerNextTime;
    protected String remark;
}
