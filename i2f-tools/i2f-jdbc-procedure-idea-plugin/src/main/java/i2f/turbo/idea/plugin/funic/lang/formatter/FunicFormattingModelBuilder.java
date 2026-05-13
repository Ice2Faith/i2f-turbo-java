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
                // 操作符前后空格
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
                // 逗号之后空格
                .after(TokenSet.create(
                        FunicTypes.TERM_COMMA
                ))
                .spaces(1)
                // 语句块前空格
                .before(TokenSet.create(
                        FunicTypes.SCRIPT_BLOCK
                ))
                .spaces(1)
                // 属性访问之前不要空格
                .before(TokenSet.create(
                        FunicTypes.INSTANCE_FIELD_VALUE_RIGHT_PART,
                        FunicTypes.INSTANCE_FUNCTION_CALL_RIGHT_PART,
                        FunicTypes.SQUARE_QUOTE_RIGHT_PART
                ))
                .none()
                // 双目运算符前后空格
                .aroundInside(TokenSet.create(
                        FunicTypes.INCR_DECR_AFTER_RIGHT_PART,
                        FunicTypes.MATH_MUL_DIV_OPERATOR_PART,
                        FunicTypes.MATH_ADD_SUB_OPERATOR_PART,
                        FunicTypes.CAST_AS_RIGHT_PART,
                        FunicTypes.COMPARE_OPERATOR_PART,
                        FunicTypes.LOGICAL_LINK_OPERATOR_PART,
                        FunicTypes.BIT_OPERATOR_PART
                ), FunicTypes.OPERATOR_SEGMENT)
                .spaces(1)
                // 后置表达式前空格
                .before(TokenSet.create(
                        FunicTypes.THIRD_OPERATE_RIGHT_PART,
                        FunicTypes.PIPELINE_FUNCTION_EXPRESS,
                        FunicTypes.ASSIGN_RIGHT_PART
                ))
                .spaces(1)
                // 语句块换行
                .afterInside(FunicTypes.TERM_CURLY_L, FunicTypes.SCRIPT_BLOCK)
                .lineBreakInCode()
                // 语句块换行
                .beforeInside(FunicTypes.TERM_CURLY_R, FunicTypes.SCRIPT_BLOCK)
                .lineBreakInCode()
                // 关键词后面空格
                .after(TokenSet.create(
                        FunicTypes.KW_FUNC,
                        FunicTypes.KW_DEF,
                        FunicTypes.KW_TRY,
                        FunicTypes.KW_THROW,
                        FunicTypes.KW_FOR,
                        FunicTypes.KW_DO,
                        FunicTypes.KW_WHILE,
                        FunicTypes.KW_IF,
                        FunicTypes.KW_NEW,
                        FunicTypes.KW_NOT,
                        FunicTypes.KW_GO,
                        FunicTypes.KW_IMPORT,
                        FunicTypes.KW_DEBUGGER
                ))
                .spaces(1)
                // 关键词前后空格
                .after(TokenSet.create(
                        FunicTypes.KW_CATCH,
                        FunicTypes.KW_FINALLY,
                        FunicTypes.KW_ELIF,
                        FunicTypes.KW_ELSE
                ))
                .spaces(1)
                // 关键字换行
                .after(TokenSet.create(
                        FunicTypes.KW_FUNC,
                        FunicTypes.KW_DEF,
                        FunicTypes.KW_TRY,
                        FunicTypes.KW_THROW,
                        FunicTypes.KW_FOR,
                        FunicTypes.KW_DO,
                        FunicTypes.KW_WHILE,
                        FunicTypes.KW_IF,
                        FunicTypes.KW_GO,
                        FunicTypes.KW_IMPORT,
                        FunicTypes.KW_DEBUGGER
                ))
                .lineBreakInCode()
                // 符号后接表达式之间空格
                .between(TokenSet.create(
                        FunicTypes.KW_AS,
                        FunicTypes.KW_NOT,
                        FunicTypes.KW_TEQ,
                        FunicTypes.KW_TNEQ,
                        FunicTypes.KW_GTE,
                        FunicTypes.KW_LTE,
                        FunicTypes.KW_GT,
                        FunicTypes.KW_LT,
                        FunicTypes.KW_NEQ,
                        FunicTypes.KW_NE,
                        FunicTypes.KW_EQ,
                        FunicTypes.KW_IN,
                        FunicTypes.KW_INSTANCEOF,
                        FunicTypes.KW_IS,
                        FunicTypes.KW_AND,
                        FunicTypes.KW_OR,
                        FunicTypes.KW_GO,
                        FunicTypes.KW_DEBUGGER,
                        FunicTypes.TERM_SEMICOLON,
                        FunicTypes.TERM_COLON,
                        FunicTypes.TERM_QUESTION,
                        FunicTypes.OP_EXTRA,
                        FunicTypes.OP_EXTEND_TO,
                        FunicTypes.OP_RECV_FROM,
                        FunicTypes.OP_DIAMOND_NAME_L,
                        FunicTypes.OP_MUL,
                        FunicTypes.OP_INT_DIV,
                        FunicTypes.OP_DIV,
                        FunicTypes.OP_MOD,
                        FunicTypes.OP_INCR,
                        FunicTypes.OP_DECR,
                        FunicTypes.OP_ADD,
                        FunicTypes.OP_SUB,
                        FunicTypes.OP_GTE,
                        FunicTypes.OP_LTE,
                        FunicTypes.OP_TNEQ,
                        FunicTypes.OP_NE,
                        FunicTypes.OP_NEQ,
                        FunicTypes.OP_TEQ,
                        FunicTypes.OP_EQ,
                        FunicTypes.OP_GT,
                        FunicTypes.OP_LT,
                        FunicTypes.OP_AND,
                        FunicTypes.OP_OR,
                        FunicTypes.OP_EXCLAM,
                        FunicTypes.OP_BIT_LMOV,
                        FunicTypes.OP_BIT_RSMOV,
                        FunicTypes.OP_BIT_RMOV,
                        FunicTypes.OP_BIT_XOR,
                        FunicTypes.OP_BIT_AND,
                        FunicTypes.OP_BIT_OR,
                        FunicTypes.OP_BIT_REVERSE,
                        FunicTypes.OP_ASSIGN,
                        FunicTypes.OP_ASSIGN_ADD,
                        FunicTypes.OP_ASSIGN_SUB,
                        FunicTypes.OP_ASSIGN_MUL,
                        FunicTypes.OP_ASSIGN_DIV,
                        FunicTypes.OP_ASSIGN_MOD,
                        FunicTypes.OP_ASSIGN_IFNULL,
                        FunicTypes.OP_ASSIGN_NOTNULL,
                        FunicTypes.OP_PIPELINE
                ), TokenSet.create(
                        FunicTypes.EXPRESS,
                        FunicTypes.EXPRESS_SEGMENT
                ))
                .spaces(1)
                // 表达式后接符号之间空格
                .between(TokenSet.create(
                        FunicTypes.EXPRESS,
                        FunicTypes.EXPRESS_SEGMENT
                ), TokenSet.create(
                        FunicTypes.KW_AS,
                        FunicTypes.KW_NOT,
                        FunicTypes.KW_TEQ,
                        FunicTypes.KW_TNEQ,
                        FunicTypes.KW_GTE,
                        FunicTypes.KW_LTE,
                        FunicTypes.KW_GT,
                        FunicTypes.KW_LT,
                        FunicTypes.KW_NEQ,
                        FunicTypes.KW_NE,
                        FunicTypes.KW_EQ,
                        FunicTypes.KW_IN,
                        FunicTypes.KW_INSTANCEOF,
                        FunicTypes.KW_IS,
                        FunicTypes.KW_AND,
                        FunicTypes.KW_OR,
                        FunicTypes.TERM_COLON,
                        FunicTypes.TERM_QUESTION,
                        FunicTypes.OP_EXTRA,
                        FunicTypes.OP_EXTEND_TO,
                        FunicTypes.OP_RECV_FROM,
                        FunicTypes.OP_DIAMOND_NAME_R,
                        FunicTypes.OP_MUL,
                        FunicTypes.OP_INT_DIV,
                        FunicTypes.OP_DIV,
                        FunicTypes.OP_MOD,
                        FunicTypes.OP_INCR,
                        FunicTypes.OP_DECR,
                        FunicTypes.OP_ADD,
                        FunicTypes.OP_SUB,
                        FunicTypes.OP_GTE,
                        FunicTypes.OP_LTE,
                        FunicTypes.OP_TNEQ,
                        FunicTypes.OP_NE,
                        FunicTypes.OP_NEQ,
                        FunicTypes.OP_TEQ,
                        FunicTypes.OP_EQ,
                        FunicTypes.OP_GT,
                        FunicTypes.OP_LT,
                        FunicTypes.OP_AND,
                        FunicTypes.OP_OR,
                        FunicTypes.OP_EXCLAM,
                        FunicTypes.OP_BIT_LMOV,
                        FunicTypes.OP_BIT_RSMOV,
                        FunicTypes.OP_BIT_RMOV,
                        FunicTypes.OP_BIT_XOR,
                        FunicTypes.OP_BIT_AND,
                        FunicTypes.OP_BIT_OR,
                        FunicTypes.OP_ASSIGN,
                        FunicTypes.OP_ASSIGN_ADD,
                        FunicTypes.OP_ASSIGN_SUB,
                        FunicTypes.OP_ASSIGN_MUL,
                        FunicTypes.OP_ASSIGN_DIV,
                        FunicTypes.OP_ASSIGN_MOD,
                        FunicTypes.OP_ASSIGN_IFNULL,
                        FunicTypes.OP_ASSIGN_NOTNULL,
                        FunicTypes.OP_PIPELINE
                ))
                .spaces(1)
                // Map中的每项需要换行
                .between(FunicTypes.TERM_COMMA, FunicTypes.UNPACK_MAP_EXPRESS)
                .lineBreakInCode()
                // 函数参数之间空格
                .betweenInside(FunicTypes.FULL_NAME, FunicTypes.IDENTIFIER, FunicTypes.FUNCTION_PARAMETER)
                .spaces(1)
                .aroundInside(FunicTypes.TERM_COLON, FunicTypes.FUNCTION_PARAMETER)
                .spaces(1)
                // 函数返回值声明之间空格
                .aroundInside(FunicTypes.TERM_COLON, FunicTypes.FUNCTION_DECLARE_RETURN)
                .spaces(1)
                // 函数调用具名参数之间空格
                .aroundInside(FunicTypes.TERM_COLON, FunicTypes.FUNCTION_ARGUMENT)
                .spaces(1)
                // 返回语句换行
                .before(TokenSet.create(
                        FunicTypes.KW_RETURN,
                        FunicTypes.RETURN_EXPRESS,
                        FunicTypes.KW_CONTINUE,
                        FunicTypes.CONTINUE_EXPRESS,
                        FunicTypes.KW_BREAK,
                        FunicTypes.BREAK_EXPRESS
                )).lineBreakInCode()
                ;
    }

}
