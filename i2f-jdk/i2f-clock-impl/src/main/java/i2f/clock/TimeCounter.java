package i2f.clock;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Ice2Faith
 * @date 2024/3/25 14:06
 * @desc
 */
public class TimeCounter {
    public static void main(String[] args) throws Exception {
        TimeCounter counter = TimeCounter.begin();
        Thread.sleep(30);
        System.out.println(counter.end("hot"));
        Thread.sleep(30);
        System.out.println(counter.end("running"));
        Thread.sleep(30);
        System.out.println(counter.end("finish"));
        System.out.println(counter.last());
        System.out.println(counter.sum());
    }

    protected volatile long initTs = 0;
    protected volatile long lastTs = 0;
    protected volatile long currTs = 0;
    protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected final LinkedList<SegmentRecord> records = new LinkedList<>();

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static String formatAbsoluteTime(long milliseconds) {
        LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
        return FORMATTER.format(dt);
    }

    public static String formatDuration(long milliseconds) {
        long hours = milliseconds / 3600000;
        long remainder = milliseconds % 3600000;
        long minutes = remainder / 60000;
        remainder = remainder % 60000;
        long seconds = remainder / 1000;
        long ms = remainder % 1000;

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, ms);
    }

    @Data
    @NoArgsConstructor
    public static class SegmentRecord {
        protected long beginTs;
        protected long endTs;
        protected String description;

        public SegmentRecord(long beginTs, long endTs, String description) {
            this.beginTs = beginTs;
            this.endTs = endTs;
            this.description = description;
        }

        public long duration() {
            return endTs - beginTs;
        }

        @Override
        public String toString() {
            return String.format("%s -> %s (%s): %s",
                    formatAbsoluteTime(beginTs),
                    formatAbsoluteTime(endTs),
                    formatDuration(duration()),
                    description == null ? "step" : description
            );
        }
    }

    public static TimeCounter begin() {
        return new TimeCounter();
    }

    public TimeCounter() {
        reset();
    }

    public TimeCounter reset() {
        lock.writeLock().lock();
        try {
            initTs = System.currentTimeMillis();
            lastTs = initTs;
            currTs = initTs;
            records.clear();
        } finally {
            lock.writeLock().unlock();
        }
        return this;
    }

    public SegmentRecord end() {
        lock.writeLock().lock();
        try {
            int size = records.size();
            return end("step-" + (size + 1));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public SegmentRecord end(String description) {
        lock.writeLock().lock();
        try {
            long ts = System.currentTimeMillis();
            SegmentRecord ret = new SegmentRecord(lastTs, ts, description);
            records.add(ret);
            lastTs = ts;
            currTs = ts;
            return ret;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public SegmentRecord last() {
        lock.readLock().lock();
        try {
            SegmentRecord last = records.isEmpty() ? new SegmentRecord(lastTs, currTs, "last") : records.getLast();
            return last;
        } finally {
            lock.readLock().unlock();
        }
    }

    public SegmentRecord sum() {
        return new SegmentRecord(initTs, currTs, "sum");
    }

    public List<SegmentRecord> records() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(records);
        } finally {
            lock.readLock().unlock();
        }
    }


}
