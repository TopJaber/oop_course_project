package client_package.view;

import javax.swing.*;
import java.awt.*;

public class MeetingPanel extends JPanel {
    private JTable meetingTable;
    private JButton addButton, editButton, deleteButton, refreshButton;
    private JTextField filterField;

    public MeetingPanel(JFrame parentFrame) {
        setLayout(new BorderLayout(10, 10));

        // Верхняя панель — фильтр
        JPanel topPanel = new JPanel(new BorderLayout());
        filterField = new JTextField();
        topPanel.add(new JLabel("Фильтр по клиенту: "), BorderLayout.WEST);
        topPanel.add(filterField, BorderLayout.CENTER);

        // Таблица встреч
        meetingTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(meetingTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Добавить встречу");
        editButton = new JButton("Изменить");
        deleteButton = new JButton("Удалить");
        refreshButton = new JButton("Обновить");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            AddMeetingDialog dialog = new AddMeetingDialog(parentFrame);
            dialog.setVisible(true);
        });

        editButton.addActionListener(e -> {
            AddMeetingDialog dialog = new AddMeetingDialog(parentFrame);
            dialog.setVisible(true);
        });
    }

    public JTable getMeetingTable() { return meetingTable; }
    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getRefreshButton() { return refreshButton; }
    public JTextField getFilterField() { return filterField; }
}