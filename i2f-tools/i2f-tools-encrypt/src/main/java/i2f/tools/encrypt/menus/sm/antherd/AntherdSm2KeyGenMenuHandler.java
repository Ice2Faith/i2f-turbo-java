package i2f.tools.encrypt.menus.sm.antherd;


import com.antherd.smcrypto.sm2.Keypair;
import i2f.extension.jce.sm.antherd.encrypt.asymmetric.Sm2Encryptor;
import i2f.tools.encrypt.IMenuHandler;

public class AntherdSm2KeyGenMenuHandler implements IMenuHandler {

    @Override
    public String name() {
        return "antherd-sm2-keygen";
    }

    @Override
    public void execute(String[] args) throws Exception {
        Keypair keypair = Sm2Encryptor.genKey();
        System.out.println("publicKey" + " ==> " + keypair.getPublicKey());
        System.out.println("privateKey" + " ==> " + keypair.getPrivateKey());
    }
}
