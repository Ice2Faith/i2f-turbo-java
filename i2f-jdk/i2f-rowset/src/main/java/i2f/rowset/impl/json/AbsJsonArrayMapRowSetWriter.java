package i2f.rowset.impl.json;

import i2f.rowset.std.IRowHeader;
import i2f.rowset.std.IRowSet;
import i2f.rowset.std.IRowSetWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/12/2 22:56
 * @desc
 */
public abstract class AbsJsonArrayMapRowSetWriter<M extends Map<String,Object>> implements IRowSetWriter<M> {
    protected boolean withHeaders = true;
    protected Charset charset = StandardCharsets.UTF_8;

    @Override
    public void write(IRowSet<M> rowSet, OutputStream os) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(os, charset);
        List<IRowHeader> headers = rowSet.getHeaders();
        if(withHeaders) {
            List<String> row=new ArrayList<>();
            for (IRowHeader header : headers) {
                String name = header.getName();
                row.add(name);
            }
            String json=toJson(row);
            writer.write(escapeSpaces(json));
            writer.write("\n");
        }
        List<Object> row=new ArrayList<>();
        while(rowSet.hasNext()) {
            M map = rowSet.next();
            if(map==null){
                continue;
            }
            row.clear();
            for (IRowHeader header : headers) {
                String name = header.getName();
                row.add(map.get(name));
            }
            String json=toJson(row);
            writer.write(escapeSpaces(json));
            writer.write("\n");
        }

        writer.flush();
    }

    public String escapeSpaces(String str){
        if(str==null){
            return null;
        }
        str=str.replace("\t","\\t");
        str=str.replace("\n","\\n");
        str=str.replace("\r","\\r");
        return str;
    }

    public abstract String toJson(Object obj);
}
