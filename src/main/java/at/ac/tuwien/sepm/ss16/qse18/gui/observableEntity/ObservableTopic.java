package at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity;

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
    private final IntegerProperty topicid;
    private final StringProperty topic;
    private Topic t;

    public ObservableTopic(Topic t){
        this.t = t;
        topicid = new SimpleIntegerProperty(t.getTopicId());
        topic = new SimpleStringProperty(t.getTopic());
    }

    public int getTopicid() {
        return topicid.get();
    }

    public IntegerProperty topicidProperty() {
        return topicid;
    }

    public void setTopicid(int topicid) {
        this.topicid.set(topicid);
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
}
