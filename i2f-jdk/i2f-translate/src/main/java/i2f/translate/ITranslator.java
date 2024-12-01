package i2f.translate;

import i2f.lifecycle.ILifeCycle;

/**
 * @author Ice2Faith
 * @date 2024/12/1 10:09
 */
public interface ITranslator extends ILifeCycle {
    String translate(String str);
}
