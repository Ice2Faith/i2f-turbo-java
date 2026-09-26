package i2f.springboot.ops.openai.controller;

import i2f.io.stream.StreamUtil;
import i2f.os.OsUtil;
import i2f.resources.ResourceUtil;
import i2f.resp.ApiResp;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @author Ice2Faith
 * @date 2026/8/26 9:08
 * @desc
 */
public class OfficeFormatUtil {
    /**
     * ConvertOfficeFile 跨平台兼容的 Office 旧版转新版函数
     * 支持 .doc -> .docx, .xls -> .xlsx, .ppt -> .pptx
     * 如果目标文件已存在，则跳过转换直接返回
     *
     * @param inputFilePath
     * @param disableOfficeDom
     * @return
     */
    public static ApiResp<File> convertOfficeFile(File inputFilePath, boolean disableOfficeDom) {
        try {
            inputFilePath = new File(inputFilePath.getAbsolutePath());

            File inputDir = inputFilePath.getParentFile();
            String inputBase = inputFilePath.getName();
            String nameOnly = inputBase;
            String ext = "";
            int idx = inputBase.lastIndexOf(".");
            if (idx >= 0) {
                nameOnly = inputBase.substring(0, idx);
                ext = inputBase.substring(idx).toLowerCase();
            }

            String targetFormat = "";
            if (Arrays.asList(
                    ".doc", ".dot", ".dotx", ".dotm", ".rtf",
                    ".wps", ".wpt", ".odt", ".ott", ".fodt", ".epub"
            ).contains(ext)) {
                targetFormat = "docx";
            } else if (Arrays.asList(
                    ".xls", ".xlsm", ".xlt", ".xltm", ".tsv",
                    ".ods", ".ots", ".et", ".ett"
            ).contains(ext)) {
                targetFormat = "xlsx";
            } else if (Arrays.asList(
                    ".ppt", ".pps", ".dps", ".odp", ".otp", ".ppsx"
            ).contains(ext)) {
                targetFormat = "pptx";
            } else {
                return ApiResp.error(String.format("un-support office input format: %s", ext));
            }

            File outputDir = new File(inputDir, ".converted");
            String outputFileName = nameOnly + "." + targetFormat;
            File finalOutputPath = new File(outputDir, outputFileName);

            if (finalOutputPath.exists()) {
                System.out.println("jump convert, output file already exists: " + finalOutputPath);
                return ApiResp.success(finalOutputPath);
            }

            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            if (OsUtil.isWindows()) {
                if (!disableOfficeDom) {
                    // 尝试使用 office
                    if (!finalOutputPath.exists()) {
                        System.out.println("try convert by windows office com: " + inputFilePath);
                        convertOfficeFileByWindowsOfficeCom(inputFilePath, finalOutputPath, targetFormat);
                    }
                    // 尝试使用 wps
                    if (!finalOutputPath.exists()) {
                        System.out.println("try convert by windows wps com: " + inputFilePath);
                        convertOfficeFileByWindowsWpsCom(inputFilePath, finalOutputPath, targetFormat);
                    }
                }
            }

            // 使用 libreoffice 进行转换
            if (!finalOutputPath.exists()) {
                System.out.println("try convert by libreoffice: " + inputFilePath);
                // 6. 获取 LibreOffice 路径并构建命令
                String loPath = findLibreOfficePath();

                String[] cmdArr = {
                        loPath,
                        "--headless",
                        "--convert-to",
                        targetFormat,
                        inputFilePath.getAbsolutePath(),
                        "--outdir", outputDir.getAbsolutePath()
                };
                String output = OsUtil.execCmd(true, 60, cmdArr, null, inputDir, null);

                if (!finalOutputPath.exists()) {
                    System.out.println("exec command is: " + Arrays.toString(cmdArr));
                    System.out.println("LibreOffice convert failure, error message is: " + output);
                }
            }

            if (!finalOutputPath.exists()) {
                return ApiResp.error("convert command exec success, but not found output file: " + finalOutputPath);
            }

            System.out.println("convert success, output file is: " + finalOutputPath);

            return ApiResp.success(finalOutputPath);
        } catch (Throwable e) {
            e.printStackTrace();
            return ApiResp.error("convert exception occurred: " + e.getClass() + ": " + e.getMessage());
        }
    }

    public static ApiResp<File> convertOfficeFileByWindowsOfficeCom(File inputFilePath, File outputFilePath, String targetFormat) {
        try {
            String srcScriptPath = "assets/script/office_to_" + targetFormat + ".vbs";
            File dstScriptPath = new File(".\\assets\\script\\office_to_" + targetFormat + ".vbs");
            if (!dstScriptPath.exists()) {
                extractAssetFile(srcScriptPath, dstScriptPath);
            }
            if (!dstScriptPath.exists()) {
                return ApiResp.error("release asset script file error");
            }
            File absInputPath = new File(inputFilePath.getAbsolutePath());
            File absOutputPath = new File(outputFilePath.getAbsolutePath());

            String[] cmdArr = {
                    "cscript",
                    dstScriptPath.getAbsolutePath(),
                    absInputPath.getAbsolutePath(),
                    absOutputPath.getAbsolutePath(),
            };
            String output = OsUtil.execCmd(true, 60, cmdArr, null, absInputPath.getParentFile(), null);
            if (!absOutputPath.exists()) {
                System.out.println("exec command is: " + String.join(" ", cmdArr));
                System.out.println("office com script convert failure, error message is: " + output);
            }
            if (!absOutputPath.exists()) {
                return ApiResp.error("office com script command exec success, but not found output file: " + outputFilePath);
            }

            return ApiResp.success(outputFilePath);
        } catch (Throwable e) {
            e.printStackTrace();
            return ApiResp.error("convert exception occurred: " + e.getClass() + ": " + e.getMessage());
        }
    }

