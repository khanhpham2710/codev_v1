package views;

import app.App;
import config.Setting;
import config.ThemeConfig;
import enums.ETheme;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;

public class EditorView extends RSyntaxTextArea {

    private final RTextScrollPane editorScrollPane;

    private boolean modified = false;

    private Setting setting = Setting.getInstance();

    public EditorView() {
        super();

        this.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        this.setCodeFoldingEnabled(true);
        this.setRoundedSelectionEdges(true);

        editorScrollPane = new RTextScrollPane(this);

        getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                modified = true;
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                modified = true;
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                modified = true;
            }
        });
    }

    public void setColorScheme(ETheme colorScheme) {
        ThemeConfig.getInstance().getTheme(colorScheme).apply(this);
        this.setFont(setting.getEditorFont());
    }

    public JScrollPane getContentPanel() {
        return editorScrollPane;
    }

    public boolean getIsModified() {
        return modified;
    }

    public void setIsModified(boolean modified) {
        this.modified = modified;
    }
}