package com.texteditor.core.pattern.command;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class FontSizeCommand implements Command {
    private final JTextPane pane;
    private final int newSize;
    private final int oldSize;
    private final int start, end;

    public FontSizeCommand(JTextPane pane, int size) {
        this.pane = pane;
        this.newSize = size;
        this.start = pane.getSelectionStart();
        this.end = pane.getSelectionEnd();
        this.oldSize = getCurrentSize();
    }

    private int getCurrentSize() {
        try {
            var attr = pane.getStyledDocument().getCharacterElement(start).getAttributes();
            Object size = attr.getAttribute(StyleConstants.FontSize);
            return size != null ? (Integer) size : 12;
        } catch (Exception e) {
            return 12;
        }
    }

    @Override public void execute() { apply(newSize); }
    @Override public void undo() { apply(oldSize); }

    private void apply(int size) {
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setFontSize(attrs, size);
        pane.getStyledDocument().setCharacterAttributes(start, end - start, attrs, false);
    }
}