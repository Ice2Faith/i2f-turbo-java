package i2f.tools.encrypt.menus.codec;


import i2f.codec.str.html.cer.HtmlCerCodec;
import i2f.tools.encrypt.IMenuHandler;

public class HtmlCerEncoderMenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "html-cer-en";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println(name() + ": require least once argument.");
            System.out.println(name() + " [...string]");
            System.out.println(name() + " hello");
            System.out.println(name() + " hello world");
            return;
        }
        for (String item : args) {
            String encode = HtmlCerCodec.INSTANCE.encode(item);
            System.out.println(item + " ==> " + encode);
        }
    }
}
