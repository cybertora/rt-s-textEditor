package com.texteditor.core.pattern.factory;

import com.texteditor.core.document.*;

import java.io.File;

public class DocumentFactory {
    public Document createDocument(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".txt")) return new TextDocument();
        if (name.endsWith(".docx")) return new RichTextDocument();
        if (name.endsWith(".pdf")) return new PdfDocument();
        throw new IllegalArgumentException("Unsupported file: " + name);
    }
}