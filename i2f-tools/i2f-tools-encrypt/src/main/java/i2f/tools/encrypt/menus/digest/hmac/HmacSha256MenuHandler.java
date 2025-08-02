package i2f.tools.encrypt.menus.digest.hmac;

import i2f.codec.CodecUtil;
import i2f.crypto.impl.jdk.digest.hmac.HmacMessageDigester;
import i2f.tools.encrypt.IMenuHandler;

public class HmacSha256MenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "hmac-sha-256";
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println(name() + ": require least once argument.");
            System.out.println(name() + " [key] [...string]");
            System.out.println(name() + " hello");
            System.out.println(name() + " hello world");
            return;
        }
        byte[] key = args[0].getBytes("UTF-8");
        for (int i = 1; i < args.length; i++) {
            String item = args[i];
            HmacMessageDigester digester = HmacMessageDigester.HMAC_SHA_256.apply(key, null);
            String encode = CodecUtil.toHexString(digester.digest(item.getBytes("UTF-8")));
            System.out.println(item + "==> (key=" + args[0] + ") ==> " + encode);
        }
    }
}
