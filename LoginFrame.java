import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LoginFrame extends JFrame {
    public static List<Object> allUsers = new ArrayList<>();
    public static List<Session> allSessions = new ArrayList<>();
    public static List<Evaluation> allEvaluations = new ArrayList<>();
    public static List<Award> allAwards = new ArrayList<>();  // NEW: Store awards
    
    public LoginFrame() {
        setTitle("Seminar Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));
        
        add(new JLabel("User ID:"));
        JTextField idField = new JTextField();
        add(idField);
        
        add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        add(nameField);
        
        add(new JLabel("Role:"));
        String[] roles = {"Student", "Evaluator", "Coordinator"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        add(roleCombo);
        
        add(new JLabel());
        JButton loginBtn = new JButton("Login");
        add(loginBtn);
        
        loginBtn.addActionListener(e -> {
            String userId = idField.getText();
            String name = nameField.getText();
            String role = (String) roleCombo.getSelectedItem();
            
            if (userId.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            
            switch (role) {
                case "Student":
                    Student student = new Student(userId, name);
                    allUsers.add(student);
                    new StudentPanel(student).setVisible(true);
                    break;
                case "Evaluator":
                    Evaluator evaluator = new Evaluator(userId, name);
                    allUsers.add(evaluator);
                    new EvaluatorPanel(evaluator).setVisible(true);
                    break;
                case "Coordinator":
                    Coordinator coordinator = new Coordinator(userId, name);
                    allUsers.add(coordinator);
                    new CoordinatorPanel(coordinator).setVisible(true);
                    break;
            }
            dispose();
        });
        
        setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}