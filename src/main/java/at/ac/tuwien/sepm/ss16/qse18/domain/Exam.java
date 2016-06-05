package at.ac.tuwien.sepm.ss16.qse18.domain;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Felix on 05.06.2016.
 */
public class Exam {
    private int examid;
    private Timestamp dueDate;
    private Timestamp created;
    private String name;
    private Subject subject;
    private List<ExerciseExam> exercises;

    public Exam(String name, Timestamp dueDate, Subject subject, List<ExerciseExam> exercises) {
        this.examid = -1;
        this.dueDate = dueDate;
        this.created = new Timestamp(System.currentTimeMillis());
        this.subject = subject;
        this.exercises = exercises;
    }

    public Exam(int examid, Timestamp dueDate, Timestamp created,
        Subject subject, List<ExerciseExam> exercises) {
        this.examid = examid;
        this.dueDate = dueDate;
        this.created = created;
        this.subject = subject;
        this.exercises = exercises;
    }

    public int getExamid() {
        return examid;
    }

    public void setExamid(int examid) {
        this.examid = examid;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<ExerciseExam> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseExam> exercises) {
        this.exercises = exercises;
    }

    @Override public String toString() {
        return "Exam{" +
            "examid=" + examid +
            ", dueDate=" + dueDate +
            ", created=" + created +
            ", name='" + name + '\'' +
            ", subject=" + subject +
            ", exercises=" + exercises +
            '}';
    }
}
