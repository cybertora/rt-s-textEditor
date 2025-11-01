package com.texteditor.core.pattern.observer;

public class EditorEvent {
    public enum Type {
        CONTENT_CHANGED, SELECTION_CHANGED, STYLE_APPLIED, FILE_LOADED, FILE_SAVED
    }

    private final Type type;
    private final String message;
    private final Object data;

    public EditorEvent(Type type, String message, Object data) {
        this.type = type;
        this.message = message;
        this.data = data;
    }

    public EditorEvent(Type type) { this(type, null, null); }
    public EditorEvent(Type type, String message) { this(type, message, null); }

    public Type getType() { return type; }
    public String getMessage() { return message; }
    public Object getData() { return data; }
}