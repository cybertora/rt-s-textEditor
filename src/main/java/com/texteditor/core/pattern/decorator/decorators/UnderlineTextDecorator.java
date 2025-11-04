package com.texteditor.core.pattern.decorator.decorators;

import com.texteditor.core.pattern.decorator.utils.Text;

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