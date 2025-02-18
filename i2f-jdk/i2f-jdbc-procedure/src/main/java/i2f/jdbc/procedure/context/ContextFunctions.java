package i2f.jdbc.procedure.context;

import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2025/2/17 16:09
 */
public class ContextFunctions {
    public static String replace(String str,String target,String replacement){
        if(str==null){
            return str;
        }
        return str.replace(target,replacement);
    }
    public static String trim(String str){
        if(str==null){
            return str;
        }
        return str.trim();
    }
    public static String upper(String str){
        if(str==null){
            return str;
        }
        return str.toUpperCase();
    }
    public static String lower(String str){
        if(str==null){
            return str;
        }
        return str.toLowerCase();
    }
    public static String chr(int ascii){
        char ch=(char) ascii;
        return ""+ch;
    }
    public static String rtrim(String str,String substr){
        if(str==null){
            return str;
        }
        if(substr==null){
            int idx=str.length()-1;
            while(idx>=0){
                char ch = str.charAt(idx);
                if(!Character.isWhitespace(ch)){
                    break;
                }
                idx--;
            }
            if(idx>=0){
                return str.substring(0,idx+1);
            }
            return str;
        }else{
            if(substr.isEmpty()){
                return str;
            }
            while(str.endsWith(substr)){
                str=str.substring(0,str.length()-substr.length());
            }
            return str;
        }
    }

    public static String ltrim(String str,String substr){
        if(str==null){
            return str;
        }
        if(substr==null){
            int idx=0;
            while(idx<str.length()){
                char ch = str.charAt(idx);
                if(!Character.isWhitespace(ch)){
                    break;
                }
                idx++;
            }
            if(idx>=0){
                return str.substring(idx);
            }
            return str;
        }else{
            if(substr.isEmpty()){
                return str;
            }
            while(str.startsWith(substr)){
                str=str.substring(substr.length());
            }
            return str;
        }
    }

    public static Object nvl(Object v1,Object v2){
        return v1!=null?v1:v2;
    }

    public static Object ifnull(Object v1,Object v2){
        return nvl(v1,v2);
    }

    public static Object decode(Object target, Object... args) {
        int i = 0;
        while (i + 1 < args.length) {
            if (Objects.equals(target, args[i])) {
                return args[i + 1];
            }
            i += 2;
        }
        if (args.length % 2 != 0) {
            return args[args.length - 1];
        }
        return target;
    }

}
