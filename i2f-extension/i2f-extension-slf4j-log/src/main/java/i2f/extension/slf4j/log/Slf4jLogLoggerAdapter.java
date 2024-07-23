package i2f.extension.slf4j.log;

import i2f.log.ILogger;
import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * @author Ice2Faith
 * @date 2024/7/23 19:05
 * @desc
 */
public class Slf4jLogLoggerAdapter implements Logger {
    private ILogger logger;

    public Slf4jLogLoggerAdapter(ILogger logger) {
        this.logger = logger;
    }

    @Override
    public String getName() {
        return logger.getLocation();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.enableTrace();
    }

    @Override
    public void trace(String s) {
        logger.trace(s);
    }

    @Override
    public void trace(String s, Object o) {
        logger.trace(s,o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        logger.trace(s,o,o1);
    }

    @Override
    public void trace(String s, Object... objects) {
        logger.trace(s,objects);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        logger.trace(throwable,s);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.enableTrace();
    }



    @Override
    public void trace(Marker marker, String s) {
        if(logger.enableTrace()) {
            logger.trace(marker+" "+s);
        }
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        if(logger.enableTrace()) {
            logger.trace(marker+" "+s,o);
        }
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        if(logger.enableTrace()) {
            logger.trace(marker+" "+s,o,o1);
        }
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        if(logger.enableTrace()) {
            logger.trace(marker+" "+s,objects);
        }
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        if(logger.enableTrace()) {
            logger.trace(throwable,marker+" "+s);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.enableDebug();
    }

    @Override
    public void debug(String s) {
        logger.debug(s);
    }

    @Override
    public void debug(String s, Object o) {
        logger.debug(s,o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        logger.debug(s,o,o1);
    }

    @Override
    public void debug(String s, Object... objects) {
        logger.debug(s,objects);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        logger.debug(throwable,s);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.enableDebug();
    }

    @Override
    public void debug(Marker marker, String s) {
        if(logger.enableDebug()) {
            logger.debug(marker+" "+s);
        }
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        if(logger.enableDebug()) {
            logger.debug(marker+" "+s,o);
        }
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        if(logger.enableDebug()) {
            logger.debug(marker+" "+s,o,o1);
        }
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        if(logger.enableDebug()) {
            logger.debug(marker+" "+s,objects);
        }
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        if(logger.enableDebug()) {
            logger.debug(throwable,marker+" "+s);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.enableInfo();
    }

    @Override
    public void info(String s) {
        logger.info(s);
    }

    @Override
    public void info(String s, Object o) {
        logger.info(s,o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        logger.info(s,o,o1);
    }

    @Override
    public void info(String s, Object... objects) {
        logger.info(s,objects);
    }

    @Override
    public void info(String s, Throwable throwable) {
        logger.info(throwable,s);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.enableInfo();
    }

    @Override
    public void info(Marker marker, String s) {
        if(logger.enableInfo()){
            logger.info(marker+" "+s);
        }
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        if(logger.enableInfo()){
            logger.info(marker+" "+s,o);
        }
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        if(logger.enableInfo()){
            logger.info(marker+" "+s,o,o1);
        }
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        if(logger.enableInfo()){
            logger.info(marker+" "+s,objects);
        }
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        if(logger.enableInfo()){
            logger.info(throwable,marker+" "+s);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.enableWarn();
    }

    @Override
    public void warn(String s) {
        logger.warn(s);
    }

    @Override
    public void warn(String s, Object o) {
        logger.warn(s,o);
    }

    @Override
    public void warn(String s, Object... objects) {
        logger.warn(s,objects);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        logger.warn(s,o,o1);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        logger.warn(throwable,s);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return logger.enableWarn();
    }

    @Override
    public void warn(Marker marker, String s) {
        if(logger.enableWarn()){
            logger.warn(marker+" "+s);
        }
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        if(logger.enableWarn()){
            logger.warn(marker+" "+s,o);
        }
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        if(logger.enableWarn()){
            logger.warn(marker+" "+s,o,o1);
        }
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        if(logger.enableWarn()){
            logger.warn(marker+" "+s,objects);
        }
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        if(logger.enableWarn()){
            logger.warn(throwable,marker+" "+s);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.enableError();
    }

    @Override
    public void error(String s) {
        logger.error(s);
    }

    @Override
    public void error(String s, Object o) {
        logger.error(s,o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        logger.error(s,o,o1);
    }

    @Override
    public void error(String s, Object... objects) {
        logger.error(s,objects);
    }

    @Override
    public void error(String s, Throwable throwable) {
        logger.error(throwable,s);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return logger.enableError();
    }

    @Override
    public void error(Marker marker, String s) {
        if(logger.enableError()){
            logger.error(marker+" "+s);
        }
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        if(logger.enableError()){
            logger.error(marker+" "+s,o);
        }
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        if(logger.enableError()){
            logger.error(marker+" "+s,o,o1);
        }
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        if(logger.enableError()){
            logger.error(marker+" "+s,objects);
        }
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        if(logger.enableError()){
            logger.error(throwable,marker+" "+s);
        }
    }
}
