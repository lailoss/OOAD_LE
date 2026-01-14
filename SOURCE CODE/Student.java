public class Student extends User {
    private String researchTitle;
    private String abstractText;
    private String supervisor;
    private String presentationType;
    private Submission submission;
    
    public Student(String userId, String name) {
        super(userId, name, "Student");
    }
    
    public void setResearchTitle(String title) { this.researchTitle = title; }
    public void setAbstractText(String abs) { this.abstractText = abs; }
    public void setSupervisor(String sup) { this.supervisor = sup; }
    public void setPresentationType(String type) { this.presentationType = type; }
    public void setSubmission(Submission sub) { this.submission = sub; }
    
    public String getResearchTitle() { return researchTitle; }
    public String getPresentationType() { return presentationType; }
    public String getSupervisor() { return supervisor; }
    public Submission getSubmission() { return submission; }
    
    public void register() {
        System.out.println("Student " + name + " registered.");
    }
}