package views;

import app.AppManager;
import com.formdev.flatlaf.FlatClientProperties;
import config.Setting;
import config.Storage;
import config.ThemeConfig;
import enums.ETheme;
import helpers.PasswordHelper;
import helpers.TokenHelper;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class LoginView extends JPanel {
    JPanel loginPanel;
    
    JTextField txtUsername;
    JPasswordField txtPassword;
    JCheckBox chRememberMe;
    JButton cmdLogin;

    Setting setting = Setting.getInstance();
    Storage storage = Storage.getInstance();

    public LoginView() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        chRememberMe = new JCheckBox("Remember me");
        cmdLogin = new JButton("Login");
        loginPanel= new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "fill,250:280"));

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username or email");

        String storedUserName = storage.getUserName();
        if (storedUserName != null && !storedUserName.isBlank()){
            txtUsername.setText(storedUserName);
        }

        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");

        JLabel lbTitle = new JLabel("Welcome back!");
        JLabel description = new JLabel("Please sign in to access your account");

        loginPanel.add(lbTitle);
        loginPanel.add(description);
        loginPanel.add(new JLabel("Username"), "gapy 8");
        loginPanel.add(txtUsername);
        loginPanel.add(new JLabel("Password"), "gapy 8");
        loginPanel.add(txtPassword);
        loginPanel.add(chRememberMe);
        chRememberMe.setSelected(Boolean.TRUE.equals(storage.getRememberMe()));
        loginPanel.add(cmdLogin);
        loginPanel.add(createSignupLabel());
        add(loginPanel);

        cmdLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = String.valueOf(txtPassword.getPassword());

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Username cannot be empty",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Password cannot be empty",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (!username.equals(storage.getUserName())) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (!PasswordHelper.matches(password, storage.getPasswordHash())) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid username or password",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (chRememberMe.isSelected()) {
                storage.setToken(TokenHelper.generateAccessToken());
            } else {
                storage.setToken(null);
            }

            storage.setUserName(username);

            JOptionPane.showMessageDialog(
                    this,
                    "Login successful"
            );

            storage.setRememberMe(chRememberMe.isSelected());
        });

        setColorTheme(setting.getCurrentTheme());
    }

    private Component createSignupLabel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JButton cmdRegister = new JButton("<html><a href=\"#\">Sign up</a></html>");
        cmdRegister.setContentAreaFilled(false);
        cmdRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdRegister.addActionListener(e -> {
            AppManager.getInstance().changeView(new RegisterView());
        });
        JLabel label = new JLabel("Don't have an account ?");
        loginPanel.add(label);
        loginPanel.add(cmdRegister);
        return panel;
    }

    public void setColorTheme(ETheme theme) {
        var t = ThemeConfig.getInstance().getTheme(theme);
        Color bg = t.bgColor;

        setBackground(bg);
        loginPanel.setBackground(t.foldBG);

        repaint();
    }
}
