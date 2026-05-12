package i2f.turbo.idea.plugin.funic.lang.formatter;

import com.intellij.formatting.*;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import i2f.turbo.idea.plugin.funic.FunicLanguage;
import i2f.turbo.idea.plugin.funic.grammar.psi.FunicTypes;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ice2Faith
 * @date 2026/5/12 22:00
 * @desc
 */
public class FunicFormattingModelBuilder implements FormattingModelBuilder {
    @Override
    public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        final CodeStyleSettings codeStyleSettings = formattingContext.getCodeStyleSettings();
        return FormattingModelProvider
                .createFormattingModelForPsiFile(formattingContext.getContainingFile(),
                        new FunicBlock(formattingContext.getNode(),
                                Wrap.createWrap(WrapType.NONE, false),
                                Alignment.createAlignment(),
                                createSpaceBuilder(codeStyleSettings)),
                        codeStyleSettings);
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        return new SpacingBuilder(settings, FunicLanguage.INSTANCE)
                .around(TokenSet.create(
                        FunicTypes.KW_IN,
                        FunicTypes.KW_INSTANCEOF,
                        FunicTypes.KW_IS,
                        FunicTypes.KW_LT,
                        FunicTypes.KW_LTE,
                        FunicTypes.KW_NE,
                        FunicTypes.KW_NEQ,
                        FunicTypes.KW_NEW,
                        FunicTypes.KW_NOT,
                        FunicTypes.KW_OR,

                        FunicTypes.OP_ADD,
                        FunicTypes.OP_AND,

                        FunicTypes.OP_ASSIGN,
                        FunicTypes.OP_ASSIGN_ADD,
                        FunicTypes.OP_ASSIGN_DIV,
                        FunicTypes.OP_ASSIGN_IFNULL,
                        FunicTypes.OP_ASSIGN_MOD,
                        FunicTypes.OP_ASSIGN_MUL,
                        FunicTypes.OP_ASSIGN_NOTNULL,
                        FunicTypes.OP_ASSIGN_SUB,

                        FunicTypes.OP_BIT_AND,
                        FunicTypes.OP_BIT_LMOV,
                        FunicTypes.OP_BIT_OR,

                        FunicTypes.OP_BIT_RMOV,
                        FunicTypes.OP_BIT_RSMOV,

                        FunicTypes.OP_DIV,

                        FunicTypes.OP_EQ,

                        FunicTypes.OP_EXTEND_TO,
                        FunicTypes.OP_EXTRA,
                        FunicTypes.OP_GT,
                        FunicTypes.OP_GTE,

                        FunicTypes.OP_INT_DIV,
                        FunicTypes.OP_LT,
                        FunicTypes.OP_LTE,

                        FunicTypes.OP_MUL,
                        FunicTypes.OP_NE,
                        FunicTypes.OP_NEQ,
                        FunicTypes.OP_OR,
                        FunicTypes.OP_PIPELINE,
                        FunicTypes.OP_RECV_FROM,

                        FunicTypes.OP_TEQ,
                        FunicTypes.OP_TNEQ,

                        FunicTypes.TERM_COLON,

                        FunicTypes.TERM_QUESTION
                ))
                .spaces(1)
                .after(TokenSet.create(
                        FunicTypes.TERM_COMMA
                ))
                .spaces(1)
                .before(TokenSet.create(
                        FunicTypes.INCR_DECR_AFTER_RIGHT_PART,
                        FunicTypes.MATH_MUL_DIV_OPERATOR_PART,
                        FunicTypes.MATH_ADD_SUB_OPERATOR_PART,
                        FunicTypes.CAST_AS_RIGHT_PART,
                        FunicTypes.COMPARE_OPERATOR_PART,
                        FunicTypes.LOGICAL_LINK_OPERATOR_PART,
                        FunicTypes.BIT_OPERATOR_PART,
                        FunicTypes.THIRD_OPERATE_RIGHT_PART,
                        FunicTypes.PIPELINE_FUNCTION_EXPRESS,
                        FunicTypes.ASSIGN_RIGHT_PART
                ))
                .spaces(1)
                .before(TokenSet.create(
                        FunicTypes.INSTANCE_FIELD_VALUE_RIGHT_PART,
                        FunicTypes.INSTANCE_FUNCTION_CALL_RIGHT_PART,
                        FunicTypes.SQUARE_QUOTE_RIGHT_PART
                ))
                .none()
                .before(TokenSet.create(
                        FunicTypes.SCRIPT_BLOCK
                ))
                .spaces(1)
                .beforeInside(TokenSet.create(
                        FunicTypes.INCR_DECR_AFTER_RIGHT_PART,
                        FunicTypes.MATH_MUL_DIV_OPERATOR_PART,
                        FunicTypes.MATH_ADD_SUB_OPERATOR_PART,
                        FunicTypes.CAST_AS_RIGHT_PART,
                        FunicTypes.COMPARE_OPERATOR_PART,
                        FunicTypes.LOGICAL_LINK_OPERATOR_PART,
                        FunicTypes.BIT_OPERATOR_PART,
                        FunicTypes.THIRD_OPERATE_RIGHT_PART,
                        FunicTypes.PIPELINE_FUNCTION_EXPRESS,
                        FunicTypes.ASSIGN_RIGHT_PART
                ),FunicTypes.OPERATOR_SEGMENT)
                .spaces(1);
    }

}
