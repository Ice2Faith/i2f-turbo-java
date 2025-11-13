package i2f.convert.tree;


import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2022/3/19 15:22
 * @desc
 */
public class TreeConvertor {
    /**
     * 适用于数据不能一次性获取，需要多次获取，典型的就是获取一棵树中的某棵子树
     *
     * @param list
     * @param provider
     * @param <T>
     * @return
     */
    public static <T extends ITreeNode<T>> List<T> list2Tree(List<T> list, INextLevelDataProvider<T> provider) {
        List<T> flat = list2TreeFlat(list, provider);
        return list2Tree(flat);
    }

    public static <T> List<T> list2Tree(List<T> list, INextLevelDataProvider<T> provider,
                                        BiPredicate<T, T> secondIsFirstParentPredicate,
                                        BiPredicate<T, T> secondIsFirstChildPredicate,
                                        BiConsumer<T, T> secondAsFirstChildConsumer) {
        List<T> flat = list2TreeFlat(list, provider);
        return list2Tree(flat,
                secondIsFirstParentPredicate,
                secondIsFirstChildPredicate,
                secondAsFirstChildConsumer);
    }

    public static <T> List<T> list2TreeFlat(List<T> list, INextLevelDataProvider<T> provider) {
        List<T> ret = new LinkedList<>();
        if (list == null || list.isEmpty()) {
            return ret;
        }
        ret.addAll(list);
        List<T> next = provider.getNextLevel(list);
        if (next != null && !next.isEmpty()) {
            ret.addAll(next);
            list2TreeFlat(next, provider);
        }
        return ret;
    }

