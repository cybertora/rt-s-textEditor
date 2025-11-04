package com.texteditor.core.pattern.strategy;

import com.texteditor.core.pattern.strategy.interfaces.TextEditingStrategy;

import javax.swing.JTextPane;
import javax.swing.text.*;

public class AlignmentStrategy implements TextEditingStrategy {
    private final int alignment;

    public AlignmentStrategy(int alignment) {
        this.alignment = alignment;
    }

    @Override
    public void apply(JTextPane pane) {
        StyledDocument doc = pane.getStyledDocument();
        int start = pane.getSelectionStart();
        int end = pane.getSelectionEnd();
        if (start == end) return;

        Element paraStart = doc.getParagraphElement(start);
        Element paraEnd = doc.getParagraphElement(end - 1);
        int paraStartOffset = paraStart.getStartOffset();
        int paraEndOffset = paraEnd.getEndOffset();

        MutableAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setAlignment(attrs, alignment);
        doc.setParagraphAttributes(paraStartOffset, paraEndOffset - paraStartOffset, attrs, false);
    }
}