package i2f.turbo.idea.plugin.ognl.lang;

import com.intellij.lexer.FlexAdapter;
import i2f.turbo.idea.plugin.ognl.grammar.parser._OgnlLexer;

public class OgnlAdapter extends FlexAdapter {
    public OgnlAdapter() {
        super(new _OgnlLexer(null));
    }
}
