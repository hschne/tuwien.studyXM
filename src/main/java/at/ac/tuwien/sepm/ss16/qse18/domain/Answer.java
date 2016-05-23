package at.ac.tuwien.sepm.ss16.qse18.domain;

/**
 * Class Answer
 * represents the entity answer
 * @parameter answerId primary key of answer
 * @parameter type indicates type of answer
 * @parameter answerString content of answer(the actual answer to a question)
 * @parameter isCorrect indicates whether the answer is correct
 * @parameter q question which the answer belongs to
 *
 * @author Felix Almer on 06.05.2016.
 */
public class Answer {
    private int answerId;
    private QuestionType type;
    private String answerString;
    private boolean isCorrect;
    private Question q;

    public Answer(){/*this constructor is left empty on purpose*/}

    public Answer(int answerId, QuestionType type, String answer, boolean isCorrect, Question q) {
        this.answerId = answerId;
        this.type = type;
        this.answerString = answer;
        this.isCorrect = isCorrect;
        this.q = q;
    }

    public Answer(QuestionType type, String answer, boolean isCorrect) {
        // -1 specifies an instance which is not yet persistent
        this.answerId = -1;
        this.type = type;
        this.answerString = answer;
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
        return answerString;
    }

    public void setAnswer(String answer) {
        this.answerString = answer;
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
        result = 31 * result + answerString.hashCode();
        result = 31 * result + (isCorrect ? 1 : 0);
        result = 31 * result + (q != null ? q.hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "Answer{" +
            "answerId=" + answerId +
            ", type=" + type +
            ", answer='" + answerString + '\'' +
            ", isCorrect=" + isCorrect +
            ", q=" + q +
            '}';

    }

    @Override
    public boolean equals(Object that){
        if (this == that)
            return true;
        if (!(that instanceof Answer))
            return false;
        return ((Answer) that).getAnswerId() == this.getAnswerId();
    }
}
