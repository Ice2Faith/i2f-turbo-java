package i2f.extension.velocity.stringify.impl;

import i2f.extension.velocity.stringify.Stringifier;
import lombok.Data;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2025/4/2 8:55
 */
@Data
public class ListableStringifier implements Stringifier {
    public static final ListableStringifier INSTANCE=new ListableStringifier();
    public static final CopyOnWriteArrayList<Stringifier> STRINGFIERS=new CopyOnWriteArrayList<>();
    protected final CopyOnWriteArrayList<Stringifier> list=new CopyOnWriteArrayList<>();

    static {
        ServiceLoader<Stringifier> svcs = ServiceLoader.load(Stringifier.class);
        if(svcs!=null){
            for (Stringifier item : svcs) {
                STRINGFIERS.add(item);
            }
        }
    }
    @Override
    public boolean support(Object obj) {
        for (Stringifier item : list) {
            if(item.support(obj)){
                return true;
            }
        }
        for (Stringifier item : STRINGFIERS) {
            if(item.support(obj)){
                return true;
            }
        }
        return DefaultStringifier.INSTANCE.support(obj);
    }

    @Override
    public String stringify(Object obj) {
        for (Stringifier item : list) {
            if(item.support(obj)){
                return item.stringify(obj);
            }
        }
        for (Stringifier item : STRINGFIERS) {
            if(item.support(obj)){
                return item.stringify(obj);
            }
        }
        return DefaultStringifier.INSTANCE.stringify(obj);
    }
}
