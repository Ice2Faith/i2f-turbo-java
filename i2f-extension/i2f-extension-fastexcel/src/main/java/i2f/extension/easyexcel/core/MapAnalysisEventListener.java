package i2f.extension.easyexcel.core;

import cn.idev.excel.context.AnalysisContext;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author Ice2Faith
 * @date 2025/1/17 20:44
 * @desc
 */
public class MapAnalysisEventListener extends WrapAnalysisEventListener<Map<Integer, Object>, Map<String, Object>> {
    protected Map<Integer, String> mapHead;

    public MapAnalysisEventListener() {
    }

    public MapAnalysisEventListener(BiConsumer<List<Map<String, Object>>, List<Map<String, Object>>> batchConsumer) {
        super(batchConsumer);
    }

    public MapAnalysisEventListener(int batchSize, BiConsumer<List<Map<String, Object>>, List<Map<String, Object>>> batchConsumer) {
        super(batchSize, batchConsumer);
    }

    @Override
    protected Map<String, Object> beforeConvert(Map<Integer, Object> bean, AnalysisContext context) {
        Map<String, Object> ret = new LinkedHashMap<>();
        for (Map.Entry<Integer, String> entry : mapHead.entrySet()) {
            Object val = bean.get(entry.getKey());
            String key = entry.getValue();
            ret.put(key, val);
        }
        return ret;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.mapHead = headMap;
    }
}
