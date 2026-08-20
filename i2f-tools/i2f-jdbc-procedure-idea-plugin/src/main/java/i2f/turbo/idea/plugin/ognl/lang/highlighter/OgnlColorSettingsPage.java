package i2f.turbo.idea.plugin.ognl.lang.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import i2f.turbo.idea.plugin.ognl.OgnlConsts;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class OgnlColorSettingsPage implements ColorSettingsPage {
    @SuppressWarnings("DialogTitleCapitalization")
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Keywords", OgnlSyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("String", OgnlSyntaxHighlighter.STRING),
            new AttributesDescriptor("Value", OgnlSyntaxHighlighter.NUMBER),
            new AttributesDescriptor("Class Reference", OgnlSyntaxHighlighter.CLASS_REFERENCE),
            new AttributesDescriptor("Paren", OgnlSyntaxHighlighter.PAREN),
            new AttributesDescriptor("Curly", OgnlSyntaxHighlighter.CURLY),
            new AttributesDescriptor("Square Bracket", OgnlSyntaxHighlighter.BRACKET_SQUARE),
            new AttributesDescriptor("Operator", OgnlSyntaxHighlighter.OPERATOR),
            new AttributesDescriptor("Comma", OgnlSyntaxHighlighter.COMMA),
            new AttributesDescriptor("Semicolon", OgnlSyntaxHighlighter.SEMICOLON),
            new AttributesDescriptor("Dot", OgnlSyntaxHighlighter.DOT),
            new AttributesDescriptor("Bad value", OgnlSyntaxHighlighter.BAD_CHARACTER)
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return OgnlConsts.ICON;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new OgnlSyntaxHighlighter();
    }

    @NonNls
    @NotNull
    @Override
    public String getDemoText() {
        return "user.roles[0].name=${login.default}.getDefaultRole().getName();";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return OgnlConsts.LANGUAGE_ID;
    }
}
