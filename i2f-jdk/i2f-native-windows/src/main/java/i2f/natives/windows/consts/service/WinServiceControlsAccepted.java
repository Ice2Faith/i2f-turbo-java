package i2f.natives.windows.consts.service;

/**
 * @author Ice2Faith
 * @date 2024/5/11 14:32
 * @desc
 */
public interface WinServiceControlsAccepted {
    int SERVICE_ACCEPT_STOP = 0x00000001;
    int SERVICE_ACCEPT_PAUSE_CONTINUE = 0x00000002;
    int SERVICE_ACCEPT_SHUTDOWN = 0x00000004;
    int SERVICE_ACCEPT_PARAMCHANGE = 0x00000008;
    int SERVICE_ACCEPT_NETBINDCHANGE = 0x00000010;
    int SERVICE_ACCEPT_HARDWAREPROFILECHANGE = 0x00000020;
    int SERVICE_ACCEPT_POWEREVENT = 0x00000040;
    int SERVICE_ACCEPT_SESSIONCHANGE = 0x00000080;
    int SERVICE_ACCEPT_PRESHUTDOWN = 0x00000100;
    int SERVICE_ACCEPT_TIMECHANGE = 0x00000200;
    int SERVICE_ACCEPT_TRIGGEREVENT = 0x00000400;
}