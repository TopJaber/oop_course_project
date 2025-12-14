package client_package.view;

import javax.swing.*;
import java.awt.*;

public class AddEmployeeDialog extends JDialog {
    private JTextField fioField = new JTextField(20);
    private JTextField positionField = new JTextField(20);
    private JTextField phoneField = new JTextField(20);
    private JTextField emailField = new JTextField(20);

    public AddEmployeeDialog(JFrame parent) {
        super(parent, "Добавить работника", true);
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
        form.add(new JLabel("Должность:"), c);
        c.gridx = 1;
        form.add(positionField, c);

        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Email:"), c);
        c.gridx = 1;
        form.add(emailField, c);

        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Номер телефона:"), c);
        c.gridx = 1;
        form.add(phoneField, c);

        JPanel buttons = new JPanel();
        JButton save = new JButton("Сохранить");
        JButton cancel = new JButton("Отмена");

        buttons.add(save);
        buttons.add(cancel);

        cancel.addActionListener(e -> dispose());

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }
}
