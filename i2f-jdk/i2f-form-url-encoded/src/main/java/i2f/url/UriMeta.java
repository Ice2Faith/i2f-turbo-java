package i2f.url;

import i2f.text.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Ice2Faith
 * @date 2024/11/30 12:47
 */
@Data
@NoArgsConstructor
public class UriMeta {
    protected String uri;
    protected String schema;
    protected String user;
    protected String password;
    protected String host;
    protected int port = -1;
    protected String path;
    protected String query;
    protected String hash;

    public static UriMeta parse(URL url) {
        return parse(url.toString());
    }

    public static UriMeta parse(URI uri) {
        return parse(uri.toString());
    }

    public static UriMeta parse(String uri) {
        if (uri.startsWith("file:")) {
            return parseFile(uri);
        } else if (uri.startsWith("jar:file:")) {
            return parseJarFile(uri);
        } else if (uri.startsWith("git@")) {
            return parseGit(uri);
        } else if (uri.startsWith("jdbc:oracle:thin:@")) {
            return parseJdbcOracle(uri);
        } else if (uri.startsWith("jdbc:sqlserver://")) {
            return parseJdbcSqlserver(uri);
        } else if (uri.startsWith("jdbc:h2:file:")) {
            return parseJdbcH2File(uri);
        } else if (uri.startsWith("jdbc:sqlite:")) {
            return parseJdbcSqlite(uri);
        }
        return parseCommon(uri);
    }

    /**
     * schema://user:password@host:port/path?query#hash"
     * https://Ice2Faith:xxx123456@gitee.com/ice2faith/i2f-turbo-knowledge.git?from=baidu.com&step=1#/web/vue"
     *
     * @param uri
     * @return
     */
    public static UriMeta parseCommon(String uri) {

        UriMeta ret = new UriMeta();
        ret.setUri(uri);

        String str = uri;
        int idx = str.indexOf("://");
        if (idx < 0) {
            throw new IllegalArgumentException("un-recognize uri schema!");
        }

        String schema = str.substring(0, idx);
        ret.setSchema(schema + "://");

        str = str.substring(idx + "://".length());

        idx = str.indexOf("/");
        int queryIdx = str.indexOf("?");
        int hashIdx = str.indexOf("#");

        if (queryIdx >= 0) {
            if (idx >= 0) {
                idx = Math.min(idx, queryIdx);
            } else {
                idx = queryIdx;
            }
        }
        if (hashIdx >= 0) {
            if (idx >= 0) {
                idx = Math.min(idx, hashIdx);
            } else {
                idx = hashIdx;
            }
        }

        String domain = str;
        if (idx >= 0) {
            domain = str.substring(0, idx);
            str = str.substring(idx);
        } else {
            str = "";
        }

        idx = domain.indexOf("@");
        String userPass = null;
        if (idx >= 0) {
            userPass = domain.substring(0, idx);
            domain = domain.substring(idx + "@".length());
        }

        if (userPass != null) {
            String[] arr = userPass.split(":", 2);
            ret.setUser(arr[0]);
            if (arr.length > 1) {
                ret.setPassword(arr[1]);
            }
        }

        idx = domain.lastIndexOf(":");
        if (idx >= 0) {
            ret.setHost(domain.substring(0, idx));
            String port = domain.substring(idx + 1);
            ret.setPort(Integer.parseInt(port));
        } else {
            ret.setHost(domain);
            ret.setPort(-1);
        }

        queryIdx = str.indexOf("?");
        hashIdx = str.indexOf("#");
        if (queryIdx < 0 && hashIdx < 0) {
            ret.setPath(str);
            return ret;
        }

        if (queryIdx >= 0 && hashIdx >= 0) {
            idx = Math.min(queryIdx, hashIdx);
        } else if (queryIdx >= 0) {
            idx = queryIdx;
        } else if (hashIdx >= 0) {
            idx = hashIdx;
        }

        if (idx >= 0) {
            ret.setPath(str.substring(0, idx));
            str = str.substring(idx);
        }

        queryIdx = str.indexOf("?");
        hashIdx = str.indexOf("#");

        if (queryIdx >= 0 && hashIdx >= 0) {
            if (queryIdx < hashIdx) {
                ret.setQuery(str.substring(queryIdx + 1, hashIdx));
                ret.setHash(str.substring(hashIdx + 1));
            } else {
                ret.setHash(str.substring(hashIdx + 1, queryIdx));
                ret.setQuery(str.substring(queryIdx + 1));
            }
        } else if (queryIdx >= 0) {
            ret.setQuery(str.substring(queryIdx + 1));
        } else if (hashIdx >= 0) {
            ret.setHash(str.substring(hashIdx + 1));
        }

        return ret;
    }

    /**
     * file:/D:/IDEA_ROOT/DevCenter/i2f-turbo/i2f-turbo-java/./output/doc.html
     *
     * @param uri
     * @return
     */
    public static UriMeta parseFile(String uri) {
        UriMeta ret = new UriMeta();
        ret.setSchema("file:");
        ret.setPath(uri.substring("file:".length()));
        return ret;
    }

    /**
     * jar:file:/path/to/jarfile.jar!/com/user/TestUser.class
     *
     * @param uri
     * @return
     */
    public static UriMeta parseJarFile(String uri) {
        UriMeta ret = new UriMeta();
        ret.setSchema("jar:file:");
        ret.setPath(uri.substring("jar:file:".length()));
        return ret;
    }

