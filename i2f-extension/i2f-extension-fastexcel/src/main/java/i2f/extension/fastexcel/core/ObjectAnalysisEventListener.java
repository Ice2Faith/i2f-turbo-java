package i2f.extension.fastexcel.core;

import java.util.List;
import java.util.function.BiConsumer;

public class ObjectAnalysisEventListener<T> extends WrapAnalysisEventListener<T, T> {
    public ObjectAnalysisEventListener() {
    }

    public ObjectAnalysisEventListener(BiConsumer<List<T>, List<T>> batchConsumer) {
        super(batchConsumer);
    }

    public ObjectAnalysisEventListener(int batchSize, BiConsumer<List<T>, List<T>> batchConsumer) {
        super(batchSize, batchConsumer);
    }
}
