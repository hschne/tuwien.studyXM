package at.ac.tuwien.sepm.ss16.qse18.domain;

import java.sql.Timestamp;

/**
 * Created by Zhang Haixiang on 10.05.2016.
 */
public class Exam {
    private int examid;
    private Timestamp created;
    private boolean passed;
    private String author;

    public int getExamid(){
        return this.examid;
    }

    public void setExamid(int examid){
        this.examid = examid;
    }

    public Timestamp getCreated(){
        return this.created;
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
}
