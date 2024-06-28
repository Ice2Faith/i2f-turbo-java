package i2f.tools.encrypt.menus.codec;


import i2f.codec.bytes.basex.Base16StringByteCodec;
import i2f.tools.encrypt.IMenuHandler;

public class Base16EncoderMenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "base16-en";
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
            String encode = Base16StringByteCodec.INSTANCE.encode(item.getBytes("UTF-8"));
            System.out.println(item + " ==> " + encode);
        }
    }
}
