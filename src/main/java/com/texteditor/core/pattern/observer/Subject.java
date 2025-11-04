package com.texteditor.core.pattern.observer;

import com.texteditor.core.pattern.observer.interfaces.Observer;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o) { observers.add(o); }
    public void notifyObservers(String content) {
        observers.forEach(o -> o.update(content));
    }
}