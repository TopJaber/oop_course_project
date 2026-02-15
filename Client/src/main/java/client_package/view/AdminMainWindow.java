package client_package.view;

import client_package.model.UserProfile;

import javax.swing.*;

public class AdminMainWindow extends JFrame {

    private final UserProfile profile;

    public AdminMainWindow(UserProfile profile) {
        this.profile = profile;

        setTitle("Админка — " + profile.getUsername());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JLabel label = new JLabel("Вы вошли как ADMIN");
        add(label);
    }
}