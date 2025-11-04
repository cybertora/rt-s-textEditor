package com.texteditor.core.pattern.strategy.size;

import com.texteditor.core.pattern.strategy.interfaces.TextEditingStrategy;

import javax.swing.JTextPane;

public class LowerCaseStrategy implements TextEditingStrategy {
    @Override
    public void apply(JTextPane pane) {
        int start = pane.getSelectionStart();
        int end = pane.getSelectionEnd();
        if (start == end) return;

        try {
            String selected = pane.getSelectedText();
            pane.replaceSelection(selected.toLowerCase());
        } catch (Exception ignored) {}
    }
}