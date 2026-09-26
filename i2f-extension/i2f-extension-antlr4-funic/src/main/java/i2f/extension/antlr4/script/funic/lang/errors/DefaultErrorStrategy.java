package i2f.extension.antlr4.script.funic.lang.errors;

import i2f.extension.antlr4.script.funic.lang.exception.throwable.FunicParseException;
import org.antlr.v4.runtime.*;

/**
 * @author Ice2Faith
 * @date 2025/5/22 23:51
 * @desc
 */
public class DefaultErrorStrategy extends org.antlr.v4.runtime.DefaultErrorStrategy {
    public static final DefaultErrorStrategy INSTANCE = new DefaultErrorStrategy();

    @Override
    public void reportError(Parser parser, RecognitionException e) {
        Token startToken = e.getOffendingToken();
        Token offendingToken = e.getOffendingToken();
        if (e instanceof NoViableAltException) {
            NoViableAltException ex = (NoViableAltException) e;
            startToken = ex.getStartToken();
        }
        TokenStream tokens = parser.getInputStream();
        String input;
        if (tokens != null) {
            if (startToken.getType() == -1) {
                input = "<EOF>";
            } else {
                input = tokens.getText(startToken, offendingToken);
            }
        } else {
            input = "<unknown input>";
        }

        int line = offendingToken.getLine();
        int charPositionInLine = offendingToken.getCharPositionInLine();
        String msg = "no viable alternative at input " + this.escapeWSAndQuote(input);
        String errorMsg = "line " + line + ":" + charPositionInLine + " " + msg;
        FunicParseException ex = new FunicParseException(errorMsg, e);
        ex.setLine(line);
        ex.setColumn(charPositionInLine);
        throw ex;
    }
}
