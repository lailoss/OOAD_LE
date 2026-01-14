import java.util.Date;

public class Submission {
    private String filePath;
    private Date submitDate;
    private String status;
    
    public Submission(String filePath, Date submitDate) {
        this.filePath = filePath;
        this.submitDate = submitDate;
        this.status = "Submitted";
    }
    
    public void upload() {
        System.out.println("File uploaded: " + filePath);
    }
    
    public String getFilePath() { return filePath; }
    public Date getSubmitDate() { return submitDate; }
    public String getStatus() { return status; }
}