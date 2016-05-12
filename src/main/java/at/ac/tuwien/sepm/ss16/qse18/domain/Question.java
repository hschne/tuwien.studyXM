package at.ac.tuwien.sepm.ss16.qse18.domain;

public class Question {
    private int questionId;
    private String question;
    private QuestionType type;

    public Question() {

    }

    public Question(int questionId, String question, QuestionType type) {
        this.questionId = questionId;
        this.question = question;
        this.type = type;
    }

    public Question(String question, QuestionType type) {
        // -1 specifies a question which is not yet persistent
        this.questionId = -1;
        this.question = question;
        this.type = type;
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

    @Override
    public String toString() {
        return "Question{" +
            "questionId=" + questionId +
            ", question='" + question + '\'' +
            ", type=" + type +
            '}';
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
