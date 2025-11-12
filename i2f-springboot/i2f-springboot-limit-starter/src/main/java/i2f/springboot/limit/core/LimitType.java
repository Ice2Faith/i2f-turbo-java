package i2f.springboot.limit.core;

/**
 * @author Ice2Faith
 * @date 2025/11/11 17:31
 */
public enum LimitType {
    GLOBAL("global"),
    IP("ip"),
    PATH("path"),
    API("api"),
    ANT_PATH("ant-path"),
    USER("user");

    private String value;
    private LimitType(String value){
        this.value=value;
    }
    public String value(){
        return this.value;
    }
}
