package i2f.extension.easyexcel.core.impl.jpa;

import i2f.extension.easyexcel.core.impl.IteratorResourceDataProvider;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Ice2Faith
 * @date 2025/12/18 9:26
 * @desc
 */
public class JpaStreamDataProvider<T> extends IteratorResourceDataProvider<JdbcTemplate, T> {
    protected JdbcTemplate jdbcTemplate;
    protected Stream<T> stream;

    public JpaStreamDataProvider(DataSource dataSource, Class<T> elemClass, Function<JdbcTemplate, Stream<T>> supplier) {
        super(elemClass);
        this.supplier=(ctx)->{
            this.jdbcTemplate=new JdbcTemplate(dataSource);
            this.stream = supplier.apply(jdbcTemplate);
            return this.stream.iterator();
        };
        this.finisher=(iter,ctx)->{
            this.stream=null;
        };
    }
}
