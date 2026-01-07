import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EvaluatorPanel extends JFrame {
    private Evaluator evaluator;
    private JComboBox<Student> studentCombo;
    private JSpinner claritySpinner, methodSpinner, resultsSpinner, presSpinner;
    private JTextArea commentArea;
    
    public EvaluatorPanel(Evaluator evaluator) {
        this.evaluator = evaluator;
        
        setTitle("Evaluator Assessment Panel - " + evaluator.getName());
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel headerLabel = new JLabel("Presentation Evaluation");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(headerLabel);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Student Selection
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Select Presenter:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        studentCombo = new JComboBox<>();
        loadStudents();
        studentCombo.setPreferredSize(new Dimension(250, 25));
        formPanel.add(studentCombo, gbc);
        
        // Rubric Scores
        String[] criteria = {"Problem Clarity", "Methodology", "Results", "Presentation"};
        JSpinner[] spinners = new JSpinner[4];
        
        for (int i = 0; i < criteria.length; i++) {
            gbc.gridx = 0; gbc.gridy = i + 1; gbc.gridwidth = 1;
            formPanel.add(new JLabel(criteria[i] + " (1-10):"), gbc);
            
            gbc.gridx = 1;
            spinners[i] = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
            formPanel.add(spinners[i], gbc);
            
            gbc.gridx = 2;
            JLabel rangeLabel = new JLabel("1=Poor, 10=Excellent");
            rangeLabel.setForeground(Color.GRAY);
            rangeLabel.setFont(new Font("Arial", Font.ITALIC, 11));
            formPanel.add(rangeLabel, gbc);
        }
        
        claritySpinner = spinners[0];
        methodSpinner = spinners[1];
        resultsSpinner = spinners[2];
        presSpinner = spinners[3];
        
        // Comments
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Comments:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.gridheight = 3;
        commentArea = new JTextArea(5, 30);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(commentArea), gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton evaluateBtn = new JButton("Submit Evaluation");
        evaluateBtn.setFont(new Font("Arial", Font.BOLD, 14));
        evaluateBtn.setBackground(new Color(60, 179, 113));
        evaluateBtn.setForeground(Color.WHITE);
        
        JButton viewBtn = new JButton("View My Evaluations");
        viewBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // LOGOUT BUTTON
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutBtn.setBackground(new Color(220, 80, 60));
        logoutBtn.setForeground(Color.WHITE);
        
        buttonPanel.add(evaluateBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(logoutBtn);
        
        // Add to frame
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        
        // Actions
        evaluateBtn.addActionListener(e -> {
            Student selected = (Student) studentCombo.getSelectedItem();
            if (selected != null) {
                Evaluation eval = new Evaluation(selected, evaluator);
                eval.setProblemClarity((int) claritySpinner.getValue());
                eval.setMethodology((int) methodSpinner.getValue());
                eval.setResults((int) resultsSpinner.getValue());
                eval.setPresentation((int) presSpinner.getValue());
                eval.setComment(commentArea.getText());
                
                evaluator.addEvaluation(eval);
                LoginFrame.allEvaluations.add(eval);
                
                String message = "<html><div style='text-align: center;'>" +
                    "<h3>Evaluation Submitted!</h3>" +
                    "<b>Student:</b> " + selected.getName() + "<br>" +
                    "<b>Total Score:</b> " + eval.getTotal() + "/40<br>" +
                    "<b>Average:</b> " + (eval.getTotal()/4.0) + "/10" +
                    "</div></html>";
                
                JOptionPane.showMessageDialog(this, message, "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        viewBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("<html><h3>My Evaluations</h3>");
            for (Evaluation eval : evaluator.getEvaluations()) {
                sb.append("<b>").append(eval.getStudent().getName()).append("</b>: ")
                  .append(eval.getTotal()).append("/40<br>");
            }
            if (evaluator.getEvaluations().isEmpty()) {
                sb.append("No evaluations submitted yet.");
            }
            sb.append("</html>");
            
            JOptionPane.showMessageDialog(this, sb.toString(), "Evaluation History", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Logout Button
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Return to login page?", "Logout", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
        
        setLocationRelativeTo(null);
    }
    
    private void loadStudents() {
        for (Object obj : LoginFrame.allUsers) {
            if (obj instanceof Student) {
                studentCombo.addItem((Student) obj);
            }
        }
        if (studentCombo.getItemCount() == 0) {
            Student sample = new Student("S001", "Sample Student");
            sample.setResearchTitle("Sample Research");
            studentCombo.addItem(sample);
        }
    }
}