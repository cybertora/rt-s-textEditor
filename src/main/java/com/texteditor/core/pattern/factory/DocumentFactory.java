package com.texteditor.core.pattern.factory;

import com.texteditor.core.document.Document;
import com.texteditor.core.document.PdfDocument;
import com.texteditor.core.document.RichTextDocument;

import java.io.File;
import java.io.IOException;

public class DocumentFactory {

    public static Document create(String type) throws IOException, IOException {
        Document doc = switch (type.toLowerCase()) {
            case "pdf" -> new PdfDocument();
            case "docx" -> new RichTextDocument();
            default -> throw new IllegalArgumentException("Unknown format: " + type);
        };
        doc.create();
        return doc;
    }

    public static Document open(File file) throws Exception {
        String ext = getExtension(file.getName());
        Document doc = create(ext);
        doc.load(file);  // ← ТЕПЕРЬ МЕТОД ЕСТЬ!
        return doc;
    }

    private static String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot == -1 ? "" : filename.substring(dot + 1);
    }
}