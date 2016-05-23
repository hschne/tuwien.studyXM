package at.ac.tuwien.sepm.ss16.qse18.domain;

/**
 * Class Topic
 * represents the entity topic
 * @parameter topicId primary key of topic
 * @parameter topic name of the topic
 * @parameter numberOfQuestions questions that belongs to the topic
 *
 * @author Felix Almer on 10.05.2016.
 */
public class Topic {
    private int topicId;
    private String topic;
    private String numberOfQuestions = "questions: 0";

    public Topic(int topicId, String topic) {
        this.topicId = topicId;
        this.topic = topic;
    }

    public Topic() {
        this.topicId = -1;
        this.topic = "";
    }

    public String getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(String numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
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

