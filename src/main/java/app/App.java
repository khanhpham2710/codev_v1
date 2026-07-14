package app;

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
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

    JMenuBar menuBar;
    JMenu settingsMenu;
    JMenuItem closeProjectItem, newProjectItem,
            exitItem;

    Font editorFont;

    public App() {
        setSize(800, 500);
        setTitle("CodeLite");
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

        editorView = new EditorView(this);

        rightSplitPanel = new JPanel();

        toolPanel = new JPanel();
        rootPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, projectView, rightSplitPanel);

        rightSplitPanel.setLayout(new BorderLayout());
        toolPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        rightSplitPanel.add(editorView.getContentPanel(), BorderLayout.CENTER);
        rightSplitPanel.add(toolPanel, BorderLayout.NORTH);

        menuBar = new JMenuBar();
        settingsMenu = new JMenu("Settings", true);

        newProjectItem = new JMenuItem("Open new Project");
        closeProjectItem = new JMenuItem("Close project");

        exitItem = new JMenuItem("Exit Codev");

        newProjectItem.addActionListener(e -> {
            projectView.getProjectTree().removeAll();
            projectView.getRoot().removeAllChildren();
            projectView.openProject();
        });


        closeProjectItem.addActionListener(e -> {
            setContentPane(welcomeView);
            this.setSize(800, 500);
            this.setLocationRelativeTo(null);
        });

        exitItem.addActionListener(e -> System.exit(0));
    }
    public void addComponent() {
        projectView.addComponent();

        menuBar.add(settingsMenu);
        settingsMenu.add(newProjectItem);
        settingsMenu.add(closeProjectItem);

        settingsMenu.addSeparator();

        settingsMenu.add(exitItem);

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
    }

    public EditorView getEditorView(){
        return editorView;
    }

    public ProjectView getProjectView(){
        return projectView;
    }
}
