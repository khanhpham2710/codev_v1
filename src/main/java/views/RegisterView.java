package views;

import app.AppManager;
import com.formdev.flatlaf.FlatClientProperties;
import config.Setting;
import config.ThemeConfig;
import enums.ETheme;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class RegisterView extends JPanel {
    JPanel registerPanel;
    
    JTextField txtFirstName;
    JTextField txtLastName;
    JRadioButton jrMale;
    JRadioButton jrFemale;
    JTextField txtUsername;
    JPasswordField txtPassword;
    JPasswordField txtConfirmPassword;
    ButtonGroup groupGender;
    JButton cmdRegister;
    PasswordStrengthStatus passwordStrengthStatus;

    Setting setting = Setting.getInstance();

    public RegisterView() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));
        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();
        cmdRegister = new JButton("Sign Up");

        cmdRegister.addActionListener(e -> {
            if (isMatchPassword()) {
                //  Do something here
            } else {

            }
        });
        passwordStrengthStatus = new PasswordStrengthStatus();

        registerPanel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));

        txtFirstName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "First name");
        txtLastName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Last name");
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username or email");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Re-enter your password");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");
        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "" +
                "showRevealButton:true");

        JLabel lbTitle = new JLabel("Welcome to " + Setting.APP_NAME);
        JLabel description = new JLabel("Login to do coding quiz");

        passwordStrengthStatus.initPasswordField(txtPassword);

        registerPanel.add(lbTitle);
        registerPanel.add(description);
        registerPanel.add(new JLabel("Full Name"), "gapy 10");
        registerPanel.add(txtFirstName, "split 2");
        registerPanel.add(txtLastName);
        registerPanel.add(new JLabel("Gender"), "gapy 8");
        registerPanel.add(createGenderPanel());
        registerPanel.add(new JLabel("Username or Email"));
        registerPanel.add(txtUsername);
        registerPanel.add(new JLabel("Password"), "gapy 8");
        registerPanel.add(txtPassword);
        registerPanel.add(passwordStrengthStatus, "gapy 0");
        registerPanel.add(new JLabel("Confirm Password"), "gapy 0");
        registerPanel.add(txtConfirmPassword);
        registerPanel.add(cmdRegister, "gapy 20");
        registerPanel.add(createLoginLabel(), "gapy 10");
        add(registerPanel);

        setColorTheme(setting.getCurrentTheme());
    }

    private Component createGenderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);

        jrMale = new JRadioButton("Male");
        jrFemale = new JRadioButton("Female");

        jrMale.setOpaque(false);
        jrFemale.setOpaque(false);

        groupGender = new ButtonGroup();
        groupGender.add(jrMale);
        groupGender.add(jrFemale);

        jrMale.setSelected(true);

        panel.add(jrMale);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(jrFemale);

        return panel;
    }

    private Component createLoginLabel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JButton cmdLogin = new JButton("<html><a href=\"#\">Sign in here</a></html>");
        cmdLogin.setContentAreaFilled(false);
        cmdLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdLogin.addActionListener(e -> {
            AppManager.getInstance().changeView(new LoginView());
        });
        JLabel label = new JLabel("Already have an account ?");
        registerPanel.add(label);
        registerPanel.add(cmdLogin);
        return panel;
    }

    public boolean isMatchPassword() {
        String password = String.valueOf(txtPassword.getPassword());
        String confirmPassword = String.valueOf(txtConfirmPassword.getPassword());
        return password.equals(confirmPassword);
    }

    public void setColorTheme(ETheme theme) {
        var t = ThemeConfig.getInstance().getTheme(theme);
        Color bg = t.bgColor;

        setBackground(bg);
        registerPanel.setBackground(t.foldBG);

        repaint();
    }
}
