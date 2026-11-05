---
name: batch-image-compress
description: 批量压缩指定目录下的所有图片到指定大小，支持最大宽高限制，使用二分法保证最佳质量，同名覆盖原文件
version: v1.0
author: Ice2Faith
tags: image,compress,batch,groovy
---

# 批量图片压缩

- 基于 `i2f.image.common.ImageCompressor` 类，递归处理指定目录下的所有图片文件，压缩到指定大小以下

## 前置条件

- 系统已集成 `i2f.image.common.ImageCompressor` 类
- 使用 `groovy_run_script` 工具执行 Groovy 脚本

## 实施步骤

1. 确认用户提供的源目录路径
2. 确认目标压缩大小（KB）
3. 确认是否需要限制最大宽高（可选，<=0 表示不限制）
4. 使用 `groovy_run_script` 工具执行批量压缩脚本

## 执行脚本

使用 `groovy_run_script` 工具执行以下脚本，将参数替换为实际值：

```groovy
import i2f.image.common.ImageCompressor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.FileVisitResult
import java.nio.file.SimpleFileVisitor

// 配置参数（根据实际情况修改）
def sourceDir = "{{源目录路径}}"
def maxKb = {{最大文件大小KB}}
def maxDimension = {{最大边长像素，不限制填0}}

// 检查是否为图片文件
def isImageFile(String fileName) {
    def extensions = ['jpg', 'jpeg', 'png', 'bmp', 'gif', 'tiff', 'tif', 'webp']
    def ext = fileName.toLowerCase().split('\\.')[-1]
    return ext in extensions
}

// 验证源目录
def sourcePath = Paths.get(sourceDir)
if (!Files.exists(sourcePath) || !Files.isDirectory(sourcePath)) {
    logger.println("错误: 源目录不存在或不是目录: ${sourceDir}")
    return [success: false, error: "源目录不存在"]
}

logger.println("==========================================")
logger.println("批量图片压缩工具")
logger.println("==========================================")
logger.println("源目录: ${sourceDir}")
logger.println("目标大小: ${maxKb} KB")
logger.println("最大边长: ${maxDimension > 0 ? maxDimension + ' 像素' : '不限制'}")
logger.println("==========================================")
logger.println("")

// 收集所有图片文件
def imageFiles = []
Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
    @Override
    FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (attrs.isRegularFile() && isImageFile(file.fileName.toString())) {
            imageFiles.add(file)
        }
        return FileVisitResult.CONTINUE
    }
})

if (imageFiles.isEmpty()) {
    logger.println("未找到任何图片文件")
    return [success: true, total: 0, message: "未找到图片文件"]
}

logger.println("找到 ${imageFiles.size()} 个图片文件")
logger.println("")

// 处理每个图片
def successCount = 0
def failCount = 0
def skipCount = 0
def results = []

imageFiles.each { Path imagePath ->
    def fileName = imagePath.fileName.toString()
    def originalSize = Files.size(imagePath) / 1024.0
    
    logger.print("处理: ${imagePath} (${String.format('%.2f', originalSize)} KB) ... ")
    
    try {
        // 创建临时输出文件
        def tempFile = File.createTempFile('compress_', '.jpg')
        tempFile.deleteOnExit()
        
        // 调用压缩方法
        def quality = ImageCompressor.compressImage(
            imagePath.toFile(),
            tempFile,
            maxKb,
            maxDimension
        )
        
        if (quality == -1) {
            // 没有发生变换
            logger.println("跳过 (已满足要求)")
            skipCount++
            tempFile.delete()
            results << [file: imagePath.toString(), status: 'skipped', originalSize: originalSize]
        } else {
            // 发生了压缩，用临时文件替换原文件
            Files.delete(imagePath)
            def newPath = imagePath.parent.resolve(fileName.replaceAll(/\.[^.]+$/, '.jpg'))
            Files.move(tempFile.toPath(), newPath)
            def newSize = Files.size(newPath) / 1024.0
            logger.println("成功 (质量: ${String.format('%.2f', quality)}, 新大小: ${String.format('%.2f', newSize)} KB)")
            successCount++
            results << [file: newPath.toString(), status: 'compressed', originalSize: originalSize, newSize: newSize, quality: quality]
        }
    } catch (Exception e) {
        logger.println("失败: ${e.message}")
        failCount++
        results << [file: imagePath.toString(), status: 'failed', error: e.message]
    }
}

logger.println("")
logger.println("==========================================")
logger.println("处理完成")
logger.println("==========================================")
logger.println("成功: ${successCount}")
logger.println("跳过: ${skipCount}")
logger.println("失败: ${failCount}")
logger.println("总计: ${imageFiles.size()}")
logger.println("==========================================")

return [
    success: true,
    total: imageFiles.size(),
    compressed: successCount,
    skipped: skipCount,
    failed: failCount,
    details: results
]
```

### 参数替换说明

执行前需要将脚本中的以下占位符替换为实际值：

| 占位符 | 说明 | 示例 |
|--------|------|------|
| `{{源目录路径}}` | 要处理的源目录绝对路径 | `/user/images` |
| `{{最大文件大小KB}}` | 压缩后的最大文件大小 | `100` |
| `{{最大边长像素，不限制填0}}` | 最大边长，0表示不限制 | `1920` 或 `0` |

## 支持的图片格式

- JPG / JPEG
- PNG
- BMP
- GIF
- TIFF / TIF
- WEBP

## 输出说明

- 压缩后的文件会覆盖原文件（同名替换）
- 所有输出格式统一为 JPG
- 脚本返回 JSON 格式的处理结果统计

## 注意事项

- 压缩操作会覆盖原文件，建议先备份重要图片
- 如果原图已经小于目标大小，则不会进行任何处理
- 对于透明背景的 PNG 图片，压缩为 JPG 后透明部分会变为白色
