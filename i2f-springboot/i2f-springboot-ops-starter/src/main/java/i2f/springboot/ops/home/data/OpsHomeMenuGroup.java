package i2f.springboot.ops.home.data;

/**
 * @author Ice2Faith
 * @date 2026/5/9 8:55
 * @desc
 */
public enum OpsHomeMenuGroup implements OpsMenuGroup {
    Default("Default",10),
    App("App",20),
    Host("Host",30),
    SQL("SQL",40),
    NoSQL("NoSQL",50),
    Oss("Oss",60),
    Schedule("Schedule",70),
    Component("Component",80),
    AI("AI",90)
    ;


    private String text;
    private int order;

    private OpsHomeMenuGroup(String text,int order) {
        this.text = text;
        this.order=order;
    }

    @Override
    public String text() {
        return this.text;
    }

    @Override
    public int order() {
        return this.order;
    }


}
