package client_package.view;

import javax.swing.*;
import java.awt.*;

public class AddMeetingDialog extends JDialog {

    public AddMeetingDialog(JFrame parent) {
        super(parent, "Create Meeting", true);
        setSize(450, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JComboBox<String> clientBox = new JComboBox<>();
        JComboBox<String> employeeBox = new JComboBox<>();

        JSpinner dateTime = new JSpinner(
                new SpinnerDateModel()
        );
        dateTime.setEditor(
                new JSpinner.DateEditor(dateTime, "dd.MM.yyyy HH:mm")
        );

        JTextField placeField = new JTextField(20);

        JTextField commentField = new JTextField(20);

        JPanel form = new JPanel(new GridBagLayout());
//        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Клиент:"), c);
        c.gridx = 1;
        form.add(clientBox, c);

        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Работник:"), c);
        c.gridx = 1;
        form.add(employeeBox, c);

        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Дата:"), c);
        c.gridx = 1;
        form.add(dateTime, c);

        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Место:"), c);
        c.gridx = 1;
        form.add(placeField, c);

        c.gridx = 0; c.gridy++;
        form.add(new JLabel("Комментарий:"), c);
        c.gridx = 1;
        form.add(commentField, c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(new JButton("Создать"));
        buttons.add(new JButton("Отмена"));

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }
}