    public static <T> List<T> list2tree(List<T> list,String keyFieldName,String parentKeyFieldName,String childrenFieldName){
        AtomicReference<Field> keyField=new AtomicReference<>();
        AtomicReference<Field> parentKeyField=new AtomicReference<>();
        AtomicReference<Field> childrenField=new AtomicReference<>();
        return list2tree(list,(t)->{
            try {
                if(t instanceof Map){
                    Map map = (Map) t;
                    return map.get(keyFieldName);
                }
                Field kf=keyField.get();
                if(kf==null) {
                    Class<?> clazz = t.getClass();
                    kf = getField(clazz,keyFieldName);
                    kf.setAccessible(true);
                    keyField.set(kf);
                }
                return kf.get(t);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(),e);
            }
        },(t)->{
            try {
                if(t instanceof Map){
                    Map map = (Map) t;
                    return map.get(parentKeyFieldName);
                }
                Field kf=parentKeyField.get();
                if(kf==null) {
                    Class<?> clazz = t.getClass();
                    kf = getField(clazz,parentKeyFieldName);
                    kf.setAccessible(true);
                    parentKeyField.set(kf);
                }
                return kf.get(t);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(),e);
            }
        },(t,item)->{
            try {
                if(t instanceof Map){
                    Map map = (Map) t;
                    Collection col = (Collection) map.get(childrenFieldName);
                    if(col==null){
                        col=new ArrayList();
                        map.put(childrenFieldName,col);
                    }
                    col.add(item);
                    return;
                }
                Field kf=childrenField.get();
                if(kf==null) {
                    Class<?> clazz = t.getClass();
                    kf = getField(clazz,childrenFieldName);
                    kf.setAccessible(true);
                    childrenField.set(kf);
                }
                Collection col = (Collection)kf.get(t);
                if(col==null){
                    Class<?> type = kf.getType();
                    if(CopyOnWriteArrayList.class.isAssignableFrom(type)){
                        col=new CopyOnWriteArrayList();
                    }else if(LinkedList.class.isAssignableFrom(type)){
                        col=new LinkedList();
                    }else if(ArrayList.class.isAssignableFrom(type)){
                        col=new ArrayList();
                    }else{
                        col=new ArrayList<>();
                    }
                    kf.set(t,col);
                }
                col.add(item);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(),e);
            }
        });
    }

    public static Field getField(Class<?> clazz,String fieldName){
        if(clazz==null || fieldName==null){
            return null;
        }
        Field ret = null;
        try {
            ret=clazz.getField(fieldName);
            if(ret!=null){
                return ret;
            }
        } catch (Exception e) {

        }
        try {
            ret=clazz.getDeclaredField(fieldName);
            if(ret!=null){
                return ret;
            }
        } catch (Exception e) {

        }
        if(Object.class.equals(clazz)){
            return ret;
        }
        Class<?> superclass = clazz.getSuperclass();
        if(superclass==null){
            return null;
        }
        return getField(superclass, fieldName);
    }

    public static <T,K> List<T> list2tree(List<T> list,
                                          Function<T,K> keyExtractor,
                                          Function<T,K> parentKeyExtractor,
                                          BiConsumer<T,T> secondAsFirstChildConsumer){
        return list2Tree(list,(v1,v2)->{
            K parentKey = parentKeyExtractor.apply(v1);
            K key = keyExtractor.apply(v2);
            return Objects.equals(parentKey,key);
        },(v1,v2)->{
            K parentKey = parentKeyExtractor.apply(v2);
            K key = keyExtractor.apply(v1);
            return Objects.equals(parentKey,key);
        },secondAsFirstChildConsumer);
    }


    /**
     * 适用于数据能一次性获取，典型的就是获取整棵树
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends ITreeNode<T>> List<T> list2Tree(List<T> list) {
        return list2Tree(list,
                ITreeNode::isMyParent,
                ITreeNode::isMyChild,
                ITreeNode::asMyChild);
    }

    private static <T extends ITreeNode<T>> void list2TreeNext(List<T> root, List<T> children) {
        list2TreeNext(root, children,
                ITreeNode::isMyChild,
                ITreeNode::asMyChild);
    }

    public static <T> List<T> list2Tree(List<T> list,
                                        BiPredicate<T, T> secondIsFirstParentPredicate,
                                        BiPredicate<T, T> secondIsFirstChildPredicate,
                                        BiConsumer<T, T> secondAsFirstChildConsumer
    ) {
        List<T> root = new LinkedList<>();
        List<T> children = new LinkedList<>();
        for (T item : list) {
            boolean isRoot = true;
            for (T mit : list) {
                if (secondIsFirstParentPredicate.test(item, mit)) {
                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                root.add(item);
            } else {
                children.add(item);
            }
        }


        if (!children.isEmpty()) {
            list2TreeNext(root, children, secondIsFirstChildPredicate, secondAsFirstChildConsumer);
        }
        return root;
    }

    private static <T> void list2TreeNext(List<T> root, List<T> children,
                                          BiPredicate<T, T> secondIsFirstChildPredicate,
                                          BiConsumer<T, T> secondAsFirstChildConsumer) {
        for (T item : root) {
            List<T> curRoot = new LinkedList<>();
            List<T> curChildren = new LinkedList<>();
            for (T mit : children) {
                if (secondIsFirstChildPredicate.test(item, mit)) {
                    secondAsFirstChildConsumer.accept(item, mit);
                    curRoot.add(mit);
                } else {
                    curChildren.add(mit);
                }
            }
            if (!curChildren.isEmpty()) {
                list2TreeNext(curRoot, curChildren, secondIsFirstChildPredicate, secondAsFirstChildConsumer);
            }
        }
    }

    public static <T> List<T> tree2List(Collection<T> tree,
                                        Function<T, ? extends Collection<T>> childProvider,
                                        Consumer<T> childCleaner) {
        List<T> ret = new LinkedList<>();
        if (tree == null || tree.isEmpty()) {
            return ret;
        }
        for (T item : tree) {
            Collection<T> child = childProvider.apply(item);
            if (childCleaner != null) {
                childCleaner.accept(item);
            }
            ret.add(item);
            List<T> next = tree2List(child, childProvider, childCleaner);
            ret.addAll(next);
        }
        return ret;
    }

    public static <T extends IChildren<T>> List<T> tree2List(Collection<T> tree) {
        return tree2List(tree, IChildren::getChildren, IChildren::cleanChildren);
    }
}
