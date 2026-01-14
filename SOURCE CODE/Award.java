import java.util.List;

public class Award {
    private String awardType;
    private Student winner;
    private Session session;
    private boolean isManual;
    
    // Constructor for auto-calculated awards
    public Award(String awardType, Session session) {
        this.awardType = awardType;
        this.session = session;
        this.isManual = false;
        this.winner = null; // Will be set by calculateWinner()
    }
    
    // Constructor for manual awards - WINNER IS SET HERE
    public Award(String awardType, Session session, Student manualWinner) {
        this.awardType = awardType;
        this.session = session;
        this.winner = manualWinner; // WINNER SET IN CONSTRUCTOR
        this.isManual = true;
    }
    
    // Calculate winner based on highest average score
    public Student calculateWinner(List<Evaluation> allEvaluations) {
        if (session.getStudents().isEmpty()) {
            return null;
        }
        
        Student bestStudent = null;
        double bestScore = -1;
        
        for (Student student : session.getStudents()) {
            double avgScore = calculateAverageScore(student, allEvaluations);
            if (avgScore > bestScore) {
                bestScore = avgScore;
                bestStudent = student;
            }
        }
        
        this.winner = bestStudent; // SET THE WINNER
        return bestStudent;
    }
    
    // Manual winner setter
    public void setWinnerManually(Student winner) {
        this.winner = winner;
        this.isManual = true;
    }
    
    // Calculate average score for a student
    private double calculateAverageScore(Student student, List<Evaluation> allEvaluations) {
        int total = 0;
        int count = 0;
        
        for (Evaluation eval : allEvaluations) {
            if (eval.getStudent() != null && eval.getStudent().equals(student)) {
                total += eval.getTotal();
                count++;
            }
        }
        
        return count > 0 ? (double) total / count : 0;
    }
    
    // GETTERS - MAKE SURE THESE EXIST
    public Student getWinner() { 
        return this.winner; 
    }
    
    public String getAwardType() { 
        return this.awardType; 
    }
    
    public Session getSession() { 
        return this.session; 
    }
    
    public boolean isManual() { 
        return this.isManual; 
    }
    
    @Override
    public String toString() {
        if (winner == null) {
            return awardType + " (No winner yet)";
        }
        return awardType + " -> " + winner.getName() + (isManual ? " (Manual)" : " (Auto)");
    }
}