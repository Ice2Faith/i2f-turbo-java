package i2f.extension.jackson.sensible.handler;

import i2f.extension.jackson.sensible.annotations.Sensible;

import java.util.Set;

public interface ISensibleHandler {
    Set<String> accept();

    Set<Class<?>> type();

    Object handle(Object obj, Sensible ann);
}
