package i2f.extension.fastexcel.core;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public class WrapAnalysisEventListener<T, R> extends AnalysisEventListener<T> {
    private List<R> legalData = new LinkedList<>();
    private List<R> illegalData = new LinkedList<>();
    protected int currSize = 0;
    protected int batchSize = 500;
    protected BiConsumer<List<R>, List<R>> batchConsumer;

    public WrapAnalysisEventListener() {
    }

    public WrapAnalysisEventListener(BiConsumer<List<R>, List<R>> batchConsumer) {
        this.batchConsumer = batchConsumer;
    }

    public WrapAnalysisEventListener(int batchSize, BiConsumer<List<R>, List<R>> batchConsumer) {
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
    protected boolean verify(R bean, AnalysisContext context) {
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
    protected R convert(R bean, AnalysisContext context) {
        return bean;
    }

    public List<R> getLegalData() {
        return legalData;
    }

    public List<R> getIllegalData() {
        return illegalData;
    }

    protected R beforeConvert(T bean, AnalysisContext context) {
        return (R) bean;
    }

    @Override
    public void invoke(T obj, AnalysisContext context) {
        R item = beforeConvert(obj, context);
        if (!verify(item, context)) {
            illegalData.add(item);
            return;
        }
        item = convert(item, context);
        legalData.add(item);

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
