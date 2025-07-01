package i2f.extension.fastexcel.core.impl;

import i2f.extension.fastexcel.core.ExcelExportMode;
import i2f.extension.fastexcel.core.ExcelExportPage;
import i2f.extension.fastexcel.core.IDataProvider;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2022/2/19 8:38
 * @desc
 * @type ARGUMENT 是进行查询的泛型参数
 * @type SERVICE 调用获取数据的Service类型
 * @type DTO 需要返回的数据列表中Bean的类型
 * 使用时必须实现抽象方法 doRequestData，一般来说，在抽象方法中你需要做的事如下
 * List<DTO> data=SERVICE.queryPage(ARGUMENT,page,args);
 */
public abstract class ServiceDataProviderAdapter<ARGUMENT, SERVICE, RETURN_TYPE> implements IDataProvider {
    protected Object[] params;
    private Class<RETURN_TYPE> returnBeanClass;
    private ExcelExportMode mode = ExcelExportMode.ALL;
    private ARGUMENT reqVo;
    private SERVICE service;


    /**
     * 构造简单的Service数据提供者
     *
     * @param returnBeanClass doRequestData方法返回值的Bean的类型
     * @param reqVo           请求的数据对象
     * @param service         提供数据获取能力的Service对象
     * @param args            service可能需要的其他参数或运行需要的其他参数
     */
    public ServiceDataProviderAdapter(ExcelExportMode mode,
                                      SERVICE service,
                                      ARGUMENT reqVo,
                                      Class<RETURN_TYPE> returnBeanClass,
                                      Object... args) {
        this.params = args;
        this.mode = mode;
        this.service = service;
        this.reqVo = reqVo;
        this.returnBeanClass = returnBeanClass;
    }

    @Override
    public boolean supportPage() {
        if (this.mode == null) {
            return false;
        }
        if (ExcelExportMode.ALL == this.mode) {
            return true;
        } else if (ExcelExportMode.PAGE == this.mode) {
            return false;
        }
        return false;
    }

    @Override
    public List<?> getData(ExcelExportPage page) {
        List<RETURN_TYPE> data = doRequestData(service, page, reqVo, params);
        return data;
    }

    @Override
    public Class<?> getDataClass() {
        return returnBeanClass;
    }

    public abstract List<RETURN_TYPE> doRequestData(SERVICE service, ExcelExportPage page, ARGUMENT reqVo, Object... args);
}
