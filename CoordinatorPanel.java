import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class CoordinatorPanel extends JFrame {
    private Coordinator coordinator;
    private JTextArea outputArea;
    
    public CoordinatorPanel(Coordinator coordinator) {
        this.coordinator = coordinator;
        setTitle("Coordinator Panel - " + coordinator.getName());
        setSize(700, 600);
        setLayout(new BorderLayout(10, 10));
        
        JPanel northPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        northPanel.setBorder(BorderFactory.createTitledBorder("Create New Session"));
        
        northPanel.add(new JLabel("Session ID:"));
        JTextField sessionIdField = new JTextField();
        northPanel.add(sessionIdField);
        
        northPanel.add(new JLabel("Venue:"));
        JTextField venueField = new JTextField();
        northPanel.add(venueField);
        
        northPanel.add(new JLabel("Type:"));
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Oral", "Poster"});
        northPanel.add(typeCombo);
        
        JButton createSessionBtn = new JButton("Create Session");
        northPanel.add(createSessionBtn);
        
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Award Management"));
        
        JButton bestOralBtn = new JButton("Best Oral Award");
        centerPanel.add(bestOralBtn);
        
        JButton bestPosterBtn = new JButton("Best Poster Award");
        centerPanel.add(bestPosterBtn);
        
        JButton generateReportBtn = new JButton("Generate Report");
        centerPanel.add(generateReportBtn);
        
        JButton viewAllBtn = new JButton("View All Data");
        centerPanel.add(viewAllBtn);
        
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));
        
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        
        createSessionBtn.addActionListener(e -> {
            Session session = new Session(
                sessionIdField.getText(),
                new Date(),
                venueField.getText(),
                (String) typeCombo.getSelectedItem()
            );
            LoginFrame.allSessions.add(session);
            outputArea.append("✓ Session created: " + session + "\n");
        });
        
        bestOralBtn.addActionListener(e -> {
            outputArea.append("\n--- Best Oral Award Calculation ---\n");
            for (Session s : LoginFrame.allSessions) {
                if (s.getType().equals("Oral")) {
                    Award award = new Award("Best Oral", s);
                    Student winner = award.calculateWinner(LoginFrame.allEvaluations);
                    if (winner != null) {
                        outputArea.append("🏆 Best Oral: " + winner.getName() + "\n");
                    } else {
                        outputArea.append("No students in oral sessions\n");
                    }
                }
            }
        });
        
        bestPosterBtn.addActionListener(e -> {
            outputArea.append("\n--- Best Poster Award Calculation ---\n");
            for (Session s : LoginFrame.allSessions) {
                if (s.getType().equals("Poster")) {
                    Award award = new Award("Best Poster", s);
                    Student winner = award.calculateWinner(LoginFrame.allEvaluations);
                    if (winner != null) {
                        outputArea.append("🏆 Best Poster: " + winner.getName() + "\n");
                    } else {
                        outputArea.append("No students in poster sessions\n");
                    }
                }
            }
        });
        
        generateReportBtn.addActionListener(e -> {
            outputArea.append("\n--- Report Generated ---\n");
            outputArea.append("Total Users: " + LoginFrame.allUsers.size() + "\n");
            outputArea.append("Total Sessions: " + LoginFrame.allSessions.size() + "\n");
            outputArea.append("Total Evaluations: " + LoginFrame.allEvaluations.size() + "\n");
            outputArea.append("Report Time: " + new Date() + "\n");
        });
        
        viewAllBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("\n=== ALL SYSTEM DATA ===\n");
            sb.append("Users (").append(LoginFrame.allUsers.size()).append("):\n");
            for (Object u : LoginFrame.allUsers) {
                sb.append("  - ").append(u).append("\n");
            }
            sb.append("\nSessions (").append(LoginFrame.allSessions.size()).append("):\n");
            for (Session s : LoginFrame.allSessions) {
                sb.append("  - ").append(s).append("\n");
            }
            sb.append("\nEvaluations (").append(LoginFrame.allEvaluations.size()).append("):\n");
            for (Evaluation ev : LoginFrame.allEvaluations) {
                sb.append("  - ").append(ev).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });
        
        setLocationRelativeTo(null);
    }
}