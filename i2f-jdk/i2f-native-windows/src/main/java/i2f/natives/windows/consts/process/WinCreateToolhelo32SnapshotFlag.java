package i2f.natives.windows.consts.process;

/**
 * @author Ice2Faith
 * @date 2024/5/9 15:24
 * @desc
 */
public interface WinCreateToolhelo32SnapshotFlag {
    int TH32CS_SNAPHEAPLIST = 0x00000001;
    int TH32CS_SNAPPROCESS = 0x00000002;
    int TH32CS_SNAPTHREAD = 0x00000004;
    int TH32CS_SNAPMODULE = 0x00000008;
    int TH32CS_SNAPMODULE32 = 0x00000010;
    int TH32CS_SNAPALL = (TH32CS_SNAPHEAPLIST | TH32CS_SNAPPROCESS | TH32CS_SNAPTHREAD | TH32CS_SNAPMODULE);
    int TH32CS_INHERIT = 0x80000000;
}
