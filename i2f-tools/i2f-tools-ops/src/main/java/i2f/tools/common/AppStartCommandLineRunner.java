package i2f.tools.common;

import i2f.os.OsUtil;
import lombok.Data;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author Ice2Faith
 * @date 2026/6/23 8:27
 * @desc
 */
@Data
@Component
public class AppStartCommandLineRunner implements ApplicationRunner, EnvironmentAware {
    protected Environment environment;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(OsUtil.isWindows()){
            String prop = environment.getProperty("server.port", "8080");
            OsUtil.runCmd(new String[]{
                    "explorer",
                    "http://localhost:"+prop+"/ops/"
            });
        }
    }
}
