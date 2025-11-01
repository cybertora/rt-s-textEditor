package com.texteditor.core.document;

import java.io.*;

public class TextDocument implements Document {
    private String content;

    @Override
    public void open(File file) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        content = sb.toString();
    }

    @Override
    public void save(File file) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }

    @Override
    public String getContent() { return content; }
    @Override
    public void setContent(String content) { this.content = content; }
}