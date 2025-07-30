package i2f.database.metadata.std;

/**
 * @author Ice2Faith
 * @date 2025/7/30 15:51
 */
public interface IColumnType {

    String text();

    boolean precision();

    boolean scale();

    StdType stdType();
}
