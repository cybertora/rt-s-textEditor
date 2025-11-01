package com.texteditor.core.pattern.command;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class ItalicCommand implements Command {
    private final JTextPane pane;
    private final int start, end;
    private final boolean wasItalic;

    public ItalicCommand(JTextPane pane) {
        this.pane = pane;
        this.start = pane.getSelectionStart();
        this.end = pane.getSelectionEnd();
        this.wasItalic = isItalicAt(start);
    }

    private boolean isItalicAt(int pos) {
        var attrs = pane.getStyledDocument().getCharacterElement(pos).getAttributes();
        return StyleConstants.isItalic(attrs);
    }

    @Override public void execute() { apply(!wasItalic); }
    @Override public void undo() { apply(wasItalic); }

    private void apply(boolean italic) {
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setItalic(attrs, italic);
        pane.getStyledDocument().setCharacterAttributes(start, end - start, attrs, false);
    }
}