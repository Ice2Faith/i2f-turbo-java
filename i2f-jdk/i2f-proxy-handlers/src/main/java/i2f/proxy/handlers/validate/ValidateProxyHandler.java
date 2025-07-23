package i2f.proxy.handlers.validate;

import i2f.annotations.core.base.NotEmpty;
import i2f.annotations.core.base.NotNull;
import i2f.annotations.core.base.Tag;
import i2f.annotations.core.base.Types;
import i2f.annotations.core.check.*;
import i2f.annotations.core.value.In;
import i2f.annotations.core.value.Max;
import i2f.annotations.core.value.Min;
import i2f.annotations.core.value.Range;
import i2f.comparator.impl.DefaultComparator;
import i2f.convert.obj.ObjectConvertor;
import i2f.invokable.IInvokable;
import i2f.invokable.method.impl.jdk.JdkMethod;
import i2f.proxy.std.IProxyInvocationHandler;
import i2f.reflect.ReflectResolver;
import i2f.typeof.TypeOf;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2025/7/23 16:55
 */
public class ValidateProxyHandler implements IProxyInvocationHandler {
    @Override
    public Object invoke(Object ivkObj, IInvokable invokable, Object... args) throws Throwable {
        if (!(invokable instanceof JdkMethod)) {
            throw new IllegalStateException("un-support invokable type=" + invokable.getClass());
        }

        JdkMethod jdkMethod = (JdkMethod) invokable;
        Method method = jdkMethod.getMethod();

        Set<String> tags = getValidateTags(method);

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            args[i] = validate(false, parameters[i], "arg" + i, args[i], parameters[i].getType(), tags);
        }

        Object ret = invokable.invoke(ivkObj, args);

        ret = validate(false, method, "return", ret, method.getReturnType(), tags);

