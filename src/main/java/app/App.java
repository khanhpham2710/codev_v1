package app;

import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import config.Setting;
import config.Storage;
import entites.FileNode;
import enums.ETheme;
import helpers.LanguageChecker;
import views.EditorView;
import views.ProjectView;
import views.WelcomeView;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class App extends JFrame {
    WelcomeView welcomeView;
    JSplitPane rootPanel;
    ProjectView projectView;
    EditorView editorView;

    JPanel rightSplitPanel;
    JPanel toolPanel;

    JButton openTerminalButton;
    JButton saveFileButton, runFileButton;

    JMenuBar menuBar;
    JMenu settingsMenu, colorSchemeItem, languageItem;;
    JMenuItem closeProjectItem, newProjectItem,
            monokaiItem, eclipseItem, nightItem, redItem, blueItem, purpleItem,
            javaItem, pythonItem, jsItem,
            autoSaveItem,
            exitItem;

    Storage storage = Storage.getInstance();
    Setting setting = Setting.getInstance();

    boolean autoSave = false;
    Timer autoSaveTimer;

    public String os = System.getProperty("os.name").toLowerCase();
    public String currentFileParentPath;
    public ProcessBuilder pb;

    public App() {
        setSize(800, 500);
        setTitle(Setting.APP_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                storage.save();
            }
        });

    }

    public void init() {
        welcomeView = new WelcomeView(this);

        projectView = new ProjectView(this);
        projectView.setMinimumSize(new Dimension(200, 0));
        projectView.init();
        projectView.initActionListeners();

        editorView = new EditorView();

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

        openTerminalButton = new JButton("Open Terminal");
        openTerminalButton.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 14));
        openTerminalButton.setBackground(new Color(30, 126, 248));
        openTerminalButton.addActionListener(e -> {
            try {
                if (os.contains("win")) {
                    pb = new ProcessBuilder("cmd" , "/c" , "start" , "powershell.exe");
                } else if (os.contains("mac")) {
                    pb = new ProcessBuilder("open", "-a", "Terminal");
                } else if (os.contains("nix") || os.contains("nux") || os.contains("bsd")) {
                    pb = new ProcessBuilder("x-terminal-emulator");
                } else
                    JOptionPane.showMessageDialog(null, "Unsupported Operating System", "Error", JOptionPane.ERROR_MESSAGE);

                FileNode selected = projectView.getSelectedFileNode();

                if (selected == null) {
                    pb.directory(new File(projectView.getProjectPath()));
                } else if (selected.getIsDirectory()) {
                    pb.directory(new File(selected.getFilePath()));
                } else {
                    File parent = new File(selected.getFilePath()).getParentFile();
                    pb.directory(parent);
                }
                pb.start();

            } catch (IOException ex) {
                System.err.println("Failed to open terminal: " + ex.getMessage());
            } catch (UnsupportedOperationException ex) {
                System.err.println(ex.getMessage());
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "Select a file to open the terminal", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        runFileButton = new BasicArrowButton(BasicArrowButton.EAST) {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int arc = 8;

                ButtonModel model = getModel();

                // Background: transparent normally, tinted on hover/press
                if (model.isPressed()) {
                    g2.setColor(new Color(30, 126, 248, 60));
                    g2.fillRoundRect(0, 0, w - 1, h - 1, arc, arc);
                } else if (model.isRollover()) {
                    g2.setColor(new Color(30, 126, 248, 30));
                    g2.fillRoundRect(0, 0, w - 1, h - 1, arc, arc);
                }

                // Border
                g2.setColor(model.isRollover() ? new Color(30, 126, 248) : new Color(30, 126, 248, 120));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

                // Arrow
                int[] xPoints = { w / 3, w * 2 / 3, w / 3 };
                int[] yPoints = { h / 4, h / 2, h * 3 / 4 };

                g2.setColor(new Color(30, 126, 248));
                g2.fillPolygon(xPoints, yPoints, 3);

                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(28, 28);
            }
        };

        runFileButton.setOpaque(false);
        runFileButton.setContentAreaFilled(false);
        runFileButton.setBorderPainted(false);
        runFileButton.setFocusPainted(false);

        runFileButton.setRolloverEnabled(true);
        runFileButton.getModel().addChangeListener(e -> runFileButton.repaint());

        runFileButton.addActionListener(e -> {
            projectView.runFile();
        });

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

        languageItem = new JMenu("Language support");
        javaItem = new JMenuItem("Java");
        pythonItem = new JMenuItem("Python");
        jsItem = new JMenuItem("Javascript");


        autoSaveItem = new JMenuItem("Auto save : Off");
        exitItem = new JMenuItem("Exit " + Setting.APP_NAME);

        newProjectItem.addActionListener(e -> {
            openProject();
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

        ETheme localTheme = storage.getTheme();
        setColorScheme(localTheme);

        AppManager.getInstance().initApplication(this);
    }
    public void addComponent() {
        projectView.addComponent();

        menuBar.add(settingsMenu);
        settingsMenu.add(newProjectItem);
        settingsMenu.add(closeProjectItem);

        settingsMenu.addSeparator();
        settingsMenu.add(colorSchemeItem);
        settingsMenu.addSeparator();
        settingsMenu.add(languageItem);
        settingsMenu.addSeparator();
        settingsMenu.add(autoSaveItem);
        settingsMenu.addSeparator();

        colorSchemeItem.add(monokaiItem);
        colorSchemeItem.add(eclipseItem);
        colorSchemeItem.add(nightItem);
        colorSchemeItem.add(redItem);
        colorSchemeItem.add(blueItem);
        colorSchemeItem.add(purpleItem);


        boolean isNodeInstalled = LanguageChecker.isNodeInstalled();

        if (isNodeInstalled){
            languageItem.add(jsItem);
        }

        boolean isJavaInstalled = LanguageChecker.isJavaInstalled();

        if (isJavaInstalled){
            languageItem.add(javaItem);
        }

        boolean isPythonInstalled = LanguageChecker.isPythonInstalled();

        if (isPythonInstalled){
            languageItem.add(pythonItem);
        }

        settingsMenu.add(exitItem);

        toolPanel.add(openTerminalButton);
        toolPanel.add(saveFileButton);
        toolPanel.add(runFileButton);

        this.add(rootPanel, BorderLayout.CENTER);
        this.add(welcomeView);
        setJMenuBar(menuBar);

        revalidate();
        repaint();

        setVisible(true);
    }

    public EditorView getEditorView(){
        return editorView;
    }

    public JButton getSaveFileButton(){
        return saveFileButton;
    }

    public JButton getRunFileButton() {
        return runFileButton;
    }

    public void setCurrentFileParentPath(String currentFileParentPath){
        this.currentFileParentPath = currentFileParentPath;
    }

    private void setColorScheme(ETheme theme){
        editorView.setColorScheme(theme);
        projectView.setColorTheme(theme);
        welcomeView.setColorTheme(theme);

        storage.setTheme(theme);
        setting.setCurrentTheme(theme);
    }

    public void openProject() {
        projectView.openProject();
        setContentPane(rootPanel);

        this.setExtendedState(MAXIMIZED_BOTH);
    }
}
