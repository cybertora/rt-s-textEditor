package com.texteditor.core.pattern.strategy;

import javax.swing.JTextPane;
import javax.swing.text.*;

public class FontSizeStrategy implements TextEditingStrategy {
    private final int size;

    public FontSizeStrategy(int size) {
        this.size = size;
    }

    @Override
    public void apply(JTextPane pane) {
        StyledDocument doc = pane.getStyledDocument();
        int start = pane.getSelectionStart();
        int end = pane.getSelectionEnd();
        if (start == end) return;

        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setFontSize(attrs, size);
        doc.setCharacterAttributes(start, end - start, attrs, false);
    }
}