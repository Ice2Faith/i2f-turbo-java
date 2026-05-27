package i2f.design.pattern.concurrency.producerConsumer.buffer;

import i2f.design.pattern.concurrency.producerConsumer.product.Dish;

/**
 * 生产者-消费者模式 —— 共享缓冲区（SharedBuffer：FixedSizeBuffer）
 *
 * <p><b>角色：</b>共享缓冲区（Shared Buffer）</p>
 *
 * <p><b>模式说明：</b>提供固定容量的缓冲区，协调生产者与消费者之间的数据流转。
 * 当缓冲区满时，生产者线程等待；当缓冲区空时，消费者线程等待。
 * 通过 synchronized 和 wait/notify 机制实现线程安全的阻塞队列。</p>
 *
 * <p><b>命名立意：</b>"出餐台"天然地充当缓冲区角色——
 * 厨师（生产者）将制作好的菜品放在出餐台上，
 * 服务员（消费者）从出餐台取走菜品。
 * 出餐台容量有限，满了厨师就要等，空了服务员就要等。</p>
 *
 * <p><b>核心方法：</b></p>
 * <pre>
 *  put(Dish dish)    : void    ← 生产者放入菜品（缓冲区满时阻塞）
 *  take()            : Dish    ← 消费者取出菜品（缓冲区空时阻塞）
 *  size()            : int     ← 当前缓冲区中的菜品数量
 *  capacity()        : int     ← 缓冲区最大容量
 * </pre>
 *
 * <p><b>线程安全机制：</b></p>
 * <ul>
 *   <li>使用 synchronized 保证 put 和 take 操作的原子性</li>
 *   <li>使用 wait() 让线程在条件不满足时进入等待状态</li>
 *   <li>使用 notifyAll() 唤醒等待的线程继续执行</li>
 * </ul>
 *
 * @author Ice2Faith
 * @date 2026/5/21 10:30
 * @see Dish
 */
public class FixedSizeBuffer {

    /**
     * 缓冲区数组（使用数组实现循环队列）。
     */
    private final Dish[] buffer;

    /**
     * 缓冲区最大容量。
     */
    private final int capacity;

    /**
     * 当前缓冲区中的菜品数量。
     */
    private int count;

    /**
     * 队头指针（消费者取菜位置）。
     */
    private int head;

    /**
     * 队尾指针（生产者放菜位置）。
     */
    private int tail;

    /**
     * 构造固定容量的缓冲区。
     *
     * @param capacity 缓冲区最大容量
     */
    public FixedSizeBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new Dish[capacity];
        this.count = 0;
        this.head = 0;
        this.tail = 0;
    }

    /**
     * 生产者放入菜品到缓冲区。
     *
     * <p>如果缓冲区已满，生产者线程将等待，直到消费者取出菜品后再继续。</p>
     *
     * @param dish 要放入的菜品
     * @throws InterruptedException 如果线程在等待时被中断
     */
    public synchronized void put(Dish dish) throws InterruptedException {
        // 缓冲区满时，生产者等待
        while (count == capacity) {
            System.out.println("  [缓冲区已满] 容量: " + capacity + "/" + capacity + "，厨师需要等待...");
            wait();
        }

        // 将菜品放入缓冲区
        buffer[tail] = dish;
        tail = (tail + 1) % capacity;
        count++;

        System.out.println("  [放入菜品] " + dish.getName() + " | 当前缓冲区: " + count + "/" + capacity);

        // 唤醒等待的消费者线程
        notifyAll();
    }

    /**
     * 消费者从缓冲区取出菜品。
     *
     * <p>如果缓冲区为空，消费者线程将等待，直到生产者放入新菜品后再继续。</p>
     *
     * @return 取出的菜品
     * @throws InterruptedException 如果线程在等待时被中断
     */
    public synchronized Dish take() throws InterruptedException {
        // 缓冲区空时，消费者等待
        while (count == 0) {
            System.out.println("  [缓冲区为空] 容量: 0/" + capacity + "，服务员需要等待...");
            wait();
        }

        // 从缓冲区取出菜品
        Dish dish = buffer[head];
        head = (head + 1) % capacity;
        count--;

        System.out.println("  [取出菜品] " + dish.getName() + " | 当前缓冲区: " + count + "/" + capacity);

        // 唤醒等待的生产者线程
        notifyAll();

        return dish;
    }

    /**
     * 获取当前缓冲区中的菜品数量。
     *
     * @return 当前数量
     */
    public synchronized int size() {
        return count;
    }

    /**
     * 获取缓冲区最大容量。
     *
     * @return 最大容量
     */
    public int capacity() {
        return capacity;
    }

    /**
     * 获取缓冲区状态描述。
     *
     * @return 状态字符串
     */
    @Override
    public String toString() {
        return "FixedSizeBuffer{容量=" + capacity + ", 当前数量=" + count + "}";
    }
}
