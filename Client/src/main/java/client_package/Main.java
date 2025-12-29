package client_package;

import client_package.view.*;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        FlatLightLaf.setup();
        UIManager.put("Button.font", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("Table.font", new Font("Tahoma", Font.PLAIN, 14));
        UIManager.put("TableHeader.font", new Font("Tahoma", Font.BOLD, 14));

        UIManager.put("TabbedPane.tabHeight", 50);
        UIManager.put("TabbedPane.font", new Font("Tahoma", Font.BOLD, 15));
        UIManager.put("Button.arc", 15);
        UIManager.put("Button.font", new Font("Tahoma", Font.BOLD, 14));
        UIManager.put("Button.background", new Color(0x4CAF50));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.focusPainted", false);
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}