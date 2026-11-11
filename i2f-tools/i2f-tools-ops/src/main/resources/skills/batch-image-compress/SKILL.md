---
name: batch-image-compress
description: 批量压缩指定目录下的所有图片到指定大小，支持最大宽高限制，使用二分法保证最佳质量，输出同名 JPG（原扩展名非 jpg 时替换为 .jpg），支持 dryRun 预演
version: v2.0
author: Ice2Faith
tags: image,compress,batch,groovy
---

# 批量图片压缩

- 基于 `i2f.image.common.ImageCompressor` 类，递归处理指定目录下的所有图片文件，压缩到指定大小以下
- 本版本使用 `groovy_run_script` 的 **`parameters` 传参**，脚本中不再需要替换任何占位符
- 日志全部用 `println` 输出（`Logger` 输出不会出现在工具返回的 `logs` 字段中，已移除）

## 前置条件

- 系统已集成 `i2f.image.common.ImageCompressor`（方法签名：`static double compressImage(File src, File dst, int maxKb, int maxDimension)`，返回 `-1` 表示无需变换）
- 使用 `groovy_run_script` 工具执行 Groovy 脚本

## 参数说明

通过 `groovy_run_script` 的 `parameters` 对象传入，**值可以是字符串或数字**（工具内按字符串传递，脚本内部自行转换）：

| 参数名 | 必填 | 默认值 | 说明 |
|--------|------|--------|------|
| `sourceDir` | 是 | - | 要压缩的图片目录，支持 `~` 开头（会展开为 user.home），递归处理子目录 |
| `maxKb` | 否 | `100` | 压缩后单文件最大大小（KB），必须 > 0 |
| `maxDimension` | 否 | `0` | 最大边长（像素），`0` 或负数表示不限制 |
| `dryRun` | 否 | `false` | 预演模式：只压缩到临时文件并报告压缩效果，**不修改任何原文件**，适合先看效果 |
| `maxDetail` | 否 | `200` | 返回值 `details` 中最多保留多少条明细，避免返回体过大 |

## 实施步骤

1. 向用户确认源目录路径（`sourceDir`）
2. 向用户确认目标大小（`maxKb`）与最大边长（`maxDimension`，可选）
3. 建议**先以 `dryRun=true` 跑一次**，把预估压缩效果给用户看，确认后再正式执行
4. 正式执行时将 `dryRun` 置为 `false`
5. 通过 `groovy_run_script` 调用，`parameters` 传参数，脚本内容原样使用下方代码（无需改动）

调用示例（伪代码）：

```
groovy_run_script(
  script = <下方代码块全文>,
  parameters = { "sourceDir": "/sdcard/DCIM/Camera", "maxKb": "100", "maxDimension": "1920", "dryRun": "true" }
)
```

## 执行脚本

直接原样使用，不要修改变量：

