package i2f.springboot.dynamic.datasource.core;

import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2024/5/22 21:22
 * @desc
 */
@Data
public class LookupDataSource {
    private String type;
    private boolean group;
    private LookupBalanceType balance;
    private String key;
}
