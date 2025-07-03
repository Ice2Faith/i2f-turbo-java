package i2f.extension.fastexcel.test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/6/24 11:30
 * @desc
 */
public class TestSysUserService {
    public List<TestSysUserVo> page(TestSysUserVo webVo, int offset, int limit) {
        List<TestSysUserVo> ret = new ArrayList<>();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 3000; i++) {
            TestSysUserVo vo = new TestSysUserVo();
            vo.setId(i + 1L);
            vo.setUsername("http://admin-" + i + "@123.com/");
            vo.setPassword("123456");
            vo.setAge(random.nextInt(30) + 18);
            vo.setStatus("正常");
            ret.add(vo);
        }

        return ret;
    }

}
