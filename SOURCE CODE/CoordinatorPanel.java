import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class CoordinatorPanel extends JFrame {
    private Coordinator coordinator;
    private JTextArea outputArea;
    private DefaultListModel<String> sessionListModel;
    private JComboBox<Session> sessionCombo;
    private JList<String> sessionList;
    
    public CoordinatorPanel(Coordinator coordinator) {
        this.coordinator = coordinator;
        setTitle("Coordinator Panel - " + coordinator.getName());
        setSize(900, 700);
        setLayout(new BorderLayout(10, 10));
        
        // ========== NAVIGATION BAR ==========
        JPanel navPanel = createNavPanel();
        
        // ========== MAIN CONTENT ==========
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Create tabs
        JPanel sessionTab = createSessionTab();
        JPanel awardTab = createAwardTab();
        JPanel studentTab = createStudentTab();
        
        tabbedPane.addTab("📅 Session Management", sessionTab);
        tabbedPane.addTab("🏆 Award Management", awardTab);
        tabbedPane.addTab("👨‍🎓 Student Management", studentTab);
        
        // ========== OUTPUT PANEL ==========
        outputArea = new JTextArea(8, 70);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Activity Log"));
        
        // ========== ASSEMBLE ==========
        add(navPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(outputScroll, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
    }
    
    // ========== NAVIGATION PANEL ==========
    private JPanel createNavPanel() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(new Color(240, 240, 240));
        navPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel titleLabel = new JLabel("Coordinator Dashboard - " + coordinator.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutBtn.setBackground(new Color(220, 80, 60));
        logoutBtn.setForeground(Color.WHITE);
        
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Return to login page?", "Logout", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
        
        navPanel.add(titleLabel, BorderLayout.WEST);
        navPanel.add(logoutBtn, BorderLayout.EAST);
        return navPanel;
    }
    
    // ========== TAB 1: SESSION MANAGEMENT ==========
    private JPanel createSessionTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Left: Create Session Panel
        JPanel createPanel = new JPanel(new GridBagLayout());
        createPanel.setBorder(BorderFactory.createTitledBorder("Create New Session"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Session ID
        gbc.gridx = 0; gbc.gridy = 0;
        createPanel.add(new JLabel("Session ID*:"), gbc);
        gbc.gridx = 1;
        JTextField sessionIdField = new JTextField(15);
        createPanel.add(sessionIdField, gbc);
        
        // Venue
        gbc.gridx = 0; gbc.gridy = 1;
        createPanel.add(new JLabel("Venue*:"), gbc);
        gbc.gridx = 1;
        JTextField venueField = new JTextField(15);
        createPanel.add(venueField, gbc);
        
        // Type
        gbc.gridx = 0; gbc.gridy = 2;
        createPanel.add(new JLabel("Type*:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Oral", "Poster"});
        createPanel.add(typeCombo, gbc);
        
        // Create Button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton createBtn = new JButton("Create Session");
        createBtn.setBackground(new Color(70, 130, 180));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(new Font("Arial", Font.BOLD, 12));
        createPanel.add(createBtn, gbc);
        
        // Right: Session List with Assignment
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        
        // Session List
        sessionListModel = new DefaultListModel<>();
        sessionList = new JList<>(sessionListModel);
        JScrollPane sessionScroll = new JScrollPane(sessionList);
        sessionScroll.setBorder(BorderFactory.createTitledBorder("Existing Sessions"));
        
        // Assignment Panel
        JPanel assignPanel = new JPanel(new BorderLayout(5, 5));
        assignPanel.setBorder(BorderFactory.createTitledBorder("Assign Students to Session"));
        
        JPanel assignControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JButton assignBtn = new JButton("Assign Selected Student");
        assignBtn.setBackground(new Color(60, 179, 113));
        assignBtn.setForeground(Color.WHITE);
        
        JButton viewSessionBtn = new JButton("View Session Details");
        JButton refreshBtn = new JButton("Refresh");
        
        assignControlPanel.add(assignBtn);
        assignControlPanel.add(viewSessionBtn);
        assignControlPanel.add(refreshBtn);
        
        // Student List for Assignment
        DefaultListModel<String> assignStudentModel = new DefaultListModel<>();
        JList<String> assignStudentList = new JList<>(assignStudentModel);
        JScrollPane assignStudentScroll = new JScrollPane(assignStudentList);
        assignStudentScroll.setBorder(BorderFactory.createTitledBorder("Available Students"));
        
        assignPanel.add(assignControlPanel, BorderLayout.NORTH);
        assignPanel.add(assignStudentScroll, BorderLayout.CENTER);
        
        rightPanel.add(sessionScroll, BorderLayout.CENTER);
        rightPanel.add(assignPanel, BorderLayout.SOUTH);
        
        // Load data
        refreshSessionList();
        loadStudentsForAssignment(assignStudentModel);
        
        // ACTIONS
        createBtn.addActionListener(e -> {
            String sessionId = sessionIdField.getText().trim();
            String venue = venueField.getText().trim();
            
            if (sessionId.isEmpty() || venue.isEmpty()) {
                showError("Please fill required fields!");
                return;
            }
            
            // Check if session ID already exists
            for (Session s : LoginFrame.allSessions) {
                if (s.getSessionId().equals(sessionId)) {
                    showError("Session ID already exists!");
                    return;
                }
            }
            
            Session session = new Session(sessionId, new Date(), venue, (String) typeCombo.getSelectedItem());
            LoginFrame.allSessions.add(session);
            refreshSessionList();
            refreshSessionCombo();
            outputArea.append("✓ Session created: " + session + "\n");
            
            // Clear fields
            sessionIdField.setText("");
            venueField.setText("");
        });
        
        assignBtn.addActionListener(e -> {
            int sessionIndex = sessionList.getSelectedIndex();
            int studentIndex = assignStudentList.getSelectedIndex();
            
            if (sessionIndex == -1 || studentIndex == -1) {
                showError("Please select both a session and a student!");
                return;
            }
            
            // Get selected session
            Session selectedSession = LoginFrame.allSessions.get(sessionIndex);
            
            // Get selected student
            Student selectedStudent = getStudentByIndex(studentIndex);
            
            if (selectedStudent != null) {
                // Check if student already in this session
                if (selectedSession.getStudents().contains(selectedStudent)) {
                    showWarning("Student already assigned to this session!", "Duplicate");
                    return;
                }
                
                selectedSession.addStudent(selectedStudent);
                outputArea.append("✓ Assigned " + selectedStudent.getName() + 
                    " to session: " + selectedSession.getSessionId() + "\n");
                showSuccess("Assigned " + selectedStudent.getName() + " to " + 
                    selectedSession.getSessionId());
                
                // Update session list display
                refreshSessionList();
            }
        });
        
        viewSessionBtn.addActionListener(e -> {
            int sessionIndex = sessionList.getSelectedIndex();
            if (sessionIndex == -1) {
                showError("Please select a session first!");
                return;
            }
            
            Session session = LoginFrame.allSessions.get(sessionIndex);
            showSessionDetails(session);
        });
        
        refreshBtn.addActionListener(e -> {
            refreshSessionList();
            loadStudentsForAssignment(assignStudentModel);
            outputArea.append("✓ Refreshed session and student lists\n");
        });
        
        // Add panels
        panel.add(createPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ========== TAB 2: AWARD MANAGEMENT ==========
    private JPanel createAwardTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Left: Configuration Panel
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBorder(BorderFactory.createTitledBorder("Award Configuration"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Session Selection
        gbc.gridx = 0; gbc.gridy = 0;
        configPanel.add(new JLabel("Select Session*:"), gbc);
        gbc.gridx = 1;
        sessionCombo = new JComboBox<>();
        refreshSessionCombo();
        configPanel.add(sessionCombo, gbc);
        
        // Refresh button for sessions
        gbc.gridx = 2;
        JButton refreshSessionsBtn = new JButton("🔄");
        refreshSessionsBtn.setToolTipText("Refresh session list");
        refreshSessionsBtn.addActionListener(e -> refreshSessionCombo());
        configPanel.add(refreshSessionsBtn, gbc);
        
        // Award Type
        gbc.gridx = 0; gbc.gridy = 1;
        configPanel.add(new JLabel("Award Type*:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        JComboBox<String> awardTypeCombo = new JComboBox<>(new String[]{
            "Best Oral Presentation", 
            "Best Poster Presentation",
            "People's Choice Award"
        });
        configPanel.add(awardTypeCombo, gbc);
        
        // Right: Action Panel
        JPanel actionPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Award Actions"));
        
        JButton autoBtn = createStyledButton("🏆 Auto-Calculate Winner", 
            new Color(60, 179, 113), "Award goes to highest scorer");
        JButton manualBtn = createStyledButton("👑 Give Award Manually", 
            new Color(255, 140, 0), "Manually select award winner");
        JButton viewBtn = createStyledButton("📋 View All Awards", 
            new Color(100, 100, 255), "View all issued awards");
        JButton checkSessionBtn = createStyledButton("👁️ Check Session Students", 
            new Color(150, 100, 200), "View students in selected session");
        
        actionPanel.add(autoBtn);
        actionPanel.add(manualBtn);
        actionPanel.add(viewBtn);
        actionPanel.add(checkSessionBtn);
        
        // ACTIONS - FIXED AWARD CREATION
        autoBtn.addActionListener(e -> {
            Session session = (Session) sessionCombo.getSelectedItem();
            if (session == null) {
                showError("Please select a session first!");
                return;
            }
            
            if (session.getStudents().isEmpty()) {
                showError("No students assigned to this session!");
                return;
            }
            
            // Create award object
            Award award = new Award((String) awardTypeCombo.getSelectedItem(), session);
            
            // Calculate winner - THIS SETS THE WINNER INSIDE THE AWARD OBJECT
            Student winner = award.calculateWinner(LoginFrame.allEvaluations);
            
            if (winner != null) {
                // Add award to system
                LoginFrame.allAwards.add(award);
                
                // DEBUG: Verify award was created
                System.out.println("=== AUTO AWARD CREATED ===");
                System.out.println("Award type: " + award.getAwardType());
                System.out.println("Winner: " + (award.getWinner() != null ? award.getWinner().getName() : "NULL"));
                System.out.println("Total awards now: " + LoginFrame.allAwards.size());
                
                outputArea.append("🏆 " + award.getAwardType() + " awarded to: " + 
                    winner.getName() + " (Auto-calculated)\n");
                
                showSuccessMessage(
                    "<html><h3>Award Issued!</h3>" +
                    "<b>Award:</b> " + award.getAwardType() + "<br>" +
                    "<b>Winner:</b> " + winner.getName() + "<br>" +
                    "<b>Method:</b> Auto-calculated (highest score)<br>" +
                    "<b>Session:</b> " + session.getSessionId() + "</html>",
                    "Award Successful"
                );
            } else {
                showWarning("No eligible students with evaluations in this session!", "No Winner");
            }
        });
        
        manualBtn.addActionListener(e -> {
            Session session = (Session) sessionCombo.getSelectedItem();
            if (session == null) {
                showError("Please select a session first!");
                return;
            }
            
            if (session.getStudents().isEmpty()) {
                showError("No students assigned to this session!");
                return;
            }
            
            // Show student selection dialog
            Student selectedStudent = showStudentSelectionDialog(session);
            if (selectedStudent != null) {
                // CREATE AWARD WITH WINNER IN CONSTRUCTOR - THIS IS THE FIX
                Award award = new Award((String) awardTypeCombo.getSelectedItem(), session, selectedStudent);
                
                // Add award to system
                LoginFrame.allAwards.add(award);
                
                // DEBUG: Verify award was created
                System.out.println("=== MANUAL AWARD CREATED ===");
                System.out.println("Award type: " + award.getAwardType());
                System.out.println("Winner: " + (award.getWinner() != null ? award.getWinner().getName() : "NULL"));
                System.out.println("Total awards now: " + LoginFrame.allAwards.size());
                
                outputArea.append("👑 " + award.getAwardType() + " manually awarded to: " + 
                    selectedStudent.getName() + "\n");
                
                showSuccessMessage(
                    "<html><h3>Manual Award Issued!</h3>" +
                    "<b>Award:</b> " + award.getAwardType() + "<br>" +
                    "<b>Winner:</b> " + selectedStudent.getName() + "<br>" +
                    "<b>Method:</b> Coordinator's choice<br>" +
                    "<b>Session:</b> " + session.getSessionId() + "</html>",
                    "Award Successful"
                );
            }
        });
        
        viewBtn.addActionListener(e -> showAllAwards());
        
        checkSessionBtn.addActionListener(e -> {
            Session session = (Session) sessionCombo.getSelectedItem();
            if (session == null) {
                showError("Please select a session first!");
                return;
            }
            showSessionDetails(session);
        });
        
        // Add panels
        panel.add(configPanel, BorderLayout.WEST);
        panel.add(actionPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ========== TAB 3: STUDENT MANAGEMENT ==========
    private JPanel createStudentTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        DefaultListModel<String> studentListModel = new DefaultListModel<>();
        JList<String> studentList = new JList<>(studentListModel);
        JScrollPane scrollPane = new JScrollPane(studentList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Registered Students"));
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton refreshBtn = createStyledButton("🔄 Refresh List", 
            new Color(100, 100, 100), "Reload student list");
        JButton detailsBtn = createStyledButton("👁️ View Details", 
            new Color(70, 130, 180), "View student details");
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(detailsBtn);
        
        // Actions
        refreshBtn.addActionListener(e -> {
            loadStudents(studentListModel);
            outputArea.append("✓ Refreshed student list\n");
        });
        
        detailsBtn.addActionListener(e -> {
            int index = studentList.getSelectedIndex();
            if (index == -1) {
                showError("Please select a student first!");
                return;
            }
            
            Student student = getStudentByIndex(index);
            if (student != null) {
                showStudentDetails(student);
            }
        });
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Initial load
        loadStudents(studentListModel);
        
        return panel;
    }
    
    // ========== HELPER METHODS ==========
    private JButton createStyledButton(String text, Color bgColor, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setToolTipText(tooltip);
        return button;
    }
    
    private void refreshSessionList() {
        sessionListModel.clear();
        for (int i = 0; i < LoginFrame.allSessions.size(); i++) {
            Session session = LoginFrame.allSessions.get(i);
            int studentCount = session.getStudents().size();
            String info = String.format("[%d] %s - %s (%d students)", 
                i + 1, session.getSessionId(), session.getType(), studentCount);
            sessionListModel.addElement(info);
        }
        if (sessionListModel.isEmpty()) {
            sessionListModel.addElement("No sessions created yet");
        }
    }
    
    private void refreshSessionCombo() {
        sessionCombo.removeAllItems();
        for (Session session : LoginFrame.allSessions) {
            sessionCombo.addItem(session);
        }
        if (sessionCombo.getItemCount() == 0) {
            sessionCombo.addItem(new Session("No sessions", new Date(), "N/A", "N/A"));
        }
    }
    
    private void loadStudents(DefaultListModel<String> model) {
        model.clear();
        for (int i = 0; i < LoginFrame.allUsers.size(); i++) {
            Object obj = LoginFrame.allUsers.get(i);
            if (obj instanceof Student) {
                Student s = (Student) obj;
                String title = s.getResearchTitle() != null ? 
                    s.getResearchTitle() : "No title";
                if (title.length() > 30) title = title.substring(0, 27) + "...";
                
                String info = String.format("[%d] %s (ID: %s) - %s", 
                    i + 1, s.getName(), s.getUserId(), title);
                model.addElement(info);
            }
        }
        if (model.isEmpty()) {
            model.addElement("No students registered yet");
        }
    }
    
    private void loadStudentsForAssignment(DefaultListModel<String> model) {
        model.clear();
        for (int i = 0; i < LoginFrame.allUsers.size(); i++) {
            Object obj = LoginFrame.allUsers.get(i);
            if (obj instanceof Student) {
                Student s = (Student) obj;
                model.addElement(String.format("[%d] %s (ID: %s)", i + 1, s.getName(), s.getUserId()));
            }
        }
        if (model.isEmpty()) {
            model.addElement("No students available");
        }
    }
    
    private Student getStudentByIndex(int listIndex) {
        int studentCount = 0;
        for (Object obj : LoginFrame.allUsers) {
            if (obj instanceof Student) {
                if (studentCount == listIndex) {
                    return (Student) obj;
                }
                studentCount++;
            }
        }
        return null;
    }
    
    private Student showStudentSelectionDialog(Session session) {
        JDialog dialog = new JDialog(this, "Select Award Winner for " + session.getSessionId(), true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout());
        
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        
        // Add students assigned to this session
        List<Student> sessionStudents = session.getStudents();
        for (int i = 0; i < sessionStudents.size(); i++) {
            Student student = sessionStudents.get(i);
            String evalInfo = getStudentEvaluationInfo(student);
            model.addElement(String.format("[%d] %s - %s", 
                i + 1, student.getName(), evalInfo));
        }
        
        if (model.isEmpty()) {
            model.addElement("No students assigned to this session");
            list.setEnabled(false);
        }
        
        JScrollPane scrollPane = new JScrollPane(list);
        
        JPanel buttonPanel = new JPanel();
        JButton selectBtn = new JButton("Select Winner");
        JButton cancelBtn = new JButton("Cancel");
        
        final Student[] selectedStudent = {null};
        
        selectBtn.addActionListener(e -> {
            int index = list.getSelectedIndex();
            if (index != -1 && !model.isEmpty()) {
                selectedStudent[0] = sessionStudents.get(index);
                dialog.dispose();
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(selectBtn);
        buttonPanel.add(cancelBtn);
        
        JLabel infoLabel = new JLabel("Session: " + session.getSessionId() + 
            " | Students: " + sessionStudents.size());
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        dialog.add(infoLabel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        
        return selectedStudent[0];
    }
    
    private String getStudentEvaluationInfo(Student student) {
        int evalCount = 0;
        int totalScore = 0;
        
        for (Evaluation eval : LoginFrame.allEvaluations) {
            if (eval.getStudent() != null && eval.getStudent().equals(student)) {
                totalScore += eval.getTotal();
                evalCount++;
            }
        }
        
        if (evalCount == 0) return "No evaluations";
        return String.format("Avg: %.1f/40 (%d evals)", (double) totalScore / evalCount, evalCount);
    }
    
    private void showSessionDetails(Session session) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><h2>Session Details</h2>");
        sb.append("<b>Session ID:</b> ").append(session.getSessionId()).append("<br>");
        sb.append("<b>Type:</b> ").append(session.getType()).append("<br>");
        sb.append("<b>Venue:</b> ").append(session.getVenue()).append("<br>");
        sb.append("<b>Date:</b> ").append(session.getDate()).append("<br>");
        sb.append("<b>Students assigned:</b> ").append(session.getStudents().size()).append("<br><br>");
        
        if (!session.getStudents().isEmpty()) {
            sb.append("<h3>Assigned Students:</h3>");
            for (int i = 0; i < session.getStudents().size(); i++) {
                Student student = session.getStudents().get(i);
                sb.append("<b>").append(i + 1).append(".</b> ").append(student.getName());
                if (student.getResearchTitle() != null) {
                    sb.append(" - ").append(student.getResearchTitle());
                }
                sb.append(" (ID: ").append(student.getUserId()).append(")<br>");
                
                // Show evaluations for this student
                for (Evaluation eval : LoginFrame.allEvaluations) {
                    if (eval.getStudent() != null && eval.getStudent().equals(student)) {
                        sb.append("&nbsp;&nbsp;&nbsp;Evaluation: ").append(eval.getTotal()).append("/40");
                        if (eval.getComment() != null && !eval.getComment().isEmpty()) {
                            sb.append(" - ").append(eval.getComment());
                        }
                        sb.append("<br>");
                    }
                }
            }
        } else {
            sb.append("<i>No students assigned to this session</i><br>");
        }
        sb.append("</html>");
        
        JOptionPane.showMessageDialog(this, sb.toString(), 
            "Session: " + session.getSessionId(), JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showStudentDetails(Student student) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><h2>Student Details</h2>");
        sb.append("<b>Name:</b> ").append(student.getName()).append("<br>");
        sb.append("<b>ID:</b> ").append(student.getUserId()).append("<br>");
        sb.append("<b>Research Title:</b> ").append(student.getResearchTitle() != null ? 
            student.getResearchTitle() : "Not set").append("<br>");
        sb.append("<b>Supervisor:</b> ").append(student.getSupervisor() != null ? 
            student.getSupervisor() : "Not set").append("<br>");
        sb.append("<b>Presentation Type:</b> ").append(student.getPresentationType() != null ? 
            student.getPresentationType() : "Not set").append("<br><br>");
        
        // Find which sessions student is assigned to
        sb.append("<h3>Sessions:</h3>");
        boolean inAnySession = false;
        for (Session session : LoginFrame.allSessions) {
            if (session.getStudents().contains(student)) {
                sb.append("• ").append(session.getSessionId()).append(" (").append(session.getType()).append(")<br>");
                inAnySession = true;
            }
        }
        if (!inAnySession) sb.append("<i>Not assigned to any session</i><br>");
        
        sb.append("<h3>Evaluations:</h3>");
        boolean hasEvals = false;
        for (Evaluation eval : LoginFrame.allEvaluations) {
            if (eval.getStudent() != null && eval.getStudent().equals(student)) {
                sb.append("• Score: ").append(eval.getTotal()).append("/40");
                if (eval.getComment() != null && !eval.getComment().isEmpty()) {
                    sb.append(" - \"").append(eval.getComment()).append("\"");
                }
                sb.append(" (by ").append(eval.getEvaluator().getName()).append(")<br>");
                hasEvals = true;
            }
        }
        if (!hasEvals) sb.append("<i>No evaluations yet</i><br>");
        
        sb.append("<h3>Awards:</h3>");
        boolean hasAwards = false;
        for (Award award : LoginFrame.allAwards) {
            if (award.getWinner() != null && award.getWinner().equals(student)) {
                sb.append("🏆 ").append(award.getAwardType());
                if (award.isManual()) sb.append(" (Manual selection)");
                sb.append("<br>");
                hasAwards = true;
            }
        }
        if (!hasAwards) sb.append("<i>No awards yet</i><br>");
        
        sb.append("</html>");
        
        JOptionPane.showMessageDialog(this, sb.toString(), 
            "Student: " + student.getName(), JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAllAwards() {
        if (LoginFrame.allAwards.isEmpty()) {
            showInfo("No awards have been issued yet.", "Awards");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("<html><h2>All Issued Awards</h2>");
        sb.append("<table border='1' cellpadding='5' style='border-collapse: collapse;'>");
        sb.append("<tr bgcolor='#f0f0f0'><th>#</th><th>Award</th><th>Winner</th><th>Session</th><th>Method</th></tr>");
        
        int row = 0;
        for (int i = 0; i < LoginFrame.allAwards.size(); i++) {
            Award award = LoginFrame.allAwards.get(i);
            String bgColor = row % 2 == 0 ? "white" : "#f9f9f9";
            sb.append("<tr bgcolor='").append(bgColor).append("'>");
            sb.append("<td>").append(i + 1).append("</td>");
            sb.append("<td>").append(award.getAwardType()).append("</td>");
            sb.append("<td>").append(award.getWinner() != null ? 
                award.getWinner().getName() : "No winner").append("</td>");
            sb.append("<td>").append(award.getSession().getSessionId()).append("</td>");
            sb.append("<td>").append(award.isManual() ? "Manual" : "Auto").append("</td>");
            sb.append("</tr>");
            row++;
        }
        sb.append("</table><br>");
        sb.append("<b>Total Awards:</b> ").append(LoginFrame.allAwards.size());
        sb.append("</html>");
        
        showInfo(sb.toString(), "All Awards (" + LoginFrame.allAwards.size() + ")");
    }
    
    // Utility dialogs
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showWarning(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    private void showInfo(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showSuccessMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}