    public static ApiResp<File> convertOfficeFileByWindowsWpsCom(File inputFilePath, File outputFilePath, String targetFormat) {
        try {
            String srcScriptPath = "assets/script/wps_to_" + targetFormat + ".vbs";
            File dstScriptPath = new File(".\\assets\\script\\wps_to_" + targetFormat + ".vbs");
            if (!dstScriptPath.exists()) {
                extractAssetFile(srcScriptPath, dstScriptPath);
            }
            if (!dstScriptPath.exists()) {
                return ApiResp.error("release asset script file error");
            }
            File absInputPath = new File(inputFilePath.getAbsolutePath());
            File absOutputPath = new File(outputFilePath.getAbsolutePath());

            String[] cmdArr = {
                    "cscript",
                    dstScriptPath.getAbsolutePath(),
                    absInputPath.getAbsolutePath(),
                    absOutputPath.getAbsolutePath(),
            };
            String output = OsUtil.execCmd(true, 60, cmdArr, null, absInputPath.getParentFile(), null);
            if (!absOutputPath.exists()) {
                System.out.println("exec command is: " + String.join(" ", cmdArr));
                System.out.println("wps com script convert failure, error message is: " + output);
            }
            if (!absOutputPath.exists()) {
                return ApiResp.error("wps com script command exec success, but not found output file: " + outputFilePath);
            }

            return ApiResp.success(outputFilePath);
        } catch (Throwable e) {
            e.printStackTrace();
            return ApiResp.error("convert exception occurred: " + e.getClass() + ": " + e.getMessage());
        }
    }

    public static void extractAssetFile(String embedPath, File localPath) throws Exception {
        localPath = new File(localPath.getAbsolutePath());
        if (localPath.getParentFile().exists()) {
            localPath.getParentFile().mkdirs();
        }

        InputStream is = ResourceUtil.getClasspathResourceAsStream(embedPath);
        StreamUtil.writeBytes(is, localPath);
    }

    private static volatile String cacheLibreOfficePath = null;

    // FindLibreOfficePath 动态探测 LibreOffice 的路径
    public static String findLibreOfficePath() {
        if (cacheLibreOfficePath != null && !cacheLibreOfficePath.isEmpty()) {
            return cacheLibreOfficePath;
        }
        cacheLibreOfficePath = findLibreOfficePath0();
        return cacheLibreOfficePath;
    }

    public static String findLibreOfficePath0() {
        String envPath = System.getenv("LIBREOFFICE_PATH");
        if (envPath != null && !envPath.isEmpty()) {
            File dir = new File(envPath);
            File file = new File(dir, "soffice.exe");
            if (file.exists()) {
                return file.getAbsolutePath();
            }
            file = new File(dir, "program");
            file = new File(file, "soffice.exe");
            if (file.exists()) {
                return file.getAbsolutePath();
            }
        }

        if (!OsUtil.isWindows()) {
            return "libreoffice";
        }

        String path = getLibreOfficePathFromRegistry();
        if (path != null && !path.isEmpty()) {
            return path;
        }

        String[] commonPaths = {
                "Program Files\\LibreOffice\\program\\soffice.exe",
                "Program Files (x86)\\LibreOffice\\program\\soffice.exe",
                "Program Files\\LibreOffice\\soffice.exe",
                "Program Files (x86)\\LibreOffice\\soffice.exe"
        };

        String[] drivers = {
                "C:\\", "D:\\", "E:\\", "F:\\", "G:\\"
        };

        for (String drive : drivers) {
            for (String relativePath : commonPaths) {
                File file = new File(drive);
                file = new File(file, relativePath);
                if (file.exists()) {
                    return file.getAbsolutePath();
                }
            }
        }

        return "soffice.exe";
    }

    public static String getLibreOfficePathFromRegistry() {
        String[] keys = {
                "HKEY_LOCAL_MACHINE\\SOFTWARE\\LibreOffice\\UNO\\InstallPath",
                "HKEY_CURRENT_USER\\SOFTWARE\\LibreOffice\\UNO\\InstallPath",
        };

        for (String key : keys) {
            String output = OsUtil.execCmd(true, 60, new String[]{
                    "reg", "query", key, "/ve"
            }, null, null, null);
            if (output == null || output.isEmpty()) {
                continue;
            }

            String path = parseRegistryOutput(output);
            if (path != null && !path.isEmpty()) {
                File dir = new File(path);
                File file = new File(dir, "soffice.exe");
                if (file.exists()) {
                    return file.getAbsolutePath();
                }
                file = new File(dir, "program");
                file = new File(file, "soffice.exe");
                if (file.exists()) {
                    return file.getAbsolutePath();
                }
            }
        }

        return null;
    }

    public static String parseRegistryOutput(String output) {
        String[] lines = output.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.contains("REG_SZ")) {
                String[] parts = line.split("REG_SZ");
                if (parts.length >= 2) {
                    String path = parts[1].trim();
                    if (!path.isEmpty()) {
                        return path;
                    }
                }
            }
        }
        return null;
    }
}
