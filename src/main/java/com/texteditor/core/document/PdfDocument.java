package com.texteditor.core.document;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.nio.file.Path;

public class PdfDocument implements Document {
    private String content;

    @Override
    public void open(File file) throws Exception {
        Path path = file.toPath();
        try (PDDocument document = PDDocument.load((InputStream) path)) {
            PDFTextStripper stripper = new PDFTextStripper();
            content = stripper.getText(document);
        }
    }

    @Override
    public void save(File file) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content != null ? content : "");
        }
    }

    @Override
    public String getContent() { return content; }
    @Override
    public void setContent(String content) { this.content = content; }
}