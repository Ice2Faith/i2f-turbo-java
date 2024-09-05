package i2f.springboot.mybatis.dynamic;

import lombok.Data;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;
import i2f.extension.mybatis.dynamic.MybatisDynamicScriptExecutor;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@ConditionalOnExpression("${i2f.mybatis.dynamic.enable:true}")
@Data
public class SpringMybatisDynamicScriptExecutor {
    private static CountDownLatch INSTANCE_LATCH=new CountDownLatch(1);
    private static volatile SpringMybatisDynamicScriptExecutor INSTANCE;

    protected DataSource dataSource;
    protected volatile Configuration mybatisConfiguration;

    public SpringMybatisDynamicScriptExecutor(DataSource dataSource, ApplicationContext applicationContext) {
        this.dataSource = dataSource;
        initConfiguration(applicationContext);
        INSTANCE=this;
        INSTANCE_LATCH.countDown();
    }

    public void initConfiguration(ApplicationContext applicationContext){
        if(mybatisConfiguration==null){
            try{
                String className="org.mybatis.spring.boot.autoconfigure.MybatisProperties";
                Class<?> clazz = Class.forName(className);
                Object properties = applicationContext.getBean(clazz);
                if(properties!=null){
                    Method method = clazz.getMethod("getConfiguration");
                    mybatisConfiguration=(Configuration) method.invoke(properties);
                }
            }catch (Exception e){

            }
        }
        if(mybatisConfiguration==null){
            try{
                String className="com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties";
                Class<?> clazz = Class.forName(className);
                Object properties = applicationContext.getBean(clazz);
                if(properties!=null){
                    Method method = clazz.getMethod("getConfiguration");
                    mybatisConfiguration=(Configuration) method.invoke(properties);
                }
            }catch (Exception e){

            }
        }
    }

    public static SpringMybatisDynamicScriptExecutor getInstance(){
        try{
            INSTANCE_LATCH.await();
        }catch (InterruptedException e){

        }
        return INSTANCE;
    }

    public Configuration getConfiguration(Configuration conf) {
        if (mybatisConfiguration != null) {
            conf= mybatisConfiguration;
        }
        return conf;
    }

    public <T> T find(String script, Object params, Class<T> returnType) throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            return MybatisDynamicScriptExecutor.find(script, params, returnType, conn, getConfiguration(null));
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    public <T> T find(String script, Object params, Class<T> returnType,
                      Configuration configuration) throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            return MybatisDynamicScriptExecutor.find(script, params, returnType, conn, getConfiguration(configuration));
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    public <T> T find(String script, Object params, Class<T> returnType,
                      String statementId, String resultMapId,
                      Configuration configuration) throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            return MybatisDynamicScriptExecutor.find(script, params, returnType, conn, statementId, resultMapId, getConfiguration(configuration));
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }


    public <T> List<T> query(String script, Object params, Class<T> returnType) throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            return MybatisDynamicScriptExecutor.query(script, params, returnType, conn, getConfiguration(null));
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    public <T> List<T> query(String script, Object params, Class<T> returnType,
                             Configuration configuration) throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            return MybatisDynamicScriptExecutor.query(script, params, returnType, conn, getConfiguration(configuration));
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    public <T> List<T> query(String script, Object params, Class<T> returnType,
                             String statementId, String resultMapId,
                             Configuration configuration) throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            return MybatisDynamicScriptExecutor.query(script, params, returnType, conn, statementId, resultMapId, getConfiguration(configuration));
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }


    public int update(String script, Object params) throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            return MybatisDynamicScriptExecutor.update(script, params, conn, getConfiguration(null));
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    public int update(String script, Object params,
                      Configuration configuration) throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            return MybatisDynamicScriptExecutor.update(script, params, conn, getConfiguration(configuration));
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    public int update(String script, Object params,
                      String statementId, String resultMapId,
                      Configuration configuration) throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            return MybatisDynamicScriptExecutor.update(script, params, conn, statementId, resultMapId, getConfiguration(configuration));
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }


}
