// Generated from D:/IDEA_ROOT/DevCenter/i2f-turbo/i2f-turbo-java/i2f-extension/i2f-extension-antlr4/src/main/java/i2f/extension/antlr4/calculator/rule/Calculator.g4 by ANTLR 4.13.2

package i2f.extension.antlr4.calculator;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class CalculatorParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
            T__9 = 10, T__10 = 11, T__11 = 12, T__12 = 13, T__13 = 14, T__14 = 15, T__15 = 16, T__16 = 17,
            T__17 = 18, T__18 = 19, T__19 = 20, T__20 = 21, T__21 = 22, T__22 = 23, T__23 = 24,
            T__24 = 25, T__25 = 26, T__26 = 27, T__27 = 28, T__28 = 29, T__29 = 30, T__30 = 31,
            T__31 = 32, T__32 = 33, T__33 = 34, T__34 = 35, T__35 = 36, T__36 = 37, T__37 = 38,
            T__38 = 39, T__39 = 40, T__40 = 41, T__41 = 42, T__42 = 43, T__43 = 44, T__44 = 45,
            T__45 = 46, T__46 = 47, T__47 = 48, T__48 = 49, T__49 = 50, T__50 = 51, T__51 = 52,
            T__52 = 53, T__53 = 54, T__54 = 55, EQUAL = 56, CONST = 57, DEC_NUMBER = 58, HEX_NUMBER = 59,
            OTC_NUMBER = 60, BIN_NUMBER = 61, HIGH_NUMBER = 62, IDENTIFIER = 63, WS = 64, UNRECOGNIZED = 65;
    public static final int
            RULE_eval = 0, RULE_number = 1, RULE_expr = 2, RULE_convertor = 3, RULE_bracket = 4,
            RULE_prefixOperator = 5, RULE_suffixOperator = 6, RULE_operatorV5 = 7,
            RULE_operatorV4 = 8, RULE_operatorV3 = 9, RULE_operatorV2 = 10, RULE_operatorV1 = 11,
            RULE_operatorV0 = 12;

    private static String[] makeRuleNames() {
        return new String[]{
                "eval", "number", "expr", "convertor", "bracket", "prefixOperator", "suffixOperator",
                "operatorV5", "operatorV4", "operatorV3", "operatorV2", "operatorV1",
                "operatorV0"
        };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[]{
                null, "'('", "','", "')'", "'+'", "'-'", "'abs'", "'neg'", "'ln'", "'sin'",
                "'cos'", "'tan'", "'arcsin'", "'arccos'", "'arctan'", "'angle'", "'radian'",
                "'floor'", "'round'", "'ceil'", "'rand'", "'feibo'", "'!'", "'%'", "'per'",
                "'**'", "'muls'", "'++'", "'adds'", "'&'", "'|'", "'~'", "'xor'", "'<<'",
                "'>>'", "'and'", "'or'", "'not'", "'lmov'", "'rmov'", "'log'", "'sqrt'",
                "'^'", "'pow'", "'*'", "'/'", "'mul'", "'div'", "'mod'", "'>='", "'<='",
                "'!='", "'<>'", "'=='", "'>'", "'<'", "'='"
        };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[]{
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, "EQUAL", "CONST", "DEC_NUMBER",
                "HEX_NUMBER", "OTC_NUMBER", "BIN_NUMBER", "HIGH_NUMBER", "IDENTIFIER",
                "WS", "UNRECOGNIZED"
        };
    }

    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "Calculator.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public CalculatorParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    @SuppressWarnings("CheckReturnValue")
    public static class EvalContext extends ParserRuleContext {
        public NumberContext number() {
            return getRuleContext(NumberContext.class, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public TerminalNode EQUAL() {
            return getToken(CalculatorParser.EQUAL, 0);
        }

        public EvalContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_eval;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterEval(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitEval(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CalculatorVisitor) return ((CalculatorVisitor<? extends T>) visitor).visitEval(this);
            else return visitor.visitChildren(this);
        }
    }

    public final EvalContext eval() throws RecognitionException {
        EvalContext _localctx = new EvalContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_eval);
        try {
            setState(34);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 0, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(26);
                    number();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(27);
                    expr(0);
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(28);
                    number();
                    setState(29);
                    match(EQUAL);
                }
                break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(31);
                    expr(0);
                    setState(32);
                    match(EQUAL);
                }
                break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class NumberContext extends ParserRuleContext {
        public TerminalNode CONST() {
            return getToken(CalculatorParser.CONST, 0);
        }

        public TerminalNode DEC_NUMBER() {
            return getToken(CalculatorParser.DEC_NUMBER, 0);
        }

        public TerminalNode HEX_NUMBER() {
            return getToken(CalculatorParser.HEX_NUMBER, 0);
        }

        public TerminalNode OTC_NUMBER() {
            return getToken(CalculatorParser.OTC_NUMBER, 0);
        }

        public TerminalNode BIN_NUMBER() {
            return getToken(CalculatorParser.BIN_NUMBER, 0);
        }

        public TerminalNode HIGH_NUMBER() {
            return getToken(CalculatorParser.HIGH_NUMBER, 0);
        }

        public NumberContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_number;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterNumber(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitNumber(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CalculatorVisitor)
                return ((CalculatorVisitor<? extends T>) visitor).visitNumber(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NumberContext number() throws RecognitionException {
        NumberContext _localctx = new NumberContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_number);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(36);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 9079256848778919936L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ExprContext extends ParserRuleContext {
        public BracketContext bracket() {
            return getRuleContext(BracketContext.class, 0);
        }

        public ConvertorContext convertor() {
            return getRuleContext(ConvertorContext.class, 0);
        }

        public PrefixOperatorContext prefixOperator() {
            return getRuleContext(PrefixOperatorContext.class, 0);
        }

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public NumberContext number() {
            return getRuleContext(NumberContext.class, 0);
        }

        public OperatorV5Context operatorV5() {
            return getRuleContext(OperatorV5Context.class, 0);
        }

        public OperatorV4Context operatorV4() {
            return getRuleContext(OperatorV4Context.class, 0);
        }

        public OperatorV3Context operatorV3() {
            return getRuleContext(OperatorV3Context.class, 0);
        }

        public OperatorV2Context operatorV2() {
            return getRuleContext(OperatorV2Context.class, 0);
        }

        public OperatorV1Context operatorV1() {
            return getRuleContext(OperatorV1Context.class, 0);
        }

        public OperatorV0Context operatorV0() {
            return getRuleContext(OperatorV0Context.class, 0);
        }

        public SuffixOperatorContext suffixOperator() {
            return getRuleContext(SuffixOperatorContext.class, 0);
        }

        public ExprContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expr;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterExpr(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitExpr(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CalculatorVisitor) return ((CalculatorVisitor<? extends T>) visitor).visitExpr(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprContext expr() throws RecognitionException {
        return expr(0);
    }

    private ExprContext expr(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        ExprContext _localctx = new ExprContext(_ctx, _parentState);
        ExprContext _prevctx = _localctx;
        int _startState = 4;
        enterRecursionRule(_localctx, 4, RULE_expr, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(45);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case T__0: {
                        setState(39);
                        bracket();
                    }
                    break;
                    case IDENTIFIER: {
                        setState(40);
                        convertor();
                    }
                    break;
                    case T__3:
                    case T__4:
                    case T__5:
                    case T__6:
                    case T__7:
                    case T__8:
                    case T__9:
                    case T__10:
                    case T__11:
                    case T__12:
                    case T__13:
                    case T__14:
                    case T__15:
                    case T__16:
                    case T__17:
                    case T__18:
                    case T__19:
                    case T__20: {
                        setState(41);
                        prefixOperator();
                        setState(42);
                        expr(8);
                    }
                    break;
                    case CONST:
                    case DEC_NUMBER:
                    case HEX_NUMBER:
                    case OTC_NUMBER:
                    case BIN_NUMBER:
                    case HIGH_NUMBER: {
                        setState(44);
                        number();
                    }
                    break;
                    default:
                        throw new NoViableAltException(this);
                }
                _ctx.stop = _input.LT(-1);
                setState(75);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 3, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            setState(73);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 2, _ctx)) {
                                case 1: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(47);
                                    if (!(precpred(_ctx, 7)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 7)");
                                    setState(48);
                                    operatorV5();
                                    setState(49);
                                    expr(8);
                                }
                                break;
                                case 2: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(51);
                                    if (!(precpred(_ctx, 6)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 6)");
                                    setState(52);
                                    operatorV4();
                                    setState(53);
                                    expr(7);
                                }
                                break;
                                case 3: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(55);
                                    if (!(precpred(_ctx, 5)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 5)");
                                    setState(56);
                                    operatorV3();
                                    setState(57);
                                    expr(6);
                                }
                                break;
                                case 4: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(59);
                                    if (!(precpred(_ctx, 4)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 4)");
                                    setState(60);
                                    operatorV2();
                                    setState(61);
                                    expr(5);
                                }
                                break;
                                case 5: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(63);
                                    if (!(precpred(_ctx, 3)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 3)");
                                    setState(64);
                                    operatorV1();
                                    setState(65);
                                    expr(4);
                                }
                                break;
                                case 6: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(67);
                                    if (!(precpred(_ctx, 2)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 2)");
                                    setState(68);
                                    operatorV0();
                                    setState(69);
                                    expr(3);
                                }
                                break;
                                case 7: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(71);
                                    if (!(precpred(_ctx, 9)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 9)");
                                    setState(72);
                                    suffixOperator();
                                }
                                break;
                            }
                        }
                    }
                    setState(77);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 3, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ConvertorContext extends ParserRuleContext {
        public TerminalNode IDENTIFIER() {
            return getToken(CalculatorParser.IDENTIFIER, 0);
        }

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public ConvertorContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_convertor;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterConvertor(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitConvertor(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CalculatorVisitor)
                return ((CalculatorVisitor<? extends T>) visitor).visitConvertor(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConvertorContext convertor() throws RecognitionException {
        ConvertorContext _localctx = new ConvertorContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_convertor);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(78);
                match(IDENTIFIER);
                setState(79);
                match(T__0);
                setState(80);
                expr(0);
                setState(85);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__1) {
                    {
                        {
                            setState(81);
                            match(T__1);
                            setState(82);
                            expr(0);
                        }
                    }
                    setState(87);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(88);
                match(T__2);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class BracketContext extends ParserRuleContext {
        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public BracketContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_bracket;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterBracket(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitBracket(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CalculatorVisitor)
                return ((CalculatorVisitor<? extends T>) visitor).visitBracket(this);
            else return visitor.visitChildren(this);
        }
    }

    public final BracketContext bracket() throws RecognitionException {
        BracketContext _localctx = new BracketContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_bracket);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(90);
                match(T__0);
                setState(91);
                expr(0);
                setState(92);
                match(T__2);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class PrefixOperatorContext extends ParserRuleContext {
        public PrefixOperatorContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_prefixOperator;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterPrefixOperator(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitPrefixOperator(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CalculatorVisitor)
                return ((CalculatorVisitor<? extends T>) visitor).visitPrefixOperator(this);
            else return visitor.visitChildren(this);
        }
    }

    public final PrefixOperatorContext prefixOperator() throws RecognitionException {
        PrefixOperatorContext _localctx = new PrefixOperatorContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_prefixOperator);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(94);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 4194288L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class SuffixOperatorContext extends ParserRuleContext {
        public SuffixOperatorContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_suffixOperator;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterSuffixOperator(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitSuffixOperator(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CalculatorVisitor)
                return ((CalculatorVisitor<? extends T>) visitor).visitSuffixOperator(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SuffixOperatorContext suffixOperator() throws RecognitionException {
        SuffixOperatorContext _localctx = new SuffixOperatorContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_suffixOperator);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(96);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 29360128L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class OperatorV5Context extends ParserRuleContext {
        public OperatorV5Context(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_operatorV5;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterOperatorV5(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitOperatorV5(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CalculatorVisitor)
                return ((CalculatorVisitor<? extends T>) visitor).visitOperatorV5(this);
            else return visitor.visitChildren(this);
        }
    }

    public final OperatorV5Context operatorV5() throws RecognitionException {
        OperatorV5Context _localctx = new OperatorV5Context(_ctx, getState());
        enterRule(_localctx, 14, RULE_operatorV5);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(98);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 503316480L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class OperatorV4Context extends ParserRuleContext {
        public OperatorV4Context(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_operatorV4;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterOperatorV4(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitOperatorV4(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CalculatorVisitor)
                return ((CalculatorVisitor<? extends T>) visitor).visitOperatorV4(this);
            else return visitor.visitChildren(this);
        }
    }

    public final OperatorV4Context operatorV4() throws RecognitionException {
        OperatorV4Context _localctx = new OperatorV4Context(_ctx, getState());
        enterRule(_localctx, 16, RULE_operatorV4);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(100);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 1098974756864L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class OperatorV3Context extends ParserRuleContext {
        public OperatorV3Context(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_operatorV3;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterOperatorV3(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitOperatorV3(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CalculatorVisitor)
                return ((CalculatorVisitor<? extends T>) visitor).visitOperatorV3(this);
            else return visitor.visitChildren(this);
        }
    }

    public final OperatorV3Context operatorV3() throws RecognitionException {
        OperatorV3Context _localctx = new OperatorV3Context(_ctx, getState());
        enterRule(_localctx, 18, RULE_operatorV3);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(102);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 16492674416640L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class OperatorV2Context extends ParserRuleContext {
        public OperatorV2Context(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_operatorV2;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterOperatorV2(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitOperatorV2(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CalculatorVisitor)
                return ((CalculatorVisitor<? extends T>) visitor).visitOperatorV2(this);
            else return visitor.visitChildren(this);
        }
    }

    public final OperatorV2Context operatorV2() throws RecognitionException {
        OperatorV2Context _localctx = new OperatorV2Context(_ctx, getState());
        enterRule(_localctx, 20, RULE_operatorV2);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(104);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 545357775765504L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class OperatorV1Context extends ParserRuleContext {
        public OperatorV1Context(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_operatorV1;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterOperatorV1(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitOperatorV1(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CalculatorVisitor)
                return ((CalculatorVisitor<? extends T>) visitor).visitOperatorV1(this);
            else return visitor.visitChildren(this);
        }
    }

    public final OperatorV1Context operatorV1() throws RecognitionException {
        OperatorV1Context _localctx = new OperatorV1Context(_ctx, getState());
        enterRule(_localctx, 22, RULE_operatorV1);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(106);
                _la = _input.LA(1);
                if (!(_la == T__3 || _la == T__4)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    @SuppressWarnings("CheckReturnValue")
    public static class OperatorV0Context extends ParserRuleContext {
        public OperatorV0Context(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_operatorV0;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterOperatorV0(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitOperatorV0(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CalculatorVisitor)
                return ((CalculatorVisitor<? extends T>) visitor).visitOperatorV0(this);
            else return visitor.visitChildren(this);
        }
    }

    public final OperatorV0Context operatorV0() throws RecognitionException {
        OperatorV0Context _localctx = new OperatorV0Context(_ctx, getState());
        enterRule(_localctx, 24, RULE_operatorV0);
        try {
            setState(116);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T__0:
                case T__3:
                case T__4:
                case T__5:
                case T__6:
                case T__7:
                case T__8:
                case T__9:
                case T__10:
                case T__11:
                case T__12:
                case T__13:
                case T__14:
                case T__15:
                case T__16:
                case T__17:
                case T__18:
                case T__19:
                case T__20:
                case CONST:
                case DEC_NUMBER:
                case HEX_NUMBER:
                case OTC_NUMBER:
                case BIN_NUMBER:
                case HIGH_NUMBER:
                case IDENTIFIER:
                    enterOuterAlt(_localctx, 1);
                {
                }
                break;
                case T__48:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(109);
                    match(T__48);
                }
                break;
                case T__49:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(110);
                    match(T__49);
                }
                break;
                case T__50:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(111);
                    match(T__50);
                }
                break;
                case T__51:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(112);
                    match(T__51);
                }
                break;
                case T__52:
                    enterOuterAlt(_localctx, 6);
                {
                    setState(113);
                    match(T__52);
                }
                break;
                case T__53:
                    enterOuterAlt(_localctx, 7);
                {
                    setState(114);
                    match(T__53);
                }
                break;
                case T__54:
                    enterOuterAlt(_localctx, 8);
                {
                    setState(115);
                    match(T__54);
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
            case 2:
                return expr_sempred((ExprContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean expr_sempred(ExprContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return precpred(_ctx, 7);
            case 1:
                return precpred(_ctx, 6);
            case 2:
                return precpred(_ctx, 5);
            case 3:
                return precpred(_ctx, 4);
            case 4:
                return precpred(_ctx, 3);
            case 5:
                return precpred(_ctx, 2);
            case 6:
                return precpred(_ctx, 9);
        }
        return true;
    }

    public static final String _serializedATN =
            "\u0004\u0001Aw\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002" +
                    "\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005" +
                    "\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007" +
                    "\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002\f\u0007" +
                    "\f\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000" +
                    "\u0001\u0000\u0001\u0000\u0003\u0000#\b\u0000\u0001\u0001\u0001\u0001" +
                    "\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002" +
                    "\u0001\u0002\u0003\u0002.\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002" +
                    "\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002" +
                    "\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002" +
                    "\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002" +
                    "\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002" +
                    "J\b\u0002\n\u0002\f\u0002M\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003" +
                    "\u0001\u0003\u0001\u0003\u0005\u0003T\b\u0003\n\u0003\f\u0003W\t\u0003" +
                    "\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004" +
                    "\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007" +
                    "\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b" +
                    "\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003" +
                    "\fu\b\f\u0001\f\u0000\u0001\u0004\r\u0000\u0002\u0004\u0006\b\n\f\u000e" +
                    "\u0010\u0012\u0014\u0016\u0018\u0000\b\u0001\u00009>\u0001\u0000\u0004" +
                    "\u0015\u0001\u0000\u0016\u0018\u0001\u0000\u0019\u001c\u0001\u0000\u001d" +
                    "\'\u0001\u0000(+\u0002\u0000\u0017\u0017,0\u0001\u0000\u0004\u0005~\u0000" +
                    "\"\u0001\u0000\u0000\u0000\u0002$\u0001\u0000\u0000\u0000\u0004-\u0001" +
                    "\u0000\u0000\u0000\u0006N\u0001\u0000\u0000\u0000\bZ\u0001\u0000\u0000" +
                    "\u0000\n^\u0001\u0000\u0000\u0000\f`\u0001\u0000\u0000\u0000\u000eb\u0001" +
                    "\u0000\u0000\u0000\u0010d\u0001\u0000\u0000\u0000\u0012f\u0001\u0000\u0000" +
                    "\u0000\u0014h\u0001\u0000\u0000\u0000\u0016j\u0001\u0000\u0000\u0000\u0018" +
                    "t\u0001\u0000\u0000\u0000\u001a#\u0003\u0002\u0001\u0000\u001b#\u0003" +
                    "\u0004\u0002\u0000\u001c\u001d\u0003\u0002\u0001\u0000\u001d\u001e\u0005" +
                    "8\u0000\u0000\u001e#\u0001\u0000\u0000\u0000\u001f \u0003\u0004\u0002" +
                    "\u0000 !\u00058\u0000\u0000!#\u0001\u0000\u0000\u0000\"\u001a\u0001\u0000" +
                    "\u0000\u0000\"\u001b\u0001\u0000\u0000\u0000\"\u001c\u0001\u0000\u0000" +
                    "\u0000\"\u001f\u0001\u0000\u0000\u0000#\u0001\u0001\u0000\u0000\u0000" +
                    "$%\u0007\u0000\u0000\u0000%\u0003\u0001\u0000\u0000\u0000&\'\u0006\u0002" +
                    "\uffff\uffff\u0000\'.\u0003\b\u0004\u0000(.\u0003\u0006\u0003\u0000)*" +
                    "\u0003\n\u0005\u0000*+\u0003\u0004\u0002\b+.\u0001\u0000\u0000\u0000," +
                    ".\u0003\u0002\u0001\u0000-&\u0001\u0000\u0000\u0000-(\u0001\u0000\u0000" +
                    "\u0000-)\u0001\u0000\u0000\u0000-,\u0001\u0000\u0000\u0000.K\u0001\u0000" +
                    "\u0000\u0000/0\n\u0007\u0000\u000001\u0003\u000e\u0007\u000012\u0003\u0004" +
                    "\u0002\b2J\u0001\u0000\u0000\u000034\n\u0006\u0000\u000045\u0003\u0010" +
                    "\b\u000056\u0003\u0004\u0002\u00076J\u0001\u0000\u0000\u000078\n\u0005" +
                    "\u0000\u000089\u0003\u0012\t\u00009:\u0003\u0004\u0002\u0006:J\u0001\u0000" +
                    "\u0000\u0000;<\n\u0004\u0000\u0000<=\u0003\u0014\n\u0000=>\u0003\u0004" +
                    "\u0002\u0005>J\u0001\u0000\u0000\u0000?@\n\u0003\u0000\u0000@A\u0003\u0016" +
                    "\u000b\u0000AB\u0003\u0004\u0002\u0004BJ\u0001\u0000\u0000\u0000CD\n\u0002" +
                    "\u0000\u0000DE\u0003\u0018\f\u0000EF\u0003\u0004\u0002\u0003FJ\u0001\u0000" +
                    "\u0000\u0000GH\n\t\u0000\u0000HJ\u0003\f\u0006\u0000I/\u0001\u0000\u0000" +
                    "\u0000I3\u0001\u0000\u0000\u0000I7\u0001\u0000\u0000\u0000I;\u0001\u0000" +
                    "\u0000\u0000I?\u0001\u0000\u0000\u0000IC\u0001\u0000\u0000\u0000IG\u0001" +
                    "\u0000\u0000\u0000JM\u0001\u0000\u0000\u0000KI\u0001\u0000\u0000\u0000" +
                    "KL\u0001\u0000\u0000\u0000L\u0005\u0001\u0000\u0000\u0000MK\u0001\u0000" +
                    "\u0000\u0000NO\u0005?\u0000\u0000OP\u0005\u0001\u0000\u0000PU\u0003\u0004" +
                    "\u0002\u0000QR\u0005\u0002\u0000\u0000RT\u0003\u0004\u0002\u0000SQ\u0001" +
                    "\u0000\u0000\u0000TW\u0001\u0000\u0000\u0000US\u0001\u0000\u0000\u0000" +
                    "UV\u0001\u0000\u0000\u0000VX\u0001\u0000\u0000\u0000WU\u0001\u0000\u0000" +
                    "\u0000XY\u0005\u0003\u0000\u0000Y\u0007\u0001\u0000\u0000\u0000Z[\u0005" +
                    "\u0001\u0000\u0000[\\\u0003\u0004\u0002\u0000\\]\u0005\u0003\u0000\u0000" +
                    "]\t\u0001\u0000\u0000\u0000^_\u0007\u0001\u0000\u0000_\u000b\u0001\u0000" +
                    "\u0000\u0000`a\u0007\u0002\u0000\u0000a\r\u0001\u0000\u0000\u0000bc\u0007" +
                    "\u0003\u0000\u0000c\u000f\u0001\u0000\u0000\u0000de\u0007\u0004\u0000" +
                    "\u0000e\u0011\u0001\u0000\u0000\u0000fg\u0007\u0005\u0000\u0000g\u0013" +
                    "\u0001\u0000\u0000\u0000hi\u0007\u0006\u0000\u0000i\u0015\u0001\u0000" +
                    "\u0000\u0000jk\u0007\u0007\u0000\u0000k\u0017\u0001\u0000\u0000\u0000" +
                    "lu\u0001\u0000\u0000\u0000mu\u00051\u0000\u0000nu\u00052\u0000\u0000o" +
                    "u\u00053\u0000\u0000pu\u00054\u0000\u0000qu\u00055\u0000\u0000ru\u0005" +
                    "6\u0000\u0000su\u00057\u0000\u0000tl\u0001\u0000\u0000\u0000tm\u0001\u0000" +
                    "\u0000\u0000tn\u0001\u0000\u0000\u0000to\u0001\u0000\u0000\u0000tp\u0001" +
                    "\u0000\u0000\u0000tq\u0001\u0000\u0000\u0000tr\u0001\u0000\u0000\u0000" +
                    "ts\u0001\u0000\u0000\u0000u\u0019\u0001\u0000\u0000\u0000\u0006\"-IKU" +
                    "t";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}