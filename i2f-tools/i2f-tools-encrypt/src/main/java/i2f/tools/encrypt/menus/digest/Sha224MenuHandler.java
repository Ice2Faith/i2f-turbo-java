package i2f.tools.encrypt.menus.digest;

import i2f.codec.CodecUtil;
import i2f.crypto.impl.jdk.digest.md.MessageDigester;
import i2f.tools.encrypt.IMenuHandler;

public class Sha224MenuHandler implements IMenuHandler {

    @Override
    public String name() {
        return "sha-224";
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
            String encode = CodecUtil.toHexString(MessageDigester.SHA_224.digest(item.getBytes("UTF-8")));
            System.out.println(item + " ==> " + encode);
        }
    }
}
