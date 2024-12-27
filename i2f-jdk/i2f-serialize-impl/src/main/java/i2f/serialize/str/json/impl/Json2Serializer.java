package i2f.serialize.str.json.impl;

import i2f.serialize.std.str.json.IJsonSerializer;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2022/3/24 17:29
 * @desc
 */
public class Json2Serializer implements IJsonSerializer {

    @Override
    public Map<String, Object> bean2Map(Object obj) {
        throw new UnsupportedOperationException("Json2 un-support bean2Map.");
    }

    @Override
    public String serialize(Object obj) {
        return Json2.toJson(obj);
    }

    @Override
    public Object deserialize(String enc) {
        throw new UnsupportedOperationException("Json2 un-support parseText.");
    }

    @Override
    public Object deserialize(String enc, Class<?> clazz) {
        throw new UnsupportedOperationException("Json2 un-support parseText.");
    }

    @Override
    public Object deserialize(String enc, Object type) {
        throw new UnsupportedOperationException("Json2 un-support parseText.");
    }

}
