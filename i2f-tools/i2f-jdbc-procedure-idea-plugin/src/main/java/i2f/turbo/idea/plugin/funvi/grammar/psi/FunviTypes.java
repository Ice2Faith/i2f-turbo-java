// This is a generated file. Not intended for manual editing.
package i2f.turbo.idea.plugin.funvi.grammar.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import i2f.turbo.idea.plugin.funvi.lang.psi.FunviElementType;
import i2f.turbo.idea.plugin.funvi.lang.psi.FunviTokenType;
import i2f.turbo.idea.plugin.funvi.grammar.psi.elements.impl.*;

public interface FunviTypes {

  IElementType BLOCK = new FunviElementType("BLOCK");
  IElementType BLOCK_BODY = new FunviElementType("BLOCK_BODY");
  IElementType BLOCK_HEAD_SEGMENT = new FunviElementType("BLOCK_HEAD_SEGMENT");
  IElementType COMMON_BLOCK = new FunviElementType("COMMON_BLOCK");
  IElementType CONTENT = new FunviElementType("CONTENT");
  IElementType ELSE_BLOCK = new FunviElementType("ELSE_BLOCK");
  IElementType IF_BLOCK = new FunviElementType("IF_BLOCK");
  IElementType PARAMETER = new FunviElementType("PARAMETER");
  IElementType PARAMETERS = new FunviElementType("PARAMETERS");
  IElementType SEGMENT = new FunviElementType("SEGMENT");
  IElementType TEXT_SEGMENT = new FunviElementType("TEXT_SEGMENT");
  IElementType VALUE = new FunviElementType("VALUE");

  IElementType OP_COLON = new FunviTokenType(":");
  IElementType OP_COMMA = new FunviTokenType(",");
  IElementType OP_DOLLAR = new FunviTokenType("$");
  IElementType OP_EXCLAIM = new FunviTokenType("!");
  IElementType OP_PAREN_L = new FunviTokenType("(");
  IElementType OP_PAREN_R = new FunviTokenType(")");
  IElementType OP_SHARP = new FunviTokenType("#");
  IElementType TERM_BLOCK_ELSE = new FunviTokenType("TERM_BLOCK_ELSE");
  IElementType TERM_BLOCK_END = new FunviTokenType("TERM_BLOCK_END");
  IElementType TERM_BLOCK_HEAD = new FunviTokenType("TERM_BLOCK_HEAD");
  IElementType TERM_BLOCK_IF = new FunviTokenType("TERM_BLOCK_IF");
  IElementType TERM_IDENTIFIER = new FunviTokenType("TERM_IDENTIFIER");
  IElementType TERM_REF_VALUE = new FunviTokenType("TERM_REF_VALUE");
  IElementType TERM_TEXT = new FunviTokenType("TERM_TEXT");
  IElementType TERM_WHITESPACE = new FunviTokenType("TERM_WHITESPACE");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == BLOCK) {
        return new FunviBlockImpl(node);
      } else if (type == BLOCK_BODY) {
        return new FunviBlockBodyImpl(node);
      } else if (type == BLOCK_HEAD_SEGMENT) {
        return new FunviBlockHeadSegmentImpl(node);
      } else if (type == COMMON_BLOCK) {
        return new FunviCommonBlockImpl(node);
      } else if (type == CONTENT) {
        return new FunviContentImpl(node);
      } else if (type == ELSE_BLOCK) {
        return new FunviElseBlockImpl(node);
      } else if (type == IF_BLOCK) {
        return new FunviIfBlockImpl(node);
      } else if (type == PARAMETER) {
        return new FunviParameterImpl(node);
      } else if (type == PARAMETERS) {
        return new FunviParametersImpl(node);
      } else if (type == SEGMENT) {
        return new FunviSegmentImpl(node);
      } else if (type == TEXT_SEGMENT) {
        return new FunviTextSegmentImpl(node);
      } else if (type == VALUE) {
        return new FunviValueImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
