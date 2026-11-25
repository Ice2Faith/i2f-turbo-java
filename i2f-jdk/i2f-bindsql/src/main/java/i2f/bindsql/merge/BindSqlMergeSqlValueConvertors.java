package i2f.bindsql.merge;

import i2f.bindsql.merge.impl.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Ice2Faith
 * @date 2026/11/25 10:58
 * @desc
 */
public class BindSqlMergeSqlValueConvertors {
    public static final CopyOnWriteArrayList<BindSqlMergeSqlValueConvertor> CONVERTORS = new CopyOnWriteArrayList<>();

    public static BindSqlMergeSqlValueConvertor getConvertor(String dialectType) {
        ServiceLoader<BindSqlMergeSqlValueConvertor> services = ServiceLoader.load(BindSqlMergeSqlValueConvertor.class);
        for (BindSqlMergeSqlValueConvertor service : services) {
            if (service == null) {
                continue;
            }
            try {
                if (service.support(dialectType)) {
                    return service;
                }
            } catch (Exception e) {
                // ignore
            }
        }

        for (BindSqlMergeSqlValueConvertor convertor : CONVERTORS) {
            if (convertor == null) {
                continue;
            }
            try {
                if (convertor.support(dialectType)) {
                    return convertor;
                }
            } catch (Exception e) {
                // ignore
            }
        }

        List<BindSqlMergeSqlValueConvertor> convertors = new ArrayList<>();
        convertors.add(PostgresMergeSqlValueConvertor.INSTANCE);
        convertors.add(OracleMergeSqlValueConvertor.INSTANCE);
        convertors.add(MySqlMergeSqlValueConvertor.INSTANCE);
        convertors.add(SqliteMergeSqlValueConvertor.INSTANCE);

        for (BindSqlMergeSqlValueConvertor convertor : convertors) {
            if (convertor == null) {
                continue;
            }
            try {
                if (convertor.support(dialectType)) {
                    return convertor;
                }
            } catch (Exception e) {
                // ignore
            }
        }

        return DefaultMergeSqlValueConvertor.INSTANCE;
    }
}
