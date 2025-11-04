package com.texteditor.core.pattern.strategy.style;

import com.texteditor.core.pattern.strategy.interfaces.TextEditingStrategy;

import javax.swing.JTextPane;
import javax.swing.text.*;

public class FontFamilyStrategy implements TextEditingStrategy {
    private final String family;

    public FontFamilyStrategy(String family) {
        this.family = family;
    }

    @Override
    public void apply(JTextPane pane) {
        StyledDocument doc = pane.getStyledDocument();
        int start = pane.getSelectionStart();
        int end = pane.getSelectionEnd();
        if (start == end) return;

        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrs, family);
        doc.setCharacterAttributes(start, end - start, attrs, false);
    }
}