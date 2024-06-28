package i2f.tools.encrypt.menus.bc.digest;


import i2f.codec.CodecUtil;
import i2f.extension.jce.bc.digest.md.BcMessageDigester;
import i2f.tools.encrypt.IMenuHandler;

public class BcSha3A384MenuHandler implements IMenuHandler {

    @Override
    public String name() {
        return "bc-sha3-384";
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
            String encode = CodecUtil.toHexString(BcMessageDigester.SHA3_384.digest(item.getBytes("UTF-8")));
            System.out.println(item + " ==> " + encode);
        }
    }
}