        return ret;
    }

    public Set<String> getValidateTags(AnnotatedElement elem) {
        Validate ann = elem.getDeclaredAnnotation(Validate.class);
        Set<String> tags = new HashSet<>();
        if (ann != null) {
            tags.addAll(Arrays.asList(ann.tags()));
        }
        return tags;
    }

    public Set<String> getQualifierTags(AnnotatedElement elem) {
        Tag ann = elem.getDeclaredAnnotation(Tag.class);
        Set<String> tags = new HashSet<>();
        if (ann != null) {
            tags.addAll(Arrays.asList(ann.value()));
        }
        return tags;
    }

    public Object validate(boolean checkNeed, AnnotatedElement elem, String elemName, Object value, Class<?> elemType, Set<String> tags) {
        if (checkNeed && !needValidate(elem, tags)) {
            return value;
        }

        validateJsrStd(checkNeed, elem, elemName, value, elemType, tags);

        validateCustom(checkNeed, elem, elemName, value, elemType, tags);

        if (value != null) {
            if (value instanceof Iterable) {
                Iterable<?> col = (Iterable<?>) value;
                int i = 0;
                for (Object item : col) {
                    if (item == null) {
                        i++;
                        continue;
                    }
                    Class<?> clazz = item.getClass();
                    validate(true, clazz, elemName + "[" + i + "]", item, clazz, tags);
                    i++;
                }
            } else if (value instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) value;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    Object key = entry.getKey();
                    if (key != null) {
                        Class<?> clazz = key.getClass();
                        validate(true, clazz, elemName + "#" + key + "", key, clazz, tags);
                    }
                    Object mv = entry.getValue();
                    if (mv != null) {
                        Class<?> clazz = mv.getClass();
                        validate(true, clazz, elemName + "[" + key + "]", mv, clazz, tags);
                    }
                }
            } else if (value.getClass().isArray()) {
                int len = Array.getLength(value);
                for (int i = 0; i < len; i++) {
                    Object item = Array.get(value, i);
                    if (item == null) {
                        continue;
                    }
                    Class<?> clazz = item.getClass();
                    validate(true, clazz, elemName + "[" + i + "]", item, clazz, tags);
                }
            } else if (TypeOf.isBaseType(value.getClass())) {
                // do nothing
            } else {
                // custom object type
                Map<Field, Class<?>> fields = ReflectResolver.getFields(value.getClass());
                for (Field field : fields.keySet()) {
                    Object fv = null;
                    try {
                        fv = ReflectResolver.valueGet(value, field);
                    } catch (Exception e) {
                        continue;
                    }
                    validate(true, field, elemName + "." + field.getName(), fv, field.getType(), tags);
                }
            }
        }

        return value;
    }

    public void validateJsrStd(boolean checkNeed, AnnotatedElement elem, String elemName, Object value, Class<?> elemType, Set<String> tags) {
        if (checkNeed && !needValidate(elem, tags)) {
            return;
        }


    }

    public void validateCustom(boolean checkNeed, AnnotatedElement elem, String elemName, Object value, Class<?> elemType, Set<String> tags) {
        if (checkNeed && !needValidate(elem, tags)) {
            return;
        }
        if (true) {
            NotNull ann = elem.getDeclaredAnnotation(NotNull.class);
            if (ann != null) {
                if (value == null) {
                    throw exception(ann.message(), elemName + " validate " + NotNull.class.getSimpleName() + "!", null);
                }
            }
        }
        if (true) {
            NotEmpty ann = elem.getDeclaredAnnotation(NotEmpty.class);
            if (ann != null) {
                if (isEmpty(value)) {
                    throw exception(ann.message(), elemName + " validate " + NotEmpty.class.getSimpleName() + "!", null);
                }
            }
        }
        if (true) {
            Types ann = elem.getDeclaredAnnotation(Types.class);
            if (ann != null) {
                if (!isInstanceOfAny(value, ann.value())) {
                    throw exception(ann.message(), elemName + " validate " + Types.class.getSimpleName() + "!", null);
                }
            }
        }

        if (true) {
            Set<Match> list = new LinkedHashSet<>();
            Match ann = elem.getDeclaredAnnotation(Match.class);
            if (ann != null) {
                list.add(ann);
            }
            Match[] arr = elem.getDeclaredAnnotationsByType(Match.class);
            if (arr != null) {
                list.addAll(Arrays.asList(arr));
            }
            boolean and = true;
            Matches annr = elem.getDeclaredAnnotation(Matches.class);
            if (annr != null) {
                list.addAll(Arrays.asList(annr.value()));
                and = annr.and();
            }

            if (!list.isEmpty()) {
                String text = String.valueOf(value);
                boolean ok = false;
                Match last = null;
                for (Match item : list) {
                    last = item;
                    if (value == null) {
                        throw exception(item.message(), elemName + " validate " + Match.class.getSimpleName() + "!", null);
                    }
                    String regex = item.value();
                    if (!regex.isEmpty()) {
                        if (!text.matches(regex)) {
                            if (and) {
                                throw exception(item.message(), elemName + " validate " + Match.class.getSimpleName() + "!", null);
                            }
                        } else {
                            ok = true;
                        }
                    }
                }
                if (!ok) {
                    throw exception(last.message(), elemName + " validate " + Match.class.getSimpleName() + "!", null);
                }
            }
        }

        if (true) {
            Set<NotMatch> list = new LinkedHashSet<>();
            NotMatch ann = elem.getDeclaredAnnotation(NotMatch.class);
            if (ann != null) {
                list.add(ann);
            }
            NotMatch[] arr = elem.getDeclaredAnnotationsByType(NotMatch.class);
            if (arr != null) {
                list.addAll(Arrays.asList(arr));
            }
            boolean and = true;
            NotMatches annr = elem.getDeclaredAnnotation(NotMatches.class);
            if (annr != null) {
                list.addAll(Arrays.asList(annr.value()));
                and = annr.and();
            }

            if (!list.isEmpty()) {
                String text = String.valueOf(value);
                boolean ok = false;
                NotMatch last = null;
                for (NotMatch item : list) {
                    last = item;
                    if (value == null) {
                        throw exception(item.message(), elemName + " validate " + NotMatch.class.getSimpleName() + "!", null);
                    }
                    String regex = item.value();
                    if (!regex.isEmpty()) {
                        if (text.matches(regex)) {
                            if (and) {
                                throw exception(item.message(), elemName + " validate " + NotMatch.class.getSimpleName() + "!", null);
                            }
                        } else {
                            ok = true;
                        }
                    }
                }
                if (!ok) {
                    throw exception(last.message(), elemName + " validate " + NotMatch.class.getSimpleName() + "!", null);
                }
            }
        }

        if (true) {
            In ann = elem.getDeclaredAnnotation(In.class);
            if (ann != null && ann.value().length != 0) {
                boolean ok = false;
                for (String str : ann.value()) {
                    Object obj = ObjectConvertor.tryConvertAsType(str, elemType);
                    if (DefaultComparator.compareDefault(obj, value) == 0) {
                        ok = true;
                        break;
                    }
                }

                if (!ok) {
                    throw exception(ann.message(), elemName + " validate " + In.class.getSimpleName() + "!", null);
                }
            }
        }

        if (true) {
            NotIn ann = elem.getDeclaredAnnotation(NotIn.class);
            if (ann != null && ann.value().length != 0) {
                for (String str : ann.value()) {
                    Object obj = ObjectConvertor.tryConvertAsType(str, elemType);
                    if (DefaultComparator.compareDefault(obj, value) == 0) {
                        throw exception(ann.message(), elemName + " validate " + NotIn.class.getSimpleName() + "!", null);
                    }
                }

            }
        }

        if (true) {
            Range ann = elem.getDeclaredAnnotation(Range.class);
            if (ann != null) {
                if (!isEmpty(ann.min())) {
                    Object min = ObjectConvertor.tryConvertAsType(ann.min(), elemType);
                    if (DefaultComparator.compareDefault(value, min) < 0) {
                        throw exception(ann.message(), elemName + " validate " + Range.class.getSimpleName() + "!", null);
                    }
                }

                if (!isEmpty(ann.max())) {
                    Object max = ObjectConvertor.tryConvertAsType(ann.max(), elemType);
                    if (DefaultComparator.compareDefault(value, max) > 0) {
                        throw exception(ann.message(), elemName + " validate " + Range.class.getSimpleName() + "!", null);
                    }
                }
            }
        }

        if (true) {
            NotRange ann = elem.getDeclaredAnnotation(NotRange.class);
            if (ann != null) {
                boolean ok = false;
                if (!isEmpty(ann.min())) {
                    Object min = ObjectConvertor.tryConvertAsType(ann.min(), elemType);
                    if (DefaultComparator.compareDefault(value, min) < 0) {
                        ok = true;
                    }
                }

                if (!isEmpty(ann.max())) {
                    Object max = ObjectConvertor.tryConvertAsType(ann.max(), elemType);
                    if (DefaultComparator.compareDefault(value, max) > 0) {
                        ok = true;
                    }
                }

                if (!ok) {
                    throw exception(ann.message(), elemName + " validate " + NotRange.class.getSimpleName() + "!", null);
                }
            }
        }

        if (true) {
            Size ann = elem.getDeclaredAnnotation(Size.class);
            if (ann != null) {
                Long min = null;
                Long max = null;
                if (!isEmpty(ann.min())) {
                    min = (Long) ObjectConvertor.tryConvertAsType(ann.min(), Long.class);
                }

                if (!isEmpty(ann.max())) {
                    max = (Long) ObjectConvertor.tryConvertAsType(ann.max(), Long.class);
                }

                long size = -1;
                if (value instanceof Collection) {
                    Collection<?> col = (Collection<?>) value;
                    size = col.size();
                } else if (value instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) value;
                    size = map.size();
                } else if (value instanceof String) {
                    String str = (String) value;
                    size = str.length();
                } else if (value instanceof StringBuilder) {
                    StringBuilder builder = (StringBuilder) value;
                    size = builder.length();
                } else if (value instanceof StringBuffer) {
                    StringBuffer buffer = (StringBuffer) value;
                    size = buffer.length();
                } else if (value instanceof CharSequence) {
                    CharSequence cs = (CharSequence) value;
                    size = cs.length();
                } else if (value != null && value.getClass().isArray()) {
                    size = Array.getLength(value);
                }

                boolean ok = true;
                if (size >= 0) {
                    if (min != null && size < min) {
                        ok = false;
                    }
                    if (max != null && size > max) {
                        ok = false;
                    }
                }

                if (!ok) {
                    throw exception(ann.message(), elemName + " validate " + Size.class.getSimpleName() + "!", null);
                }
            }
        }

        if (true) {
            NotSize ann = elem.getDeclaredAnnotation(NotSize.class);
            if (ann != null) {
                Long min = null;
                Long max = null;
                if (!isEmpty(ann.min())) {
                    min = (Long) ObjectConvertor.tryConvertAsType(ann.min(), Long.class);
                }

                if (!isEmpty(ann.max())) {
                    max = (Long) ObjectConvertor.tryConvertAsType(ann.max(), Long.class);
                }

                long size = -1;
                if (value instanceof Collection) {
                    Collection<?> col = (Collection<?>) value;
                    size = col.size();
                } else if (value instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) value;
                    size = map.size();
                } else if (value instanceof String) {
                    String str = (String) value;
                    size = str.length();
                } else if (value instanceof StringBuilder) {
                    StringBuilder builder = (StringBuilder) value;
                    size = builder.length();
                } else if (value instanceof StringBuffer) {
                    StringBuffer buffer = (StringBuffer) value;
                    size = buffer.length();
                } else if (value instanceof CharSequence) {
                    CharSequence cs = (CharSequence) value;
                    size = cs.length();
                } else if (value != null && value.getClass().isArray()) {
                    size = Array.getLength(value);
                }

                if (size >= 0) {
                    if (min != null && max != null) {
                        if ((size < min) && (size <= max)) {
                            throw exception(ann.message(), elemName + " validate " + NotSize.class.getSimpleName() + "!", null);
                        }
                    } else if (min != null && size >= min) {
                        throw exception(ann.message(), elemName + " validate " + NotSize.class.getSimpleName() + "!", null);
                    } else if (max != null && size <= max) {
                        throw exception(ann.message(), elemName + " validate " + NotSize.class.getSimpleName() + "!", null);
                    }
                }

            }
        }


        if (true) {
            Max ann = elem.getDeclaredAnnotation(Max.class);
            if (ann != null) {
                if (!isEmpty(ann.value())) {
                    Object max = ObjectConvertor.tryConvertAsType(ann.value(), elemType);
                    if (DefaultComparator.compareDefault(value, max) > 0) {
                        throw exception(ann.message(), elemName + " validate " + Max.class.getSimpleName() + "!", null);
                    }
                }

            }
        }

        if (true) {
            Min ann = elem.getDeclaredAnnotation(Min.class);
            if (ann != null) {
                if (!isEmpty(ann.value())) {
                    Object min = ObjectConvertor.tryConvertAsType(ann.value(), elemType);
                    if (DefaultComparator.compareDefault(value, min) < 0) {
                        throw exception(ann.message(), elemName + " validate " + Max.class.getSimpleName() + "!", null);
                    }
                }

            }
        }
    }


    public boolean isInstanceOfAny(Object obj, Class<?>... types) {
        if (obj == null) {
            return true;
        }
        Class<?> clazz = obj.getClass();
        for (Class<?> item : types) {
            if (item.equals(clazz)
                    || item.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    public boolean needValidate(AnnotatedElement elem, Set<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return true;
        }
        Set<String> qualifierTags = getQualifierTags(elem);
        if (qualifierTags == null || qualifierTags.isEmpty()) {
            return false;
        }
        for (String item : qualifierTags) {
            if (tags.contains(item)) {
                return true;
            }
        }
        return false;
    }

    public ValidateException exception(String message, String defaultMessage, Throwable e) {
        if (e == null) {
            if (!isEmpty(message)) {
                return new ValidateException(message);
            } else if (!isEmpty(defaultMessage)) {
                return new ValidateException(defaultMessage);
            } else {
                return new ValidateException();
            }
        } else {
            if (!isEmpty(message)) {
                return new ValidateException(message, e);
            } else if (!isEmpty(defaultMessage)) {
                return new ValidateException(defaultMessage, e);
            } else {
                return new ValidateException(e);
            }
        }
    }


    public boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            String str = (String) obj;
            return str.isEmpty();
        }
        if (obj instanceof StringBuilder) {
            StringBuilder builder = (StringBuilder) obj;
            return builder.length() == 0;
        }
        if (obj instanceof StringBuffer) {
            StringBuffer buffer = (StringBuffer) obj;
            return buffer.length() == 0;
        }
        if (obj instanceof Collection) {
            Collection<?> col = (Collection<?>) obj;
            return col.isEmpty();
        }
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            return map.isEmpty();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        return false;
    }

}
