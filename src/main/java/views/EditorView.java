package views;

import org.fife.ui.rtextarea.RTextScrollPane;

import app.App;
import org.fife.ui.rsyntaxtextarea.*;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

public class EditorView extends RSyntaxTextArea{

    private final RTextScrollPane editorScrollPane;

    InputStream monokaiStream, eclipseStream, nightStream, redStream, blueStream, purpleStream;
    Theme monokaiTheme, eclipseTheme, nightTheme, redTheme, blueTheme, purpleTheme;

    private final App app;

    public EditorView(App app) {
        super();
        this.app = app;

        monokaiStream = EditorView.class.getResourceAsStream("/themes/monokai.xml");
        eclipseStream = EditorView.class.getResourceAsStream("/themes/eclipse.xml");
        nightStream = EditorView.class.getResourceAsStream("/themes/night.xml");
        redStream = EditorView.class.getResourceAsStream("/themes/red.xml");
        blueStream = EditorView.class.getResourceAsStream("/themes/blue.xml");
        purpleStream = EditorView.class.getResourceAsStream("/themes/purple.xml");

        try {
            monokaiTheme = Theme.load(monokaiStream);
            eclipseTheme = Theme.load(eclipseStream);
            nightTheme = Theme.load(nightStream);
            redTheme = Theme.load(redStream);
            blueTheme = Theme.load(blueStream);
            purpleTheme = Theme.load(purpleStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        this.setCodeFoldingEnabled(true);
        this.setRoundedSelectionEdges(true);

        editorScrollPane = new RTextScrollPane(this);
    }

    public void setColorScheme(String colorScheme) {
        switch (colorScheme) {
            case "Monokai" : monokaiTheme.apply(this);
                break;
            case "Eclipse" : eclipseTheme.apply(this);
                break;
            case "Night" : nightTheme.apply(this);
                break;
            case "Red" : redTheme.apply(this);
                break;
            case "Blue" : blueTheme.apply(this);
                break;
            case "Purple" : purpleTheme.apply(this);
                break;
        }
        this.setFont(app.getEditorFont());
    }

    public JScrollPane getContentPanel() {
        return editorScrollPane;
    }
}