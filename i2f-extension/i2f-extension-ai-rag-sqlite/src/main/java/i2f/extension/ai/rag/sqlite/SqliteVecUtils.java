package i2f.extension.ai.rag.sqlite;

import i2f.bindsql.BindSql;
import i2f.io.stream.StreamUtil;
import i2f.jdbc.JdbcResolver;
import i2f.resources.ResourceUtil;
import i2f.std.consts.StdConst;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2026/7/21 10:54
 * @desc
 */
public class SqliteVecUtils {
    public static final String DIR_NAME = "sqlite-vec";

    public static float[] jdbcBytes2FloatArray(byte[] vectorBytes) {
        if (vectorBytes == null) {
            return null;
        }
        if (vectorBytes.length == 0) {
            return new float[0];
        }
        FloatBuffer buffer = ByteBuffer.wrap(vectorBytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
        float[] floatArray = new float[buffer.remaining()];
        buffer.get(floatArray);
        return floatArray;
    }

    public static String getLibName() {
        String libName = System.mapLibraryName("vec0");
        return libName;
    }

    public static String getLibFilePath() {
        String libName = getLibName();
        return StdConst.RUNTIME_PERSIST_DIR + "/" + DIR_NAME + "/" + libName;
    }

    public static void releaseLib() {
        try {
            String libFilePath = getLibFilePath();
            File saveFile = new File(libFilePath);
            saveFile = new File(saveFile.getAbsolutePath());

            File libDir = new File(saveFile.getParent());
            libDir = new File(libDir.getAbsolutePath());
            if (!libDir.exists()) {
                libDir.mkdirs();
            }

            InputStream is = ResourceUtil.getClasspathResourceAsStream("/assets/sqlite-vec/" + saveFile.getName());
            StreamUtil.writeBytes(is, saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(String dbFilePath) throws SQLException {
        // 启用拓展
        SQLiteConfig config = new SQLiteConfig();
        config.enableLoadExtension(true);

        // 使用配置初始化数据库连接
        Connection conn = JdbcResolver.getConnection("org.sqlite.JDBC", "jdbc:sqlite:" + dbFilePath, config.toProperties());

        // 加载 sqlite-vec 扩展库（注意路径和文件后缀，Windows 为 .dll，Linux 为 .so）
        String libFilePath = SqliteVecUtils.getLibFilePath();
        JdbcResolver.query(conn, BindSql.of("SELECT load_extension('" + libFilePath + "')"));

        return conn;
    }
}
