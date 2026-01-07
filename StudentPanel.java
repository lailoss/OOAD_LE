import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class StudentPanel extends JFrame {
    private Student student;
    private JTextField titleField, supervisorField;
    private JTextArea abstractArea;
    private JComboBox<String> typeCombo;
    private JLabel fileLabel;
    
    public StudentPanel(Student student) {
        this.student = student;
        
        // Frame setup
        setTitle("Student Registration - " + student.getName());
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main container with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // ========== HEADER ==========
        JPanel headerPanel = new JPanel();
        JLabel headerLabel = new JLabel("Research Seminar Registration Form");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(headerLabel);
        
        // ========== FORM PANEL ==========
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        
        // Panel 1: Research Title
        JPanel titlePanel = createFieldPanel("Research Title*:", "text");
        titleField = (JTextField) titlePanel.getComponent(1);
        
        // Panel 2: Supervisor
        JPanel supervisorPanel = createFieldPanel("Supervisor*:", "text");
        supervisorField = (JTextField) supervisorPanel.getComponent(1);
        
        // Panel 3: Presentation Type
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        typePanel.add(new JLabel("Presentation Type*:"));
        typeCombo = new JComboBox<>(new String[]{"Oral Presentation", "Poster Presentation"});
        typeCombo.setPreferredSize(new Dimension(200, 25));
        typePanel.add(typeCombo);
        typePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Panel 4: Abstract
        JPanel abstractPanel = new JPanel(new BorderLayout(5, 5));
        abstractPanel.add(new JLabel("Abstract*:"), BorderLayout.NORTH);
        abstractArea = new JTextArea(5, 40);
        abstractArea.setLineWrap(true);
        abstractArea.setWrapStyleWord(true);
        JScrollPane abstractScroll = new JScrollPane(abstractArea);
        abstractScroll.setPreferredSize(new Dimension(400, 100));
        abstractPanel.add(abstractScroll, BorderLayout.CENTER);
        abstractPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Panel 5: File Upload
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filePanel.add(new JLabel("Presentation File:"));
        JButton uploadBtn = new JButton("Browse...");
        fileLabel = new JLabel("No file selected");
        fileLabel.setForeground(Color.GRAY);
        filePanel.add(uploadBtn);
        filePanel.add(fileLabel);
        filePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Panel 6: Student Info
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        infoPanel.add(new JLabel("Student ID: " + student.getUserId()));
        infoPanel.add(new JLabel(" | Name: " + student.getName()));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Add all form panels
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(titlePanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(supervisorPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(typePanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(abstractPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(filePanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(infoPanel);
        
        // ========== BUTTON PANEL ==========
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton submitBtn = new JButton("Submit Registration");
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.setBackground(new Color(70, 130, 180)); // Steel blue
        submitBtn.setForeground(Color.WHITE);
        
        JButton clearBtn = new JButton("Clear Form");
        clearBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        
        buttonPanel.add(submitBtn);
        buttonPanel.add(clearBtn);
        
        // ========== ASSEMBLE MAIN PANEL ==========
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to frame
        add(mainPanel);
        
        // ========== EVENT HANDLERS ==========
        // File Upload
        uploadBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Presentation File");
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String fileName = fileChooser.getSelectedFile().getName();
                fileLabel.setText(fileName);
                fileLabel.setForeground(Color.BLUE);
            }
        });
        
        // Submit Registration
        submitBtn.addActionListener(e -> {
            if (validateForm()) {
                saveStudentData();
                showSuccessMessage();
            }
        });
        
        // Clear Form
        clearBtn.addActionListener(e -> {
            titleField.setText("");
            supervisorField.setText("");
            abstractArea.setText("");
            typeCombo.setSelectedIndex(0);
            fileLabel.setText("No file selected");
            fileLabel.setForeground(Color.GRAY);
        });
        
        // Center window
        setLocationRelativeTo(null);
    }
    
    // Helper method to create labeled text fields
    private JPanel createFieldPanel(String labelText, String type) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.add(new JLabel(labelText));
        if (type.equals("text")) {
            JTextField field = new JTextField(30);
            panel.add(field);
        }
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }
    
    // Form validation
    private boolean validateForm() {
        if (titleField.getText().trim().isEmpty()) {
            showError("Research Title is required!");
            titleField.requestFocus();
            return false;
        }
        if (supervisorField.getText().trim().isEmpty()) {
            showError("Supervisor name is required!");
            supervisorField.requestFocus();
            return false;
        }
        if (abstractArea.getText().trim().isEmpty()) {
            showError("Abstract is required!");
            abstractArea.requestFocus();
            return false;
        }
        return true;
    }
    
    // Save data to Student object
    private void saveStudentData() {
        student.setResearchTitle(titleField.getText().trim());
        student.setSupervisor(supervisorField.getText().trim());
        student.setAbstractText(abstractArea.getText().trim());
        student.setPresentationType((String) typeCombo.getSelectedItem());
        
        if (!fileLabel.getText().equals("No file selected")) {
            Submission sub = new Submission(fileLabel.getText(), new Date());
            student.setSubmission(sub);
        }
    }
    
    // Show error message
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    // Show success message
    private void showSuccessMessage() {
        String message = String.format(
            "<html><div style='text-align: center;'>" +
            "<h3>Registration Successful!</h3>" +
            "<b>Research Title:</b> %s<br>" +
            "<b>Presentation Type:</b> %s<br>" +
            "<b>Supervisor:</b> %s<br>" +
            "%s" +
            "</div></html>",
            student.getResearchTitle(),
            student.getPresentationType(),
            student.getSupervisor(),
            student.getSubmission() != null ? 
                "<b>File Uploaded:</b> " + student.getSubmission().getFilePath() : ""
        );
        
        JOptionPane.showMessageDialog(this, message, "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}