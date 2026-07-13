package app;

import views.WelcomeView;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    WelcomeView welcomeView;

    public JMenuBar menuBar;

    JMenu settingsMenu;

    public JMenuItem closeProjectItem, newProjectItem;

    public App() {
        setSize(800, 500);
        setTitle("CodeLite");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    public void init() {
        welcomeView = new WelcomeView(this);

        menuBar = new JMenuBar();
        settingsMenu = new JMenu("Settings", true);

        newProjectItem = new JMenuItem("Open new Project");
        closeProjectItem = new JMenuItem("Close project");
    }

    public void addComponent(){
        this.add(welcomeView);


        menuBar.add(settingsMenu);
        settingsMenu.add(newProjectItem);
        settingsMenu.add(closeProjectItem);

        setJMenuBar(menuBar);


        setVisible(true);
    }
}
