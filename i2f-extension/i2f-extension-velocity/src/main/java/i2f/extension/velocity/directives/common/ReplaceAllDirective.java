package i2f.extension.velocity.directives.common;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.DirectiveConstants;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author Ice2Faith
 * @date 2024/6/1 13:03
 * @desc replaceAll 指令
 * 作用：
 * 用来实现替换块内的全部内容
 * 实际就是String.replaceAll(regex,replace)的封装
 */
public class ReplaceAllDirective extends Directive {
    public static final String NAME = "replaceAll";

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
        String regex = (String) node.jjtGetChild(0).value(context);
        String replace = (String) node.jjtGetChild(1).value(context);
        Node arg = node.jjtGetChild(2);

        StringWriter sw = new StringWriter();
        arg.render(context, sw);
        String str = sw.toString();

        str = str.replaceAll(regex, replace);

        writer.write(str);
        return true;
    }
}
