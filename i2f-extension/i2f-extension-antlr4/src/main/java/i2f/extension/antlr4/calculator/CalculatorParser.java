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
			T__52 = 53, T__53 = 54, T__54 = 55, T__55 = 56, T__56 = 57, T__57 = 58, T__58 = 59,
			T__59 = 60, T__60 = 61, T__61 = 62, T__62 = 63, T__63 = 64, T__64 = 65, T__65 = 66,
			T__66 = 67, T__67 = 68, T__68 = 69, T__69 = 70, T__70 = 71, EQUAL = 72, DIGIT = 73,
			LETTER = 74, HEX_LETTER = 75, OTC_LETTER = 76, BIN_LETTER = 77, HIGH_LETTER = 78,
			CONST_PI = 79, CONST_E = 80, CONST_X = 81, CONST_T = 82, CONST_B = 83, CONST_H = 84,
			IDENTIFIER = 85, WS = 86, UNRECOGNIZED = 87;
	public static final int
			RULE_eval = 0, RULE_number = 1, RULE_expr = 2, RULE_bracket = 3, RULE_convertor = 4,
			RULE_prefixOperator = 5, RULE_suffixOperator = 6, RULE_operatorV5 = 7,
			RULE_operatorV4 = 8, RULE_operatorV3 = 9, RULE_operatorV2 = 10, RULE_operatorV1 = 11,
			RULE_operatorV0 = 12, RULE_constNumber = 13, RULE_decNumber = 14, RULE_hexNumber = 15,
			RULE_otcNumber = 16, RULE_binNumber = 17, RULE_highNumber = 18;
	private static String[] makeRuleNames() {
		return new String[]{
				"eval", "number", "expr", "bracket", "convertor", "prefixOperator", "suffixOperator",
				"operatorV5", "operatorV4", "operatorV3", "operatorV2", "operatorV1",
				"operatorV0", "constNumber", "decNumber", "hexNumber", "otcNumber", "binNumber",
				"highNumber"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[]{
				null, "'('", "')'", "','", "'~'", "'not'", "'abs'", "'neg'", "'ln'",
				"'sin'", "'cos'", "'tan'", "'arcsin'", "'arccos'", "'arctan'", "'angle'",
				"'radian'", "'floor'", "'round'", "'ceil'", "'rand'", "'feibo'", "'!'",
				"'%'", "'per'", "'**'", "'muls'", "'++'", "'adds'", "'>>>'", "'srmov'",
				"'&'", "'|'", "'xor'", "'<<'", "'>>'", "'and'", "'or'", "'lmov'", "'rmov'",
				"'log'", "'^'", "'pow'", "'//'", "'*'", "'/'", "'mul'", "'div'", "'mod'",
				"'+'", "'-'", "'add'", "'sub'", "'>='", "'<='", "'!='", "'<>'", "'=='",
				"'gte'", "'lte'", "'neq'", "'ne'", "'eq'", "'>'", "'<'", "'gt'", "'lt'",
				"'randf'", "'.'", "'e'", "'0'", "':'", "'='"
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
				null, null, null, null, null, null, null, null, null, null, null, null,
				"EQUAL", "DIGIT", "LETTER", "HEX_LETTER", "OTC_LETTER", "BIN_LETTER",
				"HIGH_LETTER", "CONST_PI", "CONST_E", "CONST_X", "CONST_T", "CONST_B",
				"CONST_H", "IDENTIFIER", "WS", "UNRECOGNIZED"
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

		public TerminalNode EQUAL() {
			return getToken(CalculatorParser.EQUAL, 0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class, 0);
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
		int _la;
		try {
			setState(46);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 2, _ctx)) {
				case 1:
					enterOuterAlt(_localctx, 1);
				{
					setState(38);
					number();
					setState(40);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la == EQUAL) {
						{
							setState(39);
							match(EQUAL);
						}
					}

				}
				break;
				case 2:
					enterOuterAlt(_localctx, 2);
				{
					setState(42);
					expr(0);
					setState(44);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la == EQUAL) {
						{
							setState(43);
							match(EQUAL);
						}
					}

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
		public ConstNumberContext constNumber() {
			return getRuleContext(ConstNumberContext.class, 0);
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
		public HighNumberContext highNumber() {
			return getRuleContext(HighNumberContext.class, 0);
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
		try {
			setState(54);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 3, _ctx)) {
				case 1:
					enterOuterAlt(_localctx, 1);
				{
					setState(48);
					constNumber();
				}
				break;
				case 2:
					enterOuterAlt(_localctx, 2);
				{
					setState(49);
					decNumber();
				}
				break;
				case 3:
					enterOuterAlt(_localctx, 3);
				{
					setState(50);
					hexNumber();
				}
				break;
				case 4:
					enterOuterAlt(_localctx, 4);
				{
					setState(51);
					otcNumber();
				}
				break;
				case 5:
					enterOuterAlt(_localctx, 5);
				{
					setState(52);
					binNumber();
				}
				break;
				case 6:
					enterOuterAlt(_localctx, 6);
				{
					setState(53);
					highNumber();
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
				setState(63);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
					case T__0: {
						setState(57);
						bracket();
					}
					break;
					case IDENTIFIER: {
						setState(58);
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
						setState(59);
						prefixOperator();
						setState(60);
						expr(8);
					}
					break;
					case T__66:
					case T__69:
					case DIGIT:
					case CONST_PI:
					case CONST_E: {
						setState(62);
						number();
					}
					break;
					default:
						throw new NoViableAltException(this);
				}
				_ctx.stop = _input.LT(-1);
				setState(93);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input, 6, _ctx);
				while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						if (_parseListeners != null) triggerExitRuleEvent();
						_prevctx = _localctx;
						{
							setState(91);
							_errHandler.sync(this);
							switch (getInterpreter().adaptivePredict(_input, 5, _ctx)) {
								case 1: {
									_localctx = new ExprContext(_parentctx, _parentState);
									pushNewRecursionContext(_localctx, _startState, RULE_expr);
									setState(65);
									if (!(precpred(_ctx, 7)))
										throw new FailedPredicateException(this, "precpred(_ctx, 7)");
									setState(66);
									operatorV5();
									setState(67);
									expr(8);
								}
								break;
								case 2: {
									_localctx = new ExprContext(_parentctx, _parentState);
									pushNewRecursionContext(_localctx, _startState, RULE_expr);
									setState(69);
									if (!(precpred(_ctx, 6)))
										throw new FailedPredicateException(this, "precpred(_ctx, 6)");
									setState(70);
									operatorV4();
									setState(71);
									expr(7);
								}
								break;
								case 3: {
									_localctx = new ExprContext(_parentctx, _parentState);
									pushNewRecursionContext(_localctx, _startState, RULE_expr);
									setState(73);
									if (!(precpred(_ctx, 5)))
										throw new FailedPredicateException(this, "precpred(_ctx, 5)");
									setState(74);
									operatorV3();
									setState(75);
									expr(6);
								}
								break;
								case 4: {
									_localctx = new ExprContext(_parentctx, _parentState);
									pushNewRecursionContext(_localctx, _startState, RULE_expr);
									setState(77);
									if (!(precpred(_ctx, 4)))
										throw new FailedPredicateException(this, "precpred(_ctx, 4)");
									setState(78);
									operatorV2();
									setState(79);
									expr(5);
								}
								break;
								case 5: {
									_localctx = new ExprContext(_parentctx, _parentState);
									pushNewRecursionContext(_localctx, _startState, RULE_expr);
									setState(81);
									if (!(precpred(_ctx, 3)))
										throw new FailedPredicateException(this, "precpred(_ctx, 3)");
									setState(82);
									operatorV1();
									setState(83);
									expr(4);
								}
								break;
								case 6: {
									_localctx = new ExprContext(_parentctx, _parentState);
									pushNewRecursionContext(_localctx, _startState, RULE_expr);
									setState(85);
									if (!(precpred(_ctx, 2)))
										throw new FailedPredicateException(this, "precpred(_ctx, 2)");
									setState(86);
									operatorV0();
									setState(87);
									expr(3);
								}
								break;
								case 7: {
									_localctx = new ExprContext(_parentctx, _parentState);
									pushNewRecursionContext(_localctx, _startState, RULE_expr);
									setState(89);
									if (!(precpred(_ctx, 9)))
										throw new FailedPredicateException(this, "precpred(_ctx, 9)");
									setState(90);
									suffixOperator();
								}
								break;
							}
						}
					}
					setState(95);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 6, _ctx);
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
		enterRule(_localctx, 6, RULE_bracket);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(96);
				match(T__0);
				setState(97);
				expr(0);
				setState(98);
				match(T__1);
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
		enterRule(_localctx, 8, RULE_convertor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(100);
				match(IDENTIFIER);
				setState(101);
				match(T__0);
				setState(102);
				expr(0);
				setState(107);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == T__2) {
					{
						{
							setState(103);
							match(T__2);
							setState(104);
							expr(0);
						}
					}
					setState(109);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(110);
				match(T__1);
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
				setState(112);
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
				setState(114);
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
				setState(116);
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
		try {
			setState(130);
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
				case T__66:
				case T__69:
				case DIGIT:
				case CONST_PI:
				case CONST_E:
				case IDENTIFIER:
					enterOuterAlt(_localctx, 1);
				{
				}
				break;
				case T__28:
					enterOuterAlt(_localctx, 2);
				{
					setState(119);
					match(T__28);
				}
				break;
				case T__29:
					enterOuterAlt(_localctx, 3);
				{
					setState(120);
					match(T__29);
				}
				break;
				case T__30:
					enterOuterAlt(_localctx, 4);
				{
					setState(121);
					match(T__30);
				}
				break;
				case T__31:
					enterOuterAlt(_localctx, 5);
				{
					setState(122);
					match(T__31);
				}
				break;
				case T__32:
					enterOuterAlt(_localctx, 6);
				{
					setState(123);
					match(T__32);
				}
				break;
				case T__33:
					enterOuterAlt(_localctx, 7);
				{
					setState(124);
					match(T__33);
				}
				break;
				case T__34:
					enterOuterAlt(_localctx, 8);
				{
					setState(125);
					match(T__34);
				}
				break;
				case T__35:
					enterOuterAlt(_localctx, 9);
				{
					setState(126);
					match(T__35);
				}
				break;
				case T__36:
					enterOuterAlt(_localctx, 10);
				{
					setState(127);
					match(T__36);
				}
				break;
				case T__37:
					enterOuterAlt(_localctx, 11);
				{
					setState(128);
					match(T__37);
				}
				break;
				case T__38:
					enterOuterAlt(_localctx, 12);
				{
					setState(129);
					match(T__38);
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
				setState(132);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 7696581394432L) != 0))) {
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
		try {
			setState(142);
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
				case T__66:
				case T__69:
				case DIGIT:
				case CONST_PI:
				case CONST_E:
				case IDENTIFIER:
					enterOuterAlt(_localctx, 1);
				{
				}
				break;
				case T__42:
					enterOuterAlt(_localctx, 2);
				{
					setState(135);
					match(T__42);
				}
				break;
				case T__43:
					enterOuterAlt(_localctx, 3);
				{
					setState(136);
					match(T__43);
				}
				break;
				case T__44:
					enterOuterAlt(_localctx, 4);
				{
					setState(137);
					match(T__44);
				}
				break;
				case T__22:
					enterOuterAlt(_localctx, 5);
				{
					setState(138);
					match(T__22);
				}
				break;
				case T__45:
					enterOuterAlt(_localctx, 6);
				{
					setState(139);
					match(T__45);
				}
				break;
				case T__46:
					enterOuterAlt(_localctx, 7);
				{
					setState(140);
					match(T__46);
				}
				break;
				case T__47:
					enterOuterAlt(_localctx, 8);
				{
					setState(141);
					match(T__47);
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
				setState(144);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & 8444249301319680L) != 0))) {
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
			setState(161);
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
				case T__66:
				case T__69:
				case DIGIT:
				case CONST_PI:
				case CONST_E:
				case IDENTIFIER:
					enterOuterAlt(_localctx, 1);
				{
				}
				break;
				case T__52:
					enterOuterAlt(_localctx, 2);
				{
					setState(147);
					match(T__52);
				}
				break;
				case T__53:
					enterOuterAlt(_localctx, 3);
				{
					setState(148);
					match(T__53);
				}
				break;
				case T__54:
					enterOuterAlt(_localctx, 4);
				{
					setState(149);
					match(T__54);
				}
				break;
				case T__55:
					enterOuterAlt(_localctx, 5);
				{
					setState(150);
					match(T__55);
				}
				break;
				case T__56:
					enterOuterAlt(_localctx, 6);
				{
					setState(151);
					match(T__56);
				}
				break;
				case T__57:
					enterOuterAlt(_localctx, 7);
				{
					setState(152);
					match(T__57);
				}
				break;
				case T__58:
					enterOuterAlt(_localctx, 8);
				{
					setState(153);
					match(T__58);
				}
				break;
				case T__59:
					enterOuterAlt(_localctx, 9);
				{
					setState(154);
					match(T__59);
				}
				break;
				case T__60:
					enterOuterAlt(_localctx, 10);
				{
					setState(155);
					match(T__60);
				}
				break;
				case T__61:
					enterOuterAlt(_localctx, 11);
				{
					setState(156);
					match(T__61);
				}
				break;
				case T__62:
					enterOuterAlt(_localctx, 12);
				{
					setState(157);
					match(T__62);
				}
				break;
				case T__63:
					enterOuterAlt(_localctx, 13);
				{
					setState(158);
					match(T__63);
				}
				break;
				case T__64:
					enterOuterAlt(_localctx, 14);
				{
					setState(159);
					match(T__64);
				}
				break;
				case T__65:
					enterOuterAlt(_localctx, 15);
				{
					setState(160);
					match(T__65);
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
	public static class ConstNumberContext extends ParserRuleContext {
		public TerminalNode CONST_PI() {
			return getToken(CalculatorParser.CONST_PI, 0);
		}

		public TerminalNode CONST_E() {
			return getToken(CalculatorParser.CONST_E, 0);
		}
		public ConstNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_constNumber;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterConstNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitConstNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof CalculatorVisitor)
				return ((CalculatorVisitor<? extends T>) visitor).visitConstNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstNumberContext constNumber() throws RecognitionException {
		ConstNumberContext _localctx = new ConstNumberContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_constNumber);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(163);
				_la = _input.LA(1);
				if (!(((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 12289L) != 0))) {
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
		public List<TerminalNode> DIGIT() {
			return getTokens(CalculatorParser.DIGIT);
		}
		public TerminalNode DIGIT(int i) {
			return getToken(CalculatorParser.DIGIT, i);
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
			if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterDecNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitDecNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof CalculatorVisitor)
				return ((CalculatorVisitor<? extends T>) visitor).visitDecNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecNumberContext decNumber() throws RecognitionException {
		DecNumberContext _localctx = new DecNumberContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_decNumber);
		int _la;
		try {
			int _alt;
			setState(208);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 18, _ctx)) {
				case 1:
					enterOuterAlt(_localctx, 1);
				{
					setState(166);
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
							case 1: {
								{
									setState(165);
									match(DIGIT);
								}
							}
							break;
							default:
								throw new NoViableAltException(this);
						}
						setState(168);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input, 11, _ctx);
					} while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
				}
				break;
				case 2:
					enterOuterAlt(_localctx, 2);
				{
					setState(171);
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
							{
								setState(170);
								match(DIGIT);
							}
						}
						setState(173);
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while (_la == DIGIT);
					setState(175);
					match(T__67);
					setState(177);
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
							case 1: {
								{
									setState(176);
									match(DIGIT);
								}
							}
							break;
							default:
								throw new NoViableAltException(this);
						}
						setState(179);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input, 13, _ctx);
					} while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
				}
				break;
				case 3:
					enterOuterAlt(_localctx, 3);
				{
					setState(182);
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
							{
								setState(181);
								match(DIGIT);
							}
						}
						setState(184);
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while (_la == DIGIT);
					setState(186);
					match(T__67);
					{
						setState(187);
						match(DIGIT);
					}
					setState(188);
					match(T__68);
					setState(190);
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
							case 1: {
								{
									setState(189);
									match(DIGIT);
								}
							}
							break;
							default:
								throw new NoViableAltException(this);
						}
						setState(192);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input, 15, _ctx);
					} while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
				}
				break;
				case 4:
					enterOuterAlt(_localctx, 4);
				{
					setState(195);
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
							{
								setState(194);
								match(DIGIT);
							}
						}
						setState(197);
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while (_la == DIGIT);
					setState(199);
					match(T__67);
					{
						setState(200);
						match(DIGIT);
					}
					setState(201);
					match(T__68);
					setState(202);
					match(T__49);
					setState(204);
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
							case 1: {
								{
									setState(203);
									match(DIGIT);
								}
							}
							break;
							default:
								throw new NoViableAltException(this);
						}
						setState(206);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input, 17, _ctx);
					} while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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
	public static class HexNumberContext extends ParserRuleContext {
		public TerminalNode CONST_X() {
			return getToken(CalculatorParser.CONST_X, 0);
		}

		public List<TerminalNode> HEX_LETTER() {
			return getTokens(CalculatorParser.HEX_LETTER);
		}
		public TerminalNode HEX_LETTER(int i) {
			return getToken(CalculatorParser.HEX_LETTER, i);
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
			if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterHexNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitHexNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof CalculatorVisitor)
				return ((CalculatorVisitor<? extends T>) visitor).visitHexNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HexNumberContext hexNumber() throws RecognitionException {
		HexNumberContext _localctx = new HexNumberContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_hexNumber);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(210);
				match(T__69);
				setState(211);
				match(CONST_X);
				setState(213);
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
						case 1: {
							{
								setState(212);
								match(HEX_LETTER);
							}
						}
						break;
						default:
							throw new NoViableAltException(this);
					}
					setState(215);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 19, _ctx);
				} while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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
		public TerminalNode CONST_T() {
			return getToken(CalculatorParser.CONST_T, 0);
		}

		public List<TerminalNode> OTC_LETTER() {
			return getTokens(CalculatorParser.OTC_LETTER);
		}
		public TerminalNode OTC_LETTER(int i) {
			return getToken(CalculatorParser.OTC_LETTER, i);
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
			if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterOtcNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitOtcNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof CalculatorVisitor)
				return ((CalculatorVisitor<? extends T>) visitor).visitOtcNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OtcNumberContext otcNumber() throws RecognitionException {
		OtcNumberContext _localctx = new OtcNumberContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_otcNumber);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(217);
				match(T__69);
				setState(218);
				match(CONST_T);
				setState(220);
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
						case 1: {
							{
								setState(219);
								match(OTC_LETTER);
							}
						}
						break;
						default:
							throw new NoViableAltException(this);
					}
					setState(222);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 20, _ctx);
				} while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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
		public TerminalNode CONST_B() {
			return getToken(CalculatorParser.CONST_B, 0);
		}

		public List<TerminalNode> BIN_LETTER() {
			return getTokens(CalculatorParser.BIN_LETTER);
		}
		public TerminalNode BIN_LETTER(int i) {
			return getToken(CalculatorParser.BIN_LETTER, i);
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
			if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterBinNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitBinNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof CalculatorVisitor)
				return ((CalculatorVisitor<? extends T>) visitor).visitBinNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BinNumberContext binNumber() throws RecognitionException {
		BinNumberContext _localctx = new BinNumberContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_binNumber);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(224);
				match(T__69);
				setState(225);
				match(CONST_B);
				setState(227);
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
						case 1: {
							{
								setState(226);
								match(BIN_LETTER);
							}
						}
						break;
						default:
							throw new NoViableAltException(this);
					}
					setState(229);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 21, _ctx);
				} while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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
	public static class HighNumberContext extends ParserRuleContext {
		public TerminalNode CONST_H() {
			return getToken(CalculatorParser.CONST_H, 0);
		}

		public List<TerminalNode> DIGIT() {
			return getTokens(CalculatorParser.DIGIT);
		}
		public TerminalNode DIGIT(int i) {
			return getToken(CalculatorParser.DIGIT, i);
		}

		public List<TerminalNode> HIGH_LETTER() {
			return getTokens(CalculatorParser.HIGH_LETTER);
		}
		public TerminalNode HIGH_LETTER(int i) {
			return getToken(CalculatorParser.HIGH_LETTER, i);
		}
		public HighNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_highNumber;
		}
		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof CalculatorListener) ((CalculatorListener) listener).enterHighNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof CalculatorListener) ((CalculatorListener) listener).exitHighNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof CalculatorVisitor)
				return ((CalculatorVisitor<? extends T>) visitor).visitHighNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HighNumberContext highNumber() throws RecognitionException {
		HighNumberContext _localctx = new HighNumberContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_highNumber);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(231);
				match(T__69);
				setState(232);
				match(CONST_H);
				setState(234);
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
						{
							setState(233);
							match(DIGIT);
						}
					}
					setState(236);
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while (_la == DIGIT);
				setState(238);
				match(T__70);
				setState(240);
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
						case 1: {
							{
								setState(239);
								match(HIGH_LETTER);
							}
						}
						break;
						default:
							throw new NoViableAltException(this);
					}
					setState(242);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 23, _ctx);
				} while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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
			"\u0004\u0001W\u00f5\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002" +
					"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002" +
					"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002" +
					"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002" +
					"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f" +
					"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012" +
					"\u0001\u0000\u0001\u0000\u0003\u0000)\b\u0000\u0001\u0000\u0001\u0000" +
					"\u0003\u0000-\b\u0000\u0003\u0000/\b\u0000\u0001\u0001\u0001\u0001\u0001" +
					"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u00017\b\u0001\u0001" +
					"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001" +
					"\u0002\u0003\u0002@\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001" +
					"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001" +
					"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001" +
					"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001" +
					"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\\\b" +
					"\u0002\n\u0002\f\u0002_\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001" +
					"\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005" +
					"\u0004j\b\u0004\n\u0004\f\u0004m\t\u0004\u0001\u0004\u0001\u0004\u0001" +
					"\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001" +
					"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001" +
					"\b\u0001\b\u0001\b\u0003\b\u0083\b\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001" +
					"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n\u008f\b\n\u0001\u000b" +
					"\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001" +
					"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u00a2" +
					"\b\f\u0001\r\u0001\r\u0001\u000e\u0004\u000e\u00a7\b\u000e\u000b\u000e" +
					"\f\u000e\u00a8\u0001\u000e\u0004\u000e\u00ac\b\u000e\u000b\u000e\f\u000e" +
					"\u00ad\u0001\u000e\u0001\u000e\u0004\u000e\u00b2\b\u000e\u000b\u000e\f" +
					"\u000e\u00b3\u0001\u000e\u0004\u000e\u00b7\b\u000e\u000b\u000e\f\u000e" +
					"\u00b8\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0004\u000e\u00bf" +
					"\b\u000e\u000b\u000e\f\u000e\u00c0\u0001\u000e\u0004\u000e\u00c4\b\u000e" +
					"\u000b\u000e\f\u000e\u00c5\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e" +
					"\u0001\u000e\u0004\u000e\u00cd\b\u000e\u000b\u000e\f\u000e\u00ce\u0003" +
					"\u000e\u00d1\b\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0004\u000f\u00d6" +
					"\b\u000f\u000b\u000f\f\u000f\u00d7\u0001\u0010\u0001\u0010\u0001\u0010" +
					"\u0004\u0010\u00dd\b\u0010\u000b\u0010\f\u0010\u00de\u0001\u0011\u0001" +
					"\u0011\u0001\u0011\u0004\u0011\u00e4\b\u0011\u000b\u0011\f\u0011\u00e5" +
					"\u0001\u0012\u0001\u0012\u0001\u0012\u0004\u0012\u00eb\b\u0012\u000b\u0012" +
					"\f\u0012\u00ec\u0001\u0012\u0001\u0012\u0004\u0012\u00f1\b\u0012\u000b" +
					"\u0012\f\u0012\u00f2\u0001\u0012\u0000\u0001\u0004\u0013\u0000\u0002\u0004" +
					"\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"" +
					"$\u0000\u0006\u0001\u0000\u0004\u0015\u0001\u0000\u0016\u0018\u0001\u0000" +
					"\u0019\u001c\u0001\u0000(*\u0001\u000014\u0002\u0000CCOP\u0123\u0000." +
					"\u0001\u0000\u0000\u0000\u00026\u0001\u0000\u0000\u0000\u0004?\u0001\u0000" +
					"\u0000\u0000\u0006`\u0001\u0000\u0000\u0000\bd\u0001\u0000\u0000\u0000" +
					"\np\u0001\u0000\u0000\u0000\fr\u0001\u0000\u0000\u0000\u000et\u0001\u0000" +
					"\u0000\u0000\u0010\u0082\u0001\u0000\u0000\u0000\u0012\u0084\u0001\u0000" +
					"\u0000\u0000\u0014\u008e\u0001\u0000\u0000\u0000\u0016\u0090\u0001\u0000" +
					"\u0000\u0000\u0018\u00a1\u0001\u0000\u0000\u0000\u001a\u00a3\u0001\u0000" +
					"\u0000\u0000\u001c\u00d0\u0001\u0000\u0000\u0000\u001e\u00d2\u0001\u0000" +
					"\u0000\u0000 \u00d9\u0001\u0000\u0000\u0000\"\u00e0\u0001\u0000\u0000" +
					"\u0000$\u00e7\u0001\u0000\u0000\u0000&(\u0003\u0002\u0001\u0000\')\u0005" +
					"H\u0000\u0000(\'\u0001\u0000\u0000\u0000()\u0001\u0000\u0000\u0000)/\u0001" +
					"\u0000\u0000\u0000*,\u0003\u0004\u0002\u0000+-\u0005H\u0000\u0000,+\u0001" +
					"\u0000\u0000\u0000,-\u0001\u0000\u0000\u0000-/\u0001\u0000\u0000\u0000" +
					".&\u0001\u0000\u0000\u0000.*\u0001\u0000\u0000\u0000/\u0001\u0001\u0000" +
					"\u0000\u000007\u0003\u001a\r\u000017\u0003\u001c\u000e\u000027\u0003\u001e" +
					"\u000f\u000037\u0003 \u0010\u000047\u0003\"\u0011\u000057\u0003$\u0012" +
					"\u000060\u0001\u0000\u0000\u000061\u0001\u0000\u0000\u000062\u0001\u0000" +
					"\u0000\u000063\u0001\u0000\u0000\u000064\u0001\u0000\u0000\u000065\u0001" +
					"\u0000\u0000\u00007\u0003\u0001\u0000\u0000\u000089\u0006\u0002\uffff" +
					"\uffff\u00009@\u0003\u0006\u0003\u0000:@\u0003\b\u0004\u0000;<\u0003\n" +
					"\u0005\u0000<=\u0003\u0004\u0002\b=@\u0001\u0000\u0000\u0000>@\u0003\u0002" +
					"\u0001\u0000?8\u0001\u0000\u0000\u0000?:\u0001\u0000\u0000\u0000?;\u0001" +
					"\u0000\u0000\u0000?>\u0001\u0000\u0000\u0000@]\u0001\u0000\u0000\u0000" +
					"AB\n\u0007\u0000\u0000BC\u0003\u000e\u0007\u0000CD\u0003\u0004\u0002\b" +
					"D\\\u0001\u0000\u0000\u0000EF\n\u0006\u0000\u0000FG\u0003\u0010\b\u0000" +
					"GH\u0003\u0004\u0002\u0007H\\\u0001\u0000\u0000\u0000IJ\n\u0005\u0000" +
					"\u0000JK\u0003\u0012\t\u0000KL\u0003\u0004\u0002\u0006L\\\u0001\u0000" +
					"\u0000\u0000MN\n\u0004\u0000\u0000NO\u0003\u0014\n\u0000OP\u0003\u0004" +
					"\u0002\u0005P\\\u0001\u0000\u0000\u0000QR\n\u0003\u0000\u0000RS\u0003" +
					"\u0016\u000b\u0000ST\u0003\u0004\u0002\u0004T\\\u0001\u0000\u0000\u0000" +
					"UV\n\u0002\u0000\u0000VW\u0003\u0018\f\u0000WX\u0003\u0004\u0002\u0003" +
					"X\\\u0001\u0000\u0000\u0000YZ\n\t\u0000\u0000Z\\\u0003\f\u0006\u0000[" +
					"A\u0001\u0000\u0000\u0000[E\u0001\u0000\u0000\u0000[I\u0001\u0000\u0000" +
					"\u0000[M\u0001\u0000\u0000\u0000[Q\u0001\u0000\u0000\u0000[U\u0001\u0000" +
					"\u0000\u0000[Y\u0001\u0000\u0000\u0000\\_\u0001\u0000\u0000\u0000][\u0001" +
					"\u0000\u0000\u0000]^\u0001\u0000\u0000\u0000^\u0005\u0001\u0000\u0000" +
					"\u0000_]\u0001\u0000\u0000\u0000`a\u0005\u0001\u0000\u0000ab\u0003\u0004" +
					"\u0002\u0000bc\u0005\u0002\u0000\u0000c\u0007\u0001\u0000\u0000\u0000" +
					"de\u0005U\u0000\u0000ef\u0005\u0001\u0000\u0000fk\u0003\u0004\u0002\u0000" +
					"gh\u0005\u0003\u0000\u0000hj\u0003\u0004\u0002\u0000ig\u0001\u0000\u0000" +
					"\u0000jm\u0001\u0000\u0000\u0000ki\u0001\u0000\u0000\u0000kl\u0001\u0000" +
					"\u0000\u0000ln\u0001\u0000\u0000\u0000mk\u0001\u0000\u0000\u0000no\u0005" +
					"\u0002\u0000\u0000o\t\u0001\u0000\u0000\u0000pq\u0007\u0000\u0000\u0000" +
					"q\u000b\u0001\u0000\u0000\u0000rs\u0007\u0001\u0000\u0000s\r\u0001\u0000" +
					"\u0000\u0000tu\u0007\u0002\u0000\u0000u\u000f\u0001\u0000\u0000\u0000" +
					"v\u0083\u0001\u0000\u0000\u0000w\u0083\u0005\u001d\u0000\u0000x\u0083" +
					"\u0005\u001e\u0000\u0000y\u0083\u0005\u001f\u0000\u0000z\u0083\u0005 " +
					"\u0000\u0000{\u0083\u0005!\u0000\u0000|\u0083\u0005\"\u0000\u0000}\u0083" +
					"\u0005#\u0000\u0000~\u0083\u0005$\u0000\u0000\u007f\u0083\u0005%\u0000" +
					"\u0000\u0080\u0083\u0005&\u0000\u0000\u0081\u0083\u0005\'\u0000\u0000" +
					"\u0082v\u0001\u0000\u0000\u0000\u0082w\u0001\u0000\u0000\u0000\u0082x" +
					"\u0001\u0000\u0000\u0000\u0082y\u0001\u0000\u0000\u0000\u0082z\u0001\u0000" +
					"\u0000\u0000\u0082{\u0001\u0000\u0000\u0000\u0082|\u0001\u0000\u0000\u0000" +
					"\u0082}\u0001\u0000\u0000\u0000\u0082~\u0001\u0000\u0000\u0000\u0082\u007f" +
					"\u0001\u0000\u0000\u0000\u0082\u0080\u0001\u0000\u0000\u0000\u0082\u0081" +
					"\u0001\u0000\u0000\u0000\u0083\u0011\u0001\u0000\u0000\u0000\u0084\u0085" +
					"\u0007\u0003\u0000\u0000\u0085\u0013\u0001\u0000\u0000\u0000\u0086\u008f" +
					"\u0001\u0000\u0000\u0000\u0087\u008f\u0005+\u0000\u0000\u0088\u008f\u0005" +
					",\u0000\u0000\u0089\u008f\u0005-\u0000\u0000\u008a\u008f\u0005\u0017\u0000" +
					"\u0000\u008b\u008f\u0005.\u0000\u0000\u008c\u008f\u0005/\u0000\u0000\u008d" +
					"\u008f\u00050\u0000\u0000\u008e\u0086\u0001\u0000\u0000\u0000\u008e\u0087" +
					"\u0001\u0000\u0000\u0000\u008e\u0088\u0001\u0000\u0000\u0000\u008e\u0089" +
					"\u0001\u0000\u0000\u0000\u008e\u008a\u0001\u0000\u0000\u0000\u008e\u008b" +
					"\u0001\u0000\u0000\u0000\u008e\u008c\u0001\u0000\u0000\u0000\u008e\u008d" +
					"\u0001\u0000\u0000\u0000\u008f\u0015\u0001\u0000\u0000\u0000\u0090\u0091" +
					"\u0007\u0004\u0000\u0000\u0091\u0017\u0001\u0000\u0000\u0000\u0092\u00a2" +
					"\u0001\u0000\u0000\u0000\u0093\u00a2\u00055\u0000\u0000\u0094\u00a2\u0005" +
					"6\u0000\u0000\u0095\u00a2\u00057\u0000\u0000\u0096\u00a2\u00058\u0000" +
					"\u0000\u0097\u00a2\u00059\u0000\u0000\u0098\u00a2\u0005:\u0000\u0000\u0099" +
					"\u00a2\u0005;\u0000\u0000\u009a\u00a2\u0005<\u0000\u0000\u009b\u00a2\u0005" +
					"=\u0000\u0000\u009c\u00a2\u0005>\u0000\u0000\u009d\u00a2\u0005?\u0000" +
					"\u0000\u009e\u00a2\u0005@\u0000\u0000\u009f\u00a2\u0005A\u0000\u0000\u00a0" +
					"\u00a2\u0005B\u0000\u0000\u00a1\u0092\u0001\u0000\u0000\u0000\u00a1\u0093" +
					"\u0001\u0000\u0000\u0000\u00a1\u0094\u0001\u0000\u0000\u0000\u00a1\u0095" +
					"\u0001\u0000\u0000\u0000\u00a1\u0096\u0001\u0000\u0000\u0000\u00a1\u0097" +
					"\u0001\u0000\u0000\u0000\u00a1\u0098\u0001\u0000\u0000\u0000\u00a1\u0099" +
					"\u0001\u0000\u0000\u0000\u00a1\u009a\u0001\u0000\u0000\u0000\u00a1\u009b" +
					"\u0001\u0000\u0000\u0000\u00a1\u009c\u0001\u0000\u0000\u0000\u00a1\u009d" +
					"\u0001\u0000\u0000\u0000\u00a1\u009e\u0001\u0000\u0000\u0000\u00a1\u009f" +
					"\u0001\u0000\u0000\u0000\u00a1\u00a0\u0001\u0000\u0000\u0000\u00a2\u0019" +
					"\u0001\u0000\u0000\u0000\u00a3\u00a4\u0007\u0005\u0000\u0000\u00a4\u001b" +
					"\u0001\u0000\u0000\u0000\u00a5\u00a7\u0005I\u0000\u0000\u00a6\u00a5\u0001" +
					"\u0000\u0000\u0000\u00a7\u00a8\u0001\u0000\u0000\u0000\u00a8\u00a6\u0001" +
					"\u0000\u0000\u0000\u00a8\u00a9\u0001\u0000\u0000\u0000\u00a9\u00d1\u0001" +
					"\u0000\u0000\u0000\u00aa\u00ac\u0005I\u0000\u0000\u00ab\u00aa\u0001\u0000" +
					"\u0000\u0000\u00ac\u00ad\u0001\u0000\u0000\u0000\u00ad\u00ab\u0001\u0000" +
					"\u0000\u0000\u00ad\u00ae\u0001\u0000\u0000\u0000\u00ae\u00af\u0001\u0000" +
					"\u0000\u0000\u00af\u00b1\u0005D\u0000\u0000\u00b0\u00b2\u0005I\u0000\u0000" +
					"\u00b1\u00b0\u0001\u0000\u0000\u0000\u00b2\u00b3\u0001\u0000\u0000\u0000" +
					"\u00b3\u00b1\u0001\u0000\u0000\u0000\u00b3\u00b4\u0001\u0000\u0000\u0000" +
					"\u00b4\u00d1\u0001\u0000\u0000\u0000\u00b5\u00b7\u0005I\u0000\u0000\u00b6" +
					"\u00b5\u0001\u0000\u0000\u0000\u00b7\u00b8\u0001\u0000\u0000\u0000\u00b8" +
					"\u00b6\u0001\u0000\u0000\u0000\u00b8\u00b9\u0001\u0000\u0000\u0000\u00b9" +
					"\u00ba\u0001\u0000\u0000\u0000\u00ba\u00bb\u0005D\u0000\u0000\u00bb\u00bc" +
					"\u0005I\u0000\u0000\u00bc\u00be\u0005E\u0000\u0000\u00bd\u00bf\u0005I" +
					"\u0000\u0000\u00be\u00bd\u0001\u0000\u0000\u0000\u00bf\u00c0\u0001\u0000" +
					"\u0000\u0000\u00c0\u00be\u0001\u0000\u0000\u0000\u00c0\u00c1\u0001\u0000" +
					"\u0000\u0000\u00c1\u00d1\u0001\u0000\u0000\u0000\u00c2\u00c4\u0005I\u0000" +
					"\u0000\u00c3\u00c2\u0001\u0000\u0000\u0000\u00c4\u00c5\u0001\u0000\u0000" +
					"\u0000\u00c5\u00c3\u0001\u0000\u0000\u0000\u00c5\u00c6\u0001\u0000\u0000" +
					"\u0000\u00c6\u00c7\u0001\u0000\u0000\u0000\u00c7\u00c8\u0005D\u0000\u0000" +
					"\u00c8\u00c9\u0005I\u0000\u0000\u00c9\u00ca\u0005E\u0000\u0000\u00ca\u00cc" +
					"\u00052\u0000\u0000\u00cb\u00cd\u0005I\u0000\u0000\u00cc\u00cb\u0001\u0000" +
					"\u0000\u0000\u00cd\u00ce\u0001\u0000\u0000\u0000\u00ce\u00cc\u0001\u0000" +
					"\u0000\u0000\u00ce\u00cf\u0001\u0000\u0000\u0000\u00cf\u00d1\u0001\u0000" +
					"\u0000\u0000\u00d0\u00a6\u0001\u0000\u0000\u0000\u00d0\u00ab\u0001\u0000" +
					"\u0000\u0000\u00d0\u00b6\u0001\u0000\u0000\u0000\u00d0\u00c3\u0001\u0000" +
					"\u0000\u0000\u00d1\u001d\u0001\u0000\u0000\u0000\u00d2\u00d3\u0005F\u0000" +
					"\u0000\u00d3\u00d5\u0005Q\u0000\u0000\u00d4\u00d6\u0005K\u0000\u0000\u00d5" +
					"\u00d4\u0001\u0000\u0000\u0000\u00d6\u00d7\u0001\u0000\u0000\u0000\u00d7" +
					"\u00d5\u0001\u0000\u0000\u0000\u00d7\u00d8\u0001\u0000\u0000\u0000\u00d8" +
					"\u001f\u0001\u0000\u0000\u0000\u00d9\u00da\u0005F\u0000\u0000\u00da\u00dc" +
					"\u0005R\u0000\u0000\u00db\u00dd\u0005L\u0000\u0000\u00dc\u00db\u0001\u0000" +
					"\u0000\u0000\u00dd\u00de\u0001\u0000\u0000\u0000\u00de\u00dc\u0001\u0000" +
					"\u0000\u0000\u00de\u00df\u0001\u0000\u0000\u0000\u00df!\u0001\u0000\u0000" +
					"\u0000\u00e0\u00e1\u0005F\u0000\u0000\u00e1\u00e3\u0005S\u0000\u0000\u00e2" +
					"\u00e4\u0005M\u0000\u0000\u00e3\u00e2\u0001\u0000\u0000\u0000\u00e4\u00e5" +
					"\u0001\u0000\u0000\u0000\u00e5\u00e3\u0001\u0000\u0000\u0000\u00e5\u00e6" +
					"\u0001\u0000\u0000\u0000\u00e6#\u0001\u0000\u0000\u0000\u00e7\u00e8\u0005" +
					"F\u0000\u0000\u00e8\u00ea\u0005T\u0000\u0000\u00e9\u00eb\u0005I\u0000" +
					"\u0000\u00ea\u00e9\u0001\u0000\u0000\u0000\u00eb\u00ec\u0001\u0000\u0000" +
					"\u0000\u00ec\u00ea\u0001\u0000\u0000\u0000\u00ec\u00ed\u0001\u0000\u0000" +
					"\u0000\u00ed\u00ee\u0001\u0000\u0000\u0000\u00ee\u00f0\u0005G\u0000\u0000" +
					"\u00ef\u00f1\u0005N\u0000\u0000\u00f0\u00ef\u0001\u0000\u0000\u0000\u00f1" +
					"\u00f2\u0001\u0000\u0000\u0000\u00f2\u00f0\u0001\u0000\u0000\u0000\u00f2" +
					"\u00f3\u0001\u0000\u0000\u0000\u00f3%\u0001\u0000\u0000\u0000\u0018(," +
					".6?[]k\u0082\u008e\u00a1\u00a8\u00ad\u00b3\u00b8\u00c0\u00c5\u00ce\u00d0" +
					"\u00d7\u00de\u00e5\u00ec\u00f2";
	public static final ATN _ATN =
			new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}