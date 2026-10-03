package views;

import components.Button.BackButton;
import config.CurrentUser;
import config.Storage;
import dto.UserDTO;
import enums.EGender;
import service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class ProfileView extends JPanel {
    private final UUID userId;
    private final UserService service;
    private final JTextField username = new JTextField(24), id = new JTextField(24),
            firstName = new JTextField(24), lastName = new JTextField(24);
    private final JComboBox<EGender> gender = new JComboBox<>(EGender.values());
    private final JPasswordField current = new JPasswordField(24), password = new JPasswordField(24),
            confirmation = new JPasswordField(24);
    private final JButton save = new JButton("Save profile"), change = new JButton("Change password"),
            reload = new JButton("Reload profile");
    private final JLabel status = new JLabel(" ");
    private boolean loaded;

    public ProfileView() {
        this(CurrentUser.getInstance().getCurrentUserId(), new UserService());
    }

    public ProfileView(UUID userId, UserService service) {
        this.userId = java.util.Objects.requireNonNull(userId);
        this.service = java.util.Objects.requireNonNull(service);
        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(16, 16, 16, 16));
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.add(new BackButton());
        JLabel title = new JLabel("My profile");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        header.add(title); header.add(reload);
        add(header, BorderLayout.NORTH);
        username.setEditable(false); id.setEditable(false);
        JPanel form = new JPanel(new GridBagLayout());
        row(form, 0, "Username", username);
        row(form, 1, "Account ID", id);
        row(form, 2, "First name", firstName);
        row(form, 3, "Last name", lastName);
        row(form, 4, "Gender", gender);
        row(form, 5, "", save);
        row(form, 6, "Current password", current);
        row(form, 7, "New password", password);
        row(form, 8, "Confirm new password", confirmation);
        row(form, 9, "", new JLabel("At least 8 characters; at most 72 UTF-8 bytes."));
        row(form, 10, "", change);
        JPanel body = new JPanel(new BorderLayout()); body.add(form, BorderLayout.NORTH);
        add(new JScrollPane(body), BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);
        reload.addActionListener(e -> load());
        save.addActionListener(e -> saveProfile());
        change.addActionListener(e -> changePassword());
        load();
    }

    private void row(JPanel panel, int y, String label, Component input) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = y; c.insets = new Insets(6, 6, 6, 10); c.anchor = GridBagConstraints.WEST;
        c.gridx = 0; panel.add(new JLabel(label), c);
        c.gridx = 1; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL; panel.add(input, c);
    }

    private void busy(boolean busy) {
        reload.setEnabled(!busy); save.setEnabled(!busy && loaded); change.setEnabled(!busy && loaded);
        firstName.setEnabled(!busy && loaded); lastName.setEnabled(!busy && loaded);
        gender.setEnabled(!busy && loaded); current.setEnabled(!busy && loaded);
        password.setEnabled(!busy && loaded); confirmation.setEnabled(!busy && loaded);
    }

    private <T> void run(String progress, Callable<T> work, Consumer<T> success) {
        busy(true); status.setText(progress);
        new SwingWorker<T, Void>() {
            @Override protected T doInBackground() throws Exception { return work.call(); }
            @Override protected void done() {
                try { success.accept(get()); }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); status.setText("Operation interrupted.");
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    status.setText(cause instanceof IllegalArgumentException ? cause.getMessage()
                            : "Database operation failed. Check your connection and try again.");
                } finally { busy(false); }
            }
        }.execute();
    }

    private void load() {
        run("Loading profile...", () -> service.getProfile(userId), user -> {
            username.setText(user.getUserName()); id.setText(user.getId().toString());
            firstName.setText(user.getFirstName()); lastName.setText(user.getLastName());
            gender.setSelectedItem(user.getGender()); loaded = true; status.setText("Profile loaded.");
        });
    }

    private void saveProfile() {
        String first = firstName.getText(), last = lastName.getText();
        EGender value = (EGender) gender.getSelectedItem();
        run("Saving profile...", () -> service.updateProfile(userId, first, last, value), user -> {
            if (CurrentUser.getInstance().isLoggedIn()
                    && userId.equals(CurrentUser.getInstance().getCurrentUserId())) {
                CurrentUser.getInstance().login(user);
            }
            firstName.setText(user.getFirstName()); lastName.setText(user.getLastName());
            status.setText("Profile saved. Your password has not changed.");
        });
    }

    private void changePassword() {
        char[] old = current.getPassword(), next = password.getPassword(), repeat = confirmation.getPassword();
        current.setText(""); password.setText(""); confirmation.setText("");
        run("Changing password...", () -> {
            try { service.changePassword(userId, old, next, repeat); return true; }
            finally { Arrays.fill(old, '\0'); Arrays.fill(next, '\0'); Arrays.fill(repeat, '\0'); }
        }, success -> {
            // Revoke the locally remembered login after changing credentials.
            Storage storage = Storage.getInstance();
            storage.setRememberMe(false); storage.setToken(null); storage.setUserName(null); storage.save();
            status.setText("Password changed. Remember me has been cleared on this device.");
        });
    }
}
