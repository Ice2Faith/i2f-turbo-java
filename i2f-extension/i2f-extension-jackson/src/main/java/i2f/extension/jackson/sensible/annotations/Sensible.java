package i2f.extension.jackson.sensible.annotations;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import i2f.extension.jackson.sensible.JacksonSensibleSerializer;
import i2f.extension.jackson.sensible.handler.impl.SensibleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@JsonSerialize(using = JacksonSensibleSerializer.class)
@JacksonAnnotationsInside
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensible {
    String type() default SensibleType.PHONE;

    int prefix() default 1;

    int suffix() default 1;

    String fill() default "*";

    String param() default "";
}
