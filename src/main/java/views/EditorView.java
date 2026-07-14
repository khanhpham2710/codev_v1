package views;

import config.Setting;
import enums.ETheme;
import org.fife.ui.rtextarea.RTextScrollPane;

import app.App;
import org.fife.ui.rsyntaxtextarea.*;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

public class EditorView extends RSyntaxTextArea{

    private final RTextScrollPane editorScrollPane;

    private final App app;

    public EditorView(App app) {
        super();
        this.app = app;

        this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        this.setCodeFoldingEnabled(true);
        this.setRoundedSelectionEdges(true);

        editorScrollPane = new RTextScrollPane(this);
    }

    public void setColorScheme(ETheme colorScheme) {
        Setting.getInstance().getTheme(colorScheme).apply(this);
        this.setFont(app.getEditorFont());
    }

    public JScrollPane getContentPanel() {
        return editorScrollPane;
    }
}