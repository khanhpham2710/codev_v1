package app;

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import config.Setting;
import enums.ETheme;
import views.EditorView;
import views.ProjectView;
import views.WelcomeView;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    WelcomeView welcomeView;
    JSplitPane rootPanel;
    ProjectView projectView;
    EditorView editorView;

    JPanel rightSplitPanel;
    JPanel toolPanel;

    JButton saveFileButton;

    JMenuBar menuBar;
    JMenu settingsMenu, colorSchemeItem;
    JMenuItem closeProjectItem, newProjectItem,
            monokaiItem, eclipseItem, nightItem, redItem, blueItem, purpleItem,
            autoSaveItem,
            exitItem;

    boolean autoSave = false;
    Timer autoSaveTimer;

    String currentFileParentPath;

    Font editorFont;

    public App() {
        setSize(800, 500);
        setTitle(Setting.APP_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    public void init() {
        editorFont = new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 18);

        welcomeView = new WelcomeView(this);

        projectView = new ProjectView(this);
        projectView.setMinimumSize(new Dimension(200, 0));
        projectView.init();
        projectView.initActionListeners();

        editorView = new EditorView(this);

        rightSplitPanel = new JPanel();

        toolPanel = new JPanel();
        rootPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectView, rightSplitPanel);

        rightSplitPanel.setLayout(new BorderLayout());
        toolPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        rightSplitPanel.add(editorView.getContentPanel(), BorderLayout.CENTER);
        rightSplitPanel.add(toolPanel, BorderLayout.NORTH);

        saveFileButton = new JButton("Save");
        saveFileButton.setEnabled(false);
        saveFileButton.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 14));
        saveFileButton.setBackground(new Color(67, 175, 21));
        saveFileButton.addActionListener(e -> projectView.saveFile());

        menuBar = new JMenuBar();
        settingsMenu = new JMenu("Settings", true);

        newProjectItem = new JMenuItem("Open new Project");
        closeProjectItem = new JMenuItem("Close project");

        colorSchemeItem = new JMenu("Color scheme");
        monokaiItem = new JMenuItem("Monokai");
        eclipseItem = new JMenuItem("Eclipse");
        nightItem = new JMenuItem("Night");
        redItem = new JMenuItem("Reversal Red");
        blueItem = new JMenuItem("Amplified Blue");
        purpleItem = new JMenuItem("Hollow Purple");

        autoSaveItem = new JMenuItem("Auto save : Off");
        exitItem = new JMenuItem("Exit " + Setting.APP_NAME);

        newProjectItem.addActionListener(e -> {
            projectView.getProjectTree().removeAll();
            projectView.getRoot().removeAllChildren();
            projectView.openProject();
        });

        autoSaveTimer = new Timer(5000, e -> {   // every 5 seconds
            if (projectView.getSelectedFileNode() != null && editorView.canUndo()) {
                projectView.saveFile();
            }
        });
        autoSaveTimer.setRepeats(true);

        autoSaveItem.addActionListener(e -> {
            if (autoSave) {
                autoSave = false;
                autoSaveItem.setText("Auto save : Off");
                saveFileButton.setVisible(true);
                autoSaveTimer.stop();
            }
            else {
                autoSave = true;
                autoSaveItem.setText("Auto save : On");
                saveFileButton.setVisible(false);
                autoSaveTimer.start();
            }
        });


        closeProjectItem.addActionListener(e -> {
            setContentPane(welcomeView);
            this.setSize(800, 500);
            this.setLocationRelativeTo(null);
        });

        monokaiItem.addActionListener(e -> {
            setColorScheme(ETheme.MONOKAI);
        });
        eclipseItem.addActionListener(e -> {
            setColorScheme(ETheme.ECLIPSE);
        });
        nightItem.addActionListener(e -> {
            setColorScheme(ETheme.NIGHT);
        });
        redItem.addActionListener(e -> {
            setColorScheme(ETheme.RED);
        });
        blueItem.addActionListener(e -> {
            setColorScheme(ETheme.BLUE);
        });
        purpleItem.addActionListener(e -> {
            setColorScheme(ETheme.PURPLE);
        });

        exitItem.addActionListener(e -> System.exit(0));
    }
    public void addComponent() {
        projectView.addComponent();

        menuBar.add(settingsMenu);
        settingsMenu.add(newProjectItem);
        settingsMenu.add(closeProjectItem);

        settingsMenu.addSeparator();
        settingsMenu.add(colorSchemeItem);
        settingsMenu.addSeparator();
        settingsMenu.add(autoSaveItem);
        settingsMenu.addSeparator();

        colorSchemeItem.add(monokaiItem);
        colorSchemeItem.add(eclipseItem);
        colorSchemeItem.add(nightItem);
        colorSchemeItem.add(redItem);
        colorSchemeItem.add(blueItem);
        colorSchemeItem.add(purpleItem);

        settingsMenu.add(exitItem);

        toolPanel.add(saveFileButton);

        this.add(rootPanel, BorderLayout.CENTER);
        this.add(welcomeView);
        setJMenuBar(menuBar);

        revalidate();
        repaint();

        setVisible(true);
    }

    public void launch() {
        setContentPane(rootPanel);

        this.setExtendedState(MAXIMIZED_BOTH);
        setColorScheme(ETheme.MONOKAI);
    }

    public EditorView getEditorView(){
        return editorView;
    }

    public ProjectView getProjectView(){
        return projectView;
    }

    public Font getEditorFont() { return editorFont; };

    public JButton getSaveFileButton(){
        return saveFileButton;
    }

    public void setCurrentFileParentPath(String currentFileParentPath){
        this.currentFileParentPath = currentFileParentPath;
    }

    private void setColorScheme(ETheme theme){
        editorView.setColorScheme(theme);
        projectView.setColorTheme(theme);
        welcomeView.setColorTheme(theme);
    }
}
