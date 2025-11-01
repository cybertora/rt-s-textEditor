package com.texteditor.core.pattern.strategy;

public class SpellCheckStrategy implements TextEditingStrategy {
    @Override public String edit(String text) {
        return text.replace("teh", "the").replace("adn", "and");
    }
}