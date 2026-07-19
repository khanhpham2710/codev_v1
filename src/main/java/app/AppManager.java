package app;

import javax.swing.*;
import java.awt.*;

public class AppManager {
    private App app;
    private static AppManager INSTANCE;

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
            app.setContentPane(form);
            app.revalidate();
            app.repaint();
        });
    }
}
