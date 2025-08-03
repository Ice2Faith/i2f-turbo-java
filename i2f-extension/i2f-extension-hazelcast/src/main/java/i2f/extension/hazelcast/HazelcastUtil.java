package i2f.extension.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

/**
 * @author Ice2Faith
 * @date 2025/8/3 18:28
 */
public class HazelcastUtil {
    public static HazelcastInstance getDefaultInstance(String clusterName){
        ClientConfig config=new ClientConfig();
        config.setClusterName(clusterName);

        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);

        return client;
    }

}
