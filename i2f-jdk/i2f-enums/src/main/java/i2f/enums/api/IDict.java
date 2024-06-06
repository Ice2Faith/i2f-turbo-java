package i2f.enums.api;

/**
 * @author Ice2Faith
 * @date 2024/2/21 14:53
 * @desc
 */
public interface IDict {
    int code();

    default String key(){
        return null;
    }

    String text();

    default String remark(){
        return null;
    }
}
