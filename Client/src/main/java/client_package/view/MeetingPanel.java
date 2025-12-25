package client_package.view;

import client_package.controller.ClientController;
import client_package.controller.EmployeeController;
import client_package.controller.MeetingController;
import client_package.model.MeetingDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MeetingPanel extends JPanel {

    private JTable meetingTable;
    private JButton addButton, editButton, deleteButton, refreshButton;
    private JTextField filterField;

    private MeetingController meetingController;
    private ClientController clientController;
    private EmployeeController employeeController;
    private JFrame parentFrame;

    // 🔥 Храним реальные DTO
    private List<MeetingDTO> currentMeetings = List.of();

    public MeetingPanel(MeetingController meetingController,
                        ClientController clientController,
                        EmployeeController employeeController,
                        JFrame parentFrame) {

        this.meetingController = meetingController;
        this.clientController = clientController;
        this.employeeController = employeeController;
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        filterField = new JTextField();
        topPanel.add(new JLabel("Фильтр по клиенту: "), BorderLayout.WEST);
        topPanel.add(filterField, BorderLayout.CENTER);

        meetingTable = new JTable();
        meetingTable.setRowHeight(28);
        meetingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(meetingTable);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Добавить");
        editButton = new JButton("Редактировать");
        deleteButton = new JButton("Удалить");
        refreshButton = new JButton("Обновить");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        initButtons();
        initFilter();
        refreshTable();
    }

    private void initButtons() {

        addButton.addActionListener(e -> {
            AddMeetingDialog dialog =
                    new AddMeetingDialog(parentFrame, null, clientController, employeeController);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                meetingController.createMeeting(dialog.getMeetingDto());
                refreshTable();
            }
        });

        editButton.addActionListener(e -> {
            int row = meetingTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Выберите встречу");
                return;
            }

            MeetingDTO selected = currentMeetings.get(row);

            AddMeetingDialog dialog =
                    new AddMeetingDialog(parentFrame, selected, clientController, employeeController);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                meetingController.updateMeeting(dialog.getMeetingDto());
                refreshTable();
            }
        });

        deleteButton.addActionListener(e -> {
            int row = meetingTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Выберите встречу");
                return;
            }

            MeetingDTO selected = currentMeetings.get(row);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Удалить встречу с клиентом " + selected.getClientFio() + "?",
                    "Подтвердите",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                meetingController.deleteMeeting(selected.getId());
                refreshTable();
            }
        });

        refreshButton.addActionListener(e -> refreshTable());
    }

    private void initFilter() {
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });
    }

    private void filter() {
        String text = filterField.getText().toLowerCase().trim();

        if (text.isEmpty()) {
            setTableData(currentMeetings);
            return;
        }

        List<MeetingDTO> filtered = currentMeetings.stream()
                .filter(m -> m.getClientFio() != null &&
                        m.getClientFio().toLowerCase().contains(text))
                .toList();

        setTableData(filtered);
    }

    public void refreshTable() {
        currentMeetings = meetingController.getAllMeetings();
        setTableData(currentMeetings);
    }

    private void setTableData(List<MeetingDTO> meetings) {
        String[] cols = {"ID", "Клиент", "Сотрудник", "Дата", "Место", "Комментарий"};
        Object[][] data = new Object[meetings.size()][cols.length];

        for (int i = 0; i < meetings.size(); i++) {
            MeetingDTO m = meetings.get(i);
            data[i][0] = m.getId();
            data[i][1] = m.getClientFio();
            data[i][2] = m.getEmployeeFio();
            data[i][3] = m.getDatetime();
            data[i][4] = m.getPlace();
            data[i][5] = m.getComment();
        }

        meetingTable.setModel(new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });
    }
}