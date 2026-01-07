package utils;

import models.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    // Static lists to simulate database
    public static List<Student> students = new ArrayList<>();
    public static List<Evaluator> evaluators = new ArrayList<>();
    public static List<Coordinator> coordinators = new ArrayList<>();
    public static List<Session> sessions = new ArrayList<>();
    public static List<Evaluation> evaluations = new ArrayList<>();
    public static List<Award> awards = new ArrayList<>();
    
    // Helper methods
    public static void addStudent(Student s) { students.add(s); }
    public static List<Student> getStudents() { return students; }
    public static void addSession(Session s) { sessions.add(s); }
    public static void addEvaluation(Evaluation e) { evaluations.add(e); }
}