```groovy
import i2f.image.common.ImageCompressor

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.nio.file.FileVisitResult
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

// ==================== 参数读取（来自 groovy_run_script 的 parameters） ====================
def args = (binding.hasVariable('parameters') && parameters) ? parameters : [:]

def strArg = { String key, String defVal = null ->
    def v = args[key]
    return (v == null || v.toString().trim().isEmpty()) ? defVal : v.toString().trim()
}
def intArg = { String key, int defVal ->
    def v = strArg(key, null)
    if (v == null) return defVal
    try { return new BigDecimal(v).intValue() } catch (Exception ignore) { return defVal }
}
def boolArg = { String key, boolean defVal ->
    def v = strArg(key, null)
    if (v == null) return defVal
    return v.equalsIgnoreCase('true') || v == '1' || v.equalsIgnoreCase('yes') || v.equalsIgnoreCase('y')
}
def fmt = { double v -> String.format('%.2f', v) }

def sourceDir = strArg('sourceDir')
def maxKb = intArg('maxKb', 100)
def maxDimension = intArg('maxDimension', 0)
def dryRun = boolArg('dryRun', false)
def maxDetail = intArg('maxDetail', 200)

// ==================== 参数校验 ====================
if (!sourceDir) {
    println "错误: 缺少必填参数 sourceDir"
    println "用法: parameters = {\"sourceDir\":\"/your/images\",\"maxKb\":\"100\",\"maxDimension\":\"0\",\"dryRun\":\"true\"}"
    return [success: false, error: "缺少必填参数 sourceDir"]
}
if (sourceDir.startsWith('~')) {
    sourceDir = System.getProperty('user.home') + sourceDir.substring(1)
}
if (maxKb <= 0) {
    println "错误: maxKb 必须大于 0，当前为 ${maxKb}"
    return [success: false, error: "maxKb 必须大于 0"]
}
if (maxDimension < 0) {
    maxDimension = 0
}
if (maxDetail <= 0) {
    maxDetail = 200
}

def sourcePath = Paths.get(sourceDir).toAbsolutePath().normalize()
if (!Files.exists(sourcePath) || !Files.isDirectory(sourcePath)) {
    println "错误: 源目录不存在或不是目录: ${sourcePath}"
    return [success: false, error: "源目录不存在或不是目录: ${sourcePath}"]
}

def isImageFile = { String fileName ->
    int idx = fileName.lastIndexOf('.')
    if (idx < 0) return false
    def ext = fileName.substring(idx + 1).toLowerCase()
    return ext in ['jpg', 'jpeg', 'png', 'bmp', 'gif', 'tiff', 'tif', 'webp']
}

println "=========================================="
println "批量图片压缩工具 v2.0"
println "=========================================="
println "源目录  : ${sourcePath}"
println "目标大小: ${maxKb} KB"
println "最大边长: ${maxDimension > 0 ? maxDimension + ' 像素' : '不限制'}"
println "预演模式: ${dryRun ? '是（不修改原文件）' : '否（会覆盖原文件）'}"
println "=========================================="

// ==================== 收集图片 ====================
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
imageFiles.sort { it.toString() }

if (imageFiles.isEmpty()) {
    println "未找到任何图片文件"
    return [success: true, total: 0, message: "未找到图片文件", sourceDir: sourcePath.toString()]
}

println "找到 ${imageFiles.size()} 个图片文件"
println ""

// ==================== 逐个处理 ====================
int doneCount = 0
int skipCount = 0
int failCount = 0
long beforeBytes = 0L
long afterBytes = 0L
def details = []

for (Path imagePath : imageFiles) {
    String fileName = imagePath.fileName.toString()
    long originalBytes = 0L
    try {
        originalBytes = Files.size(imagePath)
    } catch (Exception e) {
        println "[失败] ${imagePath} : 读取大小失败 ${e.message}"
        failCount++
        details << [file: imagePath.toString(), status: 'failed', error: "读取大小失败: ${e.message}"]
        continue
    }
    double originalKb = originalBytes / 1024.0
    beforeBytes += originalBytes

    // 已满足目标大小且无需限边 → 直接跳过，避免无谓解码/重编码
    if (maxDimension <= 0 && originalKb <= maxKb) {
        println "[跳过] ${fileName} (${fmt(originalKb)} KB) 已小于目标 ${maxKb} KB"
        skipCount++
        afterBytes += originalBytes
        details << [file: imagePath.toString(), status: 'skipped', originalKb: originalKb, reason: 'already-small-enough']
        continue
    }

    Path tempPath = null
    try {
        // 临时文件放在同目录，便于后续 move 走同分区快速替换
        tempPath = Files.createTempFile(imagePath.parent, '.compress-', '.jpg')

        double quality = ImageCompressor.compressImage(imagePath.toFile(), tempPath.toFile(), maxKb, maxDimension)

        if (quality < 0) {
            println "[跳过] ${fileName} (${fmt(originalKb)} KB) 无需变换"
            skipCount++
            afterBytes += originalBytes
            details << [file: imagePath.toString(), status: 'skipped', originalKb: originalKb, reason: 'no-transform-needed']
            continue
        }

        long newBytes = Files.size(tempPath)
        double newKb = newBytes / 1024.0

        // 计算目标路径：统一输出 .jpg，基名保持不变
        Path targetPath = imagePath
        String lower = fileName.toLowerCase()
        if (!(lower.endsWith('.jpg') || lower.endsWith('.jpeg'))) {
            String baseName = fileName.replaceAll(/(?i)\.[^.]+$/, '')
            Path candidate = imagePath.parent.resolve(baseName + '.jpg')
            if (Files.exists(candidate) && !candidate.toAbsolutePath().equals(imagePath.toAbsolutePath())) {
                int idx = 1
                Path alt = null
                do {
                    alt = imagePath.parent.resolve(baseName + '_c' + idx + '.jpg')
                    idx++
                } while (Files.exists(alt))
                candidate = alt
            }
            targetPath = candidate
        }

        if (dryRun) {
            doneCount++
            afterBytes += newBytes
            println "[预演] ${fileName} : ${fmt(originalKb)} KB -> ${fmt(newKb)} KB , 质量 ${fmt(quality)}${targetPath.equals(imagePath) ? '' : ' , 将另存为 ' + targetPath.fileName}"
            details << [
                file      : imagePath.toString(),
                status    : 'dry-run',
                originalKb: originalKb,
                newKb     : newKb,
                quality   : quality,
                target    : targetPath.toString()
            ]
        } else {
            if (targetPath.toAbsolutePath().equals(imagePath.toAbsolutePath())) {
                // 同名 .jpg：直接原子替换
                Files.move(tempPath, imagePath, StandardCopyOption.REPLACE_EXISTING)
                tempPath = null
            } else {
                // 扩展名变化：先落地新文件，再删原文件
                Files.move(tempPath, targetPath, StandardCopyOption.REPLACE_EXISTING)
                tempPath = null
                Files.deleteIfExists(imagePath)
            }
            long finalBytes = Files.size(targetPath)
            afterBytes += finalBytes
            doneCount++
            println "[压缩] ${fileName} : ${fmt(originalKb)} KB -> ${fmt(finalBytes / 1024.0)} KB , 质量 ${fmt(quality)} -> ${targetPath.fileName}"
            details << [
                file      : targetPath.toString(),
                status    : 'compressed',
                originalKb: originalKb,
                newKb     : finalBytes / 1024.0,
                quality   : quality
            ]
        }
    } catch (Throwable e) {
        println "[失败] ${fileName} : ${e.getClass().simpleName}: ${e.message}"
        failCount++
        afterBytes += originalBytes
        details << [file: imagePath.toString(), status: 'failed', error: "${e.getClass().simpleName}: ${e.message}"]
    } finally {
        if (tempPath != null) {
            try {
                Files.deleteIfExists(tempPath)
            } catch (Throwable ignore) {
            }
        }
    }
}

// ==================== 汇总 ====================
double beforeTotalKb = beforeBytes / 1024.0
double afterTotalKb = afterBytes / 1024.0
double savedKb = beforeTotalKb - afterTotalKb

println ""
println "=========================================="
println "处理完成${dryRun ? '（预演，原文件未改动）' : ''}"
println "=========================================="
println "总计  : ${imageFiles.size()}"
println "已处理: ${doneCount}"
println "跳过  : ${skipCount}"
println "失败  : ${failCount}"
println "体积  : ${fmt(beforeTotalKb)} KB -> ${fmt(afterTotalKb)} KB , 节省 ${fmt(savedKb)} KB" +
        (beforeTotalKb > 0 ? " (${fmt(savedKb / beforeTotalKb * 100)}%)" : "")
if (failCount > 0) {
    println "注意  : 有 ${failCount} 个文件处理失败，详见返回结果 details"
}
println "=========================================="

return [
    success         : failCount == 0,
    sourceDir       : sourcePath.toString(),
    dryRun          : dryRun,
    maxKb           : maxKb,
    maxDimension    : maxDimension,
    total           : imageFiles.size(),
    compressed      : doneCount,
    skipped         : skipCount,
    failed          : failCount,
    beforeKb        : Math.round(beforeTotalKb * 100) / 100.0,
    afterKb         : Math.round(afterTotalKb * 100) / 100.0,
    savedKb         : Math.round(savedKb * 100) / 100.0,
    details         : details.size() > maxDetail ? details.subList(0, maxDetail) : details,
    detailsTruncated: details.size() > maxDetail
]
```

