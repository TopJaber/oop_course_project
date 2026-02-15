package client_package.view;

import client_package.controller.ClientController;
import client_package.controller.EmployeeController;
import client_package.controller.MeetingController;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatTabbedPaneUI;

import java.awt.*;
import java.util.Set;

public class MainWindow extends JFrame {

    private final String username;
    private final String role;
    private boolean passwordChanged;

    private final ClientController clientController;
    private final EmployeeController employeeController;
    private final MeetingController meetingController;

    public MainWindow(String username, String role, boolean passwordChanged) {
        this.username = username;
        this.role = role;
        this.passwordChanged = passwordChanged;

        this.clientController = new ClientController();
        this.employeeController = new EmployeeController();
        this.meetingController =
                new MeetingController(clientController, employeeController);

        initUI();
    }

    private void initUI() {
        FlatLightLaf.setup();

        setTitle("CRM система — " + username);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---------- Верхняя панель ----------
        JLabel welcomeLabel = new JLabel("Добро пожаловать, " + username);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 15f));
        add(welcomeLabel, BorderLayout.NORTH);

        // ---------- Панели ----------
        ClientPanel clientPanel =
                new ClientPanel(clientController, this);
        EmployeePanel employeePanel =
                new EmployeePanel(employeeController, this);
        MeetingPanel meetingPanel =
                new MeetingPanel(
                        meetingController,
                        clientController,
                        employeeController,
                        this
                );

        // ---------- Вкладки ----------
        JTabbedPane tabs = new JTabbedPane();

        if (Set.of("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_EMPLOYEE").contains(role)) {
            tabs.addTab("Встречи", meetingPanel);
        }
        if (Set.of("ROLE_ADMIN", "ROLE_MANAGER").contains(role)) {
            tabs.addTab("Клиенты", clientPanel);
        }
        if ("ROLE_ADMIN".equals(role)) {
            tabs.addTab("Работники", employeePanel);
        }

        // Растягивание вкладок
        tabs.setUI(new com.formdev.flatlaf.ui.FlatTabbedPaneUI() {
            @Override
            protected int calculateTabWidth(
                    int tabPlacement,
                    int tabIndex,
                    FontMetrics metrics
            ) {
                int count = tabPane.getTabCount();
                return count == 0
                        ? super.calculateTabWidth(tabPlacement, tabIndex, metrics)
                        : tabPane.getWidth() / count;
            }
        });

        add(tabs, BorderLayout.CENTER);

        // ---------- Статусная строка ----------
        JLabel statusLabel = new JLabel("Роль: " + role);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }
}