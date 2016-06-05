package at.ac.tuwien.sepm.ss16.qse18.gui.observable;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import javafx.beans.property.*;

import java.text.SimpleDateFormat;

/**
 * @author Zhang Haixiang  on 20.05.2016.
 */
public class ObservableExerciseExam {
    private IntegerProperty examid;
    private StringProperty created;
    private BooleanProperty passed;
    private StringProperty author;
    private IntegerProperty subjectID;
    private ExerciseExam exerciseExam;

    public ObservableExerciseExam(ExerciseExam exerciseExam) {
        this.exerciseExam = exerciseExam;
        this.examid = new SimpleIntegerProperty(exerciseExam.getExamid());
        this.passed = new SimpleBooleanProperty(exerciseExam.getPassed());
        this.author = new SimpleStringProperty(exerciseExam.getAuthor());
        this.subjectID = new SimpleIntegerProperty(exerciseExam.getSubjectID());
        this.created = new SimpleStringProperty(new SimpleDateFormat("dd-MM-YYYY")
            .format(exerciseExam.getCreated()));
    }

    public ExerciseExam getExamInstance(){
        return this.exerciseExam;
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
        this.exerciseExam.setExamid(examid);
        this.examid.set(examid);
    }

    public int getSubjectID() {
        return subjectID.get();
    }

    public void setSubjectID(int subjectID) {
        this.exerciseExam.setSubjectID(subjectID);
        this.subjectID.set(subjectID);
    }


    public boolean getPassed(){
        return this.passed.get();
    }

    public void setPassed(boolean passed){
        this.exerciseExam.setPassed(passed);
        this.passed.set(passed);
    }

    public String getAuthor(){
        return this.author.get();
    }

    public void setAuthor(String author){
        this.exerciseExam.setAuthor(author);
        this.author.set(author);
    }

    public StringProperty createdProperty() {
        return this.created;
    }

    public String getCreated(String format) {
        return new SimpleDateFormat(format).format(this.exerciseExam.getCreated());
    }

    public void setCreated() {}
}
