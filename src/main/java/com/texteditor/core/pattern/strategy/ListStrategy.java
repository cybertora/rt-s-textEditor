package com.texteditor.core.pattern.strategy;

import javax.swing.JTextPane;
import javax.swing.text.*;

public class ListStrategy implements TextEditingStrategy {
    private final String type; // "bulleted" or "numbered"
    private static int counter = 1;

    public ListStrategy(String type) {
        this.type = type;
    }

    @Override
    public void apply(JTextPane pane) {
        StyledDocument doc = pane.getStyledDocument();
        int start = pane.getSelectionStart();
        int end = pane.getSelectionEnd();
        if (start == end) return;

        try {
            String text = doc.getText(start, end - start);
            String[] lines = text.split("\n");
            StringBuilder sb = new StringBuilder();
            counter = 1;
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    String prefix = "numbered".equals(type) ? (counter++) + ". " : "• ";
                    sb.append(prefix).append(line.trim());
                }
                sb.append("\n");
            }
            if (sb.length() > 0) sb.setLength(sb.length() - 1);
            doc.remove(start, end - start);
            doc.insertString(start, sb.toString(), null);
        } catch (Exception ignored) {}
    }
}