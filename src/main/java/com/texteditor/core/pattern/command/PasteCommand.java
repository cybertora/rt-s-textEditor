package com.texteditor.core.pattern.command;

import javax.swing.JTextPane;

public class PasteCommand implements Command {
    private final JTextPane pane;
    private String oldText;

    public PasteCommand(JTextPane pane) { this.pane = pane; }

    @Override public void execute() {
        oldText = pane.getText();
        pane.paste();
    }

    @Override public void undo() { pane.setText(oldText); }
}