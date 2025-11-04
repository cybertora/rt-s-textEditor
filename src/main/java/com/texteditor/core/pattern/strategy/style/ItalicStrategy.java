package com.texteditor.core.pattern.strategy.style;

import com.texteditor.core.pattern.strategy.interfaces.TextEditingStrategy;

import javax.swing.JTextPane;
import javax.swing.text.*;

public class ItalicStrategy implements TextEditingStrategy {
    @Override
    public void apply(JTextPane pane) {
        StyledDocument doc = pane.getStyledDocument();
        int start = pane.getSelectionStart();
        int end = pane.getSelectionEnd();
        if (start == end) return;

        SimpleAttributeSet attrs = new SimpleAttributeSet();
        boolean isItalic = StyleConstants.isItalic(doc.getCharacterElement(start).getAttributes());
        StyleConstants.setItalic(attrs, !isItalic);
        doc.setCharacterAttributes(start, end - start, attrs, false);
    }
}