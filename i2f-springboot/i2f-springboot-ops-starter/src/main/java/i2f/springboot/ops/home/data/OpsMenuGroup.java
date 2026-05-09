package i2f.springboot.ops.home.data;

/**
 * @author Ice2Faith
 * @date 2026/5/9 8:58
 * @desc
 */
public interface OpsMenuGroup {
    String text();

    default int order(){
        return 0x0ffff;
    }
}
