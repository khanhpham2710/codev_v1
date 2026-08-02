package components.Button;

import app.AppManager;

import javax.swing.*;
import java.awt.*;

public class BackButton extends JButton {

    public BackButton() {
        super("← Back");

        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        updateState();

        addActionListener(e -> {
            AppManager manager = AppManager.getInstance();

            if (manager.canGoBack()) {
                manager.back();
            }

            updateState();
        });
    }


    private void updateState() {
        setEnabled(
                AppManager.getInstance().canGoBack()
        );
    }
}