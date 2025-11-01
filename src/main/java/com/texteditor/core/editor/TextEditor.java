package com.texteditor.core.editor;

import com.texteditor.core.document.*;
import com.texteditor.core.pattern.command.CommandInvoker;
import com.texteditor.core.pattern.factory.DocumentFactory;
import com.texteditor.core.pattern.observer.EditorEvent;
import com.texteditor.core.pattern.observer.EditorListener;
import com.texteditor.core.pattern.strategy.TextEditingStrategy;

import javax.swing.JTextPane;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TextEditor {
    private String content = "";
    private Document currentDocument;
    private final DocumentFactory factory = new DocumentFactory();
    private final UndoManager undoManager;
    private final CommandInvoker invoker = new CommandInvoker();
    private TextEditingStrategy strategy;

    private JTextPane textPane;
    private final List<EditorListener> listeners = new ArrayList<>();

    public TextEditor() {
        this.undoManager = new UndoManager(this);
    }

    public void setTextPane(JTextPane pane) {
        this.textPane = pane;
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public void addListener(EditorListener listener) {
        listeners.add(listener);
    }

    private void fireEvent(EditorEvent event) {
        listeners.forEach(l -> l.onEditorEvent(event));
    }

    public void setContent(String content) {
        this.content = content;
        undoManager.update(content);
        fireEvent(new EditorEvent(EditorEvent.Type.CONTENT_CHANGED, "Text updated"));
    }

    public String getContent() {
        return content;
    }

    public void loadFile(File file) throws Exception {
        currentDocument = factory.createDocument(file);
        currentDocument.open(file);
        setContent(currentDocument.getContent());
        fireEvent(new EditorEvent(EditorEvent.Type.FILE_LOADED, file.getName()));
    }

    // === СОХРАНЕНИЕ: РАБОТАЕТ БЕЗ ОТКРЫТИЯ ФАЙЛА ===
    public void saveFile(File file) throws Exception {
        // Создаём документ, если его нет
        if (currentDocument == null) {
            currentDocument = factory.createDocument(file);
        }

        // Передаём textPane для стилей
        if (textPane != null) {
            if (currentDocument instanceof RichTextDocument rtd) {
                rtd.setSourcePane(textPane);
            } else if (currentDocument instanceof PdfDocument pd) {
                pd.setSourcePane(textPane);
            }
        }

        currentDocument.setContent(textPane != null ? textPane.getText() : content);
        currentDocument.save(file);
        fireEvent(new EditorEvent(EditorEvent.Type.FILE_SAVED, file.getName()));
    }

    public void setStrategy(TextEditingStrategy s) {
        this.strategy = s;
    }

    public void applyStrategy() {
        if (strategy != null) {
            setContent(strategy.edit(content));
            fireEvent(new EditorEvent(EditorEvent.Type.STYLE_APPLIED, "Strategy applied"));
        }
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }

    public CommandInvoker getInvoker() {
        return invoker;
    }

    public void undo() {
        invoker.undo();
    }

    public void redo() {
        // TODO
    }
}