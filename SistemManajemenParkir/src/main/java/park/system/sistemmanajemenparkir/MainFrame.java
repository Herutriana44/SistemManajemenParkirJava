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
import java.util.ArrayList;

public class MainFrame {
    private JFrame mainFrame;
    private JTextField plateInput, slotInput;
    private ArrayList<JButton> slotButtons = new ArrayList<>();
    private static HistoryFrame historyFrame = new HistoryFrame();

    public MainFrame() {
        mainFrame = new JFrame("Parking Management System");
        mainFrame.setSize(600, 600);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel parkingPanel = new JPanel();
        parkingPanel.setLayout(new GridLayout(5, 5, 5, 5));

        // Create parking slot buttons
        for (int i = 1; i <= 25; i++) {
            JButton slotButton = new JButton("Slot " + i);
            slotButton.setBackground(Color.GREEN);
            slotButton.addActionListener(e -> {
                slotInput.setText(slotButton.getText());
            });
            slotButtons.add(slotButton);
            parkingPanel.add(slotButton);
        }

        // Create input form panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        JLabel plateLabel = new JLabel("Plate Number:");
        JLabel slotLabel = new JLabel("Slot:");
        plateInput = new JTextField();
        slotInput = new JTextField();
        JButton getInButton = new JButton("Get In");
        JButton getOutButton = new JButton("Get Out");

        inputPanel.add(plateLabel);
        inputPanel.add(plateInput);
        inputPanel.add(slotLabel);
        inputPanel.add(slotInput);
        inputPanel.add(getInButton);
        inputPanel.add(getOutButton);

        // Add action listeners for buttons
        getInButton.addActionListener(e -> handleGetIn());
        getOutButton.addActionListener(e -> handleGetOut());

        mainFrame.add(parkingPanel, BorderLayout.CENTER);
        mainFrame.add(inputPanel, BorderLayout.SOUTH);

        // Add admin login button
        JButton adminLoginButton = new JButton("Admin Login");
        adminLoginButton.addActionListener(e -> new AdminLoginFrame(historyFrame));
        mainFrame.add(adminLoginButton, BorderLayout.NORTH);

        mainFrame.setVisible(true);
    }

    private void handleGetIn() {
        String plateNumber = plateInput.getText();
        String slot = slotInput.getText();

        if (plateNumber.isEmpty() || slot.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Please fill in both fields");
            return;
        }

        for (JButton button : slotButtons) {
            if (button.getText().equals(slot) && button.getBackground() == Color.GREEN) {
                button.setBackground(Color.RED);
                historyFrame.appendHistory(String.valueOf(historyFrame.tableModel.getRowCount() + 1),
                        plateNumber, slot, "2024-11-08 08:00", "-", "-");
                JOptionPane.showMessageDialog(mainFrame, "Vehicle " + plateNumber + " parked at " + slot);
                return;
            }
        }
        JOptionPane.showMessageDialog(mainFrame, "Slot " + slot + " is not available");
    }

    private void handleGetOut() {
        String plateNumber = plateInput.getText();
        String slot = slotInput.getText();

        if (plateNumber.isEmpty() || slot.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Please fill in both fields");
            return;
        }

        for (JButton button : slotButtons) {
            if (button.getText().equals(slot) && button.getBackground() == Color.RED) {
                button.setBackground(Color.GREEN);
                historyFrame.appendHistory(String.valueOf(historyFrame.tableModel.getRowCount() + 1),
                        plateNumber, slot, "2024-11-08 08:00", "2024-11-08 10:00", "120");
                JOptionPane.showMessageDialog(mainFrame, "Vehicle " + plateNumber + " left " + slot);
                return;
            }
        }
        JOptionPane.showMessageDialog(mainFrame, "Slot " + slot + " is already free");
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
