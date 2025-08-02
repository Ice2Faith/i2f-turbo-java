package i2f.template.render.core.impl;


import i2f.convert.obj.ObjectConvertor;
import i2f.template.render.RegexGenerator;
import i2f.template.render.core.IGenerate;
import i2f.template.render.core.ObjectFinder;
import i2f.type.tuple.impl.Tuple2;
import i2f.type.tuple.impl.Tuple3;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ice2Faith
 * @date 2021/10/20
 */
@Data
public class IfGenerate implements IGenerate {
    public Function<Object, String> mapper;
    public Object root;
    public Object data;
    public String template;
    public String test;
    public List<String> basePackages;

    @Override
    public String gen() {
        boolean isPass = test();
        if (!isPass) {
            return "";
        }

        if (template != null) {
            String tpl = template.trim();
            if ("".equals(tpl)) {
                template = null;
            }
        }
        int slen = template == null ? 64 : Math.max(template.length(), 64);

        StringBuilder builder = new StringBuilder(slen);
        if (template != null) {
            Map<String, Object> param = new HashMap<>(16);
            param.put("_item", data);
            param.put("_root", root);
            Map<String, Object> ctx = new HashMap<>(16);

            param.put("_ctx", ctx);
            String str = RegexGenerator.render(template, param, mapper, basePackages);
            builder.append(str);
        } else {
            String str = mapper.apply(data);
            builder.append(str);
        }
        return builder.toString();
    }

    private boolean test() {
        if (test == null) {
            return true;
        }
        test = test.trim();
        if ("".equals(test)) {
            return true;
        }

        List<Tuple2<String, String>> conds = new ArrayList<>(6);

        Pattern pattern = Pattern.compile("\\s+(&&|\\|\\|)\\s+");
        Matcher matcher = pattern.matcher(test);
        String lastGroup = "&&";
        int lidx = 0;
        while (matcher.find()) {
            MatchResult result = matcher.toMatchResult();
            String cond = test.substring(lidx, result.start());
            lidx = result.end();
            String group = matcher.group();
            Tuple2<String, String> pair = new Tuple2<>(lastGroup, cond);
            lastGroup = group;
            conds.add(pair);
        }
        String cond = test.substring(lidx);
        if (!"".equals(cond)) {
            Tuple2<String, String> pair = new Tuple2<>(lastGroup, cond);
            conds.add(pair);
        }

        String regex = "";
        List<Tuple2<String, Tuple3<String, String, String>>> legalConds = new ArrayList<>(conds.size());
        for (Tuple2<String, String> item : conds) {
            String cnd = item.getValue();
            cnd = cnd.trim();
            if ("".equals(cnd)) {
                continue;
            }
            if (!cnd.matches(CONDITION_REGEX)) {
                continue;
            }
            item.setV2(cnd);
            item.setV1(item.getV1().trim());
            Tuple3<String, String, String> trp = inflateCond(item.getV2());

            Tuple2<String, Tuple3<String, String, String>> pair = new Tuple2<>();
            pair.setV1(item.getV1().trim());
            pair.setV2(trp);
            legalConds.add(pair);
        }

        Map<String, Object> param = new HashMap<>(16);
        param.put("_item", data);
        param.put("_root", root);
        Map<String, Object> ctx = new HashMap<>(16);

        param.put("_ctx", ctx);
        return runCond(legalConds, param);
    }

    public static boolean runCond(List<Tuple2<String, Tuple3<String, String, String>>> conds, Object item) {
        List<Tuple2<String, Boolean>> bconds = new ArrayList<>(conds.size());
        for (Tuple2<String, Tuple3<String, String, String>> cur : conds) {
            boolean brs = getCondResult(cur.getV2(), item);
            Tuple2<String, Boolean> bcur = new Tuple2<>(cur.getV1(), brs);
            bconds.add(bcur);
        }

        boolean retVal = calcBooleanResult(bconds);

        return retVal;

    }

    public static boolean calcBooleanResult(List<Tuple2<String, Boolean>> bconds) {
        Stack<Boolean> valStack = new Stack<>();
        Stack<String> sigStack = new Stack<>();
        int i = 0;
        while (i < bconds.size()) {
            Tuple2<String, Boolean> cur = bconds.get(i);
            if (i == 0) {
                sigStack.add("=");
                valStack.add(cur.getV2());
                i++;
                continue;
            }
            String cope = cur.getV1();
            String tope = sigStack.peek();
            if (("&&".equals(cope) && "||".equals(tope))
                    || ("&&".equals(cope) && "=".equals(tope))) {
                Boolean tval = valStack.pop();
                Boolean cval = cur.getV2();
                Boolean rval = tval && cval;
                valStack.push(rval);
                i++;
                continue;
            }
            valStack.push(cur.getV2());
            sigStack.push(cur.getV1());
            i++;
        }
        boolean retVal = valStack.pop();
        while (!valStack.isEmpty()) {
            boolean sval = valStack.pop();
            retVal = retVal || sval;
        }
        return retVal;
    }

