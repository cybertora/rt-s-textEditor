package com.texteditor.core.document;

import org.apache.poi.xwpf.usermodel.*;
import javax.swing.JTextPane;
import javax.swing.text.*;
import java.awt.Color;
import java.io.*;

public class RichTextDocument implements Document {
    private String content;
    private JTextPane sourcePane; // Храним ссылку на JTextPane

    @Override
    public void open(File file) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(file))) {
            StringBuilder sb = new StringBuilder();
            for (XWPFParagraph p : doc.getParagraphs()) {
                sb.append(p.getText()).append("\n");
            }
            content = sb.toString();
        }
    }

    @Override
    public void save(File file) throws Exception {
        if (sourcePane == null) {
            // Если нет стилей — просто текст
            try (XWPFDocument doc = new XWPFDocument();
                 FileOutputStream out = new FileOutputStream(file)) {
                XWPFParagraph p = doc.createParagraph();
                p.createRun().setText(content != null ? content : "");
                doc.write(out);
            }
            return;
        }

        try (XWPFDocument doc = new XWPFDocument();
             FileOutputStream out = new FileOutputStream(file)) {

            StyledDocument styledDoc = sourcePane.getStyledDocument();
            XWPFParagraph paragraph = doc.createParagraph();
            XWPFRun run = null;

            for (int i = 0; i < styledDoc.getLength(); i++) {
                Element elem = styledDoc.getCharacterElement(i);
                AttributeSet attrs = elem.getAttributes();
                String text = styledDoc.getText(i, 1);

                // Новый run при смене стилей
                if (run == null || !sameStyle(run, attrs)) {
                    run = paragraph.createRun();
                    applyStyleToRun(run, attrs);
                }

                run.setText(text);
            }

            doc.write(out);
        }
    }

    private boolean sameStyle(XWPFRun run, AttributeSet attrs) {
        if (StyleConstants.isBold(attrs) != run.isBold()) return false;
        if (StyleConstants.isItalic(attrs) != run.isItalic()) return false;
        String family = StyleConstants.getFontFamily(attrs);
        if (family != null && !family.equals(run.getFontFamily())) return false;
        Integer size = StyleConstants.getFontSize(attrs);
        if (size != null && size != run.getFontSize()) return false;
        return true;
    }

    private void applyStyleToRun(XWPFRun run, AttributeSet attrs) {
        run.setBold(StyleConstants.isBold(attrs));
        run.setItalic(StyleConstants.isItalic(attrs));
        if (StyleConstants.isUnderline(attrs)) {
            run.setUnderline(UnderlinePatterns.SINGLE);
        }

        String family = StyleConstants.getFontFamily(attrs);
        if (family != null) run.setFontFamily(family);

        Integer size = StyleConstants.getFontSize(attrs);
        if (size != null) run.setFontSize((float) size);

        Color color = StyleConstants.getForeground(attrs);
        if (color != null) {
            run.setColor(String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
        }
    }

    // === Геттеры/сеттеры ===
    public void setSourcePane(JTextPane pane) { this.sourcePane = pane; }
    @Override public String getContent() { return content; }
    @Override public void setContent(String content) { this.content = content; }
}