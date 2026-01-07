package views;

import models.*;
import utils.Database;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    
    public LoginFrame() {
        setTitle("Seminar Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));
        
        // UI Components
        add(new JLabel("User ID:"));
        JTextField idField = new JTextField();
        add(idField);
        
        add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        add(nameField);
        
        add(new JLabel("Role:"));
        String[] roles = {"Student", "Evaluator", "Coordinator"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        add(roleComboBox);
        
        add(new JLabel()); // Empty cell
        JButton loginBtn = new JButton("Login");
        add(loginBtn);
        
        // Login Action
        loginBtn.addActionListener(e -> {
            String userId = idField.getText();
            String name = nameField.getText();
            String role = (String) roleComboBox.getSelectedItem();
            
            if (userId.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            
            // Create user based on role
            switch (role) {
                case "Student":
                    Student student = new Student(userId, name);
                    Database.addStudent(student);
                    new StudentPanel(student).setVisible(true);
                    break;
                    
                case "Evaluator":
                    Evaluator evaluator = new Evaluator(userId, name);
                    Database.evaluators.add(evaluator);
                    new EvaluatorPanel(evaluator).setVisible(true);
                    break;
                    
                case "Coordinator":
                    Coordinator coordinator = new Coordinator(userId, name);
                    Database.coordinators.add(coordinator);
                    new CoordinatorPanel(coordinator).setVisible(true);
                    break;
            }
            
            dispose(); // Close login window
        });
        
        setLocationRelativeTo(null); // Center window
    }
    
    public static void main(String[] args) {
        // Java Swing: Run in Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}