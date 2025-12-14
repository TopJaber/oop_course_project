package client_package.view;

import client_package.controller.ClientController;
import client_package.model.ClientDTO;

import javax.swing.*;
import java.awt.*;

public class ClientPanel extends JPanel {

    private JTable clientTable;
    private JButton addButton, editButton, deleteButton;

    public ClientPanel(ClientController clientController, JFrame parentFrame) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        clientTable = new JTable();
        clientTable.setRowHeight(28);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(clientTable);

        addButton = new JButton("Добавить клиента");
        editButton = new JButton("Изменить");
        deleteButton = new JButton("Удалить");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(addButton);
        buttons.add(editButton);
        buttons.add(deleteButton);

        add(scroll, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            AddClientDialog dialog = new AddClientDialog(parentFrame);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                ClientDTO dto = dialog.getClientDto();
                clientController.createClient(dto);
            }
        });

        editButton.addActionListener(e -> {
            AddClientDialog dialog = new AddClientDialog(parentFrame);
            dialog.setVisible(true);
        });
    }

    public JTable getClientTable() { return clientTable; }
    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
}