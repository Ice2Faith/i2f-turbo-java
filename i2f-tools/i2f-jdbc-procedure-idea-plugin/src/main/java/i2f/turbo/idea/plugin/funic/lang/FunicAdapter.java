package i2f.turbo.idea.plugin.funic.lang;

import com.intellij.lexer.FlexAdapter;
import i2f.turbo.idea.plugin.funic.grammar.parser._FunicLexer;
import i2f.turbo.idea.plugin.tinyscript.grammar.parser._TinyScriptLexer;

public class FunicAdapter extends FlexAdapter {
    public FunicAdapter() {
        super(new _FunicLexer(null));
    }
}
