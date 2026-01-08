package i2f.lru;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/1/8 13:58
 * @desc
 */
public class WeakEntry<K,V> extends WeakReference<Object> implements Map.Entry<K,V> {
    protected static ReferenceQueue<Object> queue=new ReferenceQueue<>();
    protected volatile V value;

    static{
        Thread thread = new Thread(()->{
            while(true) {
                Reference<?> poll = queue.poll();
                if (poll != null) {
                    // 如果能读取到，则可能有多个，直接进行循环
                    do {
                        WeakEntry entry = (WeakEntry) poll;
                        entry.value=null;
                    }while((poll= queue.poll())!=null);
                }else{
                    // 如果读取不到，当前轮空，进行随眠等到
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {

                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("weak-entry-cleaner");
        thread.start();
    }

    public WeakEntry(K key,V value) {
        super(key,queue);
        this.value = value;
    }

    protected K key(){
        Object k = get();
        if(k==null){
            this.value=null;
        }
        return (K)k;
    }

    @Override
    public K getKey() {
        return key();
    }

    @Override
    public V getValue() {
        key();
        return this.value;
    }

    @Override
    public V setValue(V value) {
        V ret=this.value;
        if(key()==null){
            return ret;
        }
        this.value=value;
        return ret;
    }

    @Override
    public void clear(){
        super.clear();
        this.value=null;
    }
}
