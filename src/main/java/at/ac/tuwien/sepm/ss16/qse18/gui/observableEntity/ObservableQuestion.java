package at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Felix on 10.05.2016.
 */
public class ObservableQuestion {
    private StringProperty question;
    private IntegerProperty type;
    private Question q;

    public ObservableQuestion(Question q) {
        this.q = q;
        this.question = new SimpleStringProperty(q.getQuestion());
        this.type = new SimpleIntegerProperty(q.getType().getValue());
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

}
