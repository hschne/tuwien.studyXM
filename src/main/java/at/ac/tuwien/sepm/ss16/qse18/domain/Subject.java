package at.ac.tuwien.sepm.ss16.qse18.domain;

import java.text.MessageFormat;

/**
 * Dto Class for subject
 *
 * @author Hans-Joerg Schroedl
 */
public class Subject {

    private int subjectId;

    private String name;

    private float ects;

    private String semester;

    private int timeSpent;

    private String author;

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getEcts() {
        return ects;
    }

    public void setEcts(float ects) {
        this.ects = ects;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semster) {
        this.semester = semster;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    @Override
    public String toString() {
        return MessageFormat.format("[Id:{0},Name:{1},Ects:{2},Semester:{3},Author:{4}]",
            getSubjectId(), getName(), getEcts(), getSemester(), getAuthor());
    }
}
