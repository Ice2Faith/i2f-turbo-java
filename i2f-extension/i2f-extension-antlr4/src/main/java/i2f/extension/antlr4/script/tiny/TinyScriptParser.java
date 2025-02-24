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
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, SINGLE_LINE_COMMENT=9, 
		MULTI_LINE_COMMENT=10, MULTILINE_STRING=11, RENDER_STRING=12, STRING=13, 
		TYPE_BOOL=14, TYPE_NULL=15, QUOTE=16, ESCAPED_CHAR=17, NAMING=18, ID=19, 
		DOUBLE_OPERAOTR=20, PREFIX_OPERATOR=21, LPAREN=22, RPAREN=23, COMMA=24, 
		DOT=25, DOLLAR=26, LCURLY=27, RCURLY=28, WS=29, REF_EXPRESS=30, DIGIT=31, 
		HEX_LETTER=32, OTC_LETTER=33, BIN_LETTER=34, INT_NUM=35, FLOAT_NUM=36, 
		CH_E=37, SCIEN_NUM_1=38, SCIEN_NUM_2=39, CH_0X=40, TYPE_HEX_NUMBER=41, 
		CH_0T=42, TYPE_OTC_NUMBER=43, CH_0B=44, TYPE_BIN_NUMBER=45;
	public static final int
		RULE_script = 0, RULE_express = 1, RULE_ifSegment = 2, RULE_conditionBlock = 3, 
		RULE_scriptBlock = 4, RULE_equalValue = 5, RULE_newInstance = 6, RULE_invokeFunction = 7, 
		RULE_functionCall = 8, RULE_refCall = 9, RULE_argumentList = 10, RULE_argument = 11,
			RULE_argumentValue = 12, RULE_constValue = 13, RULE_refValue = 14, RULE_constBool = 15,
			RULE_constNull = 16, RULE_constString = 17, RULE_constMultilineString = 18,
			RULE_constRenderString = 19, RULE_decNumber = 20, RULE_hexNumber = 21,
			RULE_otcNumber = 22, RULE_binNumber = 23, RULE_jsonValue = 24, RULE_jsonMapValue = 25,
			RULE_jsonPairs = 26, RULE_jsonPair = 27, RULE_jsonArrayValue = 28, RULE_jsonItemList = 29;
	private static String[] makeRuleNames() {
		return new String[] {
			"script", "express", "ifSegment", "conditionBlock", "scriptBlock", "equalValue", 
			"newInstance", "invokeFunction", "functionCall", "refCall", "argumentList",
				"argument", "argumentValue", "constValue", "refValue", "constBool", "constNull",
				"constString", "constMultilineString", "constRenderString", "decNumber",
				"hexNumber", "otcNumber", "binNumber", "jsonValue", "jsonMapValue", "jsonPairs",
				"jsonPair", "jsonArrayValue", "jsonItemList"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'if'", "'else'", "'='", "'new'", "':'", "'['", "']'", null, 
			null, null, null, null, null, "'null'", "'\"'", null, null, null, null, 
			null, "'('", "')'", "','", "'.'", "'$'", "'{'", "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, "SINGLE_LINE_COMMENT", 
			"MULTI_LINE_COMMENT", "MULTILINE_STRING", "RENDER_STRING", "STRING", 
			"TYPE_BOOL", "TYPE_NULL", "QUOTE", "ESCAPED_CHAR", "NAMING", "ID", "DOUBLE_OPERAOTR", 
			"PREFIX_OPERATOR", "LPAREN", "RPAREN", "COMMA", "DOT", "DOLLAR", "LCURLY", 
			"RCURLY", "WS", "REF_EXPRESS", "DIGIT", "HEX_LETTER", "OTC_LETTER", "BIN_LETTER", 
			"INT_NUM", "FLOAT_NUM", "CH_E", "SCIEN_NUM_1", "SCIEN_NUM_2", "CH_0X", 
			"TYPE_HEX_NUMBER", "CH_0T", "TYPE_OTC_NUMBER", "CH_0B", "TYPE_BIN_NUMBER"
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
	public String getGrammarFileName() { return "TinyScript.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TinyScriptParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ScriptContext extends ParserRuleContext {
		public List<ExpressContext> express() {
			return getRuleContexts(ExpressContext.class);
		}
		public ExpressContext express(int i) {
			return getRuleContext(ExpressContext.class,i);
		}
		public ScriptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_script; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterScript(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitScript(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitScript(this);
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
				setState(60);
			express(0);
				setState(67);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(61);
					match(T__0);
					setState(63);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 47110563362980L) != 0)) {
					{
						setState(62);
					express(0);
					}
				}

				}
				}
				setState(69);
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
	public static class ExpressContext extends ParserRuleContext {
		public IfSegmentContext ifSegment() {
			return getRuleContext(IfSegmentContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(TinyScriptParser.LPAREN, 0); }
		public List<ExpressContext> express() {
			return getRuleContexts(ExpressContext.class);
		}
		public ExpressContext express(int i) {
			return getRuleContext(ExpressContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(TinyScriptParser.RPAREN, 0); }
		public TerminalNode PREFIX_OPERATOR() { return getToken(TinyScriptParser.PREFIX_OPERATOR, 0); }
		public EqualValueContext equalValue() {
			return getRuleContext(EqualValueContext.class,0);
		}
		public NewInstanceContext newInstance() {
			return getRuleContext(NewInstanceContext.class,0);
		}
		public InvokeFunctionContext invokeFunction() {
			return getRuleContext(InvokeFunctionContext.class,0);
		}
		public ConstValueContext constValue() {
			return getRuleContext(ConstValueContext.class,0);
		}
		public RefValueContext refValue() {
			return getRuleContext(RefValueContext.class,0);
		}
		public JsonValueContext jsonValue() {
			return getRuleContext(JsonValueContext.class,0);
		}
		public TerminalNode DOUBLE_OPERAOTR() { return getToken(TinyScriptParser.DOUBLE_OPERAOTR, 0); }
		public ExpressContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_express; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterExpress(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitExpress(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitExpress(this);
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
				setState(84);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
					setState(71);
				ifSegment();
				}
				break;
			case 2:
				{
					setState(72);
				match(LPAREN);
					setState(73);
				express(0);
					setState(74);
				match(RPAREN);
				}
				break;
			case 3:
				{
					setState(76);
				match(PREFIX_OPERATOR);
					setState(77);
				express(8);
				}
				break;
			case 4:
				{
					setState(78);
				equalValue();
				}
				break;
			case 5:
				{
					setState(79);
				newInstance();
				}
				break;
			case 6:
				{
					setState(80);
				invokeFunction();
				}
				break;
			case 7:
				{
					setState(81);
				constValue();
				}
				break;
			case 8:
				{
					setState(82);
				refValue();
				}
				break;
			case 9:
				{
					setState(83);
				jsonValue();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
				setState(91);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExpressContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_express);
						setState(86);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(87);
					match(DOUBLE_OPERAOTR);
						setState(88);
					express(2);
					}
					} 
				}
				setState(93);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
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
	public static class IfSegmentContext extends ParserRuleContext {
		public List<TerminalNode> LPAREN() { return getTokens(TinyScriptParser.LPAREN); }
		public TerminalNode LPAREN(int i) {
			return getToken(TinyScriptParser.LPAREN, i);
		}
		public List<ConditionBlockContext> conditionBlock() {
			return getRuleContexts(ConditionBlockContext.class);
		}
		public ConditionBlockContext conditionBlock(int i) {
			return getRuleContext(ConditionBlockContext.class,i);
		}
		public List<TerminalNode> RPAREN() { return getTokens(TinyScriptParser.RPAREN); }
		public TerminalNode RPAREN(int i) {
			return getToken(TinyScriptParser.RPAREN, i);
		}
		public List<ScriptBlockContext> scriptBlock() {
			return getRuleContexts(ScriptBlockContext.class);
		}
		public ScriptBlockContext scriptBlock(int i) {
			return getRuleContext(ScriptBlockContext.class,i);
		}
		public IfSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterIfSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitIfSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitIfSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfSegmentContext ifSegment() throws RecognitionException {
		IfSegmentContext _localctx = new IfSegmentContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_ifSegment);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(94);
			match(T__1);
				setState(95);
			match(LPAREN);
				setState(96);
			conditionBlock();
				setState(97);
			match(RPAREN);
				setState(98);
			scriptBlock();
				setState(108);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
						setState(99);
					match(T__2);
						setState(100);
					match(T__1);
						setState(101);
					match(LPAREN);
						setState(102);
					conditionBlock();
						setState(103);
					match(RPAREN);
						setState(104);
					scriptBlock();
					}
					} 
				}
				setState(110);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
				setState(113);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
					setState(111);
				match(T__2);
					setState(112);
				scriptBlock();
				}
				break;
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
	public static class ConditionBlockContext extends ParserRuleContext {
		public ExpressContext express() {
			return getRuleContext(ExpressContext.class,0);
		}
		public ConditionBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterConditionBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitConditionBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitConditionBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionBlockContext conditionBlock() throws RecognitionException {
		ConditionBlockContext _localctx = new ConditionBlockContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_conditionBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(115);
			express(0);
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
	public static class ScriptBlockContext extends ParserRuleContext {
		public TerminalNode LCURLY() { return getToken(TinyScriptParser.LCURLY, 0); }
		public ScriptContext script() {
			return getRuleContext(ScriptContext.class,0);
		}
		public TerminalNode RCURLY() { return getToken(TinyScriptParser.RCURLY, 0); }
		public ScriptBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scriptBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterScriptBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitScriptBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitScriptBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScriptBlockContext scriptBlock() throws RecognitionException {
		ScriptBlockContext _localctx = new ScriptBlockContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_scriptBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(117);
			match(LCURLY);
				setState(118);
			script();
				setState(119);
			match(RCURLY);
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
	public static class EqualValueContext extends ParserRuleContext {
		public TerminalNode NAMING() { return getToken(TinyScriptParser.NAMING, 0); }
		public ExpressContext express() {
			return getRuleContext(ExpressContext.class,0);
		}
		public EqualValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterEqualValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitEqualValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitEqualValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqualValueContext equalValue() throws RecognitionException {
		EqualValueContext _localctx = new EqualValueContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_equalValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(121);
			match(NAMING);
				setState(122);
			match(T__3);
				setState(123);
			express(0);
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
	public static class NewInstanceContext extends ParserRuleContext {
		public InvokeFunctionContext invokeFunction() {
			return getRuleContext(InvokeFunctionContext.class,0);
		}
		public NewInstanceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newInstance; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterNewInstance(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitNewInstance(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitNewInstance(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NewInstanceContext newInstance() throws RecognitionException {
		NewInstanceContext _localctx = new NewInstanceContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_newInstance);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(125);
			match(T__4);
				setState(126);
			invokeFunction();
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
	public static class InvokeFunctionContext extends ParserRuleContext {
		public RefCallContext refCall() {
			return getRuleContext(RefCallContext.class,0);
		}
		public List<TerminalNode> DOT() { return getTokens(TinyScriptParser.DOT); }
		public TerminalNode DOT(int i) {
			return getToken(TinyScriptParser.DOT, i);
		}
		public List<FunctionCallContext> functionCall() {
			return getRuleContexts(FunctionCallContext.class);
		}
		public FunctionCallContext functionCall(int i) {
			return getRuleContext(FunctionCallContext.class,i);
		}
		public InvokeFunctionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_invokeFunction; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterInvokeFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitInvokeFunction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitInvokeFunction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InvokeFunctionContext invokeFunction() throws RecognitionException {
		InvokeFunctionContext _localctx = new InvokeFunctionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_invokeFunction);
		try {
			int _alt;
			setState(144);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case REF_EXPRESS:
				enterOuterAlt(_localctx, 1);
				{
					setState(128);
				refCall();
					setState(133);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
							setState(129);
						match(DOT);
							setState(130);
						functionCall();
						}
						} 
					}
					setState(135);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
				}
				}
				break;
			case NAMING:
				enterOuterAlt(_localctx, 2);
				{
					setState(136);
				functionCall();
					setState(141);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
							setState(137);
						match(DOT);
							setState(138);
						functionCall();
						}
						} 
					}
					setState(143);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
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
	public static class FunctionCallContext extends ParserRuleContext {
		public TerminalNode NAMING() { return getToken(TinyScriptParser.NAMING, 0); }
		public TerminalNode LPAREN() { return getToken(TinyScriptParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(TinyScriptParser.RPAREN, 0); }
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_functionCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(146);
			match(NAMING);
			setState(147);
				match(LPAREN);
				setState(149);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 47110557071488L) != 0)) {
				{
					setState(148);
				argumentList();
				}
			}

				setState(151);
			match(RPAREN);
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
	public static class RefCallContext extends ParserRuleContext {
		public RefValueContext refValue() {
			return getRuleContext(RefValueContext.class,0);
		}
		public TerminalNode DOT() { return getToken(TinyScriptParser.DOT, 0); }
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public RefCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterRefCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitRefCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitRefCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefCallContext refCall() throws RecognitionException {
		RefCallContext _localctx = new RefCallContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_refCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(153);
			refValue();
				setState(154);
			match(DOT);
				setState(155);
			functionCall();
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
	public static class ArgumentListContext extends ParserRuleContext {
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TinyScriptParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TinyScriptParser.COMMA, i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(157);
			argument();
				setState(162);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
					setState(158);
				match(COMMA);
					setState(159);
				argument();
				}
				}
				setState(164);
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
	public static class ArgumentContext extends ParserRuleContext {
		public ArgumentValueContext argumentValue() {
			return getRuleContext(ArgumentValueContext.class, 0);
		}

		public TerminalNode NAMING() {
			return getToken(TinyScriptParser.NAMING, 0);
		}
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_argument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1: {
				setState(165);
				match(NAMING);
				setState(166);
				match(T__5);
			}
			break;
			}
				setState(169);
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
		public InvokeFunctionContext invokeFunction() {
			return getRuleContext(InvokeFunctionContext.class, 0);
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
		enterRule(_localctx, 24, RULE_argumentValue);
		try {
			setState(175);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 12, _ctx)) {
				case 1:
				enterOuterAlt(_localctx, 1);
				{
					setState(171);
				invokeFunction();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
					setState(172);
				constValue();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
					setState(173);
				refValue();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
					setState(174);
				jsonValue();
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
	public static class ConstValueContext extends ParserRuleContext {
		public ConstBoolContext constBool() {
			return getRuleContext(ConstBoolContext.class,0);
		}
		public ConstNullContext constNull() {
			return getRuleContext(ConstNullContext.class,0);
		}
		public ConstMultilineStringContext constMultilineString() {
			return getRuleContext(ConstMultilineStringContext.class,0);
		}
		public ConstRenderStringContext constRenderString() {
			return getRuleContext(ConstRenderStringContext.class,0);
		}
		public ConstStringContext constString() {
			return getRuleContext(ConstStringContext.class,0);
		}
		public DecNumberContext decNumber() {
			return getRuleContext(DecNumberContext.class,0);
		}
		public HexNumberContext hexNumber() {
			return getRuleContext(HexNumberContext.class,0);
		}
		public OtcNumberContext otcNumber() {
			return getRuleContext(OtcNumberContext.class,0);
		}
		public BinNumberContext binNumber() {
			return getRuleContext(BinNumberContext.class,0);
		}
		public ConstValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterConstValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitConstValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitConstValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstValueContext constValue() throws RecognitionException {
		ConstValueContext _localctx = new ConstValueContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_constValue);
		try {
			setState(186);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TYPE_BOOL:
				enterOuterAlt(_localctx, 1);
				{
					setState(177);
				constBool();
				}
				break;
			case TYPE_NULL:
				enterOuterAlt(_localctx, 2);
				{
					setState(178);
				constNull();
				}
				break;
			case MULTILINE_STRING:
				enterOuterAlt(_localctx, 3);
				{
					setState(179);
				constMultilineString();
				}
				break;
			case RENDER_STRING:
				enterOuterAlt(_localctx, 4);
				{
					setState(180);
				constRenderString();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 5);
				{
					setState(181);
				constString();
				}
				break;
			case DIGIT:
			case INT_NUM:
			case FLOAT_NUM:
			case SCIEN_NUM_1:
			case SCIEN_NUM_2:
				enterOuterAlt(_localctx, 6);
				{
					setState(182);
				decNumber();
				}
				break;
			case TYPE_HEX_NUMBER:
				enterOuterAlt(_localctx, 7);
				{
					setState(183);
				hexNumber();
				}
				break;
			case TYPE_OTC_NUMBER:
				enterOuterAlt(_localctx, 8);
				{
					setState(184);
				otcNumber();
				}
				break;
			case TYPE_BIN_NUMBER:
				enterOuterAlt(_localctx, 9);
				{
					setState(185);
				binNumber();
				}
				break;
			default:
				throw new NoViableAltException(this);
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
	public static class RefValueContext extends ParserRuleContext {
		public TerminalNode REF_EXPRESS() { return getToken(TinyScriptParser.REF_EXPRESS, 0); }
		public RefValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterRefValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitRefValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitRefValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefValueContext refValue() throws RecognitionException {
		RefValueContext _localctx = new RefValueContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_refValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(188);
			match(REF_EXPRESS);
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
	public static class ConstBoolContext extends ParserRuleContext {
		public TerminalNode TYPE_BOOL() { return getToken(TinyScriptParser.TYPE_BOOL, 0); }
		public ConstBoolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constBool; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterConstBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitConstBool(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitConstBool(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstBoolContext constBool() throws RecognitionException {
		ConstBoolContext _localctx = new ConstBoolContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_constBool);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(190);
			match(TYPE_BOOL);
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
	public static class ConstNullContext extends ParserRuleContext {
		public TerminalNode TYPE_NULL() { return getToken(TinyScriptParser.TYPE_NULL, 0); }
		public ConstNullContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constNull; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterConstNull(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitConstNull(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitConstNull(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstNullContext constNull() throws RecognitionException {
		ConstNullContext _localctx = new ConstNullContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_constNull);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(192);
			match(TYPE_NULL);
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
	public static class ConstStringContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(TinyScriptParser.STRING, 0); }
		public ConstStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterConstString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitConstString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitConstString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstStringContext constString() throws RecognitionException {
		ConstStringContext _localctx = new ConstStringContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_constString);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(194);
			match(STRING);
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
	public static class ConstMultilineStringContext extends ParserRuleContext {
		public TerminalNode MULTILINE_STRING() { return getToken(TinyScriptParser.MULTILINE_STRING, 0); }
		public ConstMultilineStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constMultilineString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterConstMultilineString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitConstMultilineString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitConstMultilineString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstMultilineStringContext constMultilineString() throws RecognitionException {
		ConstMultilineStringContext _localctx = new ConstMultilineStringContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_constMultilineString);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(196);
			match(MULTILINE_STRING);
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
	public static class ConstRenderStringContext extends ParserRuleContext {
		public TerminalNode RENDER_STRING() { return getToken(TinyScriptParser.RENDER_STRING, 0); }
		public ConstRenderStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constRenderString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterConstRenderString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitConstRenderString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitConstRenderString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstRenderStringContext constRenderString() throws RecognitionException {
		ConstRenderStringContext _localctx = new ConstRenderStringContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_constRenderString);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(198);
			match(RENDER_STRING);
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
	public static class DecNumberContext extends ParserRuleContext {
		public TerminalNode SCIEN_NUM_2() { return getToken(TinyScriptParser.SCIEN_NUM_2, 0); }
		public TerminalNode SCIEN_NUM_1() { return getToken(TinyScriptParser.SCIEN_NUM_1, 0); }
		public TerminalNode FLOAT_NUM() { return getToken(TinyScriptParser.FLOAT_NUM, 0); }
		public TerminalNode INT_NUM() { return getToken(TinyScriptParser.INT_NUM, 0); }
		public TerminalNode DIGIT() { return getToken(TinyScriptParser.DIGIT, 0); }
		public DecNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterDecNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitDecNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitDecNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecNumberContext decNumber() throws RecognitionException {
		DecNumberContext _localctx = new DecNumberContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_decNumber);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(200);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 929860419584L) != 0)) ) {
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
	public static class HexNumberContext extends ParserRuleContext {
		public TerminalNode TYPE_HEX_NUMBER() { return getToken(TinyScriptParser.TYPE_HEX_NUMBER, 0); }
		public HexNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hexNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterHexNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitHexNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitHexNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HexNumberContext hexNumber() throws RecognitionException {
		HexNumberContext _localctx = new HexNumberContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_hexNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(202);
			match(TYPE_HEX_NUMBER);
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
	public static class OtcNumberContext extends ParserRuleContext {
		public TerminalNode TYPE_OTC_NUMBER() { return getToken(TinyScriptParser.TYPE_OTC_NUMBER, 0); }
		public OtcNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_otcNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterOtcNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitOtcNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitOtcNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OtcNumberContext otcNumber() throws RecognitionException {
		OtcNumberContext _localctx = new OtcNumberContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_otcNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(204);
			match(TYPE_OTC_NUMBER);
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
	public static class BinNumberContext extends ParserRuleContext {
		public TerminalNode TYPE_BIN_NUMBER() { return getToken(TinyScriptParser.TYPE_BIN_NUMBER, 0); }
		public BinNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterBinNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitBinNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitBinNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinNumberContext binNumber() throws RecognitionException {
		BinNumberContext _localctx = new BinNumberContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_binNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(206);
			match(TYPE_BIN_NUMBER);
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
	public static class JsonValueContext extends ParserRuleContext {
		public InvokeFunctionContext invokeFunction() {
			return getRuleContext(InvokeFunctionContext.class,0);
		}
		public ConstValueContext constValue() {
			return getRuleContext(ConstValueContext.class,0);
		}
		public RefValueContext refValue() {
			return getRuleContext(RefValueContext.class,0);
		}
		public JsonArrayValueContext jsonArrayValue() {
			return getRuleContext(JsonArrayValueContext.class,0);
		}
		public JsonMapValueContext jsonMapValue() {
			return getRuleContext(JsonMapValueContext.class,0);
		}
		public JsonValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterJsonValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitJsonValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitJsonValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonValueContext jsonValue() throws RecognitionException {
		JsonValueContext _localctx = new JsonValueContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_jsonValue);
		try {
			setState(213);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 14, _ctx)) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
					setState(208);
				invokeFunction();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
					setState(209);
				constValue();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
					setState(210);
				refValue();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
					setState(211);
				jsonArrayValue();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
					setState(212);
				jsonMapValue();
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
	public static class JsonMapValueContext extends ParserRuleContext {
		public TerminalNode LCURLY() { return getToken(TinyScriptParser.LCURLY, 0); }
		public TerminalNode RCURLY() { return getToken(TinyScriptParser.RCURLY, 0); }
		public JsonPairsContext jsonPairs() {
			return getRuleContext(JsonPairsContext.class,0);
		}
		public JsonMapValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonMapValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterJsonMapValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitJsonMapValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitJsonMapValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonMapValueContext jsonMapValue() throws RecognitionException {
		JsonMapValueContext _localctx = new JsonMapValueContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_jsonMapValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(215);
			match(LCURLY);
				setState(217);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING || _la==NAMING) {
				{
					setState(216);
				jsonPairs();
				}
			}

				setState(219);
			match(RCURLY);
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
	public static class JsonPairsContext extends ParserRuleContext {
		public List<JsonPairContext> jsonPair() {
			return getRuleContexts(JsonPairContext.class);
		}
		public JsonPairContext jsonPair(int i) {
			return getRuleContext(JsonPairContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TinyScriptParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TinyScriptParser.COMMA, i);
		}
		public JsonPairsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonPairs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterJsonPairs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitJsonPairs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitJsonPairs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonPairsContext jsonPairs() throws RecognitionException {
		JsonPairsContext _localctx = new JsonPairsContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_jsonPairs);
		int _la;
		try {
			setState(230);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 17, _ctx)) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
					setState(221);
				jsonPair();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
					setState(222);
				jsonPair();
					setState(227);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
						setState(223);
					match(COMMA);
						setState(224);
					jsonPair();
					}
					}
					setState(229);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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
	public static class JsonPairContext extends ParserRuleContext {
		public JsonValueContext jsonValue() {
			return getRuleContext(JsonValueContext.class,0);
		}
		public TerminalNode NAMING() { return getToken(TinyScriptParser.NAMING, 0); }
		public ConstStringContext constString() {
			return getRuleContext(ConstStringContext.class,0);
		}
		public JsonPairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonPair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterJsonPair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitJsonPair(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitJsonPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonPairContext jsonPair() throws RecognitionException {
		JsonPairContext _localctx = new JsonPairContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_jsonPair);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(234);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NAMING:
				{
					setState(232);
				match(NAMING);
				}
				break;
			case STRING:
				{
					setState(233);
				constString();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
				setState(236);
			match(T__5);
				setState(237);
			jsonValue();
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
	public static class JsonArrayValueContext extends ParserRuleContext {
		public JsonItemListContext jsonItemList() {
			return getRuleContext(JsonItemListContext.class,0);
		}
		public JsonArrayValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonArrayValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterJsonArrayValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitJsonArrayValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitJsonArrayValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonArrayValueContext jsonArrayValue() throws RecognitionException {
		JsonArrayValueContext _localctx = new JsonArrayValueContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_jsonArrayValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(239);
			match(T__6);
				setState(241);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 47110557071488L) != 0)) {
				{
					setState(240);
				jsonItemList();
				}
			}

				setState(243);
			match(T__7);
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
	public static class JsonItemListContext extends ParserRuleContext {
		public List<JsonValueContext> jsonValue() {
			return getRuleContexts(JsonValueContext.class);
		}
		public JsonValueContext jsonValue(int i) {
			return getRuleContext(JsonValueContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TinyScriptParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TinyScriptParser.COMMA, i);
		}
		public JsonItemListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonItemList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterJsonItemList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitJsonItemList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitJsonItemList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonItemListContext jsonItemList() throws RecognitionException {
		JsonItemListContext _localctx = new JsonItemListContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_jsonItemList);
		int _la;
		try {
			setState(254);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 21, _ctx)) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
					setState(245);
				jsonValue();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
					setState(246);
				jsonValue();
					setState(251);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMMA) {
					{
					{
						setState(247);
					match(COMMA);
						setState(248);
					jsonValue();
					}
					}
					setState(253);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return express_sempred((ExpressContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean express_sempred(ExpressContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
			"\u0004\u0001-\u0101\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002" +
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
					"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0001\u0000\u0001\u0000" +
					"\u0001\u0000\u0003\u0000@\b\u0000\u0005\u0000B\b\u0000\n\u0000\f\u0000" +
					"E\t\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
					"\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001U\b\u0001\u0001\u0001" +
					"\u0001\u0001\u0001\u0001\u0005\u0001Z\b\u0001\n\u0001\f\u0001]\t\u0001" +
					"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002" +
					"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002" +
					"\u0005\u0002k\b\u0002\n\u0002\f\u0002n\t\u0002\u0001\u0002\u0001\u0002" +
					"\u0003\u0002r\b\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004" +
					"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005" +
					"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007" +
					"\u0005\u0007\u0084\b\u0007\n\u0007\f\u0007\u0087\t\u0007\u0001\u0007\u0001" +
					"\u0007\u0001\u0007\u0005\u0007\u008c\b\u0007\n\u0007\f\u0007\u008f\t\u0007" +
					"\u0003\u0007\u0091\b\u0007\u0001\b\u0001\b\u0001\b\u0003\b\u0096\b\b\u0001" +
					"\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0005" +
					"\n\u00a1\b\n\n\n\f\n\u00a4\t\n\u0001\u000b\u0001\u000b\u0003\u000b\u00a8" +
					"\b\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0003" +
					"\f\u00b0\b\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001" +
					"\r\u0001\r\u0003\r\u00bb\b\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001" +
					"\u000f\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001" +
					"\u0012\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001" +
					"\u0015\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0018\u0001" +
					"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003\u0018\u00d6\b\u0018\u0001" +
					"\u0019\u0001\u0019\u0003\u0019\u00da\b\u0019\u0001\u0019\u0001\u0019\u0001" +
					"\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0005\u001a\u00e2\b\u001a\n" +
					"\u001a\f\u001a\u00e5\t\u001a\u0003\u001a\u00e7\b\u001a\u0001\u001b\u0001" +
					"\u001b\u0003\u001b\u00eb\b\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001" +
					"\u001c\u0001\u001c\u0003\u001c\u00f2\b\u001c\u0001\u001c\u0001\u001c\u0001" +
					"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0005\u001d\u00fa\b\u001d\n" +
					"\u001d\f\u001d\u00fd\t\u001d\u0003\u001d\u00ff\b\u001d\u0001\u001d\u0000" +
					"\u0001\u0002\u001e\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014" +
					"\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:\u0000\u0001\u0003\u0000" +
					"\u001f\u001f#$&\'\u010b\u0000<\u0001\u0000\u0000\u0000\u0002T\u0001\u0000" +
					"\u0000\u0000\u0004^\u0001\u0000\u0000\u0000\u0006s\u0001\u0000\u0000\u0000" +
					"\bu\u0001\u0000\u0000\u0000\ny\u0001\u0000\u0000\u0000\f}\u0001\u0000" +
					"\u0000\u0000\u000e\u0090\u0001\u0000\u0000\u0000\u0010\u0092\u0001\u0000" +
					"\u0000\u0000\u0012\u0099\u0001\u0000\u0000\u0000\u0014\u009d\u0001\u0000" +
					"\u0000\u0000\u0016\u00a7\u0001\u0000\u0000\u0000\u0018\u00af\u0001\u0000" +
					"\u0000\u0000\u001a\u00ba\u0001\u0000\u0000\u0000\u001c\u00bc\u0001\u0000" +
					"\u0000\u0000\u001e\u00be\u0001\u0000\u0000\u0000 \u00c0\u0001\u0000\u0000" +
					"\u0000\"\u00c2\u0001\u0000\u0000\u0000$\u00c4\u0001\u0000\u0000\u0000" +
					"&\u00c6\u0001\u0000\u0000\u0000(\u00c8\u0001\u0000\u0000\u0000*\u00ca" +
					"\u0001\u0000\u0000\u0000,\u00cc\u0001\u0000\u0000\u0000.\u00ce\u0001\u0000" +
					"\u0000\u00000\u00d5\u0001\u0000\u0000\u00002\u00d7\u0001\u0000\u0000\u0000" +
					"4\u00e6\u0001\u0000\u0000\u00006\u00ea\u0001\u0000\u0000\u00008\u00ef" +
					"\u0001\u0000\u0000\u0000:\u00fe\u0001\u0000\u0000\u0000<C\u0003\u0002" +
					"\u0001\u0000=?\u0005\u0001\u0000\u0000>@\u0003\u0002\u0001\u0000?>\u0001" +
					"\u0000\u0000\u0000?@\u0001\u0000\u0000\u0000@B\u0001\u0000\u0000\u0000" +
					"A=\u0001\u0000\u0000\u0000BE\u0001\u0000\u0000\u0000CA\u0001\u0000\u0000" +
					"\u0000CD\u0001\u0000\u0000\u0000D\u0001\u0001\u0000\u0000\u0000EC\u0001" +
					"\u0000\u0000\u0000FG\u0006\u0001\uffff\uffff\u0000GU\u0003\u0004\u0002" +
					"\u0000HI\u0005\u0016\u0000\u0000IJ\u0003\u0002\u0001\u0000JK\u0005\u0017" +
					"\u0000\u0000KU\u0001\u0000\u0000\u0000LM\u0005\u0015\u0000\u0000MU\u0003" +
					"\u0002\u0001\bNU\u0003\n\u0005\u0000OU\u0003\f\u0006\u0000PU\u0003\u000e" +
					"\u0007\u0000QU\u0003\u001a\r\u0000RU\u0003\u001c\u000e\u0000SU\u00030" +
					"\u0018\u0000TF\u0001\u0000\u0000\u0000TH\u0001\u0000\u0000\u0000TL\u0001" +
					"\u0000\u0000\u0000TN\u0001\u0000\u0000\u0000TO\u0001\u0000\u0000\u0000" +
					"TP\u0001\u0000\u0000\u0000TQ\u0001\u0000\u0000\u0000TR\u0001\u0000\u0000" +
					"\u0000TS\u0001\u0000\u0000\u0000U[\u0001\u0000\u0000\u0000VW\n\u0001\u0000" +
					"\u0000WX\u0005\u0014\u0000\u0000XZ\u0003\u0002\u0001\u0002YV\u0001\u0000" +
					"\u0000\u0000Z]\u0001\u0000\u0000\u0000[Y\u0001\u0000\u0000\u0000[\\\u0001" +
					"\u0000\u0000\u0000\\\u0003\u0001\u0000\u0000\u0000][\u0001\u0000\u0000" +
					"\u0000^_\u0005\u0002\u0000\u0000_`\u0005\u0016\u0000\u0000`a\u0003\u0006" +
					"\u0003\u0000ab\u0005\u0017\u0000\u0000bl\u0003\b\u0004\u0000cd\u0005\u0003" +
					"\u0000\u0000de\u0005\u0002\u0000\u0000ef\u0005\u0016\u0000\u0000fg\u0003" +
					"\u0006\u0003\u0000gh\u0005\u0017\u0000\u0000hi\u0003\b\u0004\u0000ik\u0001" +
					"\u0000\u0000\u0000jc\u0001\u0000\u0000\u0000kn\u0001\u0000\u0000\u0000" +
					"lj\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000\u0000mq\u0001\u0000\u0000" +
					"\u0000nl\u0001\u0000\u0000\u0000op\u0005\u0003\u0000\u0000pr\u0003\b\u0004" +
					"\u0000qo\u0001\u0000\u0000\u0000qr\u0001\u0000\u0000\u0000r\u0005\u0001" +
					"\u0000\u0000\u0000st\u0003\u0002\u0001\u0000t\u0007\u0001\u0000\u0000" +
					"\u0000uv\u0005\u001b\u0000\u0000vw\u0003\u0000\u0000\u0000wx\u0005\u001c" +
					"\u0000\u0000x\t\u0001\u0000\u0000\u0000yz\u0005\u0012\u0000\u0000z{\u0005" +
					"\u0004\u0000\u0000{|\u0003\u0002\u0001\u0000|\u000b\u0001\u0000\u0000" +
					"\u0000}~\u0005\u0005\u0000\u0000~\u007f\u0003\u000e\u0007\u0000\u007f" +
					"\r\u0001\u0000\u0000\u0000\u0080\u0085\u0003\u0012\t\u0000\u0081\u0082" +
					"\u0005\u0019\u0000\u0000\u0082\u0084\u0003\u0010\b\u0000\u0083\u0081\u0001" +
					"\u0000\u0000\u0000\u0084\u0087\u0001\u0000\u0000\u0000\u0085\u0083\u0001" +
					"\u0000\u0000\u0000\u0085\u0086\u0001\u0000\u0000\u0000\u0086\u0091\u0001" +
					"\u0000\u0000\u0000\u0087\u0085\u0001\u0000\u0000\u0000\u0088\u008d\u0003" +
					"\u0010\b\u0000\u0089\u008a\u0005\u0019\u0000\u0000\u008a\u008c\u0003\u0010" +
					"\b\u0000\u008b\u0089\u0001\u0000\u0000\u0000\u008c\u008f\u0001\u0000\u0000" +
					"\u0000\u008d\u008b\u0001\u0000\u0000\u0000\u008d\u008e\u0001\u0000\u0000" +
					"\u0000\u008e\u0091\u0001\u0000\u0000\u0000\u008f\u008d\u0001\u0000\u0000" +
					"\u0000\u0090\u0080\u0001\u0000\u0000\u0000\u0090\u0088\u0001\u0000\u0000" +
					"\u0000\u0091\u000f\u0001\u0000\u0000\u0000\u0092\u0093\u0005\u0012\u0000" +
					"\u0000\u0093\u0095\u0005\u0016\u0000\u0000\u0094\u0096\u0003\u0014\n\u0000" +
					"\u0095\u0094\u0001\u0000\u0000\u0000\u0095\u0096\u0001\u0000\u0000\u0000" +
					"\u0096\u0097\u0001\u0000\u0000\u0000\u0097\u0098\u0005\u0017\u0000\u0000" +
					"\u0098\u0011\u0001\u0000\u0000\u0000\u0099\u009a\u0003\u001c\u000e\u0000" +
					"\u009a\u009b\u0005\u0019\u0000\u0000\u009b\u009c\u0003\u0010\b\u0000\u009c" +
					"\u0013\u0001\u0000\u0000\u0000\u009d\u00a2\u0003\u0016\u000b\u0000\u009e" +
					"\u009f\u0005\u0018\u0000\u0000\u009f\u00a1\u0003\u0016\u000b\u0000\u00a0" +
					"\u009e\u0001\u0000\u0000\u0000\u00a1\u00a4\u0001\u0000\u0000\u0000\u00a2" +
					"\u00a0\u0001\u0000\u0000\u0000\u00a2\u00a3\u0001\u0000\u0000\u0000\u00a3" +
					"\u0015\u0001\u0000\u0000\u0000\u00a4\u00a2\u0001\u0000\u0000\u0000\u00a5" +
					"\u00a6\u0005\u0012\u0000\u0000\u00a6\u00a8\u0005\u0006\u0000\u0000\u00a7" +
					"\u00a5\u0001\u0000\u0000\u0000\u00a7\u00a8\u0001\u0000\u0000\u0000\u00a8" +
					"\u00a9\u0001\u0000\u0000\u0000\u00a9\u00aa\u0003\u0018\f\u0000\u00aa\u0017" +
					"\u0001\u0000\u0000\u0000\u00ab\u00b0\u0003\u000e\u0007\u0000\u00ac\u00b0" +
					"\u0003\u001a\r\u0000\u00ad\u00b0\u0003\u001c\u000e\u0000\u00ae\u00b0\u0003" +
					"0\u0018\u0000\u00af\u00ab\u0001\u0000\u0000\u0000\u00af\u00ac\u0001\u0000" +
					"\u0000\u0000\u00af\u00ad\u0001\u0000\u0000\u0000\u00af\u00ae\u0001\u0000" +
					"\u0000\u0000\u00b0\u0019\u0001\u0000\u0000\u0000\u00b1\u00bb\u0003\u001e" +
					"\u000f\u0000\u00b2\u00bb\u0003 \u0010\u0000\u00b3\u00bb\u0003$\u0012\u0000" +
					"\u00b4\u00bb\u0003&\u0013\u0000\u00b5\u00bb\u0003\"\u0011\u0000\u00b6" +
					"\u00bb\u0003(\u0014\u0000\u00b7\u00bb\u0003*\u0015\u0000\u00b8\u00bb\u0003" +
					",\u0016\u0000\u00b9\u00bb\u0003.\u0017\u0000\u00ba\u00b1\u0001\u0000\u0000" +
					"\u0000\u00ba\u00b2\u0001\u0000\u0000\u0000\u00ba\u00b3\u0001\u0000\u0000" +
					"\u0000\u00ba\u00b4\u0001\u0000\u0000\u0000\u00ba\u00b5\u0001\u0000\u0000" +
					"\u0000\u00ba\u00b6\u0001\u0000\u0000\u0000\u00ba\u00b7\u0001\u0000\u0000" +
					"\u0000\u00ba\u00b8\u0001\u0000\u0000\u0000\u00ba\u00b9\u0001\u0000\u0000" +
					"\u0000\u00bb\u001b\u0001\u0000\u0000\u0000\u00bc\u00bd\u0005\u001e\u0000" +
					"\u0000\u00bd\u001d\u0001\u0000\u0000\u0000\u00be\u00bf\u0005\u000e\u0000" +
					"\u0000\u00bf\u001f\u0001\u0000\u0000\u0000\u00c0\u00c1\u0005\u000f\u0000" +
					"\u0000\u00c1!\u0001\u0000\u0000\u0000\u00c2\u00c3\u0005\r\u0000\u0000" +
					"\u00c3#\u0001\u0000\u0000\u0000\u00c4\u00c5\u0005\u000b\u0000\u0000\u00c5" +
					"%\u0001\u0000\u0000\u0000\u00c6\u00c7\u0005\f\u0000\u0000\u00c7\'\u0001" +
					"\u0000\u0000\u0000\u00c8\u00c9\u0007\u0000\u0000\u0000\u00c9)\u0001\u0000" +
					"\u0000\u0000\u00ca\u00cb\u0005)\u0000\u0000\u00cb+\u0001\u0000\u0000\u0000" +
					"\u00cc\u00cd\u0005+\u0000\u0000\u00cd-\u0001\u0000\u0000\u0000\u00ce\u00cf" +
					"\u0005-\u0000\u0000\u00cf/\u0001\u0000\u0000\u0000\u00d0\u00d6\u0003\u000e" +
					"\u0007\u0000\u00d1\u00d6\u0003\u001a\r\u0000\u00d2\u00d6\u0003\u001c\u000e" +
					"\u0000\u00d3\u00d6\u00038\u001c\u0000\u00d4\u00d6\u00032\u0019\u0000\u00d5" +
					"\u00d0\u0001\u0000\u0000\u0000\u00d5\u00d1\u0001\u0000\u0000\u0000\u00d5" +
					"\u00d2\u0001\u0000\u0000\u0000\u00d5\u00d3\u0001\u0000\u0000\u0000\u00d5" +
					"\u00d4\u0001\u0000\u0000\u0000\u00d61\u0001\u0000\u0000\u0000\u00d7\u00d9" +
					"\u0005\u001b\u0000\u0000\u00d8\u00da\u00034\u001a\u0000\u00d9\u00d8\u0001" +
					"\u0000\u0000\u0000\u00d9\u00da\u0001\u0000\u0000\u0000\u00da\u00db\u0001" +
					"\u0000\u0000\u0000\u00db\u00dc\u0005\u001c\u0000\u0000\u00dc3\u0001\u0000" +
					"\u0000\u0000\u00dd\u00e7\u00036\u001b\u0000\u00de\u00e3\u00036\u001b\u0000" +
					"\u00df\u00e0\u0005\u0018\u0000\u0000\u00e0\u00e2\u00036\u001b\u0000\u00e1" +
					"\u00df\u0001\u0000\u0000\u0000\u00e2\u00e5\u0001\u0000\u0000\u0000\u00e3" +
					"\u00e1\u0001\u0000\u0000\u0000\u00e3\u00e4\u0001\u0000\u0000\u0000\u00e4" +
					"\u00e7\u0001\u0000\u0000\u0000\u00e5\u00e3\u0001\u0000\u0000\u0000\u00e6" +
					"\u00dd\u0001\u0000\u0000\u0000\u00e6\u00de\u0001\u0000\u0000\u0000\u00e7" +
					"5\u0001\u0000\u0000\u0000\u00e8\u00eb\u0005\u0012\u0000\u0000\u00e9\u00eb" +
					"\u0003\"\u0011\u0000\u00ea\u00e8\u0001\u0000\u0000\u0000\u00ea\u00e9\u0001" +
					"\u0000\u0000\u0000\u00eb\u00ec\u0001\u0000\u0000\u0000\u00ec\u00ed\u0005" +
					"\u0006\u0000\u0000\u00ed\u00ee\u00030\u0018\u0000\u00ee7\u0001\u0000\u0000" +
					"\u0000\u00ef\u00f1\u0005\u0007\u0000\u0000\u00f0\u00f2\u0003:\u001d\u0000" +
					"\u00f1\u00f0\u0001\u0000\u0000\u0000\u00f1\u00f2\u0001\u0000\u0000\u0000" +
					"\u00f2\u00f3\u0001\u0000\u0000\u0000\u00f3\u00f4\u0005\b\u0000\u0000\u00f4" +
					"9\u0001\u0000\u0000\u0000\u00f5\u00ff\u00030\u0018\u0000\u00f6\u00fb\u0003" +
					"0\u0018\u0000\u00f7\u00f8\u0005\u0018\u0000\u0000\u00f8\u00fa\u00030\u0018" +
					"\u0000\u00f9\u00f7\u0001\u0000\u0000\u0000\u00fa\u00fd\u0001\u0000\u0000" +
					"\u0000\u00fb\u00f9\u0001\u0000\u0000\u0000\u00fb\u00fc\u0001\u0000\u0000" +
					"\u0000\u00fc\u00ff\u0001\u0000\u0000\u0000\u00fd\u00fb\u0001\u0000\u0000" +
					"\u0000\u00fe\u00f5\u0001\u0000\u0000\u0000\u00fe\u00f6\u0001\u0000\u0000" +
					"\u0000\u00ff;\u0001\u0000\u0000\u0000\u0016?CT[lq\u0085\u008d\u0090\u0095" +
					"\u00a2\u00a7\u00af\u00ba\u00d5\u00d9\u00e3\u00e6\u00ea\u00f1\u00fb\u00fe";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}