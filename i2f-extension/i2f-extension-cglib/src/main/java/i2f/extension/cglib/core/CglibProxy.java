package i2f.extension.cglib.core;


import i2f.extension.cglib.IEnhancerSetting;
import i2f.extension.cglib.impl.CglibProxyHandlerAdapter;
import i2f.extension.cglib.impl.NoneEnhancerSetting;
import i2f.proxy.IProxyHandler;
import net.sf.cglib.proxy.Enhancer;

public class CglibProxy {
    private IEnhancerSetting setting;

    public CglibProxy() {
        this.setting = new NoneEnhancerSetting();
    }

    public CglibProxy(IEnhancerSetting setting) {
        this.setting = setting;
    }

    public IEnhancerSetting setSetting(IEnhancerSetting setting) {
        IEnhancerSetting old = this.setting;
        this.setting = setting;
        return old;
    }

    public <T> T getProxy(Class<T> srcClass, CglibProxyHandlerAdapter invoker) {
        Enhancer enhancer = new Enhancer();
        if (setting != null) {
            setting.set(enhancer);
        }
        enhancer.setUseFactory(true);
        enhancer.setSuperclass(srcClass);
        enhancer.setCallback(invoker);
        return (T) enhancer.create();
    }

    public <T> T getProxy(Class<T> srcClass, IProxyHandler handler) {
        return getProxy(srcClass, new CglibProxyHandlerAdapter(handler));
    }
}
