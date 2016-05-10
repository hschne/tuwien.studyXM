package at.ac.tuwien.sepm.ss16.qse18.domain;
/*
* @author Philipp Ganiu
* */
public class Question {
    private int questionid;
    private String question;
    private int type;


    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
