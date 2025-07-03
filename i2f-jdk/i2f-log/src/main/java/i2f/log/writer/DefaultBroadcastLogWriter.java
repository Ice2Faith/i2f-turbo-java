package i2f.log.writer;

import i2f.log.provider.LogWriterProvider;
import i2f.log.std.data.LogData;
import i2f.log.writer.impl.StdoutPlanTextLogWriter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/7/1 10:44
 * @desc
 */
@Data
@NoArgsConstructor
public class DefaultBroadcastLogWriter implements ILogWriter {
    private Map<String, ILogWriter> writers = new ConcurrentHashMap<>();
    private AtomicBoolean async = new AtomicBoolean(true);
    private volatile ExecutorService pool = Executors.newWorkStealingPool(5);

    {
        loadStdoutWriter();
        loadSpiWriters();
    }

    public DefaultBroadcastLogWriter(boolean async, int parallelism) {
        this.async.set(async);
        this.pool = Executors.newWorkStealingPool(Math.max(1, parallelism));
    }

    public void adjustAsync(boolean async) {
        this.async.set(async);
    }

    public void adjustPoolSize(int parallelism) {
        if (parallelism <= 0) {
            return;
        }
        this.pool = Executors.newWorkStealingPool(parallelism);
    }

    public void registry(String name, ILogWriter writer) {
        writers.put(name, writer);
    }

    public void remove(String name) {
        writers.remove(name);
    }

    public void loadStdoutWriter() {
        writers.put(StdoutPlanTextLogWriter.WRITER_NAME, new StdoutPlanTextLogWriter());
    }

    public void loadSpiWriters() {
        ServiceLoader<LogWriterProvider> providers = ServiceLoader.load(LogWriterProvider.class);
        List<LogWriterProvider> loaded = new ArrayList<>();
        for (LogWriterProvider provider : providers) {
            if (provider.test()) {
                String name = provider.getName();
                ILogWriter writer = provider.getWriter();
                writers.put(name, writer);
                System.out.println("spi log writer append : " + name);
                loaded.add(provider);
            }
        }
        for (LogWriterProvider provider : loaded) {
            provider.loaded(this);
        }
    }


    @Override
    public void write(LogData data) {
        for (Map.Entry<String, ILogWriter> entry : writers.entrySet()) {
            if (async.get()) {
                pool.submit(() -> {
                    try {
                        ILogWriter writer = entry.getValue();
                        writer.write(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                try {
                    ILogWriter writer = entry.getValue();
                    writer.write(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
