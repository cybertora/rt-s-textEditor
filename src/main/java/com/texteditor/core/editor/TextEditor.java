package com.texteditor.core.editor;

import com.texteditor.core.document.Document;
import com.texteditor.core.pattern.command.CommandInvoker;
import com.texteditor.core.pattern.factory.DocumentFactory;
import com.texteditor.core.pattern.observer.EditorEvent;
import com.texteditor.core.pattern.observer.EditorListener;
import com.texteditor.core.pattern.strategy.TextEditingStrategy;

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

    private final List<EditorListener> listeners = new ArrayList<>();

    public TextEditor() {
        this.undoManager = new UndoManager(this);
    }

    public void addListener(EditorListener listener) { listeners.add(listener); }
    private void fireEvent(EditorEvent event) {
        listeners.forEach(l -> l.onEditorEvent(event));
    }

    public void setContent(String content) {
        this.content = content;
        undoManager.update(content);
        fireEvent(new EditorEvent(EditorEvent.Type.CONTENT_CHANGED, "Text updated"));
    }

    public String getContent() { return content; }

    public void loadFile(File file) throws Exception {
        currentDocument = factory.createDocument(file);
        currentDocument.open(file);
        setContent(currentDocument.getContent());
        fireEvent(new EditorEvent(EditorEvent.Type.FILE_LOADED, file.getName()));
    }

    public void saveFile(File file) throws Exception {
        if (currentDocument != null) {
            currentDocument.setContent(content);
            currentDocument.save(file);
            fireEvent(new EditorEvent(EditorEvent.Type.FILE_SAVED, file.getName()));
        }
    }

    public void setStrategy(TextEditingStrategy s) { this.strategy = s; }
    public void applyStrategy() {
        if (strategy != null) {
            setContent(strategy.edit(content));
            fireEvent(new EditorEvent(EditorEvent.Type.STYLE_APPLIED, "Strategy applied"));
        }
    }

    public UndoManager getUndoManager() { return undoManager; }
    public CommandInvoker getInvoker() { return invoker; }

    public void undo() { invoker.undo(); }
    public void redo() { /* TODO */ }
}