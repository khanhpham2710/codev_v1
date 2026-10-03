package app;

import config.CurrentUser;
import config.Storage;
import dto.UserDTO;
import entites.UserEntity;
import mapper.UserMapper;
import views.WelcomeView;

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

    public void login(UserEntity userEntity){
        UserDTO userDTO = UserMapper.toDTO(userEntity);

        login(userDTO);
    }

    public void login(UserDTO userDTO){
        Storage storage = Storage.getInstance();

        String userName = userDTO.getUserName();
        if (userName != null) storage.setUserName(userDTO.getUserName());

        CurrentUser.getInstance().login(userDTO);
        app.addAccountMenu();
    }

    public void signOut() {
        CurrentUser.getInstance().logout();

        Storage storage = Storage.getInstance();
        storage.setToken(null);
        storage.setUserName(null);
        storage.save();

        EventQueue.invokeLater(() -> {
            history.clear();
            currentView = new WelcomeView(app);

            app.setContentPane(currentView);
            app.revalidate();
            app.repaint();
        });
    }
}
