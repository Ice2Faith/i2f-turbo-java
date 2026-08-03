package i2f.turbo.idea.plugin.funvi.lang;

import com.intellij.lexer.FlexAdapter;
import i2f.turbo.idea.plugin.funvi.grammar.parser._FunviLexer;

public class FunviAdapter extends FlexAdapter {
    public FunviAdapter() {
        super(new _FunviLexer(null));
    }
}
