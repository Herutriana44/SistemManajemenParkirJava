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
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoryFrame {
    private JFrame historyFrame;
    private JTable historyTable;
    public DefaultTableModel tableModel;

    public HistoryFrame() {
        historyFrame = new JFrame("Parking History");
        historyFrame.setSize(600, 400);
        historyFrame.setLayout(new BorderLayout());

        // Create table model with column names
        String[] columnNames = {"History ID", "License Plate", "Slot ID", "Entry Time", "Exit Time", "Duration (min)"};
        tableModel = new DefaultTableModel(columnNames, 0);

        // Create table with the table model
        historyTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(historyTable);

        // Add sample data to the table (mock data)
        addSampleData();

        historyFrame.add(scrollPane, BorderLayout.CENTER);
        historyFrame.setVisible(false);
    }

    // Method to add a row to the table (simulates adding data from database)
    public void appendHistory(String historyID, String licensePlate, String slotID, String entryTime, String exitTime, String duration) {
        tableModel.addRow(new Object[]{historyID, licensePlate, slotID, entryTime, exitTime, duration});
    }

    // Method to add sample data to the table for initial display
    private void addSampleData() {
        tableModel.addRow(new Object[]{"1", "B1234XYZ", "Slot 5", "2024-11-08 08:00", "2024-11-08 10:00", "120"});
        tableModel.addRow(new Object[]{"2", "D5678ABC", "Slot 10", "2024-11-08 09:00", "2024-11-08 11:30", "150"});
    }

    public void setVisible(boolean visible) {
        historyFrame.setVisible(visible);
    }
}
