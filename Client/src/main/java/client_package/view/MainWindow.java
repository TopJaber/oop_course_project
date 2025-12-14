package client_package.view;

import client_package.controller.ClientController;

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

        ClientController clientController = new ClientController();

        clientPanel = new ClientPanel(clientController, this);
        employeePanel = new EmployeePanel(this);
        meetingPanel = new MeetingPanel(this);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Клиенты", clientPanel);
        tabs.addTab("Работники", employeePanel);
        tabs.addTab("Встречи", meetingPanel);

        add(tabs);
        setVisible(true);
    }

    public ClientPanel getClientPanel() {
        return clientPanel;
    }

    public MeetingPanel getMeetingPanel() {
        return meetingPanel;
    }
}