package i2f.streaming.thread;

/**
 * @author Ice2Faith
 * @date 2024/2/26 9:31
 * @desc
 */
public abstract class AtomicCountDownLatchRunnable extends LifeCycleRunnable<AtomicCountDownLatch> {
    public AtomicCountDownLatchRunnable(AtomicCountDownLatch resource) {
        super(resource);
    }

    @Override
    public void onBefore(AtomicCountDownLatch resource) {

    }

    @Override
    public void onAfter(AtomicCountDownLatch resource) {
        if (resource != null) {
            System.out.println("down:" + Thread.currentThread().getName() + "-" + Thread.currentThread().getId());
            resource.down();
        }
    }
}
