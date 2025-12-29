package client_package.view;

import client_package.controller.ClientController;
import client_package.controller.EmployeeController;
import client_package.controller.MeetingController;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatTabbedPaneUI;

import java.awt.*;

public class MainWindow extends JFrame {
    private ClientPanel clientPanel;
    private EmployeePanel employeePanel;
    private MeetingPanel meetingPanel;

    public MainWindow() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Офисная CRM");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        ClientController clientController = new ClientController();
        EmployeeController employeeController = new EmployeeController();
        MeetingController meetingController =
                new MeetingController(clientController, employeeController);

        clientPanel = new ClientPanel(clientController, this);
        employeePanel = new EmployeePanel(employeeController, this);
        meetingPanel = new MeetingPanel(
                meetingController, clientController, employeeController, this);

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setBackground(Color.WHITE);
        tabs.addTab("Клиенты", clientPanel);
        tabs.addTab("Работники", employeePanel);
        tabs.addTab("Встречи", meetingPanel);

        tabs.setUI(new FlatTabbedPaneUI() {
            @Override
            protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
                int tabCount = tabPane.getTabCount();
                return tabPane.getWidth() / tabCount;
            }
        });

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