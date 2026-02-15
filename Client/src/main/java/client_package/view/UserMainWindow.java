package client_package.view;

import client_package.model.UserProfile;

import javax.swing.*;

public class UserMainWindow extends JFrame {

    private final UserProfile profile;

    public UserMainWindow(UserProfile profile) {
        this.profile = profile;

        setTitle("Приложение — " + profile.getUsername());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JLabel label = new JLabel("Обычный пользователь");
        add(label);
    }
}