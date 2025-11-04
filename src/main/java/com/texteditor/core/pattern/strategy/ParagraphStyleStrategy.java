package com.texteditor.core.pattern.strategy;

import javax.swing.JTextPane;

public interface ParagraphStyleStrategy extends TextEditingStrategy {
    void applyToParagraph(JTextPane pane, int start, int end);
}