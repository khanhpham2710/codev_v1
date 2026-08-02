package views;

import app.App;
import app.AppManager;
import com.formdev.flatlaf.fonts.jetbrains_mono.FlatJetBrainsMonoFont;
import components.Button.BaseButton;
import config.Setting;
import config.Storage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class WelcomeView extends JPanel implements ComponentListener {
    private final JLabel titleLabel;
    private final JLabel mottoLabel;
    private final BaseButton openProjectButton, actionButton;

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

        openProjectButton = new BaseButton("Open Project");
        openProjectButton.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 18));

        openProjectButton.addActionListener(e -> {
            app.openProject();
        });

        BaseButton loginButton = new BaseButton("Login");


        loginButton.addActionListener(e -> {
            AppManager.getInstance().changeView(new LoginView());
        });

        BaseButton quizButton = new BaseButton("Take quiz");
        quizButton.addActionListener(e -> {
            AppManager.getInstance().changeView(new CategoryView());
        });

        this.add(titleLabel);
        this.add(mottoLabel);
        this.add(openProjectButton);


        Storage storage = Storage.getInstance();
        if (storage.isValidToken() && storage.getRememberMe()){
            this.actionButton = quizButton;
        } else {
            this.actionButton = loginButton;
        }

        actionButton.setFont(new Font(FlatJetBrainsMonoFont.FAMILY, Font.PLAIN, 18));

        this.add(actionButton);
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
            actionButton.setBounds(
                    getWidth() / 2 - titleWidth / 4,
                    openProjectButton.getY() + buttonHeight,
                    buttonWidth, buttonHeight);
        }
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

    public void toggleDarkTheme(){
        openProjectButton.applyTheme();
        actionButton.applyTheme();
    }
}
