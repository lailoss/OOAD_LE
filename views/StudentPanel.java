package views;

import models.Student;
import models.Submission;
import utils.Database;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class StudentPanel extends JFrame {
    private Student student;
    
    public StudentPanel(Student student) {
        this.student = student;
        setTitle("Student Panel - " + student.getName());
        setSize(500, 400);
        setLayout(new GridLayout(6, 2, 10, 10));
        
        // Form fields
        JLabel titleLabel = new JLabel("Research Title:");
        JTextField titleField = new JTextField();
        
        JLabel supervisorLabel = new JLabel("Supervisor:");
        JTextField supervisorField = new JTextField();
        
        JLabel abstractLabel = new JLabel("Abstract:");
        JTextArea abstractArea = new JTextArea(3, 20);
        JScrollPane abstractScroll = new JScrollPane(abstractArea);
        
        JLabel typeLabel = new JLabel("Presentation Type:");
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Oral", "Poster"});
        
        JButton uploadBtn = new JButton("Upload File");
        JLabel fileLabel = new JLabel("No file selected");
        
        JButton submitBtn = new JButton("Submit Registration");
        
        // Add components
        add(titleLabel); add(titleField);
        add(supervisorLabel); add(supervisorField);
        add(abstractLabel); add(abstractScroll);
        add(typeLabel); add(typeCombo);
        add(new JLabel("Upload Presentation:"));
        add(uploadBtn);
        add(new JLabel("File:"));
        add(fileLabel);
        add(new JLabel()); // empty cell
        add(submitBtn);
        
        // Upload action (Java Swing JFileChooser)
        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Presentation File");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                fileLabel.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        
        // Submit action
        submitBtn.addActionListener(e -> {
            // Save student data
            student.setResearchTitle(titleField.getText());
            student.setSupervisor(supervisorField.getText());
            student.setAbstractText(abstractArea.getText());
            student.setPresentationType((String) typeCombo.getSelectedItem());
            
            // Create submission
            if (!fileLabel.getText().equals("No file selected")) {
                Submission sub = new Submission(fileLabel.getText(), new Date());
                student.setSubmission(sub);
            }
            
            // Show confirmation
            JOptionPane.showMessageDialog(this, 
                "Registration Successful!\n" +
                "Title: " + student.getResearchTitle() + "\n" +
                "Type: " + student.getPresentationType());
        });
        
        setLocationRelativeTo(null);
    }
}