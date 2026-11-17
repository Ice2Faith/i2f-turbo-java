package i2f.ai.std.rag.impl;

import i2f.ai.std.rag.RagFileReader;
import i2f.context.std.IContext;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/11/17 17:08
 * @desc
 */
@Data
@NoArgsConstructor
public class ContextRagFileReader implements RagFileReader {
    protected IContext context;

    public ContextRagFileReader(IContext context) {
        this.context = context;
    }

    public List<RagFileReader> readers() {
        List<RagFileReader> ret = context.getBeans(RagFileReader.class);
        return ret;
    }

    @Override
    public boolean support(File file) {
        for (RagFileReader reader : readers()) {
            if (reader.support(file)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String read(File file) throws IOException {
        for (RagFileReader reader : readers()) {
            if (reader.support(file)) {
                try {
                    String str = reader.read(file);
                    if (str != null && !str.isEmpty()) {
                        return str;
                    }
                } catch (Exception e) {

                }
            }
        }
        return null;
    }
}
