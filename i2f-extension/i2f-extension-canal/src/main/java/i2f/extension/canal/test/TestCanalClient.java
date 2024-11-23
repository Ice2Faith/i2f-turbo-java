package i2f.extension.canal.test;

import com.alibaba.otter.canal.protocol.CanalEntry;
import i2f.extension.canal.CanalClient;
import i2f.extension.canal.meta.CanalMeta;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/11/23 16:29
 */
public class TestCanalClient {
    public static void main(String[] args) {
        new CanalClient(new CanalMeta("192.168.1.100", 11111,
                "example",
                "canal", "xxx123456")) {
            @Override
            public void onInsertEventMap(Map<String, Object> after, String tableName, CanalEntry.Entry entry) {
                System.out.println("insert:" + tableName + "\n" + after);
            }

            @Override
            public void onDeleteEventMap(Map<String, Object> before, String tableName, CanalEntry.Entry entry) {
                System.out.println("delete:" + tableName + "\n" + before);
            }

            @Override
            public void onUpdateEventMap(Map<String, Object> after, Map<String, Object> before, String tableName, CanalEntry.Entry entry) {
                System.out.println("update:" + tableName + "\nbefore:" + before + "\nafter:" + after);
            }
        }.pattern("test_db.*")
                .batchSize(100)
                .idleMillSeconds(3000)
                .run();
    }
}
