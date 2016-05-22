package at.ac.tuwien.sepm.ss16.qse18.domain;

public class Question {
    private int questionId;
    private String question;
    private QuestionType type;
    private long questionTime;

    public Question() {
        //Only needed for unit testing!
    }

    public Question(int questionId, String question, QuestionType type, long questionTime) {
        this.questionId = questionId;
        this.question = question;
        this.type = type;
        this.questionTime = questionTime;
    }

    public Question(String question, QuestionType type, long questionTime) {
        // -1 specifies a question which is not yet persistent
        this.questionId = -1;
        this.question = question;
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
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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
            ", question='" + question + '\'' +
            ", type=" + type +
            ", questionTime=" + questionTime +
            '}';
    }

    @Override public int hashCode() {
        int result = questionId;
        result = 31 * result + (question != null ? question.hashCode() : 0);
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
        if (question != null ? !question.equals(question1.question) : question1.question != null)
            return false;
        return type == question1.type;

    }
}
