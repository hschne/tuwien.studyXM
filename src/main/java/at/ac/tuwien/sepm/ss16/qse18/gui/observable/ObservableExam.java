package at.ac.tuwien.sepm.ss16.qse18.gui.observable;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import javafx.beans.property.*;

import java.sql.Timestamp;

/**
 * @author Zhang Haixiang  on 20.05.2016.
 */
public class ObservableExam {
    private IntegerProperty examid;
    private Timestamp created;
    private BooleanProperty passed;
    private StringProperty author;
    private IntegerProperty subjectID;
    private Exam exam;

    public ObservableExam(Exam exam){
        this.exam = exam;
        this.examid = new SimpleIntegerProperty(exam.getExamid());
        this.passed = new SimpleBooleanProperty(exam.getPassed());
        this.author = new SimpleStringProperty(exam.getAuthor());
        this.subjectID = new SimpleIntegerProperty(exam.getSubjectID());
    }

    public Exam getExamInstance(){
        return this.exam;
    }

    public IntegerProperty examIDProperty(){
        return this.examid;
    }

    public BooleanProperty passedProperty(){
        return this.passed;
    }

    public StringProperty authorProperty(){
        return this.author;
    }

    public IntegerProperty subjectIDProperty(){
        return this.subjectID;
    }

    public int getExamid(){
        return this.examid.get();
    }

    public void setExamid(int examid){
        this.exam.setExamid(examid);
        this.examid.set(examid);
    }


    public int getSubjectID() {
        return subjectID.get();
    }

    public void setSubjectID(int subjectID) {
        this.exam.setSubjectID(subjectID);
        this.subjectID.set(subjectID);
    }


    public boolean getPassed(){
        return this.passed.get();
    }

    public void setPassed(boolean passed){
        this.exam.setPassed(passed);
        this.passed.set(passed);
    }

    public String getAuthor(){
        return this.author.get();
    }

    public void setAuthor(String author){
        this.exam.setAuthor(author);
        this.author.set(author);
    }
}
