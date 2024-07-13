package org.projekt2.terminal;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TerminalApplication extends JFrame {
    private static JTextField employeeIdField;
    private static JTextField roomNumberField;
    private static JComboBox<String> statusComboBox;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee Room Assignment");
        frame.setSize(300, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel employeeIdLabel = new JLabel("Employee ID:");
        employeeIdLabel.setBounds(10, 20, 100, 25);
        panel.add(employeeIdLabel);

        employeeIdField = new JTextField(20);
        employeeIdField.setBounds(120, 20, 165, 25);
        panel.add(employeeIdField);

        JLabel roomNumberLabel = new JLabel("Room Number:");
        roomNumberLabel.setBounds(10, 50, 100, 25);
        panel.add(roomNumberLabel);

        roomNumberField = new JTextField(20);
        roomNumberField.setBounds(120, 50, 165, 25);
        panel.add(roomNumberField);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(10, 80, 100, 25);
        panel.add(statusLabel);

        String[] statusOptions = {"Enter", "Exit"};
        statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setBounds(120, 80, 165, 25);
        panel.add(statusComboBox);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(10, 120, 150, 25);
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeId = employeeIdField.getText();
                String roomNumber = roomNumberField.getText();
                String status = (String) statusComboBox.getSelectedItem();
                try {
                    sendPostRequest(employeeId, roomNumber, status);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private static void sendPostRequest(String employeeId, String roomNumber, String status) throws Exception {
        String url = "http://localhost:8080/api/employeeRoom"; // Replace with your server URL
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        String jsonInputString = "{\"employeeId\": \"" + employeeId + "\", \"roomNumber\": \"" + roomNumber + "\", \"status\": \"" + status + "\"}";

        con.setDoOutput(true);
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("POST request worked");
        } else {
            System.out.println("POST request did not work");
        }
    }
}
