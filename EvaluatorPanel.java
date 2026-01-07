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
        
        add(new JLabel("Select Student:"));
        studentCombo = new JComboBox<>();
        loadStudents();
        add(studentCombo);
        
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
        
        add(new JLabel("Comments:"));
        commentArea = new JTextArea(3, 20);
        add(new JScrollPane(commentArea));
        
        JButton evaluateBtn = new JButton("Submit Evaluation");
        add(evaluateBtn);
        
        JButton viewBtn = new JButton("View My Evaluations");
        add(viewBtn);
        
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
                
                JOptionPane.showMessageDialog(this, 
                    "Evaluation submitted!\n" +
                    "Student: " + selected.getName() + "\n" +
                    "Total Score: " + eval.getTotal() + "/40");
            }
        });
        
        viewBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("My Evaluations:\n");
            for (Evaluation eval : evaluator.getEvaluations()) {
                sb.append("- ").append(eval.getStudent().getName())
                  .append(": ").append(eval.getTotal()).append("/40\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
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
            studentCombo.addItem(new Student("S001", "Sample Student"));
        }
    }
}