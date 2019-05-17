package com.sample;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.encoding.Encoding;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;
import java.io.InputStream;

public class SampleApplication {
    private static final float FONT_SIZE = 12.0f;
    private static final PDFont DEFAULT_ENG_FONT = PDType1Font.HELVETICA;

    public static void main(String[] args) {
        PDDocument document = new PDDocument();

        try {
            InputStream fontIS = SampleApplication.class.getResourceAsStream("/SourceHanSerifCN-Regular.ttf");
            PDFont nonEngFont = PDType0Font.load(document, fontIS);

            PDPage firstPage = new PDPage(PDRectangle.A4);
            document.addPage(firstPage);

            addImage(document, firstPage, "/img_placeholder.png", 30, 40);
            writeText(document, firstPage, "=== HELLO WORLD ===", 100, 0, DEFAULT_ENG_FONT);
            writeText(document, firstPage, "接着夜幕降临，第一颗流星划破了星空。", 0, 0, nonEngFont);
            writeText(document, firstPage, "接著夜幕降臨，第一顆流星劃破了星空。", 50, 0, nonEngFont);

            document.save(SampleApplication.class.getResource("").getPath() + "/sample.pdf");
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeText(PDDocument pdf, PDPage page, String content, float x, float y, PDFont font) throws IOException {
        float stringHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() * FONT_SIZE / 1000f;
        PDRectangle pageSize = page.getMediaBox();

        PDPageContentStream pdfContent = new PDPageContentStream(pdf, page, PDPageContentStream.AppendMode.APPEND, false);
        pdfContent.setFont(font, FONT_SIZE);
        pdfContent.beginText();
        pdfContent.setTextMatrix(Matrix.getTranslateInstance(x, pageSize.getHeight() - stringHeight + y));
        pdfContent.showText(content);
        pdfContent.endText();
        pdfContent.close();
    }

    private static void addImage(PDDocument pdf, PDPage page, String imagePath, float x, float y) throws IOException {
        PDImageXObject imageXObject = PDImageXObject.createFromFile(SampleApplication.class.getResource(imagePath).getPath(), pdf);
        PDRectangle pageSize = page.getMediaBox();

        PDPageContentStream pdfContent = new PDPageContentStream(pdf, page, PDPageContentStream.AppendMode.APPEND, false);
        pdfContent.drawImage(imageXObject, x, pageSize.getHeight() - imageXObject.getHeight() - y);
        pdfContent.close();
    }

}
