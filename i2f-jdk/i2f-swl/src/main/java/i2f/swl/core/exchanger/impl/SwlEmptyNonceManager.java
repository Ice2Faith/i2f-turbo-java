package i2f.swl.core.exchanger.impl;

import i2f.swl.core.exchanger.SwlNonceManager;

/**
 * @author Ice2Faith
 * @date 2024/8/4 22:25
 * @desc
 */
public class SwlEmptyNonceManager implements SwlNonceManager {
    @Override
    public boolean contains(String nonce) {
        return false;
    }

    @Override
    public void set(String nonce, long timeoutSeconds) {

    }
}
