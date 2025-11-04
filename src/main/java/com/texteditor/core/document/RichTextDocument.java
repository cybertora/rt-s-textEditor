package com.texteditor.core.document;

import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RichTextDocument implements Document {

    private XWPFDocument doc;

    @Override
    public void create() throws IOException {
        doc = new XWPFDocument();
    }

    @Override
    public void addText(String text) throws IOException {
        XWPFParagraph paragraph = doc.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(text);
    }

    @Override
    public void save(File file) throws IOException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            doc.write(out);
        }
    }

    @Override
    public void load(File file) throws IOException {
        try (FileInputStream in = new FileInputStream(file)) {
            doc = new XWPFDocument(in);
        }
    }

    public XWPFDocument getDocument() {
        return doc;
    }

    @Override
    public String getText() {
        if (doc == null) return "";

        StringBuilder text = new StringBuilder();
        List<XWPFParagraph> paragraphs = doc.getParagraphs();
        for (XWPFParagraph p : paragraphs) {
            text.append(p.getText()).append("\n");
        }
        return text.toString();
    }
}