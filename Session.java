import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Session {
    private String sessionId;
    private Date date;
    private String venue;
    private String type;
    private List<Student> students;
    private List<Evaluator> evaluators;
    
    public Session(String sessionId, Date date, String venue, String type) {
        this.sessionId = sessionId;
        this.date = date;
        this.venue = venue;
        this.type = type;
        this.students = new ArrayList<>();
        this.evaluators = new ArrayList<>();
    }
    
    public void addStudent(Student student) {
        students.add(student);
    }
    
    public void addEvaluator(Evaluator evaluator) {
        evaluators.add(evaluator);
    }
    
    public String getSessionId() { return sessionId; }
    public Date getDate() { return date; }
    public String getVenue() { return venue; }
    public String getType() { return type; }
    public List<Student> getStudents() { return students; }
    public List<Evaluator> getEvaluators() { return evaluators; }
    
    @Override
    public String toString() {
        return sessionId + " - " + type + " at " + venue;
    }
}