    public static boolean getCondResult(Tuple3<String, String, String> trp, Object item) {
        String ope = trp.getV2();
        Object param = item;
        Object left = parseValue(trp.getV1(), param);
        Object right = parseValue(trp.getV3(), param);


        if ("==".equals(ope)) {
            // all is null
            if (left == right) {
                return true;
            }
            //any is null
            if (left == null || right == null) {
                return false;
            }
            // all not null

            //can to number
            if (left != null && right != null) {
                Object lv = ObjectConvertor.tryConvertAsType(left, BigDecimal.class);
                Object rv = ObjectConvertor.tryConvertAsType(right, BigDecimal.class);
                if (lv instanceof BigDecimal && rv instanceof BigDecimal) {
                    return ((BigDecimal) lv).compareTo((BigDecimal) rv) == 0;
                }
            }

            // can to date
            if (left != null && right != null) {
                Object lv = ObjectConvertor.tryConvertAsType(left, Date.class);
                Object rv = ObjectConvertor.tryConvertAsType(right, Date.class);
                if (lv instanceof Date && rv instanceof Date) {
                    return ((Date) lv).equals((Date) rv);
                }
            }

            // can to string,or other obj
            return left.equals(right);
        } else if ("!=".equals(ope)) {
            // all is null
            if (left == right) {
                return false;
            }
            //any is null
            if (left == null || right == null) {
                return true;
            }
            // all not null

            //can to number
            if (left != null && right != null) {
                Object lv = ObjectConvertor.tryConvertAsType(left, BigDecimal.class);
                Object rv = ObjectConvertor.tryConvertAsType(right, BigDecimal.class);
                if (lv instanceof BigDecimal && rv instanceof BigDecimal) {
                    return ((BigDecimal) lv).compareTo((BigDecimal) rv) != 0;
                }
            }

            // can to date
            if (left != null && right != null) {
                Object lv = ObjectConvertor.tryConvertAsType(left, Date.class);
                Object rv = ObjectConvertor.tryConvertAsType(right, Date.class);
                if (lv instanceof Date && rv instanceof Date) {
                    return !((Date) lv).equals((Date) rv);
                }
            }
            // can to string,or other obj
            return !left.equals(right);
        } else if (">".equals(ope)) {
            // all is null
            if (left == right) {
                return false;
            }
            //any is null
            if (left == null || right == null) {
                return false;
            }
            // all not null

            //can to number
            if (left != null && right != null) {
                Object lv = ObjectConvertor.tryConvertAsType(left, BigDecimal.class);
                Object rv = ObjectConvertor.tryConvertAsType(right, BigDecimal.class);
                if (lv instanceof BigDecimal && rv instanceof BigDecimal) {
                    return ((BigDecimal) lv).compareTo((BigDecimal) rv) > 0;
                }
            }

            // can to date
            if (left != null && right != null) {
                Object lv = ObjectConvertor.tryConvertAsType(left, Date.class);
                Object rv = ObjectConvertor.tryConvertAsType(right, Date.class);
                if (lv instanceof Date && rv instanceof Date) {
                    return ((Date) lv).compareTo((Date) rv) > 0;
                }
            }
            // can to string,or other obj
            if (left instanceof String
                    && right instanceof String) {
                String lv = (String) left;
                String rv = (String) right;
                return lv.compareTo(rv) > 0;
            }
            if (left.getClass().equals(right.getClass())) {
                if (left instanceof Comparable) {
                    Comparable cl = (Comparable) left;
                    Comparable cr = (Comparable) right;
                    return cl.compareTo(cr) > 0;
                }
            }
            return false;
        } else if ("<".equals(ope)) {
            // all is null
            if (left == right) {
                return false;
            }
            //any is null
            if (left == null || right == null) {
                return false;
            }
            // all not null

            //can to number
            if (left != null && right != null) {
                Object lv = ObjectConvertor.tryConvertAsType(left, BigDecimal.class);
                Object rv = ObjectConvertor.tryConvertAsType(right, BigDecimal.class);
                if (lv instanceof BigDecimal && rv instanceof BigDecimal) {
                    return ((BigDecimal) lv).compareTo((BigDecimal) rv) < 0;
                }
            }

            // can to date
            if (left != null && right != null) {
                Object lv = ObjectConvertor.tryConvertAsType(left, Date.class);
                Object rv = ObjectConvertor.tryConvertAsType(right, Date.class);
                if (lv instanceof Date && rv instanceof Date) {
                    return ((Date) lv).compareTo((Date) rv) < 0;
                }
            }
            // can to string,or other obj
            if (left instanceof String
                    && right instanceof String) {
                String lv = (String) left;
                String rv = (String) right;
                return lv.compareTo(rv) < 0;
            }
            if (left.getClass().equals(right.getClass())) {
                if (left instanceof Comparable) {
                    Comparable cl = (Comparable) left;
                    Comparable cr = (Comparable) right;
                    return cl.compareTo(cr) < 0;
                }
            }
            return false;
        } else if (">=".equals(ope)) {
            // all is null
            if (left == right) {
                return true;
            }
            //any is null
            if (left == null || right == null) {
                return false;
            }
            // all not null

            //can to number
            if (left != null && right != null) {
                Object lv = ObjectConvertor.tryConvertAsType(left, BigDecimal.class);
                Object rv = ObjectConvertor.tryConvertAsType(right, BigDecimal.class);
                if (lv instanceof BigDecimal && rv instanceof BigDecimal) {
                    return ((BigDecimal) lv).compareTo((BigDecimal) rv) >= 0;
                }
            }

            // can to date
            if (left != null && right != null) {
                Object lv = ObjectConvertor.tryConvertAsType(left, Date.class);
                Object rv = ObjectConvertor.tryConvertAsType(right, Date.class);
                if (lv instanceof Date && rv instanceof Date) {
                    return ((Date) lv).compareTo((Date) rv) >= 0;
                }
            }
            // can to string,or other obj
            if (left instanceof String
                    && right instanceof String) {
                String lv = (String) left;
                String rv = (String) right;
                return lv.compareTo(rv) >= 0;
            }
            if (left.getClass().equals(right.getClass())) {
                if (left instanceof Comparable) {
                    Comparable cl = (Comparable) left;
                    Comparable cr = (Comparable) right;
                    return cl.compareTo(cr) >= 0;
                }
            }
            return false;
        } else if ("<=".equals(ope)) {
            // all is null
            if (left == right) {
                return true;
            }
            //any is null
            if (left == null || right == null) {
                return false;
            }
            // all not null

            //can to number
            if (left != null && right != null) {
                Object lv = ObjectConvertor.tryConvertAsType(left, BigDecimal.class);
                Object rv = ObjectConvertor.tryConvertAsType(right, BigDecimal.class);
                if (lv instanceof BigDecimal && rv instanceof BigDecimal) {
                    return ((BigDecimal) lv).compareTo((BigDecimal) rv) <= 0;
                }
            }

            // can to date
            if (left != null && right != null) {
                Object lv = ObjectConvertor.tryConvertAsType(left, Date.class);
                Object rv = ObjectConvertor.tryConvertAsType(right, Date.class);
                if (lv instanceof Date && rv instanceof Date) {
                    return ((Date) lv).compareTo((Date) rv) <= 0;
                }
            }
            // can to string,or other obj
            if (left instanceof String
                    && right instanceof String) {
                String lv = (String) left;
                String rv = (String) right;
                return lv.compareTo(rv) <= 0;
            }
            if (left.getClass().equals(right.getClass())) {
                if (left instanceof Comparable) {
                    Comparable cl = (Comparable) left;
                    Comparable cr = (Comparable) right;
                    return cl.compareTo(cr) <= 0;
                }
            }
            return false;
        } else if ("instanceof".equals(ope)) {
            if (left == null) {
                return false;
            }
            if (right == null) {
                return false;
            }
            if (!(right instanceof Class)) {
                return false;
            }
            Class lclazz = left.getClass();
            Class rclazz = (Class) right;
            return rclazz.isInstance(left);
        } else if ("match".equals(ope)) {
            String sleft = String.valueOf(left);
            String sright = String.valueOf(right);
            return sleft.matches(sright);
        }

        return false;
    }

