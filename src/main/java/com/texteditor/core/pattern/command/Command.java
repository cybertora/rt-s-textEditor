package com.texteditor.core.pattern.command;

public interface Command {
    void execute();
    void undo();
}