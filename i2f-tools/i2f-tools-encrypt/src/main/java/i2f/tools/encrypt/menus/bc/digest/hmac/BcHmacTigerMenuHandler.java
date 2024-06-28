package i2f.tools.encrypt.menus.bc.digest.hmac;


import i2f.codec.CodecUtil;
import i2f.extension.jce.bc.BcProvider;
import i2f.extension.jce.bc.digest.hmac.BcHmacMessageDigester;
import i2f.jce.jdk.digest.hmac.HmacMessageDigester;
import i2f.tools.encrypt.IMenuHandler;

public class BcHmacTigerMenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "bc-hmac-tiger";
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
            HmacMessageDigester digester = BcHmacMessageDigester.HmacTiger.apply(key, BcProvider.PROVIDER_NAME);
            String encode = CodecUtil.toHexString(digester.digest(item.getBytes("UTF-8")));
            System.out.println(item + "==> (key=" + args[0] + ") ==> " + encode);
        }
    }
}
