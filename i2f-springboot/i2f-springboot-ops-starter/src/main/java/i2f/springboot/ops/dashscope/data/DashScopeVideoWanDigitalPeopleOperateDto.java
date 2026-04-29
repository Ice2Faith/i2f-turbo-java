package i2f.springboot.ops.dashscope.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2026/4/29
 * @desc 数字人视频生成操作DTO（wan2.2-s2v）
 */
@Data
@NoArgsConstructor
public class DashScopeVideoWanDigitalPeopleOperateDto extends DashScopeBaseOperateDto {
    /**
     * 人物肖像图片URL（支持 HTTP/HTTPS 及 oss://）
     */
    protected String imageUrl;
    /**
     * 驱动音频URL（支持 HTTP/HTTPS 及 oss://，wav/mp3，<15M，<20s）
     */
    protected String audioUrl;
    /**
     * 视频分辨率，可选值：480P、720P，默认 480P
     */
    protected String resolution;
}
