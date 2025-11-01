package com.texteditor.core.pattern.strategy;

public class UpperCaseStrategy implements TextEditingStrategy {
    @Override public String edit(String text) { return text.toUpperCase(); }
}