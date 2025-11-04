package com.texteditor.ui;

import com.texteditor.core.document.*;
import com.texteditor.core.document.Document;
import com.texteditor.core.pattern.factory.DocumentFactory;
import com.texteditor.core.pattern.observer.*;
import com.texteditor.core.pattern.observer.interfaces.EditorListener;
import com.texteditor.core.pattern.strategy.*;
import com.texteditor.core.pattern.strategy.colour.BackgroundStrategy;
import com.texteditor.core.pattern.strategy.colour.ForegroundStrategy;
import com.texteditor.core.pattern.strategy.interfaces.TextEditingStrategy;
import com.texteditor.core.pattern.strategy.size.FontSizeStrategy;
import com.texteditor.core.pattern.strategy.size.LowerCaseStrategy;
import com.texteditor.core.pattern.strategy.size.UpperCaseStrategy;
import com.texteditor.core.pattern.strategy.style.BoldStrategy;
import com.texteditor.core.pattern.strategy.style.FontFamilyStrategy;
import com.texteditor.core.pattern.strategy.style.ItalicStrategy;
import com.texteditor.core.pattern.strategy.style.UnderlineStrategy;
import com.texteditor.util.FontStyle;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainEditor extends JFrame {
    private final JTextPane textPane = new JTextPane();
    private final UndoManager undoManager = new UndoManager();
    private final List<EditorListener> listeners = new ArrayList<>();
    private Document currentDocument;

    private boolean inBulletedList = false;
    private boolean inNumberedList = false;
    private int nextNumber = 1;

    public MainEditor() {
        setTitle("Text Editor Pro");
        setSize(1300, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initToolbar();          // ВСЁ ВВЕРХУ!
        initTextPane();
        initListeners();

        setVisible(true);
    }

    // === ВСЁ МЕНЮ ВВЕРХУ ===
    private void initToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        // === ФАЙЛ ===
        toolBar.add(createButton("New", this::newFile));
        toolBar.add(createButton("Open", this::openFile));
        toolBar.add(createSaveButton());
        toolBar.addSeparator();

        // === КОПИРОВАНИЕ ===
        toolBar.add(createButton("Copy", () -> textPane.copy()));
        toolBar.add(createButton("Paste", () -> textPane.paste()));
        toolBar.addSeparator();

        // === ФОРМАТИРОВАНИЕ ===
        toolBar.add(createButton("Bold", () -> applyStrategy(new BoldStrategy())));
        toolBar.add(createButton("Italic", () -> applyStrategy(new ItalicStrategy())));
        toolBar.add(createButton("Underline", () -> applyStrategy(new UnderlineStrategy())));
        toolBar.addSeparator();

        // === ШРИФТ, РАЗМЕР, ЦВЕТ ===
        JComboBox<FontStyle> fontBox = new JComboBox<>(FontStyle.values());
        fontBox.setRenderer(new FontListCellRenderer());
        fontBox.addActionListener(e -> applyStrategy(new FontFamilyStrategy(((FontStyle) fontBox.getSelectedItem()).getName())));
        toolBar.add(fontBox);

        JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(12, 8, 72, 1));
        sizeSpinner.addChangeListener(e -> applyStrategy(new FontSizeStrategy((Integer) sizeSpinner.getValue())));
        toolBar.add(sizeSpinner);

        JButton colorBtn = new JButton("Color");
        colorBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Text Color", Color.BLACK);
            if (c != null) applyStrategy(new ForegroundStrategy(c));
        });
        toolBar.add(colorBtn);

        JButton bgBtn = new JButton("Bg");
        bgBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Background", Color.YELLOW);
            if (c != null) applyStrategy(new BackgroundStrategy(c));
        });
        toolBar.add(bgBtn);

        toolBar.addSeparator();

        // === ВЫРАВНИВАНИЕ ===
        JComboBox<String> alignBox = new JComboBox<>(new String[]{"Left", "Center", "Right", "Justify"});
        alignBox.addActionListener(e -> {
            int align = switch ((String) alignBox.getSelectedItem()) {
                case "Left" -> StyleConstants.ALIGN_LEFT;
                case "Center" -> StyleConstants.ALIGN_CENTER;
                case "Right" -> StyleConstants.ALIGN_RIGHT;
                case "Justify" -> StyleConstants.ALIGN_JUSTIFIED;
                default -> StyleConstants.ALIGN_LEFT;
            };
            applyStrategy(new AlignmentStrategy(align));
        });
        toolBar.add(alignBox);

        // === СПИСКИ ===
        JComboBox<String> listBox = new JComboBox<>(new String[]{"None", "Bulleted", "Numbered"});
        listBox.addActionListener(e -> {
            String s = (String) listBox.getSelectedItem();
            if ("Bulleted".equals(s)) {
                inBulletedList = true; inNumberedList = false; nextNumber = 1;
            } else if ("Numbered".equals(s)) {
                inNumberedList = true; inBulletedList = false; nextNumber = 1;
            } else {
                inBulletedList = inNumberedList = false;
            }
        });
        toolBar.add(listBox);

        toolBar.addSeparator();

        // === РЕГИСТР ===
        JComboBox<String> caseBox = new JComboBox<>(new String[]{"Upper Case", "Lower Case"});
        caseBox.addActionListener(e -> {
            String s = (String) caseBox.getSelectedItem();
            TextEditingStrategy strategy = switch (s) {
                case "Upper Case" -> new UpperCaseStrategy();
                case "Lower Case" -> new LowerCaseStrategy();
                default -> null;
            };
            if (strategy != null) applyStrategy(strategy);
        });
        toolBar.add(caseBox);

        toolBar.addSeparator();

        // === UNDO/REDO ===
        toolBar.add(createButton("Undo", () -> { if (undoManager.canUndo()) undoManager.undo(); }));
        toolBar.add(createButton("Redo", () -> { if (undoManager.canRedo()) undoManager.redo(); }));

        add(toolBar, BorderLayout.NORTH); // ВСЁ ВВЕРХУ!
    }

    private void initTextPane() {
        add(new JScrollPane(textPane), BorderLayout.CENTER);
        textPane.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));
    }

    private void initListeners() {
        textPane.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && !e.isConsumed()) {
                    handleEnter();
                    e.consume();
                } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE && !e.isConsumed()) {
                    handleBackspace();
                    e.consume();
                }
            }
        });

        addEditorListener(event -> {
            if (event.getType() == EditorEvent.Type.FILE_LOADED) {
                undoManager.discardAllEdits();
                resetListState();
                JOptionPane.showMessageDialog(this, "Загружено: " + event.getMessage());
            } else if (event.getType() == EditorEvent.Type.FILE_SAVED) {
                JOptionPane.showMessageDialog(this, "Сохранено: " + event.getMessage());
            }
        });
    }

    private void handleBackspace() {
        StyledDocument doc = textPane.getStyledDocument();
        int pos = textPane.getCaretPosition();
        if (pos <= 0) return;

        try {
            // === СПИСКИ ===
            if (inBulletedList && pos >= 2 && doc.getText(pos - 2, 2).equals("• ")) {
                doc.remove(pos - 2, 2);
                if (pos - 2 <= 0 || doc.getText(0, pos - 2).trim().isEmpty()) {
                    inBulletedList = false;
                }
                return;
            }

            if (inNumberedList && pos >= 4) {
                String prev = doc.getText(pos - 4, 4);
                if (prev.matches("\\d+\\. ")) {
                    int len = prev.indexOf('.') + 2;
                    doc.remove(pos - len, len);
                    nextNumber = Math.max(1, nextNumber - 1);
                    if (nextNumber <= 1) inNumberedList = false;
                    return;
                }
            }

            // === ОБЫЧНЫЙ BACKSPACE ===
            doc.remove(pos - 1, 1);
        } catch (Exception ignored) {}
    }

    private void handleEnter() {
        StyledDocument doc = textPane.getStyledDocument();
        int pos = textPane.getCaretPosition();
        try {
            if (inBulletedList) {
                doc.insertString(pos, "\n• ", null);
                textPane.setCaretPosition(pos + 3);
            } else if (inNumberedList) {
                String num = String.valueOf(nextNumber++);
                doc.insertString(pos, "\n" + num + ". ", null);
                textPane.setCaretPosition(pos + num.length() + 3);
            } else {
                doc.insertString(pos, "\n", null);
            }
        } catch (Exception ignored) {}
    }

    private void resetListState() {
        inBulletedList = inNumberedList = false;
        nextNumber = 1;
    }

    // === СОХРАНЕНИЕ В ЛЮБОМ ФОРМАТЕ ===
    private JButton createSaveButton() {
        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Сохранить как...");
            chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text File (*.txt)", "txt"));
            chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Word Document (*.docx)", "docx"));
            chooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Document (*.pdf)", "pdf"));

            if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String ext = getExtension(file.getName());
                if (!file.getName().contains(".")) {
                    file = new File(file.getParent(), file.getName() + "." + ext);
                }

                try {
                    currentDocument = DocumentFactory.create(ext);
                    currentDocument.addText(textPane.getText());
                    currentDocument.save(file);
                    fireEvent(new EditorEvent(EditorEvent.Type.FILE_SAVED, file.getName()));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage());
                }
            }
        });
        return saveBtn;
    }

    private void newFile() {
        textPane.setText("");
        currentDocument = null;
        undoManager.discardAllEdits();
        resetListState();
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                currentDocument = DocumentFactory.open(file);
                String content = extractText(currentDocument);
                textPane.setText(content);
                fireEvent(new EditorEvent(EditorEvent.Type.FILE_LOADED, file.getName()));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage());
            }
        }
    }

    private String extractText(Document doc) throws Exception {
        if (doc instanceof TextDocument td) {
            return td.getContent();
        } else if (doc instanceof RichTextDocument rd) {
            return doc.getText();
        } else if (doc instanceof PdfDocument pd) {
            return "PDF content not supported for display";
        }
        return "";
    }

    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot == -1 ? "txt" : filename.substring(dot + 1).toLowerCase();
    }

    private void applyStrategy(TextEditingStrategy strategy) {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();
        if (start != end) {
            strategy.apply(textPane);
        }
    }

    private JButton createButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private void addEditorListener(EditorListener listener) { listeners.add(listener); }
    private void fireEvent(EditorEvent event) { listeners.forEach(l -> l.onEvent(event)); }

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