    /**
     * git@gitee.com:ice2faith/i2f-turbo-java.git
     *
     * @param uri
     * @return
     */
    public static UriMeta parseGit(String uri) {
        UriMeta ret = new UriMeta();
        ret.setSchema("git@");
        String str = uri.substring("git@".length());
        int idx = str.indexOf(":");
        ret.setHost(str.substring(0, idx));
        str = str.substring(idx + 1);
        idx = str.indexOf("/");
        ret.setUser(str.substring(0, idx));
        ret.setPath(str.substring(idx));
        return ret;
    }

    /**
     * jdbc:oracle:thin:@localhost:1521:orcl
     *
     * @param uri
     * @return
     */
    public static UriMeta parseJdbcOracle(String uri) {
        UriMeta ret = new UriMeta();
        ret.setSchema("jdbc:oracle:thin:@");
        String str = uri.substring("jdbc:oracle:thin:@".length());
        int idx = str.lastIndexOf(":");
        ret.setPath(str.substring(idx + 1));
        str = str.substring(0, idx);
        idx = str.lastIndexOf(":");
        ret.setHost(str.substring(0, idx));
        ret.setPort(Integer.parseInt(str.substring(idx + 1)));
        return ret;
    }

    /**
     * jdbc:sqlserver://localhost:1433;databaseName=test_db
     *
     * @param uri
     * @return
     */
    public static UriMeta parseJdbcSqlserver(String uri) {
        UriMeta ret = new UriMeta();
        ret.setSchema("jdbc:sqlserver://");
        String str = uri.substring("jdbc:sqlserver://".length());
        int idx = str.indexOf(";");
        ret.setQuery(str.substring(idx + 1));
        str = str.substring(0, idx);
        idx = str.lastIndexOf(":");
        ret.setHost(str.substring(0, idx));
        ret.setPort(Integer.parseInt(str.substring(idx + 1)));
        return ret;
    }

    /**
     * jdbc:h2:file:../xxl-job-meta/h2/xxl_job.h2.db
     *
     * @param uri
     * @return
     */
    public static UriMeta parseJdbcH2File(String uri) {
        UriMeta ret = new UriMeta();
        ret.setSchema("jdbc:h2:file:");
        ret.setPath(uri.substring("jdbc:h2:file:".length()));
        return ret;
    }

    /**
     * jdbc:sqlite:../xxl-job-meta/h2/xxl_job.h2.db
     *
     * @param uri
     * @return
     */
    public static UriMeta parseJdbcSqlite(String uri) {
        UriMeta ret = new UriMeta();
        ret.setSchema("jdbc:sqlite:");
        ret.setPath(uri.substring("jdbc:sqlite:".length()));
        return ret;
    }

    public URL toURL() throws MalformedURLException {
        return toURL(false);
    }

    public URL toURL(boolean hashBefore) throws MalformedURLException {
        return new URL(toUrlString(hashBefore));
    }

    public URI toURI() throws URISyntaxException {
        return toURI(false);
    }

    public URI toURI(boolean hashBefore) throws URISyntaxException {
        return new URI(toUrlString(hashBefore));
    }

    public String toUrlString() {
        return toUrlString(false);
    }

    public String toUrlString(boolean hashBefore) {
        if (StringUtils.isEmpty(schema)) {
            return uri;
        }
        if (schema.equals("file:")) {
            return toUrlStringFile(hashBefore);
        } else if (schema.equals("jar:file:")) {
            return toUrlStringJarFile(hashBefore);
        } else if (schema.equals("git@")) {
            return toUrlStringGit(hashBefore);
        } else if (schema.equals("jdbc:oracle:thin:@")) {
            return toUrlStringJdbcOracle(hashBefore);
        } else if (schema.equals("jdbc:sqlserver://")) {
            return toUrlStringJdbcSqlserver(hashBefore);
        } else if (schema.equals("jdbc:h2:file:")) {
            return toUrlStringJdbcH2File(hashBefore);
        } else if (schema.equals("jdbc:sqlite:")) {
            return toUrlStringJdbcSqlite(hashBefore);
        }
        return toUrlStringCommon(hashBefore);
    }

    public String toUrlStringCommon(boolean hashBefore) {
        StringBuilder builder = new StringBuilder();
        builder.append(schema);
        if (!StringUtils.isEmpty(user) || !StringUtils.isEmpty(password)) {
            if (user != null) {
                builder.append(user);
            }
            if (password != null) {
                builder.append(":");
                builder.append(password);
            }
            builder.append("@");
        }
        builder.append(host);
        if (port >= 0) {
            builder.append(":");
            builder.append(port);
        }
        if (path != null) {
            builder.append(path);
        }
        if (hashBefore) {

            if (!StringUtils.isEmpty(hash)) {
                builder.append("#");
                builder.append(hash);
            }

            if (!StringUtils.isEmpty(query)) {
                builder.append("?");
                builder.append(query);
            }

        } else {
            if (!StringUtils.isEmpty(query)) {
                builder.append("?");
                builder.append(query);
            }

            if (!StringUtils.isEmpty(hash)) {
                builder.append("#");
                builder.append(hash);
            }
        }


        return builder.toString();
    }

    public String toUrlStringFile(boolean hashBefore) {
        return schema + path;
    }

    public String toUrlStringJarFile(boolean hashBefore) {
        return schema + path;
    }

    public String toUrlStringGit(boolean hashBefore) {
        return schema + host + ":" + user + path;
    }

    public String toUrlStringJdbcOracle(boolean hashBefore) {
        return schema + host + ":" + port + ":" + path;
    }

    public String toUrlStringJdbcSqlserver(boolean hashBefore) {
        return schema + host + ":" + port + ";" + query;
    }

    public String toUrlStringJdbcH2File(boolean hashBefore) {
        return schema + path;
    }

    public String toUrlStringJdbcSqlite(boolean hashBefore) {
        return schema + path;
    }
}
