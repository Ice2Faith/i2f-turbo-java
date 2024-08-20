package i2f.thread;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Ice2Faith
 * @date 2024/8/20 20:58
 * @desc
 */
@Data
@NoArgsConstructor
public class ThreadWorkInfo {
    protected String threadName;
    protected Thread thread;
    protected long startTs;
    protected long endTs;
    protected AtomicLong taskCount=new AtomicLong(0);
    protected Throwable throwable;
    protected LinkedList<Long> taskUseMillSecondsList=new LinkedList<>();
    protected LinkedList<Long> taskWatermarkMillSecondsList=new LinkedList<>();
    protected LinkedList<Long> taskCpuMillSecondsList=new LinkedList<>();
    protected long taskUseMillSeconds;
    protected int taskUseCount;
}
