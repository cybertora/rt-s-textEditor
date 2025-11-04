package com.texteditor.core.pattern.observer;

@FunctionalInterface
public interface EditorListener {
    void onEvent(EditorEvent event);
}