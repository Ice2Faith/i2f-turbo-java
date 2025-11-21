package i2f.springboot.ops.app.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/8 18:07
 * @desc
 */
@Data
@NoArgsConstructor
public class AppThreadInfoDto {
    protected String threadName;
    protected long threadId;
    protected long blockedTime;
    protected long blockedCount;
    protected long waitedTime;
    protected long waitedCount;
    protected AppLockInfoDto lock;
    protected String lockName;
    protected long lockOwnerId;
    protected String lockOwnerName;
    protected boolean inNative;
    protected boolean suspended;
    protected Thread.State threadState;
    protected List<AppMonitorInfoDto> lockedMonitors;
    protected List<AppLockInfoDto> lockedSynchronizers;

    public static AppThreadInfoDto of(ThreadInfo info) {
        AppThreadInfoDto dto = new AppThreadInfoDto();
        dto.setThreadName(info.getThreadName());
        dto.setThreadId(info.getThreadId());
        dto.setBlockedTime(info.getBlockedTime());
        dto.setBlockedCount(info.getBlockedCount());
        dto.setWaitedTime(info.getWaitedTime());
        dto.setWaitedCount(info.getWaitedCount());
        LockInfo lockInfo = info.getLockInfo();
        if (lockInfo != null) {
            dto.setLock(AppLockInfoDto.of(lockInfo));
        }
        dto.setLockName(info.getLockName());
        dto.setLockOwnerId(info.getLockOwnerId());
        dto.setLockOwnerName(info.getLockOwnerName());
        dto.setInNative(info.isInNative());
        dto.setSuspended(info.isSuspended());
        dto.setThreadState(info.getThreadState());
        dto.setLockedMonitors(new ArrayList<>());
        MonitorInfo[] lockedMonitors = info.getLockedMonitors();
        if (lockedMonitors != null) {
            for (MonitorInfo item : lockedMonitors) {
                dto.getLockedMonitors().add(AppMonitorInfoDto.of(item));
            }
        }
        dto.setLockedSynchronizers(new ArrayList<>());
        LockInfo[] lockedSynchronizers = info.getLockedSynchronizers();
        if (lockedSynchronizers != null) {
            for (LockInfo item : lockedSynchronizers) {
                dto.getLockedSynchronizers().add(AppLockInfoDto.of(item));
            }
        }
        return dto;
    }
}
