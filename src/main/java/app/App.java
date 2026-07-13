package app;

import views.WelcomeView;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    WelcomeView welcomeView;

    public App() {
        setSize(800, 500);
        setTitle("CodeLite");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    public void init() {
        welcomeView = new WelcomeView(this);
    }

    public void addComponent(){
        this.add(welcomeView);

        setVisible(true);
    }
}
