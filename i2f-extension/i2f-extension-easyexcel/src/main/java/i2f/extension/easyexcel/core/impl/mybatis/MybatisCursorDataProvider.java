package i2f.extension.easyexcel.core.impl.mybatis;

import i2f.extension.easyexcel.core.impl.IteratorResourceDataProvider;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2025/12/18 8:30
 * @desc
 */
public class MybatisCursorDataProvider<T> extends IteratorResourceDataProvider<SqlSessionFactory, T> {
    protected SqlSession session;
    protected transient Cursor<T> cursor;

    public MybatisCursorDataProvider(SqlSessionFactory sessionFactory, Class<T> elemClass, Function<SqlSession, Cursor<T>> supplier) {
        super(elemClass);
        this.context = sessionFactory;
        this.supplier = (ctx) -> {
            this.session = sessionFactory.openSession();
            this.cursor = supplier.apply(session);
            return this.cursor.iterator();
        };
        this.finisher = (iter, ctx) -> {
            try {
                if (this.cursor != null) {
                    this.cursor.close();
                }
                this.cursor = null;
            } catch (Exception e) {

            }
            try {
                if (this.session != null) {
                    this.session.close();
                }
                this.session = null;
            } catch (Exception e) {

            }
        };
    }

    public <M> MybatisCursorDataProvider(SqlSessionFactory sessionFactory, Class<T> elemClass, Class<M> mapperClass, Function<M, Cursor<T>> extractor) {
        super(elemClass);
        this.context = sessionFactory;
        this.supplier = (ctx) -> {
            this.session = sessionFactory.openSession();
            M mapper = session.getMapper(mapperClass);
            this.cursor = extractor.apply(mapper);
            return this.cursor.iterator();
        };
        this.finisher = (iter, ctx) -> {
            try {
                if (this.cursor != null) {
                    this.cursor.close();
                }
                this.cursor = null;
            } catch (Exception e) {

            }
            try {
                if (this.session != null) {
                    this.session.close();
                }
                this.session = null;
            } catch (Exception e) {

            }
        };
    }

}
