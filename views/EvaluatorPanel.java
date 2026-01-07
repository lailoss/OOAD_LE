package views;

import models.Evaluator;
import models.Student;
import models.Evaluation;
import utils.Database;
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
        setTitle("Evaluator Panel - " + evaluator.getName());
        setSize(600, 500);
        setLayout(new GridLayout(8, 2, 10, 10));
        
        // Student selection
        add(new JLabel("Select Student:"));
        studentCombo = new JComboBox<>();
        loadStudents();
        add(studentCombo);
        
        // Rubric scores with JSpinner (1-10)
        add(new JLabel("Problem Clarity (1-10):"));
        claritySpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        add(claritySpinner);
        
        add(new JLabel("Methodology (1-10):"));
        methodSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        add(methodSpinner);
        
        add(new JLabel("Results (1-10):"));
        resultsSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        add(resultsSpinner);
        
        add(new JLabel("Presentation (1-10):"));
        presSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        add(presSpinner);
        
        // Comments
        add(new JLabel("Comments:"));
        commentArea = new JTextArea(3, 20);
        add(new JScrollPane(commentArea));
        
        // Buttons
        JButton evaluateBtn = new JButton("Submit Evaluation");
        add(evaluateBtn);
        
        JButton viewBtn = new JButton("View Assigned Sessions");
        add(viewBtn);
        
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
                
                Database.addEvaluation(eval);
                JOptionPane.showMessageDialog(this, 
                    "Evaluation submitted!\n" +
                    "Total Score: " + eval.getTotal());
            }
        });
        
        viewBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Assigned Sessions: " + evaluator.getAssignedSessions().size());
        });
        
        setLocationRelativeTo(null);
    }
    
    private void loadStudents() {
        List<Student> students = Database.getStudents();
        for (Student s : students) {
            studentCombo.addItem(s);
        }
    }
}