package at.ac.tuwien.sepm.ss16.qse18;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
public class DummyEntityFactory {

    public static Subject createDummySubject() {
        Subject s = new Subject();
        s.setSubjectId(1);
        s.setName("TESTING");
        s.setEcts(1.0f);
        s.setSemester("WS10");
        return s;
    }

    public static Topic createDummyTopic() {
        Topic topic = new Topic();
        topic.setTopic("Topic");
        topic.setTopicId(1);
        topic.setNumberOfQuestions("2");
        return topic;
    }

    public static List<Topic> createDummyTopics() {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic());
        return topics;
    }


    public static List<Exam> createDummyExams() {
        List<Exam> exams = new ArrayList<>();
        exams.add(new Exam());
        return exams;
    }

}
