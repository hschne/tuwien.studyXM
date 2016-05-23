package at.ac.tuwien.sepm.ss16.qse18.gui.observable;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import javafx.beans.property.*;

/**
 * Created by Felix on 10.05.2016.
 */
public class ObservableAnswer {
    private IntegerProperty type;
    private StringProperty answer;
    private BooleanProperty correct;
    private Answer a;

    public ObservableAnswer(Answer a) {
        this.a = a;
        this.type = new SimpleIntegerProperty(a.getType().getValue());
        this.answer = new SimpleStringProperty(a.getAnswer());
        this.correct = new SimpleBooleanProperty(a.isCorrect());
    }

    public Answer getAnswerInstance() {
        return this.a;
    }

    public IntegerProperty typeProperty() {
        return this.type;
    }

    public int getType() {
        return this.type.get();
    }

    public void setType(QuestionType type) {
        this.a.setType(type);
        this.type.set(type.getValue());
    }

    public StringProperty answerProperty() {
        return this.answer;
    }

    public String getAnswer() {
        return this.answer.get();
    }

    public void setAnswer(String answer) {
        this.a.setAnswer(answer);
        this.answer.set(answer);
    }

    public BooleanProperty correctProperty() {
        return this.correct;
    }

    public boolean isCorrect() {
        return this.correct.get();
    }

    public void setCorrect(boolean correct) {
        this.a.setCorrect(correct);
        this.correct.set(correct);
    }

    @Override
    public String toString() {
        return a.toString();
    }
}
