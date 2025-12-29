package models;

import java.util.List;
import utils.Database;

public class Award {
    private String awardId;
    private String awardType; // "Best Oral", "Best Poster", "People's Choice"
    private Student winner;
    private Session session;
    
    public Award(String awardType, Session session) {
        this.awardType = awardType;
        this.session = session;
    }
    
    public Student calculateWinner() {
        List<Student> students = session.getStudents();
        Student bestStudent = null;
        double bestScore = 0;
        
        for (Student s : students) {
            double avgScore = calculateAverageScore(s);
            if (avgScore > bestScore) {
                bestScore = avgScore;
                bestStudent = s;
            }
        }
        
        this.winner = bestStudent;
        return bestStudent;
    }
    
    private double calculateAverageScore(Student student) {
        // Simplified - calculate from evaluations
        int total = 0;
        int count = 0;
        
        for (models.Evaluation eval : Database.evaluations) {
            if (eval.getStudent().equals(student)) {
                total += eval.getTotal();
                count++;
            }
        }
        
        return count > 0 ? (double) total / count : 0;
    }
    
    // Getters
    public Student getWinner() { return winner; }
    public String getAwardType() { return awardType; }
}