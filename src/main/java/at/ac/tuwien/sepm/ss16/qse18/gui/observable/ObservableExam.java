package at.ac.tuwien.sepm.ss16.qse18.gui.observable;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.SimpleDateFormat;

/**
 * Created by Felix on 05.06.2016.
 */
public class ObservableExam {
    private IntegerProperty examid;
    private StringProperty name;
    private StringProperty created;
    private StringProperty dueDate;
    private IntegerProperty subject;
    private Exam exam;

    public void setName(String name) {
        this.name.set(name);
        this.exam.setName(name);
    }

    public String getCreated() {
        return created.get();
    }


    public String getDueDate() {
        return dueDate.get();
    }


    public int getSubject() {
        return subject.get();
    }

    public void setSubject(int subject) {
        this.subject.set(subject);
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public ObservableExam(Exam exam) {
        this.exam = exam;
        this.examid = new SimpleIntegerProperty(exam.getExamid());
        this.name = new SimpleStringProperty(exam.getName());
        this.created = new SimpleStringProperty(new SimpleDateFormat("dd-MM-YYYY")
            .format(exam.getCreated()));
        this.dueDate = new SimpleStringProperty(new SimpleDateFormat("dd-MM-YYYY")
            .format(exam.getDueDate()));
        this.subject = new SimpleIntegerProperty(exam.getSubject());
    }

    public Exam getExamInstance() {
        return this.exam;
    }

    public IntegerProperty examidProperty() {
        return this.examid;
    }

    public StringProperty nameProperty() {
        return this.name;
    }

    public StringProperty createdProperty() {
        return this.created;
    }

    public StringProperty dueDateProperty() {
        return this.dueDate;
    }

    public IntegerProperty subjectProperty() {
        return this.subject;
    }

    public void setExamid(int examid) {
        this.exam.setExamid(examid);
        this.examid.set(examid);
    }

    public int getExamid() {
        return this.examid.get();
    }

    public String getName() {
        return this.name.get();
    }

    public String getCreated(String format) {
        return new SimpleDateFormat(format).format(this.exam.getCreated());
    }

    public String getDueDate(String format) {
        return new SimpleDateFormat(format).format(this.exam.getCreated());
    }
}
