package com.texteditor.core.pattern.strategy;

import com.texteditor.util.FontStyle;

public class FontFamilyStrategy implements TextEditingStrategy {
    private final FontStyle fontStyle;

    public FontFamilyStrategy(FontStyle fontStyle) {
        this.fontStyle = fontStyle;
    }

    @Override
    public String edit(String text) {
        return text; // Возвращаем текст (изменение шрифта — в GUI)
    }

    public FontStyle getFontStyle() {
        return fontStyle;
    }
}