## 支持的图片格式

- JPG / JPEG（原样同名覆盖）
- PNG / BMP / GIF / TIFF / TIF / WEBP（压缩后输出同名 `.jpg`，原文件删除）

## 输出说明

- 输出格式统一为 **JPG**，基名保持不变
- `.jpg` / `.jpeg` 文件：同名原子替换原文件
- 其他格式：生成 `基名.jpg` 后删除原文件；若已存在同名 jpg，则使用 `基名_c1.jpg`、`基名_c2.jpg` ... 避免覆盖
- 脚本返回 JSON 统计结果（`returns` 字段），过程日志在 `logs` 字段（由 `println` 产生）
- `dryRun=true` 时不会修改任何原文件，只报告预估压缩效果

## 注意事项

- 压缩操作会覆盖原文件，**建议先备份重要图片**，或先跑一次 `dryRun=true`
- 已小于目标大小且未限制边长的文件会被直接跳过，不做任何改动（保留原始格式，避免 PNG 透明背景丢失）
- GIF 动图会变成单帧静态 JPG；如目录内有动图，执行前应提醒用户
- 透明背景的 PNG 压缩为 JPG 后透明部分会变为白色
- 临时文件以 `.compress-` 前缀创建在图片同目录，脚本结束会清理；如异常中断可手动清理 `.compress-*.jpg`