package com.texteditor.core.pattern.strategy;

import javax.swing.JTextPane;

public class SpellCheckStrategy implements TextEditingStrategy {
    @Override
    public void apply(JTextPane pane) {
        int start = pane.getSelectionStart();
        int end = pane.getSelectionEnd();
        if (start == end) return;

        try {
            String selected = pane.getSelectedText();
            javax.swing.text.StyledDocument doc = pane.getStyledDocument();
            javax.swing.text.SimpleAttributeSet red = new javax.swing.text.SimpleAttributeSet();
            javax.swing.text.StyleConstants.setForeground(red, java.awt.Color.RED);
            doc.setCharacterAttributes(start, end - start, red, false);
        } catch (Exception ignored) {}
    }
}