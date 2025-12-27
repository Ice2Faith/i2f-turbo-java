package i2f.rowset.impl.csv;

import i2f.rowset.std.IRowHeader;
import i2f.rowset.std.IRowSet;
import i2f.rowset.std.IRowSetWriter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/12/2 22:10
 * @desc
 */
@Data
@NoArgsConstructor
public class CsvMapRowSetWriter<M extends Map<String, Object>> implements IRowSetWriter<M> {
    protected boolean withHeaders = true;
    protected Charset charset = StandardCharsets.UTF_8;
    protected boolean nullAsEmpty = true;
    protected boolean escapeSpaces = false;
    protected boolean bigNumberToString = true;
    protected String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    protected String dateFormat = "yyyy-MM-dd";
    protected String timeFormat = "HH:mm:ss";
    protected static ThreadLocal<SimpleDateFormat> SDF = new ThreadLocal<>();
    protected static ThreadLocal<DateTimeFormatter> DATETIME_FORMATTER = new ThreadLocal<>();
    protected static ThreadLocal<DateTimeFormatter> DATE_FORMATTER = new ThreadLocal<>();
    protected static ThreadLocal<DateTimeFormatter> TIME_FORMATTER = new ThreadLocal<>();

    @Override
    public void write(IRowSet<M> rowSet, OutputStream os) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(os, charset);
        List<IRowHeader> headers = rowSet.getHeaders();
        if (withHeaders) {
            boolean first = true;
            for (IRowHeader header : headers) {
                String name = header.getName();
                if (!first) {
                    writer.write(",");
                }
                writer.write(toCellContent(name));
                first = false;
            }
            writer.write("\n");
        }
        while (rowSet.hasNext()) {
            M map = rowSet.next();
            if (map == null) {
                continue;
            }
            boolean first = true;
            for (IRowHeader header : headers) {
                String name = header.getName();
                if (!first) {
                    writer.write(",");
                }
                writer.write(toCellContent(map.get(name)));
                first = false;
            }
            writer.write("\n");
        }

        writer.flush();
    }

    public String toCellContent(Object value) {
        if (value == null) {
            return nullString(value);
        }
        if (value instanceof String
                || value instanceof CharSequence
                || value instanceof Appendable) {
            String str = String.valueOf(value);
            return escapeString(str);
        } else if (value instanceof Number) {
            String str = String.valueOf(value);
            if (bigNumberToString) {
                if (str.length() > 8) {
                    str = escapeString(str);
                }
            }
            return str;
        } else if (value instanceof Date) {
            SimpleDateFormat fmt = SDF.get();
            if (fmt == null) {
                fmt = new SimpleDateFormat(dateTimeFormat);
                SDF.set(fmt);
            }
            String str = fmt.format((Date) value);
            return escapeString(str);
        } else if (value instanceof LocalDateTime) {
            DateTimeFormatter fmt = DATETIME_FORMATTER.get();
            if (fmt == null) {
                fmt = DateTimeFormatter.ofPattern(dateTimeFormat);
                DATETIME_FORMATTER.set(fmt);
            }
            String str = fmt.format((LocalDateTime) value);
            return escapeString(str);
        } else if (value instanceof LocalDate) {
            DateTimeFormatter fmt = DATE_FORMATTER.get();
            if (fmt == null) {
                fmt = DateTimeFormatter.ofPattern(dateFormat);
                DATE_FORMATTER.set(fmt);
            }
            String str = fmt.format((LocalDate) value);
            return escapeString(str);
        } else if (value instanceof LocalTime) {
            DateTimeFormatter fmt = TIME_FORMATTER.get();
            if (fmt == null) {
                fmt = DateTimeFormatter.ofPattern(timeFormat);
                TIME_FORMATTER.set(fmt);
            }
            String str = fmt.format((LocalTime) value);
            return escapeString(str);
        }

        return escapeString(String.valueOf(value));
    }

    public String nullString(Object value) {
        if (nullAsEmpty) {
            return "";
        }
        return "null";
    }

    public String escapeString(String str) {
        if (str == null) {
            return nullString(str);
        }
        str = str.replace("\"", "\"\"");
        if (escapeSpaces) {
            str = str.replace("\t", "\\t");
            str = str.replace("\n", "\\n");
            str = str.replace("\r", "\\r");
        }
        return "\"" + str + "\"";
    }
}
