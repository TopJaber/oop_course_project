package client_package.view;

import client_package.controller.ClientController;
import client_package.model.ClientDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientPanel extends JPanel {
    private JTable clientTable;
    private JButton addButton, editButton, deleteButton;
    private ClientController clientController;
    private JFrame parentFrame;

    public ClientPanel(ClientController clientController, JFrame parentFrame) {
        this.clientController = clientController;
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        clientTable = new JTable();
        clientTable.setRowHeight(28);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(clientTable);

        addButton = new JButton("Добавить клиента");
        editButton = new JButton("Изменить");
        deleteButton = new JButton("Удалить");

        deleteButton.setBackground(Color.RED);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(deleteButton);

        add(scroll, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        // Инициализация кнопок
        initButtons();
        // Загрузка данных в таблицу
        refreshTable();
    }

    private void initButtons() {
        // Добавление клиента
        addButton.addActionListener(e -> {
            AddClientDialog dialog = new AddClientDialog(parentFrame);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                ClientDTO dto = dialog.getClientDto();
                clientController.createClient(dto);
                refreshTable();
            }
        });

        // Редактирование клиента
        editButton.addActionListener(e -> {
            int selectedRow = clientTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Выберите клиента для редактирования");
                return;
            }

            ClientDTO selectedClient = getSelectedClientFromTable(selectedRow);
            AddClientDialog dialog = new AddClientDialog(parentFrame, selectedClient);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                ClientDTO updatedClient = dialog.getClientDto();
                clientController.updateClient(updatedClient);
                refreshTable();
            }
        });

        // Удаление клиента
        deleteButton.addActionListener(e -> {
            int selectedRow = clientTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Выберите клиента для удаления");
                return;
            }

            ClientDTO selectedClient = getSelectedClientFromTable(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(parentFrame,
                    "Удалить клиента " + selectedClient.getFio() + "?",
                    "Подтвердите удаление", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                clientController.deleteClient(selectedClient.getId());
                refreshTable();
            }
        });
    }

    // Получение DTO клиента из выбранной строки таблицы
    private ClientDTO getSelectedClientFromTable(int row) {
        DefaultTableModel model = (DefaultTableModel) clientTable.getModel();
        ClientDTO client = new ClientDTO();
        client.setId((Long) model.getValueAt(row, 0));
        client.setFio((String) model.getValueAt(row, 1));
        client.setPhone((String) model.getValueAt(row, 2));
        client.setEmail((String) model.getValueAt(row, 3));
        return client;
    }

    // Обновление таблицы с клиентами
    public void refreshTable() {
        List<ClientDTO> clients = clientController.getAllClients();
        String[] columns = {"ID", "ФИО", "Телефон", "Email", "Задача"};
        Object[][] data = new Object[clients.size()][columns.length];

        for (int i = 0; i < clients.size(); i++) {
            ClientDTO c = clients.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getFio();
            data[i][2] = c.getPhone();
            data[i][3] = c.getEmail();
            data[i][4] = c.getNote();
        }

        clientTable.setModel(new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // запрет редактирования напрямую
            }
        });
    }

    // Геттеры кнопок и таблицы
    public JTable getClientTable() { return clientTable; }
    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
}