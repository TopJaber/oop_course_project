package client_package.view;

import client_package.model.*;

import javax.swing.*;
import java.awt.*;

public class AddClientDialog extends JDialog {
    private boolean saved = false;

    private JTextField fioField = new JTextField(20);
    private JTextField phoneField = new JTextField(20);
    private JTextField emailField = new JTextField(20);
    private JTextField noteField = new JTextField(20);

    public AddClientDialog(JFrame parent) {
        super(parent, "Добавить клиента", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("ФИО:"), c);
        c.gridx = 1;
        form.add(fioField, c);

        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Номер телефона:"), c);
        c.gridx = 1;
        form.add(phoneField, c);

        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Email:"), c);
        c.gridx = 1;
        form.add(emailField, c);

        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Задача:"), c);
        c.gridx = 1;
        form.add(noteField, c);

        JPanel buttons = new JPanel();
        JButton save = new JButton("Сохранить");
        JButton cancel = new JButton("Отмена");

        buttons.add(save);
        buttons.add(cancel);

        save.addActionListener(e -> {
            saved = true;
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    public boolean isSaved() {
        return saved;
    }

    public ClientDTO getClientDto() {
        ClientDTO dto = new ClientDTO();
        dto.setFio(fioField.getText());
        dto.setPhone(phoneField.getText());
        dto.setEmail(emailField.getText());
        return dto;
    }
}