package models;

import java.util.ArrayList;
import java.util.List;

public class Evaluator extends User {
    private List<Session> assignedSessions;
    
    public Evaluator(String userId, String name) {
        super(userId, name, "Evaluator");
        this.assignedSessions = new ArrayList<>();
    }
    
    public void evaluate(Student student, Evaluation evaluation) {
        System.out.println("Evaluating student: " + student.getName());
    }
    
    public List<Session> getAssignedSessions() {
        return assignedSessions;
    }
    
    public void addSession(Session session) {
        assignedSessions.add(session);
    }
}