package i2f.springboot.ops.openai.tool.impl;

import i2f.ai.std.mcp.server.McpServerExpose;
import i2f.ai.std.tags.AiTags;
import i2f.ai.std.tool.annotations.Tool;
import i2f.ai.std.tool.annotations.ToolParam;
import i2f.ai.std.tool.annotations.Tools;
import i2f.ai.std.tool.intent.ToolIntent;
import i2f.ai.std.tool.intent.ToolIntentItem;
import i2f.io.file.FileUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2026/9/30 14:10
 * @desc
 */
@McpServerExpose
@ToolIntent(items = @ToolIntentItem(value = "skill_manage", description = "提供技能的管理（安装、卸载、复制）能力"))
@ConditionalOnExpression("${ai.tools.skill-manage.enable:true}")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Tools(tags = {
        "skill_manage"
})
public class SkillManageTools {

    @Autowired(required = false)
    private LocalFileTools localFileTools;

    @Tool(tags = {
            AiTags.WRITABLE_VALUE
    },
            description = "install skill from local path what sub-file contains `SKILL.md` file.\n" + LocalFileTools.PATH_PROMPT
    )
    public Map<String, Object> skill_install(@ToolParam(value = "localPath", description = "the file localPath what sub-file contains `SKILL.md` file, for example 'frontend_develop' ")
                                             String localPath,
                                             @ToolParam(value = "forceCover", description = "force cover target skill if it exists, default is `false` means not cover and throws error ")
                                             boolean forceCover) throws IOException {
        if (localFileTools == null) {
            throw new IllegalStateException("current system not enable local-file access policy");
        }

        File srcFile = localFileTools.getFile(localPath);
        if (!srcFile.exists()) {
            throw new IllegalStateException("source path not exists: " + srcFile.getAbsolutePath());
        }
        if (!srcFile.isDirectory()) {
            throw new IllegalStateException("source path not is a directory: " + srcFile.getAbsolutePath());
        }
        File[] list = srcFile.listFiles();
        if (list == null || list.length == 0) {
            throw new IllegalStateException("source path is a empty dir: " + srcFile.getAbsolutePath());
        }
        File indexFile = null;
        for (File item : list) {
            if ("SKILL.md".equals(item.getName())) {
                indexFile = item;
                break;
            }
        }
        if (indexFile == null) {
            throw new IllegalStateException("source path not contains sub-file `SKILL.md`: " + srcFile.getAbsolutePath());
        }
        File skillsDir = new File("./skills");
        if (!skillsDir.exists()) {
            skillsDir.mkdirs();
        }
        File installDir = new File(skillsDir, srcFile.getName());
        if (!forceCover) {
            if (installDir.exists()) {
                list = srcFile.listFiles();
                if (list != null && list.length > 0) {
                    throw new IllegalStateException("cannot install skill, because target skill has exists");
                }
            }
        }
        installDir.mkdirs();

        FileUtil.copy(installDir, srcFile);

        Map<String, Object> ret = new HashMap<>();
        ret.put("result", "install success");
        ret.put("hint", "skill will auto-loaded on after short time.");

        return ret;
    }

    @Tool(tags = {
            AiTags.WRITABLE_VALUE
    },
            description = "uninstall skill.\n" + LocalFileTools.PATH_PROMPT
    )
    public Map<String, Object> skill_uninstall(@ToolParam(value = "skillName", description = "the skill name, for example 'frontend_develop' ")
                                               String skillName) throws IOException {
        File skillsDir = new File("./skills");

        File dstDir = new File(skillsDir, skillName);
        if (!dstDir.exists()) {
            throw new IllegalStateException("skill not exists!");
        }

        FileUtil.moveToTrash(dstDir);

        Map<String, Object> ret = new HashMap<>();
        ret.put("result", "success uninstall");

        return ret;
    }

    @Tool(tags = {
            AiTags.WRITABLE_VALUE
    },
            description = "extract skill files to local path.\n" + LocalFileTools.PATH_PROMPT
    )
    public Map<String, Object> skill_extract(@ToolParam(value = "skillName", description = "the skill name, for example 'frontend_develop' ")
                                             String skillName,
                                             @ToolParam(value = "localPath", description = "the file localPath, for example 'frontend_develop' ")
                                             String localPath) throws IOException {
        File skillsDir = new File("./skills");

        File srcDir = new File(skillsDir, skillName);
        if (!srcDir.exists()) {
            throw new IllegalStateException("skill not exists!");
        }

        if (localFileTools == null) {
            throw new IllegalStateException("current system not enable local-file access policy");
        }

        File dstDir = localFileTools.getFile(localPath);
        if (!dstDir.exists()) {
            dstDir.mkdirs();
        }

        FileUtil.copy(dstDir, srcDir);

        Map<String, Object> ret = new HashMap<>();
        ret.put("result", "skill files has extract to " + dstDir.getAbsolutePath());

        return ret;
    }
}
