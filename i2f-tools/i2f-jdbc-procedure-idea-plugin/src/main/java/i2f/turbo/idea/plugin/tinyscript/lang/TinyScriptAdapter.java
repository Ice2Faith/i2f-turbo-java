package i2f.turbo.idea.plugin.tinyscript.lang;

import com.intellij.lexer.FlexAdapter;
import i2f.turbo.idea.plugin.tinyscript.grammar.parser._TinyScriptLexer;

public class TinyScriptAdapter extends FlexAdapter {
    public TinyScriptAdapter() {
        super(new _TinyScriptLexer(null));
    }
}
