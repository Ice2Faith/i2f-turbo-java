package i2f.tools.encrypt.menus.codec;


import i2f.codec.str.html.cer.HtmlCerCodec;
import i2f.tools.encrypt.IMenuHandler;

public class HtmlCerDecoderMenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "html-cer-de";
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
            String encode = new String(HtmlCerCodec.INSTANCE.decode(item));
            System.out.println(item + " ==> " + encode);
        }
    }
}
