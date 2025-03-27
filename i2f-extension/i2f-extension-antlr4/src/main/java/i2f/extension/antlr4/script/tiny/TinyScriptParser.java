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
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38,
			T__38 = 39, T__39 = 40, T__40 = 41, T__41 = 42, T__42 = 43, T__43 = 44, T__44 = 45,
			T__45 = 46, T__46 = 47, T__47 = 48, T__48 = 49, T__49 = 50, T__50 = 51, T__51 = 52,
			T__52 = 53, T__53 = 54, T__54 = 55, TERM_COMMENT_SINGLE_LINE = 56, TERM_COMMENT_MULTI_LINE = 57,
			TERM_CONST_STRING_MULTILINE = 58, TERM_CONST_STRING_MULTILINE_QUOTE = 59,
			TERM_CONST_STRING_RENDER = 60, TERM_CONST_STRING_RENDER_SINGLE = 61, TERM_CONST_STRING = 62,
			TERM_CONST_STRING_SINGLE = 63, TERM_CONST_BOOLEAN = 64, TERM_CONST_NULL = 65,
			TERM_CONST_TYPE_CLASS = 66, REF_EXPRESS = 67, TERM_CONST_NUMBER_SCIEN_2 = 68,
			TERM_CONST_NUMBER_SCIEN_1 = 69, TERM_CONST_NUMBER_FLOAT = 70, TERM_CONST_NUMBER = 71,
			TERM_CONST_NUMBER_HEX = 72, TERM_CONST_NUMBER_OTC = 73, TERM_CONST_NUMBER_BIN = 74,
			TERM_INTEGER = 75, PREFIX_OPERATOR = 76, NAMING = 77, ROUTE_NAMING = 78, ID = 79,
			TERM_QUOTE = 80, ESCAPED_CHAR = 81, TERM_PAREN_L = 82, TERM_PAREN_R = 83, TERM_COMMA = 84,
			TERM_DOT = 85, TERM_DOLLAR = 86, TERM_CURLY_L = 87, TERM_CURLY_R = 88, TERM_SEMICOLON = 89,
			TERM_COLON = 90, TERM_BRACKET_SQUARE_L = 91, TERM_BRACKET_SQUARE_R = 92, CH_E = 93,
			CH_0X = 94, CH_0T = 95, CH_0B = 96, TERM_DIGIT = 97, TERM_HEX_LETTER = 98, TERM_OTC_LETTER = 99,
			TERM_BIN_LETTER = 100, WS = 101;
	public static final int
		RULE_script = 0, RULE_express = 1, RULE_negtiveSegment = 2, RULE_debuggerSegment = 3, 
		RULE_trySegment = 4, RULE_throwSegment = 5, RULE_tryBodyBlock = 6, RULE_catchBodyBlock = 7, 
		RULE_finallyBodyBlock = 8, RULE_classNameBlock = 9, RULE_parenSegment = 10, 
		RULE_prefixOperatorSegment = 11, RULE_controlSegment = 12, RULE_whileSegment = 13, 
		RULE_forSegment = 14, RULE_foreachSegment = 15, RULE_namingBlock = 16,
			RULE_ifSegment = 17, RULE_conditionBlock = 18, RULE_scriptBlock = 19,
			RULE_equalValue = 20, RULE_extractExpress = 21, RULE_extractPairs = 22,
			RULE_extractPair = 23, RULE_newInstance = 24, RULE_invokeFunction = 25,
			RULE_functionCall = 26, RULE_refCall = 27, RULE_argumentList = 28, RULE_argument = 29,
			RULE_argumentValue = 30, RULE_constValue = 31, RULE_refValue = 32, RULE_constBool = 33,
			RULE_constNull = 34, RULE_constClass = 35, RULE_constString = 36, RULE_constMultilineString = 37,
			RULE_constRenderString = 38, RULE_decNumber = 39, RULE_hexNumber = 40,
			RULE_otcNumber = 41, RULE_binNumber = 42, RULE_jsonValue = 43, RULE_jsonMapValue = 44,
			RULE_jsonPairs = 45, RULE_jsonPair = 46, RULE_jsonArrayValue = 47, RULE_jsonItemList = 48;
	private static String[] makeRuleNames() {
		return new String[] {
			"script", "express", "negtiveSegment", "debuggerSegment", "trySegment", 
			"throwSegment", "tryBodyBlock", "catchBodyBlock", "finallyBodyBlock", 
			"classNameBlock", "parenSegment", "prefixOperatorSegment", "controlSegment",
				"whileSegment", "forSegment", "foreachSegment", "namingBlock", "ifSegment",
				"conditionBlock", "scriptBlock", "equalValue", "extractExpress", "extractPairs",
				"extractPair", "newInstance", "invokeFunction", "functionCall", "refCall",
				"argumentList", "argument", "argumentValue", "constValue", "refValue",
				"constBool", "constNull", "constClass", "constString", "constMultilineString",
				"constRenderString", "decNumber", "hexNumber", "otcNumber", "binNumber",
				"jsonValue", "jsonMapValue", "jsonPairs", "jsonPair", "jsonArrayValue",
				"jsonItemList"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
				null, "'%'", "'as'", "'cast'", "'is'", "'instanceof'", "'typeof'", "'*'",
				"'/'", "'+'", "'-'", "'in'", "'notin'", "'>='", "'gte'", "'<='", "'lte'",
			"'!='", "'ne'", "'<>'", "'neq'", "'=='", "'eq'", "'>'", "'gt'", "'<'", 
			"'lt'", "'&&'", "'and'", "'||'", "'or'", "'?'", "'debugger'", "'try'",
				"'catch'", "'|'", "'finally'", "'throw'", "'break'", "'continue'", "'return'",
				"'while'", "'for'", "'foreach'", "'if'", "'else'", "'elif'", "'='", "'?='",
				"'+='", "'-='", "'*='", "'/='", "'%='", "'#'", "'new'", null, null, null,
				null, null, null, null, null, null, "'null'", null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, "'\"'", null,
				"'('", "')'", "','", "'.'", "'$'", "'{'", "'}'", "';'", "':'", "'['",
				"']'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, "TERM_COMMENT_SINGLE_LINE",
				"TERM_COMMENT_MULTI_LINE", "TERM_CONST_STRING_MULTILINE", "TERM_CONST_STRING_MULTILINE_QUOTE",
				"TERM_CONST_STRING_RENDER", "TERM_CONST_STRING_RENDER_SINGLE", "TERM_CONST_STRING",
				"TERM_CONST_STRING_SINGLE", "TERM_CONST_BOOLEAN", "TERM_CONST_NULL",
				"TERM_CONST_TYPE_CLASS", "REF_EXPRESS", "TERM_CONST_NUMBER_SCIEN_2",
			"TERM_CONST_NUMBER_SCIEN_1", "TERM_CONST_NUMBER_FLOAT", "TERM_CONST_NUMBER", 
			"TERM_CONST_NUMBER_HEX", "TERM_CONST_NUMBER_OTC", "TERM_CONST_NUMBER_BIN", 
			"TERM_INTEGER", "PREFIX_OPERATOR", "NAMING", "ROUTE_NAMING", "ID", "TERM_QUOTE", 
			"ESCAPED_CHAR", "TERM_PAREN_L", "TERM_PAREN_R", "TERM_COMMA", "TERM_DOT", 
			"TERM_DOLLAR", "TERM_CURLY_L", "TERM_CURLY_R", "TERM_SEMICOLON", "TERM_COLON", 
			"TERM_BRACKET_SQUARE_L", "TERM_BRACKET_SQUARE_R", "CH_E", "CH_0X", "CH_0T", 
			"CH_0B", "TERM_DIGIT", "TERM_HEX_LETTER", "TERM_OTC_LETTER", "TERM_BIN_LETTER", 
			"WS"
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
		public List<TerminalNode> TERM_SEMICOLON() { return getTokens(TinyScriptParser.TERM_SEMICOLON); }
		public TerminalNode TERM_SEMICOLON(int i) {
			return getToken(TinyScriptParser.TERM_SEMICOLON, i);
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
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(98);
			express(0);
				setState(103);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
						setState(99);
					match(TERM_SEMICOLON);
						setState(100);
					express(0);
					}
					} 
				}
				setState(105);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
				setState(107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TERM_SEMICOLON) {
				{
					setState(106);
				match(TERM_SEMICOLON);
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
	public static class ExpressContext extends ParserRuleContext {
		public DebuggerSegmentContext debuggerSegment() {
			return getRuleContext(DebuggerSegmentContext.class,0);
		}
		public IfSegmentContext ifSegment() {
			return getRuleContext(IfSegmentContext.class,0);
		}
		public ForeachSegmentContext foreachSegment() {
			return getRuleContext(ForeachSegmentContext.class,0);
		}
		public ForSegmentContext forSegment() {
			return getRuleContext(ForSegmentContext.class,0);
		}
		public WhileSegmentContext whileSegment() {
			return getRuleContext(WhileSegmentContext.class,0);
		}
		public ControlSegmentContext controlSegment() {
			return getRuleContext(ControlSegmentContext.class,0);
		}
		public TrySegmentContext trySegment() {
			return getRuleContext(TrySegmentContext.class,0);
		}
		public ThrowSegmentContext throwSegment() {
			return getRuleContext(ThrowSegmentContext.class,0);
		}
		public ParenSegmentContext parenSegment() {
			return getRuleContext(ParenSegmentContext.class,0);
		}
		public PrefixOperatorSegmentContext prefixOperatorSegment() {
			return getRuleContext(PrefixOperatorSegmentContext.class,0);
		}
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
		public NegtiveSegmentContext negtiveSegment() {
			return getRuleContext(NegtiveSegmentContext.class,0);
		}
		public List<ExpressContext> express() {
			return getRuleContexts(ExpressContext.class);
		}
		public ExpressContext express(int i) {
			return getRuleContext(ExpressContext.class,i);
		}
		public TerminalNode TERM_COLON() { return getToken(TinyScriptParser.TERM_COLON, 0); }
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
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(127);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				{
					setState(110);
				debuggerSegment();
				}
				break;
			case 2:
				{
					setState(111);
				ifSegment();
				}
				break;
			case 3:
				{
					setState(112);
				foreachSegment();
				}
				break;
			case 4:
				{
					setState(113);
				forSegment();
				}
				break;
			case 5:
				{
					setState(114);
				whileSegment();
				}
				break;
			case 6:
				{
					setState(115);
				controlSegment();
				}
				break;
			case 7:
				{
					setState(116);
				trySegment();
				}
				break;
			case 8:
				{
					setState(117);
				throwSegment();
				}
				break;
			case 9:
				{
					setState(118);
				parenSegment();
				}
				break;
			case 10:
				{
					setState(119);
				prefixOperatorSegment();
				}
				break;
			case 11:
				{
					setState(120);
				equalValue();
				}
				break;
			case 12:
				{
					setState(121);
				newInstance();
				}
				break;
			case 13:
				{
					setState(122);
				invokeFunction();
				}
				break;
			case 14:
				{
					setState(123);
				constValue();
				}
				break;
			case 15:
				{
					setState(124);
				refValue();
				}
				break;
			case 16:
				{
					setState(125);
				jsonValue();
				}
				break;
			case 17:
				{
					setState(126);
				negtiveSegment();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
				setState(154);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
						setState(152);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_express);
							setState(129);
							if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
							setState(130);
						_la = _input.LA(1);
							if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 124L) != 0))) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
							setState(131);
							express(8);
						}
						break;
					case 2:
						{
						_localctx = new ExpressContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_express);
							setState(132);
							if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
							setState(133);
						_la = _input.LA(1);
							if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 386L) != 0))) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
							setState(134);
							express(7);
						}
						break;
					case 3:
						{
						_localctx = new ExpressContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_express);
							setState(135);
							if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
							setState(136);
						_la = _input.LA(1);
						if ( !(_la==T__8 || _la==T__9) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
							setState(137);
							express(6);
						}
						break;
					case 4:
						{
						_localctx = new ExpressContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_express);
							setState(138);
							if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
							setState(139);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 134215680L) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
							setState(140);
							express(5);
						}
						break;
					case 5:
						{
						_localctx = new ExpressContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_express);
							setState(141);
							if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
							setState(142);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2013265920L) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
							setState(143);
							express(4);
						}
						break;
					case 6:
						{
						_localctx = new ExpressContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_express);
							setState(144);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
							setState(145);
						match(T__30);
							setState(146);
						express(0);
							setState(147);
						match(TERM_COLON);
							setState(148);
						express(2);
						}
						break;
					case 7:
						{
						_localctx = new ExpressContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_express);
							setState(150);
							if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
							{
								setState(151);
								match(T__0);
							}
						}
						break;
					}
					} 
				}
				setState(156);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
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
	public static class NegtiveSegmentContext extends ParserRuleContext {
		public ExpressContext express() {
			return getRuleContext(ExpressContext.class,0);
		}
		public NegtiveSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_negtiveSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterNegtiveSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitNegtiveSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitNegtiveSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NegtiveSegmentContext negtiveSegment() throws RecognitionException {
		NegtiveSegmentContext _localctx = new NegtiveSegmentContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_negtiveSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(157);
			match(T__9);
				setState(158);
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
	public static class DebuggerSegmentContext extends ParserRuleContext {
		public NamingBlockContext namingBlock() {
			return getRuleContext(NamingBlockContext.class,0);
		}
		public TerminalNode TERM_PAREN_L() { return getToken(TinyScriptParser.TERM_PAREN_L, 0); }
		public ConditionBlockContext conditionBlock() {
			return getRuleContext(ConditionBlockContext.class,0);
		}
		public TerminalNode TERM_PAREN_R() { return getToken(TinyScriptParser.TERM_PAREN_R, 0); }
		public DebuggerSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_debuggerSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterDebuggerSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitDebuggerSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitDebuggerSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DebuggerSegmentContext debuggerSegment() throws RecognitionException {
		DebuggerSegmentContext _localctx = new DebuggerSegmentContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_debuggerSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(160);
			match(T__31);
				setState(162);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
					setState(161);
				namingBlock();
				}
				break;
			}
				setState(168);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
					setState(164);
				match(TERM_PAREN_L);
					setState(165);
				conditionBlock();
					setState(166);
				match(TERM_PAREN_R);
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
	public static class TrySegmentContext extends ParserRuleContext {
		public TryBodyBlockContext tryBodyBlock() {
			return getRuleContext(TryBodyBlockContext.class,0);
		}
		public List<TerminalNode> TERM_PAREN_L() { return getTokens(TinyScriptParser.TERM_PAREN_L); }
		public TerminalNode TERM_PAREN_L(int i) {
			return getToken(TinyScriptParser.TERM_PAREN_L, i);
		}
		public List<NamingBlockContext> namingBlock() {
			return getRuleContexts(NamingBlockContext.class);
		}
		public NamingBlockContext namingBlock(int i) {
			return getRuleContext(NamingBlockContext.class,i);
		}
		public List<TerminalNode> TERM_PAREN_R() { return getTokens(TinyScriptParser.TERM_PAREN_R); }
		public TerminalNode TERM_PAREN_R(int i) {
			return getToken(TinyScriptParser.TERM_PAREN_R, i);
		}
		public List<CatchBodyBlockContext> catchBodyBlock() {
			return getRuleContexts(CatchBodyBlockContext.class);
		}
		public CatchBodyBlockContext catchBodyBlock(int i) {
			return getRuleContext(CatchBodyBlockContext.class,i);
		}
		public FinallyBodyBlockContext finallyBodyBlock() {
			return getRuleContext(FinallyBodyBlockContext.class,0);
		}
		public List<ClassNameBlockContext> classNameBlock() {
			return getRuleContexts(ClassNameBlockContext.class);
		}
		public ClassNameBlockContext classNameBlock(int i) {
			return getRuleContext(ClassNameBlockContext.class,i);
		}
		public TrySegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trySegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterTrySegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitTrySegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitTrySegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TrySegmentContext trySegment() throws RecognitionException {
		TrySegmentContext _localctx = new TrySegmentContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_trySegment);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(170);
			match(T__32);
				setState(171);
			tryBodyBlock();
				setState(188);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
						setState(172);
					match(T__33);
						setState(173);
					match(TERM_PAREN_L);
					{
						setState(174);
					classNameBlock();
						setState(179);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__34) {
						{
						{
							setState(175);
						match(T__34);
							setState(176);
						classNameBlock();
						}
						}
						setState(181);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
						setState(182);
					namingBlock();
						setState(183);
					match(TERM_PAREN_R);
						setState(184);
					catchBodyBlock();
					}
					} 
				}
				setState(190);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
			}
				setState(193);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				{
					setState(191);
				match(T__35);
					setState(192);
				finallyBodyBlock();
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
	public static class ThrowSegmentContext extends ParserRuleContext {
		public ExpressContext express() {
			return getRuleContext(ExpressContext.class,0);
		}
		public ThrowSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_throwSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterThrowSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitThrowSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitThrowSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ThrowSegmentContext throwSegment() throws RecognitionException {
		ThrowSegmentContext _localctx = new ThrowSegmentContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_throwSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(195);
			match(T__36);
				setState(196);
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
	public static class TryBodyBlockContext extends ParserRuleContext {
		public ScriptBlockContext scriptBlock() {
			return getRuleContext(ScriptBlockContext.class,0);
		}
		public TryBodyBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tryBodyBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterTryBodyBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitTryBodyBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitTryBodyBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TryBodyBlockContext tryBodyBlock() throws RecognitionException {
		TryBodyBlockContext _localctx = new TryBodyBlockContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_tryBodyBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(198);
			scriptBlock();
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
	public static class CatchBodyBlockContext extends ParserRuleContext {
		public ScriptBlockContext scriptBlock() {
			return getRuleContext(ScriptBlockContext.class,0);
		}
		public CatchBodyBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_catchBodyBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterCatchBodyBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitCatchBodyBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitCatchBodyBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CatchBodyBlockContext catchBodyBlock() throws RecognitionException {
		CatchBodyBlockContext _localctx = new CatchBodyBlockContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_catchBodyBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(200);
			scriptBlock();
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
	public static class FinallyBodyBlockContext extends ParserRuleContext {
		public ScriptBlockContext scriptBlock() {
			return getRuleContext(ScriptBlockContext.class,0);
		}
		public FinallyBodyBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_finallyBodyBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterFinallyBodyBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitFinallyBodyBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitFinallyBodyBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FinallyBodyBlockContext finallyBodyBlock() throws RecognitionException {
		FinallyBodyBlockContext _localctx = new FinallyBodyBlockContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_finallyBodyBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(202);
			scriptBlock();
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
	public static class ClassNameBlockContext extends ParserRuleContext {
		public TerminalNode NAMING() { return getToken(TinyScriptParser.NAMING, 0); }
		public ClassNameBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classNameBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterClassNameBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitClassNameBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitClassNameBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassNameBlockContext classNameBlock() throws RecognitionException {
		ClassNameBlockContext _localctx = new ClassNameBlockContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_classNameBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(204);
			match(NAMING);
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
	public static class ParenSegmentContext extends ParserRuleContext {
		public TerminalNode TERM_PAREN_L() { return getToken(TinyScriptParser.TERM_PAREN_L, 0); }
		public ExpressContext express() {
			return getRuleContext(ExpressContext.class,0);
		}
		public TerminalNode TERM_PAREN_R() { return getToken(TinyScriptParser.TERM_PAREN_R, 0); }
		public ParenSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parenSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterParenSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitParenSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitParenSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParenSegmentContext parenSegment() throws RecognitionException {
		ParenSegmentContext _localctx = new ParenSegmentContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_parenSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(206);
			match(TERM_PAREN_L);
				setState(207);
			express(0);
				setState(208);
			match(TERM_PAREN_R);
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
	public static class PrefixOperatorSegmentContext extends ParserRuleContext {
		public TerminalNode PREFIX_OPERATOR() { return getToken(TinyScriptParser.PREFIX_OPERATOR, 0); }
		public ExpressContext express() {
			return getRuleContext(ExpressContext.class,0);
		}
		public PrefixOperatorSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixOperatorSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterPrefixOperatorSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitPrefixOperatorSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitPrefixOperatorSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixOperatorSegmentContext prefixOperatorSegment() throws RecognitionException {
		PrefixOperatorSegmentContext _localctx = new PrefixOperatorSegmentContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_prefixOperatorSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(210);
			match(PREFIX_OPERATOR);
				setState(211);
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
	public static class ControlSegmentContext extends ParserRuleContext {
		public ExpressContext express() {
			return getRuleContext(ExpressContext.class,0);
		}
		public ControlSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_controlSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterControlSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitControlSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitControlSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ControlSegmentContext controlSegment() throws RecognitionException {
		ControlSegmentContext _localctx = new ControlSegmentContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_controlSegment);
		try {
			setState(219);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__37:
				enterOuterAlt(_localctx, 1);
				{
					setState(213);
				match(T__37);
				}
				break;
			case T__38:
				enterOuterAlt(_localctx, 2);
				{
					setState(214);
				match(T__38);
				}
				break;
			case T__39:
				enterOuterAlt(_localctx, 3);
				{
					setState(215);
				match(T__39);
					setState(217);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
				case 1:
					{
						setState(216);
					express(0);
					}
					break;
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
	public static class WhileSegmentContext extends ParserRuleContext {
		public TerminalNode TERM_PAREN_L() { return getToken(TinyScriptParser.TERM_PAREN_L, 0); }
		public ConditionBlockContext conditionBlock() {
			return getRuleContext(ConditionBlockContext.class,0);
		}
		public TerminalNode TERM_PAREN_R() { return getToken(TinyScriptParser.TERM_PAREN_R, 0); }
		public ScriptBlockContext scriptBlock() {
			return getRuleContext(ScriptBlockContext.class,0);
		}
		public WhileSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterWhileSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitWhileSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitWhileSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileSegmentContext whileSegment() throws RecognitionException {
		WhileSegmentContext _localctx = new WhileSegmentContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_whileSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(221);
			match(T__40);
				setState(222);
			match(TERM_PAREN_L);
				setState(223);
			conditionBlock();
				setState(224);
			match(TERM_PAREN_R);
				setState(225);
			scriptBlock();
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
	public static class ForSegmentContext extends ParserRuleContext {
		public TerminalNode TERM_PAREN_L() { return getToken(TinyScriptParser.TERM_PAREN_L, 0); }
		public List<ExpressContext> express() {
			return getRuleContexts(ExpressContext.class);
		}
		public ExpressContext express(int i) {
			return getRuleContext(ExpressContext.class,i);
		}
		public List<TerminalNode> TERM_SEMICOLON() { return getTokens(TinyScriptParser.TERM_SEMICOLON); }
		public TerminalNode TERM_SEMICOLON(int i) {
			return getToken(TinyScriptParser.TERM_SEMICOLON, i);
		}
		public ConditionBlockContext conditionBlock() {
			return getRuleContext(ConditionBlockContext.class,0);
		}
		public TerminalNode TERM_PAREN_R() { return getToken(TinyScriptParser.TERM_PAREN_R, 0); }
		public ScriptBlockContext scriptBlock() {
			return getRuleContext(ScriptBlockContext.class,0);
		}
		public ForSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterForSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitForSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitForSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForSegmentContext forSegment() throws RecognitionException {
		ForSegmentContext _localctx = new ForSegmentContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_forSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(227);
			match(T__41);
				setState(228);
			match(TERM_PAREN_L);
				setState(229);
			express(0);
				setState(230);
			match(TERM_SEMICOLON);
				setState(231);
			conditionBlock();
				setState(232);
			match(TERM_SEMICOLON);
				setState(233);
			express(0);
				setState(234);
			match(TERM_PAREN_R);
				setState(235);
			scriptBlock();
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
	public static class ForeachSegmentContext extends ParserRuleContext {
		public TerminalNode TERM_PAREN_L() { return getToken(TinyScriptParser.TERM_PAREN_L, 0); }
		public NamingBlockContext namingBlock() {
			return getRuleContext(NamingBlockContext.class,0);
		}
		public TerminalNode TERM_COLON() { return getToken(TinyScriptParser.TERM_COLON, 0); }
		public ExpressContext express() {
			return getRuleContext(ExpressContext.class,0);
		}
		public TerminalNode TERM_PAREN_R() { return getToken(TinyScriptParser.TERM_PAREN_R, 0); }
		public ScriptBlockContext scriptBlock() {
			return getRuleContext(ScriptBlockContext.class,0);
		}
		public ForeachSegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_foreachSegment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterForeachSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitForeachSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitForeachSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForeachSegmentContext foreachSegment() throws RecognitionException {
		ForeachSegmentContext _localctx = new ForeachSegmentContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_foreachSegment);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(237);
			match(T__42);
				setState(238);
			match(TERM_PAREN_L);
				setState(239);
			namingBlock();
				setState(240);
			match(TERM_COLON);
				setState(241);
			express(0);
				setState(242);
			match(TERM_PAREN_R);
				setState(243);
			scriptBlock();
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
	public static class NamingBlockContext extends ParserRuleContext {
		public TerminalNode NAMING() { return getToken(TinyScriptParser.NAMING, 0); }
		public NamingBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namingBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterNamingBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitNamingBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitNamingBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamingBlockContext namingBlock() throws RecognitionException {
		NamingBlockContext _localctx = new NamingBlockContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_namingBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(245);
			match(NAMING);
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
	public static class IfSegmentContext extends ParserRuleContext {
		public List<TerminalNode> TERM_PAREN_L() { return getTokens(TinyScriptParser.TERM_PAREN_L); }
		public TerminalNode TERM_PAREN_L(int i) {
			return getToken(TinyScriptParser.TERM_PAREN_L, i);
		}
		public List<ConditionBlockContext> conditionBlock() {
			return getRuleContexts(ConditionBlockContext.class);
		}
		public ConditionBlockContext conditionBlock(int i) {
			return getRuleContext(ConditionBlockContext.class,i);
		}
		public List<TerminalNode> TERM_PAREN_R() { return getTokens(TinyScriptParser.TERM_PAREN_R); }
		public TerminalNode TERM_PAREN_R(int i) {
			return getToken(TinyScriptParser.TERM_PAREN_R, i);
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
		enterRule(_localctx, 34, RULE_ifSegment);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(247);
			match(T__43);
				setState(248);
			match(TERM_PAREN_L);
				setState(249);
			conditionBlock();
				setState(250);
			match(TERM_PAREN_R);
				setState(251);
			scriptBlock();
				setState(264);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
						setState(255);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case T__44:
						{
							setState(252);
						match(T__44);
							setState(253);
						match(T__43);
						}
						break;
					case T__45:
						{
							setState(254);
						match(T__45);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
						setState(257);
					match(TERM_PAREN_L);
						setState(258);
					conditionBlock();
						setState(259);
					match(TERM_PAREN_R);
						setState(260);
						scriptBlock();
					}
					} 
				}
				setState(266);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
				setState(269);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
					setState(267);
				match(T__44);
					setState(268);
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
		enterRule(_localctx, 36, RULE_conditionBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(271);
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
		public TerminalNode TERM_CURLY_L() { return getToken(TinyScriptParser.TERM_CURLY_L, 0); }

		public TerminalNode TERM_CURLY_R() {
			return getToken(TinyScriptParser.TERM_CURLY_R, 0);
		}
		public ScriptContext script() {
			return getRuleContext(ScriptContext.class,0);
		}
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
		enterRule(_localctx, 38, RULE_scriptBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(273);
			match(TERM_CURLY_L);
				setState(275);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -234152120805227520L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 8732833791L) != 0)) {
					{
						setState(274);
						script();
					}
				}

				setState(277);
			match(TERM_CURLY_R);
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
		public ExpressContext express() {
			return getRuleContext(ExpressContext.class,0);
		}
		public TerminalNode ROUTE_NAMING() { return getToken(TinyScriptParser.ROUTE_NAMING, 0); }
		public TerminalNode NAMING() { return getToken(TinyScriptParser.NAMING, 0); }
		public ExtractExpressContext extractExpress() {
			return getRuleContext(ExtractExpressContext.class, 0);
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
		enterRule(_localctx, 40, RULE_equalValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(282);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
					case ROUTE_NAMING: {
						setState(279);
						match(ROUTE_NAMING);
					}
					break;
					case NAMING: {
						setState(280);
						match(NAMING);
					}
					break;
					case T__53: {
						setState(281);
						extractExpress();
					}
					break;
					default:
						throw new NoViableAltException(this);
				}
				setState(284);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 17873661021126656L) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(285);
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
				setState(287);
				match(T__53);
				setState(288);
				match(TERM_CURLY_L);
				setState(290);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((((_la - 62)) & ~0x3f) == 0 && ((1L << (_la - 62)) & 98307L) != 0)) {
					{
						setState(289);
						extractPairs();
					}
				}

				setState(292);
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
				setState(294);
				extractPair();
				setState(299);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == TERM_COMMA) {
					{
						{
							setState(295);
							match(TERM_COMMA);
							setState(296);
							extractPair();
						}
					}
					setState(301);
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
				setState(305);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
					case NAMING: {
						setState(302);
						match(NAMING);
					}
					break;
					case ROUTE_NAMING: {
						setState(303);
						match(ROUTE_NAMING);
					}
					break;
					case TERM_CONST_STRING:
					case TERM_CONST_STRING_SINGLE: {
						setState(304);
						constString();
					}
					break;
					default:
						throw new NoViableAltException(this);
				}
				setState(313);
				_errHandler.sync(this);
			_la = _input.LA(1);
				if (_la == TERM_COLON) {
					{
						setState(307);
						match(TERM_COLON);
						setState(311);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
							case NAMING: {
								setState(308);
								match(NAMING);
							}
							break;
							case ROUTE_NAMING: {
								setState(309);
								match(ROUTE_NAMING);
							}
							break;
							case TERM_CONST_STRING:
							case TERM_CONST_STRING_SINGLE: {
								setState(310);
								constString();
							}
							break;
							default:
								throw new NoViableAltException(this);
						}
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
		enterRule(_localctx, 48, RULE_newInstance);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(315);
				match(T__54);
				setState(316);
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
		public List<TerminalNode> TERM_DOT() { return getTokens(TinyScriptParser.TERM_DOT); }
		public TerminalNode TERM_DOT(int i) {
			return getToken(TinyScriptParser.TERM_DOT, i);
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
		enterRule(_localctx, 50, RULE_invokeFunction);
		try {
			int _alt;
			setState(334);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case REF_EXPRESS:
				enterOuterAlt(_localctx, 1);
				{
					setState(318);
				refCall();
					setState(323);
				_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 22, _ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
							setState(319);
						match(TERM_DOT);
							setState(320);
						functionCall();
						}
						} 
					}
					setState(325);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 22, _ctx);
				}
				}
				break;
			case NAMING:
				enterOuterAlt(_localctx, 2);
				{
					setState(326);
				functionCall();
					setState(331);
				_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 23, _ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
							setState(327);
						match(TERM_DOT);
							setState(328);
						functionCall();
						}
						} 
					}
					setState(333);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 23, _ctx);
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
		public TerminalNode TERM_PAREN_L() { return getToken(TinyScriptParser.TERM_PAREN_L, 0); }
		public TerminalNode TERM_PAREN_R() { return getToken(TinyScriptParser.TERM_PAREN_R, 0); }
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
		enterRule(_localctx, 52, RULE_functionCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(336);
			match(NAMING);
				setState(337);
			match(TERM_PAREN_L);
				setState(339);
			_errHandler.sync(this);
			_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -234152120805227520L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 8732833791L) != 0)) {
				{
					setState(338);
				argumentList();
				}
			}

				setState(341);
			match(TERM_PAREN_R);
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
		public TerminalNode TERM_DOT() { return getToken(TinyScriptParser.TERM_DOT, 0); }
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
		enterRule(_localctx, 54, RULE_refCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(343);
			refValue();
				setState(344);
			match(TERM_DOT);
				setState(345);
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
		public List<TerminalNode> TERM_COMMA() { return getTokens(TinyScriptParser.TERM_COMMA); }
		public TerminalNode TERM_COMMA(int i) {
			return getToken(TinyScriptParser.TERM_COMMA, i);
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
		enterRule(_localctx, 56, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(347);
			argument();
				setState(352);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TERM_COMMA) {
				{
				{
					setState(348);
				match(TERM_COMMA);
					setState(349);
				argument();
				}
				}
				setState(354);
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
			return getRuleContext(ArgumentValueContext.class,0);
		}
		public TerminalNode TERM_COLON() { return getToken(TinyScriptParser.TERM_COLON, 0); }
		public TerminalNode NAMING() { return getToken(TinyScriptParser.NAMING, 0); }
		public ConstStringContext constString() {
			return getRuleContext(ConstStringContext.class,0);
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
		enterRule(_localctx, 58, RULE_argument);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(360);
			_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 28, _ctx)) {
			case 1:
				{
					setState(357);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case NAMING:
					{
						setState(355);
					match(NAMING);
					}
					break;
				case TERM_CONST_STRING:
				case TERM_CONST_STRING_SINGLE:
					{
						setState(356);
					constString();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
					setState(359);
				match(TERM_COLON);
				}
				break;
			}
				setState(362);
			argumentValue();
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
	public static class ArgumentValueContext extends ParserRuleContext {
		public ExpressContext express() {
			return getRuleContext(ExpressContext.class,0);
		}
		public ArgumentValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterArgumentValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitArgumentValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitArgumentValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentValueContext argumentValue() throws RecognitionException {
		ArgumentValueContext _localctx = new ArgumentValueContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_argumentValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(364);
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
	public static class ConstValueContext extends ParserRuleContext {
		public ConstBoolContext constBool() {
			return getRuleContext(ConstBoolContext.class,0);
		}
		public ConstClassContext constClass() {
			return getRuleContext(ConstClassContext.class,0);
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
		enterRule(_localctx, 62, RULE_constValue);
		try {
			setState(376);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TERM_CONST_BOOLEAN:
				enterOuterAlt(_localctx, 1);
				{
					setState(366);
				constBool();
				}
				break;
			case TERM_CONST_TYPE_CLASS:
				enterOuterAlt(_localctx, 2);
				{
					setState(367);
				constClass();
				}
				break;
			case TERM_CONST_NULL:
				enterOuterAlt(_localctx, 3);
				{
					setState(368);
				constNull();
				}
				break;
			case TERM_CONST_STRING_MULTILINE:
			case TERM_CONST_STRING_MULTILINE_QUOTE:
				enterOuterAlt(_localctx, 4);
				{
					setState(369);
				constMultilineString();
				}
				break;
			case TERM_CONST_STRING_RENDER:
			case TERM_CONST_STRING_RENDER_SINGLE:
				enterOuterAlt(_localctx, 5);
				{
					setState(370);
				constRenderString();
				}
				break;
			case TERM_CONST_STRING:
			case TERM_CONST_STRING_SINGLE:
				enterOuterAlt(_localctx, 6);
				{
					setState(371);
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
					setState(372);
				decNumber();
				}
				break;
			case TERM_CONST_NUMBER_HEX:
				enterOuterAlt(_localctx, 8);
				{
					setState(373);
				hexNumber();
				}
				break;
			case TERM_CONST_NUMBER_OTC:
				enterOuterAlt(_localctx, 9);
				{
					setState(374);
				otcNumber();
				}
				break;
			case TERM_CONST_NUMBER_BIN:
				enterOuterAlt(_localctx, 10);
				{
					setState(375);
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
		enterRule(_localctx, 64, RULE_refValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(378);
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
		public TerminalNode TERM_CONST_BOOLEAN() { return getToken(TinyScriptParser.TERM_CONST_BOOLEAN, 0); }
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
		enterRule(_localctx, 66, RULE_constBool);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(380);
			match(TERM_CONST_BOOLEAN);
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
		public TerminalNode TERM_CONST_NULL() { return getToken(TinyScriptParser.TERM_CONST_NULL, 0); }
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
		enterRule(_localctx, 68, RULE_constNull);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(382);
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
	public static class ConstClassContext extends ParserRuleContext {
		public TerminalNode TERM_CONST_TYPE_CLASS() { return getToken(TinyScriptParser.TERM_CONST_TYPE_CLASS, 0); }
		public ConstClassContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constClass; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).enterConstClass(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TinyScriptListener ) ((TinyScriptListener)listener).exitConstClass(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TinyScriptVisitor ) return ((TinyScriptVisitor<? extends T>)visitor).visitConstClass(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstClassContext constClass() throws RecognitionException {
		ConstClassContext _localctx = new ConstClassContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_constClass);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(384);
			match(TERM_CONST_TYPE_CLASS);
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
		public TerminalNode TERM_CONST_STRING() { return getToken(TinyScriptParser.TERM_CONST_STRING, 0); }
		public TerminalNode TERM_CONST_STRING_SINGLE() { return getToken(TinyScriptParser.TERM_CONST_STRING_SINGLE, 0); }
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
		enterRule(_localctx, 72, RULE_constString);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(386);
			_la = _input.LA(1);
			if ( !(_la==TERM_CONST_STRING || _la==TERM_CONST_STRING_SINGLE) ) {
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
	public static class ConstMultilineStringContext extends ParserRuleContext {
		public TerminalNode TERM_CONST_STRING_MULTILINE() { return getToken(TinyScriptParser.TERM_CONST_STRING_MULTILINE, 0); }
		public TerminalNode TERM_CONST_STRING_MULTILINE_QUOTE() { return getToken(TinyScriptParser.TERM_CONST_STRING_MULTILINE_QUOTE, 0); }
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
		enterRule(_localctx, 74, RULE_constMultilineString);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(388);
			_la = _input.LA(1);
			if ( !(_la==TERM_CONST_STRING_MULTILINE || _la==TERM_CONST_STRING_MULTILINE_QUOTE) ) {
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
	public static class ConstRenderStringContext extends ParserRuleContext {
		public TerminalNode TERM_CONST_STRING_RENDER() { return getToken(TinyScriptParser.TERM_CONST_STRING_RENDER, 0); }
		public TerminalNode TERM_CONST_STRING_RENDER_SINGLE() { return getToken(TinyScriptParser.TERM_CONST_STRING_RENDER_SINGLE, 0); }
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
		enterRule(_localctx, 76, RULE_constRenderString);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(390);
			_la = _input.LA(1);
			if ( !(_la==TERM_CONST_STRING_RENDER || _la==TERM_CONST_STRING_RENDER_SINGLE) ) {
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
	public static class DecNumberContext extends ParserRuleContext {
		public TerminalNode TERM_CONST_NUMBER_SCIEN_2() { return getToken(TinyScriptParser.TERM_CONST_NUMBER_SCIEN_2, 0); }
		public TerminalNode TERM_CONST_NUMBER_SCIEN_1() { return getToken(TinyScriptParser.TERM_CONST_NUMBER_SCIEN_1, 0); }
		public TerminalNode TERM_CONST_NUMBER_FLOAT() { return getToken(TinyScriptParser.TERM_CONST_NUMBER_FLOAT, 0); }
		public TerminalNode TERM_CONST_NUMBER() { return getToken(TinyScriptParser.TERM_CONST_NUMBER, 0); }
		public TerminalNode TERM_DIGIT() { return getToken(TinyScriptParser.TERM_DIGIT, 0); }
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
		enterRule(_localctx, 78, RULE_decNumber);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(392);
			_la = _input.LA(1);
				if (!(((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 536870927L) != 0))) {
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
		public TerminalNode TERM_CONST_NUMBER_HEX() { return getToken(TinyScriptParser.TERM_CONST_NUMBER_HEX, 0); }
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
		enterRule(_localctx, 80, RULE_hexNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(394);
			match(TERM_CONST_NUMBER_HEX);
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
		public TerminalNode TERM_CONST_NUMBER_OTC() { return getToken(TinyScriptParser.TERM_CONST_NUMBER_OTC, 0); }
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
		enterRule(_localctx, 82, RULE_otcNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(396);
			match(TERM_CONST_NUMBER_OTC);
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
		public TerminalNode TERM_CONST_NUMBER_BIN() { return getToken(TinyScriptParser.TERM_CONST_NUMBER_BIN, 0); }
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
		enterRule(_localctx, 84, RULE_binNumber);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(398);
			match(TERM_CONST_NUMBER_BIN);
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
		enterRule(_localctx, 86, RULE_jsonValue);
		try {
			setState(405);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 30, _ctx)) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
					setState(400);
				invokeFunction();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
					setState(401);
				constValue();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
					setState(402);
				refValue();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
					setState(403);
				jsonArrayValue();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
					setState(404);
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
		public TerminalNode TERM_CURLY_L() { return getToken(TinyScriptParser.TERM_CURLY_L, 0); }
		public TerminalNode TERM_CURLY_R() { return getToken(TinyScriptParser.TERM_CURLY_R, 0); }
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
		enterRule(_localctx, 88, RULE_jsonMapValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(407);
			match(TERM_CURLY_L);
				setState(409);
			_errHandler.sync(this);
			_la = _input.LA(1);
				if (((((_la - 62)) & ~0x3f) == 0 && ((1L << (_la - 62)) & 32771L) != 0)) {
				{
					setState(408);
				jsonPairs();
				}
			}

				setState(411);
			match(TERM_CURLY_R);
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
		public List<TerminalNode> TERM_COMMA() { return getTokens(TinyScriptParser.TERM_COMMA); }
		public TerminalNode TERM_COMMA(int i) {
			return getToken(TinyScriptParser.TERM_COMMA, i);
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
		enterRule(_localctx, 90, RULE_jsonPairs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(413);
			jsonPair();
				setState(418);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TERM_COMMA) {
				{
				{
					setState(414);
				match(TERM_COMMA);
					setState(415);
				jsonPair();
				}
				}
				setState(420);
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
	public static class JsonPairContext extends ParserRuleContext {
		public TerminalNode TERM_COLON() { return getToken(TinyScriptParser.TERM_COLON, 0); }
		public ExpressContext express() {
			return getRuleContext(ExpressContext.class,0);
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
		enterRule(_localctx, 92, RULE_jsonPair);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(423);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NAMING:
				{
					setState(421);
				match(NAMING);
				}
				break;
			case TERM_CONST_STRING:
			case TERM_CONST_STRING_SINGLE:
				{
					setState(422);
				constString();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
				setState(425);
			match(TERM_COLON);
				setState(426);
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
	public static class JsonArrayValueContext extends ParserRuleContext {
		public TerminalNode TERM_BRACKET_SQUARE_L() { return getToken(TinyScriptParser.TERM_BRACKET_SQUARE_L, 0); }
		public TerminalNode TERM_BRACKET_SQUARE_R() { return getToken(TinyScriptParser.TERM_BRACKET_SQUARE_R, 0); }
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
		enterRule(_localctx, 94, RULE_jsonArrayValue);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(428);
			match(TERM_BRACKET_SQUARE_L);
				setState(430);
			_errHandler.sync(this);
			_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -234152120805227520L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 8732833791L) != 0)) {
				{
					setState(429);
				jsonItemList();
				}
			}

				setState(432);
			match(TERM_BRACKET_SQUARE_R);
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
		public List<ExpressContext> express() {
			return getRuleContexts(ExpressContext.class);
		}
		public ExpressContext express(int i) {
			return getRuleContext(ExpressContext.class,i);
		}
		public List<TerminalNode> TERM_COMMA() { return getTokens(TinyScriptParser.TERM_COMMA); }
		public TerminalNode TERM_COMMA(int i) {
			return getToken(TinyScriptParser.TERM_COMMA, i);
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
		enterRule(_localctx, 96, RULE_jsonItemList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(434);
			express(0);
				setState(439);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==TERM_COMMA) {
				{
				{
					setState(435);
				match(TERM_COMMA);
					setState(436);
				express(0);
				}
				}
				setState(441);
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
			"\u0004\u0001e\u01bb\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002" +
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"+
		"(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007,\u0002"+
					"-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u0001\u0000\u0001\u0000" +
					"\u0001\u0000\u0005\u0000f\b\u0000\n\u0000\f\u0000i\t\u0000\u0001\u0000" +
					"\u0003\u0000l\b\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001" +
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
					"\u0001\u0001\u0001\u0001\u0003\u0001\u0080\b\u0001\u0001\u0001\u0001\u0001" +
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
					"\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001\u0099\b\u0001\n\u0001" +
					"\f\u0001\u009c\t\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003" +
					"\u0001\u0003\u0003\u0003\u00a3\b\u0003\u0001\u0003\u0001\u0003\u0001\u0003" +
					"\u0001\u0003\u0003\u0003\u00a9\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004" +
					"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004\u00b2\b\u0004" +
					"\n\u0004\f\u0004\u00b5\t\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001" +
					"\u0004\u0005\u0004\u00bb\b\u0004\n\u0004\f\u0004\u00be\t\u0004\u0001\u0004" +
					"\u0001\u0004\u0003\u0004\u00c2\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005" +
					"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001" +
					"\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001" +
					"\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u00da\b\f\u0003\f\u00dc" +
					"\b\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001" +
					"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001" +
					"\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001" +
					"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001" +
					"\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001" +
					"\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u0100\b\u0011\u0001\u0011\u0001" +
					"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0005\u0011\u0107\b\u0011\n" +
					"\u0011\f\u0011\u010a\t\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u010e" +
					"\b\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0003\u0013\u0114" +
					"\b\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0003" +
					"\u0014\u011b\b\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001" +
					"\u0015\u0001\u0015\u0003\u0015\u0123\b\u0015\u0001\u0015\u0001\u0015\u0001" +
					"\u0016\u0001\u0016\u0001\u0016\u0005\u0016\u012a\b\u0016\n\u0016\f\u0016" +
					"\u012d\t\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u0132\b" +
					"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u0138" +
					"\b\u0017\u0003\u0017\u013a\b\u0017\u0001\u0018\u0001\u0018\u0001\u0018" +
					"\u0001\u0019\u0001\u0019\u0001\u0019\u0005\u0019\u0142\b\u0019\n\u0019" +
					"\f\u0019\u0145\t\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0005\u0019" +
					"\u014a\b\u0019\n\u0019\f\u0019\u014d\t\u0019\u0003\u0019\u014f\b\u0019" +
					"\u0001\u001a\u0001\u001a\u0001\u001a\u0003\u001a\u0154\b\u001a\u0001\u001a" +
					"\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001c" +
					"\u0001\u001c\u0001\u001c\u0005\u001c\u015f\b\u001c\n\u001c\f\u001c\u0162" +
					"\t\u001c\u0001\u001d\u0001\u001d\u0003\u001d\u0166\b\u001d\u0001\u001d" +
					"\u0003\u001d\u0169\b\u001d\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e" +
					"\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f" +
					"\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u0179\b\u001f" +
					"\u0001 \u0001 \u0001!\u0001!\u0001\"\u0001\"\u0001#\u0001#\u0001$\u0001" +
					"$\u0001%\u0001%\u0001&\u0001&\u0001\'\u0001\'\u0001(\u0001(\u0001)\u0001" +
					")\u0001*\u0001*\u0001+\u0001+\u0001+\u0001+\u0001+\u0003+\u0196\b+\u0001" +
					",\u0001,\u0003,\u019a\b,\u0001,\u0001,\u0001-\u0001-\u0001-\u0005-\u01a1" +
					"\b-\n-\f-\u01a4\t-\u0001.\u0001.\u0003.\u01a8\b.\u0001.\u0001.\u0001." +
					"\u0001/\u0001/\u0003/\u01af\b/\u0001/\u0001/\u00010\u00010\u00010\u0005" +
					"0\u01b6\b0\n0\f0\u01b9\t0\u00010\u0000\u0001\u00021\u0000\u0002\u0004" +
					"\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"" +
					"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`\u0000\n\u0001\u0000\u0002\u0006\u0002" +
					"\u0000\u0001\u0001\u0007\b\u0001\u0000\t\n\u0001\u0000\u000b\u001a\u0001" +
					"\u0000\u001b\u001e\u0001\u0000/5\u0001\u0000>?\u0001\u0000:;\u0001\u0000" +
					"<=\u0002\u0000DGaa\u01d0\u0000b\u0001\u0000\u0000\u0000\u0002\u007f\u0001" +
					"\u0000\u0000\u0000\u0004\u009d\u0001\u0000\u0000\u0000\u0006\u00a0\u0001" +
					"\u0000\u0000\u0000\b\u00aa\u0001\u0000\u0000\u0000\n\u00c3\u0001\u0000" +
					"\u0000\u0000\f\u00c6\u0001\u0000\u0000\u0000\u000e\u00c8\u0001\u0000\u0000" +
					"\u0000\u0010\u00ca\u0001\u0000\u0000\u0000\u0012\u00cc\u0001\u0000\u0000" +
					"\u0000\u0014\u00ce\u0001\u0000\u0000\u0000\u0016\u00d2\u0001\u0000\u0000" +
					"\u0000\u0018\u00db\u0001\u0000\u0000\u0000\u001a\u00dd\u0001\u0000\u0000" +
					"\u0000\u001c\u00e3\u0001\u0000\u0000\u0000\u001e\u00ed\u0001\u0000\u0000" +
					"\u0000 \u00f5\u0001\u0000\u0000\u0000\"\u00f7\u0001\u0000\u0000\u0000" +
					"$\u010f\u0001\u0000\u0000\u0000&\u0111\u0001\u0000\u0000\u0000(\u011a" +
					"\u0001\u0000\u0000\u0000*\u011f\u0001\u0000\u0000\u0000,\u0126\u0001\u0000" +
					"\u0000\u0000.\u0131\u0001\u0000\u0000\u00000\u013b\u0001\u0000\u0000\u0000" +
					"2\u014e\u0001\u0000\u0000\u00004\u0150\u0001\u0000\u0000\u00006\u0157" +
					"\u0001\u0000\u0000\u00008\u015b\u0001\u0000\u0000\u0000:\u0168\u0001\u0000" +
					"\u0000\u0000<\u016c\u0001\u0000\u0000\u0000>\u0178\u0001\u0000\u0000\u0000" +
					"@\u017a\u0001\u0000\u0000\u0000B\u017c\u0001\u0000\u0000\u0000D\u017e" +
					"\u0001\u0000\u0000\u0000F\u0180\u0001\u0000\u0000\u0000H\u0182\u0001\u0000" +
					"\u0000\u0000J\u0184\u0001\u0000\u0000\u0000L\u0186\u0001\u0000\u0000\u0000" +
					"N\u0188\u0001\u0000\u0000\u0000P\u018a\u0001\u0000\u0000\u0000R\u018c" +
					"\u0001\u0000\u0000\u0000T\u018e\u0001\u0000\u0000\u0000V\u0195\u0001\u0000" +
					"\u0000\u0000X\u0197\u0001\u0000\u0000\u0000Z\u019d\u0001\u0000\u0000\u0000" +
					"\\\u01a7\u0001\u0000\u0000\u0000^\u01ac\u0001\u0000\u0000\u0000`\u01b2" +
					"\u0001\u0000\u0000\u0000bg\u0003\u0002\u0001\u0000cd\u0005Y\u0000\u0000" +
					"df\u0003\u0002\u0001\u0000ec\u0001\u0000\u0000\u0000fi\u0001\u0000\u0000" +
					"\u0000ge\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000\u0000hk\u0001\u0000" +
					"\u0000\u0000ig\u0001\u0000\u0000\u0000jl\u0005Y\u0000\u0000kj\u0001\u0000" +
					"\u0000\u0000kl\u0001\u0000\u0000\u0000l\u0001\u0001\u0000\u0000\u0000" +
					"mn\u0006\u0001\uffff\uffff\u0000n\u0080\u0003\u0006\u0003\u0000o\u0080" +
					"\u0003\"\u0011\u0000p\u0080\u0003\u001e\u000f\u0000q\u0080\u0003\u001c" +
					"\u000e\u0000r\u0080\u0003\u001a\r\u0000s\u0080\u0003\u0018\f\u0000t\u0080" +
					"\u0003\b\u0004\u0000u\u0080\u0003\n\u0005\u0000v\u0080\u0003\u0014\n\u0000" +
					"w\u0080\u0003\u0016\u000b\u0000x\u0080\u0003(\u0014\u0000y\u0080\u0003" +
					"0\u0018\u0000z\u0080\u00032\u0019\u0000{\u0080\u0003>\u001f\u0000|\u0080" +
					"\u0003@ \u0000}\u0080\u0003V+\u0000~\u0080\u0003\u0004\u0002\u0000\u007f" +
					"m\u0001\u0000\u0000\u0000\u007fo\u0001\u0000\u0000\u0000\u007fp\u0001" +
					"\u0000\u0000\u0000\u007fq\u0001\u0000\u0000\u0000\u007fr\u0001\u0000\u0000" +
					"\u0000\u007fs\u0001\u0000\u0000\u0000\u007ft\u0001\u0000\u0000\u0000\u007f" +
					"u\u0001\u0000\u0000\u0000\u007fv\u0001\u0000\u0000\u0000\u007fw\u0001" +
					"\u0000\u0000\u0000\u007fx\u0001\u0000\u0000\u0000\u007fy\u0001\u0000\u0000" +
					"\u0000\u007fz\u0001\u0000\u0000\u0000\u007f{\u0001\u0000\u0000\u0000\u007f" +
					"|\u0001\u0000\u0000\u0000\u007f}\u0001\u0000\u0000\u0000\u007f~\u0001" +
					"\u0000\u0000\u0000\u0080\u009a\u0001\u0000\u0000\u0000\u0081\u0082\n\u0007" +
					"\u0000\u0000\u0082\u0083\u0007\u0000\u0000\u0000\u0083\u0099\u0003\u0002" +
					"\u0001\b\u0084\u0085\n\u0006\u0000\u0000\u0085\u0086\u0007\u0001\u0000" +
					"\u0000\u0086\u0099\u0003\u0002\u0001\u0007\u0087\u0088\n\u0005\u0000\u0000" +
					"\u0088\u0089\u0007\u0002\u0000\u0000\u0089\u0099\u0003\u0002\u0001\u0006" +
					"\u008a\u008b\n\u0004\u0000\u0000\u008b\u008c\u0007\u0003\u0000\u0000\u008c" +
					"\u0099\u0003\u0002\u0001\u0005\u008d\u008e\n\u0003\u0000\u0000\u008e\u008f" +
					"\u0007\u0004\u0000\u0000\u008f\u0099\u0003\u0002\u0001\u0004\u0090\u0091" +
					"\n\u0001\u0000\u0000\u0091\u0092\u0005\u001f\u0000\u0000\u0092\u0093\u0003" +
					"\u0002\u0001\u0000\u0093\u0094\u0005Z\u0000\u0000\u0094\u0095\u0003\u0002" +
					"\u0001\u0002\u0095\u0099\u0001\u0000\u0000\u0000\u0096\u0097\n\b\u0000" +
					"\u0000\u0097\u0099\u0005\u0001\u0000\u0000\u0098\u0081\u0001\u0000\u0000" +
					"\u0000\u0098\u0084\u0001\u0000\u0000\u0000\u0098\u0087\u0001\u0000\u0000" +
					"\u0000\u0098\u008a\u0001\u0000\u0000\u0000\u0098\u008d\u0001\u0000\u0000" +
					"\u0000\u0098\u0090\u0001\u0000\u0000\u0000\u0098\u0096\u0001\u0000\u0000" +
					"\u0000\u0099\u009c\u0001\u0000\u0000\u0000\u009a\u0098\u0001\u0000\u0000" +
					"\u0000\u009a\u009b\u0001\u0000\u0000\u0000\u009b\u0003\u0001\u0000\u0000" +
					"\u0000\u009c\u009a\u0001\u0000\u0000\u0000\u009d\u009e\u0005\n\u0000\u0000" +
					"\u009e\u009f\u0003\u0002\u0001\u0000\u009f\u0005\u0001\u0000\u0000\u0000" +
					"\u00a0\u00a2\u0005 \u0000\u0000\u00a1\u00a3\u0003 \u0010\u0000\u00a2\u00a1" +
					"\u0001\u0000\u0000\u0000\u00a2\u00a3\u0001\u0000\u0000\u0000\u00a3\u00a8" +
					"\u0001\u0000\u0000\u0000\u00a4\u00a5\u0005R\u0000\u0000\u00a5\u00a6\u0003" +
					"$\u0012\u0000\u00a6\u00a7\u0005S\u0000\u0000\u00a7\u00a9\u0001\u0000\u0000" +
					"\u0000\u00a8\u00a4\u0001\u0000\u0000\u0000\u00a8\u00a9\u0001\u0000\u0000" +
					"\u0000\u00a9\u0007\u0001\u0000\u0000\u0000\u00aa\u00ab\u0005!\u0000\u0000" +
					"\u00ab\u00bc\u0003\f\u0006\u0000\u00ac\u00ad\u0005\"\u0000\u0000\u00ad" +
					"\u00ae\u0005R\u0000\u0000\u00ae\u00b3\u0003\u0012\t\u0000\u00af\u00b0" +
					"\u0005#\u0000\u0000\u00b0\u00b2\u0003\u0012\t\u0000\u00b1\u00af\u0001" +
					"\u0000\u0000\u0000\u00b2\u00b5\u0001\u0000\u0000\u0000\u00b3\u00b1\u0001" +
					"\u0000\u0000\u0000\u00b3\u00b4\u0001\u0000\u0000\u0000\u00b4\u00b6\u0001" +
					"\u0000\u0000\u0000\u00b5\u00b3\u0001\u0000\u0000\u0000\u00b6\u00b7\u0003" +
					" \u0010\u0000\u00b7\u00b8\u0005S\u0000\u0000\u00b8\u00b9\u0003\u000e\u0007" +
					"\u0000\u00b9\u00bb\u0001\u0000\u0000\u0000\u00ba\u00ac\u0001\u0000\u0000" +
					"\u0000\u00bb\u00be\u0001\u0000\u0000\u0000\u00bc\u00ba\u0001\u0000\u0000" +
					"\u0000\u00bc\u00bd\u0001\u0000\u0000\u0000\u00bd\u00c1\u0001\u0000\u0000" +
					"\u0000\u00be\u00bc\u0001\u0000\u0000\u0000\u00bf\u00c0\u0005$\u0000\u0000" +
					"\u00c0\u00c2\u0003\u0010\b\u0000\u00c1\u00bf\u0001\u0000\u0000\u0000\u00c1" +
					"\u00c2\u0001\u0000\u0000\u0000\u00c2\t\u0001\u0000\u0000\u0000\u00c3\u00c4" +
					"\u0005%\u0000\u0000\u00c4\u00c5\u0003\u0002\u0001\u0000\u00c5\u000b\u0001" +
					"\u0000\u0000\u0000\u00c6\u00c7\u0003&\u0013\u0000\u00c7\r\u0001\u0000" +
					"\u0000\u0000\u00c8\u00c9\u0003&\u0013\u0000\u00c9\u000f\u0001\u0000\u0000" +
					"\u0000\u00ca\u00cb\u0003&\u0013\u0000\u00cb\u0011\u0001\u0000\u0000\u0000" +
					"\u00cc\u00cd\u0005M\u0000\u0000\u00cd\u0013\u0001\u0000\u0000\u0000\u00ce" +
					"\u00cf\u0005R\u0000\u0000\u00cf\u00d0\u0003\u0002\u0001\u0000\u00d0\u00d1" +
					"\u0005S\u0000\u0000\u00d1\u0015\u0001\u0000\u0000\u0000\u00d2\u00d3\u0005" +
					"L\u0000\u0000\u00d3\u00d4\u0003\u0002\u0001\u0000\u00d4\u0017\u0001\u0000" +
					"\u0000\u0000\u00d5\u00dc\u0005&\u0000\u0000\u00d6\u00dc\u0005\'\u0000" +
					"\u0000\u00d7\u00d9\u0005(\u0000\u0000\u00d8\u00da\u0003\u0002\u0001\u0000" +
					"\u00d9\u00d8\u0001\u0000\u0000\u0000\u00d9\u00da\u0001\u0000\u0000\u0000" +
					"\u00da\u00dc\u0001\u0000\u0000\u0000\u00db\u00d5\u0001\u0000\u0000\u0000" +
					"\u00db\u00d6\u0001\u0000\u0000\u0000\u00db\u00d7\u0001\u0000\u0000\u0000" +
					"\u00dc\u0019\u0001\u0000\u0000\u0000\u00dd\u00de\u0005)\u0000\u0000\u00de" +
					"\u00df\u0005R\u0000\u0000\u00df\u00e0\u0003$\u0012\u0000\u00e0\u00e1\u0005" +
					"S\u0000\u0000\u00e1\u00e2\u0003&\u0013\u0000\u00e2\u001b\u0001\u0000\u0000" +
					"\u0000\u00e3\u00e4\u0005*\u0000\u0000\u00e4\u00e5\u0005R\u0000\u0000\u00e5" +
					"\u00e6\u0003\u0002\u0001\u0000\u00e6\u00e7\u0005Y\u0000\u0000\u00e7\u00e8" +
					"\u0003$\u0012\u0000\u00e8\u00e9\u0005Y\u0000\u0000\u00e9\u00ea\u0003\u0002" +
					"\u0001\u0000\u00ea\u00eb\u0005S\u0000\u0000\u00eb\u00ec\u0003&\u0013\u0000" +
					"\u00ec\u001d\u0001\u0000\u0000\u0000\u00ed\u00ee\u0005+\u0000\u0000\u00ee" +
					"\u00ef\u0005R\u0000\u0000\u00ef\u00f0\u0003 \u0010\u0000\u00f0\u00f1\u0005" +
					"Z\u0000\u0000\u00f1\u00f2\u0003\u0002\u0001\u0000\u00f2\u00f3\u0005S\u0000" +
					"\u0000\u00f3\u00f4\u0003&\u0013\u0000\u00f4\u001f\u0001\u0000\u0000\u0000" +
					"\u00f5\u00f6\u0005M\u0000\u0000\u00f6!\u0001\u0000\u0000\u0000\u00f7\u00f8" +
					"\u0005,\u0000\u0000\u00f8\u00f9\u0005R\u0000\u0000\u00f9\u00fa\u0003$" +
					"\u0012\u0000\u00fa\u00fb\u0005S\u0000\u0000\u00fb\u0108\u0003&\u0013\u0000" +
					"\u00fc\u00fd\u0005-\u0000\u0000\u00fd\u0100\u0005,\u0000\u0000\u00fe\u0100" +
					"\u0005.\u0000\u0000\u00ff\u00fc\u0001\u0000\u0000\u0000\u00ff\u00fe\u0001" +
					"\u0000\u0000\u0000\u0100\u0101\u0001\u0000\u0000\u0000\u0101\u0102\u0005" +
					"R\u0000\u0000\u0102\u0103\u0003$\u0012\u0000\u0103\u0104\u0005S\u0000" +
					"\u0000\u0104\u0105\u0003&\u0013\u0000\u0105\u0107\u0001\u0000\u0000\u0000" +
					"\u0106\u00ff\u0001\u0000\u0000\u0000\u0107\u010a\u0001\u0000\u0000\u0000" +
					"\u0108\u0106\u0001\u0000\u0000\u0000\u0108\u0109\u0001\u0000\u0000\u0000" +
					"\u0109\u010d\u0001\u0000\u0000\u0000\u010a\u0108\u0001\u0000\u0000\u0000" +
					"\u010b\u010c\u0005-\u0000\u0000\u010c\u010e\u0003&\u0013\u0000\u010d\u010b" +
					"\u0001\u0000\u0000\u0000\u010d\u010e\u0001\u0000\u0000\u0000\u010e#\u0001" +
					"\u0000\u0000\u0000\u010f\u0110\u0003\u0002\u0001\u0000\u0110%\u0001\u0000" +
					"\u0000\u0000\u0111\u0113\u0005W\u0000\u0000\u0112\u0114\u0003\u0000\u0000" +
					"\u0000\u0113\u0112\u0001\u0000\u0000\u0000\u0113\u0114\u0001\u0000\u0000" +
					"\u0000\u0114\u0115\u0001\u0000\u0000\u0000\u0115\u0116\u0005X\u0000\u0000" +
					"\u0116\'\u0001\u0000\u0000\u0000\u0117\u011b\u0005N\u0000\u0000\u0118" +
					"\u011b\u0005M\u0000\u0000\u0119\u011b\u0003*\u0015\u0000\u011a\u0117\u0001" +
					"\u0000\u0000\u0000\u011a\u0118\u0001\u0000\u0000\u0000\u011a\u0119\u0001" +
					"\u0000\u0000\u0000\u011b\u011c\u0001\u0000\u0000\u0000\u011c\u011d\u0007" +
					"\u0005\u0000\u0000\u011d\u011e\u0003\u0002\u0001\u0000\u011e)\u0001\u0000" +
					"\u0000\u0000\u011f\u0120\u00056\u0000\u0000\u0120\u0122\u0005W\u0000\u0000" +
					"\u0121\u0123\u0003,\u0016\u0000\u0122\u0121\u0001\u0000\u0000\u0000\u0122" +
					"\u0123\u0001\u0000\u0000\u0000\u0123\u0124\u0001\u0000\u0000\u0000\u0124" +
					"\u0125\u0005X\u0000\u0000\u0125+\u0001\u0000\u0000\u0000\u0126\u012b\u0003" +
					".\u0017\u0000\u0127\u0128\u0005T\u0000\u0000\u0128\u012a\u0003.\u0017" +
					"\u0000\u0129\u0127\u0001\u0000\u0000\u0000\u012a\u012d\u0001\u0000\u0000" +
					"\u0000\u012b\u0129\u0001\u0000\u0000\u0000\u012b\u012c\u0001\u0000\u0000" +
					"\u0000\u012c-\u0001\u0000\u0000\u0000\u012d\u012b\u0001\u0000\u0000\u0000" +
					"\u012e\u0132\u0005M\u0000\u0000\u012f\u0132\u0005N\u0000\u0000\u0130\u0132" +
					"\u0003H$\u0000\u0131\u012e\u0001\u0000\u0000\u0000\u0131\u012f\u0001\u0000" +
					"\u0000\u0000\u0131\u0130\u0001\u0000\u0000\u0000\u0132\u0139\u0001\u0000" +
					"\u0000\u0000\u0133\u0137\u0005Z\u0000\u0000\u0134\u0138\u0005M\u0000\u0000" +
					"\u0135\u0138\u0005N\u0000\u0000\u0136\u0138\u0003H$\u0000\u0137\u0134" +
					"\u0001\u0000\u0000\u0000\u0137\u0135\u0001\u0000\u0000\u0000\u0137\u0136" +
					"\u0001\u0000\u0000\u0000\u0138\u013a\u0001\u0000\u0000\u0000\u0139\u0133" +
					"\u0001\u0000\u0000\u0000\u0139\u013a\u0001\u0000\u0000\u0000\u013a/\u0001" +
					"\u0000\u0000\u0000\u013b\u013c\u00057\u0000\u0000\u013c\u013d\u00032\u0019" +
					"\u0000\u013d1\u0001\u0000\u0000\u0000\u013e\u0143\u00036\u001b\u0000\u013f" +
					"\u0140\u0005U\u0000\u0000\u0140\u0142\u00034\u001a\u0000\u0141\u013f\u0001" +
					"\u0000\u0000\u0000\u0142\u0145\u0001\u0000\u0000\u0000\u0143\u0141\u0001" +
					"\u0000\u0000\u0000\u0143\u0144\u0001\u0000\u0000\u0000\u0144\u014f\u0001" +
					"\u0000\u0000\u0000\u0145\u0143\u0001\u0000\u0000\u0000\u0146\u014b\u0003" +
					"4\u001a\u0000\u0147\u0148\u0005U\u0000\u0000\u0148\u014a\u00034\u001a" +
					"\u0000\u0149\u0147\u0001\u0000\u0000\u0000\u014a\u014d\u0001\u0000\u0000" +
					"\u0000\u014b\u0149\u0001\u0000\u0000\u0000\u014b\u014c\u0001\u0000\u0000" +
					"\u0000\u014c\u014f\u0001\u0000\u0000\u0000\u014d\u014b\u0001\u0000\u0000" +
					"\u0000\u014e\u013e\u0001\u0000\u0000\u0000\u014e\u0146\u0001\u0000\u0000" +
					"\u0000\u014f3\u0001\u0000\u0000\u0000\u0150\u0151\u0005M\u0000\u0000\u0151" +
					"\u0153\u0005R\u0000\u0000\u0152\u0154\u00038\u001c\u0000\u0153\u0152\u0001" +
					"\u0000\u0000\u0000\u0153\u0154\u0001\u0000\u0000\u0000\u0154\u0155\u0001" +
					"\u0000\u0000\u0000\u0155\u0156\u0005S\u0000\u0000\u01565\u0001\u0000\u0000" +
					"\u0000\u0157\u0158\u0003@ \u0000\u0158\u0159\u0005U\u0000\u0000\u0159" +
					"\u015a\u00034\u001a\u0000\u015a7\u0001\u0000\u0000\u0000\u015b\u0160\u0003" +
					":\u001d\u0000\u015c\u015d\u0005T\u0000\u0000\u015d\u015f\u0003:\u001d" +
					"\u0000\u015e\u015c\u0001\u0000\u0000\u0000\u015f\u0162\u0001\u0000\u0000" +
					"\u0000\u0160\u015e\u0001\u0000\u0000\u0000\u0160\u0161\u0001\u0000\u0000" +
					"\u0000\u01619\u0001\u0000\u0000\u0000\u0162\u0160\u0001\u0000\u0000\u0000" +
					"\u0163\u0166\u0005M\u0000\u0000\u0164\u0166\u0003H$\u0000\u0165\u0163" +
					"\u0001\u0000\u0000\u0000\u0165\u0164\u0001\u0000\u0000\u0000\u0166\u0167" +
					"\u0001\u0000\u0000\u0000\u0167\u0169\u0005Z\u0000\u0000\u0168\u0165\u0001" +
					"\u0000\u0000\u0000\u0168\u0169\u0001\u0000\u0000\u0000\u0169\u016a\u0001" +
					"\u0000\u0000\u0000\u016a\u016b\u0003<\u001e\u0000\u016b;\u0001\u0000\u0000" +
					"\u0000\u016c\u016d\u0003\u0002\u0001\u0000\u016d=\u0001\u0000\u0000\u0000" +
					"\u016e\u0179\u0003B!\u0000\u016f\u0179\u0003F#\u0000\u0170\u0179\u0003" +
					"D\"\u0000\u0171\u0179\u0003J%\u0000\u0172\u0179\u0003L&\u0000\u0173\u0179" +
					"\u0003H$\u0000\u0174\u0179\u0003N\'\u0000\u0175\u0179\u0003P(\u0000\u0176" +
					"\u0179\u0003R)\u0000\u0177\u0179\u0003T*\u0000\u0178\u016e\u0001\u0000" +
					"\u0000\u0000\u0178\u016f\u0001\u0000\u0000\u0000\u0178\u0170\u0001\u0000" +
					"\u0000\u0000\u0178\u0171\u0001\u0000\u0000\u0000\u0178\u0172\u0001\u0000" +
					"\u0000\u0000\u0178\u0173\u0001\u0000\u0000\u0000\u0178\u0174\u0001\u0000" +
					"\u0000\u0000\u0178\u0175\u0001\u0000\u0000\u0000\u0178\u0176\u0001\u0000" +
					"\u0000\u0000\u0178\u0177\u0001\u0000\u0000\u0000\u0179?\u0001\u0000\u0000" +
					"\u0000\u017a\u017b\u0005C\u0000\u0000\u017bA\u0001\u0000\u0000\u0000\u017c" +
					"\u017d\u0005@\u0000\u0000\u017dC\u0001\u0000\u0000\u0000\u017e\u017f\u0005" +
					"A\u0000\u0000\u017fE\u0001\u0000\u0000\u0000\u0180\u0181\u0005B\u0000" +
					"\u0000\u0181G\u0001\u0000\u0000\u0000\u0182\u0183\u0007\u0006\u0000\u0000" +
					"\u0183I\u0001\u0000\u0000\u0000\u0184\u0185\u0007\u0007\u0000\u0000\u0185" +
					"K\u0001\u0000\u0000\u0000\u0186\u0187\u0007\b\u0000\u0000\u0187M\u0001" +
					"\u0000\u0000\u0000\u0188\u0189\u0007\t\u0000\u0000\u0189O\u0001\u0000" +
					"\u0000\u0000\u018a\u018b\u0005H\u0000\u0000\u018bQ\u0001\u0000\u0000\u0000" +
					"\u018c\u018d\u0005I\u0000\u0000\u018dS\u0001\u0000\u0000\u0000\u018e\u018f" +
					"\u0005J\u0000\u0000\u018fU\u0001\u0000\u0000\u0000\u0190\u0196\u00032" +
					"\u0019\u0000\u0191\u0196\u0003>\u001f\u0000\u0192\u0196\u0003@ \u0000" +
					"\u0193\u0196\u0003^/\u0000\u0194\u0196\u0003X,\u0000\u0195\u0190\u0001" +
					"\u0000\u0000\u0000\u0195\u0191\u0001\u0000\u0000\u0000\u0195\u0192\u0001" +
					"\u0000\u0000\u0000\u0195\u0193\u0001\u0000\u0000\u0000\u0195\u0194\u0001" +
					"\u0000\u0000\u0000\u0196W\u0001\u0000\u0000\u0000\u0197\u0199\u0005W\u0000" +
					"\u0000\u0198\u019a\u0003Z-\u0000\u0199\u0198\u0001\u0000\u0000\u0000\u0199" +
					"\u019a\u0001\u0000\u0000\u0000\u019a\u019b\u0001\u0000\u0000\u0000\u019b" +
					"\u019c\u0005X\u0000\u0000\u019cY\u0001\u0000\u0000\u0000\u019d\u01a2\u0003" +
					"\\.\u0000\u019e\u019f\u0005T\u0000\u0000\u019f\u01a1\u0003\\.\u0000\u01a0" +
					"\u019e\u0001\u0000\u0000\u0000\u01a1\u01a4\u0001\u0000\u0000\u0000\u01a2" +
					"\u01a0\u0001\u0000\u0000\u0000\u01a2\u01a3\u0001\u0000\u0000\u0000\u01a3" +
					"[\u0001\u0000\u0000\u0000\u01a4\u01a2\u0001\u0000\u0000\u0000\u01a5\u01a8" +
					"\u0005M\u0000\u0000\u01a6\u01a8\u0003H$\u0000\u01a7\u01a5\u0001\u0000" +
					"\u0000\u0000\u01a7\u01a6\u0001\u0000\u0000\u0000\u01a8\u01a9\u0001\u0000" +
					"\u0000\u0000\u01a9\u01aa\u0005Z\u0000\u0000\u01aa\u01ab\u0003\u0002\u0001" +
					"\u0000\u01ab]\u0001\u0000\u0000\u0000\u01ac\u01ae\u0005[\u0000\u0000\u01ad" +
					"\u01af\u0003`0\u0000\u01ae\u01ad\u0001\u0000\u0000\u0000\u01ae\u01af\u0001" +
					"\u0000\u0000\u0000\u01af\u01b0\u0001\u0000\u0000\u0000\u01b0\u01b1\u0005" +
					"\\\u0000\u0000\u01b1_\u0001\u0000\u0000\u0000\u01b2\u01b7\u0003\u0002" +
					"\u0001\u0000\u01b3\u01b4\u0005T\u0000\u0000\u01b4\u01b6\u0003\u0002\u0001" +
					"\u0000\u01b5\u01b3\u0001\u0000\u0000\u0000\u01b6\u01b9\u0001\u0000\u0000" +
					"\u0000\u01b7\u01b5\u0001\u0000\u0000\u0000\u01b7\u01b8\u0001\u0000\u0000" +
					"\u0000\u01b8a\u0001\u0000\u0000\u0000\u01b9\u01b7\u0001\u0000\u0000\u0000" +
					"$gk\u007f\u0098\u009a\u00a2\u00a8\u00b3\u00bc\u00c1\u00d9\u00db\u00ff" +
					"\u0108\u010d\u0113\u011a\u0122\u012b\u0131\u0137\u0139\u0143\u014b\u014e" +
					"\u0153\u0160\u0165\u0168\u0178\u0195\u0199\u01a2\u01a7\u01ae\u01b7";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}