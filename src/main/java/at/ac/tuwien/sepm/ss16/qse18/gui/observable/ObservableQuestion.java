package at.ac.tuwien.sepm.ss16.qse18.gui.observable;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import javafx.beans.property.*;

/**
 * Created by Felix on 10.05.2016.
 */
public class ObservableQuestion {
    private StringProperty question;
    private IntegerProperty type;
    private LongProperty questionTime;
    private Question q;

    public ObservableQuestion(Question q) {
        this.q = q;
        this.question = new SimpleStringProperty(q.getQuestion());
        this.type = new SimpleIntegerProperty(q.getType().getValue());
        this.questionTime = new SimpleLongProperty(q.getQuestionTime());
    }

    public Question getQuestionInstance() {
        return this.q;
    }

    public StringProperty questionProperty() {
        return this.question;
    }

    public String getQuestion() {
        return this.question.get();
    }

    public void setQuestion(String question) {
        this.q.setQuestion(question);
        this.question.set(question);
    }

    public IntegerProperty typeProperty() {
        return this.type;
    }

    public int getType() {
        return this.type.get();
    }

    public void setType(QuestionType qt) {
        this.q.setType(qt);
        this.type.set(qt.getValue());
    }

    public long getQuestionTime() {
        return this.questionTime.get();
    }

    public void setQuestionTime(long time) {
        this.q.setQuestionTime(time);
        this.questionTime.set(time);
    }

    public LongProperty questionTimeProperty() {
        return this.questionTime;
    }

}
