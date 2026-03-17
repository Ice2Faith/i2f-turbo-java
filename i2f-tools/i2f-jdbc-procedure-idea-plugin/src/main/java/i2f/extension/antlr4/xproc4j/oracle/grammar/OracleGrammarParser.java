// Generated from E:/MySystemDefaultFiles/Desktop/30OfficeCenter/102-CjRoot/rwd-2025/RWD_SVC/jdbc-procedure/jdbc-procedure-core/src/main/java/i2f/extension/antlr4/oracle/grammar/rule/OracleGrammar.g4 by ANTLR 4.13.2

package i2f.extension.antlr4.xproc4j.oracle.grammar;

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
public class OracleGrammarParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
            T__9 = 10, T__10 = 11, T__11 = 12, T__12 = 13, TERM_COMMENT_SINGLE_LINE = 14, TERM_COMMENT_MULTI_LINE = 15,
            TERM_CONST_STRING_SINGLE = 16, TERM_CONST_NULL = 17, TERM_CONST_NUMBER_SCIEN_2 = 18,
            TERM_CONST_NUMBER_SCIEN_1 = 19, TERM_CONST_NUMBER_FLOAT = 20, TERM_CONST_NUMBER = 21,
            TERM_INTEGER = 22, KEY_AND = 23, KEY_OR = 24, KEY_IF = 25, KEY_THEN = 26, KEY_ELSE = 27,
            KEY_END = 28, KEY_COMMIT = 29, KEY_ROLLBACK = 30, KEY_EXECUTE = 31, KEY_IMMEDIATE = 32,
            KEY_INTO = 33, KEY_IS = 34, KEY_LIKE = 35, KEY_NOT = 36, KEY_BETWEEN = 37, KEY_TO = 38,
            KEY_DEFAULT = 39, KEY_CREATE = 40, KEY_REPLACE = 41, KEY_FUNCTION = 42, KEY_PROCEDURE = 43,
            KEY_IN = 44, KEY_OUT = 45, IDENTIFIER = 46, WS = 47;
    public static final int
            RULE_convert = 0, RULE_script = 1, RULE_segment = 2, RULE_declareProcedureSegment = 3,
            RULE_argumentDeclareListSegment = 4, RULE_argumentDeclareSegment = 5,
            RULE_declareVariableSegment = 6, RULE_sqlDataType = 7, RULE_conditionSegment = 8,
            RULE_conditionInSegment = 9, RULE_conditionNotInSegment = 10, RULE_variableListSegment = 11,
            RULE_conditionBetweenSegment = 12, RULE_conditionNotLikeSegment = 13,
            RULE_conditionIsNotNullSegment = 14, RULE_conditionIsNullSegment = 15,
            RULE_conditionCompositeSegment = 16, RULE_ifElseSegment = 17, RULE_variableSegment = 18,
            RULE_functionSegment = 19, RULE_argumentListSegment = 20, RULE_commitSegment = 21,
            RULE_rollbackSegment = 22, RULE_executeImmediadeVariableSegment = 23,
            RULE_assignSegment = 24, RULE_sqlNull = 25, RULE_sqlIdentifier = 26, RULE_sqlNumber = 27,
            RULE_sqlString = 28;

    private static String[] makeRuleNames() {
        return new String[]{
                "convert", "script", "segment", "declareProcedureSegment", "argumentDeclareListSegment",
                "argumentDeclareSegment", "declareVariableSegment", "sqlDataType", "conditionSegment",
                "conditionInSegment", "conditionNotInSegment", "variableListSegment",
                "conditionBetweenSegment", "conditionNotLikeSegment", "conditionIsNotNullSegment",
                "conditionIsNullSegment", "conditionCompositeSegment", "ifElseSegment",
                "variableSegment", "functionSegment", "argumentListSegment", "commitSegment",
                "rollbackSegment", "executeImmediadeVariableSegment", "assignSegment",
                "sqlNull", "sqlIdentifier", "sqlNumber", "sqlString"
        };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[]{
                null, "';'", "'('", "')'", "','", "'>='", "'<='", "'!='", "'<>'", "'>'",
                "'<'", "'='", "'||'", "':='", null, null, null, "'null'"
        };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[]{
                null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, "TERM_COMMENT_SINGLE_LINE", "TERM_COMMENT_MULTI_LINE", "TERM_CONST_STRING_SINGLE",
                "TERM_CONST_NULL", "TERM_CONST_NUMBER_SCIEN_2", "TERM_CONST_NUMBER_SCIEN_1",
                "TERM_CONST_NUMBER_FLOAT", "TERM_CONST_NUMBER", "TERM_INTEGER", "KEY_AND",
                "KEY_OR", "KEY_IF", "KEY_THEN", "KEY_ELSE", "KEY_END", "KEY_COMMIT",
                "KEY_ROLLBACK", "KEY_EXECUTE", "KEY_IMMEDIATE", "KEY_INTO", "KEY_IS",
                "KEY_LIKE", "KEY_NOT", "KEY_BETWEEN", "KEY_TO", "KEY_DEFAULT", "KEY_CREATE",
                "KEY_REPLACE", "KEY_FUNCTION", "KEY_PROCEDURE", "KEY_IN", "KEY_OUT",
                "IDENTIFIER", "WS"
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
        return "OracleGrammar.g4";
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

    public OracleGrammarParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    @SuppressWarnings("CheckReturnValue")
    public static class ConvertContext extends ParserRuleContext {
        public ScriptContext script() {
            return getRuleContext(ScriptContext.class, 0);
        }

        public TerminalNode EOF() {
            return getToken(OracleGrammarParser.EOF, 0);
        }

        public ConvertContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_convert;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).enterConvert(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).exitConvert(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitConvert(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConvertContext convert() throws RecognitionException {
        ConvertContext _localctx = new ConvertContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_convert);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(58);
                script();
                setState(59);
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
    public static class ScriptContext extends ParserRuleContext {
        public List<SegmentContext> segment() {
            return getRuleContexts(SegmentContext.class);
        }

        public SegmentContext segment(int i) {
            return getRuleContext(SegmentContext.class, i);
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
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).enterScript(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).exitScript(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitScript(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ScriptContext script() throws RecognitionException {
        ScriptContext _localctx = new ScriptContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_script);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(61);
                segment();
                setState(66);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 0, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(62);
                                match(T__0);
                                setState(63);
                                segment();
                            }
                        }
                    }
                    setState(68);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 0, _ctx);
                }
                setState(70);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T__0) {
                    {
                        setState(69);
                        match(T__0);
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
    public static class SegmentContext extends ParserRuleContext {
        public ExecuteImmediadeVariableSegmentContext executeImmediadeVariableSegment() {
            return getRuleContext(ExecuteImmediadeVariableSegmentContext.class, 0);
        }

        public DeclareProcedureSegmentContext declareProcedureSegment() {
            return getRuleContext(DeclareProcedureSegmentContext.class, 0);
        }

        public DeclareVariableSegmentContext declareVariableSegment() {
            return getRuleContext(DeclareVariableSegmentContext.class, 0);
        }

        public AssignSegmentContext assignSegment() {
            return getRuleContext(AssignSegmentContext.class, 0);
        }

        public FunctionSegmentContext functionSegment() {
            return getRuleContext(FunctionSegmentContext.class, 0);
        }

        public IfElseSegmentContext ifElseSegment() {
            return getRuleContext(IfElseSegmentContext.class, 0);
        }

        public CommitSegmentContext commitSegment() {
            return getRuleContext(CommitSegmentContext.class, 0);
        }

        public RollbackSegmentContext rollbackSegment() {
            return getRuleContext(RollbackSegmentContext.class, 0);
        }

        public VariableSegmentContext variableSegment() {
            return getRuleContext(VariableSegmentContext.class, 0);
        }

        public ConditionSegmentContext conditionSegment() {
            return getRuleContext(ConditionSegmentContext.class, 0);
        }

        public SegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_segment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).enterSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).exitSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SegmentContext segment() throws RecognitionException {
        SegmentContext _localctx = new SegmentContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_segment);
        try {
            setState(82);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 2, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(72);
                    executeImmediadeVariableSegment();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(73);
                    declareProcedureSegment();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(74);
                    declareVariableSegment();
                }
                break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(75);
                    assignSegment();
                }
                break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(76);
                    functionSegment();
                }
                break;
                case 6:
                    enterOuterAlt(_localctx, 6);
                {
                    setState(77);
                    ifElseSegment();
                }
                break;
                case 7:
                    enterOuterAlt(_localctx, 7);
                {
                    setState(78);
                    commitSegment();
                }
                break;
                case 8:
                    enterOuterAlt(_localctx, 8);
                {
                    setState(79);
                    rollbackSegment();
                }
                break;
                case 9:
                    enterOuterAlt(_localctx, 9);
                {
                    setState(80);
                    variableSegment(0);
                }
                break;
                case 10:
                    enterOuterAlt(_localctx, 10);
                {
                    setState(81);
                    conditionSegment();
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
    public static class DeclareProcedureSegmentContext extends ParserRuleContext {
        public TerminalNode KEY_CREATE() {
            return getToken(OracleGrammarParser.KEY_CREATE, 0);
        }

        public TerminalNode IDENTIFIER() {
            return getToken(OracleGrammarParser.IDENTIFIER, 0);
        }

        public TerminalNode KEY_FUNCTION() {
            return getToken(OracleGrammarParser.KEY_FUNCTION, 0);
        }

        public TerminalNode KEY_PROCEDURE() {
            return getToken(OracleGrammarParser.KEY_PROCEDURE, 0);
        }

        public TerminalNode KEY_OR() {
            return getToken(OracleGrammarParser.KEY_OR, 0);
        }

        public TerminalNode KEY_REPLACE() {
            return getToken(OracleGrammarParser.KEY_REPLACE, 0);
        }

        public ArgumentDeclareListSegmentContext argumentDeclareListSegment() {
            return getRuleContext(ArgumentDeclareListSegmentContext.class, 0);
        }

        public DeclareProcedureSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_declareProcedureSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterDeclareProcedureSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitDeclareProcedureSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitDeclareProcedureSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DeclareProcedureSegmentContext declareProcedureSegment() throws RecognitionException {
        DeclareProcedureSegmentContext _localctx = new DeclareProcedureSegmentContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_declareProcedureSegment);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(84);
                match(KEY_CREATE);
                {
                    setState(85);
                    match(KEY_OR);
                    setState(86);
                    match(KEY_REPLACE);
                }
                setState(88);
                _la = _input.LA(1);
                if (!(_la == KEY_FUNCTION || _la == KEY_PROCEDURE)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(89);
                match(IDENTIFIER);
                setState(90);
                match(T__1);
                setState(92);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == IDENTIFIER) {
                    {
                        setState(91);
                        argumentDeclareListSegment();
                    }
                }

                setState(94);
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
    public static class ArgumentDeclareListSegmentContext extends ParserRuleContext {
        public List<ArgumentDeclareSegmentContext> argumentDeclareSegment() {
            return getRuleContexts(ArgumentDeclareSegmentContext.class);
        }

        public ArgumentDeclareSegmentContext argumentDeclareSegment(int i) {
            return getRuleContext(ArgumentDeclareSegmentContext.class, i);
        }

        public ArgumentDeclareListSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_argumentDeclareListSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterArgumentDeclareListSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitArgumentDeclareListSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitArgumentDeclareListSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ArgumentDeclareListSegmentContext argumentDeclareListSegment() throws RecognitionException {
        ArgumentDeclareListSegmentContext _localctx = new ArgumentDeclareListSegmentContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_argumentDeclareListSegment);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(96);
                argumentDeclareSegment();
                setState(101);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__3) {
                    {
                        {
                            setState(97);
                            match(T__3);
                            setState(98);
                            argumentDeclareSegment();
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
    public static class ArgumentDeclareSegmentContext extends ParserRuleContext {
        public List<TerminalNode> IDENTIFIER() {
            return getTokens(OracleGrammarParser.IDENTIFIER);
        }

        public TerminalNode IDENTIFIER(int i) {
            return getToken(OracleGrammarParser.IDENTIFIER, i);
        }

        public TerminalNode KEY_IN() {
            return getToken(OracleGrammarParser.KEY_IN, 0);
        }

        public TerminalNode KEY_OUT() {
            return getToken(OracleGrammarParser.KEY_OUT, 0);
        }

        public ArgumentDeclareSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_argumentDeclareSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterArgumentDeclareSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitArgumentDeclareSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitArgumentDeclareSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ArgumentDeclareSegmentContext argumentDeclareSegment() throws RecognitionException {
        ArgumentDeclareSegmentContext _localctx = new ArgumentDeclareSegmentContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_argumentDeclareSegment);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(104);
                match(IDENTIFIER);
                setState(106);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == KEY_IN || _la == KEY_OUT) {
                    {
                        setState(105);
                        _la = _input.LA(1);
                        if (!(_la == KEY_IN || _la == KEY_OUT)) {
                            _errHandler.recoverInline(this);
                        } else {
                            if (_input.LA(1) == Token.EOF) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                    }
                }

                setState(108);
                match(IDENTIFIER);
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
    public static class DeclareVariableSegmentContext extends ParserRuleContext {
        public SqlIdentifierContext sqlIdentifier() {
            return getRuleContext(SqlIdentifierContext.class, 0);
        }

        public SqlDataTypeContext sqlDataType() {
            return getRuleContext(SqlDataTypeContext.class, 0);
        }

        public TerminalNode KEY_DEFAULT() {
            return getToken(OracleGrammarParser.KEY_DEFAULT, 0);
        }

        public VariableSegmentContext variableSegment() {
            return getRuleContext(VariableSegmentContext.class, 0);
        }

        public DeclareVariableSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_declareVariableSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterDeclareVariableSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitDeclareVariableSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitDeclareVariableSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DeclareVariableSegmentContext declareVariableSegment() throws RecognitionException {
        DeclareVariableSegmentContext _localctx = new DeclareVariableSegmentContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_declareVariableSegment);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(110);
                sqlIdentifier();
                setState(111);
                sqlDataType();
                setState(114);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == KEY_DEFAULT) {
                    {
                        setState(112);
                        match(KEY_DEFAULT);
                        setState(113);
                        variableSegment(0);
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
    public static class SqlDataTypeContext extends ParserRuleContext {
        public SqlIdentifierContext sqlIdentifier() {
            return getRuleContext(SqlIdentifierContext.class, 0);
        }

        public List<SqlNumberContext> sqlNumber() {
            return getRuleContexts(SqlNumberContext.class);
        }

        public SqlNumberContext sqlNumber(int i) {
            return getRuleContext(SqlNumberContext.class, i);
        }

        public SqlDataTypeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_sqlDataType;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).enterSqlDataType(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).exitSqlDataType(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitSqlDataType(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SqlDataTypeContext sqlDataType() throws RecognitionException {
        SqlDataTypeContext _localctx = new SqlDataTypeContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_sqlDataType);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(116);
                sqlIdentifier();
                setState(125);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T__1) {
                    {
                        setState(117);
                        match(T__1);
                        setState(118);
                        sqlNumber();
                        setState(121);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if (_la == T__3) {
                            {
                                setState(119);
                                match(T__3);
                                setState(120);
                                sqlNumber();
                            }
                        }

                        setState(123);
                        match(T__2);
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
    public static class ConditionSegmentContext extends ParserRuleContext {
        public List<VariableSegmentContext> variableSegment() {
            return getRuleContexts(VariableSegmentContext.class);
        }

        public VariableSegmentContext variableSegment(int i) {
            return getRuleContext(VariableSegmentContext.class, i);
        }

        public TerminalNode KEY_LIKE() {
            return getToken(OracleGrammarParser.KEY_LIKE, 0);
        }

        public ConditionIsNullSegmentContext conditionIsNullSegment() {
            return getRuleContext(ConditionIsNullSegmentContext.class, 0);
        }

        public ConditionIsNotNullSegmentContext conditionIsNotNullSegment() {
            return getRuleContext(ConditionIsNotNullSegmentContext.class, 0);
        }

        public ConditionNotLikeSegmentContext conditionNotLikeSegment() {
            return getRuleContext(ConditionNotLikeSegmentContext.class, 0);
        }

        public ConditionBetweenSegmentContext conditionBetweenSegment() {
            return getRuleContext(ConditionBetweenSegmentContext.class, 0);
        }

        public ConditionInSegmentContext conditionInSegment() {
            return getRuleContext(ConditionInSegmentContext.class, 0);
        }

        public ConditionNotInSegmentContext conditionNotInSegment() {
            return getRuleContext(ConditionNotInSegmentContext.class, 0);
        }

        public ConditionSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_conditionSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterConditionSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitConditionSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitConditionSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConditionSegmentContext conditionSegment() throws RecognitionException {
        ConditionSegmentContext _localctx = new ConditionSegmentContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_conditionSegment);
        int _la;
        try {
            setState(137);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 9, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(127);
                    variableSegment(0);
                    setState(128);
                    _la = _input.LA(1);
                    if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 34359742432L) != 0))) {
                        _errHandler.recoverInline(this);
                    } else {
                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
                        _errHandler.reportMatch(this);
                        consume();
                    }
                    setState(129);
                    variableSegment(0);
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(131);
                    conditionIsNullSegment();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(132);
                    conditionIsNotNullSegment();
                }
                break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(133);
                    conditionNotLikeSegment();
                }
                break;
                case 5:
                    enterOuterAlt(_localctx, 5);
                {
                    setState(134);
                    conditionBetweenSegment();
                }
                break;
                case 6:
                    enterOuterAlt(_localctx, 6);
                {
                    setState(135);
                    conditionInSegment();
                }
                break;
                case 7:
                    enterOuterAlt(_localctx, 7);
                {
                    setState(136);
                    conditionNotInSegment();
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
    public static class ConditionInSegmentContext extends ParserRuleContext {
        public VariableSegmentContext variableSegment() {
            return getRuleContext(VariableSegmentContext.class, 0);
        }

        public TerminalNode KEY_IN() {
            return getToken(OracleGrammarParser.KEY_IN, 0);
        }

        public VariableListSegmentContext variableListSegment() {
            return getRuleContext(VariableListSegmentContext.class, 0);
        }

        public ConditionInSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_conditionInSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterConditionInSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitConditionInSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitConditionInSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConditionInSegmentContext conditionInSegment() throws RecognitionException {
        ConditionInSegmentContext _localctx = new ConditionInSegmentContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_conditionInSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(139);
                variableSegment(0);
                setState(140);
                match(KEY_IN);
                setState(141);
                match(T__1);
                setState(142);
                variableListSegment();
                setState(143);
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
    public static class ConditionNotInSegmentContext extends ParserRuleContext {
        public VariableSegmentContext variableSegment() {
            return getRuleContext(VariableSegmentContext.class, 0);
        }

        public TerminalNode KEY_NOT() {
            return getToken(OracleGrammarParser.KEY_NOT, 0);
        }

        public TerminalNode KEY_IN() {
            return getToken(OracleGrammarParser.KEY_IN, 0);
        }

        public VariableListSegmentContext variableListSegment() {
            return getRuleContext(VariableListSegmentContext.class, 0);
        }

        public ConditionNotInSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_conditionNotInSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterConditionNotInSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitConditionNotInSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitConditionNotInSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConditionNotInSegmentContext conditionNotInSegment() throws RecognitionException {
        ConditionNotInSegmentContext _localctx = new ConditionNotInSegmentContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_conditionNotInSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(145);
                variableSegment(0);
                setState(146);
                match(KEY_NOT);
                setState(147);
                match(KEY_IN);
                setState(148);
                match(T__1);
                setState(149);
                variableListSegment();
                setState(150);
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
    public static class VariableListSegmentContext extends ParserRuleContext {
        public List<VariableSegmentContext> variableSegment() {
            return getRuleContexts(VariableSegmentContext.class);
        }

        public VariableSegmentContext variableSegment(int i) {
            return getRuleContext(VariableSegmentContext.class, i);
        }

        public VariableListSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_variableListSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterVariableListSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitVariableListSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitVariableListSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final VariableListSegmentContext variableListSegment() throws RecognitionException {
        VariableListSegmentContext _localctx = new VariableListSegmentContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_variableListSegment);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(152);
                variableSegment(0);
                setState(157);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__3) {
                    {
                        {
                            setState(153);
                            match(T__3);
                            setState(154);
                            variableSegment(0);
                        }
                    }
                    setState(159);
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
    public static class ConditionBetweenSegmentContext extends ParserRuleContext {
        public List<VariableSegmentContext> variableSegment() {
            return getRuleContexts(VariableSegmentContext.class);
        }

        public VariableSegmentContext variableSegment(int i) {
            return getRuleContext(VariableSegmentContext.class, i);
        }

        public TerminalNode KEY_BETWEEN() {
            return getToken(OracleGrammarParser.KEY_BETWEEN, 0);
        }

        public TerminalNode KEY_TO() {
            return getToken(OracleGrammarParser.KEY_TO, 0);
        }

        public ConditionBetweenSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_conditionBetweenSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterConditionBetweenSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitConditionBetweenSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitConditionBetweenSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConditionBetweenSegmentContext conditionBetweenSegment() throws RecognitionException {
        ConditionBetweenSegmentContext _localctx = new ConditionBetweenSegmentContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_conditionBetweenSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(160);
                variableSegment(0);
                setState(161);
                match(KEY_BETWEEN);
                setState(162);
                variableSegment(0);
                setState(163);
                match(KEY_TO);
                setState(164);
                variableSegment(0);
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
    public static class ConditionNotLikeSegmentContext extends ParserRuleContext {
        public List<VariableSegmentContext> variableSegment() {
            return getRuleContexts(VariableSegmentContext.class);
        }

        public VariableSegmentContext variableSegment(int i) {
            return getRuleContext(VariableSegmentContext.class, i);
        }

        public TerminalNode KEY_NOT() {
            return getToken(OracleGrammarParser.KEY_NOT, 0);
        }

        public TerminalNode KEY_LIKE() {
            return getToken(OracleGrammarParser.KEY_LIKE, 0);
        }

        public ConditionNotLikeSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_conditionNotLikeSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterConditionNotLikeSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitConditionNotLikeSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitConditionNotLikeSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConditionNotLikeSegmentContext conditionNotLikeSegment() throws RecognitionException {
        ConditionNotLikeSegmentContext _localctx = new ConditionNotLikeSegmentContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_conditionNotLikeSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(166);
                variableSegment(0);
                setState(167);
                match(KEY_NOT);
                setState(168);
                match(KEY_LIKE);
                setState(169);
                variableSegment(0);
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
    public static class ConditionIsNotNullSegmentContext extends ParserRuleContext {
        public VariableSegmentContext variableSegment() {
            return getRuleContext(VariableSegmentContext.class, 0);
        }

        public TerminalNode KEY_IS() {
            return getToken(OracleGrammarParser.KEY_IS, 0);
        }

        public TerminalNode KEY_NOT() {
            return getToken(OracleGrammarParser.KEY_NOT, 0);
        }

        public TerminalNode TERM_CONST_NULL() {
            return getToken(OracleGrammarParser.TERM_CONST_NULL, 0);
        }

        public ConditionIsNotNullSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_conditionIsNotNullSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterConditionIsNotNullSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitConditionIsNotNullSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitConditionIsNotNullSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConditionIsNotNullSegmentContext conditionIsNotNullSegment() throws RecognitionException {
        ConditionIsNotNullSegmentContext _localctx = new ConditionIsNotNullSegmentContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_conditionIsNotNullSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(171);
                variableSegment(0);
                setState(172);
                match(KEY_IS);
                setState(173);
                match(KEY_NOT);
                setState(174);
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
    public static class ConditionIsNullSegmentContext extends ParserRuleContext {
        public VariableSegmentContext variableSegment() {
            return getRuleContext(VariableSegmentContext.class, 0);
        }

        public TerminalNode KEY_IS() {
            return getToken(OracleGrammarParser.KEY_IS, 0);
        }

        public TerminalNode TERM_CONST_NULL() {
            return getToken(OracleGrammarParser.TERM_CONST_NULL, 0);
        }

        public ConditionIsNullSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_conditionIsNullSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterConditionIsNullSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitConditionIsNullSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitConditionIsNullSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConditionIsNullSegmentContext conditionIsNullSegment() throws RecognitionException {
        ConditionIsNullSegmentContext _localctx = new ConditionIsNullSegmentContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_conditionIsNullSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(176);
                variableSegment(0);
                setState(177);
                match(KEY_IS);
                setState(178);
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
    public static class ConditionCompositeSegmentContext extends ParserRuleContext {
        public List<ConditionSegmentContext> conditionSegment() {
            return getRuleContexts(ConditionSegmentContext.class);
        }

        public ConditionSegmentContext conditionSegment(int i) {
            return getRuleContext(ConditionSegmentContext.class, i);
        }

        public List<TerminalNode> KEY_AND() {
            return getTokens(OracleGrammarParser.KEY_AND);
        }

        public TerminalNode KEY_AND(int i) {
            return getToken(OracleGrammarParser.KEY_AND, i);
        }

        public List<TerminalNode> KEY_OR() {
            return getTokens(OracleGrammarParser.KEY_OR);
        }

        public TerminalNode KEY_OR(int i) {
            return getToken(OracleGrammarParser.KEY_OR, i);
        }

        public ConditionCompositeSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_conditionCompositeSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterConditionCompositeSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitConditionCompositeSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitConditionCompositeSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ConditionCompositeSegmentContext conditionCompositeSegment() throws RecognitionException {
        ConditionCompositeSegmentContext _localctx = new ConditionCompositeSegmentContext(_ctx, getState());
        enterRule(_localctx, 32, RULE_conditionCompositeSegment);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(180);
                conditionSegment();
                setState(186);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == KEY_AND || _la == KEY_OR) {
                    {
                        setState(184);
                        _errHandler.sync(this);
                        switch (_input.LA(1)) {
                            case KEY_AND: {
                                setState(181);
                                match(KEY_AND);
                            }
                            break;
                            case KEY_OR: {
                                setState(182);
                                match(KEY_OR);
                                setState(183);
                                conditionSegment();
                            }
                            break;
                            default:
                                throw new NoViableAltException(this);
                        }
                    }
                    setState(188);
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
    public static class IfElseSegmentContext extends ParserRuleContext {
        public List<TerminalNode> KEY_IF() {
            return getTokens(OracleGrammarParser.KEY_IF);
        }

        public TerminalNode KEY_IF(int i) {
            return getToken(OracleGrammarParser.KEY_IF, i);
        }

        public List<ConditionCompositeSegmentContext> conditionCompositeSegment() {
            return getRuleContexts(ConditionCompositeSegmentContext.class);
        }

        public ConditionCompositeSegmentContext conditionCompositeSegment(int i) {
            return getRuleContext(ConditionCompositeSegmentContext.class, i);
        }

        public List<TerminalNode> KEY_THEN() {
            return getTokens(OracleGrammarParser.KEY_THEN);
        }

        public TerminalNode KEY_THEN(int i) {
            return getToken(OracleGrammarParser.KEY_THEN, i);
        }

        public List<ScriptContext> script() {
            return getRuleContexts(ScriptContext.class);
        }

        public ScriptContext script(int i) {
            return getRuleContext(ScriptContext.class, i);
        }

        public TerminalNode KEY_END() {
            return getToken(OracleGrammarParser.KEY_END, 0);
        }

        public List<TerminalNode> KEY_ELSE() {
            return getTokens(OracleGrammarParser.KEY_ELSE);
        }

        public TerminalNode KEY_ELSE(int i) {
            return getToken(OracleGrammarParser.KEY_ELSE, i);
        }

        public IfElseSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifElseSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).enterIfElseSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).exitIfElseSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitIfElseSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IfElseSegmentContext ifElseSegment() throws RecognitionException {
        IfElseSegmentContext _localctx = new IfElseSegmentContext(_ctx, getState());
        enterRule(_localctx, 34, RULE_ifElseSegment);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(189);
                match(KEY_IF);
                setState(190);
                conditionCompositeSegment();
                setState(191);
                match(KEY_THEN);
                setState(192);
                script();
                setState(201);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 13, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(193);
                                match(KEY_ELSE);
                                setState(194);
                                match(KEY_IF);
                                setState(195);
                                conditionCompositeSegment();
                                setState(196);
                                match(KEY_THEN);
                                setState(197);
                                script();
                            }
                        }
                    }
                    setState(203);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 13, _ctx);
                }
                setState(206);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == KEY_ELSE) {
                    {
                        setState(204);
                        match(KEY_ELSE);
                        setState(205);
                        script();
                    }
                }

                setState(208);
                match(KEY_END);
                setState(210);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == KEY_IF) {
                    {
                        setState(209);
                        match(KEY_IF);
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
    public static class VariableSegmentContext extends ParserRuleContext {
        public SqlStringContext sqlString() {
            return getRuleContext(SqlStringContext.class, 0);
        }

        public SqlNumberContext sqlNumber() {
            return getRuleContext(SqlNumberContext.class, 0);
        }

        public SqlNullContext sqlNull() {
            return getRuleContext(SqlNullContext.class, 0);
        }

        public SqlIdentifierContext sqlIdentifier() {
            return getRuleContext(SqlIdentifierContext.class, 0);
        }

        public List<VariableSegmentContext> variableSegment() {
            return getRuleContexts(VariableSegmentContext.class);
        }

        public VariableSegmentContext variableSegment(int i) {
            return getRuleContext(VariableSegmentContext.class, i);
        }

        public VariableSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_variableSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterVariableSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).exitVariableSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitVariableSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final VariableSegmentContext variableSegment() throws RecognitionException {
        return variableSegment(0);
    }

    private VariableSegmentContext variableSegment(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        VariableSegmentContext _localctx = new VariableSegmentContext(_ctx, _parentState);
        VariableSegmentContext _prevctx = _localctx;
        int _startState = 36;
        enterRecursionRule(_localctx, 36, RULE_variableSegment, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(217);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case TERM_CONST_STRING_SINGLE: {
                        setState(213);
                        sqlString();
                    }
                    break;
                    case TERM_CONST_NUMBER_SCIEN_2:
                    case TERM_CONST_NUMBER_SCIEN_1:
                    case TERM_CONST_NUMBER_FLOAT:
                    case TERM_CONST_NUMBER: {
                        setState(214);
                        sqlNumber();
                    }
                    break;
                    case TERM_CONST_NULL: {
                        setState(215);
                        sqlNull();
                    }
                    break;
                    case IDENTIFIER: {
                        setState(216);
                        sqlIdentifier();
                    }
                    break;
                    default:
                        throw new NoViableAltException(this);
                }
                _ctx.stop = _input.LT(-1);
                setState(228);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 18, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            {
                                _localctx = new VariableSegmentContext(_parentctx, _parentState);
                                pushNewRecursionContext(_localctx, _startState, RULE_variableSegment);
                                setState(219);
                                if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
                                setState(222);
                                _errHandler.sync(this);
                                _alt = 1;
                                do {
                                    switch (_alt) {
                                        case 1: {
                                            {
                                                setState(220);
                                                match(T__11);
                                                setState(221);
                                                variableSegment(0);
                                            }
                                        }
                                        break;
                                        default:
                                            throw new NoViableAltException(this);
                                    }
                                    setState(224);
                                    _errHandler.sync(this);
                                    _alt = getInterpreter().adaptivePredict(_input, 17, _ctx);
                                } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                            }
                        }
                    }
                    setState(230);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 18, _ctx);
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
    public static class FunctionSegmentContext extends ParserRuleContext {
        public SqlIdentifierContext sqlIdentifier() {
            return getRuleContext(SqlIdentifierContext.class, 0);
        }

        public ArgumentListSegmentContext argumentListSegment() {
            return getRuleContext(ArgumentListSegmentContext.class, 0);
        }

        public FunctionSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_functionSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterFunctionSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).exitFunctionSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitFunctionSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FunctionSegmentContext functionSegment() throws RecognitionException {
        FunctionSegmentContext _localctx = new FunctionSegmentContext(_ctx, getState());
        enterRule(_localctx, 38, RULE_functionSegment);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(231);
                sqlIdentifier();
                setState(232);
                match(T__1);
                setState(234);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 70368748306432L) != 0)) {
                    {
                        setState(233);
                        argumentListSegment();
                    }
                }

                setState(236);
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
    public static class ArgumentListSegmentContext extends ParserRuleContext {
        public List<VariableSegmentContext> variableSegment() {
            return getRuleContexts(VariableSegmentContext.class);
        }

        public VariableSegmentContext variableSegment(int i) {
            return getRuleContext(VariableSegmentContext.class, i);
        }

        public ArgumentListSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_argumentListSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterArgumentListSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitArgumentListSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitArgumentListSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ArgumentListSegmentContext argumentListSegment() throws RecognitionException {
        ArgumentListSegmentContext _localctx = new ArgumentListSegmentContext(_ctx, getState());
        enterRule(_localctx, 40, RULE_argumentListSegment);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(238);
                variableSegment(0);
                setState(243);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__3) {
                    {
                        {
                            setState(239);
                            match(T__3);
                            setState(240);
                            variableSegment(0);
                        }
                    }
                    setState(245);
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
    public static class CommitSegmentContext extends ParserRuleContext {
        public TerminalNode KEY_COMMIT() {
            return getToken(OracleGrammarParser.KEY_COMMIT, 0);
        }

        public CommitSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_commitSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).enterCommitSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).exitCommitSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitCommitSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final CommitSegmentContext commitSegment() throws RecognitionException {
        CommitSegmentContext _localctx = new CommitSegmentContext(_ctx, getState());
        enterRule(_localctx, 42, RULE_commitSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(246);
                match(KEY_COMMIT);
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
    public static class RollbackSegmentContext extends ParserRuleContext {
        public TerminalNode KEY_ROLLBACK() {
            return getToken(OracleGrammarParser.KEY_ROLLBACK, 0);
        }

        public RollbackSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_rollbackSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterRollbackSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).exitRollbackSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitRollbackSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final RollbackSegmentContext rollbackSegment() throws RecognitionException {
        RollbackSegmentContext _localctx = new RollbackSegmentContext(_ctx, getState());
        enterRule(_localctx, 44, RULE_rollbackSegment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(248);
                match(KEY_ROLLBACK);
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
    public static class ExecuteImmediadeVariableSegmentContext extends ParserRuleContext {
        public TerminalNode KEY_EXECUTE() {
            return getToken(OracleGrammarParser.KEY_EXECUTE, 0);
        }

        public TerminalNode KEY_IMMEDIATE() {
            return getToken(OracleGrammarParser.KEY_IMMEDIATE, 0);
        }

        public List<TerminalNode> IDENTIFIER() {
            return getTokens(OracleGrammarParser.IDENTIFIER);
        }

        public TerminalNode IDENTIFIER(int i) {
            return getToken(OracleGrammarParser.IDENTIFIER, i);
        }

        public TerminalNode KEY_INTO() {
            return getToken(OracleGrammarParser.KEY_INTO, 0);
        }

        public ExecuteImmediadeVariableSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_executeImmediadeVariableSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).enterExecuteImmediadeVariableSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener)
                ((OracleGrammarListener) listener).exitExecuteImmediadeVariableSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitExecuteImmediadeVariableSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExecuteImmediadeVariableSegmentContext executeImmediadeVariableSegment() throws RecognitionException {
        ExecuteImmediadeVariableSegmentContext _localctx = new ExecuteImmediadeVariableSegmentContext(_ctx, getState());
        enterRule(_localctx, 46, RULE_executeImmediadeVariableSegment);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(250);
                match(KEY_EXECUTE);
                setState(251);
                match(KEY_IMMEDIATE);
                setState(252);
                match(IDENTIFIER);
                setState(255);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == KEY_INTO) {
                    {
                        setState(253);
                        match(KEY_INTO);
                        setState(254);
                        match(IDENTIFIER);
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
    public static class AssignSegmentContext extends ParserRuleContext {
        public SqlIdentifierContext sqlIdentifier() {
            return getRuleContext(SqlIdentifierContext.class, 0);
        }

        public SegmentContext segment() {
            return getRuleContext(SegmentContext.class, 0);
        }

        public AssignSegmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_assignSegment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).enterAssignSegment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).exitAssignSegment(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitAssignSegment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final AssignSegmentContext assignSegment() throws RecognitionException {
        AssignSegmentContext _localctx = new AssignSegmentContext(_ctx, getState());
        enterRule(_localctx, 48, RULE_assignSegment);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(257);
                sqlIdentifier();
                setState(258);
                _la = _input.LA(1);
                if (!(_la == T__10 || _la == T__12)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(259);
                segment();
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
    public static class SqlNullContext extends ParserRuleContext {
        public TerminalNode TERM_CONST_NULL() {
            return getToken(OracleGrammarParser.TERM_CONST_NULL, 0);
        }

        public SqlNullContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_sqlNull;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).enterSqlNull(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).exitSqlNull(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitSqlNull(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SqlNullContext sqlNull() throws RecognitionException {
        SqlNullContext _localctx = new SqlNullContext(_ctx, getState());
        enterRule(_localctx, 50, RULE_sqlNull);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(261);
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
    public static class SqlIdentifierContext extends ParserRuleContext {
        public TerminalNode IDENTIFIER() {
            return getToken(OracleGrammarParser.IDENTIFIER, 0);
        }

        public SqlIdentifierContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_sqlIdentifier;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).enterSqlIdentifier(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).exitSqlIdentifier(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitSqlIdentifier(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SqlIdentifierContext sqlIdentifier() throws RecognitionException {
        SqlIdentifierContext _localctx = new SqlIdentifierContext(_ctx, getState());
        enterRule(_localctx, 52, RULE_sqlIdentifier);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(263);
                match(IDENTIFIER);
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
    public static class SqlNumberContext extends ParserRuleContext {
        public TerminalNode TERM_CONST_NUMBER_SCIEN_2() {
            return getToken(OracleGrammarParser.TERM_CONST_NUMBER_SCIEN_2, 0);
        }

        public TerminalNode TERM_CONST_NUMBER_SCIEN_1() {
            return getToken(OracleGrammarParser.TERM_CONST_NUMBER_SCIEN_1, 0);
        }

        public TerminalNode TERM_CONST_NUMBER_FLOAT() {
            return getToken(OracleGrammarParser.TERM_CONST_NUMBER_FLOAT, 0);
        }

        public TerminalNode TERM_CONST_NUMBER() {
            return getToken(OracleGrammarParser.TERM_CONST_NUMBER, 0);
        }

        public SqlNumberContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_sqlNumber;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).enterSqlNumber(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).exitSqlNumber(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitSqlNumber(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SqlNumberContext sqlNumber() throws RecognitionException {
        SqlNumberContext _localctx = new SqlNumberContext(_ctx, getState());
        enterRule(_localctx, 54, RULE_sqlNumber);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(265);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 3932160L) != 0))) {
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
    public static class SqlStringContext extends ParserRuleContext {
        public TerminalNode TERM_CONST_STRING_SINGLE() {
            return getToken(OracleGrammarParser.TERM_CONST_STRING_SINGLE, 0);
        }

        public SqlStringContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_sqlString;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).enterSqlString(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof OracleGrammarListener) ((OracleGrammarListener) listener).exitSqlString(this);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof OracleGrammarVisitor)
                return ((OracleGrammarVisitor<? extends T>) visitor).visitSqlString(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SqlStringContext sqlString() throws RecognitionException {
        SqlStringContext _localctx = new SqlStringContext(_ctx, getState());
        enterRule(_localctx, 56, RULE_sqlString);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(267);
                match(TERM_CONST_STRING_SINGLE);
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
            case 18:
                return variableSegment_sempred((VariableSegmentContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean variableSegment_sempred(VariableSegmentContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return precpred(_ctx, 5);
        }
        return true;
    }

    public static final String _serializedATN =
            "\u0004\u0001/\u010e\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002" +
                    "\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002" +
                    "\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002" +
                    "\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002" +
                    "\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f" +
                    "\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012" +
                    "\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015" +
                    "\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018" +
                    "\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b" +
                    "\u0002\u001c\u0007\u001c\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001" +
                    "\u0001\u0001\u0001\u0001\u0005\u0001A\b\u0001\n\u0001\f\u0001D\t\u0001" +
                    "\u0001\u0001\u0003\u0001G\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002" +
                    "\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002" +
                    "\u0001\u0002\u0003\u0002S\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003" +
                    "\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003" +
                    "]\b\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004" +
                    "\u0005\u0004d\b\u0004\n\u0004\f\u0004g\t\u0004\u0001\u0005\u0001\u0005" +
                    "\u0003\u0005k\b\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006" +
                    "\u0001\u0006\u0001\u0006\u0003\u0006s\b\u0006\u0001\u0007\u0001\u0007" +
                    "\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007z\b\u0007\u0001\u0007" +
                    "\u0001\u0007\u0003\u0007~\b\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001" +
                    "\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003\b\u008a\b\b\u0001\t\u0001" +
                    "\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001" +
                    "\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000b\u009c" +
                    "\b\u000b\n\u000b\f\u000b\u009f\t\u000b\u0001\f\u0001\f\u0001\f\u0001\f" +
                    "\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001" +
                    "\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001" +
                    "\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0005" +
                    "\u0010\u00b9\b\u0010\n\u0010\f\u0010\u00bc\t\u0010\u0001\u0011\u0001\u0011" +
                    "\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011" +
                    "\u0001\u0011\u0001\u0011\u0005\u0011\u00c8\b\u0011\n\u0011\f\u0011\u00cb" +
                    "\t\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u00cf\b\u0011\u0001\u0011" +
                    "\u0001\u0011\u0003\u0011\u00d3\b\u0011\u0001\u0012\u0001\u0012\u0001\u0012" +
                    "\u0001\u0012\u0001\u0012\u0003\u0012\u00da\b\u0012\u0001\u0012\u0001\u0012" +
                    "\u0001\u0012\u0004\u0012\u00df\b\u0012\u000b\u0012\f\u0012\u00e0\u0005" +
                    "\u0012\u00e3\b\u0012\n\u0012\f\u0012\u00e6\t\u0012\u0001\u0013\u0001\u0013" +
                    "\u0001\u0013\u0003\u0013\u00eb\b\u0013\u0001\u0013\u0001\u0013\u0001\u0014" +
                    "\u0001\u0014\u0001\u0014\u0005\u0014\u00f2\b\u0014\n\u0014\f\u0014\u00f5" +
                    "\t\u0014\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0017\u0001" +
                    "\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u0100\b\u0017\u0001" +
                    "\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001" +
                    "\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0001" +
                    "\u001c\u0000\u0001$\u001d\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012" +
                    "\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468\u0000\u0005\u0001\u0000" +
                    "*+\u0001\u0000,-\u0002\u0000\u0005\u000b##\u0002\u0000\u000b\u000b\r\r" +
                    "\u0001\u0000\u0012\u0015\u0115\u0000:\u0001\u0000\u0000\u0000\u0002=\u0001" +
                    "\u0000\u0000\u0000\u0004R\u0001\u0000\u0000\u0000\u0006T\u0001\u0000\u0000" +
                    "\u0000\b`\u0001\u0000\u0000\u0000\nh\u0001\u0000\u0000\u0000\fn\u0001" +
                    "\u0000\u0000\u0000\u000et\u0001\u0000\u0000\u0000\u0010\u0089\u0001\u0000" +
                    "\u0000\u0000\u0012\u008b\u0001\u0000\u0000\u0000\u0014\u0091\u0001\u0000" +
                    "\u0000\u0000\u0016\u0098\u0001\u0000\u0000\u0000\u0018\u00a0\u0001\u0000" +
                    "\u0000\u0000\u001a\u00a6\u0001\u0000\u0000\u0000\u001c\u00ab\u0001\u0000" +
                    "\u0000\u0000\u001e\u00b0\u0001\u0000\u0000\u0000 \u00b4\u0001\u0000\u0000" +
                    "\u0000\"\u00bd\u0001\u0000\u0000\u0000$\u00d9\u0001\u0000\u0000\u0000" +
                    "&\u00e7\u0001\u0000\u0000\u0000(\u00ee\u0001\u0000\u0000\u0000*\u00f6" +
                    "\u0001\u0000\u0000\u0000,\u00f8\u0001\u0000\u0000\u0000.\u00fa\u0001\u0000" +
                    "\u0000\u00000\u0101\u0001\u0000\u0000\u00002\u0105\u0001\u0000\u0000\u0000" +
                    "4\u0107\u0001\u0000\u0000\u00006\u0109\u0001\u0000\u0000\u00008\u010b" +
                    "\u0001\u0000\u0000\u0000:;\u0003\u0002\u0001\u0000;<\u0005\u0000\u0000" +
                    "\u0001<\u0001\u0001\u0000\u0000\u0000=B\u0003\u0004\u0002\u0000>?\u0005" +
                    "\u0001\u0000\u0000?A\u0003\u0004\u0002\u0000@>\u0001\u0000\u0000\u0000" +
                    "AD\u0001\u0000\u0000\u0000B@\u0001\u0000\u0000\u0000BC\u0001\u0000\u0000" +
                    "\u0000CF\u0001\u0000\u0000\u0000DB\u0001\u0000\u0000\u0000EG\u0005\u0001" +
                    "\u0000\u0000FE\u0001\u0000\u0000\u0000FG\u0001\u0000\u0000\u0000G\u0003" +
                    "\u0001\u0000\u0000\u0000HS\u0003.\u0017\u0000IS\u0003\u0006\u0003\u0000" +
                    "JS\u0003\f\u0006\u0000KS\u00030\u0018\u0000LS\u0003&\u0013\u0000MS\u0003" +
                    "\"\u0011\u0000NS\u0003*\u0015\u0000OS\u0003,\u0016\u0000PS\u0003$\u0012" +
                    "\u0000QS\u0003\u0010\b\u0000RH\u0001\u0000\u0000\u0000RI\u0001\u0000\u0000" +
                    "\u0000RJ\u0001\u0000\u0000\u0000RK\u0001\u0000\u0000\u0000RL\u0001\u0000" +
                    "\u0000\u0000RM\u0001\u0000\u0000\u0000RN\u0001\u0000\u0000\u0000RO\u0001" +
                    "\u0000\u0000\u0000RP\u0001\u0000\u0000\u0000RQ\u0001\u0000\u0000\u0000" +
                    "S\u0005\u0001\u0000\u0000\u0000TU\u0005(\u0000\u0000UV\u0005\u0018\u0000" +
                    "\u0000VW\u0005)\u0000\u0000WX\u0001\u0000\u0000\u0000XY\u0007\u0000\u0000" +
                    "\u0000YZ\u0005.\u0000\u0000Z\\\u0005\u0002\u0000\u0000[]\u0003\b\u0004" +
                    "\u0000\\[\u0001\u0000\u0000\u0000\\]\u0001\u0000\u0000\u0000]^\u0001\u0000" +
                    "\u0000\u0000^_\u0005\u0003\u0000\u0000_\u0007\u0001\u0000\u0000\u0000" +
                    "`e\u0003\n\u0005\u0000ab\u0005\u0004\u0000\u0000bd\u0003\n\u0005\u0000" +
                    "ca\u0001\u0000\u0000\u0000dg\u0001\u0000\u0000\u0000ec\u0001\u0000\u0000" +
                    "\u0000ef\u0001\u0000\u0000\u0000f\t\u0001\u0000\u0000\u0000ge\u0001\u0000" +
                    "\u0000\u0000hj\u0005.\u0000\u0000ik\u0007\u0001\u0000\u0000ji\u0001\u0000" +
                    "\u0000\u0000jk\u0001\u0000\u0000\u0000kl\u0001\u0000\u0000\u0000lm\u0005" +
                    ".\u0000\u0000m\u000b\u0001\u0000\u0000\u0000no\u00034\u001a\u0000or\u0003" +
                    "\u000e\u0007\u0000pq\u0005\'\u0000\u0000qs\u0003$\u0012\u0000rp\u0001" +
                    "\u0000\u0000\u0000rs\u0001\u0000\u0000\u0000s\r\u0001\u0000\u0000\u0000" +
                    "t}\u00034\u001a\u0000uv\u0005\u0002\u0000\u0000vy\u00036\u001b\u0000w" +
                    "x\u0005\u0004\u0000\u0000xz\u00036\u001b\u0000yw\u0001\u0000\u0000\u0000" +
                    "yz\u0001\u0000\u0000\u0000z{\u0001\u0000\u0000\u0000{|\u0005\u0003\u0000" +
                    "\u0000|~\u0001\u0000\u0000\u0000}u\u0001\u0000\u0000\u0000}~\u0001\u0000" +
                    "\u0000\u0000~\u000f\u0001\u0000\u0000\u0000\u007f\u0080\u0003$\u0012\u0000" +
                    "\u0080\u0081\u0007\u0002\u0000\u0000\u0081\u0082\u0003$\u0012\u0000\u0082" +
                    "\u008a\u0001\u0000\u0000\u0000\u0083\u008a\u0003\u001e\u000f\u0000\u0084" +
                    "\u008a\u0003\u001c\u000e\u0000\u0085\u008a\u0003\u001a\r\u0000\u0086\u008a" +
                    "\u0003\u0018\f\u0000\u0087\u008a\u0003\u0012\t\u0000\u0088\u008a\u0003" +
                    "\u0014\n\u0000\u0089\u007f\u0001\u0000\u0000\u0000\u0089\u0083\u0001\u0000" +
                    "\u0000\u0000\u0089\u0084\u0001\u0000\u0000\u0000\u0089\u0085\u0001\u0000" +
                    "\u0000\u0000\u0089\u0086\u0001\u0000\u0000\u0000\u0089\u0087\u0001\u0000" +
                    "\u0000\u0000\u0089\u0088\u0001\u0000\u0000\u0000\u008a\u0011\u0001\u0000" +
                    "\u0000\u0000\u008b\u008c\u0003$\u0012\u0000\u008c\u008d\u0005,\u0000\u0000" +
                    "\u008d\u008e\u0005\u0002\u0000\u0000\u008e\u008f\u0003\u0016\u000b\u0000" +
                    "\u008f\u0090\u0005\u0003\u0000\u0000\u0090\u0013\u0001\u0000\u0000\u0000" +
                    "\u0091\u0092\u0003$\u0012\u0000\u0092\u0093\u0005$\u0000\u0000\u0093\u0094" +
                    "\u0005,\u0000\u0000\u0094\u0095\u0005\u0002\u0000\u0000\u0095\u0096\u0003" +
                    "\u0016\u000b\u0000\u0096\u0097\u0005\u0003\u0000\u0000\u0097\u0015\u0001" +
                    "\u0000\u0000\u0000\u0098\u009d\u0003$\u0012\u0000\u0099\u009a\u0005\u0004" +
                    "\u0000\u0000\u009a\u009c\u0003$\u0012\u0000\u009b\u0099\u0001\u0000\u0000" +
                    "\u0000\u009c\u009f\u0001\u0000\u0000\u0000\u009d\u009b\u0001\u0000\u0000" +
                    "\u0000\u009d\u009e\u0001\u0000\u0000\u0000\u009e\u0017\u0001\u0000\u0000" +
                    "\u0000\u009f\u009d\u0001\u0000\u0000\u0000\u00a0\u00a1\u0003$\u0012\u0000" +
                    "\u00a1\u00a2\u0005%\u0000\u0000\u00a2\u00a3\u0003$\u0012\u0000\u00a3\u00a4" +
                    "\u0005&\u0000\u0000\u00a4\u00a5\u0003$\u0012\u0000\u00a5\u0019\u0001\u0000" +
                    "\u0000\u0000\u00a6\u00a7\u0003$\u0012\u0000\u00a7\u00a8\u0005$\u0000\u0000" +
                    "\u00a8\u00a9\u0005#\u0000\u0000\u00a9\u00aa\u0003$\u0012\u0000\u00aa\u001b" +
                    "\u0001\u0000\u0000\u0000\u00ab\u00ac\u0003$\u0012\u0000\u00ac\u00ad\u0005" +
                    "\"\u0000\u0000\u00ad\u00ae\u0005$\u0000\u0000\u00ae\u00af\u0005\u0011" +
                    "\u0000\u0000\u00af\u001d\u0001\u0000\u0000\u0000\u00b0\u00b1\u0003$\u0012" +
                    "\u0000\u00b1\u00b2\u0005\"\u0000\u0000\u00b2\u00b3\u0005\u0011\u0000\u0000" +
                    "\u00b3\u001f\u0001\u0000\u0000\u0000\u00b4\u00ba\u0003\u0010\b\u0000\u00b5" +
                    "\u00b9\u0005\u0017\u0000\u0000\u00b6\u00b7\u0005\u0018\u0000\u0000\u00b7" +
                    "\u00b9\u0003\u0010\b\u0000\u00b8\u00b5\u0001\u0000\u0000\u0000\u00b8\u00b6" +
                    "\u0001\u0000\u0000\u0000\u00b9\u00bc\u0001\u0000\u0000\u0000\u00ba\u00b8" +
                    "\u0001\u0000\u0000\u0000\u00ba\u00bb\u0001\u0000\u0000\u0000\u00bb!\u0001" +
                    "\u0000\u0000\u0000\u00bc\u00ba\u0001\u0000\u0000\u0000\u00bd\u00be\u0005" +
                    "\u0019\u0000\u0000\u00be\u00bf\u0003 \u0010\u0000\u00bf\u00c0\u0005\u001a" +
                    "\u0000\u0000\u00c0\u00c9\u0003\u0002\u0001\u0000\u00c1\u00c2\u0005\u001b" +
                    "\u0000\u0000\u00c2\u00c3\u0005\u0019\u0000\u0000\u00c3\u00c4\u0003 \u0010" +
                    "\u0000\u00c4\u00c5\u0005\u001a\u0000\u0000\u00c5\u00c6\u0003\u0002\u0001" +
                    "\u0000\u00c6\u00c8\u0001\u0000\u0000\u0000\u00c7\u00c1\u0001\u0000\u0000" +
                    "\u0000\u00c8\u00cb\u0001\u0000\u0000\u0000\u00c9\u00c7\u0001\u0000\u0000" +
                    "\u0000\u00c9\u00ca\u0001\u0000\u0000\u0000\u00ca\u00ce\u0001\u0000\u0000" +
                    "\u0000\u00cb\u00c9\u0001\u0000\u0000\u0000\u00cc\u00cd\u0005\u001b\u0000" +
                    "\u0000\u00cd\u00cf\u0003\u0002\u0001\u0000\u00ce\u00cc\u0001\u0000\u0000" +
                    "\u0000\u00ce\u00cf\u0001\u0000\u0000\u0000\u00cf\u00d0\u0001\u0000\u0000" +
                    "\u0000\u00d0\u00d2\u0005\u001c\u0000\u0000\u00d1\u00d3\u0005\u0019\u0000" +
                    "\u0000\u00d2\u00d1\u0001\u0000\u0000\u0000\u00d2\u00d3\u0001\u0000\u0000" +
                    "\u0000\u00d3#\u0001\u0000\u0000\u0000\u00d4\u00d5\u0006\u0012\uffff\uffff" +
                    "\u0000\u00d5\u00da\u00038\u001c\u0000\u00d6\u00da\u00036\u001b\u0000\u00d7" +
                    "\u00da\u00032\u0019\u0000\u00d8\u00da\u00034\u001a\u0000\u00d9\u00d4\u0001" +
                    "\u0000\u0000\u0000\u00d9\u00d6\u0001\u0000\u0000\u0000\u00d9\u00d7\u0001" +
                    "\u0000\u0000\u0000\u00d9\u00d8\u0001\u0000\u0000\u0000\u00da\u00e4\u0001" +
                    "\u0000\u0000\u0000\u00db\u00de\n\u0005\u0000\u0000\u00dc\u00dd\u0005\f" +
                    "\u0000\u0000\u00dd\u00df\u0003$\u0012\u0000\u00de\u00dc\u0001\u0000\u0000" +
                    "\u0000\u00df\u00e0\u0001\u0000\u0000\u0000\u00e0\u00de\u0001\u0000\u0000" +
                    "\u0000\u00e0\u00e1\u0001\u0000\u0000\u0000\u00e1\u00e3\u0001\u0000\u0000" +
                    "\u0000\u00e2\u00db\u0001\u0000\u0000\u0000\u00e3\u00e6\u0001\u0000\u0000" +
                    "\u0000\u00e4\u00e2\u0001\u0000\u0000\u0000\u00e4\u00e5\u0001\u0000\u0000" +
                    "\u0000\u00e5%\u0001\u0000\u0000\u0000\u00e6\u00e4\u0001\u0000\u0000\u0000" +
                    "\u00e7\u00e8\u00034\u001a\u0000\u00e8\u00ea\u0005\u0002\u0000\u0000\u00e9" +
                    "\u00eb\u0003(\u0014\u0000\u00ea\u00e9\u0001\u0000\u0000\u0000\u00ea\u00eb" +
                    "\u0001\u0000\u0000\u0000\u00eb\u00ec\u0001\u0000\u0000\u0000\u00ec\u00ed" +
                    "\u0005\u0003\u0000\u0000\u00ed\'\u0001\u0000\u0000\u0000\u00ee\u00f3\u0003" +
                    "$\u0012\u0000\u00ef\u00f0\u0005\u0004\u0000\u0000\u00f0\u00f2\u0003$\u0012" +
                    "\u0000\u00f1\u00ef\u0001\u0000\u0000\u0000\u00f2\u00f5\u0001\u0000\u0000" +
                    "\u0000\u00f3\u00f1\u0001\u0000\u0000\u0000\u00f3\u00f4\u0001\u0000\u0000" +
                    "\u0000\u00f4)\u0001\u0000\u0000\u0000\u00f5\u00f3\u0001\u0000\u0000\u0000" +
                    "\u00f6\u00f7\u0005\u001d\u0000\u0000\u00f7+\u0001\u0000\u0000\u0000\u00f8" +
                    "\u00f9\u0005\u001e\u0000\u0000\u00f9-\u0001\u0000\u0000\u0000\u00fa\u00fb" +
                    "\u0005\u001f\u0000\u0000\u00fb\u00fc\u0005 \u0000\u0000\u00fc\u00ff\u0005" +
                    ".\u0000\u0000\u00fd\u00fe\u0005!\u0000\u0000\u00fe\u0100\u0005.\u0000" +
                    "\u0000\u00ff\u00fd\u0001\u0000\u0000\u0000\u00ff\u0100\u0001\u0000\u0000" +
                    "\u0000\u0100/\u0001\u0000\u0000\u0000\u0101\u0102\u00034\u001a\u0000\u0102" +
                    "\u0103\u0007\u0003\u0000\u0000\u0103\u0104\u0003\u0004\u0002\u0000\u0104" +
                    "1\u0001\u0000\u0000\u0000\u0105\u0106\u0005\u0011\u0000\u0000\u01063\u0001" +
                    "\u0000\u0000\u0000\u0107\u0108\u0005.\u0000\u0000\u01085\u0001\u0000\u0000" +
                    "\u0000\u0109\u010a\u0007\u0004\u0000\u0000\u010a7\u0001\u0000\u0000\u0000" +
                    "\u010b\u010c\u0005\u0010\u0000\u0000\u010c9\u0001\u0000\u0000\u0000\u0016" +
                    "BFR\\ejry}\u0089\u009d\u00b8\u00ba\u00c9\u00ce\u00d2\u00d9\u00e0\u00e4" +
                    "\u00ea\u00f3\u00ff";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}