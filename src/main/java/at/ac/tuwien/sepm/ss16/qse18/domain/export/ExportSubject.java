package at.ac.tuwien.sepm.ss16.qse18.domain.export;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Felix on 14.06.2016.
 */
public class ExportSubject implements Serializable {
    private Subject subject;
    private List<ExportTopic> topics;

    public ExportSubject(Subject subject, List<ExportTopic> topics) {
        this.subject = subject;
        this.topics = topics;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<ExportTopic> getTopics() {
        return topics;
    }

    public void setTopics(List<ExportTopic> topics) {
        this.topics = topics;
    }
}
