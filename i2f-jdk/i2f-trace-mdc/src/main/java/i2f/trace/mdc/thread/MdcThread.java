package i2f.trace.mdc.thread;

/**
 * @author Ice2Faith
 * @date 2026/4/15 10:15
 * @desc
 */
public class MdcThread extends Thread {
    public MdcThread() {
    }

    public MdcThread(Runnable target) {
        super(MdcRunnable.of(target));
    }

    public MdcThread(ThreadGroup group, Runnable target) {
        super(group, MdcRunnable.of(target));
    }

    public MdcThread(String name) {
        super(name);
    }

    public MdcThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public MdcThread(Runnable target, String name) {
        super(MdcRunnable.of(target), name);
    }

    public MdcThread(ThreadGroup group, Runnable target, String name) {
        super(group, MdcRunnable.of(target), name);
    }

    public MdcThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, MdcRunnable.of(target), name, stackSize);
    }

}
