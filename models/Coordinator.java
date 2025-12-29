package views;

import models.*;
import utils.Database;
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
        
        // North Panel - Session Creation
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
        
        // Center Panel - Awards
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Award Management"));
        
        JButton bestOralBtn = new JButton("Best Oral Award");
        centerPanel.add(bestOralBtn);
        
        JButton bestPosterBtn = new JButton("Best Poster Award");
        centerPanel.add(bestPosterBtn);
        
        JButton peopleChoiceBtn = new JButton("People's Choice");
        centerPanel.add(peopleChoiceBtn);
        
        JButton generateReportBtn = new JButton("Generate Report");
        centerPanel.add(generateReportBtn);
        
        // South Panel - Output
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));
        
        // Add all panels
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        
        // Actions
        createSessionBtn.addActionListener(e -> {
            Session session = new Session(
                sessionIdField.getText(),
                new Date(),
                venueField.getText(),
                (String) typeCombo.getSelectedItem()
            );
            Database.addSession(session);
            outputArea.append("Session created: " + session + "\n");
        });
        
        bestOralBtn.addActionListener(e -> {
            for (Session s : Database.sessions) {
                if (s.getType().equals("Oral")) {
                    Award award = new Award("Best Oral", s);
                    Student winner = award.calculateWinner();
                    if (winner != null) {
                        outputArea.append("Best Oral Award: " + winner.getName() + "\n");
                    }
                }
            }
        });
        
        bestPosterBtn.addActionListener(e -> {
            outputArea.append("Calculating Best Poster Award...\n");
        });
        
        generateReportBtn.addActionListener(e -> {
            Report report = new Report();
            report.generateSummary();
            outputArea.append("Report generated at: " + new Date() + "\n");
        });
        
        setLocationRelativeTo(null);
    }
}