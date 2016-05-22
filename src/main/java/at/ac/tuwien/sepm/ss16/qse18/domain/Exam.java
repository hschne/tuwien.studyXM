package at.ac.tuwien.sepm.ss16.qse18.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhang Haixiang
 */
public class Exam {
    private int examid;
    private Timestamp created;
    private boolean passed;
    private String author;
    private int subjectID;
    private List<Question> examQuestions = new ArrayList<>();

    public int getExamid(){
        return this.examid;
    }

    public void setExamid(int examid){
        this.examid = examid;
    }

    public Timestamp getCreated(){
        return this.created;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public void setCreated(Timestamp created){
        this.created = created;
    }

    public boolean getPassed(){
        return this.passed;
    }

    public void setPassed(boolean passed){
        this.passed = passed;
    }

    public String getAuthor(){
        return this.author;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public List<Question> getExamQuestions() {
        return examQuestions;
    }

    public void setExamQuestions(List<Question> examQuestions) {
        this.examQuestions = examQuestions;
    }

    @Override public String toString(){
        return "Exam{" + "examID=" + this.examid + ", created=" + this.getCreated() + ", passed="
            + this.getPassed() + ", author= \"" + this.getAuthor() + "\", subjectID="+ this.getSubjectID()
            + "}"; //TODO anpassen nachdem Question dazu gemerged wurde
    }
}
