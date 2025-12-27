package com.i2f.test.service;

import com.i2f.test.data.dom.SysUserDo;
import com.i2f.test.mapper.TestMapper;
import i2f.bql.core.bean.Bql;
import i2f.container.builder.Builders;
import i2f.jdbc.bql.BqlTemplate;
import i2f.jdbc.data.QueryResult;
import i2f.page.ApiPage;
import i2f.page.Page;
import i2f.springboot.dynamic.datasource.aop.DataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/4/24 17:07
 * @desc
 */
@Component
public class BqlService implements ApplicationRunner, ApplicationContextAware {

    @Autowired
    private BqlTemplate bqlTemplate;

    @Resource
    private TestMapper testMapper;

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<SysUserDo> allList = testMapper.listAll();

        List<SysUserDo> sysUserDos = testMapper.listByMapper();

        BqlService proxy = context.getBean(BqlService.class);

        proxy.testRaw();

        Bql.threadMysqlMode();

        proxy.testRawBql();

        proxy.testQuery();

        proxy.testQuery();

        proxy.testQuery();

        proxy.testQuery();

        proxy.testWrite();

    }

    @DataSource(value = "master")
    public void testRaw() throws Exception {
        QueryResult qr = bqlTemplate.queryRaw(Bql.$bean()
                .$beanQuery(Builders.newObj(SysUserDo::new)
                        .set(SysUserDo::setUsername, "admin")
                        .get()
                )
        );

        System.out.println(qr.getColumns());
        System.out.println(qr.getRows());
    }

    @DataSource(value = "slave")
    public void testRawBql() throws Exception {

        QueryResult qr = bqlTemplate.queryRaw(
                Bql.$lambda()

                        .$selectLambdas(Bql.$lm(SysUserDo::getId),
                                Bql.$lm(SysUserDo::getUsername),
                                Bql.$lm(SysUserDo::getNickname),
                                Bql.$lm(SysUserDo::getStatus)
                        )
                        .$from(SysUserDo.class)
                        .$where(() -> Bql.$lambda()
                                .$eq(SysUserDo::getUsername, "admin")
                        )
        );

        System.out.println(qr.getColumns());
        System.out.println(qr.getRows());
    }

    @DataSource(value = "read", group = true)
    public void testQuery() throws Exception {
        List<SysUserDo> users = bqlTemplate.list(new SysUserDo());
        System.out.println(users);
    }

    @DataSource(value = "write", group = true)
    public void testWrite() throws Exception {
        SysUserDo admin = bqlTemplate.find(Builders.newObj(SysUserDo::new)
                .set(SysUserDo::setUsername, "admin")
                .get());
        System.out.println(admin);

        Page<SysUserDo> page = bqlTemplate.page(new SysUserDo(), ApiPage.of(0, 2));
        System.out.println(page);
    }
}
