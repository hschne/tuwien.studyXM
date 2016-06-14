package at.ac.tuwien.sepm.ss16.qse18.gui.observable;

import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A FXML Wrapper around the Topic DTO
 *
 * @author Philipp Ganiu
 */
public class ObservableTopic {
    private final IntegerProperty topicId;
    private final StringProperty topic;
    private Topic t;
    private StringProperty numberOfQuestions;

    public ObservableTopic(Topic t){
        this.t = t;
        this.topicId = new SimpleIntegerProperty(t.getTopicId());
        this.topic = new SimpleStringProperty(t.getTopic());
        this.numberOfQuestions = new SimpleStringProperty(t.getNumberOfQuestions());
    }

    public String getNumberOfQuestions() {
        return numberOfQuestions.get();
    }

    public StringProperty numberOfQuestionsProperty() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(String numberOfQuestions) {
        this.numberOfQuestions.set(numberOfQuestions);
    }

    public int getTopicid() {
        return topicId.get();
    }

    public IntegerProperty topicidProperty() {
        return topicId;
    }

    public void setTopicid(int topicid) {
        this.topicId.set(topicid);
    }

    public String getTopic() {
        return topic.get();
    }

    public StringProperty topicProperty() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic.set(topic);
    }

    public Topic getT() {
        return t;
    }

    public void setT(Topic t) {
        this.t = t;
    }

    public String toString(){
        return this.getTopic();
    }
}
