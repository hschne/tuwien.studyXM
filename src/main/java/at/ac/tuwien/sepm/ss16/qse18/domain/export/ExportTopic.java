package at.ac.tuwien.sepm.ss16.qse18.domain.export;

import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Felix on 14.06.2016.
 */
public class ExportTopic implements Serializable {
    private Topic topic;
    private List<ExportQuestion> questions;

    public ExportTopic(Topic topic, List<ExportQuestion> questions) {
        this.topic = topic;
        this.questions = questions;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public List<ExportQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ExportQuestion> questions) {
        this.questions = questions;
    }
}
