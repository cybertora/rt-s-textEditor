module com.texteditor {
    requires java.desktop;  // Swing, UndoManager
    // Apache POI (автоматически через Maven, но требует явного requires)
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    // Apache PDFBox
    requires org.apache.pdfbox;

    exports com.texteditor.ui;
    exports com.texteditor.util;
    exports com.texteditor.core.document;
    exports com.texteditor.core.pattern.observer;
    exports com.texteditor.core.pattern.strategy;
}