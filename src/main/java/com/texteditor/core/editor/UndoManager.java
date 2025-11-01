package com.texteditor.core.editor;

import com.texteditor.core.pattern.observer.Observer;

import java.util.Stack;

public class UndoManager implements Observer {
    private final Stack<String> undoStack = new Stack<>();
    private final Stack<String> redoStack = new Stack<>();
    private final TextEditor editor;

    public UndoManager(TextEditor editor) { this.editor = editor; }

    @Override
    public void update(String content) {
        undoStack.push(content);
        redoStack.clear();
    }

    public void undo() {
        if (undoStack.size() > 1) {
            redoStack.push(undoStack.pop());
            editor.setContent(undoStack.peek());
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(editor.getContent());
            editor.setContent(redoStack.pop());
        }
    }
}