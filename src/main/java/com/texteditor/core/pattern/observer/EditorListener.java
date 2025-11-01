package com.texteditor.core.pattern.observer;

@FunctionalInterface
public interface EditorListener {
    void onEditorEvent(EditorEvent event);
}