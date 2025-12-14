package client_package.view;

import javax.swing.*;
import java.awt.*;

public class EmployeePanel extends JPanel {

    private JTable employeeTable;
    private JButton addButton, editButton, deleteButton;

    public EmployeePanel(JFrame parentFrame) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        employeeTable = new JTable();
        employeeTable.setRowHeight(28);

        add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(employeeTable);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Добавить работника");
        editButton = new JButton("Изменить");
        deleteButton = new JButton("Удалить");

        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(deleteButton);

        add(scroll, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            AddEmployeeDialog dialog = new AddEmployeeDialog(parentFrame);
            dialog.setVisible(true);
        });

        editButton.addActionListener(e -> {
            AddEmployeeDialog dialog = new AddEmployeeDialog(parentFrame);
            dialog.setVisible(true);
        });
    }

    public JTable getEmployeeTable() { return employeeTable; }
    public JButton getAddButton() { return addButton; }
}