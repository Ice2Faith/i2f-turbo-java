package i2f.web.filter;

import i2f.clock.SystemClock;
import lombok.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/11/14 20:35
 * @desc
 */
@Data
@NoArgsConstructor
public class MoveAverageProcessTimeStatFilter extends OncePerHttpServletFilter {
    // 用于构建分段锁，提高并发性能
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final ConcurrentHashMap<String, ReentrantLock> segmentLockMap = new ConcurrentHashMap<>(1024);
    protected final ConcurrentHashMap<String, MoveAverageProcessTimeStatVo> statMap = new ConcurrentHashMap<>();
    protected final AtomicInteger windowCount = new AtomicInteger(100);
    protected volatile Predicate<String> sourcePathFilter = null;

    @Data
    @NoArgsConstructor
    public static class MoveAverageProcessTimeStatVo {
        protected final LinkedBlockingDeque<Long> timeQueue = new LinkedBlockingDeque<>();
        protected volatile AtomicLong sumTime = new AtomicLong(0);
        protected final AtomicInteger count = new AtomicInteger(0);
        protected volatile double avgTime = 0;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        if (sourcePathFilter != null && !sourcePathFilter.test(requestURI)) {
            chain.doFilter(request, response);
            return;
        }
        long startTime = SystemClock.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } finally {
            long endTime = SystemClock.currentTimeMillis();
            long useTime = endTime - startTime;
            // 基于请求的路径，使用hashCode分段为1024个分段
            String segment = String.valueOf(requestURI.hashCode() % 1024);
            // 根据Map的特性得到本次请求的分段锁
            ReentrantLock segmentLock = segmentLockMap.computeIfAbsent(segment, key -> new ReentrantLock());
            try {
                segmentLock.lock();
                statMap.compute(requestURI, (key, old) -> {
                    if (old == null) {
                        old = new MoveAverageProcessTimeStatVo();
                    }
                    old.getTimeQueue().add(useTime);
                    old.getSumTime().addAndGet(useTime);
                    if (old.getCount().incrementAndGet() > windowCount.get()) {
                        // 如果本次请求进来之后，技术大于窗口，这移除最老的一个时长，并从窗口中时长中扣除最老的一个时长
                        old.getCount().decrementAndGet();
                        try {
                            Long lastTime = old.timeQueue.take();
                            old.getSumTime().addAndGet(lastTime);
                        } catch (InterruptedException e) {
                            // 上面已经保证了先放入至少一个元素到队列中，因此这个异常也可以不处理

                        }
                    }
                    old.setAvgTime(old.getSumTime().get() * 1.0 / old.getCount().get());
                    return old;
                });
            } finally {
                segmentLock.unlock();
            }
        }
    }
}
