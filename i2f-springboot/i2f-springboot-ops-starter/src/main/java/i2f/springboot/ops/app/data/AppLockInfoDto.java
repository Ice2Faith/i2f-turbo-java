package i2f.springboot.ops.app.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.management.LockInfo;

/**
 * @author Ice2Faith
 * @date 2025/11/8 18:08
 * @desc
 */
@Data
@NoArgsConstructor
public class AppLockInfoDto {
    protected String className;
    protected int    identityHashCode;

    public static AppLockInfoDto of(LockInfo info){
        AppLockInfoDto dto = new AppLockInfoDto();
        dto.setClassName(info.getClassName());
        dto.setIdentityHashCode(info.getIdentityHashCode());
        return dto;
    }
}
