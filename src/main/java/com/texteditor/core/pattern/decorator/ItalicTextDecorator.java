package com.texteditor.core.pattern.decorator;

public class ItalicTextDecorator implements Text {
    private Text wrappedText;

    public ItalicTextDecorator(Text wrappedText) {
        this.wrappedText = wrappedText;
    }

    @Override
    public String getText() {
        return "<i>" + wrappedText.getText() + "</i>";
    }
}