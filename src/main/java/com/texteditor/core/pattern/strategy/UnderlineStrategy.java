package com.texteditor.core.pattern.strategy;

import javax.swing.JTextPane;
import javax.swing.text.*;

public class UnderlineStrategy implements TextEditingStrategy {
    @Override
    public void apply(JTextPane pane) {
        StyledDocument doc = pane.getStyledDocument();
        int start = pane.getSelectionStart();
        int end = pane.getSelectionEnd();
        if (start == end) return;

        SimpleAttributeSet attrs = new SimpleAttributeSet();
        boolean isUnderline = StyleConstants.isUnderline(doc.getCharacterElement(start).getAttributes());
        StyleConstants.setUnderline(attrs, !isUnderline);
        doc.setCharacterAttributes(start, end - start, attrs, false);
    }
}