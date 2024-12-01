package i2f.translate.en2zh.impl;

import i2f.bql.core.bean.Bql;
import i2f.compress.impl.jdk.ZipJdkCompressor;
import i2f.io.stream.StreamUtil;
import i2f.jdbc.JdbcResolver;
import i2f.jdbc.bql.BqlTemplate;
import i2f.jdbc.context.impl.DirectJdbcInvokeContextProvider;
import i2f.lru.LruMap;
import i2f.match.regex.RegexUtil;
import i2f.resources.ResourceUtil;
import i2f.text.StringUtils;
import i2f.translate.ITranslator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/12/1 10:10
 */
public class SimpleWordTranslator implements ITranslator {

    protected Map<String, String> priorWordTranslateMap = new ConcurrentHashMap<>();
    protected static LruMap<String, String> fastCacheMap = new LruMap<>(1024 * 16);

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

    @Override
    public String translate(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        try {
            if (conn == null || conn.isClosed()) {
                this.create();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return RegexUtil.regexFindAndReplace(str, "[a-zA-Z]+('s)?", this::translateLetters);
    }

    public synchronized Connection getConnection() throws Exception {

        File dir = new File("../database");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dbFile = new File(dir, "translate_en2zh.db");

        if (!dbFile.isFile()) {

            InputStream is = ResourceUtil.getClasspathResourceAsStream("/database/translate_en2zh.zip");


            File zipFile = new File(dir, "translate_en2zh.zip");
            StreamUtil.streamCopy(is, new FileOutputStream(zipFile));

            ZipJdkCompressor compressor = new ZipJdkCompressor();
            compressor.release(zipFile, dir);

            zipFile.delete();
        }

        Connection conn = JdbcResolver.getConnection("org.sqlite.JDBC", "jdbc:sqlite:" + dbFile.getAbsolutePath());
        return conn;
    }

    public static String toForceCamel(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean isUpper = false;
        for (int i = 0; i < arr.length; i++) {
            char ch = arr[i];
            if (ch >= 'A' && ch <= 'Z') {
                if (isUpper) {
                    if (i + 1 < arr.length) {
                        if (arr[i + 1] >= 'A' && arr[i + 1] <= 'Z') {
                            arr[i] = Character.toLowerCase(arr[i]);
                        }
                    } else {
                        arr[i] = Character.toLowerCase(arr[i]);
                    }
                }
                isUpper = true;
            } else {
                isUpper = false;
            }
        }
        return new String(arr);
    }

    public String translateLetters(String str) {
        String val = priorWordTranslateMap.get(str);
        if (val != null) {
            return val;
        }
        StringBuilder ret = new StringBuilder();
        if (str.endsWith("'s")) {
            str = str.substring(0, str.length() - "'s".length());
        }
        str = toForceCamel(str);
        str = StringUtils.toUnderScore(str).toLowerCase();
        String[] arr = str.split("_|-");
        for (String item : arr) {
            val = translateSingleWord(item);
            if (val != null) {
                ret.append(val);
            } else {
                ret.append(item);
            }
        }
        return ret.toString();
    }

    public synchronized String translateSingleWord(String str) {
        String ret = priorWordTranslateMap.get(str);
        if (ret != null) {
            return ret;
        }
        ret = fastCacheMap.get(str);
        if (ret != null) {
            return ret;
        }
        List<String> list = new ArrayList<>();
        list.add(str);
        String[] suffixes = new String[]{
                "ing", "ed", "fy", "ion", "able", "or", "er", "ies", "es", "ly", "cs",
        };
        for (String suffix : suffixes) {
            if (str.endsWith(suffix)) {
                list.add(str.substring(0, str.length() - suffix.length()));
            }
        }
        for (int i = 1; i < str.length() / 2; i++) {
            list.add(str.substring(0, str.length() - i));
        }
        for (String s : list) {
            try {
                ret = template.get(Bql.$_()
                                .$("select trans_cn\n" +
                                        "from (\n")
                                .$("select distinct 200 snum,a.trans_pos,a.trans_cn,a.word\n" +
                                        "from translate_en2zh a\n")
                                .$("where word = ?\n", s)
                                .$("and book_id = 'software'\n" +
                                        "and trans_pos = 'n'\n" +
                                        "\n" +
                                        "union \n" +
                                        "\n")

                                .$("select distinct 199 snum,a.trans_pos,a.trans_cn,a.word\n" +
                                        "from translate_en2zh a\n")
                                .$("where word = ?\n", s)
                                .$("and book_id = 'software'\n" +
                                        "\n" +
                                        "union \n" +
                                        "\n")
                                .$("select distinct 100 snum,a.trans_pos,a.trans_cn,a.word\n" +
                                        "from translate_en2zh a\n")
                                .$("where word = ?\n", s)
                                .$("and book_id = 'computer'\n" +
                                        "and trans_pos = 'n'\n" +
                                        "\n" +
                                        "union \n" +
                                        "\n")

                                .$("select distinct 99 snum,a.trans_pos,a.trans_cn,a.word\n" +
                                        "from translate_en2zh a\n")
                                .$("where word = ?\n", s)
                                .$("and book_id = 'computer'\n" +
                                        "\n" +
                                        "union \n" +
                                        "\n")
                                .$("select distinct 10 snum,a.trans_pos,a.trans_cn,a.word\n" +
                                        "from translate_en2zh a\n")
                                .$("where word = ?\n", s)
                                .$("and trans_pos = 'n'\n" +
                                        "\n" +
                                        "union \n" +
                                        "\n" +
                                        "select distinct 9 snum,a.trans_pos,a.trans_cn,a.word\n" +
                                        "from translate_en2zh a\n")
                                .$("where word = ?\n", s)
                                .$("and trans_pos is not null \n" +
                                        "\n" +
                                        "union \n" +
                                        "\n" +
                                        "select distinct 8 snum,a.trans_pos,a.trans_cn,a.word\n" +
                                        "from translate_en2zh a\n")
                                .$("where word = ?\n", s)
                                .$("and trans_pos is null \n" +
                                        "\n" +
                                        "union \n" +
                                        "\n" +
                                        "select distinct 7 snum,a.trans_pos,a.trans_cn,a.word\n" +
                                        "from translate_en2zh a\n")
                                .$("where word like ?||'%'\n", s)
                                .$("and trans_pos = 'n'\n" +
                                        "\n" +
                                        "union \n" +
                                        "\n" +
                                        "select distinct 6 snum,a.trans_pos,a.trans_cn,a.word\n" +
                                        "from translate_en2zh a\n")
                                .$("where word like ?||'%'\n", s)
                                .$("and trans_pos is not null \n" +
                                        "\n" +
                                        "union \n" +
                                        "\n" +
                                        "select distinct 5 snum,a.trans_pos,a.trans_cn,a.word\n" +
                                        "from translate_en2zh a\n")
                                .$("where word like ?||'%'\n", s)
                                .$("and trans_pos is null \n" +
                                        "\n" +
                                        ") a \n" +
                                        "order by snum desc,length(word) asc,length(trans_cn) asc\n")
                                .$(" limit ?", 1)
                                .$$()
                        ,
                        String.class
                );
            } catch (SQLException e) {

            }
            if (ret != null) {
                ret = ret.replaceAll("…", "");
                ret = ret.split("[,./;:，。、；：]", 2)[0];
            }
            if (ret != null && !ret.isEmpty()) {
                break;
            }
        }
        if (ret != null) {
            fastCacheMap.put(str, ret);
        }
        return ret;
    }
}
