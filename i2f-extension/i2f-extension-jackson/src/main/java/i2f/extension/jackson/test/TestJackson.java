package i2f.extension.jackson.test;


import i2f.extension.jackson.serializer.JacksonJsonSerializer;
import i2f.extension.jackson.serializer.JacksonXmlSerializer;
import i2f.extension.jackson.serializer.JacksonYamlSerializer;
import i2f.extension.jackson.serializer.JacksonJsonWithTypeSerializer;
import i2f.serialize.str.IStringObjectSerializer;

public class TestJackson {
    public static void main(String[] args) {
        testSerializer(new JacksonJsonSerializer());
        testSerializer(new JacksonJsonWithTypeSerializer());
        testSerializer(new JacksonXmlSerializer());
        testSerializer(new JacksonYamlSerializer());
//        testSerializer(new JacksonProtobufSerializer());
//        testSerializer(new JacksonCborSerializer());
//        testSerializer(new JacksonCsvSerializer());
//        testSerializer(new JacksonSmileSerializer());
    }

    public static <T> void testSerializer(IStringObjectSerializer serializer) {
        System.out.println("==================================================");
        System.out.println("serializer=" + serializer.getClass().getSimpleName());
        System.out.println("==================================================");

        UserDto user = UserDto.makeRandom();
        String xml = serializer.serialize(user);
        System.out.println(xml);

        UserDto bean = (UserDto) serializer.deserialize(xml, UserDto.class);
        System.out.println(bean);
    }
}
