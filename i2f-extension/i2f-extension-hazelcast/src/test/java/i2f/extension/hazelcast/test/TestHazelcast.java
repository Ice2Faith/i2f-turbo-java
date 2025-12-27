package i2f.extension.hazelcast.test;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import i2f.extension.hazelcast.HazelcastUtil;

/**
 * @author Ice2Faith
 * @date 2025/8/3 18:47
 */
public class TestHazelcast {
    public static void main(String[] args) throws Exception {
        ClientConfig config = new ClientConfig();
        config.setClusterName("demo");

        HazelcastInstance client = HazelcastUtil.getDefaultInstance("demo");
        IMap<Object, Object> map = client.getMap("map");

        map.put("1", "m1");

        Object obj = map.get("1");
        System.out.println(obj);

        IQueue<Object> queue = client.getQueue("queue");
        queue.put("1");

        obj = queue.poll();
        System.out.println(obj);

    }
}
