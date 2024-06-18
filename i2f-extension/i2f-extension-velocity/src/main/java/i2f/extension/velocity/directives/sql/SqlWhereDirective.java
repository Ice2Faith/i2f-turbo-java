package i2f.extension.velocity.directives.sql;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/6/1 13:03
 * @desc sqlWhere 指令
 * 作用：
 * 其实是trim的一种特例，主要用来针对SQL中的特例场景的
 * trimPrefixes=["and","or"]
 * trimSuffixes=["and","or"]
 * appendPrefix=" where "
 * appendSuffix=null
 * 定义:
 * #sqlWhere()
 *  body...
 * #end
 */
public class SqlWhereDirective extends Directive {
    public static final String NAME="sqlWhere";
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
        List<String> trimPrefixList=new ArrayList<>(Arrays.asList("and","or","AND","OR"));
        List<String> trimSuffixList=new ArrayList<>(Arrays.asList("and","or","AND","OR"));
        String appendPrefix=" where ";
        String appendSuffix=null;
        Node arg=node.jjtGetChild(0);

        StringWriter sw=new StringWriter();
        arg.render(context, sw);
        String str = sw.toString();


        if(!trimPrefixList.isEmpty() || !trimSuffixList.isEmpty()){
            str=str.trim();

            for (String item : trimPrefixList) {
                if(str.startsWith(item)){
                    str=str.substring(item.length());
                    break;
                }
            }

            for (String item : trimSuffixList) {
                if(str.endsWith(item)){
                    str=str.substring(0,str.length() - item.length());
                    break;
                }
            }
        }

        if(appendPrefix!=null || appendSuffix!=null){
            str=str.trim();

            boolean isEmptyBody=str.isEmpty();

            if(appendPrefix!=null && !isEmptyBody){
                str=appendPrefix+str;
            }

            if(appendSuffix!=null && !isEmptyBody){
                str=str+appendSuffix;
            }

        }


        writer.write(str);
        return true;
    }
}
