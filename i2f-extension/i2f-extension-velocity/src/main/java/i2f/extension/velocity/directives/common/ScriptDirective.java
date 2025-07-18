package i2f.extension.velocity.directives.common;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.DirectiveConstants;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2024/6/1 13:03
 * @desc trim 指令
 * 作用：
 * 用来内嵌运行一段脚本，脚本运行结束之后的结果作为渲染结果
 * 运行的脚本就是内部body块，这段脚本的类型是lang,使用root指定的对象作为运行脚本的根对象
 * 定义:
 * #script(root:Object, lang:String)
 * body...
 * #end
 * <p>
 * 使用案例：
 * #script($root, "ognl")
 * 1+count
 * #end
 * 在这个例子中，说明内部的脚本类型是ognl类型的脚本
 * 执行的脚本是 1+count
 * 使用$root作为运行脚本的根元素
 * 这样，脚本实际执行的就是 1+root.count
 * 请注意案例，这里的案例是ognl语言类型
 */
public class ScriptDirective extends Directive {
    public static final String NAME = "script";

    public static final CopyOnWriteArrayList<VelocityScriptProvider> PROVIDERS = new CopyOnWriteArrayList<>();

    public static final ThreadLocal<List<VelocityScriptProvider>> THREAD_PROVIDERS = new ThreadLocal<>();

    public static interface VelocityScriptProvider {
        boolean support(String lang);

        Object eval(String script, Object params);
    }

    public Object runScript(String script, String lang, Object params, InternalContextAdapter contextAdapter, Node node) {
        VelocityScriptProvider provider = null;
        provider = getVelocityScriptProvider(lang);
        if (provider != null) {
            return provider.eval(script, params);
        }
        throw new IllegalArgumentException("un-support script lang=" + lang + " !");
    }

    public static VelocityScriptProvider getVelocityScriptProvider(String lang) {
        List<VelocityScriptProvider> threadList = THREAD_PROVIDERS.get();
        if (threadList != null) {
            for (VelocityScriptProvider item : threadList) {
                if (item == null) {
                    continue;
                }
                if (item.support(lang)) {
                    return item;
                }
            }
        }
        for (VelocityScriptProvider item : PROVIDERS) {
            if (item == null) {
                continue;
            }
            if (item.support(lang)) {
                return item;
            }
        }
        ServiceLoader<VelocityScriptProvider> list = ServiceLoader.load(VelocityScriptProvider.class);
        if (list != null) {
            for (VelocityScriptProvider item : list) {
                if (item == null) {
                    continue;
                }
                if (item.support(lang)) {
                    return item;
                }
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getType() {
        return DirectiveConstants.BLOCK;
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        Node rootNode = node.jjtGetChild(0);
        Node langNode = node.jjtGetChild(1);
        Node bodyNode = node.jjtGetChild(2);

        Object rootObj = rootNode.value(context);
        Object langObj = langNode.value(context);
        String lang = (langObj == null ? null : String.valueOf(langObj));
        String script = bodyNode.literal();

        Object obj = runScript(script, lang, rootObj, context, node);
        String str = String.valueOf(obj);

        writer.write(str);
        return true;
    }
}
