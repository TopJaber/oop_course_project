package client_package.view;

import client_package.controller.ClientController;
import client_package.controller.EmployeeController;
import client_package.controller.MeetingController;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private ClientPanel clientPanel;
    private EmployeePanel employeePanel;
    private MeetingPanel meetingPanel;

    public MainWindow() {
        setTitle("Офисная CRM");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Сначала создаём контроллеры клиентов и сотрудников
        ClientController clientController = new ClientController();
        EmployeeController employeeController = new EmployeeController();

        // Передаём их в контроллер встреч
        MeetingController meetingController = new MeetingController(clientController, employeeController);

        // Панели
        clientPanel = new ClientPanel(clientController, this);
        employeePanel = new EmployeePanel(employeeController, this);
        meetingPanel = new MeetingPanel(meetingController, clientController, employeeController, this);

        // Добавляем вкладки
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Клиенты", clientPanel);
        tabs.addTab("Работники", employeePanel);
        tabs.addTab("Встречи", meetingPanel);

        add(tabs, BorderLayout.CENTER);
        setVisible(true);
    }

    public ClientPanel getClientPanel() {
        return clientPanel;
    }

    public MeetingPanel getMeetingPanel() {
        return meetingPanel;
    }
}