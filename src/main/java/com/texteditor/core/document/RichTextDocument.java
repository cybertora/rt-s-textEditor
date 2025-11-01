package com.texteditor.core.document;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.*;
import java.util.List;

public class RichTextDocument implements Document {
    private String content;

    @Override
    public void open(File file) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(file))) {
            StringBuilder sb = new StringBuilder();
            List<XWPFParagraph> paragraphs = doc.getParagraphs();
            for (XWPFParagraph p : paragraphs) {
                sb.append(p.getText()).append("\n");
            }
            content = sb.toString();
        }
    }

    @Override
    public void save(File file) throws Exception {
        try (XWPFDocument doc = new XWPFDocument();
             FileOutputStream out = new FileOutputStream(file)) {
            XWPFParagraph p = doc.createParagraph();
            p.createRun().setText(content);
            doc.write(out);
        }
    }

    @Override
    public String getContent() { return content; }
    @Override
    public void setContent(String content) { this.content = content; }
}