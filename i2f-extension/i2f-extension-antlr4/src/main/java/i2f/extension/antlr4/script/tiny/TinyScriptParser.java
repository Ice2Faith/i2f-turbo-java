// Generated from /extension/antlr4/script/tiny/rule/TinyScript.g4 by ANTLR 4.13.2

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
            T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
            T__9 = 10, T__10 = 11, T__11 = 12, T__12 = 13, T__13 = 14, T__14 = 15, T__15 = 16, T__16 = 17,
            T__17 = 18, T__18 = 19, T__19 = 20, T__20 = 21, T__21 = 22, T__22 = 23, T__23 = 24,
            T__24 = 25, T__25 = 26, T__26 = 27, T__27 = 28, T__28 = 29, T__29 = 30, T__30 = 31,
            T__31 = 32, T__32 = 33, T__33 = 34, T__34 = 35, T__35 = 36, T__36 = 37, T__37 = 38,
            T__38 = 39, T__39 = 40, T__40 = 41, T__41 = 42, T__42 = 43, T__43 = 44, T__44 = 45,
            T__45 = 46, T__46 = 47, T__47 = 48, T__48 = 49, T__49 = 50, T__50 = 51, T__51 = 52,
            T__52 = 53, T__53 = 54, T__54 = 55, T__55 = 56, T__56 = 57, T__57 = 58, T__58 = 59,
            TERM_COMMENT_SINGLE_LINE = 60, TERM_COMMENT_MULTI_LINE = 61, TERM_CONST_STRING_MULTILINE = 62,
            TERM_CONST_STRING_MULTILINE_QUOTE = 63, TERM_CONST_STRING_RENDER = 64, TERM_CONST_STRING_RENDER_SINGLE = 65,
            TERM_CONST_STRING = 66, TERM_CONST_STRING_SINGLE = 67, TERM_CONST_BOOLEAN = 68,
            TERM_CONST_NULL = 69, TERM_CONST_TYPE_CLASS = 70, REF_EXPRESS = 71, TERM_CONST_NUMBER_SCIEN_2 = 72,
            TERM_CONST_NUMBER_SCIEN_1 = 73, TERM_CONST_NUMBER_FLOAT = 74, TERM_CONST_NUMBER = 75,
            TERM_CONST_NUMBER_HEX = 76, TERM_CONST_NUMBER_OTC = 77, TERM_CONST_NUMBER_BIN = 78,
            TERM_INTEGER = 79, NAMING = 80, ROUTE_NAMING = 81, ID = 82, TERM_QUOTE = 83, ESCAPED_CHAR = 84,
            TERM_PAREN_L = 85, TERM_PAREN_R = 86, TERM_COMMA = 87, TERM_DOT = 88, TERM_DOLLAR = 89,
            TERM_CURLY_L = 90, TERM_CURLY_R = 91, TERM_SEMICOLON = 92, TERM_COLON = 93, TERM_BRACKET_SQUARE_L = 94,
            TERM_BRACKET_SQUARE_R = 95, CH_E = 96, CH_0X = 97, CH_0T = 98, CH_0B = 99, TERM_DIGIT = 100,
            TERM_HEX_LETTER = 101, TERM_OTC_LETTER = 102, TERM_BIN_LETTER = 103, WS = 104;
    public static final int
            RULE_script = 0, RULE_segments = 1, RULE_express = 2, RULE_negtiveSegment = 3,
            RULE_debuggerSegment = 4, RULE_trySegment = 5, RULE_throwSegment = 6,
            RULE_tryBodyBlock = 7, RULE_catchBodyBlock = 8, RULE_finallyBodyBlock = 9,
            RULE_classNameBlock = 10, RULE_parenSegment = 11, RULE_controlSegment = 12,
            RULE_whileSegment = 13, RULE_forSegment = 14, RULE_foreachSegment = 15,
            RULE_namingBlock = 16, RULE_ifSegment = 17, RULE_conditionBlock = 18,
            RULE_scriptBlock = 19, RULE_equalValue = 20, RULE_extractExpress = 21,
            RULE_extractPairs = 22, RULE_extractPair = 23, RULE_staticEnumValue = 24,
            RULE_newInstance = 25, RULE_invokeFunction = 26, RULE_functionCall = 27,
            RULE_refCall = 28, RULE_argumentList = 29, RULE_argument = 30, RULE_argumentValue = 31,
            RULE_constValue = 32, RULE_refValue = 33, RULE_constBool = 34, RULE_constNull = 35,
            RULE_constClass = 36, RULE_constString = 37, RULE_constMultilineString = 38,
            RULE_constRenderString = 39, RULE_decNumber = 40, RULE_hexNumber = 41,
            RULE_otcNumber = 42, RULE_binNumber = 43, RULE_jsonValue = 44, RULE_jsonMapValue = 45,
            RULE_jsonPairs = 46, RULE_jsonPair = 47, RULE_jsonArrayValue = 48, RULE_jsonItemList = 49;

    private static String[] makeRuleNames() {
        return new String[]{
                "script", "segments", "express", "negtiveSegment", "debuggerSegment",
                "trySegment", "throwSegment", "tryBodyBlock", "catchBodyBlock", "finallyBodyBlock",
                "classNameBlock", "parenSegment", "controlSegment", "whileSegment", "forSegment",
                "foreachSegment", "namingBlock", "ifSegment", "conditionBlock", "scriptBlock",
                "equalValue", "extractExpress", "extractPairs", "extractPair", "staticEnumValue",
                "newInstance", "invokeFunction", "functionCall", "refCall", "argumentList",
                "argument", "argumentValue", "constValue", "refValue", "constBool", "constNull",
                "constClass", "constString", "constMultilineString", "constRenderString",
                "decNumber", "hexNumber", "otcNumber", "binNumber", "jsonValue", "jsonMapValue",
                "jsonPairs", "jsonPair", "jsonArrayValue", "jsonItemList"
        };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[]{
                null, "'!'", "'not'", "'%'", "'as'", "'cast'", "'is'", "'instanceof'",
                "'typeof'", "'*'", "'/'", "'+'", "'-'", "'in'", "'notin'", "'>='", "'gte'",
                "'<='", "'lte'", "'!='", "'ne'", "'<>'", "'neq'", "'=='", "'eq'", "'>'",
                "'gt'", "'<'", "'lt'", "'&&'", "'and'", "'||'", "'or'", "'?'", "'debugger'",
                "'try'", "'catch'", "'|'", "'finally'", "'throw'", "'break'", "'continue'",
                "'return'", "'while'", "'for'", "'foreach'", "'if'", "'else'", "'elif'",
                "'='", "'?='", "'.='", "'+='", "'-='", "'*='", "'/='", "'%='", "'#'",
                "'@'", "'new'", null, null, null, null, null, null, null, null, null,
                "'null'", null, null, null, null, null, null, null, null, null, null,
                null, null, null, "'\"'", null, "'('", "')'", "','", "'.'", "'$'", "'{'",
                "'}'", "';'", "':'", "'['", "']'"
        };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[]{
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null,
                "TERM_COMMENT_SINGLE_LINE", "TERM_COMMENT_MULTI_LINE", "TERM_CONST_STRING_MULTILINE",
                "TERM_CONST_STRING_MULTILINE_QUOTE", "TERM_CONST_STRING_RENDER", "TERM_CONST_STRING_RENDER_SINGLE",
                "TERM_CONST_STRING", "TERM_CONST_STRING_SINGLE", "TERM_CONST_BOOLEAN",
                "TERM_CONST_NULL", "TERM_CONST_TYPE_CLASS", "REF_EXPRESS", "TERM_CONST_NUMBER_SCIEN_2",
                "TERM_CONST_NUMBER_SCIEN_1", "TERM_CONST_NUMBER_FLOAT", "TERM_CONST_NUMBER",
                "TERM_CONST_NUMBER_HEX", "TERM_CONST_NUMBER_OTC", "TERM_CONST_NUMBER_BIN",
                "TERM_INTEGER", "NAMING", "ROUTE_NAMING", "ID", "TERM_QUOTE", "ESCAPED_CHAR",
                "TERM_PAREN_L", "TERM_PAREN_R", "TERM_COMMA", "TERM_DOT", "TERM_DOLLAR",
                "TERM_CURLY_L", "TERM_CURLY_R", "TERM_SEMICOLON", "TERM_COLON", "TERM_BRACKET_SQUARE_L",
                "TERM_BRACKET_SQUARE_R", "CH_E", "CH_0X", "CH_0T", "CH_0B", "TERM_DIGIT",
                "TERM_HEX_LETTER", "TERM_OTC_LETTER", "TERM_BIN_LETTER", "WS"
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
        public TerminalNode EOF() {
            return getToken(TinyScriptParser.EOF, 0);
        }

        public SegmentsContext segments() {
            return getRuleContext(SegmentsContext.class, 0);
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
                setState(101);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3602739462624243706L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 69862653951L) != 0)) {
                    {
                        setState(100);
                        segments();
                    }
                }

                setState(103);
                match(EOF);
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
    public static class SegmentsContext extends ParserRuleContext {
        public List<ExpressContext> express() {
            return getRuleContexts(ExpressContext.class);
        }

        public ExpressContext express(int i) {
            return getRuleContext(ExpressContext.class, i);
        }

        public List<TerminalNode> TERM_SEMICOLON() {
            return getTokens(TinyScriptParser.TERM_SEMICOLON);
        }

        public TerminalNode TERM_SEMICOLON(int i) {
            return getToken(TinyScriptParser.TERM_SEMICOLON, i);
        }

        public SegmentsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_segments;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterSegments(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitSegments(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitSegments(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SegmentsContext segments() throws RecognitionException {
        SegmentsContext _localctx = new SegmentsContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_segments);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(105);
                express(0);
                setState(110);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 1, _ctx);
                while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(106);
                                match(TERM_SEMICOLON);
                                setState(107);
                                express(0);
                            }
                        }
                    }
                    setState(112);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 1, _ctx);
                }
                setState(114);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == TERM_SEMICOLON) {
                    {
                        setState(113);
                        match(TERM_SEMICOLON);
                    }
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
        public DebuggerSegmentContext debuggerSegment() {
            return getRuleContext(DebuggerSegmentContext.class, 0);
        }

        public IfSegmentContext ifSegment() {
            return getRuleContext(IfSegmentContext.class, 0);
        }

        public ForeachSegmentContext foreachSegment() {
            return getRuleContext(ForeachSegmentContext.class, 0);
        }

        public ForSegmentContext forSegment() {
            return getRuleContext(ForSegmentContext.class, 0);
        }

        public WhileSegmentContext whileSegment() {
            return getRuleContext(WhileSegmentContext.class, 0);
        }

        public ControlSegmentContext controlSegment() {
            return getRuleContext(ControlSegmentContext.class, 0);
        }

        public TrySegmentContext trySegment() {
            return getRuleContext(TrySegmentContext.class, 0);
        }

        public ThrowSegmentContext throwSegment() {
            return getRuleContext(ThrowSegmentContext.class, 0);
        }

        public ParenSegmentContext parenSegment() {
            return getRuleContext(ParenSegmentContext.class, 0);
        }

        public List<ExpressContext> express() {
            return getRuleContexts(ExpressContext.class);
        }

        public ExpressContext express(int i) {
            return getRuleContext(ExpressContext.class, i);
        }

        public EqualValueContext equalValue() {
            return getRuleContext(EqualValueContext.class, 0);
        }

        public NewInstanceContext newInstance() {
            return getRuleContext(NewInstanceContext.class, 0);
        }

        public InvokeFunctionContext invokeFunction() {
            return getRuleContext(InvokeFunctionContext.class, 0);
        }

        public StaticEnumValueContext staticEnumValue() {
            return getRuleContext(StaticEnumValueContext.class, 0);
        }

        public ConstValueContext constValue() {
            return getRuleContext(ConstValueContext.class, 0);
        }

        public RefValueContext refValue() {
            return getRuleContext(RefValueContext.class, 0);
        }

        public JsonValueContext jsonValue() {
            return getRuleContext(JsonValueContext.class, 0);
        }

        public NegtiveSegmentContext negtiveSegment() {
            return getRuleContext(NegtiveSegmentContext.class, 0);
        }

        public TerminalNode TERM_COLON() {
            return getToken(TinyScriptParser.TERM_COLON, 0);
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
        int _startState = 4;
        enterRecursionRule(_localctx, 4, RULE_express, _p);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(136);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 3, _ctx)) {
                    case 1: {
                        setState(117);
                        debuggerSegment();
                    }
                    break;
                    case 2: {
                        setState(118);
                        ifSegment();
                    }
                    break;
                    case 3: {
                        setState(119);
                        foreachSegment();
                    }
                    break;
                    case 4: {
                        setState(120);
                        forSegment();
                    }
                    break;
                    case 5: {
                        setState(121);
                        whileSegment();
                    }
                    break;
                    case 6: {
                        setState(122);
                        controlSegment();
                    }
                    break;
                    case 7: {
                        setState(123);
                        trySegment();
                    }
                    break;
                    case 8: {
                        setState(124);
                        throwSegment();
                    }
                    break;
                    case 9: {
                        setState(125);
                        parenSegment();
                    }
                    break;
                    case 10: {
                        setState(126);
                        _la = _input.LA(1);
                        if (!(_la == T__0 || _la == T__1)) {
                            _errHandler.recoverInline(this);
                        } else {
                            if (_input.LA(1) == Token.EOF) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                        setState(127);
                        express(16);
                    }
                    break;
                    case 11: {
                        setState(128);
                        equalValue();
                    }
                    break;
                    case 12: {
                        setState(129);
                        newInstance();
                    }
                    break;
                    case 13: {
                        setState(130);
                        invokeFunction();
                    }
                    break;
                    case 14: {
                        setState(131);
                        staticEnumValue();
                    }
                    break;
                    case 15: {
                        setState(132);
                        constValue();
                    }
                    break;
                    case 16: {
                        setState(133);
                        refValue();
                    }
                    break;
                    case 17: {
                        setState(134);
                        jsonValue();
                    }
                    break;
                    case 18: {
                        setState(135);
                        negtiveSegment();
                    }
                    break;
                }
                _ctx.stop = _input.LT(-1);
                setState(163);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
                while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            setState(161);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 4, _ctx)) {
                                case 1: {
                                    _localctx = new ExpressContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_express);
                                    setState(138);
                                    if (!(precpred(_ctx, 7)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 7)");
                                    setState(139);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 496L) != 0))) {
                                        _errHandler.recoverInline(this);
                                    } else {
                                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                        _errHandler.reportMatch(this);
                                        consume();
                                    }
                                    setState(140);
                                    express(8);
                                }
                                break;
                                case 2: {
                                    _localctx = new ExpressContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_express);
                                    setState(141);
                                    if (!(precpred(_ctx, 6)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 6)");
                                    setState(142);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 1544L) != 0))) {
                                        _errHandler.recoverInline(this);
                                    } else {
                                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                        _errHandler.reportMatch(this);
                                        consume();
                                    }
                                    setState(143);
                                    express(7);
                                }
                                break;
                                case 3: {
                                    _localctx = new ExpressContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_express);
                                    setState(144);
                                    if (!(precpred(_ctx, 5)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 5)");
                                    setState(145);
                                    _la = _input.LA(1);
                                    if (!(_la == T__10 || _la == T__11)) {
                                        _errHandler.recoverInline(this);
                                    } else {
                                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                        _errHandler.reportMatch(this);
                                        consume();
                                    }
                                    setState(146);
                                    express(6);
                                }
                                break;
                                case 4: {
                                    _localctx = new ExpressContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_express);
                                    setState(147);
                                    if (!(precpred(_ctx, 4)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 4)");
                                    setState(148);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 536862720L) != 0))) {
                                        _errHandler.recoverInline(this);
                                    } else {
                                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                        _errHandler.reportMatch(this);
                                        consume();
                                    }
                                    setState(149);
                                    express(5);
                                }
                                break;
                                case 5: {
                                    _localctx = new ExpressContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_express);
                                    setState(150);
                                    if (!(precpred(_ctx, 3)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 3)");
                                    setState(151);
                                    _la = _input.LA(1);
                                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 8053063680L) != 0))) {
                                        _errHandler.recoverInline(this);
                                    } else {
                                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                        _errHandler.reportMatch(this);
                                        consume();
                                    }
                                    setState(152);
                                    express(4);
                                }
                                break;
                                case 6: {
                                    _localctx = new ExpressContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_express);
                                    setState(153);
                                    if (!(precpred(_ctx, 1)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                                    setState(154);
                                    match(T__32);
                                    setState(155);
                                    express(0);
                                    setState(156);
                                    match(TERM_COLON);
                                    setState(157);
                                    express(2);
                                }
                                break;
                                case 7: {
                                    _localctx = new ExpressContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_express);
                                    setState(159);
                                    if (!(precpred(_ctx, 8)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 8)");
                                    {
                                        setState(160);
                                        match(T__2);
                                    }
                                }
                                break;
                            }
                        }
                    }
                    setState(165);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
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
    public static class NegtiveSegmentContext extends ParserRuleContext {
        public ExpressContext express() {
            return getRuleContext(ExpressContext.class, 0);
        }

        public NegtiveSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_negtiveSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterNegtiveSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitNegtiveSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitNegtiveSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NegtiveSegmentContext negtiveSegment() throws RecognitionException {
        NegtiveSegmentContext _localctx = new NegtiveSegmentContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_negtiveSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(166);
                match(T__11);
                setState(167);
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
    public static class DebuggerSegmentContext extends ParserRuleContext {
        public NamingBlockContext namingBlock() {
            return getRuleContext(NamingBlockContext.class, 0);
        }

        public TerminalNode TERM_PAREN_L() {
            return getToken(TinyScriptParser.TERM_PAREN_L, 0);
        }

        public ConditionBlockContext conditionBlock() {
            return getRuleContext(ConditionBlockContext.class, 0);
        }

        public TerminalNode TERM_PAREN_R() {
            return getToken(TinyScriptParser.TERM_PAREN_R, 0);
        }

        public DebuggerSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_debuggerSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterDebuggerSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitDebuggerSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitDebuggerSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DebuggerSegmentContext debuggerSegment() throws RecognitionException {
        DebuggerSegmentContext _localctx = new DebuggerSegmentContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_debuggerSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(169);
                match(T__33);
                setState(171);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 6, _ctx)) {
                    case 1: {
                        setState(170);
                        namingBlock();
                    }
                    break;
                }
                setState(177);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 7, _ctx)) {
                    case 1: {
                        setState(173);
                        match(TERM_PAREN_L);
                        setState(174);
                        conditionBlock();
                        setState(175);
                        match(TERM_PAREN_R);
                    }
                    break;
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
    public static class TrySegmentContext extends ParserRuleContext {
        public TryBodyBlockContext tryBodyBlock() {
            return getRuleContext(TryBodyBlockContext.class, 0);
        }

        public List<TerminalNode> TERM_PAREN_L() {
            return getTokens(TinyScriptParser.TERM_PAREN_L);
        }

        public TerminalNode TERM_PAREN_L(int i) {
            return getToken(TinyScriptParser.TERM_PAREN_L, i);
        }

        public List<NamingBlockContext> namingBlock() {
            return getRuleContexts(NamingBlockContext.class);
        }

        public NamingBlockContext namingBlock(int i) {
            return getRuleContext(NamingBlockContext.class, i);
        }

        public List<TerminalNode> TERM_PAREN_R() {
            return getTokens(TinyScriptParser.TERM_PAREN_R);
        }

        public TerminalNode TERM_PAREN_R(int i) {
            return getToken(TinyScriptParser.TERM_PAREN_R, i);
        }

        public List<CatchBodyBlockContext> catchBodyBlock() {
            return getRuleContexts(CatchBodyBlockContext.class);
        }

        public CatchBodyBlockContext catchBodyBlock(int i) {
            return getRuleContext(CatchBodyBlockContext.class, i);
        }

        public FinallyBodyBlockContext finallyBodyBlock() {
            return getRuleContext(FinallyBodyBlockContext.class, 0);
        }

        public List<ClassNameBlockContext> classNameBlock() {
            return getRuleContexts(ClassNameBlockContext.class);
        }

        public ClassNameBlockContext classNameBlock(int i) {
            return getRuleContext(ClassNameBlockContext.class, i);
        }

        public TrySegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_trySegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterTrySegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitTrySegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitTrySegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TrySegmentContext trySegment() throws RecognitionException {
        TrySegmentContext _localctx = new TrySegmentContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_trySegment);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(179);
                match(T__34);
                setState(180);
                tryBodyBlock();
                setState(197);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 9, _ctx);
                while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(181);
                                match(T__35);
                                setState(182);
                                match(TERM_PAREN_L);
                                {
                                    setState(183);
                                    classNameBlock();
                                    setState(188);
                                    _errHandler.sync(this);
                                    _la = _input.LA(1);
                                    while (_la == T__36) {
                                        {
                                            {
                                                setState(184);
                                                match(T__36);
                                                setState(185);
                                                classNameBlock();
                                            }
                                        }
                                        setState(190);
                                        _errHandler.sync(this);
                                        _la = _input.LA(1);
                                    }
                                }
                                setState(191);
                                namingBlock();
                                setState(192);
                                match(TERM_PAREN_R);
                                setState(193);
                                catchBodyBlock();
                            }
                        }
                    }
                    setState(199);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 9, _ctx);
                }
                setState(202);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 10, _ctx)) {
                    case 1: {
                        setState(200);
                        match(T__37);
                        setState(201);
                        finallyBodyBlock();
                    }
                    break;
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
    public static class ThrowSegmentContext extends ParserRuleContext {
        public ExpressContext express() {
            return getRuleContext(ExpressContext.class, 0);
        }

        public ThrowSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_throwSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterThrowSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitThrowSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitThrowSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ThrowSegmentContext throwSegment() throws RecognitionException {
        ThrowSegmentContext _localctx = new ThrowSegmentContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_throwSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(204);
                match(T__38);
                setState(205);
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
    public static class TryBodyBlockContext extends ParserRuleContext {
        public ScriptBlockContext scriptBlock() {
            return getRuleContext(ScriptBlockContext.class, 0);
        }

        public TryBodyBlockContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_tryBodyBlock;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterTryBodyBlock(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitTryBodyBlock(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitTryBodyBlock(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TryBodyBlockContext tryBodyBlock() throws RecognitionException {
        TryBodyBlockContext _localctx = new TryBodyBlockContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_tryBodyBlock);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(207);
                scriptBlock();
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
    public static class CatchBodyBlockContext extends ParserRuleContext {
        public ScriptBlockContext scriptBlock() {
            return getRuleContext(ScriptBlockContext.class, 0);
        }

        public CatchBodyBlockContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_catchBodyBlock;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterCatchBodyBlock(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitCatchBodyBlock(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitCatchBodyBlock(this);
            else return visitor.visitChildren(this);
        }
    }

    public final CatchBodyBlockContext catchBodyBlock() throws RecognitionException {
        CatchBodyBlockContext _localctx = new CatchBodyBlockContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_catchBodyBlock);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(209);
                scriptBlock();
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
    public static class FinallyBodyBlockContext extends ParserRuleContext {
        public ScriptBlockContext scriptBlock() {
            return getRuleContext(ScriptBlockContext.class, 0);
        }

        public FinallyBodyBlockContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_finallyBodyBlock;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterFinallyBodyBlock(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitFinallyBodyBlock(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitFinallyBodyBlock(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FinallyBodyBlockContext finallyBodyBlock() throws RecognitionException {
        FinallyBodyBlockContext _localctx = new FinallyBodyBlockContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_finallyBodyBlock);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(211);
                scriptBlock();
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
    public static class ClassNameBlockContext extends ParserRuleContext {
        public TerminalNode NAMING() {
            return getToken(TinyScriptParser.NAMING, 0);
        }

        public ClassNameBlockContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_classNameBlock;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterClassNameBlock(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitClassNameBlock(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitClassNameBlock(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ClassNameBlockContext classNameBlock() throws RecognitionException {
        ClassNameBlockContext _localctx = new ClassNameBlockContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_classNameBlock);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(213);
                match(NAMING);
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
    public static class ParenSegmentContext extends ParserRuleContext {
        public TerminalNode TERM_PAREN_L() {
            return getToken(TinyScriptParser.TERM_PAREN_L, 0);
        }

        public ExpressContext express() {
            return getRuleContext(ExpressContext.class, 0);
        }

        public TerminalNode TERM_PAREN_R() {
            return getToken(TinyScriptParser.TERM_PAREN_R, 0);
        }

        public ParenSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_parenSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterParenSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitParenSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitParenSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ParenSegmentContext parenSegment() throws RecognitionException {
        ParenSegmentContext _localctx = new ParenSegmentContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_parenSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(215);
                match(TERM_PAREN_L);
                setState(216);
                express(0);
                setState(217);
                match(TERM_PAREN_R);
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
    public static class ControlSegmentContext extends ParserRuleContext {
        public ExpressContext express() {
            return getRuleContext(ExpressContext.class, 0);
        }

        public ControlSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_controlSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterControlSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitControlSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitControlSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ControlSegmentContext controlSegment() throws RecognitionException {
        ControlSegmentContext _localctx = new ControlSegmentContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_controlSegment);
        try {
            setState(225);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T__39:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(219);
                    match(T__39);
                }
                break;
                case T__40:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(220);
                    match(T__40);
                }
                break;
                case T__41:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(221);
                    match(T__41);
                    setState(223);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 11, _ctx)) {
                        case 1: {
                            setState(222);
                            express(0);
                        }
                        break;
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
    public static class WhileSegmentContext extends ParserRuleContext {
        public TerminalNode TERM_PAREN_L() {
            return getToken(TinyScriptParser.TERM_PAREN_L, 0);
        }

        public ConditionBlockContext conditionBlock() {
            return getRuleContext(ConditionBlockContext.class, 0);
        }

        public TerminalNode TERM_PAREN_R() {
            return getToken(TinyScriptParser.TERM_PAREN_R, 0);
        }

        public ScriptBlockContext scriptBlock() {
            return getRuleContext(ScriptBlockContext.class, 0);
        }

        public WhileSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_whileSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterWhileSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitWhileSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitWhileSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final WhileSegmentContext whileSegment() throws RecognitionException {
        WhileSegmentContext _localctx = new WhileSegmentContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_whileSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(227);
                match(T__42);
                setState(228);
                match(TERM_PAREN_L);
                setState(229);
                conditionBlock();
                setState(230);
                match(TERM_PAREN_R);
                setState(231);
                scriptBlock();
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
    public static class ForSegmentContext extends ParserRuleContext {
        public TerminalNode TERM_PAREN_L() {
            return getToken(TinyScriptParser.TERM_PAREN_L, 0);
        }

        public List<ExpressContext> express() {
            return getRuleContexts(ExpressContext.class);
        }

        public ExpressContext express(int i) {
            return getRuleContext(ExpressContext.class, i);
        }

        public List<TerminalNode> TERM_SEMICOLON() {
            return getTokens(TinyScriptParser.TERM_SEMICOLON);
        }

        public TerminalNode TERM_SEMICOLON(int i) {
            return getToken(TinyScriptParser.TERM_SEMICOLON, i);
        }

        public ConditionBlockContext conditionBlock() {
            return getRuleContext(ConditionBlockContext.class, 0);
        }

        public TerminalNode TERM_PAREN_R() {
            return getToken(TinyScriptParser.TERM_PAREN_R, 0);
        }

        public ScriptBlockContext scriptBlock() {
            return getRuleContext(ScriptBlockContext.class, 0);
        }

        public ForSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_forSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterForSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitForSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitForSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ForSegmentContext forSegment() throws RecognitionException {
        ForSegmentContext _localctx = new ForSegmentContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_forSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(233);
                match(T__43);
                setState(234);
                match(TERM_PAREN_L);
                setState(235);
                express(0);
                setState(236);
                match(TERM_SEMICOLON);
                setState(237);
                conditionBlock();
                setState(238);
                match(TERM_SEMICOLON);
                setState(239);
                express(0);
                setState(240);
                match(TERM_PAREN_R);
                setState(241);
                scriptBlock();
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
    public static class ForeachSegmentContext extends ParserRuleContext {
        public TerminalNode TERM_PAREN_L() {
            return getToken(TinyScriptParser.TERM_PAREN_L, 0);
        }

        public NamingBlockContext namingBlock() {
            return getRuleContext(NamingBlockContext.class, 0);
        }

        public TerminalNode TERM_COLON() {
            return getToken(TinyScriptParser.TERM_COLON, 0);
        }

        public ExpressContext express() {
            return getRuleContext(ExpressContext.class, 0);
        }

        public TerminalNode TERM_PAREN_R() {
            return getToken(TinyScriptParser.TERM_PAREN_R, 0);
        }

        public ScriptBlockContext scriptBlock() {
            return getRuleContext(ScriptBlockContext.class, 0);
        }

        public ForeachSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_foreachSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterForeachSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitForeachSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitForeachSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ForeachSegmentContext foreachSegment() throws RecognitionException {
        ForeachSegmentContext _localctx = new ForeachSegmentContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_foreachSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(243);
                match(T__44);
                setState(244);
                match(TERM_PAREN_L);
                setState(245);
                namingBlock();
                setState(246);
                match(TERM_COLON);
                setState(247);
                express(0);
                setState(248);
                match(TERM_PAREN_R);
                setState(249);
                scriptBlock();
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
    public static class NamingBlockContext extends ParserRuleContext {
        public TerminalNode NAMING() {
            return getToken(TinyScriptParser.NAMING, 0);
        }

        public NamingBlockContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_namingBlock;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterNamingBlock(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitNamingBlock(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitNamingBlock(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NamingBlockContext namingBlock() throws RecognitionException {
        NamingBlockContext _localctx = new NamingBlockContext(_ctx, getState());
        enterRule(_localctx, 32, RULE_namingBlock);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(251);
                match(NAMING);
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
    public static class IfSegmentContext extends ParserRuleContext {
        public List<TerminalNode> TERM_PAREN_L() {
            return getTokens(TinyScriptParser.TERM_PAREN_L);
        }

        public TerminalNode TERM_PAREN_L(int i) {
            return getToken(TinyScriptParser.TERM_PAREN_L, i);
        }

        public List<ConditionBlockContext> conditionBlock() {
            return getRuleContexts(ConditionBlockContext.class);
        }

        public ConditionBlockContext conditionBlock(int i) {
            return getRuleContext(ConditionBlockContext.class, i);
        }

        public List<TerminalNode> TERM_PAREN_R() {
            return getTokens(TinyScriptParser.TERM_PAREN_R);
        }

        public TerminalNode TERM_PAREN_R(int i) {
            return getToken(TinyScriptParser.TERM_PAREN_R, i);
        }

        public List<ScriptBlockContext> scriptBlock() {
            return getRuleContexts(ScriptBlockContext.class);
        }

        public ScriptBlockContext scriptBlock(int i) {
            return getRuleContext(ScriptBlockContext.class, i);
        }

        public IfSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterIfSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitIfSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitIfSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfSegmentContext ifSegment() throws RecognitionException {
        IfSegmentContext _localctx = new IfSegmentContext(_ctx, getState());
        enterRule(_localctx, 34, RULE_ifSegment);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(253);
                match(T__45);
                setState(254);
                match(TERM_PAREN_L);
                setState(255);
                conditionBlock();
                setState(256);
                match(TERM_PAREN_R);
                setState(257);
                scriptBlock();
                setState(270);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 14, _ctx);
                while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(261);
                                _errHandler.sync(this);
                                switch (_input.LA(1)) {
                                    case T__46: {
                                        setState(258);
                                        match(T__46);
                                        setState(259);
                                        match(T__45);
                                    }
                                    break;
                                    case T__47: {
                                        setState(260);
                                        match(T__47);
                                    }
                                    break;
                                    default:
                                        throw new NoViableAltException(this);
                                }
                                setState(263);
                                match(TERM_PAREN_L);
                                setState(264);
                                conditionBlock();
                                setState(265);
                                match(TERM_PAREN_R);
                                setState(266);
                                scriptBlock();
                            }
                        }
                    }
                    setState(272);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 14, _ctx);
                }
                setState(275);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 15, _ctx)) {
                    case 1: {
                        setState(273);
                        match(T__46);
                        setState(274);
                        scriptBlock();
                    }
                    break;
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
    public static class ConditionBlockContext extends ParserRuleContext {
        public ExpressContext express() {
            return getRuleContext(ExpressContext.class, 0);
        }

        public ConditionBlockContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_conditionBlock;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterConditionBlock(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitConditionBlock(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitConditionBlock(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConditionBlockContext conditionBlock() throws RecognitionException {
        ConditionBlockContext _localctx = new ConditionBlockContext(_ctx, getState());
        enterRule(_localctx, 36, RULE_conditionBlock);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(277);
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
    public static class ScriptBlockContext extends ParserRuleContext {
        public TerminalNode TERM_CURLY_L() {
            return getToken(TinyScriptParser.TERM_CURLY_L, 0);
        }

        public TerminalNode TERM_CURLY_R() {
            return getToken(TinyScriptParser.TERM_CURLY_R, 0);
        }

        public SegmentsContext segments() {
            return getRuleContext(SegmentsContext.class, 0);
        }

        public ScriptBlockContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_scriptBlock;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterScriptBlock(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitScriptBlock(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitScriptBlock(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ScriptBlockContext scriptBlock() throws RecognitionException {
        ScriptBlockContext _localctx = new ScriptBlockContext(_ctx, getState());
        enterRule(_localctx, 38, RULE_scriptBlock);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(279);
                match(TERM_CURLY_L);
                setState(281);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3602739462624243706L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 69862653951L) != 0)) {
                    {
                        setState(280);
                        segments();
                    }
                }

                setState(283);
                match(TERM_CURLY_R);
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
    public static class EqualValueContext extends ParserRuleContext {
        public ExpressContext express() {
            return getRuleContext(ExpressContext.class, 0);
        }

        public TerminalNode ROUTE_NAMING() {
            return getToken(TinyScriptParser.ROUTE_NAMING, 0);
        }

        public TerminalNode NAMING() {
            return getToken(TinyScriptParser.NAMING, 0);
        }

        public ExtractExpressContext extractExpress() {
            return getRuleContext(ExtractExpressContext.class, 0);
        }

        public StaticEnumValueContext staticEnumValue() {
            return getRuleContext(StaticEnumValueContext.class, 0);
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
        enterRule(_localctx, 40, RULE_equalValue);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(289);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 17, _ctx)) {
                    case 1: {
                        setState(285);
                        match(ROUTE_NAMING);
                    }
                    break;
                    case 2: {
                        setState(286);
                        match(NAMING);
                    }
                    break;
                    case 3: {
                        setState(287);
                        extractExpress();
                    }
                    break;
                    case 4: {
                        setState(288);
                        staticEnumValue();
                    }
                    break;
                }
                setState(291);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 143552238122434560L) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(292);
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
    public static class ExtractExpressContext extends ParserRuleContext {
        public TerminalNode TERM_CURLY_L() {
            return getToken(TinyScriptParser.TERM_CURLY_L, 0);
        }

        public TerminalNode TERM_CURLY_R() {
            return getToken(TinyScriptParser.TERM_CURLY_R, 0);
        }

        public ExtractPairsContext extractPairs() {
            return getRuleContext(ExtractPairsContext.class, 0);
        }

        public ExtractExpressContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_extractExpress;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterExtractExpress(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitExtractExpress(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitExtractExpress(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExtractExpressContext extractExpress() throws RecognitionException {
        ExtractExpressContext _localctx = new ExtractExpressContext(_ctx, getState());
        enterRule(_localctx, 42, RULE_extractExpress);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(294);
                match(T__56);
                setState(295);
                match(TERM_CURLY_L);
                setState(297);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 49155L) != 0)) {
                    {
                        setState(296);
                        extractPairs();
                    }
                }

                setState(299);
                match(TERM_CURLY_R);
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
    public static class ExtractPairsContext extends ParserRuleContext {
        public List<ExtractPairContext> extractPair() {
            return getRuleContexts(ExtractPairContext.class);
        }

        public ExtractPairContext extractPair(int i) {
            return getRuleContext(ExtractPairContext.class, i);
        }

        public List<TerminalNode> TERM_COMMA() {
            return getTokens(TinyScriptParser.TERM_COMMA);
        }

        public TerminalNode TERM_COMMA(int i) {
            return getToken(TinyScriptParser.TERM_COMMA, i);
        }

        public ExtractPairsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_extractPairs;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterExtractPairs(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitExtractPairs(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitExtractPairs(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExtractPairsContext extractPairs() throws RecognitionException {
        ExtractPairsContext _localctx = new ExtractPairsContext(_ctx, getState());
        enterRule(_localctx, 44, RULE_extractPairs);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(301);
                extractPair();
                setState(306);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == TERM_COMMA) {
                    {
                        {
                            setState(302);
                            match(TERM_COMMA);
                            setState(303);
                            extractPair();
                        }
                    }
                    setState(308);
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
    public static class ExtractPairContext extends ParserRuleContext {
        public List<TerminalNode> NAMING() {
            return getTokens(TinyScriptParser.NAMING);
        }

        public TerminalNode NAMING(int i) {
            return getToken(TinyScriptParser.NAMING, i);
        }

        public List<TerminalNode> ROUTE_NAMING() {
            return getTokens(TinyScriptParser.ROUTE_NAMING);
        }

        public TerminalNode ROUTE_NAMING(int i) {
            return getToken(TinyScriptParser.ROUTE_NAMING, i);
        }

        public List<ConstStringContext> constString() {
            return getRuleContexts(ConstStringContext.class);
        }

        public ConstStringContext constString(int i) {
            return getRuleContext(ConstStringContext.class, i);
        }

        public TerminalNode TERM_COLON() {
            return getToken(TinyScriptParser.TERM_COLON, 0);
        }

        public ExtractPairContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_extractPair;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterExtractPair(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitExtractPair(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitExtractPair(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExtractPairContext extractPair() throws RecognitionException {
        ExtractPairContext _localctx = new ExtractPairContext(_ctx, getState());
        enterRule(_localctx, 46, RULE_extractPair);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(312);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case NAMING: {
                        setState(309);
                        match(NAMING);
                    }
                    break;
                    case ROUTE_NAMING: {
                        setState(310);
                        match(ROUTE_NAMING);
                    }
                    break;
                    case TERM_CONST_STRING:
                    case TERM_CONST_STRING_SINGLE: {
                        setState(311);
                        constString();
                    }
                    break;
                    default:
                        throw new NoViableAltException(this);
                }
                setState(320);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == TERM_COLON) {
                    {
                        setState(314);
                        match(TERM_COLON);
                        setState(318);
                        _errHandler.sync(this);
                        switch (_input.LA(1)) {
                            case NAMING: {
                                setState(315);
                                match(NAMING);
                            }
                            break;
                            case ROUTE_NAMING: {
                                setState(316);
                                match(ROUTE_NAMING);
                            }
                            break;
                            case TERM_CONST_STRING:
                            case TERM_CONST_STRING_SINGLE: {
                                setState(317);
                                constString();
                            }
                            break;
                            default:
                                throw new NoViableAltException(this);
                        }
                    }
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
    public static class StaticEnumValueContext extends ParserRuleContext {
        public List<TerminalNode> NAMING() {
            return getTokens(TinyScriptParser.NAMING);
        }

        public TerminalNode NAMING(int i) {
            return getToken(TinyScriptParser.NAMING, i);
        }

        public StaticEnumValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_staticEnumValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterStaticEnumValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitStaticEnumValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitStaticEnumValue(this);
            else return visitor.visitChildren(this);
        }
    }

    public final StaticEnumValueContext staticEnumValue() throws RecognitionException {
        StaticEnumValueContext _localctx = new StaticEnumValueContext(_ctx, getState());
        enterRule(_localctx, 48, RULE_staticEnumValue);
        try {
            setState(327);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T__57:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(322);
                    match(T__57);
                    setState(323);
                    match(NAMING);
                }
                break;
                case NAMING:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(324);
                    match(NAMING);
                    setState(325);
                    match(T__57);
                    setState(326);
                    match(NAMING);
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
        enterRule(_localctx, 50, RULE_newInstance);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(329);
                match(T__58);
                setState(330);
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

        public List<TerminalNode> TERM_DOT() {
            return getTokens(TinyScriptParser.TERM_DOT);
        }

        public TerminalNode TERM_DOT(int i) {
            return getToken(TinyScriptParser.TERM_DOT, i);
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
        enterRule(_localctx, 52, RULE_invokeFunction);
        try {
            int _alt;
            setState(348);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case REF_EXPRESS:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(332);
                    refCall();
                    setState(337);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 24, _ctx);
                    while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
                        if (_alt == 1) {
                            {
                                {
                                    setState(333);
                                    match(TERM_DOT);
                                    setState(334);
                                    functionCall();
                                }
                            }
                        }
                        setState(339);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 24, _ctx);
                    }
                }
                break;
                case NAMING:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(340);
                    functionCall();
                    setState(345);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 25, _ctx);
                    while (_alt != 2 && _alt != ATN.INVALID_ALT_NUMBER) {
                        if (_alt == 1) {
                            {
                                {
                                    setState(341);
                                    match(TERM_DOT);
                                    setState(342);
                                    functionCall();
                                }
                            }
                        }
                        setState(347);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 25, _ctx);
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

        public TerminalNode TERM_PAREN_L() {
            return getToken(TinyScriptParser.TERM_PAREN_L, 0);
        }

        public TerminalNode TERM_PAREN_R() {
            return getToken(TinyScriptParser.TERM_PAREN_R, 0);
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
        enterRule(_localctx, 54, RULE_functionCall);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(350);
                match(NAMING);
                setState(351);
                match(TERM_PAREN_L);
                setState(353);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3602739462624243706L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 69862653951L) != 0)) {
                    {
                        setState(352);
                        argumentList();
                    }
                }

                setState(355);
                match(TERM_PAREN_R);
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

        public TerminalNode TERM_DOT() {
            return getToken(TinyScriptParser.TERM_DOT, 0);
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
        enterRule(_localctx, 56, RULE_refCall);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(357);
                refValue();
                setState(358);
                match(TERM_DOT);
                setState(359);
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

        public List<TerminalNode> TERM_COMMA() {
            return getTokens(TinyScriptParser.TERM_COMMA);
        }

        public TerminalNode TERM_COMMA(int i) {
            return getToken(TinyScriptParser.TERM_COMMA, i);
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
        enterRule(_localctx, 58, RULE_argumentList);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(361);
                argument();
                setState(366);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == TERM_COMMA) {
                    {
                        {
                            setState(362);
                            match(TERM_COMMA);
                            setState(363);
                            argument();
                        }
                    }
                    setState(368);
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
        public ArgumentValueContext argumentValue() {
            return getRuleContext(ArgumentValueContext.class, 0);
        }

        public TerminalNode TERM_COLON() {
            return getToken(TinyScriptParser.TERM_COLON, 0);
        }

        public TerminalNode NAMING() {
            return getToken(TinyScriptParser.NAMING, 0);
        }

        public ConstStringContext constString() {
            return getRuleContext(ConstStringContext.class, 0);
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
        enterRule(_localctx, 60, RULE_argument);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(374);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 30, _ctx)) {
                    case 1: {
                        setState(371);
                        _errHandler.sync(this);
                        switch (_input.LA(1)) {
                            case NAMING: {
                                setState(369);
                                match(NAMING);
                            }
                            break;
                            case TERM_CONST_STRING:
                            case TERM_CONST_STRING_SINGLE: {
                                setState(370);
                                constString();
                            }
                            break;
                            default:
                                throw new NoViableAltException(this);
                        }
                        setState(373);
                        match(TERM_COLON);
                    }
                    break;
                }
                setState(376);
                argumentValue();
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
    public static class ArgumentValueContext extends ParserRuleContext {
        public ExpressContext express() {
            return getRuleContext(ExpressContext.class, 0);
        }

        public ArgumentValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_argumentValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterArgumentValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitArgumentValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitArgumentValue(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ArgumentValueContext argumentValue() throws RecognitionException {
        ArgumentValueContext _localctx = new ArgumentValueContext(_ctx, getState());
        enterRule(_localctx, 62, RULE_argumentValue);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(378);
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
    public static class ConstValueContext extends ParserRuleContext {
        public ConstBoolContext constBool() {
            return getRuleContext(ConstBoolContext.class, 0);
        }

        public ConstClassContext constClass() {
            return getRuleContext(ConstClassContext.class, 0);
        }

        public ConstNullContext constNull() {
            return getRuleContext(ConstNullContext.class, 0);
        }

        public ConstMultilineStringContext constMultilineString() {
            return getRuleContext(ConstMultilineStringContext.class, 0);
        }

        public ConstRenderStringContext constRenderString() {
            return getRuleContext(ConstRenderStringContext.class, 0);
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
        enterRule(_localctx, 64, RULE_constValue);
        try {
            setState(390);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case TERM_CONST_BOOLEAN:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(380);
                    constBool();
                }
                break;
                case TERM_CONST_TYPE_CLASS:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(381);
                    constClass();
                }
                break;
                case TERM_CONST_NULL:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(382);
                    constNull();
                }
                break;
                case TERM_CONST_STRING_MULTILINE:
                case TERM_CONST_STRING_MULTILINE_QUOTE:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(383);
                    constMultilineString();
                }
                break;
                case TERM_CONST_STRING_RENDER:
                case TERM_CONST_STRING_RENDER_SINGLE:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(384);
                    constRenderString();
                }
                break;
                case TERM_CONST_STRING:
                case TERM_CONST_STRING_SINGLE:
                    enterOuterAlt(_localctx, 6);
                {
                    setState(385);
                    constString();
                }
                break;
                case TERM_CONST_NUMBER_SCIEN_2:
                case TERM_CONST_NUMBER_SCIEN_1:
                case TERM_CONST_NUMBER_FLOAT:
                case TERM_CONST_NUMBER:
                case TERM_DIGIT:
                    enterOuterAlt(_localctx, 7);
                {
                    setState(386);
                    decNumber();
                }
                break;
                case TERM_CONST_NUMBER_HEX:
                    enterOuterAlt(_localctx, 8);
                {
                    setState(387);
                    hexNumber();
                }
                break;
                case TERM_CONST_NUMBER_OTC:
                    enterOuterAlt(_localctx, 9);
                {
                    setState(388);
                    otcNumber();
                }
                break;
                case TERM_CONST_NUMBER_BIN:
                    enterOuterAlt(_localctx, 10);
                {
                    setState(389);
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
        enterRule(_localctx, 66, RULE_refValue);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(392);
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
        public TerminalNode TERM_CONST_BOOLEAN() {
            return getToken(TinyScriptParser.TERM_CONST_BOOLEAN, 0);
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
        enterRule(_localctx, 68, RULE_constBool);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(394);
                match(TERM_CONST_BOOLEAN);
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
    public static class ConstNullContext extends ParserRuleContext {
        public TerminalNode TERM_CONST_NULL() {
            return getToken(TinyScriptParser.TERM_CONST_NULL, 0);
        }

        public ConstNullContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_constNull;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterConstNull(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitConstNull(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitConstNull(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConstNullContext constNull() throws RecognitionException {
        ConstNullContext _localctx = new ConstNullContext(_ctx, getState());
        enterRule(_localctx, 70, RULE_constNull);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(396);
                match(TERM_CONST_NULL);
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
    public static class ConstClassContext extends ParserRuleContext {
        public TerminalNode TERM_CONST_TYPE_CLASS() {
            return getToken(TinyScriptParser.TERM_CONST_TYPE_CLASS, 0);
        }

        public ConstClassContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_constClass;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterConstClass(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitConstClass(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitConstClass(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConstClassContext constClass() throws RecognitionException {
        ConstClassContext _localctx = new ConstClassContext(_ctx, getState());
        enterRule(_localctx, 72, RULE_constClass);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(398);
                match(TERM_CONST_TYPE_CLASS);
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
        public TerminalNode TERM_CONST_STRING() {
            return getToken(TinyScriptParser.TERM_CONST_STRING, 0);
        }

        public TerminalNode TERM_CONST_STRING_SINGLE() {
            return getToken(TinyScriptParser.TERM_CONST_STRING_SINGLE, 0);
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
        enterRule(_localctx, 74, RULE_constString);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(400);
                _la = _input.LA(1);
                if (!(_la == TERM_CONST_STRING || _la == TERM_CONST_STRING_SINGLE)) {
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
    public static class ConstMultilineStringContext extends ParserRuleContext {
        public TerminalNode TERM_CONST_STRING_MULTILINE() {
            return getToken(TinyScriptParser.TERM_CONST_STRING_MULTILINE, 0);
        }

        public TerminalNode TERM_CONST_STRING_MULTILINE_QUOTE() {
            return getToken(TinyScriptParser.TERM_CONST_STRING_MULTILINE_QUOTE, 0);
        }

        public ConstMultilineStringContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_constMultilineString;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterConstMultilineString(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitConstMultilineString(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitConstMultilineString(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConstMultilineStringContext constMultilineString() throws RecognitionException {
        ConstMultilineStringContext _localctx = new ConstMultilineStringContext(_ctx, getState());
        enterRule(_localctx, 76, RULE_constMultilineString);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(402);
                _la = _input.LA(1);
                if (!(_la == TERM_CONST_STRING_MULTILINE || _la == TERM_CONST_STRING_MULTILINE_QUOTE)) {
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
    public static class ConstRenderStringContext extends ParserRuleContext {
        public TerminalNode TERM_CONST_STRING_RENDER() {
            return getToken(TinyScriptParser.TERM_CONST_STRING_RENDER, 0);
        }

        public TerminalNode TERM_CONST_STRING_RENDER_SINGLE() {
            return getToken(TinyScriptParser.TERM_CONST_STRING_RENDER_SINGLE, 0);
        }

        public ConstRenderStringContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_constRenderString;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterConstRenderString(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitConstRenderString(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitConstRenderString(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConstRenderStringContext constRenderString() throws RecognitionException {
        ConstRenderStringContext _localctx = new ConstRenderStringContext(_ctx, getState());
        enterRule(_localctx, 78, RULE_constRenderString);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(404);
                _la = _input.LA(1);
                if (!(_la == TERM_CONST_STRING_RENDER || _la == TERM_CONST_STRING_RENDER_SINGLE)) {
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
    public static class DecNumberContext extends ParserRuleContext {
        public TerminalNode TERM_CONST_NUMBER_SCIEN_2() {
            return getToken(TinyScriptParser.TERM_CONST_NUMBER_SCIEN_2, 0);
        }

        public TerminalNode TERM_CONST_NUMBER_SCIEN_1() {
            return getToken(TinyScriptParser.TERM_CONST_NUMBER_SCIEN_1, 0);
        }

        public TerminalNode TERM_CONST_NUMBER_FLOAT() {
            return getToken(TinyScriptParser.TERM_CONST_NUMBER_FLOAT, 0);
        }

        public TerminalNode TERM_CONST_NUMBER() {
            return getToken(TinyScriptParser.TERM_CONST_NUMBER, 0);
        }

        public TerminalNode TERM_DIGIT() {
            return getToken(TinyScriptParser.TERM_DIGIT, 0);
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
        enterRule(_localctx, 80, RULE_decNumber);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(406);
                _la = _input.LA(1);
                if (!(((((_la - 72)) & ~0x3f) == 0 && ((1L << (_la - 72)) & 268435471L) != 0))) {
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
        public TerminalNode TERM_CONST_NUMBER_HEX() {
            return getToken(TinyScriptParser.TERM_CONST_NUMBER_HEX, 0);
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
        enterRule(_localctx, 82, RULE_hexNumber);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(408);
                match(TERM_CONST_NUMBER_HEX);
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
        public TerminalNode TERM_CONST_NUMBER_OTC() {
            return getToken(TinyScriptParser.TERM_CONST_NUMBER_OTC, 0);
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
        enterRule(_localctx, 84, RULE_otcNumber);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(410);
                match(TERM_CONST_NUMBER_OTC);
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
        public TerminalNode TERM_CONST_NUMBER_BIN() {
            return getToken(TinyScriptParser.TERM_CONST_NUMBER_BIN, 0);
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
        enterRule(_localctx, 86, RULE_binNumber);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(412);
                match(TERM_CONST_NUMBER_BIN);
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
    public static class JsonValueContext extends ParserRuleContext {
        public InvokeFunctionContext invokeFunction() {
            return getRuleContext(InvokeFunctionContext.class, 0);
        }

        public ConstValueContext constValue() {
            return getRuleContext(ConstValueContext.class, 0);
        }

        public RefValueContext refValue() {
            return getRuleContext(RefValueContext.class, 0);
        }

        public JsonArrayValueContext jsonArrayValue() {
            return getRuleContext(JsonArrayValueContext.class, 0);
        }

        public JsonMapValueContext jsonMapValue() {
            return getRuleContext(JsonMapValueContext.class, 0);
        }

        public JsonValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_jsonValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterJsonValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitJsonValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitJsonValue(this);
            else return visitor.visitChildren(this);
        }
    }

    public final JsonValueContext jsonValue() throws RecognitionException {
        JsonValueContext _localctx = new JsonValueContext(_ctx, getState());
        enterRule(_localctx, 88, RULE_jsonValue);
        try {
            setState(419);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 32, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(414);
                    invokeFunction();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(415);
                    constValue();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(416);
                    refValue();
                }
                break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(417);
                    jsonArrayValue();
                }
                break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(418);
                    jsonMapValue();
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
    public static class JsonMapValueContext extends ParserRuleContext {
        public TerminalNode TERM_CURLY_L() {
            return getToken(TinyScriptParser.TERM_CURLY_L, 0);
        }

        public TerminalNode TERM_CURLY_R() {
            return getToken(TinyScriptParser.TERM_CURLY_R, 0);
        }

        public JsonPairsContext jsonPairs() {
            return getRuleContext(JsonPairsContext.class, 0);
        }

        public JsonMapValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_jsonMapValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterJsonMapValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitJsonMapValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitJsonMapValue(this);
            else return visitor.visitChildren(this);
        }
    }

    public final JsonMapValueContext jsonMapValue() throws RecognitionException {
        JsonMapValueContext _localctx = new JsonMapValueContext(_ctx, getState());
        enterRule(_localctx, 90, RULE_jsonMapValue);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(421);
                match(TERM_CURLY_L);
                setState(423);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (((((_la - 66)) & ~0x3f) == 0 && ((1L << (_la - 66)) & 16387L) != 0)) {
                    {
                        setState(422);
                        jsonPairs();
                    }
                }

                setState(425);
                match(TERM_CURLY_R);
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
    public static class JsonPairsContext extends ParserRuleContext {
        public List<JsonPairContext> jsonPair() {
            return getRuleContexts(JsonPairContext.class);
        }

        public JsonPairContext jsonPair(int i) {
            return getRuleContext(JsonPairContext.class, i);
        }

        public List<TerminalNode> TERM_COMMA() {
            return getTokens(TinyScriptParser.TERM_COMMA);
        }

        public TerminalNode TERM_COMMA(int i) {
            return getToken(TinyScriptParser.TERM_COMMA, i);
        }

        public JsonPairsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_jsonPairs;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterJsonPairs(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitJsonPairs(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitJsonPairs(this);
            else return visitor.visitChildren(this);
        }
    }

    public final JsonPairsContext jsonPairs() throws RecognitionException {
        JsonPairsContext _localctx = new JsonPairsContext(_ctx, getState());
        enterRule(_localctx, 92, RULE_jsonPairs);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(427);
                jsonPair();
                setState(432);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == TERM_COMMA) {
                    {
                        {
                            setState(428);
                            match(TERM_COMMA);
                            setState(429);
                            jsonPair();
                        }
                    }
                    setState(434);
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
    public static class JsonPairContext extends ParserRuleContext {
        public TerminalNode TERM_COLON() {
            return getToken(TinyScriptParser.TERM_COLON, 0);
        }

        public ExpressContext express() {
            return getRuleContext(ExpressContext.class, 0);
        }

        public TerminalNode NAMING() {
            return getToken(TinyScriptParser.NAMING, 0);
        }

        public ConstStringContext constString() {
            return getRuleContext(ConstStringContext.class, 0);
        }

        public JsonPairContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_jsonPair;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterJsonPair(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitJsonPair(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitJsonPair(this);
            else return visitor.visitChildren(this);
        }
    }

    public final JsonPairContext jsonPair() throws RecognitionException {
        JsonPairContext _localctx = new JsonPairContext(_ctx, getState());
        enterRule(_localctx, 94, RULE_jsonPair);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(437);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case NAMING: {
                        setState(435);
                        match(NAMING);
                    }
                    break;
                    case TERM_CONST_STRING:
                    case TERM_CONST_STRING_SINGLE: {
                        setState(436);
                        constString();
                    }
                    break;
                    default:
                        throw new NoViableAltException(this);
                }
                setState(439);
                match(TERM_COLON);
                setState(440);
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
    public static class JsonArrayValueContext extends ParserRuleContext {
        public TerminalNode TERM_BRACKET_SQUARE_L() {
            return getToken(TinyScriptParser.TERM_BRACKET_SQUARE_L, 0);
        }

        public TerminalNode TERM_BRACKET_SQUARE_R() {
            return getToken(TinyScriptParser.TERM_BRACKET_SQUARE_R, 0);
        }

        public JsonItemListContext jsonItemList() {
            return getRuleContext(JsonItemListContext.class, 0);
        }

        public JsonArrayValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_jsonArrayValue;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterJsonArrayValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitJsonArrayValue(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitJsonArrayValue(this);
            else return visitor.visitChildren(this);
        }
    }

    public final JsonArrayValueContext jsonArrayValue() throws RecognitionException {
        JsonArrayValueContext _localctx = new JsonArrayValueContext(_ctx, getState());
        enterRule(_localctx, 96, RULE_jsonArrayValue);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(442);
                match(TERM_BRACKET_SQUARE_L);
                setState(444);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -3602739462624243706L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 69862653951L) != 0)) {
                    {
                        setState(443);
                        jsonItemList();
                    }
                }

                setState(446);
                match(TERM_BRACKET_SQUARE_R);
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
    public static class JsonItemListContext extends ParserRuleContext {
        public List<ExpressContext> express() {
            return getRuleContexts(ExpressContext.class);
        }

        public ExpressContext express(int i) {
            return getRuleContext(ExpressContext.class, i);
        }

        public List<TerminalNode> TERM_COMMA() {
            return getTokens(TinyScriptParser.TERM_COMMA);
        }

        public TerminalNode TERM_COMMA(int i) {
            return getToken(TinyScriptParser.TERM_COMMA, i);
        }

        public JsonItemListContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_jsonItemList;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).enterJsonItemList(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof TinyScriptListener) ((TinyScriptListener) listener).exitJsonItemList(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof TinyScriptVisitor)
                return ((TinyScriptVisitor<? extends T>) visitor).visitJsonItemList(this);
            else return visitor.visitChildren(this);
        }
    }

    public final JsonItemListContext jsonItemList() throws RecognitionException {
        JsonItemListContext _localctx = new JsonItemListContext(_ctx, getState());
        enterRule(_localctx, 98, RULE_jsonItemList);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(448);
                express(0);
                setState(453);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == TERM_COMMA) {
                    {
                        {
                            setState(449);
                            match(TERM_COMMA);
                            setState(450);
                            express(0);
                        }
                    }
                    setState(455);
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

    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
            case 2:
                return express_sempred((ExpressContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean express_sempred(ExpressContext _localctx, int predIndex) {
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
                return precpred(_ctx, 1);
            case 6:
                return precpred(_ctx, 8);
        }
        return true;
    }

    public static final String _serializedATN =
            "\u0004\u0001h\u01c9\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002" +
                    "\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002" +
                    "\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002" +
                    "\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002" +
                    "\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f" +
                    "\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012" +
                    "\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015" +
                    "\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018" +
                    "\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b" +
                    "\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e" +
                    "\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002" +
                    "#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002" +
                    "(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007,\u0002" +
                    "-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u00071\u0001" +
                    "\u0000\u0003\u0000f\b\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001" +
                    "\u0001\u0001\u0001\u0005\u0001m\b\u0001\n\u0001\f\u0001p\t\u0001\u0001" +
                    "\u0001\u0003\u0001s\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001" +
                    "\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001" +
                    "\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001" +
                    "\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002\u0089" +
                    "\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001" +
                    "\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001" +
                    "\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001" +
                    "\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005" +
                    "\u0002\u00a2\b\u0002\n\u0002\f\u0002\u00a5\t\u0002\u0001\u0003\u0001\u0003" +
                    "\u0001\u0003\u0001\u0004\u0001\u0004\u0003\u0004\u00ac\b\u0004\u0001\u0004" +
                    "\u0001\u0004\u0001\u0004\u0001\u0004\u0003\u0004\u00b2\b\u0004\u0001\u0005" +
                    "\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005" +
                    "\u0005\u0005\u00bb\b\u0005\n\u0005\f\u0005\u00be\t\u0005\u0001\u0005\u0001" +
                    "\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u00c4\b\u0005\n\u0005\f\u0005" +
                    "\u00c7\t\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u00cb\b\u0005\u0001" +
                    "\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b" +
                    "\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001" +
                    "\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u00e0\b\f\u0003\f\u00e2" +
                    "\b\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001" +
                    "\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001" +
                    "\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001" +
                    "\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001" +
                    "\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001" +
                    "\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u0106\b\u0011\u0001\u0011\u0001" +
                    "\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u010d\b\u0011\n" +
                    "\u0011\f\u0011\u0110\t\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u0114" +
                    "\b\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0003\u0013\u011a" +
                    "\b\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001" +
                    "\u0014\u0003\u0014\u0122\b\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001" +
                    "\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u012a\b\u0015\u0001\u0015\u0001" +
                    "\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0005\u0016\u0131\b\u0016\n" +
                    "\u0016\f\u0016\u0134\t\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0003" +
                    "\u0017\u0139\b\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0003" +
                    "\u0017\u013f\b\u0017\u0003\u0017\u0141\b\u0017\u0001\u0018\u0001\u0018" +
                    "\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u0148\b\u0018\u0001\u0019" +
                    "\u0001\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0005\u001a" +
                    "\u0150\b\u001a\n\u001a\f\u001a\u0153\t\u001a\u0001\u001a\u0001\u001a\u0001" +
                    "\u001a\u0005\u001a\u0158\b\u001a\n\u001a\f\u001a\u015b\t\u001a\u0003\u001a" +
                    "\u015d\b\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0003\u001b\u0162\b" +
                    "\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001" +
                    "\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0005\u001d\u016d\b\u001d\n" +
                    "\u001d\f\u001d\u0170\t\u001d\u0001\u001e\u0001\u001e\u0003\u001e\u0174" +
                    "\b\u001e\u0001\u001e\u0003\u001e\u0177\b\u001e\u0001\u001e\u0001\u001e" +
                    "\u0001\u001f\u0001\u001f\u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001" +
                    " \u0001 \u0001 \u0001 \u0003 \u0187\b \u0001!\u0001!\u0001\"\u0001\"\u0001" +
                    "#\u0001#\u0001$\u0001$\u0001%\u0001%\u0001&\u0001&\u0001\'\u0001\'\u0001" +
                    "(\u0001(\u0001)\u0001)\u0001*\u0001*\u0001+\u0001+\u0001,\u0001,\u0001" +
                    ",\u0001,\u0001,\u0003,\u01a4\b,\u0001-\u0001-\u0003-\u01a8\b-\u0001-\u0001" +
                    "-\u0001.\u0001.\u0001.\u0005.\u01af\b.\n.\f.\u01b2\t.\u0001/\u0001/\u0003" +
                    "/\u01b6\b/\u0001/\u0001/\u0001/\u00010\u00010\u00030\u01bd\b0\u00010\u0001" +
                    "0\u00011\u00011\u00011\u00051\u01c4\b1\n1\f1\u01c7\t1\u00011\u0000\u0001" +
                    "\u00042\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018" +
                    "\u001a\u001c\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`b\u0000\u000b\u0001" +
                    "\u0000\u0001\u0002\u0001\u0000\u0004\b\u0002\u0000\u0003\u0003\t\n\u0001" +
                    "\u0000\u000b\f\u0001\u0000\r\u001c\u0001\u0000\u001d \u0001\u000018\u0001" +
                    "\u0000BC\u0001\u0000>?\u0001\u0000@A\u0002\u0000HKdd\u01e1\u0000e\u0001" +
                    "\u0000\u0000\u0000\u0002i\u0001\u0000\u0000\u0000\u0004\u0088\u0001\u0000" +
                    "\u0000\u0000\u0006\u00a6\u0001\u0000\u0000\u0000\b\u00a9\u0001\u0000\u0000" +
                    "\u0000\n\u00b3\u0001\u0000\u0000\u0000\f\u00cc\u0001\u0000\u0000\u0000" +
                    "\u000e\u00cf\u0001\u0000\u0000\u0000\u0010\u00d1\u0001\u0000\u0000\u0000" +
                    "\u0012\u00d3\u0001\u0000\u0000\u0000\u0014\u00d5\u0001\u0000\u0000\u0000" +
                    "\u0016\u00d7\u0001\u0000\u0000\u0000\u0018\u00e1\u0001\u0000\u0000\u0000" +
                    "\u001a\u00e3\u0001\u0000\u0000\u0000\u001c\u00e9\u0001\u0000\u0000\u0000" +
                    "\u001e\u00f3\u0001\u0000\u0000\u0000 \u00fb\u0001\u0000\u0000\u0000\"" +
                    "\u00fd\u0001\u0000\u0000\u0000$\u0115\u0001\u0000\u0000\u0000&\u0117\u0001" +
                    "\u0000\u0000\u0000(\u0121\u0001\u0000\u0000\u0000*\u0126\u0001\u0000\u0000" +
                    "\u0000,\u012d\u0001\u0000\u0000\u0000.\u0138\u0001\u0000\u0000\u00000" +
                    "\u0147\u0001\u0000\u0000\u00002\u0149\u0001\u0000\u0000\u00004\u015c\u0001" +
                    "\u0000\u0000\u00006\u015e\u0001\u0000\u0000\u00008\u0165\u0001\u0000\u0000" +
                    "\u0000:\u0169\u0001\u0000\u0000\u0000<\u0176\u0001\u0000\u0000\u0000>" +
                    "\u017a\u0001\u0000\u0000\u0000@\u0186\u0001\u0000\u0000\u0000B\u0188\u0001" +
                    "\u0000\u0000\u0000D\u018a\u0001\u0000\u0000\u0000F\u018c\u0001\u0000\u0000" +
                    "\u0000H\u018e\u0001\u0000\u0000\u0000J\u0190\u0001\u0000\u0000\u0000L" +
                    "\u0192\u0001\u0000\u0000\u0000N\u0194\u0001\u0000\u0000\u0000P\u0196\u0001" +
                    "\u0000\u0000\u0000R\u0198\u0001\u0000\u0000\u0000T\u019a\u0001\u0000\u0000" +
                    "\u0000V\u019c\u0001\u0000\u0000\u0000X\u01a3\u0001\u0000\u0000\u0000Z" +
                    "\u01a5\u0001\u0000\u0000\u0000\\\u01ab\u0001\u0000\u0000\u0000^\u01b5" +
                    "\u0001\u0000\u0000\u0000`\u01ba\u0001\u0000\u0000\u0000b\u01c0\u0001\u0000" +
                    "\u0000\u0000df\u0003\u0002\u0001\u0000ed\u0001\u0000\u0000\u0000ef\u0001" +
                    "\u0000\u0000\u0000fg\u0001\u0000\u0000\u0000gh\u0005\u0000\u0000\u0001" +
                    "h\u0001\u0001\u0000\u0000\u0000in\u0003\u0004\u0002\u0000jk\u0005\\\u0000" +
                    "\u0000km\u0003\u0004\u0002\u0000lj\u0001\u0000\u0000\u0000mp\u0001\u0000" +
                    "\u0000\u0000nl\u0001\u0000\u0000\u0000no\u0001\u0000\u0000\u0000or\u0001" +
                    "\u0000\u0000\u0000pn\u0001\u0000\u0000\u0000qs\u0005\\\u0000\u0000rq\u0001" +
                    "\u0000\u0000\u0000rs\u0001\u0000\u0000\u0000s\u0003\u0001\u0000\u0000" +
                    "\u0000tu\u0006\u0002\uffff\uffff\u0000u\u0089\u0003\b\u0004\u0000v\u0089" +
                    "\u0003\"\u0011\u0000w\u0089\u0003\u001e\u000f\u0000x\u0089\u0003\u001c" +
                    "\u000e\u0000y\u0089\u0003\u001a\r\u0000z\u0089\u0003\u0018\f\u0000{\u0089" +
                    "\u0003\n\u0005\u0000|\u0089\u0003\f\u0006\u0000}\u0089\u0003\u0016\u000b" +
                    "\u0000~\u007f\u0007\u0000\u0000\u0000\u007f\u0089\u0003\u0004\u0002\u0010" +
                    "\u0080\u0089\u0003(\u0014\u0000\u0081\u0089\u00032\u0019\u0000\u0082\u0089" +
                    "\u00034\u001a\u0000\u0083\u0089\u00030\u0018\u0000\u0084\u0089\u0003@" +
                    " \u0000\u0085\u0089\u0003B!\u0000\u0086\u0089\u0003X,\u0000\u0087\u0089" +
                    "\u0003\u0006\u0003\u0000\u0088t\u0001\u0000\u0000\u0000\u0088v\u0001\u0000" +
                    "\u0000\u0000\u0088w\u0001\u0000\u0000\u0000\u0088x\u0001\u0000\u0000\u0000" +
                    "\u0088y\u0001\u0000\u0000\u0000\u0088z\u0001\u0000\u0000\u0000\u0088{" +
                    "\u0001\u0000\u0000\u0000\u0088|\u0001\u0000\u0000\u0000\u0088}\u0001\u0000" +
                    "\u0000\u0000\u0088~\u0001\u0000\u0000\u0000\u0088\u0080\u0001\u0000\u0000" +
                    "\u0000\u0088\u0081\u0001\u0000\u0000\u0000\u0088\u0082\u0001\u0000\u0000" +
                    "\u0000\u0088\u0083\u0001\u0000\u0000\u0000\u0088\u0084\u0001\u0000\u0000" +
                    "\u0000\u0088\u0085\u0001\u0000\u0000\u0000\u0088\u0086\u0001\u0000\u0000" +
                    "\u0000\u0088\u0087\u0001\u0000\u0000\u0000\u0089\u00a3\u0001\u0000\u0000" +
                    "\u0000\u008a\u008b\n\u0007\u0000\u0000\u008b\u008c\u0007\u0001\u0000\u0000" +
                    "\u008c\u00a2\u0003\u0004\u0002\b\u008d\u008e\n\u0006\u0000\u0000\u008e" +
                    "\u008f\u0007\u0002\u0000\u0000\u008f\u00a2\u0003\u0004\u0002\u0007\u0090" +
                    "\u0091\n\u0005\u0000\u0000\u0091\u0092\u0007\u0003\u0000\u0000\u0092\u00a2" +
                    "\u0003\u0004\u0002\u0006\u0093\u0094\n\u0004\u0000\u0000\u0094\u0095\u0007" +
                    "\u0004\u0000\u0000\u0095\u00a2\u0003\u0004\u0002\u0005\u0096\u0097\n\u0003" +
                    "\u0000\u0000\u0097\u0098\u0007\u0005\u0000\u0000\u0098\u00a2\u0003\u0004" +
                    "\u0002\u0004\u0099\u009a\n\u0001\u0000\u0000\u009a\u009b\u0005!\u0000" +
                    "\u0000\u009b\u009c\u0003\u0004\u0002\u0000\u009c\u009d\u0005]\u0000\u0000" +
                    "\u009d\u009e\u0003\u0004\u0002\u0002\u009e\u00a2\u0001\u0000\u0000\u0000" +
                    "\u009f\u00a0\n\b\u0000\u0000\u00a0\u00a2\u0005\u0003\u0000\u0000\u00a1" +
                    "\u008a\u0001\u0000\u0000\u0000\u00a1\u008d\u0001\u0000\u0000\u0000\u00a1" +
                    "\u0090\u0001\u0000\u0000\u0000\u00a1\u0093\u0001\u0000\u0000\u0000\u00a1" +
                    "\u0096\u0001\u0000\u0000\u0000\u00a1\u0099\u0001\u0000\u0000\u0000\u00a1" +
                    "\u009f\u0001\u0000\u0000\u0000\u00a2\u00a5\u0001\u0000\u0000\u0000\u00a3" +
                    "\u00a1\u0001\u0000\u0000\u0000\u00a3\u00a4\u0001\u0000\u0000\u0000\u00a4" +
                    "\u0005\u0001\u0000\u0000\u0000\u00a5\u00a3\u0001\u0000\u0000\u0000\u00a6" +
                    "\u00a7\u0005\f\u0000\u0000\u00a7\u00a8\u0003\u0004\u0002\u0000\u00a8\u0007" +
                    "\u0001\u0000\u0000\u0000\u00a9\u00ab\u0005\"\u0000\u0000\u00aa\u00ac\u0003" +
                    " \u0010\u0000\u00ab\u00aa\u0001\u0000\u0000\u0000\u00ab\u00ac\u0001\u0000" +
                    "\u0000\u0000\u00ac\u00b1\u0001\u0000\u0000\u0000\u00ad\u00ae\u0005U\u0000" +
                    "\u0000\u00ae\u00af\u0003$\u0012\u0000\u00af\u00b0\u0005V\u0000\u0000\u00b0" +
                    "\u00b2\u0001\u0000\u0000\u0000\u00b1\u00ad\u0001\u0000\u0000\u0000\u00b1" +
                    "\u00b2\u0001\u0000\u0000\u0000\u00b2\t\u0001\u0000\u0000\u0000\u00b3\u00b4" +
                    "\u0005#\u0000\u0000\u00b4\u00c5\u0003\u000e\u0007\u0000\u00b5\u00b6\u0005" +
                    "$\u0000\u0000\u00b6\u00b7\u0005U\u0000\u0000\u00b7\u00bc\u0003\u0014\n" +
                    "\u0000\u00b8\u00b9\u0005%\u0000\u0000\u00b9\u00bb\u0003\u0014\n\u0000" +
                    "\u00ba\u00b8\u0001\u0000\u0000\u0000\u00bb\u00be\u0001\u0000\u0000\u0000" +
                    "\u00bc\u00ba\u0001\u0000\u0000\u0000\u00bc\u00bd\u0001\u0000\u0000\u0000" +
                    "\u00bd\u00bf\u0001\u0000\u0000\u0000\u00be\u00bc\u0001\u0000\u0000\u0000" +
                    "\u00bf\u00c0\u0003 \u0010\u0000\u00c0\u00c1\u0005V\u0000\u0000\u00c1\u00c2" +
                    "\u0003\u0010\b\u0000\u00c2\u00c4\u0001\u0000\u0000\u0000\u00c3\u00b5\u0001" +
                    "\u0000\u0000\u0000\u00c4\u00c7\u0001\u0000\u0000\u0000\u00c5\u00c3\u0001" +
                    "\u0000\u0000\u0000\u00c5\u00c6\u0001\u0000\u0000\u0000\u00c6\u00ca\u0001" +
                    "\u0000\u0000\u0000\u00c7\u00c5\u0001\u0000\u0000\u0000\u00c8\u00c9\u0005" +
                    "&\u0000\u0000\u00c9\u00cb\u0003\u0012\t\u0000\u00ca\u00c8\u0001\u0000" +
                    "\u0000\u0000\u00ca\u00cb\u0001\u0000\u0000\u0000\u00cb\u000b\u0001\u0000" +
                    "\u0000\u0000\u00cc\u00cd\u0005\'\u0000\u0000\u00cd\u00ce\u0003\u0004\u0002" +
                    "\u0000\u00ce\r\u0001\u0000\u0000\u0000\u00cf\u00d0\u0003&\u0013\u0000" +
                    "\u00d0\u000f\u0001\u0000\u0000\u0000\u00d1\u00d2\u0003&\u0013\u0000\u00d2" +
                    "\u0011\u0001\u0000\u0000\u0000\u00d3\u00d4\u0003&\u0013\u0000\u00d4\u0013" +
                    "\u0001\u0000\u0000\u0000\u00d5\u00d6\u0005P\u0000\u0000\u00d6\u0015\u0001" +
                    "\u0000\u0000\u0000\u00d7\u00d8\u0005U\u0000\u0000\u00d8\u00d9\u0003\u0004" +
                    "\u0002\u0000\u00d9\u00da\u0005V\u0000\u0000\u00da\u0017\u0001\u0000\u0000" +
                    "\u0000\u00db\u00e2\u0005(\u0000\u0000\u00dc\u00e2\u0005)\u0000\u0000\u00dd" +
                    "\u00df\u0005*\u0000\u0000\u00de\u00e0\u0003\u0004\u0002\u0000\u00df\u00de" +
                    "\u0001\u0000\u0000\u0000\u00df\u00e0\u0001\u0000\u0000\u0000\u00e0\u00e2" +
                    "\u0001\u0000\u0000\u0000\u00e1\u00db\u0001\u0000\u0000\u0000\u00e1\u00dc" +
                    "\u0001\u0000\u0000\u0000\u00e1\u00dd\u0001\u0000\u0000\u0000\u00e2\u0019" +
                    "\u0001\u0000\u0000\u0000\u00e3\u00e4\u0005+\u0000\u0000\u00e4\u00e5\u0005" +
                    "U\u0000\u0000\u00e5\u00e6\u0003$\u0012\u0000\u00e6\u00e7\u0005V\u0000" +
                    "\u0000\u00e7\u00e8\u0003&\u0013\u0000\u00e8\u001b\u0001\u0000\u0000\u0000" +
                    "\u00e9\u00ea\u0005,\u0000\u0000\u00ea\u00eb\u0005U\u0000\u0000\u00eb\u00ec" +
                    "\u0003\u0004\u0002\u0000\u00ec\u00ed\u0005\\\u0000\u0000\u00ed\u00ee\u0003" +
                    "$\u0012\u0000\u00ee\u00ef\u0005\\\u0000\u0000\u00ef\u00f0\u0003\u0004" +
                    "\u0002\u0000\u00f0\u00f1\u0005V\u0000\u0000\u00f1\u00f2\u0003&\u0013\u0000" +
                    "\u00f2\u001d\u0001\u0000\u0000\u0000\u00f3\u00f4\u0005-\u0000\u0000\u00f4" +
                    "\u00f5\u0005U\u0000\u0000\u00f5\u00f6\u0003 \u0010\u0000\u00f6\u00f7\u0005" +
                    "]\u0000\u0000\u00f7\u00f8\u0003\u0004\u0002\u0000\u00f8\u00f9\u0005V\u0000" +
                    "\u0000\u00f9\u00fa\u0003&\u0013\u0000\u00fa\u001f\u0001\u0000\u0000\u0000" +
                    "\u00fb\u00fc\u0005P\u0000\u0000\u00fc!\u0001\u0000\u0000\u0000\u00fd\u00fe" +
                    "\u0005.\u0000\u0000\u00fe\u00ff\u0005U\u0000\u0000\u00ff\u0100\u0003$" +
                    "\u0012\u0000\u0100\u0101\u0005V\u0000\u0000\u0101\u010e\u0003&\u0013\u0000" +
                    "\u0102\u0103\u0005/\u0000\u0000\u0103\u0106\u0005.\u0000\u0000\u0104\u0106" +
                    "\u00050\u0000\u0000\u0105\u0102\u0001\u0000\u0000\u0000\u0105\u0104\u0001" +
                    "\u0000\u0000\u0000\u0106\u0107\u0001\u0000\u0000\u0000\u0107\u0108\u0005" +
                    "U\u0000\u0000\u0108\u0109\u0003$\u0012\u0000\u0109\u010a\u0005V\u0000" +
                    "\u0000\u010a\u010b\u0003&\u0013\u0000\u010b\u010d\u0001\u0000\u0000\u0000" +
                    "\u010c\u0105\u0001\u0000\u0000\u0000\u010d\u0110\u0001\u0000\u0000\u0000" +
                    "\u010e\u010c\u0001\u0000\u0000\u0000\u010e\u010f\u0001\u0000\u0000\u0000" +
                    "\u010f\u0113\u0001\u0000\u0000\u0000\u0110\u010e\u0001\u0000\u0000\u0000" +
                    "\u0111\u0112\u0005/\u0000\u0000\u0112\u0114\u0003&\u0013\u0000\u0113\u0111" +
                    "\u0001\u0000\u0000\u0000\u0113\u0114\u0001\u0000\u0000\u0000\u0114#\u0001" +
                    "\u0000\u0000\u0000\u0115\u0116\u0003\u0004\u0002\u0000\u0116%\u0001\u0000" +
                    "\u0000\u0000\u0117\u0119\u0005Z\u0000\u0000\u0118\u011a\u0003\u0002\u0001" +
                    "\u0000\u0119\u0118\u0001\u0000\u0000\u0000\u0119\u011a\u0001\u0000\u0000" +
                    "\u0000\u011a\u011b\u0001\u0000\u0000\u0000\u011b\u011c\u0005[\u0000\u0000" +
                    "\u011c\'\u0001\u0000\u0000\u0000\u011d\u0122\u0005Q\u0000\u0000\u011e" +
                    "\u0122\u0005P\u0000\u0000\u011f\u0122\u0003*\u0015\u0000\u0120\u0122\u0003" +
                    "0\u0018\u0000\u0121\u011d\u0001\u0000\u0000\u0000\u0121\u011e\u0001\u0000" +
                    "\u0000\u0000\u0121\u011f\u0001\u0000\u0000\u0000\u0121\u0120\u0001\u0000" +
                    "\u0000\u0000\u0122\u0123\u0001\u0000\u0000\u0000\u0123\u0124\u0007\u0006" +
                    "\u0000\u0000\u0124\u0125\u0003\u0004\u0002\u0000\u0125)\u0001\u0000\u0000" +
                    "\u0000\u0126\u0127\u00059\u0000\u0000\u0127\u0129\u0005Z\u0000\u0000\u0128" +
                    "\u012a\u0003,\u0016\u0000\u0129\u0128\u0001\u0000\u0000\u0000\u0129\u012a" +
                    "\u0001\u0000\u0000\u0000\u012a\u012b\u0001\u0000\u0000\u0000\u012b\u012c" +
                    "\u0005[\u0000\u0000\u012c+\u0001\u0000\u0000\u0000\u012d\u0132\u0003." +
                    "\u0017\u0000\u012e\u012f\u0005W\u0000\u0000\u012f\u0131\u0003.\u0017\u0000" +
                    "\u0130\u012e\u0001\u0000\u0000\u0000\u0131\u0134\u0001\u0000\u0000\u0000" +
                    "\u0132\u0130\u0001\u0000\u0000\u0000\u0132\u0133\u0001\u0000\u0000\u0000" +
                    "\u0133-\u0001\u0000\u0000\u0000\u0134\u0132\u0001\u0000\u0000\u0000\u0135" +
                    "\u0139\u0005P\u0000\u0000\u0136\u0139\u0005Q\u0000\u0000\u0137\u0139\u0003" +
                    "J%\u0000\u0138\u0135\u0001\u0000\u0000\u0000\u0138\u0136\u0001\u0000\u0000" +
                    "\u0000\u0138\u0137\u0001\u0000\u0000\u0000\u0139\u0140\u0001\u0000\u0000" +
                    "\u0000\u013a\u013e\u0005]\u0000\u0000\u013b\u013f\u0005P\u0000\u0000\u013c" +
                    "\u013f\u0005Q\u0000\u0000\u013d\u013f\u0003J%\u0000\u013e\u013b\u0001" +
                    "\u0000\u0000\u0000\u013e\u013c\u0001\u0000\u0000\u0000\u013e\u013d\u0001" +
                    "\u0000\u0000\u0000\u013f\u0141\u0001\u0000\u0000\u0000\u0140\u013a\u0001" +
                    "\u0000\u0000\u0000\u0140\u0141\u0001\u0000\u0000\u0000\u0141/\u0001\u0000" +
                    "\u0000\u0000\u0142\u0143\u0005:\u0000\u0000\u0143\u0148\u0005P\u0000\u0000" +
                    "\u0144\u0145\u0005P\u0000\u0000\u0145\u0146\u0005:\u0000\u0000\u0146\u0148" +
                    "\u0005P\u0000\u0000\u0147\u0142\u0001\u0000\u0000\u0000\u0147\u0144\u0001" +
                    "\u0000\u0000\u0000\u01481\u0001\u0000\u0000\u0000\u0149\u014a\u0005;\u0000" +
                    "\u0000\u014a\u014b\u00034\u001a\u0000\u014b3\u0001\u0000\u0000\u0000\u014c" +
                    "\u0151\u00038\u001c\u0000\u014d\u014e\u0005X\u0000\u0000\u014e\u0150\u0003" +
                    "6\u001b\u0000\u014f\u014d\u0001\u0000\u0000\u0000\u0150\u0153\u0001\u0000" +
                    "\u0000\u0000\u0151\u014f\u0001\u0000\u0000\u0000\u0151\u0152\u0001\u0000" +
                    "\u0000\u0000\u0152\u015d\u0001\u0000\u0000\u0000\u0153\u0151\u0001\u0000" +
                    "\u0000\u0000\u0154\u0159\u00036\u001b\u0000\u0155\u0156\u0005X\u0000\u0000" +
                    "\u0156\u0158\u00036\u001b\u0000\u0157\u0155\u0001\u0000\u0000\u0000\u0158" +
                    "\u015b\u0001\u0000\u0000\u0000\u0159\u0157\u0001\u0000\u0000\u0000\u0159" +
                    "\u015a\u0001\u0000\u0000\u0000\u015a\u015d\u0001\u0000\u0000\u0000\u015b" +
                    "\u0159\u0001\u0000\u0000\u0000\u015c\u014c\u0001\u0000\u0000\u0000\u015c" +
                    "\u0154\u0001\u0000\u0000\u0000\u015d5\u0001\u0000\u0000\u0000\u015e\u015f" +
                    "\u0005P\u0000\u0000\u015f\u0161\u0005U\u0000\u0000\u0160\u0162\u0003:" +
                    "\u001d\u0000\u0161\u0160\u0001\u0000\u0000\u0000\u0161\u0162\u0001\u0000" +
                    "\u0000\u0000\u0162\u0163\u0001\u0000\u0000\u0000\u0163\u0164\u0005V\u0000" +
                    "\u0000\u01647\u0001\u0000\u0000\u0000\u0165\u0166\u0003B!\u0000\u0166" +
                    "\u0167\u0005X\u0000\u0000\u0167\u0168\u00036\u001b\u0000\u01689\u0001" +
                    "\u0000\u0000\u0000\u0169\u016e\u0003<\u001e\u0000\u016a\u016b\u0005W\u0000" +
                    "\u0000\u016b\u016d\u0003<\u001e\u0000\u016c\u016a\u0001\u0000\u0000\u0000" +
                    "\u016d\u0170\u0001\u0000\u0000\u0000\u016e\u016c\u0001\u0000\u0000\u0000" +
                    "\u016e\u016f\u0001\u0000\u0000\u0000\u016f;\u0001\u0000\u0000\u0000\u0170" +
                    "\u016e\u0001\u0000\u0000\u0000\u0171\u0174\u0005P\u0000\u0000\u0172\u0174" +
                    "\u0003J%\u0000\u0173\u0171\u0001\u0000\u0000\u0000\u0173\u0172\u0001\u0000" +
                    "\u0000\u0000\u0174\u0175\u0001\u0000\u0000\u0000\u0175\u0177\u0005]\u0000" +
                    "\u0000\u0176\u0173\u0001\u0000\u0000\u0000\u0176\u0177\u0001\u0000\u0000" +
                    "\u0000\u0177\u0178\u0001\u0000\u0000\u0000\u0178\u0179\u0003>\u001f\u0000" +
                    "\u0179=\u0001\u0000\u0000\u0000\u017a\u017b\u0003\u0004\u0002\u0000\u017b" +
                    "?\u0001\u0000\u0000\u0000\u017c\u0187\u0003D\"\u0000\u017d\u0187\u0003" +
                    "H$\u0000\u017e\u0187\u0003F#\u0000\u017f\u0187\u0003L&\u0000\u0180\u0187" +
                    "\u0003N\'\u0000\u0181\u0187\u0003J%\u0000\u0182\u0187\u0003P(\u0000\u0183" +
                    "\u0187\u0003R)\u0000\u0184\u0187\u0003T*\u0000\u0185\u0187\u0003V+\u0000" +
                    "\u0186\u017c\u0001\u0000\u0000\u0000\u0186\u017d\u0001\u0000\u0000\u0000" +
                    "\u0186\u017e\u0001\u0000\u0000\u0000\u0186\u017f\u0001\u0000\u0000\u0000" +
                    "\u0186\u0180\u0001\u0000\u0000\u0000\u0186\u0181\u0001\u0000\u0000\u0000" +
                    "\u0186\u0182\u0001\u0000\u0000\u0000\u0186\u0183\u0001\u0000\u0000\u0000" +
                    "\u0186\u0184\u0001\u0000\u0000\u0000\u0186\u0185\u0001\u0000\u0000\u0000" +
                    "\u0187A\u0001\u0000\u0000\u0000\u0188\u0189\u0005G\u0000\u0000\u0189C" +
                    "\u0001\u0000\u0000\u0000\u018a\u018b\u0005D\u0000\u0000\u018bE\u0001\u0000" +
                    "\u0000\u0000\u018c\u018d\u0005E\u0000\u0000\u018dG\u0001\u0000\u0000\u0000" +
                    "\u018e\u018f\u0005F\u0000\u0000\u018fI\u0001\u0000\u0000\u0000\u0190\u0191" +
                    "\u0007\u0007\u0000\u0000\u0191K\u0001\u0000\u0000\u0000\u0192\u0193\u0007" +
                    "\b\u0000\u0000\u0193M\u0001\u0000\u0000\u0000\u0194\u0195\u0007\t\u0000" +
                    "\u0000\u0195O\u0001\u0000\u0000\u0000\u0196\u0197\u0007\n\u0000\u0000" +
                    "\u0197Q\u0001\u0000\u0000\u0000\u0198\u0199\u0005L\u0000\u0000\u0199S" +
                    "\u0001\u0000\u0000\u0000\u019a\u019b\u0005M\u0000\u0000\u019bU\u0001\u0000" +
                    "\u0000\u0000\u019c\u019d\u0005N\u0000\u0000\u019dW\u0001\u0000\u0000\u0000" +
                    "\u019e\u01a4\u00034\u001a\u0000\u019f\u01a4\u0003@ \u0000\u01a0\u01a4" +
                    "\u0003B!\u0000\u01a1\u01a4\u0003`0\u0000\u01a2\u01a4\u0003Z-\u0000\u01a3" +
                    "\u019e\u0001\u0000\u0000\u0000\u01a3\u019f\u0001\u0000\u0000\u0000\u01a3" +
                    "\u01a0\u0001\u0000\u0000\u0000\u01a3\u01a1\u0001\u0000\u0000\u0000\u01a3" +
                    "\u01a2\u0001\u0000\u0000\u0000\u01a4Y\u0001\u0000\u0000\u0000\u01a5\u01a7" +
                    "\u0005Z\u0000\u0000\u01a6\u01a8\u0003\\.\u0000\u01a7\u01a6\u0001\u0000" +
                    "\u0000\u0000\u01a7\u01a8\u0001\u0000\u0000\u0000\u01a8\u01a9\u0001\u0000" +
                    "\u0000\u0000\u01a9\u01aa\u0005[\u0000\u0000\u01aa[\u0001\u0000\u0000\u0000" +
                    "\u01ab\u01b0\u0003^/\u0000\u01ac\u01ad\u0005W\u0000\u0000\u01ad\u01af" +
                    "\u0003^/\u0000\u01ae\u01ac\u0001\u0000\u0000\u0000\u01af\u01b2\u0001\u0000" +
                    "\u0000\u0000\u01b0\u01ae\u0001\u0000\u0000\u0000\u01b0\u01b1\u0001\u0000" +
                    "\u0000\u0000\u01b1]\u0001\u0000\u0000\u0000\u01b2\u01b0\u0001\u0000\u0000" +
                    "\u0000\u01b3\u01b6\u0005P\u0000\u0000\u01b4\u01b6\u0003J%\u0000\u01b5" +
                    "\u01b3\u0001\u0000\u0000\u0000\u01b5\u01b4\u0001\u0000\u0000\u0000\u01b6" +
                    "\u01b7\u0001\u0000\u0000\u0000\u01b7\u01b8\u0005]\u0000\u0000\u01b8\u01b9" +
                    "\u0003\u0004\u0002\u0000\u01b9_\u0001\u0000\u0000\u0000\u01ba\u01bc\u0005" +
                    "^\u0000\u0000\u01bb\u01bd\u0003b1\u0000\u01bc\u01bb\u0001\u0000\u0000" +
                    "\u0000\u01bc\u01bd\u0001\u0000\u0000\u0000\u01bd\u01be\u0001\u0000\u0000" +
                    "\u0000\u01be\u01bf\u0005_\u0000\u0000\u01bfa\u0001\u0000\u0000\u0000\u01c0" +
                    "\u01c5\u0003\u0004\u0002\u0000\u01c1\u01c2\u0005W\u0000\u0000\u01c2\u01c4" +
                    "\u0003\u0004\u0002\u0000\u01c3\u01c1\u0001\u0000\u0000\u0000\u01c4\u01c7" +
                    "\u0001\u0000\u0000\u0000\u01c5\u01c3\u0001\u0000\u0000\u0000\u01c5\u01c6" +
                    "\u0001\u0000\u0000\u0000\u01c6c\u0001\u0000\u0000\u0000\u01c7\u01c5\u0001" +
                    "\u0000\u0000\u0000&enr\u0088\u00a1\u00a3\u00ab\u00b1\u00bc\u00c5\u00ca" +
                    "\u00df\u00e1\u0105\u010e\u0113\u0119\u0121\u0129\u0132\u0138\u013e\u0140" +
                    "\u0147\u0151\u0159\u015c\u0161\u016e\u0173\u0176\u0186\u01a3\u01a7\u01b0" +
                    "\u01b5\u01bc\u01c5";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}