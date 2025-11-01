package com.texteditor.core.pattern.strategy;

public class LowerCaseStrategy implements TextEditingStrategy {
    @Override public String edit(String text) { return text.toLowerCase(); }
}