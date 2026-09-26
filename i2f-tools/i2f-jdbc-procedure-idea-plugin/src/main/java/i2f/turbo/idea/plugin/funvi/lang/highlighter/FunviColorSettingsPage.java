package i2f.turbo.idea.plugin.funvi.lang.highlighter;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import i2f.turbo.idea.plugin.funvi.FunviConsts;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class FunviColorSettingsPage implements ColorSettingsPage {
    @SuppressWarnings("DialogTitleCapitalization")
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Keywords", FunviSyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("String", FunviSyntaxHighlighter.STRING),
            new AttributesDescriptor("Value", FunviSyntaxHighlighter.NUMBER),
            new AttributesDescriptor("Paren", FunviSyntaxHighlighter.PAREN),
            new AttributesDescriptor("Bad value", FunviSyntaxHighlighter.BAD_CHARACTER)
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return FunviConsts.ICON;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new FunviSyntaxHighlighter();
    }

    @NonNls
    @NotNull
    @Override
    public String getDemoText() {
        return "username = ${username}\n#if(${age>0})\n\tand age>${age}\n##";
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
        return FunviConsts.LANGUAGE_ID;
    }
}
