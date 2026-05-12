package i2f.turbo.idea.plugin.funic.lang.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import i2f.turbo.idea.plugin.funic.FunicConsts;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class FunicColorSettingsPage implements ColorSettingsPage {
    @SuppressWarnings("DialogTitleCapitalization")
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Keywords", FunicSyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("String", FunicSyntaxHighlighter.STRING),
            new AttributesDescriptor("Value", FunicSyntaxHighlighter.NUMBER),
            new AttributesDescriptor("Comment", FunicSyntaxHighlighter.LINE_COMMENT),
            new AttributesDescriptor("Class", FunicSyntaxHighlighter.CLASS_REFERENCE),
            new AttributesDescriptor("Paren", FunicSyntaxHighlighter.PAREN),
            new AttributesDescriptor("Bad value", FunicSyntaxHighlighter.BAD_CHARACTER)
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return FunicConsts.ICON;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new FunicSyntaxHighlighter();
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
        return FunicConsts.LANGUAGE_ID;
    }
}
