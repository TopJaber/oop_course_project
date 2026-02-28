package client_package.view;

import client_package.api.UserApi;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordDialog extends JDialog {

    private JPasswordField oldPasswordField = new JPasswordField(15);
    private JPasswordField newPasswordField = new JPasswordField(15);
    private JPasswordField confirmPasswordField = new JPasswordField(15);

    public ChangePasswordDialog() {
        setTitle("Смена пароля");
        setSize(300, 200);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("Старый пароль:"));
        add(oldPasswordField);

        add(new JLabel("Новый пароль:"));
        add(newPasswordField);

        add(new JLabel("Подтвердите пароль:"));
        add(confirmPasswordField);

        JButton saveButton = new JButton("Сохранить");
        add(saveButton);

        saveButton.addActionListener(e -> changePassword());

        setLocationRelativeTo(null);
        setModal(true);
        setVisible(true);
    }

    private void changePassword() {
        String oldPass = new String(oldPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this,
                    "Пароли не совпадают");
            return;
        }

        UserApi.changePassword(oldPass, newPass);
        dispose();
    }
}