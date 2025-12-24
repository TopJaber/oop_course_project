package client_package.view;

import client_package.controller.ClientController;
import client_package.controller.EmployeeController;
import client_package.controller.MeetingController;
import client_package.model.MeetingDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.time.LocalDateTime;

public class MeetingPanel extends JPanel {
    private JTable meetingTable;
    private JButton addButton, editButton, deleteButton, refreshButton;
    private JTextField filterField;

    private MeetingController meetingController;
    private ClientController clientController;
    private EmployeeController employeeController;
    private JFrame parentFrame;

    public MeetingPanel(MeetingController meetingController,
                        ClientController clientController,
                        EmployeeController employeeController,
                        JFrame parentFrame) {
        this.meetingController = meetingController;
        this.clientController = clientController;
        this.employeeController = employeeController;
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout(10, 10));

        // Верхняя панель — фильтр
        JPanel topPanel = new JPanel(new BorderLayout());
        filterField = new JTextField();
        topPanel.add(new JLabel("Фильтр по клиенту: "), BorderLayout.WEST);
        topPanel.add(filterField, BorderLayout.CENTER);

        // Таблица встреч
        meetingTable = new JTable();
        meetingTable.setRowHeight(28);
        meetingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(meetingTable);

        // Панель кнопок
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

        initButtons();
        initFilter();
        refreshTable();
    }

    private void initButtons() {
        addButton.addActionListener(e -> {
            AddMeetingDialog dialog = new AddMeetingDialog(parentFrame, clientController, employeeController);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                MeetingDTO dto = dialog.getMeetingDto();
                meetingController.createMeeting(dto);
                refreshTable();
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = meetingTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Выберите встречу для редактирования");
                return;
            }

            MeetingDTO selectedMeeting = getSelectedMeetingFromTable(selectedRow);
            AddMeetingDialog dialog = new AddMeetingDialog(parentFrame, selectedMeeting, clientController, employeeController);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                MeetingDTO updatedMeeting = dialog.getMeetingDto();
                meetingController.updateMeeting(updatedMeeting);
                refreshTable();
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = meetingTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(parentFrame, "Выберите встречу для удаления");
                return;
            }

            MeetingDTO selectedMeeting = getSelectedMeetingFromTable(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(parentFrame,
                    "Удалить встречу с клиентом " + selectedMeeting.getClientFio() + "?",
                    "Подтвердите удаление", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                meetingController.deleteMeeting(selectedMeeting.getId());
                refreshTable();
            }
        });

        refreshButton.addActionListener(e -> refreshTable());
    }

    private void initFilter() {
        filterField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });
    }

    private void filterTable() {
        String filterText = filterField.getText().trim().toLowerCase();
        if (filterText.isEmpty()) {
            refreshTable();
            return;
        }

        List<MeetingDTO> filtered = meetingController.getAllMeetings().stream()
                .filter(m -> m.getClientFio().toLowerCase().contains(filterText))
                .collect(Collectors.toList());

        setTableData(filtered);
    }

    private MeetingDTO getSelectedMeetingFromTable(int row) {
        DefaultTableModel model = (DefaultTableModel) meetingTable.getModel();
        MeetingDTO meeting = new MeetingDTO();

        Object idObj = model.getValueAt(row, 0);
        if (idObj instanceof Number) {
            meeting.setId(((Number) idObj).longValue());
        } else {
            throw new RuntimeException("Некорректный ID встречи в таблице");
        }

        meeting.setClientFio((String) model.getValueAt(row, 1));
        meeting.setEmployeeFio((String) model.getValueAt(row, 2));
        meeting.setDatetime((LocalDateTime) model.getValueAt(row, 3));
        meeting.setPlace((String) model.getValueAt(row, 4));
        meeting.setComment((String) model.getValueAt(row, 5));

        return meeting;
    }

    public void refreshTable() {
        List<MeetingDTO> meetings = meetingController.getAllMeetings();
        setTableData(meetings);
    }

    private void setTableData(List<MeetingDTO> meetings) {
        String[] columns = {"ID", "Клиент", "Сотрудник", "Дата/Время", "Место", "Комментарий"};
        Object[][] data = new Object[meetings.size()][columns.length];

        for (int i = 0; i < meetings.size(); i++) {
            MeetingDTO m = meetings.get(i);
            data[i][0] = m.getId(); // ID обязательно Long
            data[i][1] = m.getClientFio();
            data[i][2] = m.getEmployeeFio();
            data[i][3] = m.getDatetime();
            data[i][4] = m.getPlace();
            data[i][5] = m.getComment();
        }

        meetingTable.setModel(new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    // Геттеры
    public JTable getMeetingTable() { return meetingTable; }
    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getRefreshButton() { return refreshButton; }
    public JTextField getFilterField() { return filterField; }
}