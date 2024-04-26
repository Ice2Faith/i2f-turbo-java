package i2f.container.builder.collection;

import java.util.*;
import java.util.function.*;

/**
 * @author Ice2Faith
 * @date 2024/4/24 10:59
 * @desc
 */
public class ListBuilder<E, C extends List<E>> implements Supplier<C> {
    protected C list;

    public ListBuilder(C list) {
        this.list = list;
    }

    public ListBuilder(C list, Class<E> elementType) {
        this.list = list;
    }

    @Override
    public C get() {
        return list;
    }

    public <R> R getAs(Function<C, R> converter) {
        return converter.apply(list);
    }

    public ListBuilder<E, C> then(Consumer<C> consumer) {
        consumer.accept(list);
        return this;
    }

    public <U> ListBuilder<E, C> set(BiConsumer<C, U> consumer, U val) {
        consumer.accept(list, val);
        return this;
    }

    public <U, R> ListBuilder<E, C> apply(BiFunction<C, U, R> function, U val) {
        function.apply(list, val);
        return this;
    }

    public <R> ListBuilder<E, C> call(Function<C, R> function) {
        function.apply(list);
        return this;
    }

    public ListBuilder<E, C> add(E e) {
        list.add(e);
        return this;
    }

    public ListBuilder<E, C> adds(E... elements) {
        for (E element : elements) {
            list.add(element);
        }
        return this;
    }

    public ListBuilder<E, C> adds(Iterable<? extends E> elements) {
        for (E element : elements) {
            list.add(element);
        }
        return this;
    }

    public ListBuilder<E, C> adds(Iterator<? extends E> elements) {
        while (elements.hasNext()) {
            list.add(elements.next());
        }
        return this;
    }


    public <H> ListBuilder<E, C> adds(Function<H, E> elementFunction, H... elements) {
        for (H element : elements) {
            list.add(elementFunction.apply(element));
        }
        return this;
    }

    public <H> ListBuilder<E, C> adds(Iterable<H> elements, Function<H, E> elementFunction) {
        for (H element : elements) {
            list.add(elementFunction.apply(element));
        }
        return this;
    }

    public <H> ListBuilder<E, C> adds(Iterator<H> elements, Function<H, E> elementFunction) {
        while (elements.hasNext()) {
            list.add(elementFunction.apply(elements.next()));
        }
        return this;
    }


    public ListBuilder<E, C> addsNonNUll(E... elements) {
        return adds(Objects::nonNull, elements);
    }

    public ListBuilder<E, C> adds(Predicate<E> filter, E... elements) {
        for (E element : elements) {
            if (filter != null && !filter.test(element)) {
                continue;
            }
            list.add(element);
        }
        return this;
    }

    public ListBuilder<E, C> addsNonNull(Iterable<? extends E> elements) {
        return adds(elements, Objects::nonNull);
    }

    public ListBuilder<E, C> adds(Iterable<? extends E> elements, Predicate<E> filter) {
        for (E element : elements) {
            if (filter != null && !filter.test(element)) {
                continue;
            }
            list.add(element);
        }
        return this;
    }

    public ListBuilder<E, C> addsNonNull(Iterator<? extends E> elements) {
        return adds(elements, Objects::nonNull);
    }

    public ListBuilder<E, C> adds(Iterator<? extends E> elements, Predicate<E> filter) {
        while (elements.hasNext()) {
            E element = elements.next();
            if (filter != null && !filter.test(element)) {
                continue;
            }
            list.add(element);
        }
        return this;
    }


    public <H> ListBuilder<E, C> addsNonNull(Function<H, E> elementFunction, H... elements) {
        return adds(elementFunction, Objects::nonNull, elements);
    }

    public <H> ListBuilder<E, C> adds(Function<H, E> elementFunction, Predicate<E> filter, H... elements) {
        for (H item : elements) {
            E element = elementFunction.apply(item);
            if (filter != null && !filter.test(element)) {
                continue;
            }
            list.add(element);
        }
        return this;
    }

    public <H> ListBuilder<E, C> addsNonNull(Iterable<H> elements, Function<H, E> elementFunction) {
        return adds(elements, elementFunction, Objects::nonNull);
    }

    public <H> ListBuilder<E, C> adds(Iterable<H> elements, Function<H, E> elementFunction, Predicate<E> filter) {
        for (H item : elements) {
            E element = elementFunction.apply(item);
            if (filter != null && !filter.test(element)) {
                continue;
            }
            list.add(element);
        }
        return this;
    }

    public <H> ListBuilder<E, C> addsNonNull(Iterator<H> elements, Function<H, E> elementFunction) {
        return adds(elements, elementFunction, Objects::nonNull);
    }

    public <H> ListBuilder<E, C> adds(Iterator<H> elements, Function<H, E> elementFunction, Predicate<E> filter) {
        while (elements.hasNext()) {
            E element = elementFunction.apply(elements.next());
            if (filter != null && !filter.test(element)) {
                continue;
            }
            list.add(element);
        }
        return this;
    }


    public ListBuilder<E, C> remove(Object o) {
        list.remove(o);
        return this;
    }


    public ListBuilder<E, C> addAll(Collection<? extends E> c) {
        list.addAll(c);
        return this;
    }


    public ListBuilder<E, C> addAll(int index, Collection<? extends E> c) {
        list.addAll(index, c);
        return this;
    }


    public ListBuilder<E, C> removeAll(Collection<?> c) {
        list.removeAll(c);
        return this;
    }


    public ListBuilder<E, C> retainAll(Collection<?> c) {
        list.retainAll(c);
        return this;
    }


    public ListBuilder<E, C> clear() {
        list.clear();
        return this;
    }


    public ListBuilder<E, C> set(int index, E element) {
        list.set(index, element);
        return this;
    }


    public ListBuilder<E, C> add(int index, E element) {
        list.add(index, element);
        return this;
    }


    public ListBuilder<E, C> remove(int index) {
        list.remove(index);
        return this;
    }


    public ListBuilder<E, C> replaceAll(UnaryOperator<E> operator) {
        list.replaceAll(operator);
        return this;
    }


    public ListBuilder<E, C> sort(Comparator<? super E> c) {
        list.sort(c);
        return this;
    }


    public ListBuilder<E, C> removeIf(Predicate<? super E> filter) {
        list.removeIf(filter);
        return this;
    }
}
