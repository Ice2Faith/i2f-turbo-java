package i2f.extension.document.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfConvertUtil {

    /**
     * 提取 PDF 文件的全部文本内容
     *
     * @param pdfFile PDF 文件对象
     * @return 提取出的纯文本内容
     * @throws IOException 如果读取 PDF 或提取文本时发生错误
     */
    public static String extractText(File pdfFile) throws IOException {
        return extractText(pdfFile, 1, Integer.MAX_VALUE);
    }

    /**
     * 提取 PDF 文件指定页码范围的文本内容
     *
     * @param pdfFile   PDF 文件对象
     * @param startPage 起始页码（从 1 开始）
     * @param endPage   结束页码（包含该页，如果超出总页数则自动截断）
     * @return 提取出的纯文本内容
     * @throws IOException 如果读取 PDF 或提取文本时发生错误
     */
    public static String extractText(File pdfFile, int startPage, int endPage) throws IOException {
        // 使用 try-with-resources 确保文档资源自动关闭，防止内存泄漏
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();

            // PDFBox 的页码是从 1 开始的
            stripper.setStartPage(startPage);
            stripper.setEndPage(endPage);

            return stripper.getText(document);
        }
    }

    /**
     * 将 PDF 文件逐页转换为 PNG 图片并保存到指定目录
     *
     * @param pdfFile PDF 文件对象
     * @param saveDir 图片保存目录
     * @return 转换后的图片文件列表
     * @throws IOException 如果读取 PDF 或写入图片时发生错误
     */
    public static List<File> pdf2images(File pdfFile, File saveDir) throws IOException {
        List<File> imageFiles = new ArrayList<>();

        // 1. 检查并创建保存目录
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        // 2. 使用 try-with-resources 确保文档资源在方法结束时自动关闭，防止内存泄漏
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int totalPages = document.getNumberOfPages();

            // 3. 遍历 PDF 的每一页进行渲染
            for (int i = 0; i < totalPages; i++) {
                // 设置 DPI 为 150，兼顾清晰度和文件体积（如需高清可改为 300）
                BufferedImage bim = pdfRenderer.renderImageWithDPI(i, 150);

                // 4. 构建输出文件路径（例如：原文件名_1.png）
                File outputFile = new File(saveDir, "page-" + (i + 1) + ".png");

                // 5. 将图片写入磁盘
                ImageIO.write(bim, "png", outputFile);

                // 6. 将生成的文件添加到返回列表中
                imageFiles.add(outputFile);
            }
        }

        return imageFiles;
    }
}