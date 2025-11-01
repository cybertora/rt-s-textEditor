package com.texteditor.core.pattern.command;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class BoldCommand implements Command {
    private final JTextPane pane;
    private final int start, end;
    private final boolean wasBold;

    public BoldCommand(JTextPane pane) {
        this.pane = pane;
        this.start = pane.getSelectionStart();
        this.end = pane.getSelectionEnd();
        this.wasBold = isBoldAt(start);
    }

    private boolean isBoldAt(int pos) {
        var attrs = pane.getStyledDocument().getCharacterElement(pos).getAttributes();
        return StyleConstants.isBold(attrs);
    }

    @Override
    public void execute() {
        apply(!wasBold);
    }

    @Override
    public void undo() {
        apply(wasBold);
    }

    private void apply(boolean bold) {
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setBold(attrs, bold);
        pane.getStyledDocument().setCharacterAttributes(start, end - start, attrs, false);
    }
}