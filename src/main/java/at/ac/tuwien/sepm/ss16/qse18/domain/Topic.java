package at.ac.tuwien.sepm.ss16.qse18.domain;

import java.io.Serializable;

/**
 * Class Topic
 * represents the entity topicString
 * @parameter topicId primary key of topicString
 * @parameter topicString name of the topicString
 * @parameter numberOfQuestions questions that belongs to the topicString
 *
 * @author Felix Almer on 10.05.2016.
 */
public class Topic implements Serializable {
    private transient int topicId;
    private String topicString;
    private String numberOfQuestions = "questions: 0";

    public Topic(int topicId, String topicString) {
        this.topicId = topicId;
        this.topicString = topicString;
    }

    public Topic() {
        this.topicId = -1;
        this.topicString = "";
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
        return topicString;
    }

    public void setTopic(String topic) {
        this.topicString = topic;
    }

    @Override public String toString() {
        return "Topic{" +
            "topicId=" + topicId +
            ", topicString='" + topicString + '\'' +
            '}';
    }
}

