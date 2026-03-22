// Generated from D:/IDEA_ROOT/DevCenter/i2f-turbo/i2f-turbo-java/i2f-tools/i2f-jdbc-procedure-idea-plugin/src/main/java/i2f/extension/antlr4/xproc4j/oracle/grammar/rule/OracleGrammar.g4 by ANTLR 4.13.2

    package i2f.extension.antlr4.xproc4j.oracle.grammar;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class OracleGrammarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		TERM_COMMENT_SINGLE_LINE=18, TERM_COMMENT_MULTI_LINE=19, TERM_CONST_STRING_SINGLE=20, 
		TERM_CONST_NULL=21, TERM_CONST_NUMBER_SCIEN_2=22, TERM_CONST_NUMBER_SCIEN_1=23, 
		TERM_CONST_NUMBER_FLOAT=24, TERM_CONST_NUMBER=25, TERM_INTEGER=26, KEY_AND=27, 
		KEY_OR=28, KEY_IF=29, KEY_THEN=30, KEY_ELSE=31, KEY_ELSIF=32, KEY_END=33, 
		KEY_COMMIT=34, KEY_ROLLBACK=35, KEY_EXECUTE=36, KEY_IMMEDIATE=37, KEY_INTO=38, 
		KEY_IS=39, KEY_LIKE=40, KEY_NOT=41, KEY_BETWEEN=42, KEY_TO=43, KEY_DEFAULT=44, 
		KEY_CREATE=45, KEY_REPLACE=46, KEY_FUNCTION=47, KEY_PROCEDURE=48, KEY_IN=49, 
		KEY_OUT=50, KEY_RETURN=51, IDENTIFIER=52, WS=53;
	public static final int
		RULE_convert = 0, RULE_script = 1, RULE_segment = 2, RULE_returnSegment = 3, 
		RULE_declareProcedureSegment = 4, RULE_argumentDeclareListSegment = 5, 
		RULE_argumentDeclareSegment = 6, RULE_declareVariableSegment = 7, RULE_sqlDataType = 8, 
		RULE_conditionSegment = 9, RULE_conditionInSegment = 10, RULE_conditionNotInSegment = 11, 
		RULE_variableListSegment = 12, RULE_conditionBetweenSegment = 13, RULE_conditionNotLikeSegment = 14, 
		RULE_conditionIsNotNullSegment = 15, RULE_conditionIsNullSegment = 16, 
		RULE_conditionCompositeSegment = 17, RULE_ifElseSegment = 18, RULE_variableSegment = 19, 
		RULE_functionSegment = 20, RULE_argumentListSegment = 21, RULE_commitSegment = 22, 
		RULE_rollbackSegment = 23, RULE_executeImmediadeVariableSegment = 24, 
		RULE_assignSegment = 25, RULE_sqlNull = 26, RULE_sqlIdentifier = 27, RULE_sqlNumber = 28, 
		RULE_sqlString = 29;
	private static String[] makeRuleNames() {
		return new String[] {
			"convert", "script", "segment", "returnSegment", "declareProcedureSegment", 
			"argumentDeclareListSegment", "argumentDeclareSegment", "declareVariableSegment", 
			"sqlDataType", "conditionSegment", "conditionInSegment", "conditionNotInSegment", 
			"variableListSegment", "conditionBetweenSegment", "conditionNotLikeSegment", 
			"conditionIsNotNullSegment", "conditionIsNullSegment", "conditionCompositeSegment", 
			"ifElseSegment", "variableSegment", "functionSegment", "argumentListSegment", 
			"commitSegment", "rollbackSegment", "executeImmediadeVariableSegment", 
			"assignSegment", "sqlNull", "sqlIdentifier", "sqlNumber", "sqlString"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'('", "')'", "','", "':='", "'='", "'>='", "'<='", "'!='", 
			"'<>'", "'>'", "'<'", "'||'", "'*'", "'/'", "'+'", "'-'", null, null, 
			null, "'null'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, "TERM_COMMENT_SINGLE_LINE", "TERM_COMMENT_MULTI_LINE", 
			"TERM_CONST_STRING_SINGLE", "TERM_CONST_NULL", "TERM_CONST_NUMBER_SCIEN_2", 
			"TERM_CONST_NUMBER_SCIEN_1", "TERM_CONST_NUMBER_FLOAT", "TERM_CONST_NUMBER", 
			"TERM_INTEGER", "KEY_AND", "KEY_OR", "KEY_IF", "KEY_THEN", "KEY_ELSE", 
			"KEY_ELSIF", "KEY_END", "KEY_COMMIT", "KEY_ROLLBACK", "KEY_EXECUTE", 
			"KEY_IMMEDIATE", "KEY_INTO", "KEY_IS", "KEY_LIKE", "KEY_NOT", "KEY_BETWEEN", 
			"KEY_TO", "KEY_DEFAULT", "KEY_CREATE", "KEY_REPLACE", "KEY_FUNCTION", 
			"KEY_PROCEDURE", "KEY_IN", "KEY_OUT", "KEY_RETURN", "IDENTIFIER", "WS"
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
	public String getGrammarFileName() { return "OracleGrammar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public OracleGrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConvertContext extends ParserRuleContext {
		public ScriptContext script() {
			return getRuleContext(ScriptContext.class,0);
		}
		public TerminalNode EOF() { return getToken(OracleGrammarParser.EOF, 0); }
		public ConvertContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_convert; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterConvert(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitConvert(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitConvert(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConvertContext convert() throws RecognitionException {
		ConvertContext _localctx = new ConvertContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_convert);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60);
			script();
			setState(61);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
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
			return getRuleContext(SegmentContext.class,i);
		}
		public ScriptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_script; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterScript(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitScript(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitScript(this);
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
			setState(63);
			segment();
			setState(68);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(64);
					match(T__0);
					setState(65);
					segment();
					}
					} 
				}
				setState(70);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(72);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(71);
				match(T__0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SegmentContext extends ParserRuleContext {
		public ExecuteImmediadeVariableSegmentContext executeImmediadeVariableSegment() {
			return getRuleContext(ExecuteImmediadeVariableSegmentContext.class,0);
		}
		public DeclareProcedureSegmentContext declareProcedureSegment() {
			return getRuleContext(DeclareProcedureSegmentContext.class,0);
		}
		public DeclareVariableSegmentContext declareVariableSegment() {
			return getRuleContext(DeclareVariableSegmentContext.class,0);
		}
		public AssignSegmentContext assignSegment() {
			return getRuleContext(AssignSegmentContext.class,0);
		}
		public IfElseSegmentContext ifElseSegment() {
			return getRuleContext(IfElseSegmentContext.class,0);
		}
		public CommitSegmentContext commitSegment() {
			return getRuleContext(CommitSegmentContext.class,0);
		}
		public RollbackSegmentContext rollbackSegment() {
			return getRuleContext(RollbackSegmentContext.class,0);
		}
		public VariableSegmentContext variableSegment() {
			return getRuleContext(VariableSegmentContext.class,0);
		}
		public ReturnSegmentContext returnSegment() {
			return getRuleContext(ReturnSegmentContext.class,0);
		}
		public ConditionCompositeSegmentContext conditionCompositeSegment() {
			return getRuleContext(ConditionCompositeSegmentContext.class,0);
		}
		public SegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_segment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SegmentContext segment() throws RecognitionException {
		SegmentContext _localctx = new SegmentContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_segment);
		try {
			setState(84);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(74);
				executeImmediadeVariableSegment();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(75);
				declareProcedureSegment();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(76);
				declareVariableSegment();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(77);
				assignSegment();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(78);
				ifElseSegment();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(79);
				commitSegment();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(80);
				rollbackSegment();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(81);
				variableSegment(0);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(82);
				returnSegment();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(83);
				conditionCompositeSegment(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnSegmentContext extends ParserRuleContext {
		public TerminalNode KEY_RETURN() { return getToken(OracleGrammarParser.KEY_RETURN, 0); }
		public VariableSegmentContext variableSegment() {
			return getRuleContext(VariableSegmentContext.class,0);
		}
		public ReturnSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterReturnSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitReturnSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitReturnSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnSegmentContext returnSegment() throws RecognitionException {
		ReturnSegmentContext _localctx = new ReturnSegmentContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_returnSegment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			match(KEY_RETURN);
			setState(88);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4503599693561860L) != 0)) {
				{
				setState(87);
				variableSegment(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclareProcedureSegmentContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(OracleGrammarParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(OracleGrammarParser.IDENTIFIER, i);
		}
		public TerminalNode KEY_FUNCTION() { return getToken(OracleGrammarParser.KEY_FUNCTION, 0); }
		public TerminalNode KEY_PROCEDURE() { return getToken(OracleGrammarParser.KEY_PROCEDURE, 0); }
		public TerminalNode KEY_CREATE() { return getToken(OracleGrammarParser.KEY_CREATE, 0); }
		public ArgumentDeclareListSegmentContext argumentDeclareListSegment() {
			return getRuleContext(ArgumentDeclareListSegmentContext.class,0);
		}
		public TerminalNode KEY_RETURN() { return getToken(OracleGrammarParser.KEY_RETURN, 0); }
		public TerminalNode KEY_OR() { return getToken(OracleGrammarParser.KEY_OR, 0); }
		public TerminalNode KEY_REPLACE() { return getToken(OracleGrammarParser.KEY_REPLACE, 0); }
		public DeclareProcedureSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declareProcedureSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterDeclareProcedureSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitDeclareProcedureSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitDeclareProcedureSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclareProcedureSegmentContext declareProcedureSegment() throws RecognitionException {
		DeclareProcedureSegmentContext _localctx = new DeclareProcedureSegmentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_declareProcedureSegment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KEY_CREATE) {
				{
				setState(90);
				match(KEY_CREATE);
				setState(93);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==KEY_OR) {
					{
					setState(91);
					match(KEY_OR);
					setState(92);
					match(KEY_REPLACE);
					}
				}

				}
			}

			setState(97);
			_la = _input.LA(1);
			if ( !(_la==KEY_FUNCTION || _la==KEY_PROCEDURE) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(98);
			match(IDENTIFIER);
			setState(99);
			match(T__1);
			setState(101);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(100);
				argumentDeclareListSegment();
				}
			}

			setState(103);
			match(T__2);
			setState(106);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KEY_RETURN) {
				{
				setState(104);
				match(KEY_RETURN);
				setState(105);
				match(IDENTIFIER);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
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
			return getRuleContext(ArgumentDeclareSegmentContext.class,i);
		}
		public ArgumentDeclareListSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentDeclareListSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterArgumentDeclareListSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitArgumentDeclareListSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitArgumentDeclareListSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentDeclareListSegmentContext argumentDeclareListSegment() throws RecognitionException {
		ArgumentDeclareListSegmentContext _localctx = new ArgumentDeclareListSegmentContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_argumentDeclareListSegment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			argumentDeclareSegment();
			setState(113);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(109);
				match(T__3);
				setState(110);
				argumentDeclareSegment();
				}
				}
				setState(115);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentDeclareSegmentContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(OracleGrammarParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(OracleGrammarParser.IDENTIFIER, i);
		}
		public TerminalNode KEY_IN() { return getToken(OracleGrammarParser.KEY_IN, 0); }
		public TerminalNode KEY_OUT() { return getToken(OracleGrammarParser.KEY_OUT, 0); }
		public ArgumentDeclareSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentDeclareSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterArgumentDeclareSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitArgumentDeclareSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitArgumentDeclareSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentDeclareSegmentContext argumentDeclareSegment() throws RecognitionException {
		ArgumentDeclareSegmentContext _localctx = new ArgumentDeclareSegmentContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_argumentDeclareSegment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			match(IDENTIFIER);
			setState(118);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KEY_IN || _la==KEY_OUT) {
				{
				setState(117);
				_la = _input.LA(1);
				if ( !(_la==KEY_IN || _la==KEY_OUT) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(120);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DeclareVariableSegmentContext extends ParserRuleContext {
		public SqlIdentifierContext sqlIdentifier() {
			return getRuleContext(SqlIdentifierContext.class,0);
		}
		public SqlDataTypeContext sqlDataType() {
			return getRuleContext(SqlDataTypeContext.class,0);
		}
		public VariableSegmentContext variableSegment() {
			return getRuleContext(VariableSegmentContext.class,0);
		}
		public TerminalNode KEY_DEFAULT() { return getToken(OracleGrammarParser.KEY_DEFAULT, 0); }
		public DeclareVariableSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declareVariableSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterDeclareVariableSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitDeclareVariableSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitDeclareVariableSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclareVariableSegmentContext declareVariableSegment() throws RecognitionException {
		DeclareVariableSegmentContext _localctx = new DeclareVariableSegmentContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_declareVariableSegment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			sqlIdentifier();
			setState(123);
			sqlDataType();
			setState(126);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 17592186044512L) != 0)) {
				{
				setState(124);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 17592186044512L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(125);
				variableSegment(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SqlDataTypeContext extends ParserRuleContext {
		public SqlIdentifierContext sqlIdentifier() {
			return getRuleContext(SqlIdentifierContext.class,0);
		}
		public List<SqlNumberContext> sqlNumber() {
			return getRuleContexts(SqlNumberContext.class);
		}
		public SqlNumberContext sqlNumber(int i) {
			return getRuleContext(SqlNumberContext.class,i);
		}
		public SqlDataTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sqlDataType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterSqlDataType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitSqlDataType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitSqlDataType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SqlDataTypeContext sqlDataType() throws RecognitionException {
		SqlDataTypeContext _localctx = new SqlDataTypeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_sqlDataType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128);
			sqlIdentifier();
			setState(137);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(129);
				match(T__1);
				setState(130);
				sqlNumber();
				setState(133);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(131);
					match(T__3);
					setState(132);
					sqlNumber();
					}
				}

				setState(135);
				match(T__2);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
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
			return getRuleContext(VariableSegmentContext.class,i);
		}
		public TerminalNode KEY_LIKE() { return getToken(OracleGrammarParser.KEY_LIKE, 0); }
		public ConditionIsNullSegmentContext conditionIsNullSegment() {
			return getRuleContext(ConditionIsNullSegmentContext.class,0);
		}
		public ConditionIsNotNullSegmentContext conditionIsNotNullSegment() {
			return getRuleContext(ConditionIsNotNullSegmentContext.class,0);
		}
		public ConditionNotLikeSegmentContext conditionNotLikeSegment() {
			return getRuleContext(ConditionNotLikeSegmentContext.class,0);
		}
		public ConditionBetweenSegmentContext conditionBetweenSegment() {
			return getRuleContext(ConditionBetweenSegmentContext.class,0);
		}
		public ConditionInSegmentContext conditionInSegment() {
			return getRuleContext(ConditionInSegmentContext.class,0);
		}
		public ConditionNotInSegmentContext conditionNotInSegment() {
			return getRuleContext(ConditionNotInSegmentContext.class,0);
		}
		public ConditionSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterConditionSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitConditionSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitConditionSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionSegmentContext conditionSegment() throws RecognitionException {
		ConditionSegmentContext _localctx = new ConditionSegmentContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_conditionSegment);
		int _la;
		try {
			setState(149);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(139);
				variableSegment(0);
				setState(140);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1099511635904L) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(141);
				variableSegment(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(143);
				conditionIsNullSegment();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(144);
				conditionIsNotNullSegment();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(145);
				conditionNotLikeSegment();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(146);
				conditionBetweenSegment();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(147);
				conditionInSegment();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(148);
				conditionNotInSegment();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionInSegmentContext extends ParserRuleContext {
		public VariableSegmentContext variableSegment() {
			return getRuleContext(VariableSegmentContext.class,0);
		}
		public TerminalNode KEY_IN() { return getToken(OracleGrammarParser.KEY_IN, 0); }
		public VariableListSegmentContext variableListSegment() {
			return getRuleContext(VariableListSegmentContext.class,0);
		}
		public ConditionInSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionInSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterConditionInSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitConditionInSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitConditionInSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionInSegmentContext conditionInSegment() throws RecognitionException {
		ConditionInSegmentContext _localctx = new ConditionInSegmentContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_conditionInSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(151);
			variableSegment(0);
			setState(152);
			match(KEY_IN);
			setState(153);
			match(T__1);
			setState(154);
			variableListSegment();
			setState(155);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionNotInSegmentContext extends ParserRuleContext {
		public VariableSegmentContext variableSegment() {
			return getRuleContext(VariableSegmentContext.class,0);
		}
		public TerminalNode KEY_NOT() { return getToken(OracleGrammarParser.KEY_NOT, 0); }
		public TerminalNode KEY_IN() { return getToken(OracleGrammarParser.KEY_IN, 0); }
		public VariableListSegmentContext variableListSegment() {
			return getRuleContext(VariableListSegmentContext.class,0);
		}
		public ConditionNotInSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionNotInSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterConditionNotInSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitConditionNotInSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitConditionNotInSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionNotInSegmentContext conditionNotInSegment() throws RecognitionException {
		ConditionNotInSegmentContext _localctx = new ConditionNotInSegmentContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_conditionNotInSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			variableSegment(0);
			setState(158);
			match(KEY_NOT);
			setState(159);
			match(KEY_IN);
			setState(160);
			match(T__1);
			setState(161);
			variableListSegment();
			setState(162);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
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
			return getRuleContext(VariableSegmentContext.class,i);
		}
		public VariableListSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableListSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterVariableListSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitVariableListSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitVariableListSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableListSegmentContext variableListSegment() throws RecognitionException {
		VariableListSegmentContext _localctx = new VariableListSegmentContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_variableListSegment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			variableSegment(0);
			setState(169);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(165);
				match(T__3);
				setState(166);
				variableSegment(0);
				}
				}
				setState(171);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
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
			return getRuleContext(VariableSegmentContext.class,i);
		}
		public TerminalNode KEY_BETWEEN() { return getToken(OracleGrammarParser.KEY_BETWEEN, 0); }
		public TerminalNode KEY_TO() { return getToken(OracleGrammarParser.KEY_TO, 0); }
		public ConditionBetweenSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionBetweenSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterConditionBetweenSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitConditionBetweenSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitConditionBetweenSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionBetweenSegmentContext conditionBetweenSegment() throws RecognitionException {
		ConditionBetweenSegmentContext _localctx = new ConditionBetweenSegmentContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_conditionBetweenSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			variableSegment(0);
			setState(173);
			match(KEY_BETWEEN);
			setState(174);
			variableSegment(0);
			setState(175);
			match(KEY_TO);
			setState(176);
			variableSegment(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
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
			return getRuleContext(VariableSegmentContext.class,i);
		}
		public TerminalNode KEY_NOT() { return getToken(OracleGrammarParser.KEY_NOT, 0); }
		public TerminalNode KEY_LIKE() { return getToken(OracleGrammarParser.KEY_LIKE, 0); }
		public ConditionNotLikeSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionNotLikeSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterConditionNotLikeSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitConditionNotLikeSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitConditionNotLikeSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionNotLikeSegmentContext conditionNotLikeSegment() throws RecognitionException {
		ConditionNotLikeSegmentContext _localctx = new ConditionNotLikeSegmentContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_conditionNotLikeSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			variableSegment(0);
			setState(179);
			match(KEY_NOT);
			setState(180);
			match(KEY_LIKE);
			setState(181);
			variableSegment(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionIsNotNullSegmentContext extends ParserRuleContext {
		public VariableSegmentContext variableSegment() {
			return getRuleContext(VariableSegmentContext.class,0);
		}
		public TerminalNode KEY_IS() { return getToken(OracleGrammarParser.KEY_IS, 0); }
		public TerminalNode KEY_NOT() { return getToken(OracleGrammarParser.KEY_NOT, 0); }
		public TerminalNode TERM_CONST_NULL() { return getToken(OracleGrammarParser.TERM_CONST_NULL, 0); }
		public ConditionIsNotNullSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionIsNotNullSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterConditionIsNotNullSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitConditionIsNotNullSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitConditionIsNotNullSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionIsNotNullSegmentContext conditionIsNotNullSegment() throws RecognitionException {
		ConditionIsNotNullSegmentContext _localctx = new ConditionIsNotNullSegmentContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_conditionIsNotNullSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			variableSegment(0);
			setState(184);
			match(KEY_IS);
			setState(185);
			match(KEY_NOT);
			setState(186);
			match(TERM_CONST_NULL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionIsNullSegmentContext extends ParserRuleContext {
		public VariableSegmentContext variableSegment() {
			return getRuleContext(VariableSegmentContext.class,0);
		}
		public TerminalNode KEY_IS() { return getToken(OracleGrammarParser.KEY_IS, 0); }
		public TerminalNode TERM_CONST_NULL() { return getToken(OracleGrammarParser.TERM_CONST_NULL, 0); }
		public ConditionIsNullSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionIsNullSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterConditionIsNullSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitConditionIsNullSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitConditionIsNullSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionIsNullSegmentContext conditionIsNullSegment() throws RecognitionException {
		ConditionIsNullSegmentContext _localctx = new ConditionIsNullSegmentContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_conditionIsNullSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(188);
			variableSegment(0);
			setState(189);
			match(KEY_IS);
			setState(190);
			match(TERM_CONST_NULL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConditionCompositeSegmentContext extends ParserRuleContext {
		public List<ConditionCompositeSegmentContext> conditionCompositeSegment() {
			return getRuleContexts(ConditionCompositeSegmentContext.class);
		}
		public ConditionCompositeSegmentContext conditionCompositeSegment(int i) {
			return getRuleContext(ConditionCompositeSegmentContext.class,i);
		}
		public ConditionSegmentContext conditionSegment() {
			return getRuleContext(ConditionSegmentContext.class,0);
		}
		public TerminalNode KEY_AND() { return getToken(OracleGrammarParser.KEY_AND, 0); }
		public TerminalNode KEY_OR() { return getToken(OracleGrammarParser.KEY_OR, 0); }
		public ConditionCompositeSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionCompositeSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterConditionCompositeSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitConditionCompositeSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitConditionCompositeSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionCompositeSegmentContext conditionCompositeSegment() throws RecognitionException {
		return conditionCompositeSegment(0);
	}

	private ConditionCompositeSegmentContext conditionCompositeSegment(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ConditionCompositeSegmentContext _localctx = new ConditionCompositeSegmentContext(_ctx, _parentState);
		ConditionCompositeSegmentContext _prevctx = _localctx;
		int _startState = 34;
		enterRecursionRule(_localctx, 34, RULE_conditionCompositeSegment, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(193);
				match(T__1);
				setState(194);
				conditionCompositeSegment(0);
				setState(195);
				match(T__2);
				}
				break;
			case 2:
				{
				setState(197);
				conditionSegment();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(205);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ConditionCompositeSegmentContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_conditionCompositeSegment);
					setState(200);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(201);
					_la = _input.LA(1);
					if ( !(_la==KEY_AND || _la==KEY_OR) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(202);
					conditionCompositeSegment(3);
					}
					} 
				}
				setState(207);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfElseSegmentContext extends ParserRuleContext {
		public List<TerminalNode> KEY_IF() { return getTokens(OracleGrammarParser.KEY_IF); }
		public TerminalNode KEY_IF(int i) {
			return getToken(OracleGrammarParser.KEY_IF, i);
		}
		public List<ConditionCompositeSegmentContext> conditionCompositeSegment() {
			return getRuleContexts(ConditionCompositeSegmentContext.class);
		}
		public ConditionCompositeSegmentContext conditionCompositeSegment(int i) {
			return getRuleContext(ConditionCompositeSegmentContext.class,i);
		}
		public List<TerminalNode> KEY_THEN() { return getTokens(OracleGrammarParser.KEY_THEN); }
		public TerminalNode KEY_THEN(int i) {
			return getToken(OracleGrammarParser.KEY_THEN, i);
		}
		public List<ScriptContext> script() {
			return getRuleContexts(ScriptContext.class);
		}
		public ScriptContext script(int i) {
			return getRuleContext(ScriptContext.class,i);
		}
		public TerminalNode KEY_END() { return getToken(OracleGrammarParser.KEY_END, 0); }
		public List<TerminalNode> KEY_ELSE() { return getTokens(OracleGrammarParser.KEY_ELSE); }
		public TerminalNode KEY_ELSE(int i) {
			return getToken(OracleGrammarParser.KEY_ELSE, i);
		}
		public List<TerminalNode> KEY_ELSIF() { return getTokens(OracleGrammarParser.KEY_ELSIF); }
		public TerminalNode KEY_ELSIF(int i) {
			return getToken(OracleGrammarParser.KEY_ELSIF, i);
		}
		public IfElseSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifElseSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterIfElseSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitIfElseSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitIfElseSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfElseSegmentContext ifElseSegment() throws RecognitionException {
		IfElseSegmentContext _localctx = new IfElseSegmentContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_ifElseSegment);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(208);
			match(KEY_IF);
			setState(209);
			conditionCompositeSegment(0);
			setState(210);
			match(KEY_THEN);
			setState(211);
			script();
			setState(223);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(215);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case KEY_ELSE:
						{
						setState(212);
						match(KEY_ELSE);
						setState(213);
						match(KEY_IF);
						}
						break;
					case KEY_ELSIF:
						{
						setState(214);
						match(KEY_ELSIF);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(217);
					conditionCompositeSegment(0);
					setState(218);
					match(KEY_THEN);
					setState(219);
					script();
					}
					} 
				}
				setState(225);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			}
			setState(228);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KEY_ELSE) {
				{
				setState(226);
				match(KEY_ELSE);
				setState(227);
				script();
				}
			}

			setState(230);
			match(KEY_END);
			setState(232);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KEY_IF) {
				{
				setState(231);
				match(KEY_IF);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableSegmentContext extends ParserRuleContext {
		public List<VariableSegmentContext> variableSegment() {
			return getRuleContexts(VariableSegmentContext.class);
		}
		public VariableSegmentContext variableSegment(int i) {
			return getRuleContext(VariableSegmentContext.class,i);
		}
		public FunctionSegmentContext functionSegment() {
			return getRuleContext(FunctionSegmentContext.class,0);
		}
		public SqlStringContext sqlString() {
			return getRuleContext(SqlStringContext.class,0);
		}
		public SqlNumberContext sqlNumber() {
			return getRuleContext(SqlNumberContext.class,0);
		}
		public SqlNullContext sqlNull() {
			return getRuleContext(SqlNullContext.class,0);
		}
		public SqlIdentifierContext sqlIdentifier() {
			return getRuleContext(SqlIdentifierContext.class,0);
		}
		public VariableSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterVariableSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitVariableSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitVariableSegment(this);
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
		int _startState = 38;
		enterRecursionRule(_localctx, 38, RULE_variableSegment, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(235);
				match(T__1);
				setState(236);
				variableSegment(0);
				setState(237);
				match(T__2);
				}
				break;
			case 2:
				{
				setState(239);
				match(T__16);
				setState(240);
				variableSegment(6);
				}
				break;
			case 3:
				{
				setState(241);
				functionSegment();
				}
				break;
			case 4:
				{
				setState(242);
				sqlString();
				}
				break;
			case 5:
				{
				setState(243);
				sqlNumber();
				}
				break;
			case 6:
				{
				setState(244);
				sqlNull();
				}
				break;
			case 7:
				{
				setState(245);
				sqlIdentifier();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(263);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(261);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
					case 1:
						{
						_localctx = new VariableSegmentContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_variableSegment);
						setState(248);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(249);
						_la = _input.LA(1);
						if ( !(_la==T__13 || _la==T__14) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(250);
						variableSegment(9);
						}
						break;
					case 2:
						{
						_localctx = new VariableSegmentContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_variableSegment);
						setState(251);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(252);
						_la = _input.LA(1);
						if ( !(_la==T__15 || _la==T__16) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(253);
						variableSegment(8);
						}
						break;
					case 3:
						{
						_localctx = new VariableSegmentContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_variableSegment);
						setState(254);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(257); 
						_errHandler.sync(this);
						_alt = 1;
						do {
							switch (_alt) {
							case 1:
								{
								{
								setState(255);
								match(T__12);
								setState(256);
								variableSegment(0);
								}
								}
								break;
							default:
								throw new NoViableAltException(this);
							}
							setState(259); 
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
						} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
						}
						break;
					}
					} 
				}
				setState(265);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionSegmentContext extends ParserRuleContext {
		public SqlIdentifierContext sqlIdentifier() {
			return getRuleContext(SqlIdentifierContext.class,0);
		}
		public ArgumentListSegmentContext argumentListSegment() {
			return getRuleContext(ArgumentListSegmentContext.class,0);
		}
		public FunctionSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterFunctionSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitFunctionSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitFunctionSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionSegmentContext functionSegment() throws RecognitionException {
		FunctionSegmentContext _localctx = new FunctionSegmentContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_functionSegment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(266);
			sqlIdentifier();
			setState(267);
			match(T__1);
			setState(269);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4503599693561860L) != 0)) {
				{
				setState(268);
				argumentListSegment();
				}
			}

			setState(271);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
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
			return getRuleContext(VariableSegmentContext.class,i);
		}
		public ArgumentListSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentListSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterArgumentListSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitArgumentListSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitArgumentListSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListSegmentContext argumentListSegment() throws RecognitionException {
		ArgumentListSegmentContext _localctx = new ArgumentListSegmentContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_argumentListSegment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(273);
			variableSegment(0);
			setState(278);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__3) {
				{
				{
				setState(274);
				match(T__3);
				setState(275);
				variableSegment(0);
				}
				}
				setState(280);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CommitSegmentContext extends ParserRuleContext {
		public TerminalNode KEY_COMMIT() { return getToken(OracleGrammarParser.KEY_COMMIT, 0); }
		public CommitSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commitSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterCommitSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitCommitSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitCommitSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommitSegmentContext commitSegment() throws RecognitionException {
		CommitSegmentContext _localctx = new CommitSegmentContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_commitSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(281);
			match(KEY_COMMIT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RollbackSegmentContext extends ParserRuleContext {
		public TerminalNode KEY_ROLLBACK() { return getToken(OracleGrammarParser.KEY_ROLLBACK, 0); }
		public RollbackSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rollbackSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterRollbackSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitRollbackSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitRollbackSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RollbackSegmentContext rollbackSegment() throws RecognitionException {
		RollbackSegmentContext _localctx = new RollbackSegmentContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_rollbackSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			match(KEY_ROLLBACK);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExecuteImmediadeVariableSegmentContext extends ParserRuleContext {
		public TerminalNode KEY_EXECUTE() { return getToken(OracleGrammarParser.KEY_EXECUTE, 0); }
		public TerminalNode KEY_IMMEDIATE() { return getToken(OracleGrammarParser.KEY_IMMEDIATE, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(OracleGrammarParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(OracleGrammarParser.IDENTIFIER, i);
		}
		public SqlStringContext sqlString() {
			return getRuleContext(SqlStringContext.class,0);
		}
		public TerminalNode KEY_INTO() { return getToken(OracleGrammarParser.KEY_INTO, 0); }
		public ExecuteImmediadeVariableSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_executeImmediadeVariableSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterExecuteImmediadeVariableSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitExecuteImmediadeVariableSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitExecuteImmediadeVariableSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExecuteImmediadeVariableSegmentContext executeImmediadeVariableSegment() throws RecognitionException {
		ExecuteImmediadeVariableSegmentContext _localctx = new ExecuteImmediadeVariableSegmentContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_executeImmediadeVariableSegment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(285);
			match(KEY_EXECUTE);
			setState(286);
			match(KEY_IMMEDIATE);
			setState(289);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(287);
				match(IDENTIFIER);
				}
				break;
			case TERM_CONST_STRING_SINGLE:
				{
				setState(288);
				sqlString();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(293);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KEY_INTO) {
				{
				setState(291);
				match(KEY_INTO);
				setState(292);
				match(IDENTIFIER);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AssignSegmentContext extends ParserRuleContext {
		public SqlIdentifierContext sqlIdentifier() {
			return getRuleContext(SqlIdentifierContext.class,0);
		}
		public SegmentContext segment() {
			return getRuleContext(SegmentContext.class,0);
		}
		public AssignSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterAssignSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitAssignSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitAssignSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignSegmentContext assignSegment() throws RecognitionException {
		AssignSegmentContext _localctx = new AssignSegmentContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_assignSegment);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			sqlIdentifier();
			setState(296);
			_la = _input.LA(1);
			if ( !(_la==T__4 || _la==T__5) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(297);
			segment();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SqlNullContext extends ParserRuleContext {
		public TerminalNode TERM_CONST_NULL() { return getToken(OracleGrammarParser.TERM_CONST_NULL, 0); }
		public SqlNullContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sqlNull; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterSqlNull(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitSqlNull(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitSqlNull(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SqlNullContext sqlNull() throws RecognitionException {
		SqlNullContext _localctx = new SqlNullContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_sqlNull);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(299);
			match(TERM_CONST_NULL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SqlIdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(OracleGrammarParser.IDENTIFIER, 0); }
		public SqlIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sqlIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterSqlIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitSqlIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitSqlIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SqlIdentifierContext sqlIdentifier() throws RecognitionException {
		SqlIdentifierContext _localctx = new SqlIdentifierContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_sqlIdentifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(301);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SqlNumberContext extends ParserRuleContext {
		public TerminalNode TERM_CONST_NUMBER_SCIEN_2() { return getToken(OracleGrammarParser.TERM_CONST_NUMBER_SCIEN_2, 0); }
		public TerminalNode TERM_CONST_NUMBER_SCIEN_1() { return getToken(OracleGrammarParser.TERM_CONST_NUMBER_SCIEN_1, 0); }
		public TerminalNode TERM_CONST_NUMBER_FLOAT() { return getToken(OracleGrammarParser.TERM_CONST_NUMBER_FLOAT, 0); }
		public TerminalNode TERM_CONST_NUMBER() { return getToken(OracleGrammarParser.TERM_CONST_NUMBER, 0); }
		public SqlNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sqlNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterSqlNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitSqlNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitSqlNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SqlNumberContext sqlNumber() throws RecognitionException {
		SqlNumberContext _localctx = new SqlNumberContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_sqlNumber);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(303);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 62914560L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SqlStringContext extends ParserRuleContext {
		public TerminalNode TERM_CONST_STRING_SINGLE() { return getToken(OracleGrammarParser.TERM_CONST_STRING_SINGLE, 0); }
		public SqlStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sqlString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).enterSqlString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof OracleGrammarListener ) ((OracleGrammarListener)listener).exitSqlString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof OracleGrammarVisitor ) return ((OracleGrammarVisitor<? extends T>)visitor).visitSqlString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SqlStringContext sqlString() throws RecognitionException {
		SqlStringContext _localctx = new SqlStringContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_sqlString);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
			match(TERM_CONST_STRING_SINGLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 17:
			return conditionCompositeSegment_sempred((ConditionCompositeSegmentContext)_localctx, predIndex);
		case 19:
			return variableSegment_sempred((VariableSegmentContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean conditionCompositeSegment_sempred(ConditionCompositeSegmentContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean variableSegment_sempred(VariableSegmentContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 8);
		case 2:
			return precpred(_ctx, 7);
		case 3:
			return precpred(_ctx, 9);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u00015\u0134\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001C\b\u0001"+
		"\n\u0001\f\u0001F\t\u0001\u0001\u0001\u0003\u0001I\b\u0001\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002U\b\u0002\u0001\u0003"+
		"\u0001\u0003\u0003\u0003Y\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0003\u0004^\b\u0004\u0003\u0004`\b\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0003\u0004f\b\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0003\u0004k\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0005"+
		"\u0005p\b\u0005\n\u0005\f\u0005s\t\u0005\u0001\u0006\u0001\u0006\u0003"+
		"\u0006w\b\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0003\u0007\u007f\b\u0007\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0003\b\u0086\b\b\u0001\b\u0001\b\u0003\b\u008a\b\b\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0003\t\u0096\b\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\f\u0001\f\u0001\f\u0005\f\u00a8\b\f\n\f\f\f\u00ab\t\f\u0001"+
		"\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0003"+
		"\u0011\u00c7\b\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u00cc"+
		"\b\u0011\n\u0011\f\u0011\u00cf\t\u0011\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u00d8\b\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u00de\b\u0012"+
		"\n\u0012\f\u0012\u00e1\t\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u00e5"+
		"\b\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u00e9\b\u0012\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013"+
		"\u00f7\b\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0004\u0013\u0102\b\u0013"+
		"\u000b\u0013\f\u0013\u0103\u0005\u0013\u0106\b\u0013\n\u0013\f\u0013\u0109"+
		"\t\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u010e\b\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0005\u0015"+
		"\u0115\b\u0015\n\u0015\f\u0015\u0118\t\u0015\u0001\u0016\u0001\u0016\u0001"+
		"\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003"+
		"\u0018\u0122\b\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u0126\b\u0018"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u001a\u0001\u001a"+
		"\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0000\u0002\"&\u001e\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:\u0000"+
		"\t\u0001\u0000/0\u0001\u000012\u0002\u0000\u0005\u0006,,\u0002\u0000\u0006"+
		"\f((\u0001\u0000\u001b\u001c\u0001\u0000\u000e\u000f\u0001\u0000\u0010"+
		"\u0011\u0001\u0000\u0005\u0006\u0001\u0000\u0016\u0019\u0145\u0000<\u0001"+
		"\u0000\u0000\u0000\u0002?\u0001\u0000\u0000\u0000\u0004T\u0001\u0000\u0000"+
		"\u0000\u0006V\u0001\u0000\u0000\u0000\b_\u0001\u0000\u0000\u0000\nl\u0001"+
		"\u0000\u0000\u0000\ft\u0001\u0000\u0000\u0000\u000ez\u0001\u0000\u0000"+
		"\u0000\u0010\u0080\u0001\u0000\u0000\u0000\u0012\u0095\u0001\u0000\u0000"+
		"\u0000\u0014\u0097\u0001\u0000\u0000\u0000\u0016\u009d\u0001\u0000\u0000"+
		"\u0000\u0018\u00a4\u0001\u0000\u0000\u0000\u001a\u00ac\u0001\u0000\u0000"+
		"\u0000\u001c\u00b2\u0001\u0000\u0000\u0000\u001e\u00b7\u0001\u0000\u0000"+
		"\u0000 \u00bc\u0001\u0000\u0000\u0000\"\u00c6\u0001\u0000\u0000\u0000"+
		"$\u00d0\u0001\u0000\u0000\u0000&\u00f6\u0001\u0000\u0000\u0000(\u010a"+
		"\u0001\u0000\u0000\u0000*\u0111\u0001\u0000\u0000\u0000,\u0119\u0001\u0000"+
		"\u0000\u0000.\u011b\u0001\u0000\u0000\u00000\u011d\u0001\u0000\u0000\u0000"+
		"2\u0127\u0001\u0000\u0000\u00004\u012b\u0001\u0000\u0000\u00006\u012d"+
		"\u0001\u0000\u0000\u00008\u012f\u0001\u0000\u0000\u0000:\u0131\u0001\u0000"+
		"\u0000\u0000<=\u0003\u0002\u0001\u0000=>\u0005\u0000\u0000\u0001>\u0001"+
		"\u0001\u0000\u0000\u0000?D\u0003\u0004\u0002\u0000@A\u0005\u0001\u0000"+
		"\u0000AC\u0003\u0004\u0002\u0000B@\u0001\u0000\u0000\u0000CF\u0001\u0000"+
		"\u0000\u0000DB\u0001\u0000\u0000\u0000DE\u0001\u0000\u0000\u0000EH\u0001"+
		"\u0000\u0000\u0000FD\u0001\u0000\u0000\u0000GI\u0005\u0001\u0000\u0000"+
		"HG\u0001\u0000\u0000\u0000HI\u0001\u0000\u0000\u0000I\u0003\u0001\u0000"+
		"\u0000\u0000JU\u00030\u0018\u0000KU\u0003\b\u0004\u0000LU\u0003\u000e"+
		"\u0007\u0000MU\u00032\u0019\u0000NU\u0003$\u0012\u0000OU\u0003,\u0016"+
		"\u0000PU\u0003.\u0017\u0000QU\u0003&\u0013\u0000RU\u0003\u0006\u0003\u0000"+
		"SU\u0003\"\u0011\u0000TJ\u0001\u0000\u0000\u0000TK\u0001\u0000\u0000\u0000"+
		"TL\u0001\u0000\u0000\u0000TM\u0001\u0000\u0000\u0000TN\u0001\u0000\u0000"+
		"\u0000TO\u0001\u0000\u0000\u0000TP\u0001\u0000\u0000\u0000TQ\u0001\u0000"+
		"\u0000\u0000TR\u0001\u0000\u0000\u0000TS\u0001\u0000\u0000\u0000U\u0005"+
		"\u0001\u0000\u0000\u0000VX\u00053\u0000\u0000WY\u0003&\u0013\u0000XW\u0001"+
		"\u0000\u0000\u0000XY\u0001\u0000\u0000\u0000Y\u0007\u0001\u0000\u0000"+
		"\u0000Z]\u0005-\u0000\u0000[\\\u0005\u001c\u0000\u0000\\^\u0005.\u0000"+
		"\u0000][\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000\u0000^`\u0001\u0000"+
		"\u0000\u0000_Z\u0001\u0000\u0000\u0000_`\u0001\u0000\u0000\u0000`a\u0001"+
		"\u0000\u0000\u0000ab\u0007\u0000\u0000\u0000bc\u00054\u0000\u0000ce\u0005"+
		"\u0002\u0000\u0000df\u0003\n\u0005\u0000ed\u0001\u0000\u0000\u0000ef\u0001"+
		"\u0000\u0000\u0000fg\u0001\u0000\u0000\u0000gj\u0005\u0003\u0000\u0000"+
		"hi\u00053\u0000\u0000ik\u00054\u0000\u0000jh\u0001\u0000\u0000\u0000j"+
		"k\u0001\u0000\u0000\u0000k\t\u0001\u0000\u0000\u0000lq\u0003\f\u0006\u0000"+
		"mn\u0005\u0004\u0000\u0000np\u0003\f\u0006\u0000om\u0001\u0000\u0000\u0000"+
		"ps\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000qr\u0001\u0000\u0000"+
		"\u0000r\u000b\u0001\u0000\u0000\u0000sq\u0001\u0000\u0000\u0000tv\u0005"+
		"4\u0000\u0000uw\u0007\u0001\u0000\u0000vu\u0001\u0000\u0000\u0000vw\u0001"+
		"\u0000\u0000\u0000wx\u0001\u0000\u0000\u0000xy\u00054\u0000\u0000y\r\u0001"+
		"\u0000\u0000\u0000z{\u00036\u001b\u0000{~\u0003\u0010\b\u0000|}\u0007"+
		"\u0002\u0000\u0000}\u007f\u0003&\u0013\u0000~|\u0001\u0000\u0000\u0000"+
		"~\u007f\u0001\u0000\u0000\u0000\u007f\u000f\u0001\u0000\u0000\u0000\u0080"+
		"\u0089\u00036\u001b\u0000\u0081\u0082\u0005\u0002\u0000\u0000\u0082\u0085"+
		"\u00038\u001c\u0000\u0083\u0084\u0005\u0004\u0000\u0000\u0084\u0086\u0003"+
		"8\u001c\u0000\u0085\u0083\u0001\u0000\u0000\u0000\u0085\u0086\u0001\u0000"+
		"\u0000\u0000\u0086\u0087\u0001\u0000\u0000\u0000\u0087\u0088\u0005\u0003"+
		"\u0000\u0000\u0088\u008a\u0001\u0000\u0000\u0000\u0089\u0081\u0001\u0000"+
		"\u0000\u0000\u0089\u008a\u0001\u0000\u0000\u0000\u008a\u0011\u0001\u0000"+
		"\u0000\u0000\u008b\u008c\u0003&\u0013\u0000\u008c\u008d\u0007\u0003\u0000"+
		"\u0000\u008d\u008e\u0003&\u0013\u0000\u008e\u0096\u0001\u0000\u0000\u0000"+
		"\u008f\u0096\u0003 \u0010\u0000\u0090\u0096\u0003\u001e\u000f\u0000\u0091"+
		"\u0096\u0003\u001c\u000e\u0000\u0092\u0096\u0003\u001a\r\u0000\u0093\u0096"+
		"\u0003\u0014\n\u0000\u0094\u0096\u0003\u0016\u000b\u0000\u0095\u008b\u0001"+
		"\u0000\u0000\u0000\u0095\u008f\u0001\u0000\u0000\u0000\u0095\u0090\u0001"+
		"\u0000\u0000\u0000\u0095\u0091\u0001\u0000\u0000\u0000\u0095\u0092\u0001"+
		"\u0000\u0000\u0000\u0095\u0093\u0001\u0000\u0000\u0000\u0095\u0094\u0001"+
		"\u0000\u0000\u0000\u0096\u0013\u0001\u0000\u0000\u0000\u0097\u0098\u0003"+
		"&\u0013\u0000\u0098\u0099\u00051\u0000\u0000\u0099\u009a\u0005\u0002\u0000"+
		"\u0000\u009a\u009b\u0003\u0018\f\u0000\u009b\u009c\u0005\u0003\u0000\u0000"+
		"\u009c\u0015\u0001\u0000\u0000\u0000\u009d\u009e\u0003&\u0013\u0000\u009e"+
		"\u009f\u0005)\u0000\u0000\u009f\u00a0\u00051\u0000\u0000\u00a0\u00a1\u0005"+
		"\u0002\u0000\u0000\u00a1\u00a2\u0003\u0018\f\u0000\u00a2\u00a3\u0005\u0003"+
		"\u0000\u0000\u00a3\u0017\u0001\u0000\u0000\u0000\u00a4\u00a9\u0003&\u0013"+
		"\u0000\u00a5\u00a6\u0005\u0004\u0000\u0000\u00a6\u00a8\u0003&\u0013\u0000"+
		"\u00a7\u00a5\u0001\u0000\u0000\u0000\u00a8\u00ab\u0001\u0000\u0000\u0000"+
		"\u00a9\u00a7\u0001\u0000\u0000\u0000\u00a9\u00aa\u0001\u0000\u0000\u0000"+
		"\u00aa\u0019\u0001\u0000\u0000\u0000\u00ab\u00a9\u0001\u0000\u0000\u0000"+
		"\u00ac\u00ad\u0003&\u0013\u0000\u00ad\u00ae\u0005*\u0000\u0000\u00ae\u00af"+
		"\u0003&\u0013\u0000\u00af\u00b0\u0005+\u0000\u0000\u00b0\u00b1\u0003&"+
		"\u0013\u0000\u00b1\u001b\u0001\u0000\u0000\u0000\u00b2\u00b3\u0003&\u0013"+
		"\u0000\u00b3\u00b4\u0005)\u0000\u0000\u00b4\u00b5\u0005(\u0000\u0000\u00b5"+
		"\u00b6\u0003&\u0013\u0000\u00b6\u001d\u0001\u0000\u0000\u0000\u00b7\u00b8"+
		"\u0003&\u0013\u0000\u00b8\u00b9\u0005\'\u0000\u0000\u00b9\u00ba\u0005"+
		")\u0000\u0000\u00ba\u00bb\u0005\u0015\u0000\u0000\u00bb\u001f\u0001\u0000"+
		"\u0000\u0000\u00bc\u00bd\u0003&\u0013\u0000\u00bd\u00be\u0005\'\u0000"+
		"\u0000\u00be\u00bf\u0005\u0015\u0000\u0000\u00bf!\u0001\u0000\u0000\u0000"+
		"\u00c0\u00c1\u0006\u0011\uffff\uffff\u0000\u00c1\u00c2\u0005\u0002\u0000"+
		"\u0000\u00c2\u00c3\u0003\"\u0011\u0000\u00c3\u00c4\u0005\u0003\u0000\u0000"+
		"\u00c4\u00c7\u0001\u0000\u0000\u0000\u00c5\u00c7\u0003\u0012\t\u0000\u00c6"+
		"\u00c0\u0001\u0000\u0000\u0000\u00c6\u00c5\u0001\u0000\u0000\u0000\u00c7"+
		"\u00cd\u0001\u0000\u0000\u0000\u00c8\u00c9\n\u0002\u0000\u0000\u00c9\u00ca"+
		"\u0007\u0004\u0000\u0000\u00ca\u00cc\u0003\"\u0011\u0003\u00cb\u00c8\u0001"+
		"\u0000\u0000\u0000\u00cc\u00cf\u0001\u0000\u0000\u0000\u00cd\u00cb\u0001"+
		"\u0000\u0000\u0000\u00cd\u00ce\u0001\u0000\u0000\u0000\u00ce#\u0001\u0000"+
		"\u0000\u0000\u00cf\u00cd\u0001\u0000\u0000\u0000\u00d0\u00d1\u0005\u001d"+
		"\u0000\u0000\u00d1\u00d2\u0003\"\u0011\u0000\u00d2\u00d3\u0005\u001e\u0000"+
		"\u0000\u00d3\u00df\u0003\u0002\u0001\u0000\u00d4\u00d5\u0005\u001f\u0000"+
		"\u0000\u00d5\u00d8\u0005\u001d\u0000\u0000\u00d6\u00d8\u0005 \u0000\u0000"+
		"\u00d7\u00d4\u0001\u0000\u0000\u0000\u00d7\u00d6\u0001\u0000\u0000\u0000"+
		"\u00d8\u00d9\u0001\u0000\u0000\u0000\u00d9\u00da\u0003\"\u0011\u0000\u00da"+
		"\u00db\u0005\u001e\u0000\u0000\u00db\u00dc\u0003\u0002\u0001\u0000\u00dc"+
		"\u00de\u0001\u0000\u0000\u0000\u00dd\u00d7\u0001\u0000\u0000\u0000\u00de"+
		"\u00e1\u0001\u0000\u0000\u0000\u00df\u00dd\u0001\u0000\u0000\u0000\u00df"+
		"\u00e0\u0001\u0000\u0000\u0000\u00e0\u00e4\u0001\u0000\u0000\u0000\u00e1"+
		"\u00df\u0001\u0000\u0000\u0000\u00e2\u00e3\u0005\u001f\u0000\u0000\u00e3"+
		"\u00e5\u0003\u0002\u0001\u0000\u00e4\u00e2\u0001\u0000\u0000\u0000\u00e4"+
		"\u00e5\u0001\u0000\u0000\u0000\u00e5\u00e6\u0001\u0000\u0000\u0000\u00e6"+
		"\u00e8\u0005!\u0000\u0000\u00e7\u00e9\u0005\u001d\u0000\u0000\u00e8\u00e7"+
		"\u0001\u0000\u0000\u0000\u00e8\u00e9\u0001\u0000\u0000\u0000\u00e9%\u0001"+
		"\u0000\u0000\u0000\u00ea\u00eb\u0006\u0013\uffff\uffff\u0000\u00eb\u00ec"+
		"\u0005\u0002\u0000\u0000\u00ec\u00ed\u0003&\u0013\u0000\u00ed\u00ee\u0005"+
		"\u0003\u0000\u0000\u00ee\u00f7\u0001\u0000\u0000\u0000\u00ef\u00f0\u0005"+
		"\u0011\u0000\u0000\u00f0\u00f7\u0003&\u0013\u0006\u00f1\u00f7\u0003(\u0014"+
		"\u0000\u00f2\u00f7\u0003:\u001d\u0000\u00f3\u00f7\u00038\u001c\u0000\u00f4"+
		"\u00f7\u00034\u001a\u0000\u00f5\u00f7\u00036\u001b\u0000\u00f6\u00ea\u0001"+
		"\u0000\u0000\u0000\u00f6\u00ef\u0001\u0000\u0000\u0000\u00f6\u00f1\u0001"+
		"\u0000\u0000\u0000\u00f6\u00f2\u0001\u0000\u0000\u0000\u00f6\u00f3\u0001"+
		"\u0000\u0000\u0000\u00f6\u00f4\u0001\u0000\u0000\u0000\u00f6\u00f5\u0001"+
		"\u0000\u0000\u0000\u00f7\u0107\u0001\u0000\u0000\u0000\u00f8\u00f9\n\b"+
		"\u0000\u0000\u00f9\u00fa\u0007\u0005\u0000\u0000\u00fa\u0106\u0003&\u0013"+
		"\t\u00fb\u00fc\n\u0007\u0000\u0000\u00fc\u00fd\u0007\u0006\u0000\u0000"+
		"\u00fd\u0106\u0003&\u0013\b\u00fe\u0101\n\t\u0000\u0000\u00ff\u0100\u0005"+
		"\r\u0000\u0000\u0100\u0102\u0003&\u0013\u0000\u0101\u00ff\u0001\u0000"+
		"\u0000\u0000\u0102\u0103\u0001\u0000\u0000\u0000\u0103\u0101\u0001\u0000"+
		"\u0000\u0000\u0103\u0104\u0001\u0000\u0000\u0000\u0104\u0106\u0001\u0000"+
		"\u0000\u0000\u0105\u00f8\u0001\u0000\u0000\u0000\u0105\u00fb\u0001\u0000"+
		"\u0000\u0000\u0105\u00fe\u0001\u0000\u0000\u0000\u0106\u0109\u0001\u0000"+
		"\u0000\u0000\u0107\u0105\u0001\u0000\u0000\u0000\u0107\u0108\u0001\u0000"+
		"\u0000\u0000\u0108\'\u0001\u0000\u0000\u0000\u0109\u0107\u0001\u0000\u0000"+
		"\u0000\u010a\u010b\u00036\u001b\u0000\u010b\u010d\u0005\u0002\u0000\u0000"+
		"\u010c\u010e\u0003*\u0015\u0000\u010d\u010c\u0001\u0000\u0000\u0000\u010d"+
		"\u010e\u0001\u0000\u0000\u0000\u010e\u010f\u0001\u0000\u0000\u0000\u010f"+
		"\u0110\u0005\u0003\u0000\u0000\u0110)\u0001\u0000\u0000\u0000\u0111\u0116"+
		"\u0003&\u0013\u0000\u0112\u0113\u0005\u0004\u0000\u0000\u0113\u0115\u0003"+
		"&\u0013\u0000\u0114\u0112\u0001\u0000\u0000\u0000\u0115\u0118\u0001\u0000"+
		"\u0000\u0000\u0116\u0114\u0001\u0000\u0000\u0000\u0116\u0117\u0001\u0000"+
		"\u0000\u0000\u0117+\u0001\u0000\u0000\u0000\u0118\u0116\u0001\u0000\u0000"+
		"\u0000\u0119\u011a\u0005\"\u0000\u0000\u011a-\u0001\u0000\u0000\u0000"+
		"\u011b\u011c\u0005#\u0000\u0000\u011c/\u0001\u0000\u0000\u0000\u011d\u011e"+
		"\u0005$\u0000\u0000\u011e\u0121\u0005%\u0000\u0000\u011f\u0122\u00054"+
		"\u0000\u0000\u0120\u0122\u0003:\u001d\u0000\u0121\u011f\u0001\u0000\u0000"+
		"\u0000\u0121\u0120\u0001\u0000\u0000\u0000\u0122\u0125\u0001\u0000\u0000"+
		"\u0000\u0123\u0124\u0005&\u0000\u0000\u0124\u0126\u00054\u0000\u0000\u0125"+
		"\u0123\u0001\u0000\u0000\u0000\u0125\u0126\u0001\u0000\u0000\u0000\u0126"+
		"1\u0001\u0000\u0000\u0000\u0127\u0128\u00036\u001b\u0000\u0128\u0129\u0007"+
		"\u0007\u0000\u0000\u0129\u012a\u0003\u0004\u0002\u0000\u012a3\u0001\u0000"+
		"\u0000\u0000\u012b\u012c\u0005\u0015\u0000\u0000\u012c5\u0001\u0000\u0000"+
		"\u0000\u012d\u012e\u00054\u0000\u0000\u012e7\u0001\u0000\u0000\u0000\u012f"+
		"\u0130\u0007\b\u0000\u0000\u01309\u0001\u0000\u0000\u0000\u0131\u0132"+
		"\u0005\u0014\u0000\u0000\u0132;\u0001\u0000\u0000\u0000\u001dDHTX]_ej"+
		"qv~\u0085\u0089\u0095\u00a9\u00c6\u00cd\u00d7\u00df\u00e4\u00e8\u00f6"+
		"\u0103\u0105\u0107\u010d\u0116\u0121\u0125";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}