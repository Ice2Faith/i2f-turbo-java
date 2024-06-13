package i2f.velocity.directives.common;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.DirectiveConstants;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @author Ice2Faith
 * @date 2024/6/1 13:03
 * @desc richFor 指令
 * 作用：
 * 用来增强for循环的
 * 支持对iterable,iterator,enumeration,map,array进行循环
 * 同时暴露$index全局变量
 * 支持嵌套，对$index,$item表里进行了堆栈保护
 * 定义:
 * #richFor($item,begin:Integer,end:Integer,step:Integer,separator:String,open:String,close:String)
 * body...
 * #end
 * 最多支持7个参数，至少需要前4个参数
 * $item 迭代的变量
 * begin 其实变量
 * end 结束变量
 * step 步长
 * separator 分隔符
 * open 开始符号
 * close 结束符号
 */
public class ForiDirective extends Directive {
    public static final String NAME = "fori";

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
        Node itemNode = node.jjtGetChild(0);
        String itemExpression = itemNode.literal();
        String itemName = itemExpression.substring(1);
        Object bakItemValue = itemNode.value(context);

        Node beginNode = node.jjtGetChild(1);
        int begin = Integer.parseInt(String.valueOf(beginNode.value(context)));

        Node endNode = node.jjtGetChild(2);
        int end = Integer.parseInt(String.valueOf(endNode.value(context)));

        Node stepNode = node.jjtGetChild(3);
        int step = Integer.parseInt(String.valueOf(stepNode.value(context)));

        boolean initState = begin < end;

        String indexName = "index";

        Object bakIndexValue = context.get(indexName);

        String separator = null;
        String open = null;
        String close = null;

        int i = 4;
        Node arg = null;
        while (i <= 7) {
            arg = node.jjtGetChild(i);
            if (arg instanceof ASTBlock) {
                // body
                break;
            } else {
                if (i == 4) {
                    Object value = arg.value(context);
                    if (value != null) {
                        separator = String.valueOf(value);
                    }
                } else if (i == 5) {
                    Object value = arg.value(context);
                    if (value != null) {
                        open = String.valueOf(value);
                    }
                } else if (i == 6) {
                    Object value = arg.value(context);
                    if (value != null) {
                        close = String.valueOf(value);
                    }
                }
            }

            i++;
        }

        StringWriter sw = new StringWriter();
        int idx = 0;
        for (int item = begin; (item < end) == initState; item += step) {
            context.put(itemName, item);
            context.put(indexName, idx);
            if (idx > 0) {
                if (separator != null) {
                    sw.write(separator);
                }
            }
            arg.render(context, sw);

            idx++;
        }


        context.put(itemName, bakItemValue);
        context.put(indexName, bakIndexValue);

        String str = sw.toString();
        if (open != null || close != null) {
            str = str.trim();
            if (!str.isEmpty()) {
                if (open != null) {
                    str = open + str;
                }
                if (close != null) {
                    str = str + close;
                }
            }
        }

        writer.write(str);
        return true;
    }
}
