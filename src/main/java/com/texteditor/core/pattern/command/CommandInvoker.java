package com.texteditor.core.pattern.command;

import java.util.Stack;

public class CommandInvoker {
    private final Stack<Command> history = new Stack<>();

    public void executeCommand(Command cmd) {
        cmd.execute();
        history.push(cmd);
    }

    public void undo() {
        if (!history.isEmpty()) history.pop().undo();
    }
}