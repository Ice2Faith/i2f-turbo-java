package i2f.image.common;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * @author Ice2Faith
 * @date 2026/8/6 9:54
 * @desc 图片压缩到指定大小
 * 主要用途：用于视觉模型进行识别
 */
public class ImageCompressor {

    /**
     * 将图片压缩到指定大小（KB）以下的 JPG 格式
     * 因此，输出的格式一定是jpg格式
     *
     * @param inputFile    原始图片文件
     * @param outputFile   压缩之后保存的文件
     * @param maxKbSize    最大的输出文件大小（KB）
     * @param maxDimension 最大边的大小，如果为<=0则不进行大小缩放，否则等比例缩放到该范围内
     * @return 如果发生了压缩变换，则返回压缩质量[0.0,1.0] ，如果没有发生变换，则返回-1。当发生变换，才会存储到 outputFile, 否则还是源文件
     */
    public static double compressImage(File inputFile, File outputFile, int maxKbSize, int maxDimension) throws IOException {

        boolean sizeOk = false;
        if (inputFile.length() / 1024.0 <= maxKbSize) {
            sizeOk = true;
        }
        if (sizeOk) {
            if (maxDimension <= 0) {
                return -1;
            }
        }

        BufferedImage originalImage = ImageIO.read(inputFile);

        if (originalImage == null) {
            throw new IOException("cannot read image file(maybe format not support): " + inputFile.getAbsolutePath());
        }

        if (sizeOk) {
            if (maxDimension > 0) {
                if (originalImage.getWidth() <= maxDimension && originalImage.getHeight() <= maxDimension) {
                    return -1;
                }
            }
        }

        // 1. 检查并缩放尺寸
        BufferedImage processedImage = resizeIfNeeded(originalImage, maxDimension);

        // 2. 确保图片是 RGB 模式（JPEG 不支持带 Alpha 通道的 ARGB）
        processedImage = removeAlphaIfNeeded(processedImage);

        // 3. 获取 JPEG 的 ImageWriter
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("cannot found JPEG ImageWriter");
        }
        ImageWriter writer = writers.next();

        // 4. 使用二分法寻找最佳压缩质量
        float minQuality = 0.0f;
        float maxQuality = 1.0f;
        float bestQuality = 0.5f;
        byte[] bestBytes = null;

        long targetSizeBytes = maxKbSize * 1024L;

        for (int i = 0; i < 10; i++) {
            bestQuality = (minQuality + maxQuality) / 2.0f;
            byte[] compressedBytes = compressToBytes(processedImage, writer, bestQuality);

            if (compressedBytes.length <= targetSizeBytes) {
                bestBytes = compressedBytes;
                minQuality = bestQuality; // 尝试提高质量
            } else {
                maxQuality = bestQuality; // 降低质量
            }

            if (Math.abs(maxQuality - minQuality) < 0.01f) {
                break;
            }
        }

        // 5. 写入文件
        if (bestBytes != null) {
            try (OutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(bestBytes);
            }
        }

        return bestQuality;
    }

    /**
     * 检查图片尺寸，如果任意边超过 maxDimension，则等比例缩放
     *
     * @param maxDimension 最大边长度，如果 <= 0 则表示不进行缩放
     */
    public static BufferedImage resizeIfNeeded(BufferedImage image, int maxDimension) {
        // 如果没有限制最大宽高，直接返回
        if (maxDimension <= 0) {
            return image;
        }

        int width = image.getWidth();
        int height = image.getHeight();

        // 如果宽和高都在限制范围内，直接返回原图
        if (width <= maxDimension && height <= maxDimension) {
            return image;
        }

        // 计算缩放比例
        double scale = 1.0;
        if (width > height) {
            scale = (double) maxDimension / width;
        } else {
            scale = (double) maxDimension / height;
        }

        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);

        // 使用 AffineTransformOp 进行高质量缩放 (双线性插值)
        AffineTransform transform = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

        // 创建目标图片并缩放
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, image.getType() == 0 ? BufferedImage.TYPE_INT_RGB : image.getType());
        op.filter(image, resizedImage);

        return resizedImage;
    }

    /**
     * 确保图片是 RGB 模式（JPEG 不支持带 Alpha 通道的 ARGB）
     * 如果有alpha通道，则进行重绘为不带alpha通道的图片返回
     */
    public static BufferedImage removeAlphaIfNeeded(BufferedImage processedImage) {
        if (processedImage.getColorModel().hasAlpha()) {
            BufferedImage rgbImage = new BufferedImage(
                    processedImage.getWidth(),
                    processedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );
            Graphics2D g = rgbImage.createGraphics();
            g.drawImage(processedImage, 0, 0, Color.WHITE, null);
            g.dispose();
            processedImage = rgbImage;
        }
        return processedImage;
    }

    /**
     * 根据指定的质量参数将 BufferedImage 压缩为字节数组
     */
    public static byte[] compressToBytes(BufferedImage image, ImageWriter writer, float quality) throws IOException {
        ImageWriteParam param = writer.getDefaultWriteParam();

        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {

            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), param);
            return baos.toByteArray();
        }
    }

    /**
     * 将图片文件转换为 base64 的 data url
     *
     * @param file 图片文件
     * @return data url
     * @throws IOException
     */
    public static String imageFileToBase64DataUrl(File file) throws IOException {
        long len = file.length();
        byte[] buff = new byte[(int) len];
        try (FileInputStream is = new FileInputStream(file)) {
            is.read(buff);
        }
        String name = file.getName();
        String suffix = "";
        int idx = name.lastIndexOf(".");
        if (idx >= 0) {
            suffix = name.substring(idx).toLowerCase();
        }
        String mimeType = "image/jpeg";
        if (".png".equals(suffix)) {
            mimeType = "image/png";
        } else if (".jpg".equals(suffix)) {
            mimeType = "image/jpeg";
        } else if (".jpeg".equals(suffix)) {
            mimeType = "image/jpeg";
        } else if (".jpe".equals(suffix)) {
            mimeType = "image/jpeg";
        } else if (".bmp".equals(suffix)) {
            mimeType = "image/bmp";
        } else if (".webp".equals(suffix)) {
            mimeType = "image/webp";
        }
        String base64 = java.util.Base64.getEncoder().encodeToString(buff);
        return "data:" + mimeType + ";base64," + base64;
    }

    public static void main(String[] args) throws Exception {
        String input = "./f1.png";   // 替换为你的原图路径
        String output = "./f1-out.jpg"; // 替换为输出路径
        int maxSizeKb = 128;          // 目标大小 128KB
        int maxDimension = 2080;

        double bestQuality = compressImage(new File(input), new File(output), maxSizeKb, maxDimension);
        System.out.println("图像质量："+bestQuality);
        if (bestQuality >= 0) {
            System.out.println("处理成功：" + output);
        } else {
            System.out.println("不需处理：" + input);
        }
        File imageFile = bestQuality >= 0 ? new File(output) : new File(input);
        String dataUrl = imageFileToBase64DataUrl(imageFile);
        System.out.println(dataUrl);
    }
}
