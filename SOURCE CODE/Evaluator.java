import java.util.ArrayList;
import java.util.List;

public class Evaluator extends User {
    private List<Session> assignedSessions;
    private List<Evaluation> evaluations;
    
    public Evaluator(String userId, String name) {
        super(userId, name, "Evaluator");
        this.assignedSessions = new ArrayList<>();
        this.evaluations = new ArrayList<>();
    }
    
    public void evaluate(Student student, Evaluation evaluation) {
        evaluations.add(evaluation);
        System.out.println("Evaluated student: " + student.getName());
    }
    
    public List<Session> getAssignedSessions() { return assignedSessions; }
    public List<Evaluation> getEvaluations() { return evaluations; }
    
    public void addSession(Session session) {
        assignedSessions.add(session);
    }
    
    public void addEvaluation(Evaluation eval) {
        evaluations.add(eval);
    }
}