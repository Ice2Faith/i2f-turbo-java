package i2f.tools.encrypt.menus;


import i2f.resources.ResourceUtil;
import i2f.tools.encrypt.IMenuHandler;

public class HelpMenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "help";
    }

    @Override
    public void execute(String[] args) throws Exception {
        String help = ResourceUtil.getClasspathResourceAsString("static/help.txt", "UTF-8");
        System.out.println(help);
    }
}
