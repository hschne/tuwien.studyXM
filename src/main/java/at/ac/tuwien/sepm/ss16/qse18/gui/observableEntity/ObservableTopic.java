package at.ac.tuwien.sepm.ss16.qse18.gui.observableEntity;

import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ObservableTopic {

    private final StringProperty name;
    private Topic topic;

    public ObservableTopic(Topic topic) {
        this.topic = topic;
        name = new SimpleStringProperty(topic.getTopic());
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
        topic.setTopic(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Topic getTopic() {
        return topic;
    }
}
