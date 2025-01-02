package i2f.serialize.std.str;

import i2f.serialize.std.adapter.StringBytesSerializerAdapter;
import i2f.serialize.std.bytes.IBytesObjectSerializer;

public interface IStringObjectSerializer extends IStringTypeSerializer<Object> {
    default IBytesObjectSerializer asBytesSerializer() {
        return new StringBytesSerializerAdapter(this);
    }

    default IBytesObjectSerializer asBytesSerializer(String charset) {
        return new StringBytesSerializerAdapter(this, charset);
    }
}
