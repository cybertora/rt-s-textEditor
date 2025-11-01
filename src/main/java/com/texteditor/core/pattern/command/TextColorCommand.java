package com.texteditor.core.pattern.command;

import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Color;

public class TextColorCommand implements Command {
    private final JTextPane pane;
    private final Color newColor;
    private final Color oldColor;
    private final int start, end;

    public TextColorCommand(JTextPane pane, Color color) {
        this.pane = pane;
        this.newColor = color;
        this.start = pane.getSelectionStart();
        this.end = pane.getSelectionEnd();
        this.oldColor = getCurrentColor();
    }

    private Color getCurrentColor() {
        try {
            var attr = pane.getStyledDocument().getCharacterElement(start).getAttributes();
            Object color = attr.getAttribute(StyleConstants.Foreground);
            return color instanceof Color c ? c : Color.BLACK;
        } catch (Exception e) {
            return Color.BLACK;
        }
    }

    @Override public void execute() { apply(newColor); }
    @Override public void undo() { apply(oldColor); }

    private void apply(Color color) {
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setForeground(attrs, color);
        pane.getStyledDocument().setCharacterAttributes(start, end - start, attrs, false);
    }
}