package i2f.extension.easyexcel.core.impl;


import i2f.extension.easyexcel.core.IDataProvider;
import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2021/10/19
 */
@Data
public abstract class AbsDataProviderAdapter implements IDataProvider {
    protected Object[] params;

    @Override
    public void preProcess() {

    }

}
