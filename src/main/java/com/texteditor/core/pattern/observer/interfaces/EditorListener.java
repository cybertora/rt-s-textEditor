package com.texteditor.core.pattern.observer.interfaces;

import com.texteditor.core.pattern.observer.EditorEvent;

@FunctionalInterface
public interface EditorListener {
    void onEvent(EditorEvent event);
}