package i2f.extension.fastexcel.test;

import i2f.extension.fastexcel.ExcelExportUtil;
import i2f.extension.fastexcel.ExcelImportUtil;
import i2f.extension.fastexcel.core.ExcelExportMode;
import i2f.extension.fastexcel.core.ExcelExportPage;
import i2f.extension.fastexcel.core.impl.ListDataProvider;
import i2f.extension.fastexcel.core.impl.ServiceDataProviderAdapter;

import java.io.File;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/6/24 11:26
 * @desc
 */
public class TestExcelUtil {
    public static void main(String[] args) throws Exception {
        long ts = System.currentTimeMillis();
        testExportMap();
        System.out.println("exportMapTs:" + (System.currentTimeMillis() - ts));

        File file = new File("./output/export.xlsx");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        List<Map<String, Object>> list = ExcelImportUtil.read(file);
        System.out.println(list);
//
//        ts = System.currentTimeMillis();
//        testServiceExport();
//        System.out.println("exportTs:" + (System.currentTimeMillis() - ts));
//
//        ts = System.currentTimeMillis();
//        testImport();
//        System.out.println("importTs:" + (System.currentTimeMillis() - ts));
    }

    public static void testExportMap() throws Exception {
        File file = new File("./output/export.xlsx");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("id", "1");
        map.put("name", "test");
        map.put("age", 18);
        list.add(map);
        list.add(map);
        ExcelExportUtil.write(new ListDataProvider(list),
                file,
                "用户信息表");
    }

    public static void testImport() throws Exception {
        File file = new File("./output/export.xlsx");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        List<TestSysUserVo> list = ExcelImportUtil.read(file, TestSysUserVo.class);
        System.out.println(list);
    }

    public static void testListExport() throws Exception {
        File file = new File("./output/export.xlsx");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        File templateFile = null;//new File("./i2f-extension/i2f-extension-easyexcel/src/main/java/i2f/extension/easyexcel/test/sys-user.xlsx");
        TestSysUserVo webVo = new TestSysUserVo();
        TestSysUserService baseService = new TestSysUserService();
        List<TestSysUserVo> list = baseService.page(webVo, 0, Integer.MAX_VALUE);
        ExcelExportUtil.write(new ListDataProvider(list, TestSysUserVo.class),
                file,
                "用户信息表",
                templateFile, null,
                (task) -> {
                    task.setExcludeColumnTags(new HashSet<>(Arrays.asList("no-export")));
                    task.setEnableCellStyleAnnotation(true);
                });
    }

    public static void testServiceExport() throws Exception {
        File file = new File("./output/export.xlsx");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        File templateFile = null;//new File("./i2f-extension/i2f-extension-easyexcel/src/main/java/i2f/extension/easyexcel/test/sys-user.xlsx");
        TestSysUserVo webVo = new TestSysUserVo();
        TestSysUserService baseService = new TestSysUserService();
        ExcelExportUtil.write(new ServiceDataProviderAdapter<TestSysUserVo, TestSysUserService, TestSysUserVo>(
                ExcelExportMode.ALL, baseService, webVo, TestSysUserVo.class) {
            @Override
            public List<TestSysUserVo> doRequestData(TestSysUserService service, ExcelExportPage page, TestSysUserVo reqVo, Object... args) {
                if (page == null) {
                    return service.page(reqVo, 0, Integer.MAX_VALUE);
                }
                return service.page(webVo, page.getIndex() * page.getSize(), page.getSize());
            }
        }, file, "用户表", templateFile, null, (task) -> {
            task.setExcludeColumnTags(new HashSet<>(Arrays.asList("no-export")));
            task.setEnableCellStyleAnnotation(true);
        });
    }
}
