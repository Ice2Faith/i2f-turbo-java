package i2f.tools.encrypt.menus.sm.antherd;


import i2f.extension.jce.sm.antherd.encrypt.symmetric.Sm4Encryptor;
import i2f.tools.encrypt.IMenuHandler;

/**
 * @author Ice2Faith
 * @date 2023/2/20 17:04
 * @desc
 */
public class AntherdSm4GenKeyMenuHandler implements IMenuHandler {
    @Override
    public String name() {
        return "antherd-sm4-keygen";
    }

    @Override
    public void execute(String[] args) throws Exception {
        String key = Sm4Encryptor.genKey();
        System.out.println("==> " + key);
    }
}
