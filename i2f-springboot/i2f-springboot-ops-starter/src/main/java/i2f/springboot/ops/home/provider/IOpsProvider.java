package i2f.springboot.ops.home.provider;

import i2f.springboot.ops.home.data.OpsHomeMenuDto;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2025/11/28 14:36
 */
public interface IOpsProvider {
    List<OpsHomeMenuDto> getMenus();
}
