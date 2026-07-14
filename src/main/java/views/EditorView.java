package views;

import org.fife.ui.rtextarea.RTextScrollPane;

import app.App;
import org.fife.ui.rsyntaxtextarea.*;

import javax.swing.*;

public class EditorView extends RSyntaxTextArea{

    private final RTextScrollPane editorScrollPane;

    private final App app;

    public EditorView(App app) {
        super();
        this.app = app;
        editorScrollPane = new RTextScrollPane(this);
    }

    public JScrollPane getContentPanel() {
        return editorScrollPane;
    }
}