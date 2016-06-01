package at.ac.tuwien.sepm.ss16.qse18.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Exam
 * represents the entity exam
 * @parameter examid primary key of exam
 * @parameter created indicates the date in which the exam was created
 * @parameter passed indicates whether exam has been passed or not
 * @parameter author name of the user who created the exam
 * @parameter subjectID id of the subject which the exam belongs to
 * @parameter examQuestions List containing the questions of an exam
 *
 * @author Zhang Haixiang
 */
public class Exam {
    private int examid;
    private Timestamp created;
    private boolean passed;
    private String author;
    private int subjectID;
    private List<Question> examQuestions = new ArrayList<>();
    private long examTime;

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

    public long getExamTime() {
        return examTime;
    }

    public void setExamTime(long examTime) {
        this.examTime = examTime;
    }

    @Override public String toString(){
        String ret =  "Exam{" + "examID=" + this.examid + ", created=" + this.getCreated() + ", passed="
            + this.getPassed() + ", author= \"" + this.getAuthor() + "\", subjectID="+ this.getSubjectID()
            + "}";

        for(Question q: examQuestions){
            ret += q.toString();
        }

        return ret;
    }
}
