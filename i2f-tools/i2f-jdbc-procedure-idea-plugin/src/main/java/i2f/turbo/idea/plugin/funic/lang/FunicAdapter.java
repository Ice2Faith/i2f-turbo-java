package i2f.turbo.idea.plugin.funic.lang;

import com.intellij.lexer.FlexAdapter;
import i2f.turbo.idea.plugin.funic.grammar.parser._FunicLexer;

public class FunicAdapter extends FlexAdapter {
    public FunicAdapter() {
        super(new _FunicLexer(null));
    }
}
