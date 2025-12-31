package client_package.view;

import client_package.controller.EmployeeController;
import client_package.model.EmployeeDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeePanel extends JPanel {

    private JTable employeeTable;
    private JButton addButton, editButton, deleteButton;
    private EmployeeController employeeController;
    private JFrame parentFrame;

    public EmployeePanel(EmployeeController employeeController, JFrame parentFrame) {
        this.employeeController = employeeController;
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        employeeTable = new JTable();
        employeeTable.setRowHeight(28);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(employeeTable);

        addButton = new JButton("Добавить работника");
        editButton = new JButton("Изменить");
        deleteButton = new JButton("Удалить");

        deleteButton.setBackground(new Color(0xF44336));
        deleteButton.putClientProperty("JButton.hoverBackground", new Color(0xE53935));
        deleteButton.putClientProperty("JButton.pressedBackground", new Color(0xC62828));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(deleteButton);

        add(scroll, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        initButtons();
        refreshTable();
    }

    private void initButtons() {
        // Добавление сотрудника
        addButton.addActionListener(e -> {
            AddEmployeeDialog dialog = new AddEmployeeDialog(parentFrame);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                EmployeeDTO dto = dialog.getEmployeeDto();
                employeeController.createEmployee(dto);
                refreshTable();
            }
        });

        // Редактирование сотрудника
        editButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Выберите сотрудника для редактирования");
                return;
            }

            EmployeeDTO selectedEmployee = getSelectedEmployeeFromTable(selectedRow);
            AddEmployeeDialog dialog = new AddEmployeeDialog(parentFrame, selectedEmployee);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                EmployeeDTO updatedEmployee = dialog.getEmployeeDto();
                employeeController.updateEmployee(updatedEmployee);
                refreshTable();
            }
        });

        // Удаление сотрудника
        deleteButton.addActionListener(e -> {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Выберите сотрудника для удаления");
                return;
            }

            EmployeeDTO selectedEmployee = getSelectedEmployeeFromTable(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(parentFrame,
                    "Удалить сотрудника " + selectedEmployee.getFio() + "?",
                    "Подтвердите удаление", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                employeeController.deleteEmployee(selectedEmployee.getId());
                refreshTable();
            }
        });
    }

    // Получение DTO сотрудника из выбранной строки
    private EmployeeDTO getSelectedEmployeeFromTable(int row) {
        DefaultTableModel model = (DefaultTableModel) employeeTable.getModel();
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId((Long) model.getValueAt(row, 0));
        employee.setFio((String) model.getValueAt(row, 1));
        employee.setPosition((String) model.getValueAt(row, 2));
        employee.setEmail((String) model.getValueAt(row, 3));
        return employee;
    }

    // Обновление таблицы с сотрудниками
    public void refreshTable() {
        List<EmployeeDTO> employees = employeeController.getAllEmployees();
        String[] columns = {"ID", "ФИО", "Должность", "Email", "Номер телефона"};
        Object[][] data = new Object[employees.size()][columns.length];

        for (int i = 0; i < employees.size(); i++) {
            EmployeeDTO e = employees.get(i);
            data[i][0] = e.getId();
            data[i][1] = e.getFio();
            data[i][2] = e.getPosition();
            data[i][3] = e.getEmail();
            data[i][4] = e.getPhone();
        }

        employeeTable.setModel(new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    // Геттеры
    public JTable getEmployeeTable() { return employeeTable; }
    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
}