// Generated from C:/home/dev/java/dev-center/i2f-turbo-java/i2f-extension/i2f-extension-antlr4/src/main/java/i2f/extension/antlr4/script/tiny/rule/TinyScript.g4 by ANTLR 4.13.2

package i2f.extension.antlr4.script.tiny;

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
public class TinyScriptParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, TYPE_BOOL = 4, STRING = 5, QUOTE = 6, ESCAPED_CHAR = 7,
            NAMING = 8, ID = 9, DOUBLE_OPERAOTR = 10, LPAREN = 11, RPAREN = 12, COMMA = 13, DOT = 14,
            DOLLAR = 15, LCURLY = 16, RCURLY = 17, WS = 18, REF_EXPRESS = 19, DIGIT = 20, HEX_LETTER = 21,
            OTC_LETTER = 22, BIN_LETTER = 23, INT_NUM = 24, FLOAT_NUM = 25, CH_E = 26, SCIEN_NUM_1 = 27,
            SCIEN_NUM_2 = 28, CH_0X = 29, TYPE_HEX_NUMBER = 30, CH_0T = 31, TYPE_OTC_NUMBER = 32,
            CH_0B = 33, TYPE_BIN_NUMBER = 34;
    public static final int
            RULE_script = 0, RULE_express = 1, RULE_equalValue = 2, RULE_newInstance = 3,
            RULE_invokeFunction = 4, RULE_functionCall = 5, RULE_refCall = 6, RULE_argumentList = 7,
            RULE_argument = 8, RULE_constValue = 9, RULE_refValue = 10, RULE_constBool = 11,
            RULE_constString = 12, RULE_decNumber = 13, RULE_hexNumber = 14, RULE_otcNumber = 15,
            RULE_binNumber = 16;

    private static String[] makeRuleNames() {
        return new String[]{
                "script", "express", "equalValue", "newInstance", "invokeFunction", "functionCall",
                "refCall", "argumentList", "argument", "constValue", "refValue", "constBool",
                "constString", "decNumber", "hexNumber", "otcNumber", "binNumber"
        };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[]{
                null, "';'", "'='", "'new'", null, null, "'\"'", null, null, null, null,
                "'('", "')'", "','", "'.'", "'$'", "'{'", "'}'"
        };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[]{
                null, null, null, null, "TYPE_BOOL", "STRING", "QUOTE", "ESCAPED_CHAR",
                "NAMING", "ID", "DOUBLE_OPERAOTR", "LPAREN", "RPAREN", "COMMA", "DOT",
                "DOLLAR", "LCURLY", "RCURLY", "WS", "REF_EXPRESS", "DIGIT", "HEX_LETTER",
                "OTC_LETTER", "BIN_LETTER", "INT_NUM", "FLOAT_NUM", "CH_E", "SCIEN_NUM_1",
                "SCIEN_NUM_2", "CH_0X", "TYPE_HEX_NUMBER", "CH_0T", "TYPE_OTC_NUMBER",
                "CH_0B", "TYPE_BIN_NUMBER"
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
        return "TinyScript.g4";
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

    public TinyScriptParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ScriptContext extends ParserRuleContext {
        public List<ExpressContext> express() {
            return getRuleContexts(ExpressContext.class);
        }

        public ExpressContext express(int i) {
            return getRuleContext(ExpressContext.class, i);
        }

        public ScriptContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_script;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterScript(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitScript(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitScript(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ScriptContext script() throws RecognitionException {
        ScriptContext _localctx = new ScriptContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_script);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(34);
                express(0);
                setState(41);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__0) {
                    {
                        {
                            setState(35);
                            match(T__0);
                            setState(37);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 23003136312L) != 0)) {
                                {
                                    setState(36);
                                    express(0);
                                }
                            }

                        }
                    }
                    setState(43);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
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
    public static class ExpressContext extends ParserRuleContext {
        public EqualValueContext equalValue() {
            return getRuleContext(EqualValueContext.class, 0);
        }

        public NewInstanceContext newInstance() {
            return getRuleContext(NewInstanceContext.class, 0);
        }

        public InvokeFunctionContext invokeFunction() {
            return getRuleContext(InvokeFunctionContext.class, 0);
        }

        public ConstValueContext constValue() {
            return getRuleContext(ConstValueContext.class, 0);
        }

        public RefValueContext refValue() {
            return getRuleContext(RefValueContext.class, 0);
        }

        public List<ExpressContext> express() {
            return getRuleContexts(ExpressContext.class);
        }

        public ExpressContext express(int i) {
            return getRuleContext(ExpressContext.class, i);
        }

        public TerminalNode DOUBLE_OPERAOTR() {
            return getToken(TinyScriptParser.DOUBLE_OPERAOTR, 0);
        }

        public ExpressContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_express;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterExpress(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitExpress(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitExpress(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExpressContext express() throws RecognitionException {
        return express(0);
    }

    private ExpressContext express(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        ExpressContext _localctx = new ExpressContext(_ctx, _parentState);
        ExpressContext _prevctx = _localctx;
        int _startState = 2;
        enterRecursionRule(_localctx, 2, RULE_express, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(50);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 2, _ctx)) {
                    case 1: {
                        setState(45);
                        equalValue();
                    }
                    break;
                    case 2: {
                        setState(46);
                        newInstance();
                    }
                    break;
                    case 3: {
                        setState(47);
                        invokeFunction();
                    }
                    break;
                    case 4: {
                        setState(48);
                        constValue();
                    }
                    break;
                    case 5: {
                        setState(49);
                        refValue();
                    }
                    break;
                }
                _ctx.stop = _input.LT(-1);
                setState(57);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 3, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            {
                                _localctx = new ExpressContext(_parentctx, _parentState);
                                pushNewRecursionContext(_localctx, _startState, RULE_express);
                                setState(52);
                                if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
                                setState(53);
                                match(DOUBLE_OPERAOTR);
                                setState(54);
                                express(4);
                            }
                        }
                    }
                    setState(59);
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
    public static class EqualValueContext extends ParserRuleContext {
        public TerminalNode NAMING() {
            return getToken(TinyScriptParser.NAMING, 0);
        }

        public ExpressContext express() {
            return getRuleContext(ExpressContext.class, 0);
        }

        public EqualValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_equalValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterEqualValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitEqualValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitEqualValue(this);
            else return visitor.visitChildren(this);
        }
    }

    public final EqualValueContext equalValue() throws RecognitionException {
        EqualValueContext _localctx = new EqualValueContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_equalValue);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(60);
                match(NAMING);
                setState(61);
                match(T__1);
                setState(62);
                express(0);
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
    public static class NewInstanceContext extends ParserRuleContext {
        public InvokeFunctionContext invokeFunction() {
            return getRuleContext(InvokeFunctionContext.class, 0);
        }

        public NewInstanceContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_newInstance;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterNewInstance(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitNewInstance(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitNewInstance(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NewInstanceContext newInstance() throws RecognitionException {
        NewInstanceContext _localctx = new NewInstanceContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_newInstance);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(64);
                match(T__2);
                setState(65);
                invokeFunction();
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
    public static class InvokeFunctionContext extends ParserRuleContext {
        public RefCallContext refCall() {
            return getRuleContext(RefCallContext.class, 0);
        }

        public List<TerminalNode> DOT() {
            return getTokens(TinyScriptParser.DOT);
        }

        public TerminalNode DOT(int i) {
            return getToken(TinyScriptParser.DOT, i);
        }

        public List<FunctionCallContext> functionCall() {
            return getRuleContexts(FunctionCallContext.class);
        }

        public FunctionCallContext functionCall(int i) {
            return getRuleContext(FunctionCallContext.class, i);
        }

        public InvokeFunctionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_invokeFunction;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterInvokeFunction(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitInvokeFunction(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitInvokeFunction(this);
            else return visitor.visitChildren(this);
        }
    }

    public final InvokeFunctionContext invokeFunction() throws RecognitionException {
        InvokeFunctionContext _localctx = new InvokeFunctionContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_invokeFunction);
        try {
            int _alt;
            setState(83);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case REF_EXPRESS:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(67);
                    refCall();
                    setState(72);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 4, _ctx);
                    while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                        if (_alt == 1) {
                            {
                                {
                                    setState(68);
                                    match(DOT);
                                    setState(69);
                                    functionCall();
                                }
                            }
                        }
                        setState(74);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 4, _ctx);
                    }
                }
                break;
                case NAMING:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(75);
                    functionCall();
                    setState(80);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
                    while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                        if (_alt == 1) {
                            {
                                {
                                    setState(76);
                                    match(DOT);
                                    setState(77);
                                    functionCall();
                                }
                            }
                        }
                        setState(82);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
                    }
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

    @SuppressWarnings("CheckReturnValue")
    public static class FunctionCallContext extends ParserRuleContext {
        public TerminalNode NAMING() {
            return getToken(TinyScriptParser.NAMING, 0);
        }

        public TerminalNode LPAREN() {
            return getToken(TinyScriptParser.LPAREN, 0);
        }

        public TerminalNode RPAREN() {
            return getToken(TinyScriptParser.RPAREN, 0);
        }

        public ArgumentListContext argumentList() {
            return getRuleContext(ArgumentListContext.class, 0);
        }

        public FunctionCallContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_functionCall;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterFunctionCall(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitFunctionCall(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitFunctionCall(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FunctionCallContext functionCall() throws RecognitionException {
        FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_functionCall);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(85);
                match(NAMING);
                setState(86);
                match(LPAREN);
                setState(88);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 23003136304L) != 0)) {
                    {
                        setState(87);
                        argumentList();
                    }
                }

                setState(90);
                match(RPAREN);
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
    public static class RefCallContext extends ParserRuleContext {
        public RefValueContext refValue() {
            return getRuleContext(RefValueContext.class, 0);
        }

        public TerminalNode DOT() {
            return getToken(TinyScriptParser.DOT, 0);
        }

        public FunctionCallContext functionCall() {
            return getRuleContext(FunctionCallContext.class, 0);
        }

        public RefCallContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_refCall;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterRefCall(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitRefCall(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitRefCall(this);
            else return visitor.visitChildren(this);
        }
    }

    public final RefCallContext refCall() throws RecognitionException {
        RefCallContext _localctx = new RefCallContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_refCall);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(92);
                refValue();
                setState(93);
                match(DOT);
                setState(94);
                functionCall();
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
    public static class ArgumentListContext extends ParserRuleContext {
        public List<ArgumentContext> argument() {
            return getRuleContexts(ArgumentContext.class);
        }

        public ArgumentContext argument(int i) {
            return getRuleContext(ArgumentContext.class, i);
        }

        public List<TerminalNode> COMMA() {
            return getTokens(TinyScriptParser.COMMA);
        }

        public TerminalNode COMMA(int i) {
            return getToken(TinyScriptParser.COMMA, i);
        }

        public ArgumentListContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_argumentList;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterArgumentList(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitArgumentList(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitArgumentList(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ArgumentListContext argumentList() throws RecognitionException {
        ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_argumentList);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(96);
                argument();
                setState(101);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(97);
                            match(COMMA);
                            setState(98);
                            argument();
                        }
                    }
                    setState(103);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
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
    public static class ArgumentContext extends ParserRuleContext {
        public InvokeFunctionContext invokeFunction() {
            return getRuleContext(InvokeFunctionContext.class, 0);
        }

        public ConstValueContext constValue() {
            return getRuleContext(ConstValueContext.class, 0);
        }

        public RefValueContext refValue() {
            return getRuleContext(RefValueContext.class, 0);
        }

        public ArgumentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_argument;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterArgument(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitArgument(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitArgument(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ArgumentContext argument() throws RecognitionException {
        ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_argument);
        try {
            setState(107);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 9, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(104);
                    invokeFunction();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(105);
                    constValue();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(106);
                    refValue();
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
    public static class ConstValueContext extends ParserRuleContext {
        public ConstBoolContext constBool() {
            return getRuleContext(ConstBoolContext.class, 0);
        }

        public ConstStringContext constString() {
            return getRuleContext(ConstStringContext.class, 0);
        }

        public DecNumberContext decNumber() {
            return getRuleContext(DecNumberContext.class, 0);
        }

        public HexNumberContext hexNumber() {
            return getRuleContext(HexNumberContext.class, 0);
        }

        public OtcNumberContext otcNumber() {
            return getRuleContext(OtcNumberContext.class, 0);
        }

        public BinNumberContext binNumber() {
            return getRuleContext(BinNumberContext.class, 0);
        }

        public ConstValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_constValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterConstValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitConstValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitConstValue(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConstValueContext constValue() throws RecognitionException {
        ConstValueContext _localctx = new ConstValueContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_constValue);
        try {
            setState(115);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case TYPE_BOOL:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(109);
                    constBool();
                }
                break;
                case STRING:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(110);
                    constString();
                }
                break;
                case DIGIT:
                case INT_NUM:
                case FLOAT_NUM:
                case SCIEN_NUM_1:
                case SCIEN_NUM_2:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(111);
                    decNumber();
                }
                break;
                case TYPE_HEX_NUMBER:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(112);
                    hexNumber();
                }
                break;
                case TYPE_OTC_NUMBER:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(113);
                    otcNumber();
                }
                break;
                case TYPE_BIN_NUMBER:
                    enterOuterAlt(_localctx, 6);
                {
                    setState(114);
                    binNumber();
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

    @SuppressWarnings("CheckReturnValue")
    public static class RefValueContext extends ParserRuleContext {
        public TerminalNode REF_EXPRESS() {
            return getToken(TinyScriptParser.REF_EXPRESS, 0);
        }

        public RefValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_refValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterRefValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitRefValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitRefValue(this);
            else return visitor.visitChildren(this);
        }
    }

    public final RefValueContext refValue() throws RecognitionException {
        RefValueContext _localctx = new RefValueContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_refValue);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(117);
                match(REF_EXPRESS);
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
    public static class ConstBoolContext extends ParserRuleContext {
        public TerminalNode TYPE_BOOL() {
            return getToken(TinyScriptParser.TYPE_BOOL, 0);
        }

        public ConstBoolContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_constBool;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterConstBool(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitConstBool(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitConstBool(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConstBoolContext constBool() throws RecognitionException {
        ConstBoolContext _localctx = new ConstBoolContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_constBool);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(119);
                match(TYPE_BOOL);
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
    public static class ConstStringContext extends ParserRuleContext {
        public TerminalNode STRING() {
            return getToken(TinyScriptParser.STRING, 0);
        }

        public ConstStringContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_constString;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterConstString(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitConstString(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitConstString(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConstStringContext constString() throws RecognitionException {
        ConstStringContext _localctx = new ConstStringContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_constString);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(121);
                match(STRING);
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
    public static class DecNumberContext extends ParserRuleContext {
        public TerminalNode SCIEN_NUM_2() {
            return getToken(TinyScriptParser.SCIEN_NUM_2, 0);
        }

        public TerminalNode SCIEN_NUM_1() {
            return getToken(TinyScriptParser.SCIEN_NUM_1, 0);
        }

        public TerminalNode FLOAT_NUM() {
            return getToken(TinyScriptParser.FLOAT_NUM, 0);
        }

        public TerminalNode INT_NUM() {
            return getToken(TinyScriptParser.INT_NUM, 0);
        }

        public TerminalNode DIGIT() {
            return getToken(TinyScriptParser.DIGIT, 0);
        }

        public DecNumberContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_decNumber;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterDecNumber(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitDecNumber(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitDecNumber(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DecNumberContext decNumber() throws RecognitionException {
        DecNumberContext _localctx = new DecNumberContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_decNumber);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(123);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 454033408L) != 0))) {
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
    public static class HexNumberContext extends ParserRuleContext {
        public TerminalNode TYPE_HEX_NUMBER() {
            return getToken(TinyScriptParser.TYPE_HEX_NUMBER, 0);
        }

        public HexNumberContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_hexNumber;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterHexNumber(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitHexNumber(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitHexNumber(this);
            else return visitor.visitChildren(this);
        }
    }

    public final HexNumberContext hexNumber() throws RecognitionException {
        HexNumberContext _localctx = new HexNumberContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_hexNumber);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(125);
                match(TYPE_HEX_NUMBER);
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
    public static class OtcNumberContext extends ParserRuleContext {
        public TerminalNode TYPE_OTC_NUMBER() {
            return getToken(TinyScriptParser.TYPE_OTC_NUMBER, 0);
        }

        public OtcNumberContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_otcNumber;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterOtcNumber(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitOtcNumber(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitOtcNumber(this);
            else return visitor.visitChildren(this);
        }
    }

    public final OtcNumberContext otcNumber() throws RecognitionException {
        OtcNumberContext _localctx = new OtcNumberContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_otcNumber);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(127);
                match(TYPE_OTC_NUMBER);
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
    public static class BinNumberContext extends ParserRuleContext {
        public TerminalNode TYPE_BIN_NUMBER() {
            return getToken(TinyScriptParser.TYPE_BIN_NUMBER, 0);
        }

        public BinNumberContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_binNumber;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterBinNumber(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitBinNumber(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitBinNumber(this);
            else return visitor.visitChildren(this);
        }
    }

    public final BinNumberContext binNumber() throws RecognitionException {
        BinNumberContext _localctx = new BinNumberContext(_ctx, getState());
        enterRule(_localctx, 32, RULE_binNumber);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(129);
                match(TYPE_BIN_NUMBER);
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
            case 1:
                return express_sempred((ExpressContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean express_sempred(ExpressContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return precpred(_ctx, 3);
        }
        return true;
    }

    public static final String _serializedATN =
            "\u0004\u0001\"\u0084\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002" +
                    "\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002" +
                    "\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002" +
                    "\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002" +
                    "\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f" +
                    "\u0002\u0010\u0007\u0010\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000" +
                    "&\b\u0000\u0005\u0000(\b\u0000\n\u0000\f\u0000+\t\u0000\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u00013\b" +
                    "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u00018\b\u0001\n\u0001" +
                    "\f\u0001;\t\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001" +
                    "\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0005" +
                    "\u0004G\b\u0004\n\u0004\f\u0004J\t\u0004\u0001\u0004\u0001\u0004\u0001" +
                    "\u0004\u0005\u0004O\b\u0004\n\u0004\f\u0004R\t\u0004\u0003\u0004T\b\u0004" +
                    "\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005Y\b\u0005\u0001\u0005" +
                    "\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007" +
                    "\u0001\u0007\u0001\u0007\u0005\u0007d\b\u0007\n\u0007\f\u0007g\t\u0007" +
                    "\u0001\b\u0001\b\u0001\b\u0003\bl\b\b\u0001\t\u0001\t\u0001\t\u0001\t" +
                    "\u0001\t\u0001\t\u0003\tt\b\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b" +
                    "\u0001\f\u0001\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001" +
                    "\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0000\u0001\u0002\u0011\u0000" +
                    "\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c" +
                    "\u001e \u0000\u0001\u0003\u0000\u0014\u0014\u0018\u0019\u001b\u001c\u0085" +
                    "\u0000\"\u0001\u0000\u0000\u0000\u00022\u0001\u0000\u0000\u0000\u0004" +
                    "<\u0001\u0000\u0000\u0000\u0006@\u0001\u0000\u0000\u0000\bS\u0001\u0000" +
                    "\u0000\u0000\nU\u0001\u0000\u0000\u0000\f\\\u0001\u0000\u0000\u0000\u000e" +
                    "`\u0001\u0000\u0000\u0000\u0010k\u0001\u0000\u0000\u0000\u0012s\u0001" +
                    "\u0000\u0000\u0000\u0014u\u0001\u0000\u0000\u0000\u0016w\u0001\u0000\u0000" +
                    "\u0000\u0018y\u0001\u0000\u0000\u0000\u001a{\u0001\u0000\u0000\u0000\u001c" +
                    "}\u0001\u0000\u0000\u0000\u001e\u007f\u0001\u0000\u0000\u0000 \u0081\u0001" +
                    "\u0000\u0000\u0000\")\u0003\u0002\u0001\u0000#%\u0005\u0001\u0000\u0000" +
                    "$&\u0003\u0002\u0001\u0000%$\u0001\u0000\u0000\u0000%&\u0001\u0000\u0000" +
                    "\u0000&(\u0001\u0000\u0000\u0000\'#\u0001\u0000\u0000\u0000(+\u0001\u0000" +
                    "\u0000\u0000)\'\u0001\u0000\u0000\u0000)*\u0001\u0000\u0000\u0000*\u0001" +
                    "\u0001\u0000\u0000\u0000+)\u0001\u0000\u0000\u0000,-\u0006\u0001\uffff" +
                    "\uffff\u0000-3\u0003\u0004\u0002\u0000.3\u0003\u0006\u0003\u0000/3\u0003" +
                    "\b\u0004\u000003\u0003\u0012\t\u000013\u0003\u0014\n\u00002,\u0001\u0000" +
                    "\u0000\u00002.\u0001\u0000\u0000\u00002/\u0001\u0000\u0000\u000020\u0001" +
                    "\u0000\u0000\u000021\u0001\u0000\u0000\u000039\u0001\u0000\u0000\u0000" +
                    "45\n\u0003\u0000\u000056\u0005\n\u0000\u000068\u0003\u0002\u0001\u0004" +
                    "74\u0001\u0000\u0000\u00008;\u0001\u0000\u0000\u000097\u0001\u0000\u0000" +
                    "\u00009:\u0001\u0000\u0000\u0000:\u0003\u0001\u0000\u0000\u0000;9\u0001" +
                    "\u0000\u0000\u0000<=\u0005\b\u0000\u0000=>\u0005\u0002\u0000\u0000>?\u0003" +
                    "\u0002\u0001\u0000?\u0005\u0001\u0000\u0000\u0000@A\u0005\u0003\u0000" +
                    "\u0000AB\u0003\b\u0004\u0000B\u0007\u0001\u0000\u0000\u0000CH\u0003\f" +
                    "\u0006\u0000DE\u0005\u000e\u0000\u0000EG\u0003\n\u0005\u0000FD\u0001\u0000" +
                    "\u0000\u0000GJ\u0001\u0000\u0000\u0000HF\u0001\u0000\u0000\u0000HI\u0001" +
                    "\u0000\u0000\u0000IT\u0001\u0000\u0000\u0000JH\u0001\u0000\u0000\u0000" +
                    "KP\u0003\n\u0005\u0000LM\u0005\u000e\u0000\u0000MO\u0003\n\u0005\u0000" +
                    "NL\u0001\u0000\u0000\u0000OR\u0001\u0000\u0000\u0000PN\u0001\u0000\u0000" +
                    "\u0000PQ\u0001\u0000\u0000\u0000QT\u0001\u0000\u0000\u0000RP\u0001\u0000" +
                    "\u0000\u0000SC\u0001\u0000\u0000\u0000SK\u0001\u0000\u0000\u0000T\t\u0001" +
                    "\u0000\u0000\u0000UV\u0005\b\u0000\u0000VX\u0005\u000b\u0000\u0000WY\u0003" +
                    "\u000e\u0007\u0000XW\u0001\u0000\u0000\u0000XY\u0001\u0000\u0000\u0000" +
                    "YZ\u0001\u0000\u0000\u0000Z[\u0005\f\u0000\u0000[\u000b\u0001\u0000\u0000" +
                    "\u0000\\]\u0003\u0014\n\u0000]^\u0005\u000e\u0000\u0000^_\u0003\n\u0005" +
                    "\u0000_\r\u0001\u0000\u0000\u0000`e\u0003\u0010\b\u0000ab\u0005\r\u0000" +
                    "\u0000bd\u0003\u0010\b\u0000ca\u0001\u0000\u0000\u0000dg\u0001\u0000\u0000" +
                    "\u0000ec\u0001\u0000\u0000\u0000ef\u0001\u0000\u0000\u0000f\u000f\u0001" +
                    "\u0000\u0000\u0000ge\u0001\u0000\u0000\u0000hl\u0003\b\u0004\u0000il\u0003" +
                    "\u0012\t\u0000jl\u0003\u0014\n\u0000kh\u0001\u0000\u0000\u0000ki\u0001" +
                    "\u0000\u0000\u0000kj\u0001\u0000\u0000\u0000l\u0011\u0001\u0000\u0000" +
                    "\u0000mt\u0003\u0016\u000b\u0000nt\u0003\u0018\f\u0000ot\u0003\u001a\r" +
                    "\u0000pt\u0003\u001c\u000e\u0000qt\u0003\u001e\u000f\u0000rt\u0003 \u0010" +
                    "\u0000sm\u0001\u0000\u0000\u0000sn\u0001\u0000\u0000\u0000so\u0001\u0000" +
                    "\u0000\u0000sp\u0001\u0000\u0000\u0000sq\u0001\u0000\u0000\u0000sr\u0001" +
                    "\u0000\u0000\u0000t\u0013\u0001\u0000\u0000\u0000uv\u0005\u0013\u0000" +
                    "\u0000v\u0015\u0001\u0000\u0000\u0000wx\u0005\u0004\u0000\u0000x\u0017" +
                    "\u0001\u0000\u0000\u0000yz\u0005\u0005\u0000\u0000z\u0019\u0001\u0000" +
                    "\u0000\u0000{|\u0007\u0000\u0000\u0000|\u001b\u0001\u0000\u0000\u0000" +
                    "}~\u0005\u001e\u0000\u0000~\u001d\u0001\u0000\u0000\u0000\u007f\u0080" +
                    "\u0005 \u0000\u0000\u0080\u001f\u0001\u0000\u0000\u0000\u0081\u0082\u0005" +
                    "\"\u0000\u0000\u0082!\u0001\u0000\u0000\u0000\u000b%)29HPSXeks";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}