package i2f.turbo.idea.plugin.tinyscript.lang.formatter;

import com.intellij.formatting.*;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import i2f.turbo.idea.plugin.tinyscript.TinyScriptLanguage;
import i2f.turbo.idea.plugin.tinyscript.grammar.psi.TinyScriptTypes;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ice2Faith
 * @date 2026/5/12 22:00
 * @desc
 */
public class TinyScriptFormattingModelBuilder implements FormattingModelBuilder {
    @Override
    public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        final CodeStyleSettings codeStyleSettings = formattingContext.getCodeStyleSettings();
        return FormattingModelProvider
                .createFormattingModelForPsiFile(formattingContext.getContainingFile(),
                        new TinyScriptBlock(formattingContext.getNode(),
                                Wrap.createWrap(WrapType.NONE, false),
                                Alignment.createAlignment(),
                                createSpaceBuilder(codeStyleSettings)),
                        codeStyleSettings);
    }

    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        return new SpacingBuilder(settings, TinyScriptLanguage.INSTANCE)
                // 操作符前后空格
                .around(TokenSet.create(
                        TinyScriptTypes.KEY_IN,
                        TinyScriptTypes.KEY_INSTANCE_OF,
                        TinyScriptTypes.KEY_TYPE_OF,
                        TinyScriptTypes.KEY_IS,
                        TinyScriptTypes.KEY_LT,
                        TinyScriptTypes.KEY_LTE,
                        TinyScriptTypes.KEY_NE,
                        TinyScriptTypes.KEY_NEQ,
                        TinyScriptTypes.KEY_NEW,
                        TinyScriptTypes.KEY_NOT,
                        TinyScriptTypes.KEY_OR,

                        TinyScriptTypes.OP_ADD,
                        TinyScriptTypes.OP_AND,

                        TinyScriptTypes.OP_ASSIGN,
                        TinyScriptTypes.OP_ASSIGN_ADD,
                        TinyScriptTypes.OP_ASSIGN_DIV,
                        TinyScriptTypes.OP_ASSIGN_IFNULL,
                        TinyScriptTypes.OP_ASSIGN_MOD,
                        TinyScriptTypes.OP_ASSIGN_MUL,
                        TinyScriptTypes.OP_ASSIGN_NOTNULL,
                        TinyScriptTypes.OP_ASSIGN_SUB,

                        TinyScriptTypes.OP_DIV,

                        TinyScriptTypes.OP_EQ,

                        TinyScriptTypes.OP_GT,
                        TinyScriptTypes.OP_GTE,

                        TinyScriptTypes.OP_LT,
                        TinyScriptTypes.OP_LTE,

                        TinyScriptTypes.OP_MUL,
                        TinyScriptTypes.OP_NE,
                        TinyScriptTypes.OP_NEQ,
                        TinyScriptTypes.OP_OR,
                        TinyScriptTypes.OP_PIPELINE,

                        TinyScriptTypes.TERM_COLON,

                        TinyScriptTypes.TERM_QUESTION
                ))
                .spaces(1)
                // 逗号之后空格
                .after(TokenSet.create(
                        TinyScriptTypes.TERM_COMMA
                ))
                .spaces(1)
                // 语句块前空格
                .before(TokenSet.create(
                        TinyScriptTypes.SCRIPT_BLOCK
                ))
                .spaces(1)
                // 属性访问之前不要空格
                .before(TokenSet.create(
                        TinyScriptTypes.SQUARE_QUOTE_RIGHT_PART
                ))
                .none()
                // 双目运算符前后空格
                .aroundInside(TokenSet.create(
                        TinyScriptTypes.MATH_MUL_DIV_OPERATOR_PART,
                        TinyScriptTypes.MATH_ADD_SUB_OPERATOR_PART,
                        TinyScriptTypes.CAST_INSTANCE_OF_RIGHT_PART,
                        TinyScriptTypes.COMPARE_OPERATOR_PART,
                        TinyScriptTypes.LOGICAL_LINK_OPERATOR_PART
                ), TinyScriptTypes.OPERATOR_SEGMENT)
                .spaces(1)
                // 后置表达式前空格
                .before(TokenSet.create(
                        TinyScriptTypes.THIRD_OPERATE_RIGHT_PART,
                        TinyScriptTypes.PIPELINE_FUNCTION_SEGMENT
                ))
                .spaces(1)
                // 语句块换行
                .afterInside(TinyScriptTypes.TERM_CURLY_L, TinyScriptTypes.SCRIPT_BLOCK)
                .lineBreakInCode()
                // 语句块换行
                .beforeInside(TinyScriptTypes.TERM_CURLY_R, TinyScriptTypes.SCRIPT_BLOCK)
                .lineBreakInCode()
                // 关键词后面空格
                .after(TokenSet.create(
                        TinyScriptTypes.KEY_FUNC,
                        TinyScriptTypes.KEY_TRY,
                        TinyScriptTypes.KEY_THROW,
                        TinyScriptTypes.KEY_FOR,
                        TinyScriptTypes.KEY_DO,
                        TinyScriptTypes.KEY_WHILE,
                        TinyScriptTypes.KEY_IF,
                        TinyScriptTypes.KEY_NEW,
                        TinyScriptTypes.KEY_NOT,
                        TinyScriptTypes.KEY_DEBUGGER
                ))
                .spaces(1)
                // 关键词前后空格
                .after(TokenSet.create(
                        TinyScriptTypes.KEY_CATCH,
                        TinyScriptTypes.KEY_FINALLY,
                        TinyScriptTypes.KEY_ELIF,
                        TinyScriptTypes.KEY_ELSE
                ))
                .spaces(1)
                // 关键字换行
                .after(TokenSet.create(
                        TinyScriptTypes.KEY_FUNC,
                        TinyScriptTypes.KEY_TRY,
                        TinyScriptTypes.KEY_THROW,
                        TinyScriptTypes.KEY_FOR,
                        TinyScriptTypes.KEY_DO,
                        TinyScriptTypes.KEY_WHILE,
                        TinyScriptTypes.KEY_IF,
                        TinyScriptTypes.KEY_DEBUGGER
                ))
                .lineBreakInCode()
                // 符号后接表达式之间空格
                .between(TokenSet.create(
                        TinyScriptTypes.KEY_AS,
                        TinyScriptTypes.KEY_NOT,
                        TinyScriptTypes.KEY_GTE,
                        TinyScriptTypes.KEY_LTE,
                        TinyScriptTypes.KEY_GT,
                        TinyScriptTypes.KEY_LT,
                        TinyScriptTypes.KEY_NEQ,
                        TinyScriptTypes.KEY_NE,
                        TinyScriptTypes.KEY_EQ,
                        TinyScriptTypes.KEY_IN,
                        TinyScriptTypes.KEY_INSTANCE_OF,
                        TinyScriptTypes.KEY_TYPE_OF,
                        TinyScriptTypes.KEY_IS,
                        TinyScriptTypes.KEY_AND,
                        TinyScriptTypes.KEY_OR,
                        TinyScriptTypes.KEY_DEBUGGER,
                        TinyScriptTypes.TERM_SEMICOLON,
                        TinyScriptTypes.TERM_COLON,
                        TinyScriptTypes.TERM_QUESTION,
                        TinyScriptTypes.OP_MUL,
                        TinyScriptTypes.OP_DIV,
                        TinyScriptTypes.OP_MOD,
                        TinyScriptTypes.OP_ADD,
                        TinyScriptTypes.OP_SUB,
                        TinyScriptTypes.OP_GTE,
                        TinyScriptTypes.OP_LTE,
                        TinyScriptTypes.OP_NE,
                        TinyScriptTypes.OP_NEQ,
                        TinyScriptTypes.OP_EQ,
                        TinyScriptTypes.OP_GT,
                        TinyScriptTypes.OP_LT,
                        TinyScriptTypes.OP_AND,
                        TinyScriptTypes.OP_OR,
                        TinyScriptTypes.OP_EXCLAM,
                        TinyScriptTypes.OP_ASSIGN,
                        TinyScriptTypes.OP_ASSIGN_ADD,
                        TinyScriptTypes.OP_ASSIGN_SUB,
                        TinyScriptTypes.OP_ASSIGN_MUL,
                        TinyScriptTypes.OP_ASSIGN_DIV,
                        TinyScriptTypes.OP_ASSIGN_MOD,
                        TinyScriptTypes.OP_ASSIGN_IFNULL,
                        TinyScriptTypes.OP_ASSIGN_NOTNULL,
                        TinyScriptTypes.OP_PIPELINE
                ), TokenSet.create(
                        TinyScriptTypes.EXPRESS,
                        TinyScriptTypes.EXPRESS_SEGMENT
                ))
                .spaces(1)
                // 表达式后接符号之间空格
                .between(TokenSet.create(
                        TinyScriptTypes.EXPRESS,
                        TinyScriptTypes.EXPRESS_SEGMENT
                ), TokenSet.create(
                        TinyScriptTypes.KEY_AS,
                        TinyScriptTypes.KEY_NOT,
                        TinyScriptTypes.KEY_GTE,
                        TinyScriptTypes.KEY_LTE,
                        TinyScriptTypes.KEY_GT,
                        TinyScriptTypes.KEY_LT,
                        TinyScriptTypes.KEY_NEQ,
                        TinyScriptTypes.KEY_NE,
                        TinyScriptTypes.KEY_EQ,
                        TinyScriptTypes.KEY_IN,
                        TinyScriptTypes.KEY_INSTANCE_OF,
                        TinyScriptTypes.KEY_TYPE_OF,
                        TinyScriptTypes.KEY_IS,
                        TinyScriptTypes.KEY_AND,
                        TinyScriptTypes.KEY_OR,
                        TinyScriptTypes.TERM_COLON,
                        TinyScriptTypes.TERM_QUESTION,
                        TinyScriptTypes.OP_MUL,
                        TinyScriptTypes.OP_DIV,
                        TinyScriptTypes.OP_MOD,
                        TinyScriptTypes.OP_ADD,
                        TinyScriptTypes.OP_SUB,
                        TinyScriptTypes.OP_GTE,
                        TinyScriptTypes.OP_LTE,
                        TinyScriptTypes.OP_NE,
                        TinyScriptTypes.OP_NEQ,
                        TinyScriptTypes.OP_EQ,
                        TinyScriptTypes.OP_GT,
                        TinyScriptTypes.OP_LT,
                        TinyScriptTypes.OP_AND,
                        TinyScriptTypes.OP_OR,
                        TinyScriptTypes.OP_EXCLAM,
                        TinyScriptTypes.OP_ASSIGN,
                        TinyScriptTypes.OP_ASSIGN_ADD,
                        TinyScriptTypes.OP_ASSIGN_SUB,
                        TinyScriptTypes.OP_ASSIGN_MUL,
                        TinyScriptTypes.OP_ASSIGN_DIV,
                        TinyScriptTypes.OP_ASSIGN_MOD,
                        TinyScriptTypes.OP_ASSIGN_IFNULL,
                        TinyScriptTypes.OP_ASSIGN_NOTNULL,
                        TinyScriptTypes.OP_PIPELINE
                ))
                .spaces(1)
                // Map中的每项需要换行
                .between(TinyScriptTypes.TERM_COMMA, TinyScriptTypes.JSON_PAIR)
                .lineBreakInCode()
                // 函数调用具名参数之间空格
                .aroundInside(TinyScriptTypes.TERM_COLON, TinyScriptTypes.ARGUMENT)
                .spaces(1)
                // 返回语句换行
                .before(TokenSet.create(
                        TinyScriptTypes.KEY_RETURN,
                        TinyScriptTypes.CONTROL_SEGMENT,
                        TinyScriptTypes.KEY_CONTINUE,
                        TinyScriptTypes.KEY_BREAK
                )).lineBreakInCode()
                ;
    }

}
