// Generated from /extension/antlr4/tpl/rule/Tpl.g4 by ANTLR 4.13.2

    package i2f.extension.antlr4.tpl.grammar;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class TplParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		REF_VALUE=1, BLOCK_END=2, BLOCK_ELSE=3, BLOCK_IF=4, BLOCK_HEAD=5, IDENTIFIER=6, 
		WHITESPACE=7, PAREN_L=8, PAREN_R=9, COMMA=10, SHARP=11, DOLLAR=12, EXCLAIM=13, 
		COLON=14, TEXT=15;
	public static final int
		RULE_root = 0, RULE_segment = 1, RULE_block = 2, RULE_commonBlock = 3, 
		RULE_ifBlock = 4, RULE_elseBlock = 5, RULE_blockHead = 6, RULE_blockBody = 7, 
		RULE_parameters = 8, RULE_parameter = 9, RULE_value = 10, RULE_text = 11, 
		RULE_content = 12;
	private static String[] makeRuleNames() {
		return new String[] {
			"root", "segment", "block", "commonBlock", "ifBlock", "elseBlock", "blockHead", 
			"blockBody", "parameters", "parameter", "value", "text", "content"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "'('", "')'", "','", 
			"'#'", "'$'", "'!'", "':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "REF_VALUE", "BLOCK_END", "BLOCK_ELSE", "BLOCK_IF", "BLOCK_HEAD", 
			"IDENTIFIER", "WHITESPACE", "PAREN_L", "PAREN_R", "COMMA", "SHARP", "DOLLAR", 
			"EXCLAIM", "COLON", "TEXT"
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
	public String getGrammarFileName() { return "Tpl.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TplParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RootContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(TplParser.EOF, 0); }
		public List<SegmentContext> segment() {
			return getRuleContexts(SegmentContext.class);
		}
		public SegmentContext segment(int i) {
			return getRuleContext(SegmentContext.class,i);
		}
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).exitRoot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TplVisitor ) return ((TplVisitor<? extends T>)visitor).visitRoot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 65522L) != 0)) {
				{
				{
				setState(26);
				segment();
				}
				}
				setState(31);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(32);
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
	public static class SegmentContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TextContext text() {
			return getRuleContext(TextContext.class,0);
		}
		public SegmentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_segment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).enterSegment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).exitSegment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TplVisitor ) return ((TplVisitor<? extends T>)visitor).visitSegment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SegmentContext segment() throws RecognitionException {
		SegmentContext _localctx = new SegmentContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_segment);
		try {
			setState(37);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BLOCK_IF:
			case BLOCK_HEAD:
				enterOuterAlt(_localctx, 1);
				{
				setState(34);
				block();
				}
				break;
			case REF_VALUE:
				enterOuterAlt(_localctx, 2);
				{
				setState(35);
				value();
				}
				break;
			case IDENTIFIER:
			case WHITESPACE:
			case PAREN_L:
			case PAREN_R:
			case COMMA:
			case SHARP:
			case DOLLAR:
			case EXCLAIM:
			case COLON:
			case TEXT:
				enterOuterAlt(_localctx, 3);
				{
				setState(36);
				text();
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
	public static class BlockContext extends ParserRuleContext {
		public IfBlockContext ifBlock() {
			return getRuleContext(IfBlockContext.class,0);
		}
		public CommonBlockContext commonBlock() {
			return getRuleContext(CommonBlockContext.class,0);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TplVisitor ) return ((TplVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_block);
		try {
			setState(41);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BLOCK_IF:
				enterOuterAlt(_localctx, 1);
				{
				setState(39);
				ifBlock();
				}
				break;
			case BLOCK_HEAD:
				enterOuterAlt(_localctx, 2);
				{
				setState(40);
				commonBlock();
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
	public static class CommonBlockContext extends ParserRuleContext {
		public BlockHeadContext blockHead() {
			return getRuleContext(BlockHeadContext.class,0);
		}
		public TerminalNode PAREN_L() { return getToken(TplParser.PAREN_L, 0); }
		public TerminalNode PAREN_R() { return getToken(TplParser.PAREN_R, 0); }
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public BlockBodyContext blockBody() {
			return getRuleContext(BlockBodyContext.class,0);
		}
		public TerminalNode BLOCK_END() { return getToken(TplParser.BLOCK_END, 0); }
		public CommonBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_commonBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).enterCommonBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).exitCommonBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TplVisitor ) return ((TplVisitor<? extends T>)visitor).visitCommonBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CommonBlockContext commonBlock() throws RecognitionException {
		CommonBlockContext _localctx = new CommonBlockContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_commonBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			blockHead();
			setState(44);
			match(PAREN_L);
			setState(46);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==REF_VALUE || _la==IDENTIFIER) {
				{
				setState(45);
				parameters();
				}
			}

			setState(48);
			match(PAREN_R);
			setState(52);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(49);
				blockBody();
				setState(50);
				match(BLOCK_END);
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
	public static class IfBlockContext extends ParserRuleContext {
		public TerminalNode BLOCK_IF() { return getToken(TplParser.BLOCK_IF, 0); }
		public TerminalNode PAREN_L() { return getToken(TplParser.PAREN_L, 0); }
		public TerminalNode PAREN_R() { return getToken(TplParser.PAREN_R, 0); }
		public BlockBodyContext blockBody() {
			return getRuleContext(BlockBodyContext.class,0);
		}
		public TerminalNode BLOCK_END() { return getToken(TplParser.BLOCK_END, 0); }
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public List<ElseBlockContext> elseBlock() {
			return getRuleContexts(ElseBlockContext.class);
		}
		public ElseBlockContext elseBlock(int i) {
			return getRuleContext(ElseBlockContext.class,i);
		}
		public IfBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).enterIfBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).exitIfBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TplVisitor ) return ((TplVisitor<? extends T>)visitor).visitIfBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfBlockContext ifBlock() throws RecognitionException {
		IfBlockContext _localctx = new IfBlockContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_ifBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54);
			match(BLOCK_IF);
			setState(55);
			match(PAREN_L);
			setState(57);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==REF_VALUE || _la==IDENTIFIER) {
				{
				setState(56);
				parameters();
				}
			}

			setState(59);
			match(PAREN_R);
			setState(60);
			blockBody();
			setState(64);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BLOCK_ELSE) {
				{
				{
				setState(61);
				elseBlock();
				}
				}
				setState(66);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(67);
			match(BLOCK_END);
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
	public static class ElseBlockContext extends ParserRuleContext {
		public TerminalNode BLOCK_ELSE() { return getToken(TplParser.BLOCK_ELSE, 0); }
		public TerminalNode PAREN_L() { return getToken(TplParser.PAREN_L, 0); }
		public TerminalNode PAREN_R() { return getToken(TplParser.PAREN_R, 0); }
		public BlockBodyContext blockBody() {
			return getRuleContext(BlockBodyContext.class,0);
		}
		public ParametersContext parameters() {
			return getRuleContext(ParametersContext.class,0);
		}
		public ElseBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).enterElseBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).exitElseBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TplVisitor ) return ((TplVisitor<? extends T>)visitor).visitElseBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseBlockContext elseBlock() throws RecognitionException {
		ElseBlockContext _localctx = new ElseBlockContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_elseBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
			match(BLOCK_ELSE);
			setState(70);
			match(PAREN_L);
			setState(72);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==REF_VALUE || _la==IDENTIFIER) {
				{
				setState(71);
				parameters();
				}
			}

			setState(74);
			match(PAREN_R);
			setState(75);
			blockBody();
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
	public static class BlockHeadContext extends ParserRuleContext {
		public TerminalNode BLOCK_HEAD() { return getToken(TplParser.BLOCK_HEAD, 0); }
		public BlockHeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockHead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).enterBlockHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).exitBlockHead(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TplVisitor ) return ((TplVisitor<? extends T>)visitor).visitBlockHead(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockHeadContext blockHead() throws RecognitionException {
		BlockHeadContext _localctx = new BlockHeadContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_blockHead);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			match(BLOCK_HEAD);
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
	public static class BlockBodyContext extends ParserRuleContext {
		public List<SegmentContext> segment() {
			return getRuleContexts(SegmentContext.class);
		}
		public SegmentContext segment(int i) {
			return getRuleContext(SegmentContext.class,i);
		}
		public BlockBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).enterBlockBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).exitBlockBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TplVisitor ) return ((TplVisitor<? extends T>)visitor).visitBlockBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockBodyContext blockBody() throws RecognitionException {
		BlockBodyContext _localctx = new BlockBodyContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_blockBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 65522L) != 0)) {
				{
				{
				setState(79);
				segment();
				}
				}
				setState(84);
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
	public static class ParametersContext extends ParserRuleContext {
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(TplParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(TplParser.COMMA, i);
		}
		public ParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).enterParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).exitParameters(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TplVisitor ) return ((TplVisitor<? extends T>)visitor).visitParameters(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParametersContext parameters() throws RecognitionException {
		ParametersContext _localctx = new ParametersContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_parameters);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			parameter();
			setState(90);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(86);
				match(COMMA);
				setState(87);
				parameter();
				}
				}
				setState(92);
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
	public static class ParameterContext extends ParserRuleContext {
		public TerminalNode REF_VALUE() { return getToken(TplParser.REF_VALUE, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(TplParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(TplParser.IDENTIFIER, i);
		}
		public TerminalNode COLON() { return getToken(TplParser.COLON, 0); }
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).enterParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).exitParameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TplVisitor ) return ((TplVisitor<? extends T>)visitor).visitParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_parameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(93);
				match(IDENTIFIER);
				setState(94);
				match(COLON);
				}
				break;
			}
			setState(97);
			_la = _input.LA(1);
			if ( !(_la==REF_VALUE || _la==IDENTIFIER) ) {
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
	public static class ValueContext extends ParserRuleContext {
		public TerminalNode REF_VALUE() { return getToken(TplParser.REF_VALUE, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TplVisitor ) return ((TplVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			match(REF_VALUE);
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
	public static class TextContext extends ParserRuleContext {
		public List<ContentContext> content() {
			return getRuleContexts(ContentContext.class);
		}
		public ContentContext content(int i) {
			return getRuleContext(ContentContext.class,i);
		}
		public TextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_text; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).enterText(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).exitText(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TplVisitor ) return ((TplVisitor<? extends T>)visitor).visitText(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TextContext text() throws RecognitionException {
		TextContext _localctx = new TextContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_text);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(102); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(101);
					content();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(104); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
	public static class ContentContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(TplParser.IDENTIFIER, 0); }
		public TerminalNode TEXT() { return getToken(TplParser.TEXT, 0); }
		public TerminalNode WHITESPACE() { return getToken(TplParser.WHITESPACE, 0); }
		public TerminalNode COMMA() { return getToken(TplParser.COMMA, 0); }
		public TerminalNode PAREN_L() { return getToken(TplParser.PAREN_L, 0); }
		public TerminalNode PAREN_R() { return getToken(TplParser.PAREN_R, 0); }
		public TerminalNode DOLLAR() { return getToken(TplParser.DOLLAR, 0); }
		public TerminalNode SHARP() { return getToken(TplParser.SHARP, 0); }
		public TerminalNode EXCLAIM() { return getToken(TplParser.EXCLAIM, 0); }
		public TerminalNode COLON() { return getToken(TplParser.COLON, 0); }
		public ContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_content; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).enterContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TplListener ) ((TplListener)listener).exitContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TplVisitor ) return ((TplVisitor<? extends T>)visitor).visitContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContentContext content() throws RecognitionException {
		ContentContext _localctx = new ContentContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_content);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 65472L) != 0)) ) {
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

	public static final String _serializedATN =
		"\u0004\u0001\u000fm\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0001\u0000\u0005\u0000\u001c\b\u0000\n\u0000\f\u0000\u001f"+
		"\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0003"+
		"\u0001&\b\u0001\u0001\u0002\u0001\u0002\u0003\u0002*\b\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0003\u0003/\b\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0003\u00035\b\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0003\u0004:\b\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0005\u0004?\b\u0004\n\u0004\f\u0004B\t\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005I\b\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0005\u0007"+
		"Q\b\u0007\n\u0007\f\u0007T\t\u0007\u0001\b\u0001\b\u0001\b\u0005\bY\b"+
		"\b\n\b\f\b\\\t\b\u0001\t\u0001\t\u0003\t`\b\t\u0001\t\u0001\t\u0001\n"+
		"\u0001\n\u0001\u000b\u0004\u000bg\b\u000b\u000b\u000b\f\u000bh\u0001\f"+
		"\u0001\f\u0001\f\u0000\u0000\r\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u0000\u0002\u0002\u0000\u0001\u0001\u0006\u0006"+
		"\u0001\u0000\u0006\u000fl\u0000\u001d\u0001\u0000\u0000\u0000\u0002%\u0001"+
		"\u0000\u0000\u0000\u0004)\u0001\u0000\u0000\u0000\u0006+\u0001\u0000\u0000"+
		"\u0000\b6\u0001\u0000\u0000\u0000\nE\u0001\u0000\u0000\u0000\fM\u0001"+
		"\u0000\u0000\u0000\u000eR\u0001\u0000\u0000\u0000\u0010U\u0001\u0000\u0000"+
		"\u0000\u0012_\u0001\u0000\u0000\u0000\u0014c\u0001\u0000\u0000\u0000\u0016"+
		"f\u0001\u0000\u0000\u0000\u0018j\u0001\u0000\u0000\u0000\u001a\u001c\u0003"+
		"\u0002\u0001\u0000\u001b\u001a\u0001\u0000\u0000\u0000\u001c\u001f\u0001"+
		"\u0000\u0000\u0000\u001d\u001b\u0001\u0000\u0000\u0000\u001d\u001e\u0001"+
		"\u0000\u0000\u0000\u001e \u0001\u0000\u0000\u0000\u001f\u001d\u0001\u0000"+
		"\u0000\u0000 !\u0005\u0000\u0000\u0001!\u0001\u0001\u0000\u0000\u0000"+
		"\"&\u0003\u0004\u0002\u0000#&\u0003\u0014\n\u0000$&\u0003\u0016\u000b"+
		"\u0000%\"\u0001\u0000\u0000\u0000%#\u0001\u0000\u0000\u0000%$\u0001\u0000"+
		"\u0000\u0000&\u0003\u0001\u0000\u0000\u0000\'*\u0003\b\u0004\u0000(*\u0003"+
		"\u0006\u0003\u0000)\'\u0001\u0000\u0000\u0000)(\u0001\u0000\u0000\u0000"+
		"*\u0005\u0001\u0000\u0000\u0000+,\u0003\f\u0006\u0000,.\u0005\b\u0000"+
		"\u0000-/\u0003\u0010\b\u0000.-\u0001\u0000\u0000\u0000./\u0001\u0000\u0000"+
		"\u0000/0\u0001\u0000\u0000\u000004\u0005\t\u0000\u000012\u0003\u000e\u0007"+
		"\u000023\u0005\u0002\u0000\u000035\u0001\u0000\u0000\u000041\u0001\u0000"+
		"\u0000\u000045\u0001\u0000\u0000\u00005\u0007\u0001\u0000\u0000\u0000"+
		"67\u0005\u0004\u0000\u000079\u0005\b\u0000\u00008:\u0003\u0010\b\u0000"+
		"98\u0001\u0000\u0000\u00009:\u0001\u0000\u0000\u0000:;\u0001\u0000\u0000"+
		"\u0000;<\u0005\t\u0000\u0000<@\u0003\u000e\u0007\u0000=?\u0003\n\u0005"+
		"\u0000>=\u0001\u0000\u0000\u0000?B\u0001\u0000\u0000\u0000@>\u0001\u0000"+
		"\u0000\u0000@A\u0001\u0000\u0000\u0000AC\u0001\u0000\u0000\u0000B@\u0001"+
		"\u0000\u0000\u0000CD\u0005\u0002\u0000\u0000D\t\u0001\u0000\u0000\u0000"+
		"EF\u0005\u0003\u0000\u0000FH\u0005\b\u0000\u0000GI\u0003\u0010\b\u0000"+
		"HG\u0001\u0000\u0000\u0000HI\u0001\u0000\u0000\u0000IJ\u0001\u0000\u0000"+
		"\u0000JK\u0005\t\u0000\u0000KL\u0003\u000e\u0007\u0000L\u000b\u0001\u0000"+
		"\u0000\u0000MN\u0005\u0005\u0000\u0000N\r\u0001\u0000\u0000\u0000OQ\u0003"+
		"\u0002\u0001\u0000PO\u0001\u0000\u0000\u0000QT\u0001\u0000\u0000\u0000"+
		"RP\u0001\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000S\u000f\u0001\u0000"+
		"\u0000\u0000TR\u0001\u0000\u0000\u0000UZ\u0003\u0012\t\u0000VW\u0005\n"+
		"\u0000\u0000WY\u0003\u0012\t\u0000XV\u0001\u0000\u0000\u0000Y\\\u0001"+
		"\u0000\u0000\u0000ZX\u0001\u0000\u0000\u0000Z[\u0001\u0000\u0000\u0000"+
		"[\u0011\u0001\u0000\u0000\u0000\\Z\u0001\u0000\u0000\u0000]^\u0005\u0006"+
		"\u0000\u0000^`\u0005\u000e\u0000\u0000_]\u0001\u0000\u0000\u0000_`\u0001"+
		"\u0000\u0000\u0000`a\u0001\u0000\u0000\u0000ab\u0007\u0000\u0000\u0000"+
		"b\u0013\u0001\u0000\u0000\u0000cd\u0005\u0001\u0000\u0000d\u0015\u0001"+
		"\u0000\u0000\u0000eg\u0003\u0018\f\u0000fe\u0001\u0000\u0000\u0000gh\u0001"+
		"\u0000\u0000\u0000hf\u0001\u0000\u0000\u0000hi\u0001\u0000\u0000\u0000"+
		"i\u0017\u0001\u0000\u0000\u0000jk\u0007\u0001\u0000\u0000k\u0019\u0001"+
		"\u0000\u0000\u0000\f\u001d%).49@HRZ_h";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}