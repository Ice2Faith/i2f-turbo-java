package i2f.spi;


import java.util.Set;

public interface SpiComponentFactory<T> {
    default Set<Class<?>> requires() {
        return null;
    }

    Class<?> type();

    T build();
}
