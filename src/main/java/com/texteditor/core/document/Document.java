package com.texteditor.core.document;

import java.io.File;

public interface Document {
    void open(File file) throws Exception;
    void save(File file) throws Exception;
    String getContent();
    void setContent(String content);
}