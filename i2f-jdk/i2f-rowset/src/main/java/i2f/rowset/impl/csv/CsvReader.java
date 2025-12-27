package i2f.rowset.impl.csv;

import lombok.*;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ice2Faith
 * @date 2025/12/3 14:20
 * @desc
 */
@Data
@NoArgsConstructor
public class CsvReader implements Iterator<List<String>>, Closeable {
    protected boolean nullAsEmpty = true;
    protected boolean escapeSpaces = false;
    protected BufferedReader reader;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected final StringBuffer buffer = new StringBuffer();
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    protected final AtomicReference<List<String>> holder = new AtomicReference<>();

    public CsvReader(BufferedReader reader) {
        this.reader = reader;
    }

    public CsvReader(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    @Override
    public boolean hasNext() {
        try {
            if (holder.get() == null) {
                holder.set(nextRow());
            }
            return holder.get() != null;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public List<String> next() {
        try {
            if (holder.get() == null) {
                holder.set(nextRow());
            }
            List<String> ret = holder.get();
            holder.set(null);
            return ret;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public List<String> nextRow() throws IOException {
        List<String> ret = new ArrayList<>();

        if (reader == null) {
            return null;
        }

        String line = null;
        while ((line = reader.readLine()) != null) {
            buffer.append(line).append("\n");
            boolean ok = handleLine(buffer, ret);
            if (ok) {
                return ret;
            }
        }

        if (line == null) {
            try {
                if (buffer.length() > 0) {
                    for (int i = 0; i < buffer.length(); i++) {
                        if (!Character.isWhitespace(buffer.charAt(i))) {
                            throw new IOException("invalid csv file data!");
                        }
                    }
                }
            } finally {
                close();
            }
        }

        return null;
    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }

    public boolean handleLine(StringBuffer buffer, List<String> ret) {
        boolean hasPayload = false;
        for (int k = 0; k < buffer.length(); k++) {
            if (!Character.isWhitespace(buffer.charAt(k))) {
                hasPayload = true;
                break;
            }
        }
        if (!hasPayload) {
            ret.clear();
            return false;
        }
        int i = 0;
        int lastIdx = i;
        boolean enclose = false;
        while (i < buffer.length()) {
            char ch = buffer.charAt(i);
            if (ch == ',' || i == buffer.length() - 1) {
                if (enclose) {
                    i++;
                } else {
                    String str = buffer.substring(lastIdx, i);
                    ret.add(unescapeCellContent(str));
                    i++;
                    lastIdx = i;
                }
            } else if (ch == '"') {
                if (enclose) {
                    int j = i + 1;
                    if (j < buffer.length()) {
                        char nch = buffer.charAt(j);
                        if (nch == '"') {
                            i += 2;
                        } else {
                            enclose = false;
                            i++;
                        }
                    } else {
                        enclose = false;
                        i++;
                    }
                } else {
                    enclose = true;
                    lastIdx = i;
                    i++;
                }
            } else {
                i++;
            }
        }
        for (int j = lastIdx; j < buffer.length(); j++) {
            if (!Character.isWhitespace(buffer.charAt(j))) {
                ret.clear();
                return false;
            }
        }

        buffer.setLength(0);
        return true;
    }

    public String unescapeCellContent(String str) {
        if (str == null) {
            return null;
        }
        String tstr = str.trim();
        if (tstr.isEmpty()) {
            if (nullAsEmpty) {
                return null;
            }
        }

        if (tstr.startsWith("\"")) {
            tstr = tstr.substring(1, tstr.length() - 1);
            if (escapeSpaces) {
                tstr = tstr.replace("\\t", "\t");
                tstr = tstr.replace("\\r", "\r");
                tstr = tstr.replace("\\n", "\n");
            }
            return tstr;
        }

        return str;
    }

}
