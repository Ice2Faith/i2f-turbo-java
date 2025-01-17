package i2f.extension.easyexcel.annotation;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2022/10/12 11:52
 * @desc
 */
@Target({
        ElementType.FIELD,
        ElementType.TYPE,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ExcelCellStyles.class)
public @interface ExcelCellStyle {
    // 当SpEL表达式值为true时，应用这里的配置，SpEL表达式广泛应用于Spring配置中
    // 在Spring配置中，默认是spring上下文
    // 但是在本实例中，上下文为ExcelStyleCallbackMeta
    // 因此，在spel表达式中，可以直接使用ExcelStyleCallbackMeta中的属性
    String spel() default "#{true}";

    /**
     * 指定为表头，此值为true则对应的样式应用到表头上
     *
     * @return
     */
    boolean head() default false;

    String fontName() default "";

    IndexedColors fontColor() default IndexedColors.AUTOMATIC;

    Bool fontBold() default Bool.UNSET;

    Bool fontItalic() default Bool.UNSET;

    boolean fontUnderline() default false;

    Bool fontStrikeout() default Bool.UNSET;

    IndexedColors backgroundColor() default IndexedColors.AUTOMATIC;

    HorizontalAlignment alignHorizontal() default HorizontalAlignment.LEFT;

    VerticalAlignment alignVertical() default VerticalAlignment.TOP;

    Bool textWrapped() default Bool.UNSET;

    Bool quotePrefix() default Bool.UNSET;

    short colWidth() default 20; // lower than 0,not effect

    boolean hyperLink() default false;

    HyperlinkType linkType() default HyperlinkType.URL;

    String formula() default "";

    String comment() default "";

    boolean commentSpel() default false;

    String[] selection() default {};

    String selectionSpel() default "";

    public enum Bool {
        UNSET(null),
        TRUE(true),
        FALSE(false);

        private Boolean value;

        private Bool(Boolean value) {
            this.value = value;
        }

        public Boolean value() {
            return this.value;
        }
    }
}
