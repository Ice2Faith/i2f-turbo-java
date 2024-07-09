package com.test.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @author Ice2Faith
 * @date 2024/7/4 19:04
 * @desc
 */
@Component
public class TestJob {

    @XxlJob("testJob")
    public ReturnT<String> testJob(String param) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>");
        int slt = (int) (Math.random() * 60 * 1000);
        System.out.println(slt);
        Thread.sleep(slt);
        System.out.println("<<<<<<<<<<<<<<<<<");
        return ReturnT.SUCCESS;
    }
}
