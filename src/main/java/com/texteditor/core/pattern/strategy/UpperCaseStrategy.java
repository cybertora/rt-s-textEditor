package com.texteditor.core.pattern.strategy;

import javax.swing.JTextPane;

public class UpperCaseStrategy implements TextEditingStrategy {
    @Override
    public void apply(JTextPane pane) {
        int start = pane.getSelectionStart();
        int end = pane.getSelectionEnd();
        if (start == end) return;

        try {
            String selected = pane.getSelectedText();
            pane.replaceSelection(selected.toUpperCase());
        } catch (Exception ignored) {}
    }
}