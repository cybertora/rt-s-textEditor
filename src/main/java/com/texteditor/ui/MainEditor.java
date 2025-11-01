package com.texteditor.ui;

import com.texteditor.core.editor.TextEditor;
import com.texteditor.core.pattern.command.*;
import com.texteditor.core.pattern.observer.EditorEvent;
import com.texteditor.core.pattern.strategy.*;
import com.texteditor.util.FontStyle;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;

public class MainEditor extends JFrame {
    private final TextEditor editor = new TextEditor();
    private final JTextPane textPane = new JTextPane();

    // Для Apply
    private FontStyle pendingFont = null;
    private Integer pendingSize = null;
    private Color pendingColor = null;
    private String pendingAlign = null;
    private String pendingCase = null;

    public MainEditor() {
        setTitle("Clean Text Editor");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        toolBar.add(createButton("Load", this::showOpenDialog));
        toolBar.add(createButton("Save", this::showSaveDialog));
        toolBar.addSeparator();

        toolBar.add(createButton("Copy", () -> execute(new CopyCommand(textPane))));
        toolBar.add(createButton("Paste", () -> execute(new PasteCommand(textPane))));
        toolBar.addSeparator();

        toolBar.add(createButton("Bold", () -> execute(new BoldCommand(textPane))));
        toolBar.add(createButton("Italic", () -> execute(new ItalicCommand(textPane))));
        toolBar.add(createButton("Underline", () -> execute(new UnderlineCommand(textPane))));
        toolBar.addSeparator();

        toolBar.add(createButton("Undo", editor::undo));
        toolBar.add(createButton("Redo", editor::redo));

        // === Edit Panel (Apply) ===
        JComboBox<FontStyle> fontBox = new JComboBox<>(FontStyle.values());
        fontBox.setRenderer(new FontListCellRenderer());
        fontBox.addActionListener(e -> pendingFont = (FontStyle) fontBox.getSelectedItem());

        JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(12, 8, 72, 1));
        sizeSpinner.addChangeListener(e -> pendingSize = (Integer) sizeSpinner.getValue());

        JComboBox<String> caseBox = new JComboBox<>(new String[]{"Upper Case", "Lower Case", "Spell Check"});
        caseBox.addActionListener(e -> pendingCase = (String) caseBox.getSelectedItem());

        JButton colorBtn = new JButton("Color");
        colorBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Choose Text Color", Color.BLACK);
            if (c != null) pendingColor = c;
        });

        JComboBox<String> alignBox = new JComboBox<>(new String[]{"Left", "Center", "Right", "Justify"});
        alignBox.addActionListener(e -> pendingAlign = (String) alignBox.getSelectedItem());

        JButton applyBtn = new JButton("Apply");
        applyBtn.addActionListener(e -> applyPendingChanges());

        JPanel editPanel = new JPanel(new FlowLayout());
        editPanel.add(new JLabel("Edit:"));
        editPanel.add(fontBox);
        editPanel.add(new JLabel("Size:"));
        editPanel.add(sizeSpinner);
        editPanel.add(caseBox);
        editPanel.add(colorBtn);
        editPanel.add(alignBox);
        editPanel.add(applyBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(toolBar, BorderLayout.NORTH);
        topPanel.add(editPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(textPane), BorderLayout.CENTER);

        // === Синхронизация только текста ===
        textPane.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { syncContent(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { syncContent(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { syncContent(); }
            private void syncContent() { editor.setContent(textPane.getText()); }
        });

        // === Загрузка файла ===
        editor.addListener(event -> {
            if (event.getType() == EditorEvent.Type.FILE_LOADED) {
                SwingUtilities.invokeLater(() -> {
                    textPane.setText(editor.getContent());
                    textPane.setCaretPosition(0);
                });
            }
        });

        setVisible(true);
    }

    // === Применение всех изменений ===
    private void applyPendingChanges() {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();

        if (start == end) {
            JOptionPane.showMessageDialog(this, "Выделите текст!");
            return;
        }

        StyledDocument doc = textPane.getStyledDocument();

        // Шрифт
        if (pendingFont != null) {
            SimpleAttributeSet attrs = new SimpleAttributeSet();
            StyleConstants.setFontFamily(attrs, pendingFont.getName());
            doc.setCharacterAttributes(start, end - start, attrs, false);
            pendingFont = null;
        }

        // Размер
        if (pendingSize != null) {
            editor.getInvoker().executeCommand(new FontSizeCommand(textPane, pendingSize));
            pendingSize = null;
        }

        // Цвет
        if (pendingColor != null) {
            SimpleAttributeSet attrs = new SimpleAttributeSet();
            StyleConstants.setForeground(attrs, pendingColor);
            doc.setCharacterAttributes(start, end - start, attrs, false);
            pendingColor = null;
        }

        // Выравнивание (на абзац)
        if (pendingAlign != null) {
            MutableAttributeSet attrs = new SimpleAttributeSet();
            switch (pendingAlign) {
                case "Left" -> StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);
                case "Center" -> StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_CENTER);
                case "Right" -> StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_RIGHT);
                case "Justify" -> StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_JUSTIFIED);
            }
            doc.setParagraphAttributes(start, end - start, attrs, false);
            pendingAlign = null;
        }

        // Upper/Lower/Spell
        if (pendingCase != null) {
            TextEditingStrategy strategy = switch (pendingCase) {
                case "Upper Case" -> new UpperCaseStrategy();
                case "Lower Case" -> new LowerCaseStrategy();
                case "Spell Check" -> new SpellCheckStrategy();
                default -> null;
            };
            if (strategy != null) {
                String selectedText = textPane.getSelectedText();
                String newText = strategy.edit(selectedText);
                textPane.replaceSelection(newText);
                editor.setContent(textPane.getText());
            }
            pendingCase = null;
        }

        editor.setContent(textPane.getText());
    }

    private JButton createButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private void execute(Command command) {
        editor.getInvoker().executeCommand(command);
    }

    private void showOpenDialog() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                editor.loadFile(chooser.getSelectedFile());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage());
            }
        }
    }

    private void showSaveDialog() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                editor.saveFile(chooser.getSelectedFile());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage());
            }
        }
    }

    // === Рендерер шрифтов ===
    private static class FontListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof FontStyle fs) {
                label.setText(fs.getName());
                label.setFont(new Font(fs.getName(), Font.PLAIN, 12));
            }
            return label;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainEditor::new);
    }
}