package client_package.view;

import client_package.controller.ClientController;
import client_package.controller.EmployeeController;
import client_package.model.ClientDTO;
import client_package.model.EmployeeDTO;
import client_package.model.MeetingDTO;
import client_package.model.Status;

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

    private final ClientController clientController;
    private final EmployeeController employeeController;

    // Конструктор для добавления новой встречи
    public AddMeetingDialog(JFrame parent, ClientController clientController, EmployeeController employeeController) {
        this(parent, null, clientController, employeeController);
    }

    // Конструктор для редактирования встречи
    public AddMeetingDialog(JFrame parent, MeetingDTO meeting, ClientController clientController, EmployeeController employeeController) {
        super(parent, meeting == null ? "Добавить встречу" : "Редактировать встречу", true);
        this.meeting = meeting;
        this.clientController = clientController;
        this.employeeController = employeeController;

        setSize(450, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Настройка JSpinner для даты и времени
        dateTimeSpinner = new JSpinner(new SpinnerDateModel());
        dateTimeSpinner.setEditor(new JSpinner.DateEditor(dateTimeSpinner, "dd.MM.yyyy HH:mm"));

        // Если редактирование — заполняем поля текущими значениями
        if (meeting != null) {
            clientBox.addItem(meeting.getClientFio());
            clientBox.setSelectedItem(meeting.getClientFio());

            employeeBox.addItem(meeting.getEmployeeFio());
            employeeBox.setSelectedItem(meeting.getEmployeeFio());

            Date date = Date.from(meeting.getDatetime().atZone(ZoneId.systemDefault()).toInstant());
            dateTimeSpinner.setValue(date);

            placeField.setText(meeting.getPlace());
            commentField.setText(meeting.getComment());
        }

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0; form.add(new JLabel("Клиент:"), c); c.gridx = 1; form.add(clientBox, c);
        c.gridx = 0; c.gridy++; form.add(new JLabel("Работник:"), c); c.gridx = 1; form.add(employeeBox, c);
        c.gridx = 0; c.gridy++; form.add(new JLabel("Дата:"), c); c.gridx = 1; form.add(dateTimeSpinner, c);
        c.gridx = 0; c.gridy++; form.add(new JLabel("Место:"), c); c.gridx = 1; form.add(placeField, c);
        c.gridx = 0; c.gridy++; form.add(new JLabel("Комментарий:"), c); c.gridx = 1; form.add(commentField, c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Сохранить");
        JButton cancelBtn = new JButton("Отмена");
        buttons.add(saveBtn); buttons.add(cancelBtn);

        saveBtn.addActionListener(e -> { saved = true; dispose(); });
        cancelBtn.addActionListener(e -> dispose());

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    public boolean isSaved() { return saved; }

    public MeetingDTO getMeetingDto() {
        MeetingDTO dto = meeting != null ? meeting : new MeetingDTO();

        dto.setClientFio((String) clientBox.getSelectedItem());
        dto.setEmployeeFio((String) employeeBox.getSelectedItem());

        Date date = (Date) dateTimeSpinner.getValue();
        dto.setDatetime(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));

        dto.setPlace(placeField.getText());
        dto.setComment(commentField.getText());

        return dto;
    }

    public void setClients(String[] clients) { clientBox.removeAllItems(); for (String c : clients) clientBox.addItem(c);}
    public void setEmployees(String[] employees) { employeeBox.removeAllItems(); for (String e : employees) employeeBox.addItem(e);}
}

