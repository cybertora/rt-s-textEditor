package com.texteditor.core.document;

import java.io.File;
import java.io.IOException;

public interface Document {
    void create() throws IOException;
    void addText(String text) throws IOException;
    void save(File file) throws IOException;
    void load(File file) throws IOException;
    String getText() throws IOException;
}