package i2f.springboot.ops.app.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.management.MonitorInfo;

/**
 * @author Ice2Faith
 * @date 2025/11/8 18:10
 * @desc
 */
@Data
@NoArgsConstructor
public class AppMonitorInfoDto {
    protected String className;
    protected int    identityHashCode;
    protected int    lockedStackDepth;
    public static AppMonitorInfoDto of(MonitorInfo info) {
        AppMonitorInfoDto dto = new AppMonitorInfoDto();
        dto.setClassName(info.getClassName());
        dto.setIdentityHashCode(info.getIdentityHashCode());
        dto.setLockedStackDepth(info.getLockedStackDepth());
        return dto;
    }
}
