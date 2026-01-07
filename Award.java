import java.util.List;

public class Award {
    private String awardType;
    private Student winner;
    private Session session;
    
    public Award(String awardType, Session session) {
        this.awardType = awardType;
        this.session = session;
    }
    
    public Student calculateWinner(List<Evaluation> allEvaluations) {
        List<Student> students = session.getStudents();
        Student bestStudent = null;
        double bestScore = 0;
        
        for (Student s : students) {
            double avgScore = calculateAverageScore(s, allEvaluations);
            if (avgScore > bestScore) {
                bestScore = avgScore;
                bestStudent = s;
            }
        }
        
        this.winner = bestStudent;
        return bestStudent;
    }
    
    private double calculateAverageScore(Student student, List<Evaluation> allEvaluations) {
        int total = 0;
        int count = 0;
        
        for (Evaluation eval : allEvaluations) {
            if (eval.getStudent().equals(student)) {
                total += eval.getTotal();
                count++;
            }
        }
        
        return count > 0 ? (double) total / count : 0;
    }
    
    public Student getWinner() { return winner; }
    public String getAwardType() { return awardType; }
    
    @Override
    public String toString() {
        return awardType + ": " + (winner != null ? winner.getName() : "No winner");
    }
}