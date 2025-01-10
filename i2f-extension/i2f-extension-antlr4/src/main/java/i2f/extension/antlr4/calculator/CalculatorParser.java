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
            T__66 = 67, T__67 = 68, T__68 = 69, T__69 = 70, T__70 = 71, T__71 = 72, EQUAL = 73,
            DIGIT = 74, LETTER = 75, HEX_LETTER = 76, OTC_LETTER = 77, BIN_LETTER = 78, HIGH_LETTER = 79,
            TYPE_DEC_NUMBER = 80, TYPE_HEX_NUMBER = 81, TYPE_OTC_NUMBER = 82, TYPE_BIN_NUMBER = 83,
            TYPE_HIGN_NUMBER = 84, IDENTIFIER = 85, WS = 86, UNRECOGNIZED = 87;
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
                "'pi'", "'PI'", "'e'", "'E'", "'randf'", "'.'", "'='"
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
                null, "EQUAL", "DIGIT", "LETTER", "HEX_LETTER", "OTC_LETTER", "BIN_LETTER",
                "HIGH_LETTER", "TYPE_DEC_NUMBER", "TYPE_HEX_NUMBER", "TYPE_OTC_NUMBER",
                "TYPE_BIN_NUMBER", "TYPE_HIGN_NUMBER", "IDENTIFIER", "WS", "UNRECOGNIZED"
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

        public DecNumberContext decNumber() {
            return getRuleContext(DecNumberContext.class, 0);
        }

        public ConstNumberContext constNumber() {
            return getRuleContext(ConstNumberContext.class, 0);
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
            switch (_input.LA(1)) {
                case TYPE_HEX_NUMBER:
                    enterOuterAlt(_localctx, 1);
				{
                    setState(48);
                    hexNumber();
				}
				break;
                case TYPE_OTC_NUMBER:
                    enterOuterAlt(_localctx, 2);
				{
                    setState(49);
                    otcNumber();
				}
				break;
                case TYPE_BIN_NUMBER:
                    enterOuterAlt(_localctx, 3);
				{
                    setState(50);
                    binNumber();
				}
				break;
                case TYPE_HIGN_NUMBER:
                    enterOuterAlt(_localctx, 4);
				{
                    setState(51);
                    highNumber();
				}
				break;
                case DIGIT:
                case TYPE_DEC_NUMBER:
                    enterOuterAlt(_localctx, 5);
				{
                    setState(52);
                    decNumber();
				}
				break;
                case T__66:
                case T__67:
                case T__68:
                case T__69:
                case T__70:
                    enterOuterAlt(_localctx, 6);
				{
                    setState(53);
                    constNumber();
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
	public static class ExprContext extends ParserRuleContext {
        public NumberContext number() {
            return getRuleContext(NumberContext.class, 0);
        }
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
                    case T__66:
                    case T__67:
                    case T__68:
                    case T__69:
                    case T__70:
                    case DIGIT:
                    case TYPE_DEC_NUMBER:
                    case TYPE_HEX_NUMBER:
                    case TYPE_OTC_NUMBER:
                    case TYPE_BIN_NUMBER:
                    case TYPE_HIGN_NUMBER: {
                        setState(57);
                        number();
                    }
                    break;
                    case T__0: {
                        setState(58);
                        bracket();
                    }
                    break;
                    case IDENTIFIER: {
                        setState(59);
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
                        setState(60);
                        prefixOperator();
                        setState(61);
                        expr(7);
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
                                    if (!(precpred(_ctx, 6)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 6)");
                                    setState(66);
                                    operatorV5();
                                    setState(67);
                                    expr(7);
                                }
                                break;
                                case 2: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(69);
                                    if (!(precpred(_ctx, 5)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 5)");
                                    setState(70);
                                    operatorV4();
                                    setState(71);
                                    expr(6);
                                }
                                break;
                                case 3: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(73);
                                    if (!(precpred(_ctx, 4)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 4)");
                                    setState(74);
                                    operatorV3();
                                    setState(75);
                                    expr(5);
                                }
                                break;
                                case 4: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(77);
                                    if (!(precpred(_ctx, 3)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 3)");
                                    setState(78);
                                    operatorV2();
                                    setState(79);
                                    expr(4);
                                }
                                break;
                                case 5: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(81);
                                    if (!(precpred(_ctx, 2)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 2)");
                                    setState(82);
                                    operatorV1();
                                    setState(83);
                                    expr(3);
                                }
                                break;
                                case 6: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(85);
                                    if (!(precpred(_ctx, 1)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                                    setState(86);
                                    operatorV0();
                                    setState(87);
                                    expr(2);
                                }
                                break;
                                case 7: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(89);
                                    if (!(precpred(_ctx, 8)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 8)");
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
                case T__67:
                case T__68:
                case T__69:
                case T__70:
                case DIGIT:
                case TYPE_DEC_NUMBER:
                case TYPE_HEX_NUMBER:
                case TYPE_OTC_NUMBER:
                case TYPE_BIN_NUMBER:
                case TYPE_HIGN_NUMBER:
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
                case T__67:
                case T__68:
                case T__69:
                case T__70:
                case DIGIT:
                case TYPE_DEC_NUMBER:
                case TYPE_HEX_NUMBER:
                case TYPE_OTC_NUMBER:
                case TYPE_BIN_NUMBER:
                case TYPE_HIGN_NUMBER:
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
                case T__67:
                case T__68:
                case T__69:
                case T__70:
                case DIGIT:
                case TYPE_DEC_NUMBER:
                case TYPE_HEX_NUMBER:
                case TYPE_OTC_NUMBER:
                case TYPE_BIN_NUMBER:
                case TYPE_HIGN_NUMBER:
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
                if (!(((((_la - 67)) & ~0x3f) == 0 && ((1L << (_la - 67)) & 31L) != 0))) {
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
        public TerminalNode TYPE_DEC_NUMBER() {
            return getToken(CalculatorParser.TYPE_DEC_NUMBER, 0);
        }

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
		try {
			int _alt;
            setState(179);
			_errHandler.sync(this);
            switch (_input.LA(1)) {
                case TYPE_DEC_NUMBER:
                    enterOuterAlt(_localctx, 1);
				{
                    setState(165);
                    match(TYPE_DEC_NUMBER);
                }
                break;
                case DIGIT:
                    enterOuterAlt(_localctx, 2);
				{
                    setState(167);
                    _errHandler.sync(this);
                    _alt = 1;
                    do {
                        switch (_alt) {
                            case 1: {
                                {
                                    setState(166);
                                    match(DIGIT);
                                }
                            }
                            break;
                            default:
                                throw new NoViableAltException(this);
                        }
                        setState(169);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 11, _ctx);
                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                    setState(177);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 13, _ctx)) {
                        case 1: {
                            setState(171);
                            match(T__71);
                            setState(173);
					_errHandler.sync(this);
					_alt = 1;
					do {
						switch (_alt) {
                            case 1: {
                                {
                                    setState(172);
                                    match(DIGIT);
                                }
							}
							break;
                            default:
                                throw new NoViableAltException(this);
						}
                        setState(175);
						_errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 12, _ctx);
                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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
	public static class HexNumberContext extends ParserRuleContext {
        public TerminalNode TYPE_HEX_NUMBER() {
            return getToken(CalculatorParser.TYPE_HEX_NUMBER, 0);
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
			enterOuterAlt(_localctx, 1);
			{
                setState(181);
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
            return getToken(CalculatorParser.TYPE_OTC_NUMBER, 0);
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
			enterOuterAlt(_localctx, 1);
			{
                setState(183);
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
            return getToken(CalculatorParser.TYPE_BIN_NUMBER, 0);
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
			enterOuterAlt(_localctx, 1);
			{
                setState(185);
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

	@SuppressWarnings("CheckReturnValue")
	public static class HighNumberContext extends ParserRuleContext {
        public TerminalNode TYPE_HIGN_NUMBER() {
            return getToken(CalculatorParser.TYPE_HIGN_NUMBER, 0);
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
		try {
			enterOuterAlt(_localctx, 1);
			{
                setState(187);
                match(TYPE_HIGN_NUMBER);
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
                return precpred(_ctx, 6);
            case 1:
                return precpred(_ctx, 5);
            case 2:
                return precpred(_ctx, 4);
            case 3:
                return precpred(_ctx, 3);
            case 4:
                return precpred(_ctx, 2);
            case 5:
                return precpred(_ctx, 1);
            case 6:
                return precpred(_ctx, 8);
		}
		return true;
	}

	public static final String _serializedATN =
            "\u0004\u0001W\u00be\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002" +
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
                    "\b\f\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0004\u000e\u00a8\b\u000e" +
                    "\u000b\u000e\f\u000e\u00a9\u0001\u000e\u0001\u000e\u0004\u000e\u00ae\b" +
                    "\u000e\u000b\u000e\f\u000e\u00af\u0003\u000e\u00b2\b\u000e\u0003\u000e" +
                    "\u00b4\b\u000e\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0011" +
                    "\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0012\u0000\u0001\u0004\u0013" +
                    "\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a" +
                    "\u001c\u001e \"$\u0000\u0006\u0001\u0000\u0004\u0015\u0001\u0000\u0016" +
                    "\u0018\u0001\u0000\u0019\u001c\u0001\u0000(*\u0001\u000014\u0001\u0000" +
                    "CG\u00e1\u0000.\u0001\u0000\u0000\u0000\u00026\u0001\u0000\u0000\u0000" +
                    "\u0004?\u0001\u0000\u0000\u0000\u0006`\u0001\u0000\u0000\u0000\bd\u0001" +
                    "\u0000\u0000\u0000\np\u0001\u0000\u0000\u0000\fr\u0001\u0000\u0000\u0000" +
                    "\u000et\u0001\u0000\u0000\u0000\u0010\u0082\u0001\u0000\u0000\u0000\u0012" +
                    "\u0084\u0001\u0000\u0000\u0000\u0014\u008e\u0001\u0000\u0000\u0000\u0016" +
                    "\u0090\u0001\u0000\u0000\u0000\u0018\u00a1\u0001\u0000\u0000\u0000\u001a" +
                    "\u00a3\u0001\u0000\u0000\u0000\u001c\u00b3\u0001\u0000\u0000\u0000\u001e" +
                    "\u00b5\u0001\u0000\u0000\u0000 \u00b7\u0001\u0000\u0000\u0000\"\u00b9" +
                    "\u0001\u0000\u0000\u0000$\u00bb\u0001\u0000\u0000\u0000&(\u0003\u0002" +
                    "\u0001\u0000\')\u0005I\u0000\u0000(\'\u0001\u0000\u0000\u0000()\u0001" +
                    "\u0000\u0000\u0000)/\u0001\u0000\u0000\u0000*,\u0003\u0004\u0002\u0000" +
                    "+-\u0005I\u0000\u0000,+\u0001\u0000\u0000\u0000,-\u0001\u0000\u0000\u0000" +
                    "-/\u0001\u0000\u0000\u0000.&\u0001\u0000\u0000\u0000.*\u0001\u0000\u0000" +
                    "\u0000/\u0001\u0001\u0000\u0000\u000007\u0003\u001e\u000f\u000017\u0003" +
                    " \u0010\u000027\u0003\"\u0011\u000037\u0003$\u0012\u000047\u0003\u001c" +
                    "\u000e\u000057\u0003\u001a\r\u000060\u0001\u0000\u0000\u000061\u0001\u0000" +
                    "\u0000\u000062\u0001\u0000\u0000\u000063\u0001\u0000\u0000\u000064\u0001" +
                    "\u0000\u0000\u000065\u0001\u0000\u0000\u00007\u0003\u0001\u0000\u0000" +
                    "\u000089\u0006\u0002\uffff\uffff\u00009@\u0003\u0002\u0001\u0000:@\u0003" +
                    "\u0006\u0003\u0000;@\u0003\b\u0004\u0000<=\u0003\n\u0005\u0000=>\u0003" +
                    "\u0004\u0002\u0007>@\u0001\u0000\u0000\u0000?8\u0001\u0000\u0000\u0000" +
                    "?:\u0001\u0000\u0000\u0000?;\u0001\u0000\u0000\u0000?<\u0001\u0000\u0000" +
                    "\u0000@]\u0001\u0000\u0000\u0000AB\n\u0006\u0000\u0000BC\u0003\u000e\u0007" +
                    "\u0000CD\u0003\u0004\u0002\u0007D\\\u0001\u0000\u0000\u0000EF\n\u0005" +
                    "\u0000\u0000FG\u0003\u0010\b\u0000GH\u0003\u0004\u0002\u0006H\\\u0001" +
                    "\u0000\u0000\u0000IJ\n\u0004\u0000\u0000JK\u0003\u0012\t\u0000KL\u0003" +
                    "\u0004\u0002\u0005L\\\u0001\u0000\u0000\u0000MN\n\u0003\u0000\u0000NO" +
                    "\u0003\u0014\n\u0000OP\u0003\u0004\u0002\u0004P\\\u0001\u0000\u0000\u0000" +
                    "QR\n\u0002\u0000\u0000RS\u0003\u0016\u000b\u0000ST\u0003\u0004\u0002\u0003" +
                    "T\\\u0001\u0000\u0000\u0000UV\n\u0001\u0000\u0000VW\u0003\u0018\f\u0000" +
                    "WX\u0003\u0004\u0002\u0002X\\\u0001\u0000\u0000\u0000YZ\n\b\u0000\u0000" +
                    "Z\\\u0003\f\u0006\u0000[A\u0001\u0000\u0000\u0000[E\u0001\u0000\u0000" +
                    "\u0000[I\u0001\u0000\u0000\u0000[M\u0001\u0000\u0000\u0000[Q\u0001\u0000" +
                    "\u0000\u0000[U\u0001\u0000\u0000\u0000[Y\u0001\u0000\u0000\u0000\\_\u0001" +
                    "\u0000\u0000\u0000][\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000\u0000" +
                    "^\u0005\u0001\u0000\u0000\u0000_]\u0001\u0000\u0000\u0000`a\u0005\u0001" +
                    "\u0000\u0000ab\u0003\u0004\u0002\u0000bc\u0005\u0002\u0000\u0000c\u0007" +
                    "\u0001\u0000\u0000\u0000de\u0005U\u0000\u0000ef\u0005\u0001\u0000\u0000" +
                    "fk\u0003\u0004\u0002\u0000gh\u0005\u0003\u0000\u0000hj\u0003\u0004\u0002" +
                    "\u0000ig\u0001\u0000\u0000\u0000jm\u0001\u0000\u0000\u0000ki\u0001\u0000" +
                    "\u0000\u0000kl\u0001\u0000\u0000\u0000ln\u0001\u0000\u0000\u0000mk\u0001" +
                    "\u0000\u0000\u0000no\u0005\u0002\u0000\u0000o\t\u0001\u0000\u0000\u0000" +
                    "pq\u0007\u0000\u0000\u0000q\u000b\u0001\u0000\u0000\u0000rs\u0007\u0001" +
                    "\u0000\u0000s\r\u0001\u0000\u0000\u0000tu\u0007\u0002\u0000\u0000u\u000f" +
                    "\u0001\u0000\u0000\u0000v\u0083\u0001\u0000\u0000\u0000w\u0083\u0005\u001d" +
                    "\u0000\u0000x\u0083\u0005\u001e\u0000\u0000y\u0083\u0005\u001f\u0000\u0000" +
                    "z\u0083\u0005 \u0000\u0000{\u0083\u0005!\u0000\u0000|\u0083\u0005\"\u0000" +
                    "\u0000}\u0083\u0005#\u0000\u0000~\u0083\u0005$\u0000\u0000\u007f\u0083" +
                    "\u0005%\u0000\u0000\u0080\u0083\u0005&\u0000\u0000\u0081\u0083\u0005\'" +
                    "\u0000\u0000\u0082v\u0001\u0000\u0000\u0000\u0082w\u0001\u0000\u0000\u0000" +
                    "\u0082x\u0001\u0000\u0000\u0000\u0082y\u0001\u0000\u0000\u0000\u0082z" +
                    "\u0001\u0000\u0000\u0000\u0082{\u0001\u0000\u0000\u0000\u0082|\u0001\u0000" +
                    "\u0000\u0000\u0082}\u0001\u0000\u0000\u0000\u0082~\u0001\u0000\u0000\u0000" +
                    "\u0082\u007f\u0001\u0000\u0000\u0000\u0082\u0080\u0001\u0000\u0000\u0000" +
                    "\u0082\u0081\u0001\u0000\u0000\u0000\u0083\u0011\u0001\u0000\u0000\u0000" +
                    "\u0084\u0085\u0007\u0003\u0000\u0000\u0085\u0013\u0001\u0000\u0000\u0000" +
                    "\u0086\u008f\u0001\u0000\u0000\u0000\u0087\u008f\u0005+\u0000\u0000\u0088" +
                    "\u008f\u0005,\u0000\u0000\u0089\u008f\u0005-\u0000\u0000\u008a\u008f\u0005" +
                    "\u0017\u0000\u0000\u008b\u008f\u0005.\u0000\u0000\u008c\u008f\u0005/\u0000" +
                    "\u0000\u008d\u008f\u00050\u0000\u0000\u008e\u0086\u0001\u0000\u0000\u0000" +
                    "\u008e\u0087\u0001\u0000\u0000\u0000\u008e\u0088\u0001\u0000\u0000\u0000" +
                    "\u008e\u0089\u0001\u0000\u0000\u0000\u008e\u008a\u0001\u0000\u0000\u0000" +
                    "\u008e\u008b\u0001\u0000\u0000\u0000\u008e\u008c\u0001\u0000\u0000\u0000" +
                    "\u008e\u008d\u0001\u0000\u0000\u0000\u008f\u0015\u0001\u0000\u0000\u0000" +
                    "\u0090\u0091\u0007\u0004\u0000\u0000\u0091\u0017\u0001\u0000\u0000\u0000" +
                    "\u0092\u00a2\u0001\u0000\u0000\u0000\u0093\u00a2\u00055\u0000\u0000\u0094" +
                    "\u00a2\u00056\u0000\u0000\u0095\u00a2\u00057\u0000\u0000\u0096\u00a2\u0005" +
                    "8\u0000\u0000\u0097\u00a2\u00059\u0000\u0000\u0098\u00a2\u0005:\u0000" +
                    "\u0000\u0099\u00a2\u0005;\u0000\u0000\u009a\u00a2\u0005<\u0000\u0000\u009b" +
                    "\u00a2\u0005=\u0000\u0000\u009c\u00a2\u0005>\u0000\u0000\u009d\u00a2\u0005" +
                    "?\u0000\u0000\u009e\u00a2\u0005@\u0000\u0000\u009f\u00a2\u0005A\u0000" +
                    "\u0000\u00a0\u00a2\u0005B\u0000\u0000\u00a1\u0092\u0001\u0000\u0000\u0000" +
                    "\u00a1\u0093\u0001\u0000\u0000\u0000\u00a1\u0094\u0001\u0000\u0000\u0000" +
                    "\u00a1\u0095\u0001\u0000\u0000\u0000\u00a1\u0096\u0001\u0000\u0000\u0000" +
                    "\u00a1\u0097\u0001\u0000\u0000\u0000\u00a1\u0098\u0001\u0000\u0000\u0000" +
                    "\u00a1\u0099\u0001\u0000\u0000\u0000\u00a1\u009a\u0001\u0000\u0000\u0000" +
                    "\u00a1\u009b\u0001\u0000\u0000\u0000\u00a1\u009c\u0001\u0000\u0000\u0000" +
                    "\u00a1\u009d\u0001\u0000\u0000\u0000\u00a1\u009e\u0001\u0000\u0000\u0000" +
                    "\u00a1\u009f\u0001\u0000\u0000\u0000\u00a1\u00a0\u0001\u0000\u0000\u0000" +
                    "\u00a2\u0019\u0001\u0000\u0000\u0000\u00a3\u00a4\u0007\u0005\u0000\u0000" +
                    "\u00a4\u001b\u0001\u0000\u0000\u0000\u00a5\u00b4\u0005P\u0000\u0000\u00a6" +
                    "\u00a8\u0005J\u0000\u0000\u00a7\u00a6\u0001\u0000\u0000\u0000\u00a8\u00a9" +
                    "\u0001\u0000\u0000\u0000\u00a9\u00a7\u0001\u0000\u0000\u0000\u00a9\u00aa" +
                    "\u0001\u0000\u0000\u0000\u00aa\u00b1\u0001\u0000\u0000\u0000\u00ab\u00ad" +
                    "\u0005H\u0000\u0000\u00ac\u00ae\u0005J\u0000\u0000\u00ad\u00ac\u0001\u0000" +
                    "\u0000\u0000\u00ae\u00af\u0001\u0000\u0000\u0000\u00af\u00ad\u0001\u0000" +
                    "\u0000\u0000\u00af\u00b0\u0001\u0000\u0000\u0000\u00b0\u00b2\u0001\u0000" +
                    "\u0000\u0000\u00b1\u00ab\u0001\u0000\u0000\u0000\u00b1\u00b2\u0001\u0000" +
                    "\u0000\u0000\u00b2\u00b4\u0001\u0000\u0000\u0000\u00b3\u00a5\u0001\u0000" +
                    "\u0000\u0000\u00b3\u00a7\u0001\u0000\u0000\u0000\u00b4\u001d\u0001\u0000" +
                    "\u0000\u0000\u00b5\u00b6\u0005Q\u0000\u0000\u00b6\u001f\u0001\u0000\u0000" +
                    "\u0000\u00b7\u00b8\u0005R\u0000\u0000\u00b8!\u0001\u0000\u0000\u0000\u00b9" +
                    "\u00ba\u0005S\u0000\u0000\u00ba#\u0001\u0000\u0000\u0000\u00bb\u00bc\u0005" +
                    "T\u0000\u0000\u00bc%\u0001\u0000\u0000\u0000\u000f(,.6?[]k\u0082\u008e" +
                    "\u00a1\u00a9\u00af\u00b1\u00b3";
	public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}