package i2f.ai.std.rag.impl;

import i2f.ai.std.rag.RagFileReader;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2026/7/14 19:13
 * @desc
 */
@Data
@NoArgsConstructor
public class ListableRagFileReader implements RagFileReader {
    protected final CopyOnWriteArrayList<RagFileReader> readers = new CopyOnWriteArrayList<>();

    public ListableRagFileReader(RagFileReader... readers) {
        this.readers.addAll(Arrays.asList(readers));
    }

    public ListableRagFileReader(Collection<RagFileReader> readers) {
        this.readers.addAll(readers);
    }

    @Override
    public boolean support(File file) {
        for (RagFileReader reader : readers) {
            if (reader.support(file)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String read(File file) throws IOException {
        for (RagFileReader reader : readers) {
            if (reader.support(file)) {
                return reader.read(file);
            }
        }
        return null;
    }
}
