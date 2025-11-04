package com.texteditor.core.pattern.strategy;

import javax.swing.JTextPane;
import javax.swing.text.*;
import java.awt.Color;

public class BackgroundStrategy implements TextEditingStrategy {
    private final Color color;

    public BackgroundStrategy(Color color) {
        this.color = color;
    }

    @Override
    public void apply(JTextPane pane) {
        StyledDocument doc = pane.getStyledDocument();
        int start = pane.getSelectionStart();
        int end = pane.getSelectionEnd();
        if (start == end) return;

        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setBackground(attrs, color);
        doc.setCharacterAttributes(start, end - start, attrs, false);
    }
}