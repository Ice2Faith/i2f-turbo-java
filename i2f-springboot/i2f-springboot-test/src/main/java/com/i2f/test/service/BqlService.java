package com.i2f.test.service;

import com.i2f.test.data.dom.SysUserDo;
import i2f.bql.core.bean.Bql;
import i2f.container.builder.Builders;
import i2f.jdbc.data.QueryResult;
import i2f.page.ApiPage;
import i2f.page.Page;
import i2f.springboot.jdbc.bql.components.BqlTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/4/24 17:07
 * @desc
 */
@Component
public class BqlService implements ApplicationRunner {

    @Autowired
    private BqlTemplate bqlTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        QueryResult qr = bqlTemplate.queryRaw(Bql.$bean()
                .$beanQuery(Builders.newObj(SysUserDo::new)
                        .set(SysUserDo::setUsername, "admin")
                        .get()
                )
        );

        System.out.println(qr.getColumns());
        System.out.println(qr.getRows());

        Bql.threadMysqlMode();

        qr = bqlTemplate.queryRaw(
                Bql.$lambda()

                        .$selectLambdas(Bql.$lm(SysUserDo::getId),
                                Bql.$lm(SysUserDo::getUsername),
                                Bql.$lm(SysUserDo::getRealname),
                                Bql.$lm(SysUserDo::getStatus)
                        )
                        .$from(SysUserDo.class)
                        .$where(() -> Bql.$lambda()
                                .$eq(SysUserDo::getUsername, "admin")
                        )
        );

        System.out.println(qr.getColumns());
        System.out.println(qr.getRows());


        List<SysUserDo> users = bqlTemplate.list(new SysUserDo());
        System.out.println(users);

        SysUserDo admin = bqlTemplate.find(Builders.newObj(SysUserDo::new)
                .set(SysUserDo::setUsername, "admin")
                .get());
        System.out.println(admin);

        Page<SysUserDo> page = bqlTemplate.page(new SysUserDo(), ApiPage.of(0, 2));
        System.out.println(page);

    }
}
