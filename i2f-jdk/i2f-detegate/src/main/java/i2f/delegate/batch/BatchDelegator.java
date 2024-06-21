package i2f.delegate.batch;

import i2f.functional.consumer.except.impl.IExConsumer2;
import i2f.functional.func.except.impl.IExFunction1;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Ice2Faith
 * @date 2024/6/21 16:12
 * @desc 批量委托
 */
public class BatchDelegator {
    public static <T, E, R extends Iterable<E>> void batch(
            T arg,
            IExFunction1<R, T> elementsSupplier,
            int batchCount,
            IExConsumer2<List<E>, T> batchConsumer) throws Throwable {
        batch(arg, elementsSupplier, batchCount, LinkedList::new, batchConsumer);
    }

    /**
     * 批量委托
     * 对elementsSupplier以arg为参数提供的系列可迭代对象进行每batchCount个元素提供给batchConsumer消费
     * 当elementsSupplier返回为null时，终止运行
     *
     * @param arg               可使用的一个参数
     * @param elementsSupplier  提供一些列的可迭代对象，返回null则表示没有更多元素
     * @param batchCount        每批次提供给batchConsumer的元素个数
     * @param containerSupplier 容器提供者，为存储元素产生新的容器
     * @param batchConsumer     批量消费者，消费每批次的数据
     * @param <T>               参数类型
     * @param <E>               元素类型
     * @param <R>               可迭代对象类型
     * @param <C>               批次集合类型
     * @throws Throwable
     */
    public static <T, E, R extends Iterable<E>, C extends Collection<E>> void batch(
            T arg,
            IExFunction1<R, T> elementsSupplier,
            int batchCount,
            Supplier<C> containerSupplier,
            IExConsumer2<C, T> batchConsumer) throws Throwable {
        C once = null;
        int currentCount = 0;
        while (true) {
            R elements = elementsSupplier.apply(arg);
            if (elements == null) {
                break;
            }
            for (E element : elements) {
                if (once == null) {
                    once = containerSupplier.get();
                }
                once.add(element);
                currentCount++;
                if (currentCount == batchCount) {
                    batchConsumer.accept(once, arg);
                    once = containerSupplier.get();
                    currentCount = 0;
                }
            }
        }
        if (once != null && !once.isEmpty()) {
            batchConsumer.accept(once, arg);
        }
    }
}
