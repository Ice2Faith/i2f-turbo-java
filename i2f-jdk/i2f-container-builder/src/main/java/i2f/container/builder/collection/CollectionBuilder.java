package i2f.container.builder.collection;

import i2f.typeof.token.TypeToken;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.*;

/**
 * @author Ice2Faith
 * @date 2024/4/24 10:54
 * @desc
 */
public class CollectionBuilder<E, C extends Collection<E>> implements Supplier<C> {
    protected C col;

    public CollectionBuilder(C col) {
        this.col = col;
    }

    public CollectionBuilder(C col, Class<E> elementType) {
        this.col = col;
    }

    public CollectionBuilder(C col, TypeToken<E> elementType) {
        this.col = col;
    }

    @Override
    public C get() {
        return col;
    }

    public <R> R getAs(Function<C, R> converter) {
        return converter.apply(col);
    }

    public CollectionBuilder<E, C> then(Consumer<C> consumer) {
        consumer.accept(col);
        return this;
    }

    public <U> CollectionBuilder<E, C> set(BiConsumer<C, U> consumer, U val) {
        consumer.accept(col, val);
        return this;
    }

    public <U, R> CollectionBuilder<E, C> apply(BiFunction<C, U, R> function, U val) {
        function.apply(col, val);
        return this;
    }

    public <R> CollectionBuilder<E, C> call(Function<C, R> function) {
        function.apply(col);
        return this;
    }

    public CollectionBuilder<E, C> add(E e) {
        col.add(e);
        return this;
    }

    public CollectionBuilder<E, C> adds(E... elements) {
        for (E element : elements) {
            col.add(element);
        }
        return this;
    }

    public CollectionBuilder<E, C> adds(Iterable<? extends E> elements) {
        for (E element : elements) {
            col.add(element);
        }
        return this;
    }

    public CollectionBuilder<E, C> adds(Iterator<? extends E> elements) {
        while (elements.hasNext()) {
            col.add(elements.next());
        }
        return this;
    }

    public <H> CollectionBuilder<E, C> adds(Function<H, E> elementFunction, H... elements) {
        for (H element : elements) {
            col.add(elementFunction.apply(element));
        }
        return this;
    }

    public <H> CollectionBuilder<E, C> adds(Iterable<H> elements, Function<H, E> elementFunction) {
        for (H element : elements) {
            col.add(elementFunction.apply(element));
        }
        return this;
    }

    public <H> CollectionBuilder<E, C> adds(Iterator<H> elements, Function<H, E> elementFunction) {
        while (elements.hasNext()) {
            col.add(elementFunction.apply(elements.next()));
        }
        return this;
    }


    public CollectionBuilder<E, C> addsNonNUll(E... elements) {
        return adds(Objects::nonNull, elements);
    }

    public CollectionBuilder<E, C> adds(Predicate<E> filter, E... elements) {
        for (E element : elements) {
            if (filter != null && !filter.test(element)) {
                continue;
            }
            col.add(element);
        }
        return this;
    }

    public CollectionBuilder<E, C> addsNonNull(Iterable<? extends E> elements) {
        return adds(elements, Objects::nonNull);
    }

    public CollectionBuilder<E, C> adds(Iterable<? extends E> elements, Predicate<E> filter) {
        for (E element : elements) {
            if (filter != null && !filter.test(element)) {
                continue;
            }
            col.add(element);
        }
        return this;
    }

    public CollectionBuilder<E, C> addsNonNull(Iterator<? extends E> elements) {
        return adds(elements, Objects::nonNull);
    }

    public CollectionBuilder<E, C> adds(Iterator<? extends E> elements, Predicate<E> filter) {
        while (elements.hasNext()) {
            E element = elements.next();
            if (filter != null && !filter.test(element)) {
                continue;
            }
            col.add(element);
        }
        return this;
    }


    public <H> CollectionBuilder<E, C> addsNonNull(Function<H, E> elementFunction, H... elements) {
        return adds(elementFunction, Objects::nonNull, elements);
    }

    public <H> CollectionBuilder<E, C> adds(Function<H, E> elementFunction, Predicate<E> filter, H... elements) {
        for (H item : elements) {
            E element = elementFunction.apply(item);
            if (filter != null && !filter.test(element)) {
                continue;
            }
            col.add(element);
        }
        return this;
    }

    public <H> CollectionBuilder<E, C> addsNonNull(Iterable<H> elements, Function<H, E> elementFunction) {
        return adds(elements, elementFunction, Objects::nonNull);
    }

    public <H> CollectionBuilder<E, C> adds(Iterable<H> elements, Function<H, E> elementFunction, Predicate<E> filter) {
        for (H item : elements) {
            E element = elementFunction.apply(item);
            if (filter != null && !filter.test(element)) {
                continue;
            }
            col.add(element);
        }
        return this;
    }

    public <H> CollectionBuilder<E, C> addsNonNull(Iterator<H> elements, Function<H, E> elementFunction) {
        return adds(elements, elementFunction, Objects::nonNull);
    }

    public <H> CollectionBuilder<E, C> adds(Iterator<H> elements, Function<H, E> elementFunction, Predicate<E> filter) {
        while (elements.hasNext()) {
            E element = elementFunction.apply(elements.next());
            if (filter != null && !filter.test(element)) {
                continue;
            }
            col.add(element);
        }
        return this;
    }


    public CollectionBuilder<E, C> remove(Object o) {
        col.remove(o);
        return this;
    }

    public CollectionBuilder<E, C> addAll(Collection<? extends E> c) {
        col.addAll(c);
        return this;
    }

    public CollectionBuilder<E, C> removeAll(Collection<?> c) {
        col.removeAll(c);
        return this;
    }

    public CollectionBuilder<E, C> retainAll(Collection<?> c) {
        col.retainAll(c);
        return this;
    }

    public CollectionBuilder<E, C> clear() {
        col.clear();
        return this;
    }

    public CollectionBuilder<E, C> removeIf(Predicate<? super E> filter) {
        col.removeIf(filter);
        return this;
    }
}
