package client_package.view;

import client_package.controller.ClientController;
import client_package.controller.EmployeeController;
import client_package.model.MeetingDTO;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class AddMeetingDialog extends JDialog {

    private boolean saved = false;

    private JComboBox<String> clientBox = new JComboBox<>();
    private JComboBox<String> employeeBox = new JComboBox<>();
    private JSpinner dateTimeSpinner;
    private JTextField placeField = new JTextField(20);
    private JTextField commentField = new JTextField(20);

    private MeetingDTO meeting;
    private Long[] clientIds;
    private Long[] employeeIds;

    public AddMeetingDialog(JFrame parent, MeetingDTO meeting,
                            ClientController clientController, EmployeeController employeeController) {
        super(parent, meeting == null ? "Добавить встречу" : "Редактировать встречу", true);
        this.meeting = meeting;

        setSize(450, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Spinner для даты
        dateTimeSpinner = new JSpinner(new SpinnerDateModel());
        dateTimeSpinner.setEditor(new JSpinner.DateEditor(dateTimeSpinner, "dd.MM.yyyy HH:mm"));

        // Подгружаем клиентов
        List<String> clientNames = clientController.getAllClients().stream()
                .map(c -> c.getFio())
                .toList();
        clientIds = clientController.getAllClients().stream()
                .map(c -> c.getId())
                .toArray(Long[]::new);
        for (String name : clientNames) clientBox.addItem(name);

        // Подгружаем сотрудников
        List<String> employeeNames = employeeController.getAllEmployees().stream()
                .map(e -> e.getFio())
                .toList();
        employeeIds = employeeController.getAllEmployees().stream()
                .map(e -> e.getId())
                .toArray(Long[]::new);
        for (String name : employeeNames) employeeBox.addItem(name);

        // Если редактирование — выбрать текущие значения
        if (meeting != null) {
            placeField.setText(meeting.getPlace());
            commentField.setText(meeting.getComment());
            Date date = Date.from(meeting.getDatetime().atZone(ZoneId.systemDefault()).toInstant());
            dateTimeSpinner.setValue(date);

            // выбираем клиента по ID
            for (int i = 0; i < clientIds.length; i++) {
                if (clientIds[i].equals(meeting.getClientId())) {
                    clientBox.setSelectedIndex(i);
                    break;
                }
            }

            // выбираем сотрудника по ID
            for (int i = 0; i < employeeIds.length; i++) {
                if (employeeIds[i].equals(meeting.getEmployeeId())) {
                    employeeBox.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Создание формы
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0; form.add(new JLabel("Клиент:"), c);
        c.gridx = 1; form.add(clientBox, c);

        c.gridx = 0; c.gridy++; form.add(new JLabel("Работник:"), c);
        c.gridx = 1; form.add(employeeBox, c);

        c.gridx = 0; c.gridy++; form.add(new JLabel("Дата:"), c);
        c.gridx = 1; form.add(dateTimeSpinner, c);

        c.gridx = 0; c.gridy++; form.add(new JLabel("Место:"), c);
        c.gridx = 1; form.add(placeField, c);

        c.gridx = 0; c.gridy++; form.add(new JLabel("Комментарий:"), c);
        c.gridx = 1; form.add(commentField, c);

        // Кнопки
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Сохранить");
        JButton cancelBtn = new JButton("Отмена");

        cancelBtn.setBackground(new Color(0xF44336));
        cancelBtn.putClientProperty("JButton.hoverBackground", new Color(0xE53935));
        cancelBtn.putClientProperty("JButton.pressedBackground", new Color(0xC62828));

        buttons.add(saveBtn);
        buttons.add(cancelBtn);

        saveBtn.addActionListener(e -> {
            saved = true;
            dispose();
        });

        cancelBtn.addActionListener(e -> dispose());

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    public boolean isSaved() {
        return saved;
    }

    public MeetingDTO getMeetingDto() {
        MeetingDTO dto;

        if (meeting != null) {
            dto = meeting;
        } else {
            // создание
            dto = new MeetingDTO();
        }

        dto.setClientId(clientIds[clientBox.getSelectedIndex()]);
        dto.setClientFio((String) clientBox.getSelectedItem());

        dto.setEmployeeId(employeeIds[employeeBox.getSelectedIndex()]);
        dto.setEmployeeFio((String) employeeBox.getSelectedItem());

        Date date = (Date) dateTimeSpinner.getValue();
        dto.setDatetime(LocalDateTime.ofInstant(
                date.toInstant(), ZoneId.systemDefault()
        ));

        dto.setPlace(placeField.getText());
        dto.setComment(commentField.getText());

        return dto;
    }
}

