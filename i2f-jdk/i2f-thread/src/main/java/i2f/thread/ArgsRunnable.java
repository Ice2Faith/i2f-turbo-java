package i2f.thread;

/**
 * @author Ice2Faith
 * @date 2022/4/11 8:43
 * @desc
 */
public abstract class ArgsRunnable implements Runnable {
    protected Object[] args;

    public ArgsRunnable(Object... args) {
        this.args = args;
    }

    @Override
    public void run() {
        doRun(args);
    }

    public abstract void doRun(Object... args);
}
