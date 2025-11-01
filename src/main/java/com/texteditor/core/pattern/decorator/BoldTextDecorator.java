package com.texteditor.core.pattern.decorator;

public class BoldTextDecorator implements Text {
    private Text wrappedText;

    public BoldTextDecorator(Text wrappedText) {
        this.wrappedText = wrappedText;
    }

    @Override
    public String getText() {
        return "<b>" + wrappedText.getText() + "</b>"; // Симулируем HTML-стиль; в реальности используйте StyledDocument в Swing
    }
}