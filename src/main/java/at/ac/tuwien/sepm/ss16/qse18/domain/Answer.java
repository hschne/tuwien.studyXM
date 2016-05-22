package at.ac.tuwien.sepm.ss16.qse18.domain;

import at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity.ObservableAnswer;

/**
 * Created by Felix on 06.05.2016.
 */
public class Answer {
    private int answerId;
    private QuestionType type;
    private String answer;
    private boolean isCorrect;
    private Question q;

    public Answer(){/*this constructor is left empty on purpose*/}

    public Answer(int answerId, QuestionType type, String answer, boolean isCorrect, Question q) {
        this.answerId = answerId;
        this.type = type;
        this.answer = answer;
        this.isCorrect = isCorrect;
        this.q = q;
    }

    public Answer(QuestionType type, String answer, boolean isCorrect) {
        // -1 specifies an instance which is not yet persistent
        this.answerId = -1;
        this.type = type;
        this.answer = answer;
        this.isCorrect = isCorrect;
        this.q = null;

    }

    public Question getQuestion() {
        return q;
    }

    public void setQuestion(Question q) {
        this.q = q;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    @Override public int hashCode() {
        int result = answerId;
        result = 31 * result + type.hashCode();
        result = 31 * result + answer.hashCode();
        result = 31 * result + (isCorrect ? 1 : 0);
        result = 31 * result + (q != null ? q.hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "Answer{" +
            "answerId=" + answerId +
            ", type=" + type +
            ", answer='" + answer + '\'' +
            ", isCorrect=" + isCorrect +
            ", q=" + q +
            '}';

    }

    @Override
    public boolean equals(Object that){
        if (this == that) return true;
        if (!(that instanceof Answer)) return false;
        return ((Answer) that).getAnswerId() == this.getAnswerId();
    }
}
