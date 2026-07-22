package app;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class AppManager {
    private App app;
    private static AppManager INSTANCE;
    private final Stack<JComponent> history = new Stack<>();

    private JComponent currentView;


    public static AppManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppManager();
        }
        return INSTANCE;
    }

    private AppManager() {

    }

    public void initApplication(App app) {
        this.app = app;
    }

    public void changeView(JComponent form) {
        EventQueue.invokeLater(() -> {
            if (currentView != null) {
                history.push(currentView);
            }

            currentView = form;

            app.setContentPane(form);
            app.revalidate();
            app.repaint();

        });
    }

    public void back() {
        EventQueue.invokeLater(() -> {
            if (!history.isEmpty()) {
                currentView = history.pop();

                app.setContentPane(currentView);
                app.revalidate();
                app.repaint();
            }
        });
    }

    public boolean canGoBack() {
        return !history.isEmpty();
    }
}
