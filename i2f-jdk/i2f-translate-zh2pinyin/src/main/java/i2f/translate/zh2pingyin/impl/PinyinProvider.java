package i2f.translate.zh2pingyin.impl;

import i2f.bql.core.bean.Bql;
import i2f.compress.impl.jdk.ZipJdkCompressor;
import i2f.io.stream.StreamUtil;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.bql.BqlTemplate;
import i2f.jdbc.context.impl.DirectJdbcInvokeContextProvider;
import i2f.lifecycle.ILifeCycle;
import i2f.lru.LruMap;
import i2f.resources.ResourceUtil;
import i2f.text.StringUtils;
import i2f.translate.zh2pingyin.data.Zh2PinyinVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Ice2Faith
 * @date 2024/12/1 10:10
 */
@Data
@NoArgsConstructor
public class PinyinProvider implements ILifeCycle {

    public static final PinyinProvider PROVIDER;

    static {
        PROVIDER = new PinyinProvider();
        PROVIDER.create();
    }

    protected static LruMap<String, Zh2PinyinVo> fastCacheMap = new LruMap<>(1024 * 8);

    protected volatile Connection conn;
    protected volatile BqlTemplate template;

    @Override
    public synchronized void create() {
        try {
            if (conn != null) {
                conn.close();
            }
            conn = getConnection();
            template = new BqlTemplate(new DirectJdbcInvokeContextProvider(conn));
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public synchronized void destroy() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
            conn = null;
            template = null;
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public synchronized Connection getConnection() throws Exception {

        File dir = new File("../database");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dbFile = new File(dir, "translate_zh2pinyin.db");

        if (!dbFile.isFile()) {

            InputStream is = ResourceUtil.getClasspathResourceAsStream("/assets/database/translate_zh2pinyin.zip");


            File zipFile = new File(dir, "translate_zh2pinyin.zip");
            StreamUtil.streamCopy(is, new FileOutputStream(zipFile));

            ZipJdkCompressor compressor = new ZipJdkCompressor();
            compressor.release(zipFile, dir);

            zipFile.delete();
        }

        Connection conn = JdbcResolver.getConnection("org.sqlite.JDBC", "jdbc:sqlite:" + dbFile.getAbsolutePath());
        return conn;
    }

    public synchronized Zh2PinyinVo getWordInfo(String str) {
        Zh2PinyinVo ret = fastCacheMap.get(str);
        if (ret != null) {
            return ret;
        }
        try {
            ret = template.find(Bql.$_()
                            .$("select id,word,old_word,stroke_num,pin_yin,radicals \n" +
                                    "from translate_zh2pinyin \n")
                            .$("where word =?\n", str)
                            .$("or old_word =?", str)
                            .$(" limit ?", 1)
                            .$$()
                    ,
                    Zh2PinyinVo.class, e -> StringUtils.toCamel(e.toLowerCase())
            );
        } catch (SQLException e) {

        }
        if (ret != null) {
            fastCacheMap.put(str, ret);
        }
        return ret;
    }
}
