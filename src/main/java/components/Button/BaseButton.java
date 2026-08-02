package components.Button;

import config.ThemeConfig;

import javax.swing.*;
import java.awt.*;

public class BaseButton extends JButton {

    private static final Color DARK_COLOR =
            new Color(20, 125, 241);

    private static final Color LIGHT_COLOR =
            new Color(12, 182, 41);

    public BaseButton() {
        super();
        applyTheme();
    }


    public BaseButton(String text) {
        super(text);
        applyTheme();
    }


    public void applyTheme() {
        ThemeConfig themeConfig = ThemeConfig.getInstance();

        if (themeConfig.getDarkTheme()) {
            setBackground(DARK_COLOR);
        } else {
            setBackground(LIGHT_COLOR);
        }

        setFocusPainted(false);
    }
}
