package i2f.jdbc.procedure.node.base;

import i2f.jdbc.procedure.consts.FeatureConsts;
import i2f.jdbc.procedure.signal.SignalException;
import i2f.typeof.TypeOf;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2025/4/17 15:24
 */
public class MatchException {
    public static Map.Entry<Throwable, Boolean> matchException(Throwable e, Collection<String> features, Collection<Class<?>> catchClasses) {
        if(e==null){
            return new AbstractMap.SimpleEntry<>(e,false);
        }
        if(features==null){
            features= Collections.emptyList();
        }
        if(catchClasses==null){
            catchClasses= Collections.singletonList(Throwable.class);
        }
        if (features.contains(FeatureConsts.CAUSE_RAW)) {
            for (Class<?> catchClass : catchClasses) {
                if (TypeOf.instanceOf(e, catchClass)) {
                    return new AbstractMap.SimpleEntry<>(e, true);
                }
            }
        } else if (features.contains(FeatureConsts.CAUSE_ROOT)) {
            Throwable t = e;
            while (t != null) {
                Throwable cause = t.getCause();
                if (cause != null) {
                    t = cause;
                } else {
                    break;
                }
            }
            for (Class<?> catchClass : catchClasses) {
                if (TypeOf.instanceOf(t, catchClass)) {
                    return new AbstractMap.SimpleEntry<>(t, true);
                }
            }
        } else if (features.contains(FeatureConsts.CAUSE_FIRST)) {
            Throwable t = e;
            while (t != null) {
                for (Class<?> catchClass : catchClasses) {
                    if (TypeOf.instanceOf(t, catchClass)) {
                        return new AbstractMap.SimpleEntry<>(t, true);
                    }
                }
                Throwable cause = t.getCause();
                if (cause != null) {
                    t = cause;
                } else {
                    break;
                }
            }
            for (Class<?> catchClass : catchClasses) {
                if (TypeOf.instanceOf(t, catchClass)) {
                    return new AbstractMap.SimpleEntry<>(t, true);
                }
            }
        } else if (features.contains(FeatureConsts.CAUSE_LAST)) {
            Throwable s = null;
            Throwable t = e;
            while (t != null) {
                for (Class<?> catchClass : catchClasses) {
                    if (TypeOf.instanceOf(t, catchClass)) {
                        s = t;
                    }
                }
                Throwable cause = t.getCause();
                if (cause != null) {
                    t = cause;
                } else {
                    break;
                }
            }
            if (s != null) {
                return new AbstractMap.SimpleEntry<>(s, true);
            }
        } else {
            Throwable t=extractException(e);
            for (Class<?> catchClass : catchClasses) {
                if (TypeOf.instanceOf(t, catchClass)) {
                    return new AbstractMap.SimpleEntry<>(t, true);
                }
            }

        }
        return new AbstractMap.SimpleEntry<>(e, false);
    }

    public static Throwable extractException(Throwable e){
        Throwable t = e;
        while (t instanceof SignalException) {
            SignalException ex = (SignalException) t;
            Throwable cause = ex.getCause();
            if (cause != null) {
                t = cause;
            } else {
                break;
            }
        }
        return t;
    }
}
