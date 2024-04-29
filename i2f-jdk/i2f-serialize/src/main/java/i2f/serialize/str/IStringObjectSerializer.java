package i2f.serialize.str;

import i2f.serialize.adapter.StringBytesSerializerAdapter;
import i2f.serialize.bytes.IBytesObjectSerializer;

public interface IStringObjectSerializer extends IStringTypeSerializer<Object> {
    default IBytesObjectSerializer asBytesSerializer() {
        return new StringBytesSerializerAdapter(this);
    }

    default IBytesObjectSerializer asBytesSerializer(String charset) {
        return new StringBytesSerializerAdapter(this, charset);
    }
}
