package at.ac.tuwien.sepm.ss16.qse18.domain;

/**
 * Created by Felix on 10.05.2016.
 */
public class Topic {
    private int topicId;
    private String topic;

    public Topic(int topicId, String topic) {
        this.topicId = topicId;
        this.topic = topic;
    }

    public Topic() {
        this.topicId = -1;
        this.topic = "";
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override public String toString() {
        return "Topic{" +
            "topicId=" + topicId +
            ", topic='" + topic + '\'' +
            '}';
    }
}

