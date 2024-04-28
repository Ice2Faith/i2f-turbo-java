package i2f.bindsql.data;

import i2f.bindsql.BindSql;
import i2f.page.ApiPage;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2024/4/28 9:06
 * @desc
 */
@Data
@NoArgsConstructor
public class PageBindSql {
    private BindSql countSql;
    private BindSql pageSql;
    private ApiPage page;
}
