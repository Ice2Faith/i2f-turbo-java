package i2f.extension.sevenz.callbak;

import i2f.compress.data.CompressBindData;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.InputStreamSequentialInStream;
import net.sf.sevenzipjbinding.impl.OutItemFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/6/29 17:23
 * @desc
 */
@Data
@NoArgsConstructor
public class CompressSevenZCallback implements IOutCreateCallback<IOutItem7z>, ICryptoGetTextPassword {
    private String password;
    private List<CompressBindData> inputs;
    private boolean operationResult;
    private long total;
    private long completed;

    public CompressSevenZCallback(Collection<CompressBindData> inputs) {
        this.inputs = new ArrayList<>(inputs);
    }

    public CompressSevenZCallback(String password, Collection<CompressBindData> inputs) {
        this.password = password;
        this.inputs = new ArrayList<>(inputs);
    }

    @Override
    public String cryptoGetTextPassword() throws SevenZipException {
        return password;
    }

    @Override
    public void setOperationResult(boolean b) throws SevenZipException {
        this.operationResult=b;
    }

    @Override
    public IOutItem7z getItemInformation(int i, OutItemFactory<IOutItem7z> outItemFactory) throws SevenZipException {
        IOutItem7z item = outItemFactory.createOutItem();
        CompressBindData bindData = inputs.get(i);
        if (bindData.getInputStream() == null) {
            // Directory
            item.setPropertyIsDir(true);
        } else {
            // File
            if(bindData.getSize()>=0) {
                item.setDataSize(bindData.getSize());
            }
        }

        String path = bindData.getDirectory() + "/" + bindData.getFileName();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        item.setPropertyPath(path);

        return item;
    }

    @Override
    public ISequentialInStream getStream(int i) throws SevenZipException {
        InputStream is = inputs.get(i).getInputStream();
        if(is==null){
            return null;
        }
        return new InputStreamSequentialInStream(is);
    }

    @Override
    public void setTotal(long l) throws SevenZipException {
        this.total=l;
    }

    @Override
    public void setCompleted(long l) throws SevenZipException {
        this.completed=l;
    }
}
