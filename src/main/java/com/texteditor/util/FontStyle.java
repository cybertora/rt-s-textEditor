package com.texteditor.util;

public enum FontStyle {
    ARIAL("Arial"),
    TIMES_NEW_ROMAN("Times New Roman"),
    COURIER_NEW("Courier New"),
    VERDANA("Verdana"),
    GEORGIA("Georgia"),
    COMIC_SANS_MS("Comic Sans MS"),
    TAHOMA("Tahoma"),
    TREBUCHET_MS("Trebuchet MS"),
    IMPACT("Impact"),
    LUCIDA_CONSOLE("Lucida Console");

    private final String name;
    FontStyle(String name) { this.name = name; }
    public String getName() { return name; }
}