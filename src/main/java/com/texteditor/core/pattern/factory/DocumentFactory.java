package com.texteditor.core.pattern.factory;

import com.texteditor.core.document.*;

import java.io.File;

public class DocumentFactory {
    public Document createDocument(File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".txt")) {
            return new TextDocument();
        } else if (name.endsWith(".docx")) {
            return new RichTextDocument();
        } else if (name.endsWith(".pdf")) {
            return new PdfDocument();
        } else {
            return new TextDocument(); // по умолчанию
        }
    }
}