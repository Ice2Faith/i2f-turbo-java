package i2f.turbo.idea.plugin.tinyscript.lang.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import i2f.turbo.idea.plugin.tinyscript.TinyScriptConsts;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class TinyScriptColorSettingsPage implements ColorSettingsPage {
    @SuppressWarnings("DialogTitleCapitalization")
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Keywords", TinyScriptSyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("String", TinyScriptSyntaxHighlighter.STRING),
            new AttributesDescriptor("Value", TinyScriptSyntaxHighlighter.NUMBER),
            new AttributesDescriptor("Comment", TinyScriptSyntaxHighlighter.COMMENT),
            new AttributesDescriptor("Class", TinyScriptSyntaxHighlighter.CLASS_REFERENCE),
            new AttributesDescriptor("Paren", TinyScriptSyntaxHighlighter.PAREN),
            new AttributesDescriptor("Bad value", TinyScriptSyntaxHighlighter.BAD_CHARACTER)
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return TinyScriptConsts.ICON;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new TinyScriptSyntaxHighlighter();
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
        return TinyScriptConsts.LANGUAGE_ID;
    }
}