    public static Object parseValue(String str, Object param) {
        if (str.matches(NUMBER_REGEX)) {
            return new BigDecimal(str);
        } else if (str.matches(RegexGenerator.SINGLE_QUOTE_STRING_REGEX)) {
            return str.substring(1, str.length() - 1);
        } else if (str.matches(RegexGenerator.OBJECT_FIELD_ROUTE_REGEX)) {
            return ObjectFinder.getObjectByDotKeyWithReference(param, str);
        }
        return null;
    }

    public static Tuple3<String, String, String> inflateCond(String cond) {
        Tuple3<String, String, String> trp = new Tuple3<>();
        Pattern pattern = Pattern.compile(OPERATOR_REGEX);
        Matcher matcher = pattern.matcher(cond);
        if (matcher.find()) {
            MatchResult result = matcher.toMatchResult();
            String group = matcher.group();
            trp.setV2(group.trim());
            trp.setV1(cond.substring(0, result.start()).trim());
            trp.setV3(cond.substring(result.end()).trim());
        }

        return trp;
    }

    public static final String NUMBER_REGEX = "[+|-]?(0|[1-9][0-9]*(.[0-9]+)?)";
    public static final String OPERATOR_REGEX = "==|\\!=|\\>=|\\<=|\\>|\\<|instanceof|match";
    public static final String CONDITION_REGEX = RegexGenerator.OBJECT_FIELD_ROUTE_REGEX + "\\s*(" + OPERATOR_REGEX + ")\\s*((" + RegexGenerator.OBJECT_FIELD_ROUTE_REGEX + ")|(" + NUMBER_REGEX + ")|(" + RegexGenerator.SINGLE_QUOTE_STRING_REGEX + "))";
}
