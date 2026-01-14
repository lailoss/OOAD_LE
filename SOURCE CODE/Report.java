import java.util.Date;

public class Report {
    private Date generatedDate;
    private String content;
    
    public Report() {
        this.generatedDate = new Date();
    }
    
    public void generateSummary() {
        System.out.println("Generating summary report...");
        content = "Report generated at: " + generatedDate;
    }
    
    public void exportToPDF() {
        System.out.println("Exporting to PDF...");
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
}