/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package park.system.sistemmanajemenparkir;

/**
 *
 * @author herutriana44
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminLoginFrame {
    private JFrame adminLoginFrame;

    public AdminLoginFrame(HistoryFrame historyFrame) {
        adminLoginFrame = new JFrame("Admin Login");
        adminLoginFrame.setSize(300, 150);
        adminLoginFrame.setLayout(new GridLayout(3, 2));

        JLabel adminUserLabel = new JLabel("Username:");
        JLabel adminPassLabel = new JLabel("Password:");
        JTextField adminUserInput = new JTextField();
        JPasswordField adminPassInput = new JPasswordField();
        JButton loginButton = new JButton("Login");

        adminLoginFrame.add(adminUserLabel);
        adminLoginFrame.add(adminUserInput);
        adminLoginFrame.add(adminPassLabel);
        adminLoginFrame.add(adminPassInput);
        adminLoginFrame.add(loginButton);

        loginButton.addActionListener(e -> {
            if (adminUserInput.getText().equals("admin") && String.valueOf(adminPassInput.getPassword()).equals("password")) {
                historyFrame.setVisible(true);
                adminLoginFrame.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(adminLoginFrame, "Invalid login credentials");
            }
        });

        adminLoginFrame.setVisible(true);
    }
}

