package i2f.serialize.std.bytes;

import i2f.serialize.std.adapter.BytesStringSerializerAdapter;
import i2f.serialize.std.str.IStringObjectSerializer;

public interface IBytesObjectSerializer extends IBytesTypeSerializer<Object> {
    default IStringObjectSerializer asStringSerializer() {
        return new BytesStringSerializerAdapter(this);
    }

    default IStringObjectSerializer asStringSerializer(String charset) {
        return new BytesStringSerializerAdapter(this, charset);
    }
}
