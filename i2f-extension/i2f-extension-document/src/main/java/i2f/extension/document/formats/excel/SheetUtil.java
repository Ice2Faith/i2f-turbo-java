package i2f.extension.document.formats.excel;

import com.aspose.cells.License;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import i2f.io.file.FileUtil;
import i2f.io.stream.StreamUtil;

import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2023/5/25 14:24
 * @desc
 */
public class SheetUtil {
    private static AtomicBoolean hasLicence = new AtomicBoolean(false);

    public static void licence() {
        if (hasLicence.get()) {
            return;
        }
        try {
            License license = new License();
            InputStream is = new ByteArrayInputStream(LICENSE_TEXT.getBytes());
            license.setLicense(is);
            hasLicence.set(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void convert(File excelFile, SheetFormat saveFormat, File outFile) throws Exception {
        convert(excelFile, saveFormat.code(), outFile);
    }

    public static void convert(File excelFile, int saveFormat, File outFile) throws Exception {
        licence();
        Workbook workbook = new Workbook(excelFile.getAbsolutePath());
        if (saveFormat == SaveFormat.PDF) {
            PdfSaveOptions options = new PdfSaveOptions();
            options.setAllColumnsInOnePagePerSheet(true);
            workbook.save(outFile.getAbsolutePath(), options);
        } else {
            workbook.save(outFile.getAbsolutePath(), saveFormat);
        }
    }

    public static void convert(InputStream excelIs, SheetFormat saveFormat, OutputStream outOs) throws Exception {
        convert(excelIs, saveFormat.code(), outOs);
    }

    public static void convert(InputStream excelIs, int saveFormat, OutputStream outOs) throws Exception {
        File excelFile = FileUtil.getTempFile();
        FileUtil.save(excelIs, excelFile);

        File outFile = FileUtil.getTempFile();
        convert(excelFile, saveFormat, outFile);

        FileInputStream fis = new FileInputStream(outFile);
        StreamUtil.streamCopy(fis, outOs, false, true);

        excelFile.delete();
        outFile.delete();
    }

    public static void excel2pdf(File excelFile, File outFile) throws Exception {
        convert(excelFile, SaveFormat.PDF, outFile);
    }

    public static void excel2csv(File excelFile, File outFile) throws Exception {
        convert(excelFile, SaveFormat.CSV, outFile);
    }

    public static void excel2tiff(File excelFile, File outFile) throws Exception {
        convert(excelFile, SaveFormat.TIFF, outFile);
    }

    public static void excel2pdf(InputStream excelIs, OutputStream outOs) throws Exception {
        convert(excelIs, SaveFormat.PDF, outOs);
    }

    public static void excel2csv(InputStream excelIs, OutputStream outOs) throws Exception {
        convert(excelIs, SaveFormat.CSV, outOs);
    }

    public static void excel2tiff(InputStream excelIs, OutputStream outOs) throws Exception {
        convert(excelIs, SaveFormat.TIFF, outOs);
    }

    public static final String LICENSE_TEXT = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<License>\n" +
            "    <Data>\n" +
            "        <Products>\n" +
            "            <Product>Aspose.Total for Java</Product>\n" +
            "            <Product>Aspose.Words for Java</Product>\n" +
            "        </Products>\n" +
            "        <EditionType>Enterprise</EditionType>\n" +
            "        <SubscriptionExpiry>20991231</SubscriptionExpiry>\n" +
            "        <LicenseExpiry>20991231</LicenseExpiry>\n" +
            "        <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n" +
            "    </Data>\n" +
            "    <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\n" +
            "</License>";
}
