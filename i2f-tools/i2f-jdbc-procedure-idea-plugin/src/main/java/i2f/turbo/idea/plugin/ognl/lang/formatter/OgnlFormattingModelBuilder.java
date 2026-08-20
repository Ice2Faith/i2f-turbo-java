package i2f.turbo.idea.plugin.ognl.lang.formatter;

import com.intellij.formatting.*;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import i2f.turbo.idea.plugin.ognl.OgnlLanguage;
import i2f.turbo.idea.plugin.ognl.grammar.psi.OgnlTypes;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ice2Faith
 * @date 2026/5/12 22:00
 * @desc
 */
public class OgnlFormattingModelBuilder implements FormattingModelBuilder {
    @Override
    public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        final CodeStyleSettings codeStyleSettings = formattingContext.getCodeStyleSettings();
        return FormattingModelProvider
                .createFormattingModelForPsiFile(formattingContext.getContainingFile(),
                        new OgnlBlock(formattingContext.getNode(),
                                Wrap.createWrap(WrapType.NONE, false),
                                Alignment.createAlignment(),
                                createSpaceBuilder(codeStyleSettings)),
                        codeStyleSettings);
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        return new SpacingBuilder(settings, OgnlLanguage.INSTANCE)
                // 操作符前后空格
                .around(TokenSet.create(
                        OgnlTypes.KW_IN,
                        OgnlTypes.KW_INSTANCEOF,
                        OgnlTypes.KW_LT,
                        OgnlTypes.KW_LTE,
                        OgnlTypes.KW_NE,
                        OgnlTypes.KW_NEQ,
                        OgnlTypes.KW_NEW,
                        OgnlTypes.KW_NOT,
                        OgnlTypes.KW_OR,

                        OgnlTypes.OP_ADD,
                        OgnlTypes.OP_AND,

                        OgnlTypes.OP_ASSIGN,

                        OgnlTypes.OP_BIT_AND,
                        OgnlTypes.OP_BIT_LMOV,
                        OgnlTypes.OP_BIT_OR,

                        OgnlTypes.OP_BIT_RMOV,
                        OgnlTypes.OP_BIT_RSMOV,

                        OgnlTypes.OP_DIV,

                        OgnlTypes.OP_EQ,

                        OgnlTypes.OP_GT,
                        OgnlTypes.OP_GTE,

                        OgnlTypes.OP_LT,
                        OgnlTypes.OP_LTE,

                        OgnlTypes.OP_MUL,
                        OgnlTypes.OP_NE,
                        OgnlTypes.OP_OR,

                        OgnlTypes.TERM_COLON,

                        OgnlTypes.TERM_QUESTION
                ))
                .spaces(1)
                // 逗号之后空格
                .after(TokenSet.create(
                        OgnlTypes.TERM_COMMA
                ))
                .spaces(1)
                // 属性访问之前不要空格
                .before(TokenSet.create(
                        OgnlTypes.PROPERTY_EXPRESS,
                        OgnlTypes.INSTANCE_FUNCTION_CALL_RIGHT_PART,
                        OgnlTypes.SQUARE_EXPRESS
                ))
                .none()
                // 双目运算符前后空格
                .aroundInside(TokenSet.create(
                        OgnlTypes.MATH_MUL_DIV_OPERATOR_PART,
                        OgnlTypes.MATH_ADD_SUB_OPERATOR_PART,
                        OgnlTypes.COMPARE_OPERATOR_PART,
                        OgnlTypes.LOGICAL_LINK_HIGH_OPERATOR_PART,
                        OgnlTypes.LOGICAL_LINK_LOW_OPERATOR_PART,
                        OgnlTypes.BIT_OPERATOR_PART
                ), OgnlTypes.OPERATOR_SEGMENT)
                .spaces(1)
                // 后置表达式前空格
                .before(TokenSet.create(
                        OgnlTypes.THIRD_OPERATE_RIGHT_PART,
                        OgnlTypes.ASSIGN_RIGHT_PART
                ))
                .spaces(1)
                // 关键词后面空格
                .after(TokenSet.create(
                        OgnlTypes.KW_NEW,
                        OgnlTypes.KW_NOT
                ))
                .spaces(1)
                // 符号后接表达式之间空格
                .between(TokenSet.create(
                        OgnlTypes.KW_NOT,
                        OgnlTypes.KW_GTE,
                        OgnlTypes.KW_LTE,
                        OgnlTypes.KW_GT,
                        OgnlTypes.KW_LT,
                        OgnlTypes.KW_NEQ,
                        OgnlTypes.KW_NE,
                        OgnlTypes.KW_EQ,
                        OgnlTypes.KW_IN,
                        OgnlTypes.KW_INSTANCEOF,
                        OgnlTypes.KW_AND,
                        OgnlTypes.KW_OR,
                        OgnlTypes.TERM_COLON,
                        OgnlTypes.TERM_QUESTION,
                        OgnlTypes.OP_MUL,
                        OgnlTypes.OP_DIV,
                        OgnlTypes.OP_MOD,
                        OgnlTypes.OP_ADD,
                        OgnlTypes.OP_SUB,
                        OgnlTypes.OP_GTE,
                        OgnlTypes.OP_LTE,
                        OgnlTypes.OP_NE,
                        OgnlTypes.OP_EQ,
                        OgnlTypes.OP_GT,
                        OgnlTypes.OP_LT,
                        OgnlTypes.OP_AND,
                        OgnlTypes.OP_OR,
                        OgnlTypes.OP_EXCLAM,
                        OgnlTypes.OP_BIT_LMOV,
                        OgnlTypes.OP_BIT_RSMOV,
                        OgnlTypes.OP_BIT_RMOV,
                        OgnlTypes.OP_BIT_XOR,
                        OgnlTypes.OP_BIT_AND,
                        OgnlTypes.OP_BIT_OR,
                        OgnlTypes.OP_BIT_REVERSE,
                        OgnlTypes.OP_ASSIGN
                ), TokenSet.create(
                        OgnlTypes.EXPRESS,
                        OgnlTypes.EXPRESS_SEGMENT
                ))
                .spaces(1)
                // 表达式后接符号之间空格
                .between(TokenSet.create(
                        OgnlTypes.EXPRESS,
                        OgnlTypes.EXPRESS_SEGMENT
                ), TokenSet.create(
                        OgnlTypes.KW_NOT,
                        OgnlTypes.KW_GTE,
                        OgnlTypes.KW_LTE,
                        OgnlTypes.KW_GT,
                        OgnlTypes.KW_LT,
                        OgnlTypes.KW_NEQ,
                        OgnlTypes.KW_NE,
                        OgnlTypes.KW_EQ,
                        OgnlTypes.KW_IN,
                        OgnlTypes.KW_INSTANCEOF,
                        OgnlTypes.KW_AND,
                        OgnlTypes.KW_OR,
                        OgnlTypes.TERM_COLON,
                        OgnlTypes.TERM_QUESTION,
                        OgnlTypes.OP_MUL,
                        OgnlTypes.OP_DIV,
                        OgnlTypes.OP_MOD,
                        OgnlTypes.OP_ADD,
                        OgnlTypes.OP_SUB,
                        OgnlTypes.OP_GTE,
                        OgnlTypes.OP_LTE,
                        OgnlTypes.OP_NE,
                        OgnlTypes.OP_EQ,
                        OgnlTypes.OP_GT,
                        OgnlTypes.OP_LT,
                        OgnlTypes.OP_AND,
                        OgnlTypes.OP_OR,
                        OgnlTypes.OP_EXCLAM,
                        OgnlTypes.OP_BIT_LMOV,
                        OgnlTypes.OP_BIT_RSMOV,
                        OgnlTypes.OP_BIT_RMOV,
                        OgnlTypes.OP_BIT_XOR,
                        OgnlTypes.OP_BIT_AND,
                        OgnlTypes.OP_BIT_OR,
                        OgnlTypes.OP_ASSIGN
                ))
                .spaces(1)
                // 函数参数之间空格
                .betweenInside(OgnlTypes.FULL_NAME, OgnlTypes.IDENTIFIER, OgnlTypes.COMMA_EXPRESS_RIGHT_PART)
                .spaces(1)
                ;
    }

}
