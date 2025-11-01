package com.texteditor.core.document;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;
import javax.swing.JTextPane;
import javax.swing.text.*;
import java.awt.Color;
import java.io.*;

public class PdfDocument implements Document {
    private String content;
    private JTextPane sourcePane;

    @Override
    public void open(File file) throws Exception {
        try (PDDocument doc = PDDocument.load(file)) {
            content = new org.apache.pdfbox.text.PDFTextStripper().getText(doc);
        }
    }

    @Override
    public void save(File file) throws Exception {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);
            PDPageContentStream stream = new PDPageContentStream(doc, page);

            if (sourcePane == null) {
                stream.beginText();
                stream.setFont(PDType1Font.HELVETICA, 12);
                stream.newLineAtOffset(50, 750);
                stream.showText(content != null ? content : "");
                stream.endText();
            } else {
                StyledDocument styledDoc = sourcePane.getStyledDocument();
                float y = 750;
                float leading = 15;

                stream.beginText();
                stream.newLineAtOffset(50, y);
                stream.setLeading(leading);

                for (int i = 0; i < styledDoc.getLength(); i++) {
                    Element elem = styledDoc.getCharacterElement(i);
                    AttributeSet attrs = elem.getAttributes();
                    String text = styledDoc.getText(i, 1);

                    PDType1Font font = PDType1Font.HELVETICA;
                    if (StyleConstants.isBold(attrs) && StyleConstants.isItalic(attrs))
                        font = PDType1Font.HELVETICA_BOLD_OBLIQUE;
                    else if (StyleConstants.isBold(attrs))
                        font = PDType1Font.HELVETICA_BOLD;
                    else if (StyleConstants.isItalic(attrs))
                        font = PDType1Font.HELVETICA_OBLIQUE;

                    Integer size = StyleConstants.getFontSize(attrs);
                    float fontSize = size != null ? size : 12;

                    stream.setFont(font, fontSize);
                    Color color = StyleConstants.getForeground(attrs);
                    if (color != null) {
                        stream.setNonStrokingColor(color);
                    }

                    stream.showText(text);

                    if (text.equals("\n")) {
                        stream.newLine();
                        y -= leading;
                        if (y < 50) {
                            stream.endText();
                            stream.close();
                            page = new PDPage(PDRectangle.A4);
                            doc.addPage(page);
                            stream = new PDPageContentStream(doc, page);
                            stream.beginText();
                            stream.newLineAtOffset(50, 750);
                            stream.setLeading(leading);
                            y = 750;
                        }
                    }
                }
                stream.endText();
            }

            stream.close();
            doc.save(file);
        }
    }

    public void setSourcePane(JTextPane pane) { this.sourcePane = pane; }
    @Override public String getContent() { return content; }
    @Override public void setContent(String content) { this.content = content; }
}