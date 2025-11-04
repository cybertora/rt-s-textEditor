package com.texteditor.core.document;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

public class PdfDocument implements Document {

    private PDDocument document;
    private PDPageContentStream contentStream;
    private PDPage page;
    private float yPosition = 750;
    private final float margin = 50;
    private final float lineHeight = 14;

    @Override
    public void create() throws IOException {
        document = new PDDocument();
        page = new PDPage();
        document.addPage(page);
        contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(margin, yPosition);
    }

    @Override
    public void addText(String text) throws IOException {
        contentStream.showText(text);
        contentStream.newLineAtOffset(0, -lineHeight);
        yPosition -= lineHeight;

        if (yPosition < 50) {
            contentStream.endText();
            contentStream.close();

            page = new PDPage();
            document.addPage(page);
            contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(margin, 750);
            yPosition = 750;
        }
    }

    @Override
    public void save(File file) throws IOException {
        contentStream.endText();
        contentStream.close();
        document.save(file);
        document.close();
    }

    @Override
    public void load(File file) throws IOException {
        try(PDDocument loaded = PDDocument.load(file)) {

        }
    }

    @Override
    public String getText() throws IOException {
        return "PDF content cannot be displayed in editor";
    }
}