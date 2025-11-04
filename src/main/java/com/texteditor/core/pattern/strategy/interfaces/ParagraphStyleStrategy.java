package com.texteditor.core.pattern.strategy.interfaces;

import javax.swing.JTextPane;

public interface ParagraphStyleStrategy extends TextEditingStrategy {
    void applyToParagraph(JTextPane pane, int start, int end);
}