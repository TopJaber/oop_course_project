package client_package.view;

import client_package.model.EmployeeDTO;

import javax.swing.*;
import java.awt.*;

public class AddEmployeeDialog extends JDialog {
    private boolean saved = false;

    private JTextField fioField = new JTextField(20);
    private JTextField positionField = new JTextField(20);
    private JTextField phoneField = new JTextField(20);
    private JTextField emailField = new JTextField(20);

    private EmployeeDTO employee; // редактируемый сотрудник

    // Конструктор для добавления нового сотрудника
    public AddEmployeeDialog(JFrame parent) {
        this(parent, null);
    }

    // Конструктор для редактирования существующего сотрудника
    public AddEmployeeDialog(JFrame parent, EmployeeDTO employee) {
        super(parent, employee == null ? "Добавить работника" : "Редактировать работника", true);
        this.employee = employee;

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

        // Если редактирование — заполняем поля текущими значениями
        if (employee != null) {
            fioField.setText(employee.getFio());
            positionField.setText(employee.getPosition());
            emailField.setText(employee.getEmail());
            phoneField.setText(employee.getPhone());
        }

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

    public EmployeeDTO getEmployeeDto() {
        EmployeeDTO dto = employee != null ? employee : new EmployeeDTO();
        dto.setFio(fioField.getText());
        dto.setPosition(positionField.getText());
        dto.setEmail(emailField.getText());
        dto.setPhone(phoneField.getText());
        return dto;
    }
}
