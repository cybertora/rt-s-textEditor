package com.texteditor.core.document;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class TextDocument implements Document {

    private final StringBuilder content = new StringBuilder();

    @Override
    public void create() throws IOException {
        content.setLength(0);
    }

    @Override
    public void addText(String text) throws IOException {
        content.append(text).append("\n");
    }

    @Override
    public void save(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content.toString());
        }
    }

    @Override
    public void load(File file) throws IOException {
        content.setLength(0);
        try (FileReader reader = new FileReader(file); Scanner scanner = new Scanner(reader)) {
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
        }
    }

    @Override
    public String getText() throws IOException {
        return content.toString();
    }

    public String getContent() {
        return content.toString();
    }
}