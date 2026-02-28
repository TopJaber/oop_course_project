package client_package;

import client_package.api.AuthContext;
import client_package.model.UserProfile;
import client_package.model.UserProfileDTO;
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
        UIManager.put("Button.hoverBackground", new Color(0x3A883D));
        UIManager.put("Button.pressedBackground", new Color(0x2C7130));
        UIManager.put("Button.focusedBackground", new Color(0x4CAF50));
        UIManager.put("Button.disabledBackground", new Color(0xA5D6A7));

        UIManager.put("Button.focusPainted", false);
        UIManager.put("Button.foreground", Color.WHITE);

        SwingUtilities.invokeLater(() -> {

            LoginDialog dialog = new LoginDialog(null);
            dialog.setVisible(true);

            if (!dialog.isSucceeded()) {
                System.exit(0);
            }

            UserProfile profile = dialog.getProfile();

            MainWindow mainWindow = new MainWindow(
                    profile.getUsername(),
                    profile.getRoles().getFirst(),
                    profile.isPasswordChanged()
            );
            mainWindow.setVisible(true);
        });
    }
}