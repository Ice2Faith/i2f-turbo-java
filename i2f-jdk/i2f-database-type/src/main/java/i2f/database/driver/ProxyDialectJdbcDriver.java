package i2f.database.driver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Ice2Faith
 * @date 2026/6/5 20:53
 * @desc 一个用来代理真实方言类型的专用数据库驱动
 * 驱动URL格式：jdbc:proxy:{方言类型}:{其他真实驱动的部分}
 * 比如，原本的Jdbc连接是：jdbc:mysql://localhost:3306/test_db?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8
 * 那么，代理连接就为：jdbc:proxy:mysql:mysql://localhost:3306/test_db?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8
 * 这是没有兼容的情况，可以这样使用
 * 那么，例如 DM 数据库的兼容模式
 * 原本的URL为: jdbc:dm://localhost:5230/
 * 那么如果是Oracle兼容模式，连接就可以为：jdbc:proxy:oracle:dm://localhost:5230/
 * 这样就表示应采用 oracle 方言
 */
public class ProxyDialectJdbcDriver implements Driver {
    public static final String URL_PREFIX = "jdbc:proxy:";
    public static final int MAJOR_VERSION = 1;
    public static final int MINOR_VERSION = 0;

    static {
        try {
            DriverManager.registerDriver(new ProxyDialectJdbcDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ProxyDialectJdbcUrlMeta parseProxyMeta(String url) {
        if (!url.startsWith(URL_PREFIX)) {
            return null;
        }
        String left = url.substring(URL_PREFIX.length());
        String[] arr = left.split(":", 2);
        if (arr.length != 2) {
            return null;
        }
        return new ProxyDialectJdbcUrlMeta(arr[0], arr[1]);
    }

    public Driver getRealDriver(String url) throws SQLException {
        ProxyDialectJdbcUrlMeta meta = parseProxyMeta(url);
        if (meta == null) {
            throw new IllegalArgumentException("invalid proxy url: " + url);
        }
        Driver driver = DriverManager.getDriver(meta.getRealJdbcUrl());
        return driver;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        ProxyDialectJdbcUrlMeta meta = parseProxyMeta(url);
        if (meta == null) {
            throw new IllegalArgumentException("invalid proxy url: " + url);
        }
        Driver driver = DriverManager.getDriver(meta.getRealJdbcUrl());
        Connection connection = driver.connect(url, info);
        InvocationHandler handler = new ProxyDialectConnectionInvocationHandler(connection, driver, meta);
        Connection ret = (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{ProxyDialectConnection.class},
                handler);
        return ret;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        ProxyDialectJdbcUrlMeta meta = parseProxyMeta(url);
        return meta != null;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        Driver driver = getRealDriver(url);
        return driver.getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @Override
    public int getMinorVersion() {
        return MINOR_VERSION;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getGlobal();
    }
}
