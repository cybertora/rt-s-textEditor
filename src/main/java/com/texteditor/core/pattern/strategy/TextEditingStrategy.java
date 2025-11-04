package com.texteditor.core.pattern.strategy;

import javax.swing.JTextPane;

public interface TextEditingStrategy {
    void apply(JTextPane pane);
}