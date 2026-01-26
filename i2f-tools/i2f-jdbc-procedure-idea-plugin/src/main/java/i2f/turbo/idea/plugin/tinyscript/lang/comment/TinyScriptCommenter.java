package i2f.turbo.idea.plugin.tinyscript.lang.comment;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ice2Faith
 * @date 2026/1/25 22:56
 * @desc
 */
public class TinyScriptCommenter implements Commenter {

    @Override
    public String getLineCommentPrefix() {
        return "//";
    }

    @Override
    @Nullable
    public String getBlockCommentPrefix() {
        return "/*";
    }

    @Override
    @Nullable
    public String getBlockCommentSuffix() {
        return "*/";
    }

    @Override
    @Nullable
    public String getCommentedBlockCommentPrefix() {
        return null; // 通常不需要，除非嵌套注释有特殊处理
    }

    @Override
    @Nullable
    public String getCommentedBlockCommentSuffix() {
        return null;
    }
}
