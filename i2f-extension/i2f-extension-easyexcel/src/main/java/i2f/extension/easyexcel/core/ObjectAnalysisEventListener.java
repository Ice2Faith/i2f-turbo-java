package i2f.extension.easyexcel.core;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public class ObjectAnalysisEventListener<T> extends AnalysisEventListener<T> {
    private List<T> legalData = new LinkedList<>();
    private List<T> illegalData = new LinkedList<>();
    protected int currSize = 0;
    protected int batchSize = 500;
    protected BiConsumer<List<T>, List<T>> batchConsumer;

    public ObjectAnalysisEventListener() {
    }

    public ObjectAnalysisEventListener(BiConsumer<List<T>, List<T>> batchConsumer) {
        this.batchConsumer = batchConsumer;
    }

    public ObjectAnalysisEventListener(int batchSize, BiConsumer<List<T>, List<T>> batchConsumer) {
        this.batchSize = batchSize;
        this.batchConsumer = batchConsumer;
    }

    /**
     * 按需实现过滤错误数据逻辑，返回false则表述数据错误
     *
     * @param bean
     * @param context
     * @return
     */
    protected boolean verify(T bean, AnalysisContext context) {
        return true;
    }

    /**
     * 按需实现数据转换逻辑，根据需要可能需要对这个实体bean的字段进行转换
     * 常见的就是做字典的翻译
     *
     * @param bean
     * @param context
     * @return
     */
    protected T convert(T bean, AnalysisContext context) {
        return bean;
    }

    public List<T> getLegalData() {
        return legalData;
    }

    public List<T> getIllegalData() {
        return illegalData;
    }

    @Override
    public void invoke(T obj, AnalysisContext context) {
        if (!verify(obj, context)) {
            illegalData.add(obj);
            return;
        }
        obj = convert(obj, context);
        legalData.add(obj);

        currSize++;
        if (currSize == batchSize) {
            if (batchConsumer != null) {
                batchConsumer.accept(legalData, illegalData);
                legalData = new LinkedList<>();
                illegalData = new LinkedList<>();
                currSize = 0;
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (batchConsumer != null) {
            batchConsumer.accept(legalData, illegalData);
            legalData = new LinkedList<>();
            illegalData = new LinkedList<>();
            currSize = 0;
        }
    }
}
