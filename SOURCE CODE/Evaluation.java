public class Evaluation {
    private Student student;
    private Evaluator evaluator;
    private int problemClarity;
    private int methodology;
    private int results;
    private int presentation;
    private String comment;
    
    public Evaluation(Student student, Evaluator evaluator) {
        this.student = student;
        this.evaluator = evaluator;
    }
    
    public int getTotalScore() {
        return problemClarity + methodology + results + presentation;
    }
    
    public void setProblemClarity(int score) { 
        if (score >= 1 && score <= 10) this.problemClarity = score; 
    }
    public void setMethodology(int score) { 
        if (score >= 1 && score <= 10) this.methodology = score; 
    }
    public void setResults(int score) { 
        if (score >= 1 && score <= 10) this.results = score; 
    }
    public void setPresentation(int score) { 
        if (score >= 1 && score <= 10) this.presentation = score; 
    }
    public void setComment(String comment) { this.comment = comment; }
    
    public Student getStudent() { return student; }
    public int getTotal() { return getTotalScore(); }
    public String getComment() { return comment; }
    public Evaluator getEvaluator() { return evaluator; }
    
    @Override
    public String toString() {
        return student.getName() + ": " + getTotalScore() + "/40";
    }
}