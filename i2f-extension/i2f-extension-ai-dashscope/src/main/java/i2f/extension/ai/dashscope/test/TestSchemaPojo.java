package i2f.extension.ai.dashscope.test;

import i2f.extension.ai.dashscope.impl.DashScopeAi;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2026/3/19 15:53
 * @desc
 */
public class TestSchemaPojo {
    private String name;
    private int age;
    private SubSchemaPojo sub;
    private DashScopeAi.GenResponseFormat responseFormat;
    private int[] position;
    private List<String> nickNames;
    private Date createTime;

    public static class SubSchemaPojo {
        private String name;
        private int age;
        private LocalDate inureDate;
    }
}
