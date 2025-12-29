package i2f.extension.easyexcel.core.impl.mybatis;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2025/12/29 16:33
 * @desc
 */
public class MybatisCursorList<T> extends ArrayList<T> implements  Function<SqlSession,Cursor<T>> {
    @Getter
    @Setter
    protected Function<SqlSession,Cursor<T>> cursorCreator;

    public MybatisCursorList(Function<SqlSession,Cursor<T>> cursorCreator) {
        this.cursorCreator=cursorCreator;
    }

    public static<T> MybatisCursorList<T> of(Function<SqlSession,Cursor<T>> cursorCreator){
        return new MybatisCursorList<>(cursorCreator);
    }

    public static<M,T> MybatisCursorList<T> of(Class<M> mapperClass, Function<M,Cursor<T>> cursorCreator){
        return new MybatisCursorList<>((session)->{
            M mapper = session.getMapper(mapperClass);
            return cursorCreator.apply(mapper);
        });
    }

    @Override
    public Cursor<T> apply(SqlSession sqlSession) {
        return cursorCreator.apply(sqlSession);
    }
}
