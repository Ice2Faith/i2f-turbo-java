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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/6/1 13:03
 * @desc trim 指令
 * 作用：
 * 用来移除body中出现的指定前缀trimPrefixes和后缀trimSuffixes，
 * 同时当body不为空串时，移除结束后，可以添加指定的前缀appendPrefix和后缀appendSuffix
 * 定义:
 * #trim(trimPrefixes:String|String[], trimSuffixes:String|String[],appendPrefix:String,appendSuffix:String)
 * body...
 * #end
 * 最多支持4个参数
 * trimPrefixes: 要移除的前缀，可以是单个字符串，也可以是字符串数组，将会先对body进行trim之后判断前缀，任意前缀被去除之后退出
 * trimSuffixes: 要移除的后缀，可以是单个字符串，也可以是字符串数组，将会先对body进行trim之后判断后缀，任意后缀被去除之后退出
 * appendPrefix: 要添加的前缀，只能是单个字符串，仅在内部body不为空串时添加
 * appendSuffix: 要添加的后缀，只能是单个字符串，仅在内部body不为空串时添加
 * 并且，参数是支持变长参数的
 * 完整的使用是4个参数，参数个数可以小于4个，从而构成重载
 * 所以，以下的使用都是正确的
 * #trim(trimPrefixes:String|String[], trimSuffixes:String|String[],appendPrefix:String,appendSuffix:String)
 * #trim(trimPrefixes:String|String[], trimSuffixes:String|String[],appendPrefix:String)
 * #trim(trimPrefixes:String|String[], trimSuffixes:String|String[])
 * #trim(trimPrefixes:String|String[])
 * #trim()
 * 使用案例：
 * 1. 在拼接SQL中，使用where语句时，移除内部多余的前置或者后置and,or引导符号
 * #trim(["and","or"],["and","or"]," where "," ")
 * #foreach($column in $map.keySet())
 * and a.$column = #sql($map.get($column))
 * #end
 * #end
 * 可以看到，这个配置中，移除前缀["and","or"]，移除后缀["and","or"]，并且添加前缀"where "，添加后缀" "
 * 需要特别注意，我们在添加前缀和后缀是，多添加了空格，这是为了避免因为trim操作导致的丢失SQL的空格分隔符，导致最终的SQL不正确
 * 那么，这个例子的最终结果为：
 * where a.id = 1  and a.age = 22  and a.username = zhang
 * 这样，就完成了我们的SQL的要求，接下来只要配合一些其他的处理，进行一些变换，处理字符串[zhang]的SQL转义
 * 或者使用占位符，最终使用预编译的SQL就行
 * 比如，经过下面的处理过程
 * where a.id = ${0} and a.age = ${1} and a.username = ${2}
 * 此时，可以使用一个map来记录映射关系：
 * {${2}=zhang, ${1}=12, ${3}=1, ${0}=1}
 * 那么，怎么实现这种替换呢？
 * 我们可以添加一个对象方法，比如：
 * public class ValueWrapper {
 * public Map<String,Object> map=new HashMap<>();
 * public Object wrap(Object value){
 * String key= "${"+map.size()+"}";
 * map.put(key,value);
 * return key;
 * }
 * }
 * 这个方法，就将真实的值转换为了一个唯一的key
 * 然后，每次渲染的时候，添加这个新对象到context中
 * 比如：
 * ValueWrapper wrapper=new ValueWrapper();
 * params.put("_wrap",wrapper);
 * 当我们渲染结束之后，wrapper.map中就有了真实值和映射关系
 * where a.id = ${0} and a.age = ${1} and a.username = ${2}
 * {${2}=zhang, ${1}=12, ${3}=1, ${0}=1}
 * 然后，我们使用正则匹配替换，得到顺序的绑定变量和占位符串
 * where a.id = ? and a.age = ? and a.username = ?
 * 得到变量列表：
 * [1, 12, zhang]
 * 这样，我们就可以在PrepareStatement中依次的设置变量即可完成预编译
 */
public class TrimDirective extends Directive {
    public static final String NAME = "trim";

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
        List<String> trimPrefixList = new ArrayList<>();
        List<String> trimSuffixList = new ArrayList<>();
        String appendPrefix = null;
        String appendSuffix = null;
        int i = 0;
        Node arg = null;
        while (i <= 4) {
            arg = node.jjtGetChild(i);
            if (arg instanceof ASTBlock) {
                // body
                break;
            } else if (arg instanceof ASTObjectArray) {
                Collection value = (Collection) ((ASTObjectArray) arg).value(context);
                for (Object item : value) {
                    if (item == null) {
                        continue;
                    }
                    if (i == 0) {
                        trimPrefixList.add(String.valueOf(item));
                    } else if (i == 1) {
                        trimSuffixList.add(String.valueOf(item));
                    } else if (i == 2) {
                        appendPrefix = String.valueOf(item);
                    } else if (i == 3) {
                        appendSuffix = String.valueOf(item);
                    }
                }
            } else {
                Object value = arg.value(context);
                if (value != null) {
                    if (i == 0) {
                        trimPrefixList.add(String.valueOf(value));
                    } else if (i == 1) {
                        trimSuffixList.add(String.valueOf(value));
                    } else if (i == 2) {
                        appendPrefix = String.valueOf(value);
                    } else if (i == 3) {
                        appendSuffix = String.valueOf(value);
                    }
                }
            }

            i++;
        }

        StringWriter sw = new StringWriter();
        arg.render(context, sw);
        String str = sw.toString();

        if (!trimPrefixList.isEmpty() || !trimSuffixList.isEmpty()) {
            str = str.trim();

            for (String item : trimPrefixList) {
                if (str.startsWith(item)) {
                    str = str.substring(item.length());
                    break;
                }
            }

            for (String item : trimSuffixList) {
                if (str.endsWith(item)) {
                    str = str.substring(0, str.length() - item.length());
                    break;
                }
            }
        }

        if (appendPrefix != null || appendSuffix != null) {
            str = str.trim();

            boolean isEmptyBody = str.isEmpty();

            if (appendPrefix != null && !isEmptyBody) {
                str = appendPrefix + str;
            }

            if (appendSuffix != null && !isEmptyBody) {
                str = str + appendSuffix;
            }

        }


        writer.write(str);
        return true;
    }
}
