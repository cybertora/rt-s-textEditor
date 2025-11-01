package com.texteditor.core.pattern.command;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class UnderlineCommand implements Command {
    private final JTextPane pane;
    private final int start, end;
    private final boolean wasUnderlined;

    public UnderlineCommand(JTextPane pane) {
        this.pane = pane;
        this.start = pane.getSelectionStart();
        this.end = pane.getSelectionEnd();
        this.wasUnderlined = isUnderlinedAt(start);
    }

    private boolean isUnderlinedAt(int pos) {
        var attrs = pane.getStyledDocument().getCharacterElement(pos).getAttributes();
        return StyleConstants.isUnderline(attrs);
    }

    @Override public void execute() { apply(!wasUnderlined); }
    @Override public void undo() { apply(wasUnderlined); }

    private void apply(boolean underline) {
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setUnderline(attrs, underline);
        pane.getStyledDocument().setCharacterAttributes(start, end - start, attrs, false);
    }
}