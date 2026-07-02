package i2f.serialize.str.json.impl;

import i2f.reflect.RichConverter;
import i2f.serialize.std.str.json.IJsonSerializer;
import i2f.typeof.token.TypeToken;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;

/**
 * @author Ice2Faith
 * @date 2022/3/24 17:29
 * @desc
 */
@Data
@NoArgsConstructor
public class Json2Serializer implements IJsonSerializer {
    private JsonGenerator generator = JsonGenerator.INSTANCE;
    private boolean weakMatchField = true;

    @Override
    public String serialize(Object obj) {
        return generator.toJson(obj);
    }

    @Override
    public Object deserialize(String enc) {
        return JsonParser.parse(enc);
    }

    @Override
    public Object deserialize(String enc, Class<?> clazz) {
        Object obj = JsonParser.parse(enc);
        return RichConverter.convert(obj, clazz, weakMatchField);
    }

    @Override
    public Object deserialize(String enc, Object type) {
        if (type instanceof Type) {
            Object obj = JsonParser.parse(enc);
            return RichConverter.convert2Type(obj, (Type) type, weakMatchField);
        } else if (type instanceof TypeToken) {
            Object obj = JsonParser.parse(enc);
            return RichConverter.convert(obj, (TypeToken) type, weakMatchField);
        }
        throw new UnsupportedOperationException("Json2 un-support parseText.");
    }

}
