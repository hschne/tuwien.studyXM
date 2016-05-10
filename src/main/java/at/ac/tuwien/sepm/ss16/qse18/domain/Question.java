package at.ac.tuwien.sepm.ss16.qse18.domain;
/*
* @author Philipp Ganiu
* */
public class Question {
    private int questionid;
    private String question;
    private int type;
    private boolean question_passed;
    private boolean already_answered;

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

    public boolean isQuestion_passed() {
        return question_passed;
    }

    public void setQuestion_passed(boolean question_passed) {
        this.question_passed = question_passed;
    }

    public boolean isAlready_answered() {
        return already_answered;
    }

    public void setAlready_answered(boolean already_answered) {
        this.already_answered = already_answered;
    }

}
