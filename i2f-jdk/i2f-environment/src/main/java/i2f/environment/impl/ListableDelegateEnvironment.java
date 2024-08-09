package i2f.environment.impl;

import i2f.environment.IEnvironment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2024/8/9 11:08
 * @desc
 */
public class ListableDelegateEnvironment implements IEnvironment {
    protected CopyOnWriteArrayList<IEnvironment> list = new CopyOnWriteArrayList<>();

    {
        list.add(SystemAdditionalEnvironment.INSTANCE);
    }

    @Override
    public String getProperty(String name) {
        for (IEnvironment env : list) {
            String prop = env.getProperty(name);
            if (prop != null) {
                return prop;
            }
        }
        return null;
    }

    @Override
    public Map<String, String> getAllProperties() {
        Map<String, String> ret = new LinkedHashMap<>();
        ArrayList<IEnvironment> arr = new ArrayList<>(list);
        int size = arr.size();
        for (int i = size - 1; i >= 0; i--) {
            ret.putAll(arr.get(i).getAllProperties());
        }
        return ret;
    }
}
