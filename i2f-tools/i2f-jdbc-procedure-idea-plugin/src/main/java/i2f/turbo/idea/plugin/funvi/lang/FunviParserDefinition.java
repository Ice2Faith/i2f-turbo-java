package i2f.turbo.idea.plugin.funvi.lang;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import i2f.turbo.idea.plugin.funvi.FunviLanguage;
import i2f.turbo.idea.plugin.funvi.grammar.parser.FunviParser;
import i2f.turbo.idea.plugin.funvi.grammar.psi.FunviTypes;
import org.jetbrains.annotations.NotNull;

public class FunviParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE = new IFileElementType(FunviLanguage.INSTANCE);

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new FunviAdapter();
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new FunviParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return FunviTypes.Factory.createElement(node);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new FunviFile(viewProvider);
    }
}
