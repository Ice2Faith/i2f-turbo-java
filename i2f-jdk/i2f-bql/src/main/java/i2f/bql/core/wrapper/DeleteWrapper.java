package i2f.bql.core.wrapper;


/**
 * @author Ice2Faith
 * @date 2024/12/5 9:24
 */
public class DeleteWrapper<H extends DeleteWrapper<H>> extends ConditionWrapper<H> {
    protected Object table;

    public static DeleteWrapper<?> from() {
        return new DeleteWrapper<>();
    }

    @Override
    public i2f.bql.core.Bql<?> get() {
        i2f.bql.core.lambda.Bql<?> ret = i2f.bql.core.lambda.Bql.$lambda()
                .$reset();
        if (table instanceof Class) {
            ret.$deleteFrom((Class) table);
        } else {
            ret.$deleteFrom(String.valueOf(table));
        }
        ret.$where(super::get);
        return ret;
    }

    public H table(String table) {
        this.table = table;
        return (H) this;
    }

    public H table(Class<?> table) {
        this.table = table;
        return (H) this;
    }


}
