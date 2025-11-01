package com.texteditor.core.pattern.decorator;

public class BasicText implements Text {
    private String text;

    public BasicText(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }
}