package client_package.view;

import client_package.api.AuthApi;
import client_package.model.UserProfile;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {

    private boolean succeeded = false;

    private JTextField loginField;
    private JPasswordField passwordField;
    private UserProfile profile;

    public LoginDialog(Frame parent) {
        super(parent, "Вход в систему", true);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Логин
        c.gridx = 0; c.gridy = 0;
        panel.add(new JLabel("Логин:"), c);

        loginField = new JTextField(15);
        c.gridx = 1;
        panel.add(loginField, c);

        // Пароль
        c.gridx = 0; c.gridy = 1;
        panel.add(new JLabel("Пароль:"), c);

        passwordField = new JPasswordField(15);
        c.gridx = 1;
        panel.add(passwordField, c);

        // Кнопки
        JPanel buttons = new JPanel();
        JButton loginBtn = new JButton("Войти");
        JButton cancelBtn = new JButton("Отмена");

        loginBtn.addActionListener(e -> login());
        cancelBtn.addActionListener(e -> dispose());

        buttons.add(loginBtn);
        buttons.add(cancelBtn);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);

        pack();
        setResizable(false);
        setLocationRelativeTo(getParent());
    }

    private void login() {
        String username = loginField.getText().trim();
        String password = new String(passwordField.getPassword());

        try {
            profile = AuthApi.login(username, password);

            succeeded = true;
            dispose();

        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Ошибка входа",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public JTextField getLoginField() {
        return loginField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public UserProfile getProfile() {
        return profile;
    }
}