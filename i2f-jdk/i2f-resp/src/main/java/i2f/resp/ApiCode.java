package i2f.resp;


/**
 * @author Ice2Faith
 * @date 2022/3/19 15:17
 * @desc
 */
public interface ApiCode {
    int SUCCESS = 200;
    int ERROR = 0;
    int NO_LOGIN = 401;
    int NO_AUTH = 403;
    int UNKNOWN = 402;
    int NOT_FOUND = 404;
    int SYS_EXCEPTION = 500;
}
