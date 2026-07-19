package views;

import app.App;
import app.AppManager;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import config.Setting;
import config.ThemeConfig;
import enums.ETheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class WelcomeView extends JPanel implements ComponentListener {
    private final JLabel titleLabel;
    private final JLabel mottoLabel;
    private final JButton openProjectButton;
    private final JButton loginButton;

    final int titleWidth = 400, titleHeight = 200,
            mottoWidth = 400, mottoHeight = 100,
            buttonWidth = 200, buttonHeight = 50;

    public WelcomeView(App app) {
        this.addComponentListener(this);
        this.setBounds(0, 0, app.getWidth(), app.getHeight());
        this.setLayout(null);
//        this.setOpaque(false);

        titleLabel = new JLabel("{" + Setting.APP_NAME + "}");
        titleLabel.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.BOLD, 52));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mottoLabel = new JLabel("Code editing simplified");
        mottoLabel.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 24));
        mottoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        openProjectButton = new JButton("Open Project");
        openProjectButton.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 18));

        openProjectButton.setBackground(new Color(12, 100, 181));

        openProjectButton.addActionListener(e -> {
            app.openProject();
        });

        loginButton = new JButton("Login");
        loginButton.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 18));

        loginButton.setBackground(new Color(12, 100, 181));

        loginButton.addActionListener(e -> {
            AppManager.getInstance().changeView(new LoginView());
        });

        this.add(titleLabel);
        this.add(mottoLabel);
        this.add(openProjectButton);
        this.add(loginButton);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (titleLabel != null && mottoLabel != null && openProjectButton != null) {
            titleLabel.setBounds(
                    getWidth() / 2 - titleWidth / 2,
                    (getHeight() / 2 - titleHeight / 2) - 50,
                    titleWidth, titleHeight);
            mottoLabel.setBounds(
                    getWidth() / 2 - mottoWidth / 2,
                    titleLabel.getY() + titleHeight / 2,
                    mottoWidth, mottoHeight);
            openProjectButton.setBounds(
                    getWidth() / 2 - titleWidth / 4,
                    mottoLabel.getY() + mottoHeight,
                    buttonWidth, buttonHeight);
            loginButton.setBounds(
                    getWidth() / 2 - titleWidth / 4,
                    openProjectButton.getY() + buttonHeight,
                    buttonWidth, buttonHeight);
        }
    }

    public void setColorTheme(ETheme theme) {
        var t = ThemeConfig.getInstance().getTheme(theme);
        Color bg = t.bgColor;

        setBackground(bg);

        titleLabel.setForeground(t.caretColor);
        mottoLabel.setForeground(t.caretColor);

        repaint();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
