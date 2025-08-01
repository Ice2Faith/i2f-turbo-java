package i2f.extension.fastexcel.test;

import i2f.extension.fastexcel.ExcelExportUtil;
import i2f.extension.fastexcel.ExcelImportUtil;
import i2f.extension.fastexcel.core.ExcelExportMode;
import i2f.extension.fastexcel.core.ExcelExportPage;
import i2f.extension.fastexcel.core.IDataProvider;
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
        testExtractorExport();
        System.out.println("exportMapTs:" + (System.currentTimeMillis() - ts));

        /*File file = new File("./output/export.xlsx");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        List<Map<String, Object>> list = ExcelImportUtil.read(file);
        System.out.println(list);*/
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
        File templateFile = null;//new File("./i2f-extension/i2f-extension-easyexcel/src/test/java/i2f/extension/easyexcel/test/sys-user.xlsx");
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
        File templateFile = null;//new File("./i2f-extension/i2f-extension-easyexcel/src/test/java/i2f/extension/easyexcel/test/sys-user.xlsx");
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

    public static void testExtractorExport() throws Exception {
        File file = new File("./output/export.xlsx");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        File templateFile = null; // new File("./i2f-extension/i2f-extension-fastexcel/src/test/java/i2f/extension/fastexcel/test/sys-user.xlsx");
        TestSysUserVo webVo = new TestSysUserVo();
        TestSysUserService baseService = new TestSysUserService();
        IDataProvider provider = IDataProvider.of(page -> {
            if (page.getIndex() >= 100) {
                return null;
            }
            List<TestSysUserVo> ret = new ArrayList<>();
            for (int i = 0; i < page.getSize(); i++) {
                TestSysUserVo item = new TestSysUserVo();
                item.setId(page.getIndex() * 100000L + i);
                item.setUsername("user-" + page.getIndex() + "-" + i);
                item.setAge(page.getIndex());
                item.setStatus("正常");
                ret.add(item);

            }
            return ret;
        }, TestSysUserVo.class);
        List<TestSysUserVo> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            TestSysUserVo item = new TestSysUserVo();
            item.setId(0L + i);
            item.setUsername("user-" + i);
            item.setAge(i);
            item.setStatus("正常");
            list.add(item);
        }
        provider = new ListDataProvider(list, TestSysUserVo.class);
        ExcelExportUtil.write(provider, file, "用户表", templateFile, null, (task) -> {
            task.setExcludeColumnTags(new HashSet<>(Arrays.asList("no-export")));
            task.setEnableCellStyleAnnotation(true);
            task.setSheetSize(30);
            task.setPageSize(10);
        });
    }
}
