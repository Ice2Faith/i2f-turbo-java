package i2f.url.test;

import i2f.url.UriMeta;

/**
 * @author Ice2Faith
 * @date 2024/11/30 15:50
 */
public class TestUriMeta {
    public static void main(String[] args) {
        String[] urls = new String[]{
                "jdbc:mysql://localhost:3306/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai",
                "jdbc:oracle:thin:@localhost:1521:orcl",
                "jdbc:gbase://localhost:5258/xxl_job",
                "jdbc:postgresql://localhost:5432/xxl_job",
                "jdbc:h2:file:../xxl-job-meta/h2/xxl_job.h2.db",
                "jdbc:dm://localhost:5236",
                "jdbc:dm://localhost:5236/?user=root",
                "jdbc:dm://localhost:5236?user=root",
                "jdbc:dm://localhost:5236#/logout",
                "jdbc:dm://localhost:5236?user=root#/login",
                "jdbc:dm://localhost:5236#/logout?type=wechat",
                "jdbc:kingbase8://localhost:54321/test_db",
                "jdbc:sqlserver://localhost:1433;databaseName=test_db",
                "http://localhost:63342/i2f-turbo-java/i2f-extension-reverse-engineer-generator/tpl/doc/tables-struct.html?_ijt=rblqflhoe896r4erpd181he9ia&_ij_reload=RELOAD_ON_SAVE",
                "https://www.bilibili.com/video/BV1tvU9YbE4e/?spm_id_from=333.1007.tianma.1-1-1.click&vd_source=4628f88881f128426a727cedb9ddc861",
                "http://192.168.1.100:9600/web/#/login/custom?type=wechat&code=123456&callback=456",
                "http://192.168.1.100:9600/web/?type=wechat&code=123456&callback=456#/login/custom",
                "http://192.168.1.100:9600/web/?type=wechat&code=123456&callback=456#/login/custom",
                "https://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@github.com/Ice2Faith/xxl-job-multiply.git",
                "git@gitee.com:ice2faith/i2f-turbo-java.git",
                "https://Ice2Faith:xxx123456@gitee.com/ice2faith/i2f-turbo-knowledge.git",
                "jar:file:/path/to/jarfile.jar!/com/user/TestUser.class",
                "file:/D:/IDEA_ROOT/DevCenter/i2f-turbo/i2f-turbo-java/./output/doc.html",
                "http://root:xxx123456@[fe80::193e:7ba3:960e:1e87%9]:9999/login.do?type=thrid"
        };

        for (String url : urls) {
            System.out.println("=========================================");
            System.out.println(url);
            try {
                UriMeta vo = UriMeta.parse(url);
                System.out.println(vo.toUrlString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("ok");
    }
}
