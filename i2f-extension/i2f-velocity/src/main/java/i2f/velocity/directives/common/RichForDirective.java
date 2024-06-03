package i2f.velocity.directives.common;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.DirectiveConstants;
import org.apache.velocity.runtime.parser.node.ASTBlock;
import org.apache.velocity.runtime.parser.node.ASTObjectArray;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.*;

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
 * #richFor($item,$collection,separator:String,open:String,close:String)
 *  body...
 * #end
 * 最多支持5个参数,至少需要前2个参数
 * $item 迭代的变量
 * $collection 要迭代的集合
 * separator 分隔符
 * open 开始符号
 * close 结束符号
 * 使用示例：
 * #richFor($item,$map,",","{","}")
 *     ${item}-$index:
 *     #richFor($item,$map,",","{","}")
 *         $item:$index:$map.get($item)
 *     #end
 * #end
 * 这里使用了双层嵌套，并且每层的变量名都相同的情况下
 * 依旧保持了正确的双层嵌套
 *
 */
public class RichForDirective extends Directive {
    public static final String NAME="richFor";
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
        String itemExpression= itemNode.literal();
        String itemName=itemExpression.substring(1);
        Object bakItemValue = itemNode.value(context);

        Node collectionNode = node.jjtGetChild(1);
        String collectionExpression = collectionNode.literal();
        Object collection=collectionNode.value(context);

        String indexName="index";

        Object bakIndexValue = context.get(indexName);

        String separator = null;
        String open=null;
        String close=null;

        int i=2;
        Node arg=null;
        while(i<=5) {
            arg = node.jjtGetChild(i);
            if (arg instanceof ASTBlock) {
                // body
                break;
            }else{
                if(i==2){
                    Object value = arg.value(context);
                    if(value!=null){
                        separator=String.valueOf(value);
                    }
                }else if(i==3){
                    Object value = arg.value(context);
                    if(value!=null){
                        open=String.valueOf(value);
                    }
                }else if(i==4){
                    Object value = arg.value(context);
                    if(value!=null){
                        close=String.valueOf(value);
                    }
                }
            }

            i++;
        }

        StringWriter sw=new StringWriter();
        if(collection==null){
            return true;
        }else if(collection instanceof Iterable){
            Iterable iterable=(Iterable)collection;
            int idx=0;
            for (Object item : iterable) {
                context.put(itemName, item);
                context.put(indexName,idx);
                if(idx>0){
                    if(separator!=null){
                        sw.write(separator);
                    }
                }
                arg.render(context, sw);

                idx++;
            }
        }else if(collection instanceof Iterator){
            Iterator iterator=(Iterator)collection;
            int idx=0;
            while(iterator.hasNext()) {
                Object item=iterator.next();
                context.put(itemName, item);
                context.put(indexName,idx);
                if(idx>0){
                    if(separator!=null){
                        sw.write(separator);
                    }
                }
                arg.render(context, sw);

                idx++;
            }
        }else if(collection instanceof Enumeration){
            Enumeration enumeration=(Enumeration)collection;
            int idx=0;
            while(enumeration.hasMoreElements()) {
                Object item=enumeration.nextElement();
                context.put(itemName, item);
                context.put(indexName,idx);
                if(idx>0){
                    if(separator!=null){
                        sw.write(separator);
                    }
                }
                arg.render(context, sw);

                idx++;
            }
        }else if(collection instanceof Map){
            Map map=(Map)collection;
            int idx=0;
            for(Object item : map.keySet()) {
                context.put(itemName, item);
                context.put(indexName,idx);
                if(idx>0){
                    if(separator!=null){
                        sw.write(separator);
                    }
                }
                arg.render(context, sw);

                idx++;
            }
        }else if(collection.getClass().isArray()){
            int len= Array.getLength(collection);
            int idx=0;
            while(idx<len) {
                Object item=Array.get(collection,idx);
                context.put(itemName, item);
                context.put(indexName,idx);
                if(idx>0){
                    if(separator!=null){
                        sw.write(separator);
                    }
                }
                arg.render(context, sw);

                idx++;
            }
        }


        context.put(itemName,bakItemValue);
        context.put(indexName,bakIndexValue);

        String str = sw.toString();
        if(open!=null || close!=null){
            str=str.trim();
            if(!str.isEmpty()){
                if(open!=null){
                    str=open+str;
                }
                if(close!=null){
                    str=str+close;
                }
            }
        }

        writer.write(str);
        return true;
    }
}
