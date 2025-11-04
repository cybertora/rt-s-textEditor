package com.texteditor.core.pattern.strategy.interfaces;

import javax.swing.JTextPane;

public interface TextEditingStrategy {
    void apply(JTextPane pane);
}