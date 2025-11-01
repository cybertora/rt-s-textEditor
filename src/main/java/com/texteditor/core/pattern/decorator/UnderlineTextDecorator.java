package com.texteditor.core.pattern.decorator;

public class UnderlineTextDecorator implements Text {
    private Text wrappedText;

    public UnderlineTextDecorator(Text wrappedText) {
        this.wrappedText = wrappedText;
    }

    @Override
    public String getText() {
        return "<u>" + wrappedText.getText() + "</u>";
    }
}