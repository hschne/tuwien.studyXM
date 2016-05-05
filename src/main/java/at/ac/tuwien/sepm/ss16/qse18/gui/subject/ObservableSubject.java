package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import javafx.beans.property.*;

/**
 * A FXML Wrapper around the Subject DTO
 *
 * @author Hans-Joerg Schroedl
 */
public class ObservableSubject {

    private final StringProperty name;
    private final DoubleProperty ects;
    private final StringProperty semester;
    private final IntegerProperty timeSpent;
    private final StringProperty author;
    private Subject subject;

    public ObservableSubject(Subject subject) {
        this.subject = subject;
        name = new SimpleStringProperty(subject.getName());
        ects = new SimpleDoubleProperty(subject.getEcts());
        semester = new SimpleStringProperty(subject.getName());
        timeSpent = new SimpleIntegerProperty(subject.getTimeSpent());
        author = new SimpleStringProperty(subject.getName());
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
        subject.setName(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public double getEcts() {
        return ects.get();
    }

    public void setEcts(float ects) {
        this.ects.set(ects);
        subject.setEcts(ects);
    }

    public DoubleProperty ectsProperty() {
        return ects;
    }

    public String getSemester() {
        return semester.get();
    }

    public void setSemester(String semester) {
        this.semester.set(semester);
        subject.setSemester(semester);
    }

    public StringProperty semesterProperty() {
        return semester;
    }

    public int getTimeSpent() {
        return timeSpent.get();
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent.set(timeSpent);
        subject.setTimeSpent(timeSpent);
    }

    public IntegerProperty timeSpentProperty() {
        return timeSpent;
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
        subject.setAuthor(author);
    }

    public StringProperty authorProperty() {
        return author;
    }

    public Subject getSubject() {
        return subject;
    }



}
