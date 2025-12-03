package i2f.rowset.impl.csv;

import i2f.rowset.std.IRowHeader;
import i2f.rowset.std.IRowSet;
import i2f.rowset.std.IRowSetReader;
import i2f.rowset.std.impl.SimpleRowHeader;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2025/12/3 15:18
 * @desc
 */
@Data
@NoArgsConstructor
public class CsvMapRowSetReader implements IRowSetReader<Map<String, Object>> {
    protected boolean withHeaders = true;
    protected Charset charset = StandardCharsets.UTF_8;
    protected boolean nullAsEmpty = true;
    protected boolean escapeSpaces = false;

    protected boolean tryConvertAutoType = true;
    protected String[] fmtPatterns=new String[]{
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
            "HH:mm:ss"
    };

    @Override
    public IRowSet<Map<String, Object>> read(InputStream is) throws IOException {
        CsvReader reader = new CsvReader(new InputStreamReader(is, charset));
        reader.setEscapeSpaces(escapeSpaces);
        reader.setNullAsEmpty(nullAsEmpty);
        return new IRowSet<Map<String, Object>>() {
            protected List<IRowHeader> headers;
            protected AtomicBoolean isFirst = new AtomicBoolean(true);
            protected List<String> firstRow = null;
            protected AtomicBoolean consumeFirstRow = new AtomicBoolean(false);

            public void readFirstRow() {
                if (!isFirst.getAndSet(false)) {
                    return;
                }
                if (reader.hasNext()) {
                    firstRow = reader.next();
                }
                headers = new ArrayList<>();
                if (withHeaders) {
                    if (firstRow != null) {
                        for (String name : firstRow) {
                            headers.add(new SimpleRowHeader(name));
                        }
                    }
                    consumeFirstRow.set(true);
                } else {
                    if (firstRow != null) {
                        for (int i = 0; i < firstRow.size(); i++) {
                            headers.add(new SimpleRowHeader("c" + (i + 1)));
                        }
                    }
                }
            }

            @Override
            public List<IRowHeader> getHeaders() {
                readFirstRow();
                return headers;
            }

            @Override
            public boolean hasNext() {
                readFirstRow();
                if (!consumeFirstRow.get()) {
                    return firstRow != null;
                }
                return reader.hasNext();
            }

            @Override
            public Map<String, Object> next() {
                List<String> row = null;
                readFirstRow();
                if (!consumeFirstRow.getAndSet(true)) {
                    row = firstRow;
                } else {
                    row = reader.next();
                }
                Map<String, Object> ret = new LinkedHashMap<>();
                for (int i = 0; i < row.size(); i++) {
                    String str = row.get(i);
                    Object value = tryConvertAutoType(str);
                    String key = null;
                    IRowHeader header = headers.get(i);
                    if (header != null) {
                        key = header.getName();
                    }
                    if (key == null) {
                        key = "c" + (i + 1);
                    }
                    ret.put(key, value);
                }
                return ret;
            }

            @Override
            public void close() throws IOException {
                reader.close();
            }
        };
    }

    public Object tryConvertAutoType(String str) {
        if (!tryConvertAutoType) {
            return str;
        }
        try {
            return new BigDecimal(str);
        } catch (Exception e) {

        }
        if("null".equals(str)){
            return null;
        }
        if("true".equals(str)){
            return true;
        }
        if("false".equals(str)){
            return str;
        }
        try {
            for (String pattern : fmtPatterns) {
                try {
                    SimpleDateFormat fmt = new SimpleDateFormat(pattern);
                    return fmt.parse(str);
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {

        }
        return str;
    }
}
