package i2f.form.dialog.std;

import i2f.form.dialog.IPreviewDialog;

import java.net.URI;
import java.net.URL;

/**
 * @author Ice2Faith
 * @date 2025/5/10 13:07
 */
public interface IUriPreviewDialog extends IPreviewDialog {
    default URI castAsUri(Object obj){
        if(obj instanceof URI){
            return (URI)obj;
        }
        try{
            URI uri=new URI(String.valueOf(obj));
            if(uri!=null){
                return uri;
            }
        }catch(Exception e){

        }
        return null;
    }
    default String getUriFileSuffix(URI uri){
        String path = uri.getPath();
        int idx = path.lastIndexOf(".");
        if (idx < 0) {
            return "";
        }
        String suffix = path.substring(idx).toLowerCase();
        return suffix;
    }
    @Override
    default boolean support(Object obj) {
        URI uri = castAsUri(obj);
        return uri!=null;
    }
}
