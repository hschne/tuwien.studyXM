package at.ac.tuwien.sepm.ss16.qse18.domain;

import java.io.Serializable;

/**
 * Class Question
 * represents the entity question
 * @parameter questionId primary key of question
 * @parameter questionString content of question(the actual question)
 * @parameter type indicated the type of question
 * @parameter questionTime indicates the estimated time needed to answer the question
 *
 * @author Felix Almer
 *
 * */
public class Question implements Serializable {
    private int questionId;
    private String questionString;
    private QuestionType type;
    private long questionTime;

    public Question() {
        //Only needed for unit testing!
    }

    public Question(int questionId, String question, QuestionType type, long questionTime) {
        this.questionId = questionId;
        this.questionString = question;
        this.type = type;
        this.questionTime = questionTime;
    }

    public Question(String question, QuestionType type, long questionTime) {
        // -1 specifies a question which is not yet persistent
        this.questionId = -1;
        this.questionString = question;
        this.type = type;
        this.questionTime = questionTime;

    }

    public long getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(long questionTime) {
        this.questionTime = questionTime;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return questionString;
    }

    public void setQuestion(String question) {
        this.questionString = question;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    @Override public String toString() {
        return "Question{" +
            "questionId=" + questionId +
            ", question='" + questionString + '\'' +
            ", type=" + type +
            ", questionTime=" + questionTime +
            '}';
    }

    @Override public int hashCode() {
        int result = questionId;
        result = 31 * result + (questionString != null ? questionString.hashCode() : 0);
        result = 31 * result + type.hashCode();
        result = 31 * result + (int) (questionTime ^ (questionTime >>> 32));
        return result;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Question question1 = (Question) o;

        if (questionId != question1.questionId)
            return false;
        if (questionString != null ?
            !questionString.equals(question1.questionString) : question1.questionString != null)
            return false;
        return type == question1.type;

    }
}
