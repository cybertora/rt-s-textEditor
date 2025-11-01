package com.texteditor.core.pattern.command;

import javax.swing.JTextPane;

public class CopyCommand implements Command {
    private final JTextPane pane;

    public CopyCommand(JTextPane pane) { this.pane = pane; }

    @Override public void execute() { pane.copy(); }
    @Override public void undo() { /* no undo */ }
}